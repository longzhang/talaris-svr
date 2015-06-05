package me.ele.talaris.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 	Java的DateFormat比较怪。
 *  经测试，format和parse的格式控制不一样。
 *  format的格式设置如果是yy，只返回年份后两位，设置为yyyy就是四位。
 *  月份的配置项M和m的使用也有类似问题。
 *  遂使用此工具类统一格式转换问题。
 *  
 *  TODO: 这个类还要再经过一些测试来检验。
 *  
 *  
 */
public class DateFormat {
	private SimpleDateFormat formatSDF = new SimpleDateFormat("yyyy年M月d日");
	
	private SimpleDateFormat parseSDF = new SimpleDateFormat("yy年M月d日");
	
	
	public DateFormat() {
		
	}
	
	public String format(Date date) {
		return formatSDF.format(date);
	}
	
	public Date parse(String s) throws ParseException
	{
		return parseSDF.parse(s);
	}
}
