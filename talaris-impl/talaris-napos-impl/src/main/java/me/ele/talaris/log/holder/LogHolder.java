package me.ele.talaris.log.holder;

/**
 * 把日志串在一起，一行输出
 * 
 * @author zhengwen
 *
 */
public class LogHolder {
    private static InheritableThreadLocal<StringBuffer> LOG = new InheritableThreadLocal<StringBuffer>();

    public static void bunchLog(String s) {
        StringBuffer sb = LOG.get();
        if (sb == null) {
            sb = new StringBuffer();
        }
        sb.append(s);
        LOG.set(sb);
    }

    public static StringBuffer getLog() {
        return LOG.get() == null ? new StringBuffer("") : LOG.get();
    }

    public static void clear() {
        LOG.set(null);
    }

}
