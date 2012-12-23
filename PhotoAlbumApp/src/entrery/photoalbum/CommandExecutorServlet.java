package entrery.photoalbum;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entrery.photoalbum.commands.Action;
import entrery.photoalbum.commands.CommandFactory;
import entrery.photoalbum.commands.ICommand;
import entrery.photoalbum.resource.load.ImageUtil;

/**
 * Servlet implementation class CommandExecutorServlet
 */
@WebServlet("/CommandExecutorServlet/*")
public class CommandExecutorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CommandExecutorServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
    	super.init();
       	ImageUtil.registerServletContext(getServletContext());
    };
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	//Single point of action processing
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		Action action = Action.valueOf(request.getParameter("action"));
		ICommand command = CommandFactory.getInstance().createCommand(action, request, response);
		command.execute();
		
		if(shouldForwardToMainPage(request)) { 
			forwardToMainPage(request, response);
		}			
	}

	private boolean shouldForwardToMainPage(HttpServletRequest request) {
		return request.getParameter("forward").equals("TRUE");
	}
	
	private void forwardToMainPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("PhotoAlbum.jsp?categoryPath=" + request.getParameter("categoryPath"));
	}

}
