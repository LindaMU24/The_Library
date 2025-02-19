import java.sql.Date;
import java.sql.Timestamp;

public class Loans {

    private int id;
    private int userId;
    private int bookId;
    private Timestamp loanDate;
    private Date returnDate;

    public Loans(int id, int userId, int bookId, Timestamp loanDate, Date returnDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Timestamp getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Timestamp loanDate) {
        this.loanDate = loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", loanDate=" + loanDate +
                ", returnDate=" + returnDate;
    }
}