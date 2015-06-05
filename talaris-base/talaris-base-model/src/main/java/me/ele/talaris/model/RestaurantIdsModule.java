package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

@Table(name = "channel_restaurant_module")
public class RestaurantIdsModule {
    @Column(pk = true, auto_increase = true)
    private long id;
    // 当前是到那一块了餐厅号了
    @Column
    private int current_module_id;
    // 一次取多少餐厅
    @Column
    private int rst_count_once;
    // 拉单时间
    @Column
    private Timestamp create_at;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrent_module_id() {
        return current_module_id;
    }

    public void setCurrent_module_id(int current_module_id) {
        this.current_module_id = current_module_id;
    }

    public int getRst_count_once() {
        return rst_count_once;
    }

    public void setRst_count_once(int rst_count_once) {
        this.rst_count_once = rst_count_once;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }

    public RestaurantIdsModule(long id, int current_module_id, int rst_count_once, Timestamp create_at) {
        super();
        this.id = id;
        this.current_module_id = current_module_id;
        this.rst_count_once = rst_count_once;
        this.create_at = create_at;
    }

    public RestaurantIdsModule() {
        super();
    }

}
