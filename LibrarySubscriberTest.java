import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibrarySubscriberTest {

    @Test
    void testLibrarySubscriberConstructor() {
        Library library = new Library("City Library");
        LibrarySubscriber subscriber = new LibrarySubscriber(library, "Jane", "Doe");
        assertEquals("Jane Doe", subscriber.getFullName());
        assertTrue(subscriber.getBorrowedBooks().isEmpty());
    }

    @Test
    void testLibrarySubscriberAddBook() {
        Library library = new Library("City Library");
        LibrarySubscriber subscriber = new LibrarySubscriber(library, "Jane", "Doe");
        Book book = new Book("1984", "George Orwell", 1);

        subscriber.addBook(book);
        assertEquals(1, subscriber.getBorrowedBookCount());
        assertTrue(subscriber.getBorrowedBooks().contains(book));
    }

    @Test
    void testLibrarySubscriberBorrowItem() {
        Library library = new Library("City Library");
        LibrarySubscriber subscriber = new LibrarySubscriber(library, "Jane", "Doe");

        subscriber.borrowItem(); 
    }
}
