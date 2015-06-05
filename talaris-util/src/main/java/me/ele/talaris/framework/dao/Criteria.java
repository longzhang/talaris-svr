package me.ele.talaris.framework.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.ele.talaris.utils.Utils;
import me.ele.talaris.utils.Utils.Transformer;

public class Criteria implements ICriteriaElement {

    public static class Condition implements ICriteriaElement {
        private String clause;
        private List<Object> arguments;

        public String getClause() {
            return clause;
        }

        public void setClause(String clause) {
            this.clause = clause;
        }

        public List<Object> getArguments() {
            return arguments;
        }

        public void setArguments(List<Object> arguments) {
            this.arguments = arguments;
        }

        public Condition(String clause, List<Object> arguments) {
            super();
            this.clause = clause;
            this.arguments = arguments;
        }

        public List<Object> getParameterList() {
            return this.getArguments();
        }
    }

    private String logic;
    private List<ICriteriaElement> children;

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public List<ICriteriaElement> getChildren() {
        return children;
    }

    public void setChildren(List<ICriteriaElement> children) {
        this.children = children;
    }

    public Criteria() {
        this.logic = "and";
        this.children = new ArrayList<ICriteriaElement>();
    }

    public String getClause() {
        List<String> sqlModules = Utils.transform(this.children, new Transformer<ICriteriaElement, String>() {
            public String transform(ICriteriaElement e) {
                if (e instanceof Criteria) {
                    return "(" + e.getClause() + ")";
                }
                return e.getClause();
            }
        });
        StringBuffer sb = new StringBuffer();
        int size = sqlModules.size();
        for (int i = 0; i < size; i++) {
            if (i != size - 1) {
                sb.append(sqlModules.get(i));
                sb.append(" " + this.logic + " ");
            } else {
                sb.append(sqlModules.get(i));
            }
        }
        return sb.toString();
        // jdk版本问题。jdk8才支持join
        // return String.join(" " + this.logic + " ",
        // Utils.transform(this.children, new Transformer<ICriteriaElement, String>() {
        // public String transform(ICriteriaElement e) {
        // if (e instanceof Criteria) {
        // return "(" + e.getClause() + ")";
        // }
        // return e.getClause();
        // }
        // }));

    }

    public List<Object> getParameterList() {
        List<Object> params = new ArrayList<Object>();
        for (ICriteriaElement ce : this.children) {
            params.addAll(ce.getParameterList());
        }
        return params;
    }

    public Criteria and(String clause, Object... args) {
        this.children.add(new Condition(clause, Arrays.asList(args)));
        return this;
    }

}
