import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    @Test
    void testLibraryConstructor() {
        Library library = new Library("City Library");
        assertEquals("City Library", library.getLibraryName());
    }
}
