import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BookMenu {
    private BooksDAO booksDAO = new BooksDAO();
    private Scanner sc = new Scanner(System.in);

    public void showBooksOptions(boolean isAdmin) throws SQLException {
        while (true) {
            System.out.println("----- Book Options -----");
            System.out.println("1. List all books");
            System.out.println("2. Search for a book");
            System.out.println("0. Back to the Menu");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    List<Books> books = booksDAO.listAllBooks(isAdmin);
                    for (Books book : books) {
                        System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Available: " + (book.isAvailable() ? "Yes" : "No"));
                        if (isAdmin) {
                        }
                    }
                    break;
                case "2":
                    searchForBookMenu();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void searchForBookMenu() throws SQLException {
        System.out.println("Choose search criteria:");
        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.println("3. Genre");
        System.out.print("Enter your choice: ");

        String choice = sc.nextLine();
        switch (choice) {
            case "1":
                searchTitle();
                break;
            case "2":
                searchAuthor();
                break;
            case "3":
                listGenres();
                break;
            default:
                System.out.println("Invalid option, try again.");
        }
    }

    private void searchTitle() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter search criteria (part of the title):");
        String titlePart = sc.nextLine();

        BooksDAO booksDAO = new BooksDAO();
        List<Books> matchingBooks = booksDAO.searchBookByTitle(titlePart);

        if (matchingBooks.isEmpty()) {
            System.out.println("No books found matching the criteria.");
        } else {
            System.out.println("Books found:");
            for (Books book : matchingBooks) {
                System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() +
                        ", Year: " + book.getYear() + ", Genre: " + book.getGenre() +
                        ", Available: " + (book.isAvailable() ? "Yes" : "No"));
            }
        }
    }

    private void searchAuthor() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter search criteria (part of the Authors name):");
        String authorPart = sc.nextLine();

        BooksDAO booksDAO = new BooksDAO();
        List<Books> matchingBooks = booksDAO.searchBookByAuthor(authorPart);

        if (matchingBooks.isEmpty()) {
            System.out.println("No books found matching the criteria.");
        } else {
            System.out.println("Books found:");
            for (Books book : matchingBooks) {
                System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() +
                        ", Year: " + book.getYear() + ", Genre: " + book.getGenre() +
                        ", Available: " + (book.isAvailable() ? "Yes" : "No"));
            }
        }
    }

    private void listGenres() throws SQLException {
        BooksDAO booksDAO = new BooksDAO();
        List<String> genres = booksDAO.getAllUniqueGenres();
        System.out.println("Available genres:");
        for (String genre : genres) {
            System.out.println(genre);
        }

        System.out.print("Enter genre: ");
        String selectedGenre = sc.nextLine();
        List<Books> booksInGenre = booksDAO.getAllGenres(selectedGenre);

        System.out.println("Books in the selected genre:");
        for (Books book : booksInGenre) {
            System.out.println(book);
        }
    }
}
