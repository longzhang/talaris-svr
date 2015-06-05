package me.ele.talaris.web.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import me.ele.talaris.web.framework.compress.InitNeedCompressInterfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 全站压缩过滤器
public class WebAPIGzipFilter implements Filter {
    public Logger logger = LoggerFactory.getLogger(WebAPIGzipFilter.class);
    private static final String YES = "Y";
    private String needToCompress = "N";
    private String appointInterface = "N";

    public void destroy() {
        needToCompress = "N";

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (!YES.equals(needToCompress)) {
            chain.doFilter(request, response);
            return;
        }
        String uri = request.getRequestURI();
        if (YES.equals(appointInterface)) {
            // 不在需要压缩的接口上
            if (!InitNeedCompressInterfaces.needCompressInterfaces.contains(uri)) {
                chain.doFilter(request, response);
                return;
            }
        }

        GzipHttpServletResponse gresponse = new GzipHttpServletResponse(response);
        chain.doFilter(request, gresponse);// 放行
        // 压缩代码写在此处
        // 找一个内存缓冲字节流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把数据压缩到缓冲字节流流中
        GZIPOutputStream gout = new GZIPOutputStream(baos);
        // 取出数据：压缩后的
        byte b[] = gresponse.getOldBytes();// 原始字节
        logger.debug("原数据大小为：{}", b.length);
        gout.write(b);
        gout.close();// 保证所有的数据都进入内存缓存流
        // 取出压缩后的数据
        b = baos.toByteArray();
        logger.debug("压缩后数据大小为：{}", b.length);
        // 输出前一定要告知客户端压缩方式
        response.setHeader("Content-Encoding", "gzip");
        response.setContentLength(b.length);// 告知客户端正文的大小
        // 用服务器的响应对象输出

        ServletOutputStream out = response.getOutputStream();
        out.write(b);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.needToCompress = filterConfig.getInitParameter("needToCompress");
        this.appointInterface = filterConfig.getInitParameter("appointInterface");
    }

}

class GzipHttpServletResponse extends HttpServletResponseWrapper {
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private PrintWriter pw;

    public GzipHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    // 把原始数据封装到一个缓冲流中
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new MyServletOutputStream(baos);
    }

    // 字符流：把原始数据封装到一个缓冲流中
    @Override
    public PrintWriter getWriter() throws IOException {
        pw = new PrintWriter(new OutputStreamWriter(baos, super.getCharacterEncoding()));// 字符流转成字节流编码会丢失
        return pw;
    }

    // 返回baos中的缓存数据：原始
    public byte[] getOldBytes() {
        try {
            if (pw != null) {
                pw.close();
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

}

class MyServletOutputStream extends ServletOutputStream {
    private ByteArrayOutputStream baos;

    public MyServletOutputStream(ByteArrayOutputStream baos) {
        this.baos = baos;
    }

    @Override
    public void write(int b) throws IOException {
        baos.write(b);
    }
}
