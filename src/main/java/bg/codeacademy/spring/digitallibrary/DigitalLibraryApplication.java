package bg.codeacademy.spring.digitallibrary;

import bg.codeacademy.spring.digitallibrary.config.FileStorageProperties;
import bg.codeacademy.spring.digitallibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})

public class DigitalLibraryApplication {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:h2:file:./digital_library";

	//  Database credentials
	static final String USER = "sa";
	static final String PASS = "password";

	public static void main(String[] args) {

		SpringApplication.run(DigitalLibraryApplication.class, args);

		Connection conn = null;
		Statement stmt = null;
		try{
			//Register JDBC driver
			//Class.forName("com.mysql.jdbc.Driver");  depricated!

			//Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			//Execute a query
			System.out.println("Creating database...");
			stmt = conn.createStatement();

			String tableBooks = "CREATE TABLE IF NOT EXISTS TBL_BOOKS"
					+ "(book_id BIGINT IDENTITY NOT NULL, "
					+ "title VARCHAR2(255) NOT NULL, "
					+ "genre VARCHAR2(20) NOT NULL, "
					+ "author VARCHAR2(40) NOT NULL, "
					+ "isEnabled BOOLEAN NOT NULL, "
					+ "PRIMARY KEY (book_id));";


			String tableUsers = "CREATE TABLE IF NOT EXISTS TBL_USERS "
					+ "(user_id BIGINT IDENTITY NOT NULL, "
					+ "username VARCHAR2(20) NOT NULL, "
					+ "name VARCHAR2(50) NOT NULL, "  // ADDED NAME COLUMN
					+ "password VARCHAR2(255) NOT NULL, "
					+ "role VARCHAR2(6) NOT NULL, "
					+ "isEnabled BOOLEAN NOT NULL, "
					+ "PRIMARY KEY (user_id));";

			String tableLibrary = "CREATE TABLE IF NOT EXISTS TBL_LIBRARY "
					+ "(user_id BIGINT NOT NULL, "
					+ "book_id BIGINT NOT NULL, "
					+ "FOREIGN KEY (book_id) REFERENCES TBL_BOOKS (book_id),"
					+ "FOREIGN KEY (user_id) REFERENCES TBL_USERS (user_id)); ";

			String tableRating = "CREATE TABLE IF NOT EXISTS TBL_RATING "
					+ "(user_id BIGINT NOT NULL, "
					+ "book_id BIGINT NOT NULL, "
					+ "rating FLOAT NOT NULL,"  // changed from INT to FLOAT
					+ "FOREIGN KEY (book_id) REFERENCES TBL_BOOKS (book_id), "
					+ "FOREIGN KEY (user_id) REFERENCES TBL_USERS (user_id)); ";

			String tableResent = "CREATE TABLE IF NOT EXISTS TBL_RECENT "
					+ "(user_id BIGINT NOT NULL, "
					+ "book_id BIGINT NOT NULL, "
					+ "readDate TIMESTAMP DEFAULT SYSDATE() NOT NULL ,"
					+ "FOREIGN KEY (book_id) REFERENCES TBL_BOOKS (book_id), "
					+ "FOREIGN KEY (user_id) REFERENCES TBL_USERS (user_id)); ";

			stmt.executeUpdate(tableBooks);
			stmt.executeUpdate(tableUsers);
			stmt.executeUpdate(tableLibrary);
			stmt.executeUpdate(tableRating);
			stmt.executeUpdate(tableResent);

			System.out.println("Database created successfully...");

		}
		catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		}
		catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		finally {
			//finally block used to close resources
			try {
				if (stmt != null) {
					stmt.close();
				}
			}
			catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null) {
					conn.close();
				}
			}
			catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try
	}//end main
}//end JDBCExample



