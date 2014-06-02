package entrery.photoalbum.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import entrery.photoalbum.resource.load.ImageDAO;
import entrery.photoalbum.resource.load.ImageUtil;
 
@ManagedBean
@RequestScoped
public class ImagesView {
     
    public List<String> getImages() throws Exception {
    	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	
		String categoryPath = request.getParameter("categoryPath");
		//String categoryPath = "sasho";
		//String user = (String)request.getSession().getAttribute("user");
		String user = "sasho";
	
	
		if(categoryPath == null || categoryPath.equals("null")) {
			categoryPath = user; 
		}
		
//		if(!categoryPath.contains(user)) {
//			request.getSession().invalidate();
//			getServletContext().getRequestDispatcher("/LogoutServlet").forward(request, response);
//		}

		String requestedCategory = ImageUtil.getLastCategory(categoryPath);
		
		ImageDAO imageDAO = new ImageDAO();
		List<String> images = imageDAO.getAllImageNamesForUserCategory(user, requestedCategory);
		List<String> nestedCategories = imageDAO.getAllNestedCategories(user, requestedCategory);
		
		List<String> allImages = new ArrayList<>();
		
		for (String folder : nestedCategories) {
			String childCategoryPath = categoryPath + "@" + folder + ".jpg";
			allImages.add("CommandExecutorServlet/" + childCategoryPath + "?action=LOAD&forward=FALSE");
		}
    	
		for (String image : images) {
			String imagePath = categoryPath + "@" + image;
			allImages.add("CommandExecutorServlet/" + imagePath + "?action=LOAD&forward=FALSE");
		}
		
    	return allImages;
    }
}