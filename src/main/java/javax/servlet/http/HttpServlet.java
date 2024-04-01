/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

package javax.servlet.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.*;


/**
 *
 * Provides an abstract class to be subclassed to create
 * an HTTP servlet suitable for a Web site. A subclass of
 * <code>HttpServlet</code> must override at least 
 * one method, usually one of these:
 * 提供要子类化的抽象类，以创建适合Web站点的HTTP servlet。
 * HttpServlet的子类必须至少覆盖（override）以下方法中的一个：
 *
 * <ul>
 * <li> <code>doGet</code>, if the servlet supports HTTP GET requests
 * <li> <code>doPost</code>, for HTTP POST requests
 * <li> <code>doPut</code>, for HTTP PUT requests
 * <li> <code>doDelete</code>, for HTTP DELETE requests
 * <li> <code>init</code> and <code>destroy</code>, 
 * to manage resources that are held for the life of the servlet
 * <li> <code>getServletInfo</code>, which the servlet uses to
 * provide information about itself 
 * </ul>
 * doGet()方法，如果servlet支持HTTP GET请求
 * doPost()方法，如果servlet支持HTTP POST请求
 * doPut()方法，如果servlet支持HTTP PUT请求
 * doDelete()方法，如果servlet支持HTTP DELETE请求
 * init()方法、destroy()方法，管理servlet在生命周期内持有的资源
 * getServletInfo()方法，提供servlet自身信息
 *
 * <p>There's almost no reason to override the <code>service</code>
 * method. <code>service</code> handles standard HTTP
 * requests by dispatching them to the handler methods
 * for each HTTP request type (the <code>do</code><i>XXX</i>
 * methods listed above).
 * 理应重写service()方法。service()方法是这样处理标准HTTP请求的：
 * 根据每个HTTP请求的类型，把他们分发给与之对应的handler方法。
 *（以上列出的doXXX()方法）
 *
 * <p>Likewise, there's almost no reason to override the 
 * <code>doOptions</code> and <code>doTrace</code> methods.
 * 同样地，理应重写doOptions()方法和doTrace()方法。
 * 
 * <p>Servlets typically run on multithreaded servers,
 * so be aware that a servlet must handle concurrent
 * requests and be careful to synchronize access to shared resources.
 * Shared resources include in-memory data such as
 * instance or class variables and external objects
 * such as files, database connections, and network 
 * connections.
 * Servlet通常运行在多线程服务器上，因此请注意，servlet必须处理并发请求，
 * 并注意对共享资源的访问进行同步操作。共享资源包括内存数据（如实例或类变量）
 * 和外部对象（如文档、数据库连接和网络连接）。
 * See the
 * <a href="http://java.sun.com/Series/Tutorial/java/threads/multithreaded.html">
 * Java Tutorial on Multithreaded Programming</a> for more
 * information on handling multiple threads in a Java program.
 *
 * @author  Various
 */

public abstract class HttpServlet extends GenericServlet
{
    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_HEAD = "HEAD";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_TRACE = "TRACE";

    private static final String HEADER_IFMODSINCE = "If-Modified-Since";
    private static final String HEADER_LASTMOD = "Last-Modified";
    
    private static final String LSTRING_FILE =
        "javax.servlet.http.LocalStrings";
    private static ResourceBundle lStrings =
        ResourceBundle.getBundle(LSTRING_FILE);
   
    
    /**
     * Does nothing, because this is an abstract class.
     * 什幺都不做，因为这是一个抽象类。
     */

    public HttpServlet() { }
    

