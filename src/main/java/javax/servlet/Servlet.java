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


/**
 * Defines methods that all servlets must implement.
 *
 * <p>A servlet is a small Java program that runs within a Web server.
 * Servlets receive and respond to requests from Web clients,
 * usually across HTTP, the HyperText Transfer Protocol. 
 *
 * <p>To implement this interface, you can write a generic servlet
 * that extends
 * <code>javax.servlet.GenericServlet</code> or an HTTP servlet that
 * extends <code>javax.servlet.http.HttpServlet</code>.
 *
 * <p>This interface defines methods to initialize a servlet,
 * to service requests, and to remove a servlet from the server.
 * These are known as life-cycle methods and are called in the
 * following sequence:
 * <ol>
 * <li>The servlet is constructed, then initialized with the <code>init</code> method.
 * <li>Any calls from clients to the <code>service</code> method are handled.
 * <li>The servlet is taken out of service, then destroyed with the 
 * <code>destroy</code> method, then garbage collected and finalized.
 * </ol>
 *
 * <p>In addition to the life-cycle methods, this interface
 * provides the <code>getServletConfig</code> method, which the servlet 
 * can use to get any startup information, and the <code>getServletInfo</code>
 * method, which allows the servlet to return basic information about itself,
 * such as author, version, and copyright.
 *
 * @author 	Various
 *
 * @see 	GenericServlet
 * @see 	javax.servlet.http.HttpServlet
 *
 */
 
/**
 * 定义所有 Servlet 必须实现的方法.
 *
 * Servlet 是在 Web Server 中运行的小型 Java 程序. Servlet 通常通过 HTTP（超文本传输协议）
 * 接收和响应 Web 客户端请求.
 *
 * 要实现 Servlet 接口，可以继承 javax.servlet.GenericServlet 或 javax.servlet.http.HttpServlet.
 *
 * 该接口定义了初始化 servlet、处理请求、从 server 移除 servlet 的方法. 这些方法被称为 Servlet
 * 生命周期方法，按以下顺序调用：
 * 1. servlet 实例化后，使用 init() 方法进行初始化.
 * 2. client 对 service()方法的调用会被处理.
 * 3. service() 方法结束后，使用 destroy() 方法销毁 servlet，最后进行 GC 和收尾工作.
 * 
 * 除生命周期方法外，该接口还提供了 getServletInfo() 方法，允许 servlet 返回有关自身的基本信息，如作者、版本和版权.
 */

public interface Servlet {

    /**
     * Called by the servlet container to indicate to a servlet that the 
     * servlet is being placed into service.
     *
     * <p>The servlet container calls the <code>init</code>
     * method exactly once after instantiating the servlet.
     * The <code>init</code> method must complete successfully
     * before the servlet can receive any requests.
     *
     * <p>The servlet container cannot place the servlet into service
     * if the <code>init</code> method
     * <ol>
     * <li>Throws a <code>ServletException</code>
     * <li>Does not return within a time period defined by the Web server
     * </ol>
     *
     *
     * @param config			a <code>ServletConfig</code> object 
     *					containing the servlet's
     * 					configuration and initialization parameters
     *
     * @exception ServletException 	if an exception has occurred that
     *					interferes with the servlet's normal
     *					operation
     *
     * @see 				UnavailableException
     * @see 				#getServletConfig
     *
     */

	/**
	 * 由 servlet 容器调用，指出即将使用的servlet.
	 * servlet 容器在实例化 servlet 后只会调用一次 init()方法.
	 * init() 方法必须成功完成，然后 servlet 才能接收请求.
	 * servlet 容器将不能使用 servlet 如果init()方法：
	 *　· 抛出 ServletException 异常
	 *  · 在 web server 指定的时间内没有 return
	 *
	 * 参数 config ，ServletConfig 对象，包含servlet配置信息和初始参数
	 */
    public void init(ServletConfig config) throws ServletException;
    
    

