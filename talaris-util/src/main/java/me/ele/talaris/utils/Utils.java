package me.ele.talaris.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个工具类用来方便处理一些基础数据、基础数据结构的常见的判断和转换。
 * 比如String相关的有isEmpty、killNull、isEmptyWithTrim函数。 目前还包含List的一些变换函数。 这个类还在不断完善中。
 * 
 * 
 * 这个类的编写原则就是：只依赖于Java基础库、以及sge自己的基础库。
 * 
 * 
 * 
 */
public class Utils {
    public static boolean isEmpty(String s) {
        if (s == null)
            return true;
        if ("".equals(s))
            return true;
        return false;
    }

    public static boolean isEmptyWithTrim(String s) {
        if (s == null)
            return true;
        if (s.trim().equals(""))
            return true;
        return false;
    }

    public static String killNull(String s) {
        if (s == null)
            return "";
        else
            return s;
    }

    public static String safeToString(Object obj) {
        if (obj == null)
            return "";
        return obj.toString();
    }

    public static boolean equals(String strA, String strB) {
        if (strA == null) {
            if (strB == null)
                return true;
            else
                return false;
        } else {
            return strA.equals(strB);
        }
    }

    public static int[] toArray(List<Integer> list) {
        if (list == null)
            return new int[0];
        int[] array = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static <T> List<T> toList(T[] array) {
        if (array == null)
            return Collections.emptyList();
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        return list;
    }

    /**
     * 转换器。配合Utils.transform函数，对列表内容进行变换。
     * 
     * @param <E>
     * 原对象
     * @param <T>
     * 转换后的对象
     */
    public static interface Transformer<E, T> {
        public T transform(E e);
    }

    /**
     * 对List的内容进行变换
     * 
     * @param elist
     * 包含类型为E的对象的列表
     * @param transformer
     * 转换器
     * @return 包含类型为T的对象的列表
     */
    public static <E, T> List<T> transform(Collection<E> elist, Transformer<E, T> transformer) {
        List<T> tlist = new ArrayList<T>();
        for (E e : elist) {
            tlist.add(transformer.transform(e));
        }
        return tlist;
    }

    public static String contentToString(List<String> list, String separator) {
        if (list == null)
            return "";
        if (list.size() == 0)
            return "";
        if (list.size() == 1)
            return list.get(0);
        StringBuilder sb = new StringBuilder();
        sb.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            sb.append(separator);
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    // /////////////////////////////////

    private static NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));

    public static final String comma(double number) {
        return formatter.format(number);
    }

    public static final String comma(long number) {
        return formatter.format(number);
    }

    public static final String comma(Object number) {
        return formatter.format(number);
    }

    public static String comma(BigDecimal number, int scale) {
        return formatter.format(number.setScale(scale, BigDecimal.ROUND_HALF_UP));
    }

    public static int compare(String a, String b) {
        return killNull(a).compareTo(killNull(b));
    }

    public static String prettyDouble(double number, int scale) {
        return new BigDecimal(number).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 匹配简体中文
     * 
     * @param str
     * @return
     */
    public static boolean isChineseCharacter(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[\\u4e00-\\u9fa5](\\s*[\\u4e00-\\u9fa5])*$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static String isValidate(String s) {
        char[] array = s.toCharArray();
        char[] result = new char[s.length()];
        int i = 0;
        for (char c : array) {
            if (isChineseCharacter(String.valueOf(c))) {
                result[i] = c;
                i++;
            }
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                result[i] = c;
                i++;
            }
            if (String.valueOf(c).equals(",") || String.valueOf(c).equals("，") || String.valueOf(c).equals(".")
                    || String.valueOf(c).equals("。")) {
                result[i] = c;
                i++;
            }

        }
        return String.valueOf(result);

    }

    //
    // public static String padRight(String str, int total_length) {
    // int length = str.length();
    // int bytes_length = str.getBytes().length;
    //
    // int cnchar_count = (bytes_length-length)/3;
    //
    // int current_output_length = cnchar_count + length;
    //
    // if (current_output_length >= total_length) {
    // return str;
    // }
    //
    // return String.format("%s%" + (total_length - current_output_length) +
    // "s", str, "");
    // }

    /**
     * 国家号码段分配如下 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 　　
     * 联通：130、131、132、152、155、156、185、186、176
     *
     * 电信：133、153、180、189、（1349卫通） 粗略验证，和前端保持一致
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        // /^1[3|4|5|7|8]\d{9}$/
        // ^[1]([3][0-9]{1}|59|58|88|89|76)[0-9]{8}$
        p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$"); // 验证手机号,
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

}