    /**
     *
     * Called by the server (via the <code>service</code> method) to
     * allow a servlet to handle a GET request. 
     * 由服务器调用（通过service()方法）以允许 Servlet 处理 GET 请求。
     *
     * <p>Overriding this method to support a GET request also
     * automatically supports an HTTP HEAD request. A HEAD
     * request is a GET request that returns no body in the
     * response, only the request header fields.
     * 重写此方法以支持GET方法，HEAD方法也会自动支持。
     * HEAD 请求是 GET 请求，在响应中不返回正文，只返回请求标头字段。
     *
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or 
     * output stream object, and finally, write the response data.
     * It's best to include content type and encoding. When using
     * a <code>PrintWriter</code> object to return the response,
     * set the content type before accessing the
     * <code>PrintWriter</code> object.
     * 重写此方法时，读取请求数据，写入响应标头，获取响应的写入器
     * 或输出流对象，最后写入响应数据。最好包括内容类型和编码。
     * 使用 PrintWriter 对象返回响应时，请在访问 PrintWriter 对象
     * 之前设置内容类型
     *
     * <p>The servlet container must write the headers before
     * committing the response, because in HTTP the headers must be sent
     * before the response body.
     * Servlet 容器必须在提交响应之前写入标头，因为在 HTTP 中，标头必须在响应正文之前发送。
     *
     * <p>Where possible, set the Content-Length header (with the
     * {@link javax.servlet.ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection 
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.
     * 在可能的情况下，使用 setContentLength 方法设置 Content-Length 标头，
     * 以允许 servlet 容器使用持久连接将其响应返回给客户端，从而提高性能。
     * 如果整个响应适合响应缓冲区，则会自动设置内容长度。
     *
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header.
     * 使用 HTTP 1.1 分块编码（这意味着响应具有 Transfer-Encoding 标头）时，
     * 请勿设置 Content-Length 标头。
     *
     * <p>The GET method should be safe, that is, without
     * any side effects for which users are held responsible.
     * For example, most form queries have no side effects.
     * If a client request is intended to change stored data,
     * the request should use some other HTTP method.
     * GET方法应该是安全的，也就是说，没有任何由用户负责的副作用。
     * 例如，大多数表单查询没有副作用。如果客户端请求打算更改存储的数据，
     * 则该请求应该使用其他HTTP方法。
     *
     * <p>The GET method should also be idempotent, meaning
     * that it can be safely repeated. Sometimes making a
     * method safe also makes it idempotent. For example, 
     * repeating queries is both safe and idempotent, but
     * buying a product online or modifying data is neither
     * safe nor idempotent. 
     * GET方法也应该是幂等的，这意味着它可以安全地重复。
     * 有时使一个方法安全也会使它幂等。例如，重复查询既安全又幂等，
     * 但在线购买产品或修改数据既不安全也不幂等。
     *
     * <p>If the request is incorrectly formatted, <code>doGet</code>
     * returns an HTTP "Bad Request" message.
     * 如果请求格式不正确，doGet()返回HTTP消息"Bad Request"。
     * 
     * @param req   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     * 一个HttpServletRequest对象，它包含客户端对servlet发出的请求
     *
     * @param resp  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     * 一个HttpServletResponse对象，它包含servlet发送给客户端的响应
     * 
     * @exception IOException   if an input or output error is 
     *                              detected when the servlet handles
     *                              the GET request
     * 如果在servlet处理GET请求时检测到输入或输出错误
     *
     * @exception ServletException  if the request for the GET
     *                                  could not be handled
     * 如果无法处理GET请求
     * @see javax.servlet.ServletResponse#setContentType
     */

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String protocol = req.getProtocol();
        String msg = lStrings.getString("http.method_get_not_supported");
        if (protocol.endsWith("1.1")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
    }


    /**
     *
     * Returns the time the <code>HttpServletRequest</code>
     * object was last modified,
     * in milliseconds since midnight January 1, 1970 GMT.
     * If the time is unknown, this method returns a negative
     * number (the default).
     * 返回HttpServletRequest对象最后一次修改的时间，以毫秒为单位，
     * 从1970 GMT时间1月1日午夜开始。
     * 如果时间未知，此方法将返回一个负数(默认值)。
     *
     * <p>Servlets that support HTTP GET requests and can quickly determine
     * their last modification time should override this method.
     * This makes browser and proxy caches work more effectively,
     * reducing the load on server and network resources.
     * 支持HTTP GET请求并能快速确定其最后修改时间的servlet应该覆盖此方法。
     * 这使得浏览器和代理缓存更有效地工作，减少了服务器和网络资源的负载。
     *
     * @param req   the <code>HttpServletRequest</code> 
     *                  object that is sent to the servlet
     *					发送给servlet的HttpServletRequest对象
     *
     * @return  a   <code>long</code> integer specifying
     *                  the time the <code>HttpServletRequest</code>
     *                  object was last modified, in milliseconds
     *                  since midnight, January 1, 1970 GMT, or
     *                  -1 if the time is not known
     * 一个长整数，指定HttpServletRequest对象最后一次修改的时间，
     * 单位为毫秒，从1970年1月1日午夜开始，如果时间未知，则为-1
     */

