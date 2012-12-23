package entrery.photoalbum.commands;

import javax.servlet.http.HttpServletRequest;
import entrery.photoalbum.resource.load.ResourceController;

public class DeleteCommand implements ICommand {
	
	private HttpServletRequest request;
	
	public DeleteCommand(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public void execute() {
		String artifactPath = request.getParameter("categoryPath");
		String artifactId = request.getParameter("artifactId");
		
		removeArtifact(artifactPath, artifactId);  
	}

	private void removeArtifact(String artifactPath, String artifactId) {
		ResourceController.removeArtifact(artifactPath, artifactId);
	}
}
