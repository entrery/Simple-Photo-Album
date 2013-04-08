package entrery.photoalbum;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entrery.photoalbum.resource.load.ImageDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String userName = request.getParameter("name");
		String userPass = request.getParameter("pass");

		if (isAuthenticated(userName, userPass)) {
			HttpSession session = request.getSession(true);
			session.setAttribute("user", userName);
			
			forward("/PhotoAlbum.jsp?categoryPath=" + userName, request, response);
		} else {
			
			forward("/loginPage.jsp?error=true", request, response);
			
		}
	}

	private boolean isAuthenticated(String requestedUserName, String requestedPassword) {
		ImageDAO dao = new ImageDAO();
		String user = dao.getUser(requestedUserName, requestedPassword);
		return user != null ? true : false;
	}

	private void forward(String toUrl, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher(toUrl);
		dispatcher.forward(request, response);
	}
	
}