    protected long getLastModified(HttpServletRequest req) {
        return -1;
    }


    /**
     * 
     *
     * <p>Receives an HTTP HEAD request from the protected
     * <code>service</code> method and handles the
     * request.
     * The client sends a HEAD request when it wants
     * to see only the headers of a response, such as
     * Content-Type or Content-Length. The HTTP HEAD
     * method counts the output bytes in the response
     * to set the Content-Length header accurately.
     * 从service()方法接收HTTP HEAD请求并处理该请求.
	 * 当客户端只希望看到响应的报头(例如Content-Type或Content-Length)时，
	 * 它发送HEAD请求。HTTP HEAD方法对响应中的输出字节进行计数，
	 * 以便准确地设置Content-Length报头。
     *
     * <p>If you override this method, you can avoid computing
     * the response body and just set the response headers
     * directly to improve performance. Make sure that the
     * <code>doHead</code> method you write is both safe
     * and idempotent (that is, protects itself from being
     * called multiple times for one HTTP HEAD request).
     * 如果您重写此方法，则可以避免计算响应体，而只需直接设置响应头以提高性能。
     * 确保您编写的doHead方法既安全又幂等(也就是说，保护自己不因一个HTTP HEAD请求而被多次调用)。
     *
     * <p>If the HTTP HEAD request is incorrectly formatted,
     * <code>doHead</code> returns an HTTP "Bad Request"
     * message.
     * 如果HTTP HEAD请求格式不正确，doHead将返回HTTP消息"Bad Request"
     *
     * @param req   the request object that is passed to the servlet
     *				传递给servlet的请求对象
     *                        
     * @param resp  the response object that the servlet
     *                  uses to return the headers to the clien
     * 					Servlet 用于将标头返回给客户机的响应对象
     *
     * @exception IOException   if an input or output error occurs
     * 							如果出现输入或输出错误
     *
     * @exception ServletException  if the request for the HEAD
     *                                  could not be handled
     *									如果无法处理HEAD请求
     */
    protected void doHead(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        NoBodyResponse response = new NoBodyResponse(resp);
        
        doGet(req, response);
        response.setContentLength();
    }


