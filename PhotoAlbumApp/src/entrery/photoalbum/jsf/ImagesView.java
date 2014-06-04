package entrery.photoalbum.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import entrery.photoalbum.resource.load.ImageDAO;
import entrery.photoalbum.resource.load.ImageUtil;

@ManagedBean
@RequestScoped
public class ImagesView {

	private String categoryPath;
	private TreeNode root;
	private UploadedFile file;
	 
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
     
    public void upload() {
        if(file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
	
	@PostConstruct
	public void createCategoryTree() {
		String category = "sasho";
		root = new DefaultTreeNode(new Data("photoalbum.jsf?categoryPath=sasho", "root"));
		
		getAllSubCategories(category, root);
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
		return new ImageDAO().getAllNestedCategories("sasho", category);
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
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		// String categoryPath = request.getParameter("categoryPath");
		// String categoryPath = "sasho";
		// String user = (String)request.getSession().getAttribute("user");
		String user = "sasho";

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
		List<String> nestedCategories = imageDAO.getAllNestedCategories(user,
				requestedCategory);

		List<String> allImages = new ArrayList<>();

		for (String folder : nestedCategories) {
			String childCategoryPath = categoryPath + "@" + folder + ".jpg";
			allImages.add("CommandExecutorServlet/" + childCategoryPath
					+ "?action=LOAD&forward=FALSE");
		}

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
}