import java.io.Serializable;

public class Book implements Serializable, Comparable<Book> {
    private String title;
    private String author;
    private int bookId;

    public Book(String title, String author, int bookId) {
        this.title = title;
        this.author = author;
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getBookId() {
        return bookId;
    }

    @Override
    public int compareTo(Book other) {
        boolean thisIsNumeric = this.title.matches("^\\d.*");
        boolean otherIsNumeric = other.title.matches("^\\d.*");

        if (thisIsNumeric && !otherIsNumeric) {
            return 1;
        } else if (!thisIsNumeric && otherIsNumeric) {
            return -1;
        } else {
            return this.title.trim().toLowerCase().compareTo(other.title.trim().toLowerCase());
        }
    }



}