    /**
     *
     * Returns a {@link ServletConfig} object, which contains
     * initialization and startup parameters for this servlet.
     * The <code>ServletConfig</code> object returned is the one 
     * passed to the <code>init</code> method. 
     *
     * <p>Implementations of this interface are responsible for storing the 
     * <code>ServletConfig</code> object so that this 
     * method can return it. The {@link GenericServlet}
     * class, which implements this interface, already does this.
     *
     * @return		the <code>ServletConfig</code> object
     *			that initializes this servlet
     *
     * @see 		#init
     *
     * 返回一个 ServletConfig 对象，包含该servlet的初始化参数和启动参数.
     * 返回的 ServletConfig 对象是传递给 init() 方法的那一个.
     * 该接口(Servlet接口)的实现需要存储             ServletConfig 对象，以便 
     * getServletConfig() 方法可以返回该 ServletConfig 对象.
     * 实现了Servlet接口的GenericServlet类，已经做到了这点.
     * 返回值 初始化该 servlet 的 ServletConfig 对象
     *
     */

    public ServletConfig getServletConfig();
    
    

    /**
     * Called by the servlet container to allow the servlet to respond to 
     * a request.
     * 由 servlet 容器调用，允许 servlet 响应请求
     *
     * <p>This method is only called after the servlet's <code>init()</code>
     * method has completed successfully.
     * 只有在servlet的init()方法成功完成后，才会调用service()方法
     * 
     * <p>  The status code of the response always should be set for a servlet 
     * that throws or sends an error.
     * 对于抛出异常或发送错误的 servlet，应始终设置响应的状态码
     *
     * 
     * <p>Servlets typically run inside multithreaded servlet containers
     * that can handle multiple requests concurrently. Developers must 
     * be aware to synchronize access to any shared resources such as files,
     * network connections, and as well as the servlet's class and instance 
     * variables. 
     * Servlet 通常运行在多线程 Servlet 容器中，该容器可以并发处理多个请求。
     * 开发人员在访问任何共享资源时，必须注意进行同步操作，包括文件、网络连接
     * 以及 servlet 的类和实例变量。
     * More information on multithreaded programming in Java is available in 
     * <a href="http://java.sun.com/Series/Tutorial/java/threads/multithreaded.html">
     * the Java tutorial on multi-threaded programming</a>.
     *
     *
     * @param req 	the <code>ServletRequest</code> object that contains
     *			the client's request 包含客户端请求的ServletRequest对象
     *
     * @param res 	the <code>ServletResponse</code> object that contains
     *			the servlet's response 包含客户端响应的ServletResponse对象
     *
     * @exception ServletException 	if an exception occurs that interferes
     *					with the servlet's normal operation 
     * 如果出现影响servlet正常操作的异常
     *
     * @exception IOException 		if an input or output exception occurs
     * 如果出现输入或输出异常
     *
     */

    public void service(ServletRequest req, ServletResponse res)
	throws ServletException, IOException;
	
	

    /**
     * Returns information about the servlet, such
     * as author, version, and copyright.
     * 返回servlet相关信息，如作者、版本和版权。
     * 
     * <p>The string that this method returns should
     * be plain text and not markup of any kind (such as HTML, XML,
     * etc.).
     * 此方法返回的字符串应该是纯文本，而不是任何类型的标记文本(如HTML、XML等)。
     *
     * @return 		a <code>String</code> containing servlet information
     * 包含servlet信息的字符串
     *
     */

    public String getServletInfo();
    
    

    /**
     *
     * Called by the servlet container to indicate to a servlet that the
     * servlet is being taken out of service.  This method is
     * only called once all threads within the servlet's
     * <code>service</code> method have exited or after a timeout
     * period has passed. After the servlet container calls this 
     * method, it will not call the <code>service</code> method again
     * on this servlet.
     *
     * 由servlet容器调用，以指示servlet正在退出服务。此方法仅在servlet
     * service()方法中的所有线程都退出或超时之后才调用。servlet容器调用
     * 此方法之后，将不再调用该servlet的service()方法。

     * <p>This method gives the servlet an opportunity 
     * to clean up any resources that are being held (for example, memory,
     * file handles, threads) and make sure that any persistent state is
     * synchronized with the servlet's current state in memory.
     * 这种方法为Servlet提供了清理任何正在被持有的资源的机会（例如，内存、
     * 文件句柄、线程），并确保任何持久状态与Servlet在内存中的当前状态同步。
     *
     */

    public void destroy();
}
