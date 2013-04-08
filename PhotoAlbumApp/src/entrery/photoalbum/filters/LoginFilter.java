package entrery.photoalbum.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/LoginFilter")
public class LoginFilter implements Filter {

	private List<String> urlList;
	private FilterConfig filterConfig;
	
    public LoginFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
		
		
        String urls = filterConfig.getInitParameter("avoid");
        StringTokenizer token = new StringTokenizer(urls, ",");
 
        urlList = new ArrayList<String>();
 
        while (token.hasMoreTokens()) {
            urlList.add(token.nextToken());
        }
		
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String url = request.getServletPath();
        boolean allowedRequest = false;
         
        if(urlList.contains(url)) {
            allowedRequest = true;
            chain.doFilter(req, res);
        }
             
        if (!allowedRequest) {
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("loginPage.jsp");
            } else {
                chain.doFilter(req, res);
            }
        } 
    }

	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
    }
}
