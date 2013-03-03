package entrery.photoalbum.resource.api;

import java.io.IOException;
import java.io.InputStream;

public interface IResourceController {

	public byte[] loadImage(String imageFilePath, String mimeType);
	
	public void createImage(InputStream imageInputStream, String pathToSafe) throws IOException;
	
	public void createDirectory(String newCategoryFilePath);
	
	public void deleteArtifact(String artifactPath);
}
