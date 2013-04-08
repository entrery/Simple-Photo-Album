package entrery.photoalbum.resource.load;

/** Will be used to calculate the right path
 *  for artefact reading/writing
 */

public class FileUtil {

	private static final String FILE_PATH = "C:\\Users\\EnTrERy\\Desktop\\My Pictures\\";
	
	public static String generateFilePath(String artifactPath, String artifactId) {
		return generaFilePath(artifactPath + "@" + artifactId);
	}
	
	public static String generaFilePath(String fullArtifactPath) {
		return FILE_PATH + convertToFilePath(fullArtifactPath);
	}
	
	public static String generateCategoryPath(String categoryPath, String categoryName, boolean isCategoryImage) { 
		if(isCategoryImage) {
			String categoryImageName = categoryName + ".jpg";
			return buildCategoryPath(categoryPath, categoryImageName);
		}
		
		return buildCategoryPath(categoryPath, categoryName);
	}
	
	private static String buildCategoryPath(String categoryPath, String categoryArtifactName) {
		String toConvert = "";
		if (categoryPath == null || categoryPath.equals("")) {
			toConvert = categoryArtifactName;
		} else {
			toConvert = categoryPath + "@" + categoryArtifactName;
		}

		return FileUtil.generaFilePath(toConvert);
	}
	
	public static boolean isImage(String artifactId) {
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
