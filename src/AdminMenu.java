import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminMenu {

    private UsersDAO usersDAO = new UsersDAO();
    private Scanner sc = new Scanner(System.in);

    public void showMenu() throws SQLException {
        while (true) {
            System.out.println("----- Admin Menu -----");
            System.out.println("1. Log In");
            System.out.println("2. Register New Admin");
            System.out.println("0. Back to Main Menu");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    loginAdmin();
                    break;
                case "2":
                    registerAdmin();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void loginAdmin() throws SQLException {
        System.out.println("Enter email:");
        String email = sc.nextLine();

        if (!isAdmin(email)) {
            System.out.println("User is not an admin.");
            return;
        }

        System.out.println("Enter password:");
        String password = sc.nextLine();

        Users admin = usersDAO.adminLogin(email, password);
        if (admin != null) {
            System.out.println("Login successful! Welcome, " + admin.getName());
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private boolean isAdmin(String email) throws SQLException {
        // Kontrollera om e-postadressen tillhör en admin
        String roleCheckQuery = "SELECT role FROM users WHERE email = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement roleCheckStmt = conn.prepareStatement(roleCheckQuery)) {

            roleCheckStmt.setString(1, email);

            try (ResultSet rs = roleCheckStmt.executeQuery()) {
                if (rs.next()) {
                    String roleString = rs.getString("role");
                    return "admin".equalsIgnoreCase(roleString);
                }
            }
        }
        return false;
    }

    // Placeholder för registerAdmin-metoden
    private void registerAdmin() {
        System.out.println("Registering new admin...");
        System.out.println("Enter your name:");
        String name = sc.nextLine();
        System.out.println("Enter your email:");
        String email = sc.nextLine();
        System.out.println("Enter your password:");
        String password = sc.nextLine();

        // Använd Role direkt istället för Users.Role
        Users newUser = new Users(0, name, password, email, Role.ADMIN);

        try {
            boolean success = usersDAO.addUser(newUser);
            if (success) {
                System.out.println("Registration successful! You can now log in.");
            } else {
                System.out.println("Registration failed. Email might already be in use.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while trying to register. Please try again later.");
            e.printStackTrace();
        }
    }
}

