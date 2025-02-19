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

                if (isAdmin) {
                    int userId = rs.getInt("user_id");
                    String userName = rs.getString("user_name");
                    Date loanDate = rs.getDate("loan_date");
                    Date returnDate = rs.getDate("return_date");

                    if (userId != 0 && userName != null) {
                        System.out.println("Loaned to user: ID: " + userId + " Name: " + userName);
                    }
                    if (loanDate != null) {
                        System.out.println("Loan date: " + loanDate);
                    }
                    if (returnDate != null) {
                        System.out.println("Return date: " + returnDate);
                    }
                }

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Hantera undantag på ett lämpligt sätt
        }
        return books;
    }

    public void addNewBook(String title, String author, int year, String genre) throws SQLException {
        String query = "INSERT INTO books (title, author, year, genre, available) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, year);
            pstmt.setString(4, genre);
            pstmt.setBoolean(5, true); // Nya böcker är tillgängliga som standard
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
    }
