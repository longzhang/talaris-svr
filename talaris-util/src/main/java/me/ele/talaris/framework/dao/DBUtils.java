package me.ele.talaris.framework.dao;

import java.util.List;

import me.ele.talaris.utils.Pair;
import me.ele.talaris.utils.Utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DBUtils {
    public static <E> Pair<List<E>, Long> selectPage(JdbcTemplate jdbcTemplate, final String originalSQL,
            final String countColumnString, int pageNum, int pageSize, Object[] args, RowMapper<E> rowMapper) {
        String sql = originalSQL.trim();
        if (!sql.toLowerCase().startsWith("select")) {
            throw new IllegalArgumentException("SQL is not start with [select]");
        }

        sql = sql.substring("select".length()).trim();

        int keywordFromIndex = sql.toLowerCase().indexOf(" from ");

        String columns = sql.substring(0, keywordFromIndex);

        String countQuery = String.format("select count(%s) from  (%s)", countColumnString, originalSQL);

        long total = jdbcTemplate.queryForLong(countQuery, args);

        String searchQuery_rn____ = String.format("select %s, rownum rn____ from (%s) ", columns, originalSQL);

        // 这个SQL参考了
        // http://www.oracle.com/technetwork/issue-archive/2006/06-sep/o56asktom-086197.html
        // 以及
        // http://stackoverflow.com/questions/11680364/oracle-faster-paging-query
        String paginationSearchQuery = "select " + columns + " from ( " + searchQuery_rn____
                + " ) where rn____ >= ? and rownum <= ?";

        List<Object> argsEx = Utils.toList(args);
        argsEx.add((pageNum - 1) * pageSize + 1);
        argsEx.add(pageSize);

        List<E> list = jdbcTemplate.query(paginationSearchQuery, argsEx.toArray(new Object[argsEx.size()]), rowMapper);

        return new Pair<List<E>, Long>(list, total);
    }

}
