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
        String titlePart = "";

        // Loop för att säkerställa att användaren skriver in något
        while (titlePart.isEmpty()) {
            System.out.println("Enter search criteria (part of the title):");
            titlePart = sc.nextLine().trim();  // trim() tar bort överflödiga mellanslag
            if (titlePart.isEmpty()) {
                System.out.println("Search criteria cannot be empty! Please try again.");
            }
        }

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
        String authorPart = "";
        while (authorPart.isEmpty()) {
            System.out.println("Enter search criteria (part of the Authors name):");
            authorPart = sc.nextLine().trim();
            if (authorPart.isEmpty()) {
                System.out.println("Search criteria cannot be empty! Please try again.");
            }
        }

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
            System.out.println("- " + genre);
        }

        String selectedGenre = "";
        while (selectedGenre.isEmpty()) {
            System.out.print("Enter part of the genre: ");
            selectedGenre = sc.nextLine().trim();

            selectedGenre = selectedGenre.replaceAll("^-\\s*", "");

            List<Books> booksInGenre = booksDAO.getAllGenres(selectedGenre);

            if (booksInGenre.isEmpty()) {
                System.out.println("No books found in the selected genre.");
            } else {
                System.out.println("Books in the selected genre:");
                for (Books book : booksInGenre) {
                    System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() +
                            ", Year: " + book.getYear() + ", Genre: " + book.getGenre() +
                            ", Available: " + (book.isAvailable() ? "Yes" : "No"));
                }
            }
        }
    }
}
