/**
 * 
 */
package me.ele.talaris.model;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author chaoguodeng
 *
 */
@Table(name = "ele_order_detail")
@Human(label = "饿单详情")
public class EleOrderDetail {
    @Column(pk = true, auto_increase = true)
    @Human(label = "饿单详情id")
    private long id;

    @Column
    @Human(label = "饿单id")
    private long ele_order_id;

    @Column
    @Human(label = "饿单详情")
    private String ele_order_detail;


    public long getEle_order_id() {
		return ele_order_id;
	}

	public void setEle_order_id(long ele_order_id) {
		this.ele_order_id = ele_order_id;
	}

	public void setEle_order_id(int ele_order_id) {
        this.ele_order_id = ele_order_id;
    }

    public String getEle_order_detail() {
        return ele_order_detail;
    }

    public void setEle_order_detail(String ele_order_detail) {
        this.ele_order_detail = ele_order_detail;
    }

    public EleOrderDetail(long id, int ele_order_id, String ele_order_detail) {
        this.id = id;
        this.ele_order_id = ele_order_id;
        this.ele_order_detail = ele_order_detail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EleOrderDetail() {
        super();
    }

}
