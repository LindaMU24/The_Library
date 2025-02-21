import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoansDAO {

    public void createLoan(Connection connection, int userId, int bookId) throws SQLException {
        String query = "INSERT INTO loans (user_id, book_id, loan_date, return_date) VALUES (?, ?, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 1 MONTH))";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<BorrowedBook> getBorrowedBooksByUser(int userId) throws SQLException {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String query = "SELECT b.*, l.id as loan_id, l.loan_date, l.return_date FROM books b JOIN loans l ON b.id = l.book_id WHERE l.user_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                String genre = rs.getString("genre");
                boolean available = rs.getBoolean("available");

                int loanId = rs.getInt("loan_id");
                Timestamp loanDate = rs.getTimestamp("loan_date");
                Date returnDate = rs.getDate("return_date");

                Books book = new Books(bookId, title, author, year, genre, available);
                Loans loan = new Loans(loanId, userId, bookId, loanDate, returnDate);

                borrowedBooks.add(new BorrowedBook(book, loan));
            }
        }

        return borrowedBooks;
    }

    public boolean isBookBorrowedByUser(int userId, int bookId) throws SQLException {
        String query = "SELECT 1 FROM loans WHERE user_id = ? AND book_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        }
    }

    public void removeLoan(Connection connection, int userId, int bookId) throws SQLException {
        String query = "DELETE FROM loans WHERE user_id = ? AND book_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Logga felet för att hjälpa med felsökning
            throw e; // Kasta vidare undantaget så att det kan hanteras i transaktionen
        }
    }
}
