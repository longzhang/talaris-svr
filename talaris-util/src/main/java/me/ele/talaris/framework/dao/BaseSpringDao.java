package me.ele.talaris.framework.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ele.talaris.framework.dao.TableDescriptor.ColumnDescriptor;
import me.ele.talaris.utils.Generics;
import me.ele.talaris.utils.Pair;
import me.ele.talaris.utils.Utils;
import me.ele.talaris.utils.Utils.Transformer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@SuppressWarnings({ "rawtypes" })
public abstract class BaseSpringDao<E> {

    protected JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected TableDescriptor tableDescriptor;

    /**
     * Bean的类型
     */
    private Class clazz;

    protected RowMapper<E> rowMapper;

    public TableDescriptor getTableDescriptor() {
        return tableDescriptor;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getSelectAllColumnsClause() {
        return sql_selectAllColumnsClause;
    }

    public BaseSpringDao(RowMapper<E> rowMapper, TableDescriptor tableDescriptor) {
        clazz = Generics.returnedClass(this);
        this.tableDescriptor = tableDescriptor;

        this.rowMapper = rowMapper;

        initSQLs();

    }

    public BaseSpringDao(RowMapper<E> rowMapper) {
        clazz = Generics.returnedClass(this);
        tableDescriptor = BeanTableContext.getBeanTableDescriptor(clazz);
        if (tableDescriptor == null) {
            tableDescriptor = new TableDescriptor(clazz);
            // 同时被两个线程写，结果也不会有问题，所以不做synchronized控制。
            BeanTableContext.registerBeanTable(clazz, tableDescriptor);
        }

        this.rowMapper = rowMapper;

        initSQLs();

    }

    protected String sql_allColumnString;

    protected String sql_pkColumnString;

    protected String sql_notPkColumnString;

    protected String sql_selectAllColumnsClause;

    private String sql_insert;

    private String sql_insert_withoutPK;

    private String sql_updateWithPK;

    private String sql_deleteWithPK;

    private int[] allColumnTypeArray;

    private int[] notPkColumnTypeArray;

    private int[] pkColumnTypeArray;

    private String wherePK;

    private void initSQLs() {

        sql_allColumnString = columnsToString(tableDescriptor.getColumns());

        sql_pkColumnString = columnsToString(tableDescriptor.getPkColumns());

        sql_notPkColumnString = columnsToString(tableDescriptor.getNotPkColumns());

        // 最常用的select所有列的子句
        sql_selectAllColumnsClause = String.format("select %s from %s ", sql_allColumnString,
                tableDescriptor.getTableName());

        Transformer<ColumnDescriptor, String> paramTransformer = new Transformer<ColumnDescriptor, String>() {
            public String transform(ColumnDescriptor e) {
                return e.getColumnName() + "=?";
            }
        };
        List<String> questionMarks = new ArrayList<String>();
        int columnsCount = tableDescriptor.getColumns().size();
        for (int i = 0; i < columnsCount; i++) {
            questionMarks.add("?");
        }

        sql_insert = String.format("insert into %s(%s) values(%s)", tableDescriptor.getTableName(),
                sql_allColumnString, Utils.contentToString(questionMarks, ","));

        questionMarks = new ArrayList<String>();
        int notPkcolumnsCount = tableDescriptor.getNotPkColumns().size();
        for (int i = 0; i < notPkcolumnsCount; i++) {
            questionMarks.add("?");
        }
        sql_insert_withoutPK = String.format("insert into %s(%s) values(%s)", tableDescriptor.getTableName(),
                sql_notPkColumnString, Utils.contentToString(questionMarks, ","));

        List<String> notPKColumnsParams = Utils.transform(tableDescriptor.getNotPkColumns(), paramTransformer);

        String setNotPK = Utils.contentToString(notPKColumnsParams, ", ");

        wherePK = Utils.contentToString(Utils.transform(tableDescriptor.getPkColumns(), paramTransformer), " and ");

        allColumnTypeArray = new int[tableDescriptor.getColumns().size()];

        for (int i = 0; i < tableDescriptor.getColumns().size(); i++) {
            allColumnTypeArray[i] = tableDescriptor.getColumns().get(i).getType();
        }

        List<ColumnDescriptor> notPkColumns = tableDescriptor.getNotPkColumns();
        notPkColumnTypeArray = new int[notPkColumns.size()];

        for (int i = 0; i < notPkColumns.size(); i++) {
            notPkColumnTypeArray[i] = notPkColumns.get(i).getType();
        }

        pkColumnTypeArray = new int[tableDescriptor.getPkColumns().size()];
        for (int i = 0; i < tableDescriptor.getPkColumns().size(); i++) {
            pkColumnTypeArray[i] = tableDescriptor.getPkColumns().get(i).getType();
        }

        sql_updateWithPK = String
                .format("update %s set %s where %s", tableDescriptor.getTableName(), setNotPK, wherePK);

        sql_deleteWithPK = String.format("delete from %s where %s", tableDescriptor.getTableName(), wherePK);
    }

    protected String columnsToString(List<ColumnDescriptor> cs) {
        List<String> list = Utils.transform(cs, new Transformer<ColumnDescriptor, String>() {
            public String transform(ColumnDescriptor e) {
                return e.getColumnName();
            }
        });

        return Utils.contentToString(list, ",");
    }

    /**
     * 
     * @param where
     * 包含了where之后的所有子句
     * @param args
     * 可以是普通的绑定参数，也可以是org.springframework.jdbc.core.
     * SqlParameterValue类型的绑定参数
     * @return
     */
    public List<E> select(String where, Object... args) {
        return jdbcTemplate.query(sql_selectAllColumnsClause + Utils.killNull(where), args, rowMapper);
    }

    public E selectOneOrNull(String where, Object... args) {
        List<E> list = jdbcTemplate.query(sql_selectAllColumnsClause + Utils.killNull(where), args, rowMapper);
        if (list.size() > 0)
            return list.get(0);
        else
            return null;
    }

    public long count(String where, Object... args) {
        return jdbcTemplate.queryForLong(
                String.format("select count(%s) from %s %s", this.sql_pkColumnString,
                        this.tableDescriptor.getTableName(), Utils.killNull(where)), args);
    }

    public List<E> query(Map<String, ?> params) {
        if (params.size() == 0)
            return this.select("");
        NamedParameterJdbcTemplate npJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);

        String whereClause = " where ";
        String[] keys = params.keySet().toArray(new String[params.size()]);
        for (int i = 0; i < keys.length; i++) {
            if (i != 0)
                whereClause += " and ";
            whereClause += String.format("%s=?", keys[i]);
        }

        String sql = String.format("select %s from %s %s", sql_allColumnString, tableDescriptor.getTableName(),
                whereClause);

        return npJdbcTemplate.query(sql, params, this.rowMapper);
    }

