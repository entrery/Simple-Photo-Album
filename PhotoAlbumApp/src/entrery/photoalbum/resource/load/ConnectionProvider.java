package entrery.photoalbum.resource.load;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {

	private static final String DB_LOCATION = "jdbc:derby://localhost/D:/ALL MY STUFF/Desktop 14.12.2012/derbyDBs/db";

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