    /**
     *
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a POST request.
     * 由服务器调用（通过service()方法）以允许 Servlet 处理 POST 请求。
     *
     * The HTTP POST method allows the client to send
     * data of unlimited length to the Web server a single time
     * and is useful when posting information such as
     * credit card numbers.
     * HTTP POST 方法允许客户端单次向 Web 服务器发送无限长度的数据，
     * 并且在发送信用卡号等信息时非常有用。
     *
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or output
     * stream object, and finally, write the response data. It's best 
     * to include content type and encoding. When using a
     * <code>PrintWriter</code> object to return the response, set the 
     * content type before accessing the <code>PrintWriter</code> object. 
     * 重写此方法时，读取请求数据，写入响应标头，获取响应的writer对象或
     * 输出流对象，最后写入响应数据。最好包括内容类型和编码。使用PrintWriter
     * 对象返回响应时，请在访问PrintWriter对象前设置内容类型。
     *
     * <p>The servlet container must write the headers before committing the
     * response, because in HTTP the headers must be sent before the 
     * response body.
     * Servlet 容器必须在提交响应之前写入标头，因为在 HTTP 中，标头必须在响应正文之前发送。
     *
     * <p>Where possible, set the Content-Length header (with the
     * {@link javax.servlet.ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection 
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.  
     * 在可能的情况下，使用 setContentLength 方法设置 Content-Length 标头，
     * 以允许 servlet 容器使用持久连接将其响应返回给客户端，从而提高性能。
     * 如果整个响应适合响应缓冲区，则会自动设置内容长度。
     *
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header. 
     * 使用 HTTP 1.1 分块编码（这意味着响应具有 Transfer-Encoding 标头）时，
     * 请勿设置 Content-Length 标头。
     * 
     * <p>This method does not need to be either safe or idempotent.
     * Operations requested through POST can have side effects for
     * which the user can be held accountable, for example, 
     * updating stored data or buying items online.
     * 此方法无需满足安全性或幂等性。通过 POST 请求的操作可能会产生副作用，
     * 用户可以对此负责，例如，更新存储的数据或在线购买物品。
     *
     * <p>If the HTTP POST request is incorrectly formatted,
     * <code>doPost</code> returns an HTTP "Bad Request" message.
     * 如果HTTP POST 请求格式不正确，doPost 将返回HTTP消息"Bad Request"
     *
     * @param req   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *				一个 HttpServletRequest 对象，该对象包含客户端对 Servlet 发出的请求
     *
     * @param resp  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *				一个 HttpServletResponse 对象，该对象包含 Servlet 发送到客户端的响应
     * 
     * @exception IOException   if an input or output error is 
     *                              detected when the servlet handles
     *                              the request
     * 如果在servlet处理POST请求时检测到输入或输出错误
     *
     * @exception ServletException  if the request for the POST
     *                                  could not be handled
     * 如果无法处理POST请求
     *
     * @see javax.servlet.ServletOutputStream
     * @see javax.servlet.ServletResponse#setContentType
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String protocol = req.getProtocol();
        String msg = lStrings.getString("http.method_post_not_supported");
        if (protocol.endsWith("1.1")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
    }


    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a PUT request.
     * 由服务器调用（通过service()方法）以允许 Servlet 处理 PUT 请求。
     *
     * The PUT operation allows a client to 
     * place a file on the server and is similar to 
     * sending a file by FTP.
     * PUT操作允许客户端向服务器发送文件，类似通过FTP发送文件
     *
     * <p>When overriding this method, leave intact
     * any content headers sent with the request (including
     * Content-Length, Content-Type, Content-Transfer-Encoding,
     * Content-Encoding, Content-Base, Content-Language, Content-Location,
     * Content-MD5, and Content-Range). If your method cannot
     * handle a content header, it must issue an error message
     * (HTTP 501 - Not Implemented) and discard the request.
     * For more information on HTTP 1.1, see RFC 2616
     * <a href="http://www.ietf.org/rfc/rfc2616.txt"></a>.
     * 重写此方法时，请保留随请求一起发送的任何内容标头不变。
     * 如果方法不能处理内容标头，必须发送错误信息(HTTP 501 - 未实现)，并且
     * 丢弃该请求
     *
     * <p>This method does not need to be either safe or idempotent.
     * Operations that <code>doPut</code> performs can have side
     * effects for which the user can be held accountable. When using
     * this method, it may be useful to save a copy of the
     * affected URL in temporary storage.
     * 此方法无需满足安全性或幂等性。doPut 执行的操作可能会产生副作用，
     * 用户可以对此负责。使用此方法时，将受影响的 URL 的副本保存在临时存储中
     * 可能很有用。
     *
     * <p>If the HTTP PUT request is incorrectly formatted,
     * <code>doPut</code> returns an HTTP "Bad Request" message.
     * 如果HTTP PUT 请求格式不正确，doPut 将返回HTTP消息"Bad Request"
     *
     * @param req   the {@link HttpServletRequest} object that
     *                  contains the request the client made of
     *                  the servlet
     * 一个 HttpServletRequest 对象，该对象包含客户端对 Servlet 发出的请求
     *
     * @param resp  the {@link HttpServletResponse} object that
     *                  contains the response the servlet returns
     *                  to the client
     * 一个 HttpServletResponse 对象，该对象包含 Servlet 发送到客户端的响应
     *
     * @exception IOException   if an input or output error occurs
     *                              while the servlet is handling the
     *                              PUT request
     * 如果在servlet处理PUT请求时检测到输入或输出错误
     *
     * @exception ServletException  if the request for the PUT
     *                                  cannot be handled
     * 如果无法处理PUT请求
     */
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String protocol = req.getProtocol();
        String msg = lStrings.getString("http.method_put_not_supported");
        if (protocol.endsWith("1.1")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
    }


    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a DELETE request.
     * 由服务器调用（通过service()方法）以允许 Servlet 处理 DELETE 请求。
     *
     * The DELETE operation allows a client to remove a document
     * or Web page from the server.
     * DELETE 操作允许客户端删除服务器上的文档或Web页面
     * 
     * <p>This method does not need to be either safe
     * or idempotent. Operations requested through
     * DELETE can have side effects for which users
     * can be held accountable. When using
     * this method, it may be useful to save a copy of the
     * affected URL in temporary storage.
     * 此方法无需满足安全性或幂等性。DELETE 请求的操作可能会产生副作用，
     * 用户可以对此负责。使用此方法时，将受影响的 URL 的副本保存在临时存储中
     * 可能很有用。
     *
     * <p>If the HTTP DELETE request is incorrectly formatted,
     * <code>doDelete</code> returns an HTTP "Bad Request"
     * message.
     * 如果HTTP DELETE 请求格式不正确，doDelete 将返回HTTP消息"Bad Request"
     *
     * @param req   the {@link HttpServletRequest} object that
     *                  contains the request the client made of
     *                  the servlet
     * HttpServletRequest 对象，该对象包含客户端对 Servlet 发出的请求
     *
     * @param resp  the {@link HttpServletResponse} object that
     *                  contains the response the servlet returns
     *                  to the client 
     * HttpServletResponse 对象，该对象包含 Servlet 发送到客户端的响应
     *
     * @exception IOException   if an input or output error occurs
     *                              while the servlet is handling the
     *                              DELETE request
     * 如果在servlet处理DELETE请求时检测到输入或输出错误
     *
     * @exception ServletException  if the request for the
     *                                  DELETE cannot be handled
     * 如果无法处理DELETE请求
     */
    protected void doDelete(HttpServletRequest req,
                            HttpServletResponse resp)
        throws ServletException, IOException
    {
        String protocol = req.getProtocol();
        String msg = lStrings.getString("http.method_delete_not_supported");
        if (protocol.endsWith("1.1")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
    }
    

    private Method[] getAllDeclaredMethods(Class<? extends HttpServlet> c) {

        Class<?> clazz = c;
        Method[] allMethods = null;

        while (!clazz.equals(HttpServlet.class)) {
            Method[] thisMethods = clazz.getDeclaredMethods();
            if (allMethods != null && allMethods.length > 0) {
                Method[] subClassMethods = allMethods;
                allMethods =
                    new Method[thisMethods.length + subClassMethods.length];
                System.arraycopy(thisMethods, 0, allMethods, 0,
                                 thisMethods.length);
                System.arraycopy(subClassMethods, 0, allMethods, thisMethods.length,
                                 subClassMethods.length);
            } else {
                allMethods = thisMethods;
            }

            clazz = clazz.getSuperclass();
        }

        return ((allMethods != null) ? allMethods : new Method[0]);
    }


    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a OPTIONS request.
     * 由服务器调用（通过service()方法）以允许 Servlet 处理 OPTIONS 请求。
     *
     * The OPTIONS request determines which HTTP methods 
     * the server supports and
     * returns an appropriate header. For example, if a servlet
     * overrides <code>doGet</code>, this method returns the
     * following header:
     *
     * <p><code>Allow: GET, HEAD, TRACE, OPTIONS</code>
     *
     * OPTIONS 请求确定服务端支持哪些HTTP方法，并返回对应的标头。例如，
     * 如果servlet重写了doGet()方法，那么该方法会返回以下标头：
     * Allow: GET, HEAD, TRACE, OPTIONS
     *
     * <p>There's no need to override this method unless the
     * servlet implements new HTTP methods, beyond those 
     * implemented by HTTP 1.1.
     * 除非servlet实现了HTTP1.1之外的新的HTTP方法，否则无需重写这个方法。
     *
     * @param req   the {@link HttpServletRequest} object that
     *                  contains the request the client made of
     *                  the servlet 
     * HttpServletRequest 对象，该对象包含客户端对 Servlet 发出的请求
     *
     * @param resp  the {@link HttpServletResponse} object that
     *                  contains the response the servlet returns
     *                  to the client  
     * HttpServletResponse 对象，该对象包含 Servlet 发送到客户端的响应
     *
     * @exception IOException   if an input or output error occurs
     *                              while the servlet is handling the
     *                              OPTIONS request
     * 如果在servlet处理 OPTIONS 请求时检测到输入或输出错误
     * 
     * @exception ServletException  if the request for the
     *                                  OPTIONS cannot be handled
     * 如果无法处理 OPTIONS 请求
     */
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        Method[] methods = getAllDeclaredMethods(this.getClass());
        
        boolean ALLOW_GET = false;
        boolean ALLOW_HEAD = false;
        boolean ALLOW_POST = false;
        boolean ALLOW_PUT = false;
        boolean ALLOW_DELETE = false;
        boolean ALLOW_TRACE = true;
        boolean ALLOW_OPTIONS = true;
        
        for (int i=0; i<methods.length; i++) {
            String methodName = methods[i].getName();
            
            if (methodName.equals("doGet")) {
                ALLOW_GET = true;
                ALLOW_HEAD = true;
            } else if (methodName.equals("doPost")) {
                ALLOW_POST = true;
            } else if (methodName.equals("doPut")) {
                ALLOW_PUT = true;
            } else if (methodName.equals("doDelete")) {
                ALLOW_DELETE = true;
            }
            
        }
        
        // we know "allow" is not null as ALLOW_OPTIONS = true
        // when this method is invoked
        StringBuilder allow = new StringBuilder();
        if (ALLOW_GET) {
            allow.append(METHOD_GET);
        }
        if (ALLOW_HEAD) {
            if (allow.length() > 0) {
                allow.append(", ");
            }
            allow.append(METHOD_HEAD);
        }
        if (ALLOW_POST) {
            if (allow.length() > 0) {
                allow.append(", ");
            }
            allow.append(METHOD_POST);
        }
        if (ALLOW_PUT) {
            if (allow.length() > 0) {
                allow.append(", ");
            }
            allow.append(METHOD_PUT);
        }
        if (ALLOW_DELETE) {
            if (allow.length() > 0) {
                allow.append(", ");
            }
            allow.append(METHOD_DELETE);
        }
        if (ALLOW_TRACE) {
            if (allow.length() > 0) {
                allow.append(", ");
            }
            allow.append(METHOD_TRACE);
        }
        if (ALLOW_OPTIONS) {
            if (allow.length() > 0) {
                allow.append(", ");
            }
            allow.append(METHOD_OPTIONS);
        }
        
        resp.setHeader("Allow", allow.toString());
    }
    
    
    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a TRACE request.
     * 由服务器调用（通过service()方法）以允许 Servlet 处理 TRACE 请求。
     *
     * A TRACE returns the headers sent with the TRACE
     * request to the client, so that they can be used in
     * debugging. There's no need to override this method. 
     * TRACE 将与 TRACE 请求一起发送的标头返回给客户端，以便它们可用于调试。
     * 无需重写此方法。
     *
     * @param req   the {@link HttpServletRequest} object that
     *                  contains the request the client made of
     *                  the servlet
     * HttpServletRequest 对象，该对象包含客户端对 Servlet 发出的请求
     *
     * @param resp  the {@link HttpServletResponse} object that
     *                  contains the response the servlet returns
     *                  to the client                                
     * HttpServletResponse 对象，该对象包含 Servlet 发送到客户端的响应
     * 
     * @exception IOException   if an input or output error occurs
     *                              while the servlet is handling the
     *                              TRACE request
     * 如果在servlet处理 TRACE 请求时检测到输入或输出错误
     *
     * @exception ServletException  if the request for the
     *                                  TRACE cannot be handled
     * 如果无法处理 TRACE 请求
     */
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException
    {
        
        int responseLength;

        String CRLF = "\r\n";
        StringBuilder buffer = new StringBuilder("TRACE ").append(req.getRequestURI())
            .append(" ").append(req.getProtocol());

        Enumeration<String> reqHeaderEnum = req.getHeaderNames();

        while( reqHeaderEnum.hasMoreElements() ) {
            String headerName = reqHeaderEnum.nextElement();
            buffer.append(CRLF).append(headerName).append(": ")
                .append(req.getHeader(headerName));
        }

        buffer.append(CRLF);

        responseLength = buffer.length();

        resp.setContentType("message/http");
        resp.setContentLength(responseLength);
        ServletOutputStream out = resp.getOutputStream();
        out.print(buffer.toString());
    }


