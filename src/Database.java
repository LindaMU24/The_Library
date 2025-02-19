import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String url = "jdbc:mysql://localhost:3306/library";
    private static final String user = "root";
    private static final String password = System.getenv("MYSQL_PASSWORD");

    public static Connection getConnection() throws SQLException {
        Connection conn =null;
        try {
            conn = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}
