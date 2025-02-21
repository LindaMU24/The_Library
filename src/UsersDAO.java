import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO {

    public Users userLogin(String email, String password) throws SQLException {
        String query = "SELECT id, name, email, role FROM users WHERE email = ? AND password = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String roleString = rs.getString("role");
                    Role role = Role.valueOf(roleString.toUpperCase());

                    return new Users(id, name, password, email, role);
                }
            }
        }
        return null; // Returnera null om inloggningen misslyckas
    }

    public Users adminLogin(String email, String password) throws SQLException {
        String query = "SELECT id, name, email, role FROM users WHERE email = ? AND password = ? AND role = 'admin'";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String roleString = rs.getString("role");
                    Role role = Role.valueOf(roleString.toUpperCase());

                    return new Users(id, name, password, email, role);
                }
            }
        }
        return null;
    }

    public boolean addUser(Users user) throws SQLException {
        String query = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole().name().toLowerCase());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Returnera true om en rad lades till
        }
    }

    public List<Users> listAllUsers() throws SQLException {
        List<Users> users = new ArrayList<>();
        String query = "SELECT id, name, password, email, role FROM users"; // Lägg till 'password' i frågan

        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password"); // Hämta lösenordet
                String email = rs.getString("email");
                String roleString = rs.getString("role");

                // Konvertera strängen till stora bokstäver för att matcha enum-konstanterna
                Role role = Role.valueOf(roleString.toUpperCase());

                Users user = new Users(id, name, password, email, role);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}