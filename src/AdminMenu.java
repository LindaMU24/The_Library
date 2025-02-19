import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private UsersDAO usersDAO = new UsersDAO();
    private BookMenu bookMenu = new BookMenu();
    private BooksDAO booksDAO = new BooksDAO();
    private Scanner sc = new Scanner(System.in);

    public void showMenu() throws SQLException {
        while (true) {
            System.out.println("----- Admin Menu -----");
            System.out.println("1. Log In");
            System.out.println("2. Register New Admin");
            System.out.println("3. List all books"); //This can lead to -> List all -> List available -> List borrowed books
            System.out.println("4. Add new book");
            System.out.println("5. Delete book");
            System.out.println("6. List all users");
            System.out.println("0. Back to Main Menu");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    loginAdmin();
                    break;
                case "2":
                    registerAdmin();
                    break;
                case "3":
                    bookMenu.showBooksOptions(true);
                    break;
                    case "4": newBook();
                    break;
                    case "5": deleteBook();
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

    private void registerAdmin() {
        System.out.println("Registering new admin...");
        System.out.println("Enter your name:");
        String name = sc.nextLine();
        System.out.println("Enter your email:");
        String email = sc.nextLine();
        System.out.println("Enter your password:");
        String password = sc.nextLine();

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
    private void newBook() throws SQLException {
        System.out.println("Enter title:");
        String title = sc.nextLine();
        if (title == null || title.isEmpty()) {
            System.out.println("Invalid title, please try again.");
            return;
        }

        System.out.println("Enter author:");
        String author = sc.nextLine();
        if (author == null || author.isEmpty()) {
            System.out.println("Invalid author, please try again.");
            return;
        }

        System.out.println("Enter year of book release:");
        int year = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter genre:");
        String genre = sc.nextLine();
        if (genre == null || genre.isEmpty()) {
            System.out.println("Invalid genre, please try again.");
            return;
        }
        BooksDAO booksDAO = new BooksDAO();

        booksDAO.addNewBook(title, author, year, genre);
        System.out.println("Book added successfully!");
    }

    public void printBooks(List<Books> books, boolean isAdmin) {
        for (Books book : books) {
            System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor());

            if (isAdmin) {
                System.out.println("Year: " + book.getYear() + ", Genre: " + book.getGenre() + ", Available: " + book.isAvailable());
            }
        }
    }

    private void deleteBook() throws SQLException {

        BooksDAO booksDAO = new BooksDAO();
        List<Books> books = booksDAO.listAllBooks(true); // Sätt till true om du är admin

        // Skriv ut böckerna
        printBooks(books, true); // Sätt till true om du är admin

        System.out.println("Enter the ID of the book to delete:");
        int bookId = sc.nextInt();
        sc.nextLine();

        booksDAO.deleteBook(bookId);
    }
    }


