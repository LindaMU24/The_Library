public class BorrowedBook {
    private Books book;
    private Loans loan;

    public BorrowedBook(Books book, Loans loan) {
        this.book = book;
        this.loan = loan;
    }

    public Books getBook() {
        return book;
    }

    public void setBook(Books book) {
        this.book = book;
    }

    public Loans getLoan() {
        return loan;
    }

    public void setLoan(Loans loan) {
        this.loan = loan;
    }
    @Override
    public String toString() {
        return "Title: " + book.getTitle() +
                ", Author: " + book.getAuthor() +
                ", Loan Date: " + loan.getLoanDate() +
                ", Return Date: " + loan.getReturnDate();
    }
}
