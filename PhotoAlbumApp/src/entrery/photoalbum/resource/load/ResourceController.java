package entrery.photoalbum.resource.load;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import entrery.photoalbum.resource.api.IResourceController;

//This controller should not know how to convert from url to real file path. It should only be able to write and read images from/to the file system

public class ResourceController implements IResourceController {
	
	public byte[] loadImage(String imageFilePath, String mimeType) {
		ByteArrayOutputStream os = null;
		try {
			BufferedImage img = ImageIO.read(new File(imageFilePath));
			os = new ByteArrayOutputStream();
			ImageIO.write(img, mimeType, os); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return os.toByteArray();
	}

	public void createImage(InputStream imageInputStream, String pathToSafe) throws IOException {
		 BufferedOutputStream imageOutputStream = new BufferedOutputStream(new FileOutputStream(new File(pathToSafe)));
		 writeToOutputStream(imageInputStream, imageOutputStream); 
	}
	
	public void createDirectory(String newCategoryFilePath) {
		File file = new File(newCategoryFilePath);
		file.mkdir();
	} 
	
	public void deleteArtifact(String filePath) {
		deleteFile(new File(filePath));
	}

	private void writeToOutputStream(InputStream inputStream, OutputStream outputStream) throws IOException {
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
	
		try {
		    input = new BufferedInputStream(inputStream);
		    output = new BufferedOutputStream(outputStream);
		    byte[] buffer = new byte[8192];
		    int length = 0;
		    while ((length = input.read(buffer)) != -1) {
		    	output.write(buffer, 0, length);
		    }
		    
		} finally {
		    if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
		    if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
		}
	}

	private void deleteFile(File fileToDelete) {
		File[] files = fileToDelete.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					deleteFile(file);
				} else {
					file.delete();
					new ImageDAO().deleteArtifactFromImageMetadata(file.getName());
				}
			}
		}
		fileToDelete.delete();
		new ImageDAO().deleteArtifactFromImageMetadata(fileToDelete.getName());
	}
}
