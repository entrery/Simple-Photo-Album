package entrery.photoalbum.resource.load;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ResourceController {
	
	private static final String FILE_PATH = "C:\\Users\\EnTrERy\\Desktop\\My Pictures\\";
	
	public static InputStream getImage(String imageId, String mimeType) {
		String imageFilePath = convertToFilePath(imageId);
		InputStream imageInputStream = null;
		try {
			BufferedImage img = ImageIO.read(new File(FILE_PATH + imageFilePath));
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, mimeType, os); 
			imageInputStream = new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return imageInputStream;
	}
	
	public static void persistImage(InputStream imageInputStream, String categoryPath, String imageId) throws IOException {
		 String pathToSafe = FILE_PATH + convertToFilePath(categoryPath + "@" + imageId);
		 BufferedOutputStream imageOutputStream = new BufferedOutputStream(new FileOutputStream(new File(pathToSafe)));
		 writeToOutputStream(imageInputStream, imageOutputStream); 
	}

	public static void createDirectoryImage(InputStream defaultFolderImageInputStream, String categoryPath, String categoryName) throws IOException {
		String toConvert = "";
		if ( categoryPath == null || categoryPath.equals("")) {
			toConvert =  categoryName + ".jpg";
		} else {
			toConvert = categoryPath + "@" + categoryName + ".jpg";
		}
		
		String pathToSafe = FILE_PATH + convertToFilePath(toConvert);
		 FileOutputStream fos = new FileOutputStream(new File(pathToSafe));
		 BufferedOutputStream bos = new BufferedOutputStream(fos);
		 writeToOutputStream(defaultFolderImageInputStream, bos);
	}
	
	public static void writeToOutputStream(InputStream inputStream, OutputStream outputStream) throws IOException {
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
	
	public static void createDirectory(String categoryName, String categoryPath) {
		String toConvert = "";
		if (categoryPath == null || categoryPath.equals("")) {
			toConvert = categoryName;
		} else {
			toConvert = categoryPath + "@" + categoryName;
		}
		
		 String newCategoryPath = FILE_PATH + convertToFilePath(toConvert);
		 File file = new File(newCategoryPath);
		 file.mkdir();
	} 
	
	public static void removeArtifact(String fullArtifactPath, String artifactId) {
		String filePath = FILE_PATH + convertToFilePath(fullArtifactPath + "@" + artifactId);
	
		if(!isImage(artifactId)) {
			removeArtifact(fullArtifactPath, artifactId + ".jpg");
		}
		
		deleteFile(new File(filePath));
	}

	private static void deleteFile(File fileToDelete) {
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
	
	private static boolean isImage(String artifactId) {
		return artifactId.contains(".jpg") || artifactId.contains(".png");
	}

	private static String convertToFilePath(String path) {
		String[] pathParts = path.split("@");
		String result = "";
		for (String pathPart : pathParts) {
			result += "\\" + pathPart;
		}
		return result;
	}
	
}
