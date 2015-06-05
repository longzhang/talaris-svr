/**
 * 
 */
package me.ele.talaris.model;

import java.sql.Date;
import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author chaoguodeng
 *
 */
@Table(name = "statistic_function_log")
@Human(label = "功能统计日志")
public class StatisticFunctionLog {
	@Column(pk = true, auto_increase = true)
	@Human(label = "日志ID")
	private int id;
	
	@Column
	@Human(label = "访问地址")
	private String url;
	
	@Column
	@Human(label = "功能描述")
	private String description;
	
	@Column
	@Human(label = "统计数量")
	private int count;
	
	@Column
	@Human(label = "统计日期")
	private Date statistic_date;
	
	@Column
	@Human(label = "统计创建时间")
	private Timestamp created_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getStatistic_date() {
		return statistic_date;
	}

	public void setStatistic_date(Date statistic_date) {
		this.statistic_date = statistic_date;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public StatisticFunctionLog(int id, String url, String description,
			int count, Date statistic_date, Timestamp created_at) {
		this.id = id;
		this.url = url;
		this.description = description;
		this.count = count;
		this.statistic_date = statistic_date;
		this.created_at = created_at;
	}

	public StatisticFunctionLog() {
		super();
	}

}
