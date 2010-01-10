/*
 * Copyright 2010 @ashigeru.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ashigeru.lab.appengine.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ashigeru.lab.appengine.action.Action;

/**
 * パスによってアクションを起動する。
 * @author ashigeru
 */
public class ActionServlet extends HttpServlet {

    private static final long serialVersionUID = 170269886436846759L;

    private static final Logger LOG = Logger.getLogger(ActionServlet.class.getName());

    private static final String ACTION_PREFIX;
    static {
        String className = Action.class.getName();
        ACTION_PREFIX = className.substring(0, className.lastIndexOf('.') + 1);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        req.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();
        LOG.info("PathInfo: " + path);
        String name = path.length() <= 1 ? "Default" : path.substring(1);
        Action action = load(name);
        if (action == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        else {
            action.run();
            res.setContentType("text/plain");
            res.setCharacterEncoding("UTF-8");
            action.flushTo(res.getWriter());
        }
    }

    private Action load(String name) {
        try {
            return Class.forName(
                ACTION_PREFIX + name,
                false,
                getClass().getClassLoader())
            .asSubclass(Action.class)
            .newInstance();
        }
        catch (Exception e) {
            LOG.log(Level.WARNING, ACTION_PREFIX + name + " is not found.");
            return null;
        }
    }
}