    /**
     * Receives standard HTTP requests from the public
     * <code>service</code> method and dispatches
     * them to the <code>do</code><i>XXX</i> methods defined in 
     * this class. This method is an HTTP-specific version of the 
     * {@link javax.servlet.Servlet#service} method. There's no
     * need to override this method.
     * 从public service()方法接收标准 HTTP 请求，并将其分发给
     * 此类中定义的 doXXX() 方法。此方法是 javax.servlet.Servlet#service 方法
     * 的 HTTP 特定版本, 无需覆盖此方法。
     *
     * @param req   the {@link HttpServletRequest} object that
     *                  contains the request the client made of
     *                  the servlet
     * HttpServletRequest 对象，该对象包含客户端对 Servlet 发出的请求
     *
     * @param resp  the {@link HttpServletResponse} object that
     *                  contains the response the servlet returns
     *                  to the client     
     * HttpServletResponse 对象，该对象包含 Servlet 发送到客户端的响应
     *
     * @exception IOException   if an input or output error occurs
     *                              while the servlet is handling the
     *                              HTTP request
     * 如果在servlet处理 HTTP 请求时检测到输入或输出错误
     *
     * @exception ServletException  if the HTTP request
     *                                  cannot be handled
     * 如果无法处理 HTTP 请求 
     *
     * @see javax.servlet.Servlet#service
     */
    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String method = req.getMethod();

