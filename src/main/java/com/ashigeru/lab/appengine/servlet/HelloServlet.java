package com.ashigeru.lab.appengine.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日本語使える？
 * @author ashigeru
 */
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 5621334767921939712L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/plain");
        res.setCharacterEncoding("UTF-8");
        PrintWriter writer = res.getWriter();
        writer.println("Hello, world!");
    }
}
