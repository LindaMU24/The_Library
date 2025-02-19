import java.sql.SQLException;
import java.util.Scanner;

public class UserMenu {

    private UsersDAO usersDAO = new UsersDAO();
    private Scanner sc = new Scanner(System.in);

    public void showMenu() throws SQLException {
        while (true) {
            System.out.println("----- User Menu -----");
            System.out.println("1. Log In");
            System.out.println("2. Register New User");
            System.out.println("0. Back to Main Menu");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    loginUser();
                    break;
                case "2":
                    registerUser();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void loginUser() {
        System.out.println("Enter your email:");
        String email = sc.nextLine();
        System.out.println("Enter your password:");
        String password = sc.nextLine();

        try {
            Users user = usersDAO.userLogin(email, password);
            if (user != null) {
                System.out.println("Welcome, " + user.getName() + "!");
                // Här kan du kalla på en metod för att visa användarens meny eller funktioner
            } else {
                System.out.println("Invalid email or password. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while trying to log in. Please try again later.");
            e.printStackTrace();
        }
    }

    private void registerUser() {
        System.out.println("Enter your name:");
        String name = sc.nextLine();
        System.out.println("Enter your email:");
        String email = sc.nextLine();
        System.out.println("Enter your password:");
        String password = sc.nextLine();

        // Använd Role direkt istället för Users.Role
        Users newUser = new Users(0, name, password, email, Role.USER);

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