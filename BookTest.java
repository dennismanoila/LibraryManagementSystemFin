import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookConstructor() {
        Book book = new Book("1984", "George Orwell", 1);
        assertEquals("1984", book.getTitle());
        assertEquals("George Orwell", book.getAuthor());
        assertEquals(1, book.getBookId());
    }

    @Test
    void testBookComparison() {
        Book book1 = new Book("1984", "George Orwell", 1);
        Book book2 = new Book("Animal Farm", "George Orwell", 2);

        int comparisonResult = book1.compareTo(book2);
        System.out.println("Comparison Result: " + comparisonResult);

        assertTrue(comparisonResult > 0);
    }


}
