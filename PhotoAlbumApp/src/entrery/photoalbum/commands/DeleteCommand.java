package entrery.photoalbum.commands;

import javax.servlet.http.HttpServletRequest;

import entrery.photoalbum.resource.api.IResourceController;
import entrery.photoalbum.resource.load.FileUtil;

public class DeleteCommand implements ICommand {
	
	private HttpServletRequest request;
	private IResourceController resourceController;
	
	public DeleteCommand(HttpServletRequest request, IResourceController resourceController) {
		this.request = request;
		this.resourceController = resourceController;
	}
	
	@Override
	public void execute() {
		String artifactPath = request.getParameter("categoryPath");
		String artifactId = request.getParameter("artifactId");
		
		removeArtifact(artifactPath, artifactId);  
	}

	private void removeArtifact(String artifactPath, String artifactId) {
		String filePath = FileUtil.generateFilePath(artifactPath, artifactId);
		
		if(!FileUtil.isImage(artifactId)) {
			removeArtifact(artifactPath, artifactId + ".jpg");
		}
		
		resourceController.deleteArtifact(filePath);
	}
}
