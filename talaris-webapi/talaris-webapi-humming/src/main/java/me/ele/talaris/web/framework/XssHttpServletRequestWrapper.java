package me.ele.talaris.web.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String s = StringEscapeUtils.escapeSql(name);
        return StringEscapeUtils.escapeHtml(super.getHeader(s));
    }

    @Override
    public String getQueryString() {
        String s = StringEscapeUtils.escapeSql(super.getQueryString());
        return StringEscapeUtils.escapeHtml(s);
    }

    @Override
    public String getParameter(String name) {
        String s = StringEscapeUtils.escapeSql(name);
        return StringEscapeUtils.escapeHtml(s);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                String s = StringEscapeUtils.escapeSql(values[i]);
                escapseValues[i] = StringEscapeUtils.escapeHtml(s);
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

}
