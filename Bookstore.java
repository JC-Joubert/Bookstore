import java.sql.*;
import java.util.Scanner;

public class Bookstore {

    private static void resultSetPrinter(ResultSet rset) {
        try {
            if (rset.next()) {
                do {
                    // Get information on books
                    int id = rset.getInt("id");
                    String title = rset.getString("title");
                    String author = rset.getString("author");
                    int qty = rset.getInt("qty");

                    // Display records
                    System.out.println("ID: " + id
                            + "\nTitle: " + title
                            + "\nAuthor: " + author
                            + "\nQuantity: " + qty
                            + "\n");

                } while (rset.next());

            } else {
                System.out.println("No records found.\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void displayBooks(Statement stmt) {
        try {
            // Execute a SQL select query, the query result is returned in a ResultSet object
            String strSelect = "select * from books";

            ResultSet rset = stmt.executeQuery(strSelect);

            System.out.println("Records of all books: ");
            int rowCount = 0;

            // Get information on books
            while (rset.next()) {
                int id = rset.getInt("id");
                String title = rset.getString("title");
                String author = rset.getString("author");
                int qty = rset.getInt("qty");
                ++rowCount;

                // Display records
                System.out.println(id + ", " + title + ", " + author + ", " + qty);
            }

            System.out.println("\nTotal number of records = " + rowCount + "\n");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void addBook(Statement stmt, Scanner input) {
        try {
            // Collect information from user
            input.nextLine();
            System.out.println("Please enter the Title of the book you would like to add: ");
            String title = input.nextLine();
            System.out.println("Please enter the Author of the book: ");
            String author = input.nextLine();
            System.out.println("Please enter the quantity in stock of the book: ");
            int qty = input.nextInt();

            // Automatically assigns the book ID as 1 above the current max
            String strMaxID = "select max(id) from books";
            ResultSet rset = stmt.executeQuery(strMaxID);
            rset.next();
            int id = rset.getInt(1) + 1;

            // Insert single quotes around string values
            title = "'" + title + "'";
            author = "'" + author + "'";

            // Insert book into system
            String strInsert = "insert into books "
                    + "values(" + id + ", " + title + ", " + author + ", " + qty + ")";
            int countInserted = stmt.executeUpdate(strInsert);
            System.out.println(countInserted + " records entered. \n");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void updateBook(Statement stmt, Scanner input) {
        try {
            // Collect information for book to be updated
            displayBooks(stmt);
            System.out.println("Please enter the ID of the book you would like to update: ");
            int id = input.nextInt();
            System.out.println("\n");

            int selection = 0;

            while (selection != 4) {
                // Display information on selected book
                System.out.println("Selected book:");
                String strDisplay = "select * from books where id = " + id;
                ResultSet rset = stmt.executeQuery(strDisplay);
                resultSetPrinter(rset);

                // Display a menu of fields the user can edit and get selection
                System.out.println("Enter corresponding number of the field you would like to change:\n"
                        + "1. Title\n"
                        + "2. Author\n"
                        + "3. Quantity\n"
                        + "4. Return");
                selection = input.nextInt();

                // Update title
                if (selection == 1) {
                    input.nextLine();
                    System.out.println("Please enter the new title: ");
                    String newTitle = "'" + input.nextLine() + "'";

                    String strUpdate = "update books "
                            + "set title = " + newTitle
                            + " where id = " + id;

                    int countUpdate = stmt.executeUpdate(strUpdate);
                    System.out.println(countUpdate + " records affected.\n");

                // Update author
                } else if (selection == 2) {
                    input.nextLine();
                    System.out.println("Please enter the new author: ");
                    String newAuthor = "'" + input.nextLine() + "'";

                    String strUpdate = "update books "
                            + "set author = " + newAuthor
                            + " where id = " + id;

                    int countUpdate = stmt.executeUpdate(strUpdate);
                    System.out.println(countUpdate + " records affected.\n");

                // Update quantity
                } else if (selection == 3) {
                    input.nextLine();
                    System.out.println("Please enter the new quantity: ");
                    int newQty = input.nextInt();

                    String strUpdate = "update books "
                            + "set qty = " + newQty
                            + " where id = " + id;

                    int countUpdate = stmt.executeUpdate(strUpdate);
                    System.out.println(countUpdate + " records affected.\n");

                // Return to main menu
                } else if (selection == 4) {
                    System.out.println("Returning to main menu.\n");

                // Invalid entry handler
                } else {
                    System.out.println("Invalid Selection.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void deleteBook(Statement stmt, Scanner input) {
        try {
            // Collect information on book to be deleted
            System.out.println("Please enter ID of the book you would like to delete: ");
            int id = input.nextInt();

            // Execute SQL delete update
            String strDelete = "delete from books "
                    + "where id = " + id;

            int countDeleted = stmt.executeUpdate(strDelete);
            if (countDeleted == 1) {
                System.out.println(countDeleted + " record deleted.\n");

            } else {
                System.out.println("No records deleted.\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void searchBook(Statement stmt, Scanner input) {
        try {
            int selection = 0;
            while (selection != 5) {
                // Display menu of fields the user can search for and get selection
                System.out.println("Enter corresponding number for the field you would like to search for; \n"
                        + "1. ID\n"
                        + "2. Title\n"
                        + "3. Author\n"
                        + "4. Quantity\n"
                        + "5. Return\n");
                selection = input.nextInt();

                // Search via ID
                if (selection == 1) {
                    System.out.println("Enter the book ID to search for: ");
                    int bookSearch = input.nextInt();

                    String strSelect = "select * from books where id = " + bookSearch;
                    ResultSet rset = stmt.executeQuery(strSelect);

                    System.out.println("Results of given ID: ");
                    resultSetPrinter(rset);

                // Search via partial or full title
                } else if (selection == 2) {
                    input.nextLine();
                    System.out.println("Enter full or partial title of the book to search for: ");
                    String bookSearch = input.nextLine();

                    String strSelect = "select * from books where title like '%" + bookSearch + "%'";
                    ResultSet rset = stmt.executeQuery(strSelect);

                    System.out.println("Results containing given title: ");
                    resultSetPrinter(rset);

                // Search via partial or full author name
                } else if (selection == 3) {
                    input.nextLine();
                    System.out.println("Enter full or partial name of author to search for: ");
                    String bookSearch = input.nextLine();

                    String strSelect = "select * from books where author like '%" + bookSearch + "%'";
                    ResultSet rset = stmt.executeQuery(strSelect);

                    System.out.println("Results for given author: ");
                    resultSetPrinter(rset);

                // Search via quantity
                } else if (selection == 4) {
                    System.out.println("Enter a quantity to search for: ");
                    int bookSearch = input.nextInt();

                    String strSelect = "select * from books where qty <= " + bookSearch;
                    ResultSet rset = stmt.executeQuery(strSelect);

                    System.out.println("Results for quantity of or below the given quantity: ");
                    resultSetPrinter(rset);

                // Return to main menu
                } else if (selection == 5) {
                    System.out.println("Returning to main menu.\n");

                // Invalid entry handler
                } else {
                    System.out.println("Invalid Selection.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (
                // Allocate a database 'Connection' object
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/ebookstore?useSSL=false",
                        "jaco",
                        "jacobus3991");

                // Allocate a 'Statement' object in Connection
                Statement stmt = conn.createStatement()
        ) {

            Scanner input = new Scanner(System.in);
            int selection = 0;

            // Menu for user action input
            while (selection != 6) {
                System.out.println("Enter the number corresponding to the action: \n"
                        + "1. Enter new book\n"
                        + "2. Update book\n"
                        + "3. Delete book\n"
                        + "4. Search books\n"
                        + "5. Show all books\n"
                        + "6. Exit");
                selection = input.nextInt();

                // Selections
                // Call method to add book
                if (selection == 1) {
                    addBook(stmt, input);

                // Call method to update book
                } else if (selection == 2) {
                    updateBook(stmt, input);

                // Call method to delete book
                } else if (selection == 3) {
                    deleteBook(stmt, input);

                // Call method to search for book
                } else if (selection == 4) {
                    searchBook(stmt, input);

                // Call method to display all books
                } else if (selection == 5) {
                    displayBooks(stmt);

                // Quit program
                } else if (selection == 6) {
                    System.out.println("Goodbye");
                    input.close();

                // Invalid entry handler
                } else {
                    System.out.println("Invalid selection.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}