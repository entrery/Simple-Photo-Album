package entrery.photoalbum.commands;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.eaio.uuid.UUID;

import entrery.photoalbum.resource.api.IResourceController;
import entrery.photoalbum.resource.load.FileUtil;
import entrery.photoalbum.resource.load.ImageDAO;
import entrery.photoalbum.resource.load.ImageUtil;

public class UploadCommand implements ICommand {

	private HttpServletRequest request;
	private IResourceController resourceController;
	
	public UploadCommand(HttpServletRequest request, IResourceController resourceController) {
		this.request = request;
		this.resourceController = resourceController;
	}
	
	@Override
	public void execute() {	
		List<FileItem> formDataItems = getFormDataItems();		
		FileItem imageItem = getFormDataItem("photo", formDataItems);
		String imageId = buildImageId(imageItem.getContentType());
	    String categoryPath = getFormDataItem("categoryPath", formDataItems).getString();
	    
		insertIntoMeasureMetadata(categoryPath, imageId);
		saveImage(imageItem, categoryPath, imageId);
	}
	
	private FileItem getFormDataItem(String elemKey, List<FileItem> items) {
		FileItem result = null;
		for (FileItem item : items) {
			if(item.getFieldName().equals(elemKey)) {
				result = item;
			}
        }
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<FileItem> getFormDataItems() {
		List<FileItem> list = null;
		try {
			list = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		};
		return list;
	}

	private String buildImageId(String rawMimeType) {
		UUID uuid = new UUID();
	    String mimeType = ImageUtil.getMimeTypeExtension(rawMimeType);
	    return uuid.toString() + "." + mimeType;		
	}

	private void insertIntoMeasureMetadata(String categoryPath, String imageId) {
		String user = (String)request.getSession().getAttribute("user");
		String imageCategory = ImageUtil.getLastCategory(categoryPath);
		ImageDAO imageDAO = new ImageDAO();
		imageDAO.insertIntoImageMetadata(imageId, imageCategory, null, user, 0);
	}

	private void saveImage(FileItem imageItem, String categoryPath, String imageId) {
		try {
			InputStream imageInputStream = imageItem.getInputStream(); 
			String imageFilePath = FileUtil.generateFilePath(categoryPath, imageId);
			resourceController.createImage(imageInputStream, imageFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
