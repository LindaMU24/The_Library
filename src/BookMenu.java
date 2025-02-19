import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BookMenu {
    private BooksDAO booksDAO = new BooksDAO();
    private Scanner sc = new Scanner(System.in);

    public void showBooksOptions(boolean isAdmin) throws SQLException {
        System.out.println("----- Book Options -----");
        System.out.println("1. List all books");
        System.out.println("2. Search for a book");
        System.out.println("0. Back to Main Menu");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                List<Books> books = booksDAO.listAllBooks(isAdmin);
                for (Books book : books) {
                    System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor());
                    // Lägg till mer information om du vill
                }
                break;
            case "2":
                System.out.println("Enter book title:");
                String title = sc.nextLine();
               // booksDAO.searchBookByTitle(title);
                break;
                case "0":
                    return;
            default:
                System.out.println("Invalid choice, please try again.");
        }
    }
}