    /**
     * 
     * @param where
     * 包含了where之后的所有子句
     * @param fromIndex
     * 1-based index
     * @param selectSize
     * size want to select
     * @param args
     * 可以是普通的绑定参数，也可以是org.springframework.jdbc.core.
     * SqlParameterValue类型的绑定参数
     * @return
     */
    public List<E> selectLimit(String where, int fromIndex, int selectSize, Object... args) {
        String originalQuerySQL = String.format("select %s from %s %s", sql_allColumnString,
                this.tableDescriptor.getTableName(), where);

        return selectLimit(fromIndex, selectSize, originalQuerySQL, args);
    }

    private List<E> selectLimit(long fromIndex, long selectSize, String originalQuerySQL, Object... args) {
        String paginationSearchQuery = String.format("select %s from (%s) as SUM1 limit ? offset ?",
                sql_allColumnString, originalQuerySQL);

        List<Object> argsEx = Utils.toList(args);
        argsEx.add(selectSize);
        argsEx.add(fromIndex - 1); // convert to zero-based for mysql offset

        List<E> list = this.jdbcTemplate.query(paginationSearchQuery, argsEx.toArray(new Object[argsEx.size()]),
                rowMapper);
        return list;
    }

    /**
     * 
     * @param where
     * @param pageNum
     * 1-based index
     * @param PageSize
     * @param args
     * @return
     */
    public Pair<List<E>, Long> selectPage(String where, int pageNum, int PageSize, Object... args) {
        String originalQuerySQL = String.format("select %s from %s %s", sql_allColumnString,
                this.tableDescriptor.getTableName(), where);

        return selectPage(pageNum, PageSize, originalQuerySQL, args);
    }

