/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 *
 * Defines a generic, protocol-independent
 * servlet. To write an HTTP servlet for use on the
 * Web, extend {@link javax.servlet.http.HttpServlet} instead.
 * 定义一个通用的、与协议无关的 servlet, 为了在Web上使用HTTP servlet，
 * 应该继承HttpServlet
 *
 * <p><code>GenericServlet</code> implements the <code>Servlet</code>
 * and <code>ServletConfig</code> interfaces. <code>GenericServlet</code>
 * may be directly extended by a servlet, although it's more common to extend
 * a protocol-specific subclass such as <code>HttpServlet</code>.
 * GenericServlet 实现了 Servlet 和 ServletConfig 接口。GenericServlet 可以
 * 直接被自定义 servlet 继承，尽管更常见的做法是继承一个特定协议的子类，
 * 比如 HttpServlet
 *
 * <p><code>GenericServlet</code> makes writing servlets
 * easier. It provides simple versions of the lifecycle methods 
 * <code>init</code> and <code>destroy</code> and of the methods 
 * in the <code>ServletConfig</code> interface. <code>GenericServlet</code>
 * also implements the <code>log</code> method, declared in the
 * <code>ServletContext</code> interface. 
 * GenericServlet 使编写 servlet 更容易。它提供了生命周期方法init()和
 * destroy()的简化版本，以及ServletConfig接口中的方法。GenericServlet
 * 还实现了ServletContext接口中声明的log()方法。
 * 
 * <p>To write a generic servlet, you need only
 * override the abstract <code>service</code> method. 
 * 要编写一个通用的servlet，只需要重写(override)抽象方法service()
 *
 *
 * @author 	Various
 */

 
