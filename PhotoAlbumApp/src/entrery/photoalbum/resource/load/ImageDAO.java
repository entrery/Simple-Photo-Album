package entrery.photoalbum.resource.load;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageDAO {
	private Connection connection;
	private PreparedStatement statement;
	private ResultSet resultSet;
	
	public ImageDAO() {

	}
	
	public List<String> getAllImageNamesForUserCategory(String username, String category) throws Exception {
		List<String> imageNames = null;
		try {
			imageNames = getPictureNamesForCategory(username, category);
		} catch (Exception ex) {
			ex.printStackTrace();
			imageNames = Collections.emptyList();
		} finally {
			closeAll();
		}
		
		return imageNames;
	}

	public void insertIntoImageMetadata(String imageId, String imageCategory, String mimeType, String userName, int isDirectory) {
		try {
			insertImageRecordIntoMetadata(imageId, imageCategory, mimeType, userName, isDirectory);
		} catch (Exception ex) {
			ex.printStackTrace(); 
		} finally {
			closeAll();
		}		
	}
	
	public List<String> getAllNestedCategories(String username, String parentCategory) {
		List<String> categoryNames = null;
		try {
			categoryNames = getNestedCategoryNames(username, parentCategory, 1);
		} catch (Exception ex) {
			ex.printStackTrace(); 
			categoryNames = Collections.emptyList();
		} finally {
			closeAll();
		}
		
		return categoryNames;
	}
	
	public void deleteArtifactFromImageMetadata(String artifactId) {
		try {
			deleteArtifact(artifactId);
		} catch (Exception ex) {
			ex.printStackTrace(); 
		} finally {
			closeAll();
		}
	}

	private void deleteArtifact(String artifactId) throws Exception {
		connection = getConnection();
		statement = connection.prepareStatement(getDeleteStatement(artifactId));
		statement.execute();		
	}

	private String getDeleteStatement(String artifactId) {
		return "DELETE FROM IMAGE_METADATA WHERE \"ARTIFACT_NAME\"='" + artifactId + "'";
	}

	private List<String> getNestedCategoryNames(String username, String parentCategory, int i) throws Exception {
		connection = getConnection();
		statement = connection.prepareStatement(getCategoryArtefactSelectStatement(username, parentCategory, 1));
		resultSet = statement.executeQuery();
		
		List<String> imageNames = new ArrayList<>();
		while (resultSet.next()) {
			imageNames.add(resultSet.getString("ARTIFACT_NAME"));
		}
		
		return imageNames;
	}

	private void insertImageRecordIntoMetadata(String imageId, String imageCategory, String mimeType, String userName, int isDirectory) throws Exception {
		connection = getConnection();
		statement = connection.prepareStatement(createInsertStatement(imageId, imageCategory, mimeType, userName, isDirectory));
		statement.executeUpdate();		
	}
	
	private String getCategoryArtefactSelectStatement(String username, String category, int isDirectory) {
		String sql = "SELECT \"ARTIFACT_NAME\" FROM IMAGE_METADATA WHERE \"CATEGORY\" = '" + category +
				"' AND \"USER_NAME\" = '" + username + "'" + " AND \"DIRECTORY\" = " + isDirectory;
		return sql;
	}

	private static String createInsertStatement(String imageId, String imageCategory, String mimeType, String userName, int isDirectory) {
		String sql = "INSERT INTO IMAGE_METADATA values ('" + userName + "','" + imageCategory + "','" + mimeType + "','" + imageId + "', " + isDirectory + ")";
					 		
		return sql;
	}

	private List<String> getPictureNamesForCategory(String username, String category) throws Exception {
		connection = getConnection();
		statement = connection.prepareStatement(getCategoryArtefactSelectStatement(username, category, 0));
		resultSet = statement.executeQuery();
		
		List<String> imageNames = new ArrayList<>();
		while (resultSet.next()) {
			imageNames.add(resultSet.getString("ARTIFACT_NAME"));
		}
		
		return imageNames;
	}
	
	public String getUser(String username, String password) {
		String selectSql = "SELECT \"USER_NAME\" FROM \"USERS\" WHERE \"USER_NAME\"='" + username + "' AND \"PASSWORD\"='" + password +"'";
		String user = null;
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement(selectSql);
			resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				user = resultSet.getString("USER_NAME");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}
	
	public String getUser(String username) {
		String selectSql = "SELECT \"USER_NAME\" FROM \"USERS\" WHERE \"USER_NAME\"='" + username + "'";
		String user = null;
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement(selectSql);
			resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				user = resultSet.getString("USER_NAME");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	private void closeAll() {
		try {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private Connection getConnection() throws Exception {
			return  ConnectionProvider.getConnection();
	}
	
	

	public void registerUser(String userName, String userPass, String name, String email) {
		String insertSQL = "INSERT INTO \"USERS\" VALUES ('" + userName + "','" + userPass + "','" + name + "','" + email + "')";
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement(insertSQL);
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