    /**
     * 这个protected函数可以实现更多定制化的选择，原始SQL语句可以完整的指定。
     * 
     * @param pageNum
     * @param PageSize
     * @param originalQuerySQL
     * @param args
     * @return
     */
    protected Pair<List<E>, Long> selectPage(int pageNum, int PageSize, String originalQuerySQL, Object... args) {
        String countQuery = String.format("select count(*) from  (%s) as SUM", originalQuerySQL);

        long total = this.jdbcTemplate.queryForLong(countQuery, args);

        List<E> list = selectLimit((pageNum - 1) * PageSize + 1, PageSize, originalQuerySQL, args);

        return new Pair<List<E>, Long>(list, total);
    }

    /**
     * 因为使用了反射，所以性能较低，反射部分的代码比不使用反射性能低10倍。
     * 但Hibernate也用反射，如果排除掉cache的作用，这个方法的性能不会比hibernate低。
     * 
     * 不建议使用这个函数进行批量操作
     * 
     * @param e
     * @return
     */
    public int insert(E e) {
        if (!tableDescriptor.getPkColumns().get(0).isAutoIncrease()) {
            return jdbcTemplate.update(sql_insert, columnArray(e, tableDescriptor.getColumns()), allColumnTypeArray);
        }

        if (tableDescriptor.getPkColumns().size() != 1)
            throw new IllegalStateException("auto increase pk column should be one and only one");
        ColumnDescriptor single_pk = tableDescriptor.getPkColumns().get(0);
        if (single_pk.getType() != Types.INTEGER && single_pk.getType() != Types.BIGINT) {
            throw new IllegalStateException("auto increase pk column must be int or long");
        }

        SimpleJdbcInsert simpleInsert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(
                this.tableDescriptor.getTableName()).usingGeneratedKeyColumns(single_pk.getColumnName());

        Map<String, Object> params = new HashMap<String, Object>();
        for (ColumnDescriptor col : tableDescriptor.getNotPkColumns()) {
            try {
                params.put(col.getColumnName(), col.getReadMethod().invoke(e));
            } catch (Throwable t) {
                throw new IllegalStateException(t);
            }
        }
        try {
            final Number key = simpleInsert.executeAndReturnKey(params);
            if (single_pk.getType() == Types.INTEGER) {
                single_pk.getWriteMethod().invoke(e, key.intValue());
            } else if (single_pk.getType() == Types.BIGINT) {
                single_pk.getWriteMethod().invoke(e, key.longValue());
            }
        } catch (Throwable t) {
            throw new IllegalStateException(t);
        }

        return 1;
    }

    /**
     * 因为使用了反射，所以性能较低，反射部分的代码比不使用反射性能低10倍。
     * 但Hibernate也用反射，如果排除掉cache的作用，这个方法的性能不会比hibernate低。
     * 
     * 不建议使用这个函数进行批量操作
     * 
     * @param e
     * @return
     */
    public int update(E e) {
        return jdbcTemplate.update(sql_updateWithPK, columnArray(e, tableDescriptor.getColumns()), allColumnTypeArray);
    }

    public int delete(E e) {
        return jdbcTemplate.update(sql_deleteWithPK, columnArray(e, tableDescriptor.getPkColumns()), pkColumnTypeArray);
    }

    protected static <E> Object[] columnArray(E e, List<ColumnDescriptor> list) {
        Object[] objs = new Object[list.size()];

        for (int i = 0; i < objs.length; i++) {
            try {
                objs[i] = list.get(i).getReadMethod().invoke(e);
            } catch (Throwable t) {
                throw new IllegalStateException(t);
            }
        }
        return objs;
    }

    /**
     * 该方法如果有做批量写入，可能没考虑没有自增长主键的情况。目前只会被batchinsert deliveryOrders使用
     * 本来不想写在该地方，但是sql_insert 是private ，所以放在基础类里了
     * 使用时一定注意该方法：郑稳
     * 
     * @param es
     */
    public void batchInsert(List<E> es) {
        List<Object[]> args = new ArrayList<Object[]>();
        for (E e : es) {
            Object[] objects = new Object[20];
            int i = 0;
            for (ColumnDescriptor col : tableDescriptor.getNotPkColumns()) {
                try {
                    objects[i] = col.getReadMethod().invoke(e);
                    i++;
                } catch (Throwable t) {
                    throw new IllegalStateException(t);
                }
            }
            args.add(objects);
        }
        jdbcTemplate.batchUpdate(sql_insert_withoutPK, args);
    }
}
