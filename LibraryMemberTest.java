import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibraryMemberTest {

    @Test
    void testLibraryMemberConstructor() {
        Library library = new Library("City Library");
        LibraryMember member = new LibraryMember(library, "John", "Doe");
        assertEquals("John Doe", member.getFullName());
        assertEquals("City Library", member.getLibrary().getLibraryName());
    }
}
