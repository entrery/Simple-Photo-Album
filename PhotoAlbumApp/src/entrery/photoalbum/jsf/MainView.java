package entrery.photoalbum.jsf;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import com.eaio.uuid.UUID;

import entrery.photoalbum.commands.CreateDirectoryCommand;
import entrery.photoalbum.resource.api.IResourceController;
import entrery.photoalbum.resource.load.FileUtil;
import entrery.photoalbum.resource.load.ImageDAO;
import entrery.photoalbum.resource.load.ImageUtil;
import entrery.photoalbum.resource.load.ResourceController;

@ManagedBean
@ViewScoped
public class MainView {

	private String categoryPath;
	private TreeNode root;
	private UploadedFile file;
	private String newCategoryName;
	 
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String create() {
    	new CreateDirectoryCommand(null, new ResourceController()).execute(UserService.getLoggedInUser(), categoryPath, newCategoryName);
    	return "photoalbum.jsf?faces-redirect=true&includeViewParams=true";
    }
    
    public String upload() {
        if(file != null) {
        	try {
        		String imageId = buildImageId(file.getContentType());
        		insertIntoImageMetadata(categoryPath, imageId);
				saveImage(file.getInputstream(), categoryPath, imageId);

				FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);
			} catch (Exception e) {
				 FacesMessage message = new FacesMessage("Error! ", file.getFileName() + " is not uploaded.");
		         FacesContext.getCurrentInstance().addMessage(null, message);
			}
        }
        return "photoalbum.jsf?faces-redirect=true&includeViewParams=true";
    }
    
    private String buildImageId(String rawMimeType) {
		UUID uuid = new UUID();
	    String mimeType = ImageUtil.getMimeTypeExtension(rawMimeType);
	    return uuid.toString() + "." + mimeType;		
	}

	private void insertIntoImageMetadata(String categoryPath, String imageId) {
		//String user = (String)request.getSession().getAttribute("user");
		//String user = "sasho";
		
		String user = UserService.getLoggedInUser();
		
		String imageCategory = ImageUtil.getLastCategory(categoryPath);
		ImageDAO imageDAO = new ImageDAO();
		imageDAO.insertIntoImageMetadata(imageId, imageCategory, null, user, 0);
	}

	private void saveImage(InputStream imageInputStream, String categoryPath, String imageId) throws Exception {
		IResourceController resourceController = new ResourceController();
			String imageFilePath = FileUtil.generateFilePath(categoryPath, imageId);
			resourceController.createImage(imageInputStream, imageFilePath);
	}
	
	@PostConstruct
	public void createCategoryTree() {
		//String category = "sasho";

		String user = UserService.getLoggedInUser();
		root = new DefaultTreeNode();
		
		DefaultTreeNode userCategoryNode = new DefaultTreeNode(new Data("photoalbum.jsf?categoryPath=" + user, user));
		root.getChildren().add(userCategoryNode);
		
		getAllSubCategories(user, userCategoryNode);
	}
	
	private void getAllSubCategories(String category, TreeNode node) {
		
		List<String> firstLevelSubCategoris = getFirstLevelSubCategories(category);
		
		for (String subCategory : firstLevelSubCategoris) {
			Data parentData = (Data)node.getData();
			TreeNode newNode = new DefaultTreeNode(new Data(parentData.getUrl() + "@" + subCategory, subCategory));
			node.getChildren().add(newNode);
			
			getAllSubCategories(subCategory, newNode);
		}
	}

	private List<String> getFirstLevelSubCategories(String category) {
		return new ImageDAO().getAllNestedCategories(UserService.getLoggedInUser(), category);
	}

	public static class Data {

		private String url;
		private String name;
	
		public Data(String url, String name) {
			this.setUrl(url);
			this.setName(name);
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public List<String> getImages() throws Exception {
//		HttpServletRequest request = (HttpServletRequest) FacesContext
//				.getCurrentInstance().getExternalContext().getRequest();

		// String categoryPath = request.getParameter("categoryPath");
		// String categoryPath = "sasho";
		// String user = (String)request.getSession().getAttribute("user");
		//String user = "sasho";

		String user = UserService.getLoggedInUser();
		
		if (categoryPath == null || categoryPath.equals("null")) {
			categoryPath = user;
		}

		// if(!categoryPath.contains(user)) {
		// request.getSession().invalidate();
		// getServletContext().getRequestDispatcher("/LogoutServlet").forward(request,
		// response);
		// }

		String requestedCategory = ImageUtil.getLastCategory(categoryPath);

		ImageDAO imageDAO = new ImageDAO();
		List<String> images = imageDAO.getAllImageNamesForUserCategory(user,
				requestedCategory);

		List<String> allImages = new ArrayList<>();

		for (String image : images) {
			String imagePath = categoryPath + "@" + image;
			allImages.add("CommandExecutorServlet/" + imagePath
					+ "?action=LOAD&forward=FALSE");
		}

		return allImages;
	}

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public String getNewCategoryName() {
		return newCategoryName;
	}

	public void setNewCategoryName(String newCategoryName) {
		this.newCategoryName = newCategoryName;
	}
}