        if (method.equals(METHOD_GET)) {
            long lastModified = getLastModified(req);
            if (lastModified == -1) {
                // servlet doesn't support if-modified-since, no reason
                // to go through further expensive logic
                doGet(req, resp);
            } else {
                long ifModifiedSince = req.getDateHeader(HEADER_IFMODSINCE);
                if (ifModifiedSince < lastModified) {
                    // If the servlet mod time is later, call doGet()
                    // Round down to the nearest second for a proper compare
                    // A ifModifiedSince of -1 will always be less
                    maybeSetLastModified(resp, lastModified);
                    doGet(req, resp);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                }
            }

        } else if (method.equals(METHOD_HEAD)) {
            long lastModified = getLastModified(req);
            maybeSetLastModified(resp, lastModified);
            doHead(req, resp);

        } else if (method.equals(METHOD_POST)) {
            doPost(req, resp);
            
        } else if (method.equals(METHOD_PUT)) {
            doPut(req, resp);
            
        } else if (method.equals(METHOD_DELETE)) {
            doDelete(req, resp);
            
        } else if (method.equals(METHOD_OPTIONS)) {
            doOptions(req,resp);
            
        } else if (method.equals(METHOD_TRACE)) {
            doTrace(req,resp);
            
        } else {
            //
            // Note that this means NO servlet supports whatever
            // method was requested, anywhere on this server.
            //

            String errMsg = lStrings.getString("http.method_not_implemented");
            Object[] errArgs = new Object[1];
            errArgs[0] = method;
            errMsg = MessageFormat.format(errMsg, errArgs);
            
            resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, errMsg);
        }
    }
    

    /*
     * Sets the Last-Modified entity header field, if it has not
     * already been set and if the value is meaningful.  Called before
     * doGet, to ensure that headers are set before response data is
     * written.  A subclass might have set this header already, so we
     * check.
     * 设置 Last-Modified 实体标头字段（如果尚未设置该字段，并且该值是否有意义）。
     * 在 doGet 之前调用，以确保在写入响应数据之前设置标头。
     * 子类可能已经设置了这个标头，所以我们检查一下。
     */
    private void maybeSetLastModified(HttpServletResponse resp,
                                      long lastModified) {
        if (resp.containsHeader(HEADER_LASTMOD))
            return;
        if (lastModified >= 0)
            resp.setDateHeader(HEADER_LASTMOD, lastModified);
    }
   
    
    /**
     * Dispatches client requests to the protected
     * <code>service</code> method. There's no need to
     * override this method.
     * 将客户端请求分发给protected service()方法，无需覆盖该方法
     * 
     * @param req   the {@link HttpServletRequest} object that
     *                  contains the request the client made of
     *                  the servlet
     * HttpServletRequest 对象，该对象包含客户端对 Servlet 发出的请求
     *
     * @param res   the {@link HttpServletResponse} object that
     *                  contains the response the servlet returns
     *                  to the client                 
     * HttpServletResponse 对象，该对象包含 Servlet 发送到客户端的响应
     *
     * @exception IOException   if an input or output error occurs
     *                              while the servlet is handling the
     *                              HTTP request
     * 如果在servlet处理 HTTP 请求时检测到输入或输出错误
     *
     * @exception ServletException  if the HTTP request cannot
     *                                  be handled
     * 如果无法处理 HTTP 请求 
     * 
     * @see javax.servlet.Servlet#service
     */
    @Override
    public void service(ServletRequest req, ServletResponse res)
        throws ServletException, IOException
    {
        HttpServletRequest  request;
        HttpServletResponse response;
        
        if (!(req instanceof HttpServletRequest &&
                res instanceof HttpServletResponse)) {
            throw new ServletException("non-HTTP request or response");
        }

        request = (HttpServletRequest) req;
        response = (HttpServletResponse) res;

        service(request, response);
    }
}


