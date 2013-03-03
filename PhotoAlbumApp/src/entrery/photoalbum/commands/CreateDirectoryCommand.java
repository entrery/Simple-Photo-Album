package entrery.photoalbum.commands;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import entrery.photoalbum.resource.load.ImageDAO;
import entrery.photoalbum.resource.load.ImageUtil;
import entrery.photoalbum.resource.load.ResourceController;

public class CreateDirectoryCommand implements ICommand {
	
	private HttpServletRequest request;
	
	public CreateDirectoryCommand(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public void execute() {
		String categoryPath = request.getParameter("categoryPath");
		String newCategoryName = request.getParameter("categoryName");
		
		insertIntoImageMetadata(categoryPath, newCategoryName);
		createCategory(categoryPath, newCategoryName);
	}
	
	private void insertIntoImageMetadata(String categoryPath, String newCategoryName) {
		String user = (String)request.getSession().getAttribute("user");
		String parentCategory = ImageUtil.getLastCategory(categoryPath);
		ImageDAO imageDAO = new ImageDAO();
		imageDAO.insertIntoImageMetadata(newCategoryName, parentCategory, "jpg", user, 1);
	}
	
	private void createCategory(String categoryPath, String newCategoryName) {
		try {
			InputStream defaultFolderImageToStream = ImageUtil.getDefaultFolderImageAsStream();
			ResourceController.createDirectory(newCategoryName, categoryPath);
			ResourceController.createDirectoryImage(defaultFolderImageToStream, categoryPath, newCategoryName);  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
