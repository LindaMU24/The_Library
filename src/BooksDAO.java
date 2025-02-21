import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDAO {

    public List<Books> listAllBooks(boolean isAdmin) throws SQLException {
        List<Books> books = new ArrayList<>();
        String query = "SELECT b.*, l.user_id, u.name AS user_name, l.loan_date, l.return_date \n" +
                "FROM books b \n" +
                "LEFT JOIN loans l ON b.id = l.book_id\n" +
                "LEFT JOIN users u ON l.user_id = u.id";

        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                String genre = rs.getString("genre");
                boolean available = rs.getBoolean("available");
                Books book = new Books(id, title, author, year, genre, available);

                books.add(book); // Lägg till boken i listan oavsett om användaren är admin eller inte
                if (isAdmin) {
                    int userId = rs.getInt("user_id");
                    String userName = rs.getString("user_name");
                    Date loanDate = rs.getDate("loan_date");
                    Date returnDate = rs.getDate("return_date");

                    printLoanInfo(userId, userName, loanDate, returnDate, title, author);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //Hantera undantag på ett lämpligt sätt
        }
        return books;
    }
    private void printLoanInfo(int userId, String userName, Date loanDate, Date returnDate, String title, String author) {
        if (userId != 0 && userName != null) {
            System.out.println("Loaned to user: ID: " + userId + " Name: " + userName);
            System.out.println("Title: " + title + ", Author: " + author);
        }
        if (loanDate != null) {
            System.out.println("Loan date: " + loanDate);
        }
        if (returnDate != null) {
            System.out.println("Return date: " + returnDate);
        }
    }

    public void addNewBook(String title, String author, int year, String genre) throws SQLException {
        String query = "INSERT INTO books (title, author, year, genre, available) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, year);
            pstmt.setString(4, genre);
            pstmt.setBoolean(5, true);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteBook(int id) throws SQLException {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("No book found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isBookAvailable(int bookId) throws SQLException {
        String query = "SELECT available FROM books WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("available");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Returnera false om boken inte hittas eller om ett fel uppstår
    }

    public Books getBookById(int bookId) throws SQLException {
        Books book = null;
        String query = "SELECT * FROM books WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                String genre = rs.getString("genre");
                boolean available = rs.getBoolean("available");

                book = new Books(id, title, author, year, genre, available);
            }
        }
        return book;
    }

    public void updateBookAvailability(Connection connection, int bookId, boolean available) throws SQLException {
        String query = "UPDATE books SET available = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setBoolean(1, available);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Books> searchBookByTitle(String titlePart) throws SQLException {
        List<Books> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE title LIKE ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + titlePart + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                String genre = rs.getString("genre");
                boolean available = rs.getBoolean("available");

                Books book = new Books(id, title, author, year, genre, available);
                books.add(book);
            }
        }
        return books;
    }

    public List<Books> searchBookByAuthor(String authorPart) throws SQLException {
        List<Books> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE author LIKE ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + authorPart + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                String genre = rs.getString("genre");
                boolean available = rs.getBoolean("available");

                Books book = new Books(id, title, author, year, genre, available);
                books.add(book);
            }
        }
        return books;
    }

    public List<String> getAllUniqueGenres() throws SQLException {
        List<String> genres = new ArrayList<>();
        String query = "SELECT DISTINCT genre FROM books";

        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                genres.add(rs.getString("genre"));
            }
        }
        return genres;
    }

    public List<Books> getAllGenres(String genre) throws SQLException {
        List<Books> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE genre = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, genre);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String bookGenre = rs.getString("genre");
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                boolean available = rs.getBoolean("available");

                Books book = new Books(id, title, author, year, genre, available);
                books.add(book);
            }
            return books;
        }
    }
}