/*
 * A response that includes no body, for use in (dumb) "HEAD" support.
 * This just swallows that body, counting the bytes in order to set
 * the content length appropriately.  All other methods delegate directly
 * to the wrapped HTTP Servlet Response object.
 * 不包含正文的响应，用于（哑）“HEAD”支持。这只会吞噬该正文，计算字节数以适当地设置内容长度。
 * 所有其他方法都直接委托给包装的 HTTP Servlet Response 对象。
 */
// file private
class NoBodyResponse extends HttpServletResponseWrapper {

    private static final ResourceBundle lStrings
        = ResourceBundle.getBundle("javax.servlet.http.LocalStrings");

    private NoBodyOutputStream noBody;
    private PrintWriter writer;
    private boolean didSetContentLength;
    private boolean usingOutputStream;

    // file private
    NoBodyResponse(HttpServletResponse r) {
        super(r);
        noBody = new NoBodyOutputStream();
    }

    // file private
    void setContentLength() {
        if (!didSetContentLength) {
            if (writer != null) {
                writer.flush();
            }
            setContentLength(noBody.getContentLength());
        }
    }

    @Override
    public void setContentLength(int len) {
        super.setContentLength(len);
        didSetContentLength = true;
    }

    @Override
    public void setContentLengthLong(long len) {
        super.setContentLengthLong(len);
        didSetContentLength = true;
    }

