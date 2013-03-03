package entrery.photoalbum.resource.load;

import java.io.InputStream;

import javax.servlet.ServletContext;

public class ImageUtil {
	
	private static ServletContext servletContext;
	
	public static String getLastCategory(String categoryPath) {
		if(categoryPath == null) {
			return "";
		}
		return categoryPath.substring(categoryPath.lastIndexOf("@") + 1); 
	}
	
	public static String getMimeTypeExtension(String fullMimeType) {
		return fullMimeType.substring(fullMimeType.indexOf("/") + 1); 
	}
	
	public static void registerServletContext(ServletContext context) {
		servletContext = context;
	}
	
	public static InputStream getDefaultFolderImageAsStream() {
		return servletContext.getResourceAsStream("/resources/images/folder.jpg");
	}

}
