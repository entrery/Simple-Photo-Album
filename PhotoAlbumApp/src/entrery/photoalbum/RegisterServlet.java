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

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("name");
		String userPass = request.getParameter("pass");
		String realName = request.getParameter("realName");
		String email = request.getParameter("email");
		
		if(!userExists(userName)) {
			ImageDAO dao = new ImageDAO();
			dao.registerUser(userName, userPass, realName, email);
			
			HttpSession session = request.getSession(true);
			session.setAttribute("user", userName);
			
			ServletContext context = getServletContext();
			String url = "/CommandExecutorServlet?action=CREATE&forward=TRUE&categoryName=" + userName;
			RequestDispatcher dispatcher = context.getRequestDispatcher(url);
			dispatcher.forward(request, response);
		} else {
			ServletContext context = getServletContext();
			String url = "/registerPage.jsp?error=true";
			RequestDispatcher dispatcher = context.getRequestDispatcher(url);
			dispatcher.forward(request, response);
		}
	}

	
	private boolean userExists(String requestedUserName) {
		ImageDAO dao = new ImageDAO();
		String user = dao.getUser(requestedUserName);
		return user != null ? true : false;
	}
}
