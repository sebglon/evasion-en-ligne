/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sgl
 */
public class CrawlServlet implements Filter {

    /**
     * Special URL token that gets passed from the crawler to the servlet
     * filter. This token is used in case there are already existing query
     * parameters.
     */
    private static final String ESCAPED_FRAGMENT_FORMAT1 = "_escaped_fragment_=";
    private static final int ESCAPED_FRAGMENT_LENGTH1 = ESCAPED_FRAGMENT_FORMAT1.length();
    /**
     * Special URL token that gets passed from the crawler to the servlet
     * filter. This token is used in case there are not already existing query
     * parameters.
     */
    private static final String ESCAPED_FRAGMENT_FORMAT2 = "&" + ESCAPED_FRAGMENT_FORMAT1;
    private static final int ESCAPED_FRAGMENT_LENGTH2 = ESCAPED_FRAGMENT_FORMAT2.length();

    private WebClient webClient = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String requestURI = httpRequest.getRequestURI();
        final String queryString = httpRequest.getQueryString();

        if ((queryString != null) && queryString.contains("_escaped_fragment_") &&  !queryString.contains("/ws/")) {

            final String domain = httpRequest.getServerName();
            final int port = httpRequest.getServerPort();

            // Rewrite the URL back to the original #! version
            //   -- basically remove _escaped_fragment_ from the query. 
            // Unescape any %XX characters as need be.
            final String urlStringWithHashFragment = requestURI + rewriteQueryString(queryString);
            final String protocol = httpRequest.getScheme();
            final URL urlWithHashFragment = new URL(protocol, domain, port, urlStringWithHashFragment);

            // use the headless browser to obtain an HTML snapshot
            webClient = new WebClient(BrowserVersion.CHROME);
            // set options
            WebClientOptions options = webClient.getOptions();
            options.setCssEnabled(true);
            webClient.setCssErrorHandler(new SilentCssErrorHandler());
            
            options.setThrowExceptionOnScriptError(true);
            options.setThrowExceptionOnFailingStatusCode(false);
            options.setRedirectEnabled(false);
            options.setAppletEnabled(false);
            options.setJavaScriptEnabled(true);
            options.setTimeout(50000);
            webClient.addRequestHeader("Access-Control-Allow-Origin", "*");

            final HtmlPage page = webClient.getPage(urlWithHashFragment);

            // important!  Give the headless browser enough time to execute JavaScript
            // The exact time to wait may depend on your application.
            webClient.setJavaScriptTimeout(20000);
            webClient.waitForBackgroundJavaScript(20000);
            // return the snapshot
            final PrintWriter out = httpResponse.getWriter();
            out.write(page.asXml());
            webClient.closeAllWindows();
        } else {
                // not an _escaped_fragment_ URL, so move up the chain of servlet (filters)
                chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        if (webClient != null) {
            webClient.closeAllWindows();
        }
    }

    private static String rewriteQueryString(String queryString)
            throws UnsupportedEncodingException {
        // Seek the escaped fragment.
        int index = queryString.indexOf(ESCAPED_FRAGMENT_FORMAT2);
        int length = ESCAPED_FRAGMENT_LENGTH2;
        if (index == -1) {
            index = queryString.indexOf(ESCAPED_FRAGMENT_FORMAT1);
            length = ESCAPED_FRAGMENT_LENGTH1;
        }
        if (index != -1) {
            // Found the escaped fragment, so build back the original decoded one.
            final StringBuilder queryStringSb = new StringBuilder();
            // Add url parameters if any.
            if (index > 0) {
                queryStringSb.append("?");
                queryStringSb.append(queryString.substring(0, index));
            }
            // Add the hash fragment as a replacement for the escaped fragment.
            queryStringSb.append("#!");
            // Add the decoded token.
            final String token2Decode = queryString.substring(index + length, queryString.length());
            final String tokenDecoded = URLDecoder.decode(token2Decode, "UTF-8");
            queryStringSb.append(tokenDecoded);
            return queryStringSb.toString();
        }
        return queryString;
    }
}
