package entrery.photoalbum.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entrery.photoalbum.resource.load.ResourceController;

public class RetrieveImageCommand implements ICommand {

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public RetrieveImageCommand(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public void execute() {
		String fullImagePath = request.getPathInfo().substring(1); 
		String imageName = fullImagePath.substring(fullImagePath.lastIndexOf("@") + 1);
		String mimeType = imageName.substring(imageName.lastIndexOf(".") + 1);
		
		initResponseHeaders(fullImagePath, mimeType);
		sendImage(getImageAsStream(fullImagePath, mimeType));		
	}

	private InputStream getImageAsStream(String fullImagePath, String mimeType) {
		return ResourceController.getImage(fullImagePath, mimeType);
	}

	private void initResponseHeaders(String fullImagePath, String mimeType) {
		response.setHeader("Content-Type", "image/" + mimeType);
		response.setHeader("Content-Disposition", "inline; filename=\"" + fullImagePath + "\"");
	}

	private void sendImage(InputStream imageInputStream) {
		OutputStream servletOutputStream;
		try {
			servletOutputStream = response.getOutputStream();
			ResourceController.writeToOutputStream(imageInputStream, servletOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
