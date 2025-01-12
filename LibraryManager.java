import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class LibraryManager {
    private List<Book> books;
    private List<LibrarySubscriber> subscribers;

    public LibraryManager() {
        books = new ArrayList<>();
        subscribers = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<LibrarySubscriber> getSubscribers() {
        return subscribers;
    }

    public void loadBooksFromDatabase() {
        books.clear();
        String query = "SELECT * FROM Books";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                books.add(new Book(title, author, id));
            }

            System.out.println("Books loaded from the database successfully.");

        } catch (Exception e) {
            System.out.println("Error loading books from the database.");
            e.printStackTrace();
        }
    }

    public void loadSubscribersFromDatabase() {
        subscribers.clear();
        String query = "SELECT * FROM LibrarySubscribers";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                subscribers.add(new LibrarySubscriber(new Library("City Library"), firstName, lastName));
            }

            System.out.println("Subscribers loaded from the database successfully.");

        } catch (Exception e) {
            System.out.println("Error loading subscribers from the database.");
            e.printStackTrace();
        }
    }

    public void clearDatabase() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM BorrowedBooks");
            statement.executeUpdate("DELETE FROM Books");
            statement.executeUpdate("DELETE FROM LibrarySubscribers");

            System.out.println("Database cleared successfully. Starting fresh.");

        } catch (Exception e) {
            System.out.println("Error clearing the database.");
            e.printStackTrace();
        }
    }

    public void addBook(String title, String author, int bookId) {

        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty()) {
            System.out.println("Error: Title and Author must not be empty.");
            return;
        }

        if (bookId <= 0) {
            System.out.println("Error: Book ID must be a positive number.");
            return;
        }

        String checkQuery = "SELECT COUNT(*) FROM Books WHERE id = ?";
        String query = "INSERT INTO Books (id, title, author) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, bookId);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                System.out.println("Error: A book with this ID already exists.");
                return;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, bookId);
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, author);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Book added successfully.");
                }

                loadBooksFromDatabase();

            } catch (Exception e) {
                System.out.println("Error adding book to the database.");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Error checking book ID before insertion.");
            e.printStackTrace();
        }
    }

    public void addSubscriber(String firstName, String lastName) {

        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            System.out.println("Error: First and Last name must not be empty.");
            return;
        }

        if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
            System.out.println("Error: Names should only contain letters.");
            return;
        }

        String checkQuery = "SELECT COUNT(*) FROM LibrarySubscribers WHERE first_name = ? AND last_name = ?";
        String query = "INSERT INTO LibrarySubscribers (first_name, last_name) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            checkStmt.setString(1, firstName);
            checkStmt.setString(2, lastName);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                System.out.println("Error: A subscriber with this name already exists.");
                return;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Subscriber added successfully.");
                }

                loadSubscribersFromDatabase();

            } catch (Exception e) {
                System.out.println("Error adding subscriber to the database.");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Error checking subscriber before insertion.");
            e.printStackTrace();
        }
    }

    public void displayBooks() {
        String query = "SELECT * FROM Books";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Books in Library:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                System.out.println("ID: " + id + ", Title: " + title + ", Author: " + author);
            }

        } catch (Exception e) {
            System.out.println("Error fetching books from the database.");
            e.printStackTrace();
        }
    }

    public void displaySubscribers() {
        String query = "SELECT first_name, last_name FROM LibrarySubscribers";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Library Subscribers:");
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                System.out.println("Name: " + firstName + " " + lastName);
            }

        } catch (Exception e) {
            System.out.println("Error fetching subscribers from the database.");
            e.printStackTrace();
        }
    }

    public void sortBooksByTitle() {
        if (books.isEmpty()) {
            System.out.println("Error: No books available to sort.");
            return;
        }

        books.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
        System.out.println("Books sorted by title:");
        for (Book book : books) {
            System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor());
        }
    }

    public void sortSubscribersByName() {

        if (subscribers.isEmpty()) {
            System.out.println("Error: No subscribers available to sort.");
            return;
        }

        subscribers.sort((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName()));
        System.out.println("Subscribers sorted by name:");
        for (LibrarySubscriber subscriber : subscribers) {
            System.out.println("Name: " + subscriber.getFullName());
        }
    }

    public void userBorrowsBook(Scanner scanner) {
        List<String> subscriberNames = new ArrayList<>();
        String subscriberQuery = "SELECT first_name, last_name FROM LibrarySubscribers";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet subscriberResultSet = statement.executeQuery(subscriberQuery)) {

            System.out.println("Select a subscriber:");
            int count = 1;

            while (subscriberResultSet.next()) {
                String firstName = subscriberResultSet.getString("first_name");
                String lastName = subscriberResultSet.getString("last_name");
                String fullName = firstName + " " + lastName;

                subscriberNames.add(fullName);
                System.out.println(count++ + ". " + fullName);
            }

            if (subscriberNames.isEmpty()) {
                System.out.println("No subscribers available.");
                return;
            }

            int subscriberChoice;
            while (true) {
                System.out.print("Enter subscriber number: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                    continue;
                }
                subscriberChoice = scanner.nextInt() - 1;
                scanner.nextLine();
                if (subscriberChoice >= 0 && subscriberChoice < subscriberNames.size()) {
                    break;
                }
                System.out.println("Invalid selection. Try again.");
            }

            String selectedSubscriberName = subscriberNames.get(subscriberChoice);
            String[] subscriberNameParts = selectedSubscriberName.split(" ", 2);
            String selectedFirstName = subscriberNameParts[0];
            String selectedLastName = subscriberNameParts[1];

            List<Integer> bookIds = new ArrayList<>();
            List<String> bookTitles = new ArrayList<>();
            String bookQuery = "SELECT * FROM Books";

            try (ResultSet bookResultSet = statement.executeQuery(bookQuery)) {
                System.out.println("Select a book to borrow:");
                count = 1;

                while (bookResultSet.next()) {
                    int id = bookResultSet.getInt("id");
                    String title = bookResultSet.getString("title");
                    String author = bookResultSet.getString("author");

                    bookIds.add(id);
                    bookTitles.add(title + " by " + author);

                    System.out.println(count++ + ". " + title + " by " + author);
                }

                if (bookIds.isEmpty()) {
                    System.out.println("No books available to borrow.");
                    return;
                }

                int bookChoice;
                while (true) {
                    System.out.print("Enter book number: ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.next();
                        continue;
                    }
                    bookChoice = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (bookChoice >= 0 && bookChoice < bookIds.size()) {
                        break;
                    }
                    System.out.println("Invalid selection. Try again.");
                }

                int selectedBookId = bookIds.get(bookChoice);
                String selectedBookTitle = bookTitles.get(bookChoice);

                String checkBorrowedQuery = "SELECT * FROM BorrowedBooks WHERE subscriber_id = " +
                        "(SELECT id FROM LibrarySubscribers WHERE first_name = ? AND last_name = ?) AND book_id = ?";

                try (PreparedStatement checkStatement = connection.prepareStatement(checkBorrowedQuery)) {
                    checkStatement.setString(1, selectedFirstName);
                    checkStatement.setString(2, selectedLastName);
                    checkStatement.setInt(3, selectedBookId);

                    ResultSet checkResultSet = checkStatement.executeQuery();
                    if (checkResultSet.next()) {
                        System.out.println("Error: Subscriber has already borrowed this book.");
                        return;
                    }
                }

                String borrowQuery = "INSERT INTO BorrowedBooks (subscriber_id, book_id) VALUES (" +
                        "(SELECT id FROM LibrarySubscribers WHERE first_name = ? AND last_name = ?), ?)";

                try (PreparedStatement borrowStatement = connection.prepareStatement(borrowQuery)) {
                    borrowStatement.setString(1, selectedFirstName);
                    borrowStatement.setString(2, selectedLastName);
                    borrowStatement.setInt(3, selectedBookId);

                    int rowsInserted = borrowStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println(selectedSubscriberName + " has borrowed \"" + selectedBookTitle + "\"");
                    }
                } catch (Exception e) {
                    System.out.println("Error recording borrowing transaction.");
                    e.printStackTrace();
                }

            } catch (Exception e) {
                System.out.println("Error fetching available books.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("Error fetching subscribers.");
            e.printStackTrace();
        }
    }


    public void groupSubscribersByBorrowedBooks() {
        String query = "SELECT s.first_name, s.last_name, COUNT(b.book_id) AS borrowed_count " +
                "FROM LibrarySubscribers s " +
                "LEFT JOIN BorrowedBooks b ON s.id = b.subscriber_id " +
                "GROUP BY s.id " +
                "ORDER BY borrowed_count DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Subscribers grouped by borrowed books:");
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int borrowedCount = resultSet.getInt("borrowed_count");

                System.out.println("Name: " + firstName + " " + lastName + " - Borrowed Books: " + borrowedCount);
            }

        } catch (Exception e) {
            System.out.println("Error grouping subscribers by borrowed books.");
            e.printStackTrace();
        }
    }


    public void sortBooksAndSubscribersSimultaneously() {
        Thread bookSortingThread = new Thread(this::sortBooksByTitle);
        Thread subscriberSortingThread = new Thread(this::sortSubscribersByName);

        bookSortingThread.start();
        subscriberSortingThread.start();

        try {
            bookSortingThread.join();
            subscriberSortingThread.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        System.out.println("Books and subscribers sorted simultaneously.");
    }
}