    @Override
    public void setHeader(String name, String value) {
        super.setHeader(name, value);
        checkHeader(name);
    }

    @Override
    public void addHeader(String name, String value) {
        super.addHeader(name, value);
        checkHeader(name);
    }

    @Override
    public void setIntHeader(String name, int value) {
        super.setIntHeader(name, value);
        checkHeader(name);
    }

    @Override
    public void addIntHeader(String name, int value) {
        super.addIntHeader(name, value);
        checkHeader(name);
    }

    private void checkHeader(String name) {
        if ("content-length".equalsIgnoreCase(name)) {
            didSetContentLength = true;
        }
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {

        if (writer != null) {
            throw new IllegalStateException(
                lStrings.getString("err.ise.getOutputStream"));
        }
        usingOutputStream = true;

        return noBody;
    }

    @Override
    public PrintWriter getWriter() throws UnsupportedEncodingException {

        if (usingOutputStream) {
            throw new IllegalStateException(
                lStrings.getString("err.ise.getWriter"));
        }

        if (writer == null) {
            OutputStreamWriter w = new OutputStreamWriter(
                noBody, getCharacterEncoding());
            writer = new PrintWriter(w);
        }

        return writer;
    }
}


/*
 * Servlet output stream that gobbles up all its data.
 * 吞噬其所有数据的 Servlet 输出流。
 */
// file private
class NoBodyOutputStream extends ServletOutputStream {

    private static final String LSTRING_FILE =
        "javax.servlet.http.LocalStrings";
    private static ResourceBundle lStrings =
        ResourceBundle.getBundle(LSTRING_FILE);

    private int contentLength = 0;

    // file private
    NoBodyOutputStream() {}

    // file private
    int getContentLength() {
        return contentLength;
    }

    @Override
    public void write(int b) {
        contentLength++;
    }

    @Override
    public void write(byte buf[], int offset, int len)
        throws IOException
    {
        if (buf == null) {
            throw new NullPointerException(
                    lStrings.getString("err.io.nullArray"));
        }

        if (offset < 0 || len < 0 || offset+len > buf.length) {
            String msg = lStrings.getString("err.io.indexOutOfBounds");
            Object[] msgArgs = new Object[3];
            msgArgs[0] = Integer.valueOf(offset);
            msgArgs[1] = Integer.valueOf(len);
            msgArgs[2] = Integer.valueOf(buf.length);
            msg = MessageFormat.format(msg, msgArgs);
            throw new IndexOutOfBoundsException(msg);
        }

        contentLength += len;
    }


    public boolean isReady() {
        return false;
    }

    public void setWriteListener(WriteListener writeListener) {

    }
}
