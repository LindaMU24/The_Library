import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class ConsolView {

    private Scanner sc = new Scanner(System.in);
    private UserMenu userMenu = new UserMenu();
    private AdminMenu adminMenu = new AdminMenu(); // Om du har en separat meny för adminfunktioner

    public void showMenu() throws SQLException {
        while (true) {
            System.out.println("Welcome to the Library");
            System.out.println("---- Main Menu ----");
            System.out.println("1. User Login");
            System.out.println("2. Admin Login");
            System.out.println("0. Exit");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    userMenu.showMenu();
                    break;
                case "2":
                    adminMenu.showMenu();
                    break;
                case "0":
                    System.out.println("Exiting the application. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}
