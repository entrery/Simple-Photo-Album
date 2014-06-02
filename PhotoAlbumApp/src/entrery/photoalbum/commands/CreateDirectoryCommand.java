package entrery.photoalbum.commands;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import entrery.photoalbum.resource.api.IResourceController;
import entrery.photoalbum.resource.load.FileUtil;
import entrery.photoalbum.resource.load.ImageDAO;
import entrery.photoalbum.resource.load.ImageUtil;

public class CreateDirectoryCommand implements ICommand {
	
	private HttpServletRequest request;
	private IResourceController resourceController;
	
	public CreateDirectoryCommand(HttpServletRequest request, IResourceController resourceController) {
		this.request = request;
		this.resourceController = resourceController;
	}
	
	@Override
	public void execute() {
		String categoryPath = request.getParameter("categoryPath");
		String newCategoryName = request.getParameter("categoryName");
		
		insertIntoImageMetadata(categoryPath, newCategoryName);
		createCategory(categoryPath, newCategoryName);
	}
	
	private void insertIntoImageMetadata(String categoryPath, String newCategoryName) {
		//String user = (String)request.getSession().getAttribute("user");
		String user = "sasho";
		String parentCategory = ImageUtil.getLastCategory(categoryPath);
		ImageDAO imageDAO = new ImageDAO();
		imageDAO.insertIntoImageMetadata(newCategoryName, parentCategory, "jpg", user, 1);
	}
	
	private void createCategory(String categoryPath, String newCategoryName) {
		try {
			InputStream defaultFolderImageToStream = ImageUtil.getDefaultFolderImageAsStream();
			
			String pathToCreateDir = FileUtil.generateCategoryPath(categoryPath, newCategoryName, false);
			String pathToSafeCategoryImage = FileUtil.generateCategoryPath(categoryPath, newCategoryName, true);
			
			resourceController.createDirectory(pathToCreateDir);
			resourceController.createImage(defaultFolderImageToStream, pathToSafeCategoryImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
