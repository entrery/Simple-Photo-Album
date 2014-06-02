package entrery.photoalbum.resource.load;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {

	private static final String DB_LOCATION = "jdbc:derby://localhost/C:/Users/EnTrERy/Desktop/db-derby-10.9.1.0-bin/photo_album_db/photo";

	public static Connection getConnection() throws Exception  {
		Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		Connection connection = DriverManager.getConnection(DB_LOCATION);
		
		return connection; 
	}
	
	public static void closeConnection(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}
}
