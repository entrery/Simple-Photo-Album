package entrery.photoalbum.commands;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entrery.photoalbum.resource.api.IResourceController;
import entrery.photoalbum.resource.load.FileUtil;

public class RetrieveImageCommand implements ICommand {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private IResourceController resourceController;
	
	public RetrieveImageCommand(HttpServletRequest request, HttpServletResponse response, IResourceController resourceController) {
		this.request = request;
		this.response = response;
		this.resourceController = resourceController;
	}

	@Override
	public void execute() {
		String urlImagePath = request.getPathInfo().substring(1); 
		String imageName = urlImagePath.substring(urlImagePath.lastIndexOf("@") + 1);
		String mimeType = imageName.substring(imageName.lastIndexOf(".") + 1);
		
		initResponseHeaders(urlImagePath, mimeType);
		sendImage(getImage(urlImagePath, mimeType));		
	}

	private byte[] getImage(String urlImagePath, String mimeType) {
		String imageFilePath = FileUtil.generaFilePath(urlImagePath);
		return resourceController.loadImage(imageFilePath, mimeType);
	}

	private void initResponseHeaders(String fullImagePath, String mimeType) {
		response.setHeader("Content-Type", "image/" + mimeType);
		response.setHeader("Content-Disposition", "inline; filename=\"" + fullImagePath + "\"");
	}

	private void sendImage(byte[] image) {
		OutputStream servletOutputStream;
		try {
			servletOutputStream = response.getOutputStream();
			servletOutputStream.write(image);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