public abstract class GenericServlet 
    implements Servlet, ServletConfig, java.io.Serializable
{
    private static final String LSTRING_FILE = "javax.servlet.LocalStrings";
    private static ResourceBundle lStrings =
        ResourceBundle.getBundle(LSTRING_FILE);

    private transient ServletConfig config;
    

    /**
     *
     * Does nothing. All of the servlet initialization
     * is done by one of the <code>init</code> methods.
     * 什么都不做。所有的servlet初始化由init()方法完成。
     *
     */
    public GenericServlet() { }
    
    
    /**
     * Called by the servlet container to indicate to a servlet that the
     * servlet is being taken out of service.  See {@link Servlet#destroy}.
     * 由Servlet容器调用，用于通知Servlet即将停止服务。
     * 
     */
    public void destroy() {
    }
    
    
    /**
     * Returns a <code>String</code> containing the value of the named
     * initialization parameter, or <code>null</code> if the parameter does
     * not exist.  See {@link ServletConfig#getInitParameter}.
     * 返回一个包含指定初始化参数值的字符串，如果该参数不存在，则返回null。
     *
     * <p>This method is supplied for convenience. It gets the 
     * value of the named parameter from the servlet's 
     * <code>ServletConfig</code> object.
     * 为方便起见，提供此方法。它从 Servlet 的 ServletConfig 对象中获取指定参数名的值。
     *
     * @param name 		a <code>String</code> specifying the name 
     *				of the initialization parameter
     * 指定初始化参数名的String
     *
     * @return String 		a <code>String</code> containing the value
     *				of the initialization parameter
     * 包含初始化参数值的String
     *
     */ 
    public String getInitParameter(String name) {
        ServletConfig sc = getServletConfig();
        if (sc == null) {
            throw new IllegalStateException(
                lStrings.getString("err.servlet_config_not_initialized"));
        }

        return sc.getInitParameter(name);
    }
    
    
   /**
    * Returns the names of the servlet's initialization parameters 
    * as an <code>Enumeration</code> of <code>String</code> objects,
    * or an empty <code>Enumeration</code> if the servlet has no
    * initialization parameters.  See {@link
    * ServletConfig#getInitParameterNames}.
    * 返回servlet的初始化参数名的String枚举，如果 servlet 没有初始化参数，则返回空枚举。
    *
    * <p>This method is supplied for convenience. It gets the 
    * parameter names from the servlet's <code>ServletConfig</code> object. 
    * 为方便起见，提供此方法。它从 Servlet 的 ServletConfig 对象中获取参数名的枚举。
    *
    * @return Enumeration 	an enumeration of <code>String</code>
    *				objects containing the names of 
    *				the servlet's initialization parameters
    * String枚举类型，包含servlet的所有初始化参数名
    */
    public Enumeration<String> getInitParameterNames() {
        ServletConfig sc = getServletConfig();
        if (sc == null) {
            throw new IllegalStateException(
                lStrings.getString("err.servlet_config_not_initialized"));
        }

        return sc.getInitParameterNames();
    }   
     

    /**
     * Returns this servlet's {@link ServletConfig} object.
     * 返回servlet的ServletConfig对象
     *
     * @return ServletConfig 	the <code>ServletConfig</code> object
     *				that initialized this servlet
     * 初始化该servlet的ServletConfig对象
     */    
    public ServletConfig getServletConfig() {
	return config;
    }
 
    
    /**
     * Returns a reference to the {@link ServletContext} in which this servlet
     * is running.  See {@link ServletConfig#getServletContext}.
     * 返回servlet正在使用的ServletContext引用
     *
     * <p>This method is supplied for convenience. It gets the 
     * context from the servlet's <code>ServletConfig</code> object.
     * 为方便起见，提供此方法。它从 Servlet 的 ServletConfig 对象中获取context。
     *
     * @return ServletContext 	the <code>ServletContext</code> object
     *				passed to this servlet by the <code>init</code>
     *				method
     *   通过init()方法传递给servlet的ServletContext对象
     */
    public ServletContext getServletContext() {
        ServletConfig sc = getServletConfig();
        if (sc == null) {
            throw new IllegalStateException(
                lStrings.getString("err.servlet_config_not_initialized"));
        }

        return sc.getServletContext();
    }


    /**
     * Returns information about the servlet, such as 
     * author, version, and copyright. 
     * By default, this method returns an empty string.  Override this method
     * to have it return a meaningful value.  See {@link
     * Servlet#getServletInfo}.
     * 返回servlet的信息，比如作者、版本、版权。默认返回空字符串，重写该方法
     * 以返回有意义的值。
     *
     * @return String 		information about this servlet, by default an
     * 				empty string 该servlet的信息，默认空字符串
     */    
    public String getServletInfo() {
	return "";
    }


    /**
     * Called by the servlet container to indicate to a servlet that the
     * servlet is being placed into service.  See {@link Servlet#init}.
     * 由 Servlet 容器调用，以通知 Servlet 将要开始服务。
     *
     * <p>This implementation stores the {@link ServletConfig}
     * object it receives from the servlet container for later use.
     * When overriding this form of the method, call 
     * <code>super.init(config)</code>.
     * 此方法保存了从 servlet 容器接收的 ServletConfig 对象，以供以后使用。
     * 重写此方法时，需要调用 super.init(config)
     *
     * @param config 			the <code>ServletConfig</code> object
     *					that contains configutation
     *					information for this servlet
     * ServletConfig对象包含该servlet的配置信息
     *
     * @exception ServletException 	if an exception occurs that
     *					interrupts the servlet's normal
     *					operation
     *  如果出现影响servlet正常操作的异常
     *
     * @see 				UnavailableException
     */
    public void init(ServletConfig config) throws ServletException {
	this.config = config;
	this.init();
    }


    /**
     * A convenience method which can be overridden so that there's no need
     * to call <code>super.init(config)</code>.
     * 可以重写的便捷方法，因此无需调用 super.init（config）
     *
     * <p>Instead of overriding {@link #init(ServletConfig)}, simply override
     * this method and it will be called by
     * <code>GenericServlet.init(ServletConfig config)</code>.
     * The <code>ServletConfig</code> object can still be retrieved via {@link
     * #getServletConfig}. 
     * 无需重写 init（ServletConfig），只需覆盖此方法，它就会被 
     * GenericServlet.init（ServletConfig config） 调用。
     * 仍可通过 getServletConfig() 获取 ServletConfig 对象
     *
     * @exception ServletException 	if an exception occurs that
     *					interrupts the servlet's
     *					normal operation
     * 如果出现影响servlet正常操作的异常
     */
    public void init() throws ServletException {

    }
    

    /**
     * Writes the specified message to a servlet log file, prepended by the
     * servlet's name.  See {@link ServletContext#log(String)}.
     * 将指定的消息写入 servlet 日志文档，servlet 名称在前面
     *
     * @param msg 	a <code>String</code> specifying
     *			the message to be written to the log file 要写入的一个字符串，指定要写入日志文档的消息
     */     
    public void log(String msg) {
	getServletContext().log(getServletName() + ": "+ msg);
    }
   
   
    /**
     * Writes an explanatory message and a stack trace
     * for a given <code>Throwable</code> exception
     * to the servlet log file, prepended by the servlet's name.
     * See {@link ServletContext#log(String, Throwable)}.
     * 将给定 Throwable 异常的解释性消息和堆栈跟踪写入 servlet 日志文档，servlet名称在前
     *
     *
     * @param message 		a <code>String</code> that describes
     *				the error or exception 描述错误或异常的 String
     *
     * @param t			the <code>java.lang.Throwable</code> error
     * 				or exception Throwable错误或异常
     */   
    public void log(String message, Throwable t) {
	getServletContext().log(getServletName() + ": " + message, t);
    }
    
    
    /**
     * Called by the servlet container to allow the servlet to respond to
     * a request.  See {@link Servlet#service}.
     * 由 Servlet 容器调用，以允许 Servlet 响应请求
     * 
     * <p>This method is declared abstract so subclasses, such as 
     * <code>HttpServlet</code>, must override it.
     * 该方法是抽象方法，子类必须重写该方法，比如HttpServlet
     *
     * @param req 	the <code>ServletRequest</code> object
     *			that contains the client's request
     * 包含客户端请求的ServletRequest对象
     *
     * @param res 	the <code>ServletResponse</code> object
     *			that will contain the servlet's response
     * 未来会包含servlet响应的ServletResponse对象
     *
     * @exception ServletException 	if an exception occurs that
     *					interferes with the servlet's
     *					normal operation occurred 如果出现影响servlet正常操作的异常
     *
     * @exception IOException 		if an input or output 如果出现输入或输出异常
     *					exception occurs
     */

    public abstract void service(ServletRequest req, ServletResponse res)
	throws ServletException, IOException;
    

    /**
     * Returns the name of this servlet instance. 返回servlet实例名
     * See {@link ServletConfig#getServletName}.
     *
     * @return          the name of this servlet instance servlet实例名
     */
    public String getServletName() {
        ServletConfig sc = getServletConfig();
        if (sc == null) {
            throw new IllegalStateException(
                lStrings.getString("err.servlet_config_not_initialized"));
        }

        return sc.getServletName();
    }
}
