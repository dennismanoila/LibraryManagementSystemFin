import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryManager libraryManager = new LibraryManager();

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("load")) {
                libraryManager.loadBooksFromDatabase();
                libraryManager.loadSubscribersFromDatabase();
                System.out.println("Data loaded successfully from the database.");
            } else if (args[0].equalsIgnoreCase("new")) {
                libraryManager.clearDatabase();
                System.out.println("Starting fresh with an empty database.");
            } else {
                System.out.println("Invalid argument. Use 'load' to load existing data or 'new' to start fresh.");
                return;
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Do you want to load existing data or start fresh?");
                System.out.print("Enter 'load' to load data or 'new' to start fresh: ");
                String choice = scanner.nextLine().trim().toLowerCase();

                if (choice.equals("load")) {
                    libraryManager.loadBooksFromDatabase();
                    libraryManager.loadSubscribersFromDatabase();
                    System.out.println("Data loaded successfully from the database.");
                    break;
                } else if (choice.equals("new")) {
                    libraryManager.clearDatabase();
                    System.out.println("Starting fresh with an empty database.");
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'load' or 'new'.");
                }
            }
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMainMenu();

            System.out.print("Choose an option: ");
            int option;
            try {
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.print("Enter book title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter author: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter book ID (positive integer): ");
                        int bookId = scanner.nextInt();
                        scanner.nextLine();
                        libraryManager.addBook(title, author, bookId);
                        break;
                    case 2:
                        System.out.print("Enter subscriber first name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Enter subscriber last name: ");
                        String lastName = scanner.nextLine();
                        libraryManager.addSubscriber(firstName, lastName);
                        break;
                    case 3:
                        libraryManager.userBorrowsBook(scanner);
                        break;
                    case 4:
                        libraryManager.displayBooks();
                        break;
                    case 5:
                        libraryManager.displaySubscribers();
                        break;
                    case 6:
                        libraryManager.sortBooksByTitle();
                        break;
                    case 7:
                        libraryManager.sortSubscribersByName();
                        break;
                    case 8:
                        libraryManager.sortBooksAndSubscribersSimultaneously();
                        break;
                    case 9:
                        libraryManager.groupSubscribersByBorrowedBooks();
                        break;
                    case 10:
                        System.out.println("Exiting. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    public static void displayMainMenu() {
        System.out.println("Library Management System");
        System.out.println("1. Add Book");
        System.out.println("2. Add Subscriber");
        System.out.println("3. User Borrows Book");
        System.out.println("4. Display Books");
        System.out.println("5. Display Subscribers");
        System.out.println("6. Sort Books by Title");
        System.out.println("7. Sort Subscribers by Name");
        System.out.println("8. Sort books and subscribers simultaneously (multithreading)");
        System.out.println("9. Group Subscribers by Borrowed Books");
        System.out.println("10. Exit");
    }
}
