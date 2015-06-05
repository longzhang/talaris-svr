/**
 * 
 */
package me.ele.talaris.model;

import java.math.BigDecimal;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author chaoguodeng
 *
 */
@Table(name = "city")
@Human(label = "城市")
public class City {
	@Column(pk = true, auto_increase = true)
	@Human(label = "城市id")
	private int id;

	@Column
	@Human(label = "城市名称")
	private String name;

	@Column
	@Human(label = "代码")
	private int district_code;

	@Column
	@Human
	private String abbr;

	@Column
	@Human
	private String hint;

	@Column
	@Human
	private String area_code;

	@Column
	@Human(label = "公司名称")
	private String company_name;

	@Column
	@Human(label = "公司地址")
	private String company_address;
	
	@Column
	@Human
	private int sort;
	
	@Column
	@Human
	private String notice_emails;
	
	@Column
	@Human
	private int is_valid;
	
	@Column
	@Human
	private String boundary;
	
	@Column
	@Human
	private BigDecimal latitude;
	
	@Column
	@Human
	private BigDecimal longitude;
	
	@Column
	@Human
	private String company_abbr;
	
	@Column
	@Human
	private String country_region;
	
	@Column
	@Human
	private int country_region_id;
	
	@Column
	@Human
	private int is_map;
	
	@Column
	@Human
	private String pinyin;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDistrict_code() {
		return district_code;
	}

	public void setDistrict_code(int district_code) {
		this.district_code = district_code;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCompany_address() {
		return company_address;
	}

	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getNotice_emails() {
		return notice_emails;
	}

	public void setNotice_emails(String notice_emails) {
		this.notice_emails = notice_emails;
	}

	public int getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(int is_valid) {
		this.is_valid = is_valid;
	}

	public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public String getCompany_abbr() {
		return company_abbr;
	}

	public void setCompany_abbr(String company_abbr) {
		this.company_abbr = company_abbr;
	}

	public String getCountry_region() {
		return country_region;
	}

	public void setCountry_region(String country_region) {
		this.country_region = country_region;
	}

	public int getCountry_region_id() {
		return country_region_id;
	}

	public void setCountry_region_id(int country_region_id) {
		this.country_region_id = country_region_id;
	}

	public int getIs_map() {
		return is_map;
	}

	public void setIs_map(int is_map) {
		this.is_map = is_map;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public City(int id, String name, int district_code, String abbr,
			String hint, String area_code, String company_name,
			String company_address, int sort, String notice_emails,
			int is_valid, String boundary, BigDecimal latitude,
			BigDecimal longitude, String company_abbr, String country_region,
			int country_region_id, int is_map, String pinyin) {
		this.id = id;
		this.name = name;
		this.district_code = district_code;
		this.abbr = abbr;
		this.hint = hint;
		this.area_code = area_code;
		this.company_name = company_name;
		this.company_address = company_address;
		this.sort = sort;
		this.notice_emails = notice_emails;
		this.is_valid = is_valid;
		this.boundary = boundary;
		this.latitude = latitude;
		this.longitude = longitude;
		this.company_abbr = company_abbr;
		this.country_region = country_region;
		this.country_region_id = country_region_id;
		this.is_map = is_map;
		this.pinyin = pinyin;
	}

	public City(String name, int district_code, String abbr, String hint,
			String area_code, String company_name, String company_address,
			int sort, String notice_emails, int is_valid, String boundary,
			BigDecimal latitude, BigDecimal longitude, String company_abbr,
			String country_region, int country_region_id, int is_map,
			String pinyin) {
		this.name = name;
		this.district_code = district_code;
		this.abbr = abbr;
		this.hint = hint;
		this.area_code = area_code;
		this.company_name = company_name;
		this.company_address = company_address;
		this.sort = sort;
		this.notice_emails = notice_emails;
		this.is_valid = is_valid;
		this.boundary = boundary;
		this.latitude = latitude;
		this.longitude = longitude;
		this.company_abbr = company_abbr;
		this.country_region = country_region;
		this.country_region_id = country_region_id;
		this.is_map = is_map;
		this.pinyin = pinyin;
	}

	public City() {
		super();
	}

}
