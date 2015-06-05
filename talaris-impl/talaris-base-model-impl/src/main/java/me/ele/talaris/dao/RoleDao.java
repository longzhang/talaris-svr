/**
 * 
 */
package me.ele.talaris.dao;

import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class RoleDao extends BaseSpringDao<Role> {
    private final static Logger logger = LoggerFactory.getLogger(RoleDao.class);

    public RoleDao() {
        super(new BeanPropertyRowMapper<Role>(Role.class));
    }

    /**
     * 增加权限信息
     * 
     * @param role
     * @return
     */
    public int addRole(Role role) {
        int result = insert(role);
        logger.debug("增加权限信息:" + result);
        return result;
    }

    /**
     * 
     * 通过roleName返回role
     * 
     * @param String: roleName
     * 
     * @return Object: Role
     */
    public Role getRoleByRoleName(String roleName) {
        return this.selectOneOrNull("where role_name =? and status=?", roleName, 1);
    }

    /**
     * 
     * 通过roleName返回roleId
     * 
     * @param String: roleName
     * 
     * @return Object: roleId
     */
    public int getRoleIdByRoleName(String roleName) {
        Role role = this.getRoleByRoleName(roleName);
        if (role == null) {
            return 0;
        }
        return role.getId();
    }

    /**
     * 逻辑删除权限
     * 
     * @param role
     * @return
     */
    public int deleteRole(Role role) {
        int result = this.jdbcTemplate.update("update role set status = 0 where id= ?", role.getId());
        logger.debug("修改权限信息:" + result);
        return result;
    }

    /**
     * 查询所有权限信息
     * 
     * @param Role
     * @return
     */
    public List<Role> queryAll(Role Role) {
        List<Role> result = this.select("order by Id");
        logger.debug("查询所有权限信息:" + result);
        return result;
    }

    /**
     * 查询权限信息(通过RoleId)
     * 
     * @param Role
     * @return
     */
    public Role getRoleByRoleId(int roleId) {
        Role result = this.selectOneOrNull("where id = ?", roleId);
        logger.debug("通过roleId查询权限信息:" + result);
        return result;
    }

}
