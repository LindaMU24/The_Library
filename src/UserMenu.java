import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


    public class UserMenu {

    private UsersDAO usersDAO = new UsersDAO();
    private BookMenu bookMenu = new BookMenu();
    private LoansDAO loansDAO = new LoansDAO();
    private BooksDAO booksDAO = new BooksDAO();
    private Scanner sc = new Scanner(System.in);
    private boolean isLoggedIn = false;
    private Users loggedInUser;

    public void showMenu() throws SQLException {
        while (true) {
            if (!isLoggedIn) {
                showLoginMenu();
            } else {
                showUserMenu();
            }
            if (!isLoggedIn) {
                break;
            }
        }
    }

    private void showLoginMenu() throws SQLException {

        System.out.println("----- User Menu -----");
        System.out.println("1. Log In");
        System.out.println("2. Register New User");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");

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
                System.out.println("Invalid option, try again.");
        }
    }

    private void showUserMenu() throws SQLException {
        System.out.println("----- User Menu -----");
        System.out.println("1. List Books");
        System.out.println("2. Borrow a Book");
        System.out.println("3. Return a Book");
        System.out.println("4. List My Borrowed Books");
        System.out.println("0. Log Out");
        System.out.print("Choose an option: ");

        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                bookMenu.showBooksOptions(false);
                break;
            case "2":
               borrowBook();
                break;
            case "3":
              returnBook();
                break;
            case "4":
             listBorrowedBooks();
                break;
            case "0":
                logOut();
                break;
            default:
                System.out.println("Invalid option, try again.");
        }
    }
        private void loginUser() throws SQLException {
            String email = "";
            String password = "";

            while (email.isEmpty()) {
                System.out.println("Enter your email:");
                email = sc.nextLine().trim();
                if (email.isEmpty()) {
                    System.out.println("Email cannot be empty. Please try again.");
                }
            }

            while (password.isEmpty()) {
                System.out.println("Enter your password:");
                password = sc.nextLine().trim();
                if (password.isEmpty()) {
                    System.out.println("Password cannot be empty. Please try again.");
                }
            }

            try {
                Users user = usersDAO.userLogin(email, password);
                if (user != null) {
                    System.out.println("Welcome, " + user.getName() + "!");
                    isLoggedIn = true;
                    loggedInUser = user; // Spara den inloggade användaren
                } else {
                    System.out.println("Invalid email or password. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("An error occurred while trying to log in. Please try again later.");
                e.printStackTrace();
            }
        }

    private void registerUser() {
        System.out.println("Registering new user...");
        String name = "";
        String email = "";
        String password = "";


        while (true) {
            System.out.println("Enter your name:");
            name = sc.nextLine();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty! Please try again.");
            } else {
                break;
            }
        }
        while (true) {
            System.out.println("Enter your email:");
            email = sc.nextLine();
            if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                System.out.println("Please enter a valid email address. Try again.");
            } else {
                break;
            }
        }
        while (true) {
            System.out.println("Enter your password:");
            password = sc.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty! Please try again.");
            } else {
                break;
            }
        }
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
    private void listBooks() throws SQLException {
        List<Books> books = booksDAO.listAllBooks(false);
        for (Books book : books) {
            System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() +
                    ", Author: " + book.getAuthor()+ ", Year: " + book.getYear() +
                    ", Genre: " + book.getGenre() +
                    ", Available: " + (book.isAvailable() ? "Yes" : "No"));
        }
    }

    private void borrowBook() throws SQLException {
        if (loggedInUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        listBooks();

        System.out.println("Enter the ID of the book you want to borrow:");
        int bookId = sc.nextInt();
        sc.nextLine();

        Connection connection = null;

        try {
            connection = Database.getConnection();
            connection.setAutoCommit(false);

            Books book = booksDAO.getBookById(bookId);
            if (book != null && book.isAvailable()) {
                // Uppdatera bokens status i databasen
                booksDAO.updateBookAvailability(connection, bookId, false);

                // Lägg till lånet i loans-tabellen
                loansDAO.createLoan(connection, loggedInUser.getId(), bookId);

                connection.commit(); // Bekräfta transaktionen
                System.out.println("You have successfully borrowed the book: " + book.getTitle());
            } else {
                System.out.println("The book is not available for borrowing.");
            }
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Rulla tillbaka transaktionen vid fel
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true); // Återställ autocommit
                connection.close();
            }
        }
    }

    private void returnBook() throws SQLException {
        if (loggedInUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        listBorrowedBooks();

        System.out.println("Enter the ID of the book you want to return:");
        int bookId = sc.nextInt();
        sc.nextLine();

        Connection connection = null;

        try {
            connection = Database.getConnection();
            connection.setAutoCommit(false); // Börja transaktionen

            // Kontrollera om boken är lånad av användaren
            if (loansDAO.isBookBorrowedByUser(loggedInUser.getId(), bookId)) {
                // Ta bort lånet och uppdatera bokens status
                loansDAO.removeLoan(connection, loggedInUser.getId(), bookId);
                booksDAO.updateBookAvailability(connection, bookId, true);

                connection.commit(); // Bekräfta transaktionen
                System.out.println("You have successfully returned the book.");
            } else {
                System.out.println("You have not borrowed this book.");
            }
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Rulla tillbaka vid fel
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true); // Återställ autocommit
                connection.close();
            }
        }
    }
    private void listBorrowedBooks() throws SQLException {
        if (loggedInUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        List<BorrowedBook> borrowedBooks = loansDAO.getBorrowedBooksByUser(loggedInUser.getId());
        if (borrowedBooks.isEmpty()) {
            System.out.println("You have no borrowed books.");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (BorrowedBook borrowedBook : borrowedBooks) {
                Books book = borrowedBook.getBook();
                Loans loan = borrowedBook.getLoan();
                String loanDateFormatted = loan.getLoanDate() != null ? dateFormat.format(loan.getLoanDate()) : "N/A";
                String returnDateFormatted = loan.getReturnDate() != null ? dateFormat.format(loan.getReturnDate()) : "N/A";
                System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() +
                        ", Loan Date: " + loanDateFormatted +
                        ", Return Date: " + returnDateFormatted);
            }
        }
    }

    private void logOut() {
        // Logga ut användaren
        System.out.println("Logging out...");
        isLoggedIn = false;
    }
    }
