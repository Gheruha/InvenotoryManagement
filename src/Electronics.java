import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*; // SQL
import java.util.logging.Level; // For errors.
import java.util.logging.Logger; // For errors.

public class Electronics extends Item {

    Electronics(int id, String name, int quantity, double price, String quantityMeasure) {
        super(id, name, quantity, price, quantityMeasure);
    }

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Electronics> electronicsList = new ArrayList<>();
    static PreparedStatement sqlPreparedStatement;
    static String output;
    static ResultSet result; // holds the result from SQL

    // ========================= ADD ELECTRONICS =========================

    public static void addElectronic() throws ClassNotFoundException {
        String insertElectronicQuery = "insert into electronics(id, name, quantity, price, quantityMeasure) values (? , ? , ? , ? , ?)";
        String quantityMeasure = "pieces";
        try {
            while (true) {
                System.out.println(
                        "-------------------------------------------------------------------------------------");
                System.out.print("     Enter electronic ID (or -1 to stop): ");
                int electronicId = scanner.nextInt();

                // Check if the user wants to stop
                if (electronicId == -1) {
                    break;
                }

                scanner.nextLine(); // Consume the newline character left by nextInt()
                System.out.print("     Enter electronic name: ");
                String electronicName = scanner.nextLine();
                System.out.print("     Enter electronic quantity: ");
                int quantity = scanner.nextInt();
                System.out.print("     Enter electronic price: ");
                double price = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline character left by nextDouble()

                // Execute query and hold it in the resultSet variable

                int result = executeUpdate(insertElectronicQuery, electronicId, electronicName, quantity, price,
                        quantityMeasure);

                if (result > 0) {
                    System.out.println("     Electronic added successfully!");

                } else {
                    System.out.println("     Failed to add electronic.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // ========================= SEE ELECTRONICS =========================
    public static void seeElectronics() throws SQLException, ClassNotFoundException {
        String selectAll = "select * from electronics;";

        System.out.println(
                "-------------------------------------------------------------------------------------");
        try (
                ResultSet result = executeTheQuery(selectAll)) {

            System.out.println("                                  ELECTRONICS LIST\n\n");
            System.out.printf("%-5s%-20s%-10s%-10s%-15s\n", "ID", "NAME", "QUANTITY", "PRICE", "MEASURE");
            while (result.next()) {
                int dataId = result.getInt("id");
                String dataName = result.getString("name");
                int dataQuantity = result.getInt("quantity");
                double dataPrice = result.getInt("price");
                String dataQuantityMeasure = result.getString("quantityMeasure");

                System.out.printf("%-5d%-20s%-10d%-10.2f%-15s\n", dataId, dataName, dataQuantity, dataPrice,
                        dataQuantityMeasure);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================= CHANGE ELECTRONICS QUANTITY =======================
    public static void changeElectronicsQuantity() throws ClassNotFoundException {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("                     CHANGE QUANTITY ELECTRONICS\n\n");
        System.out.print("     Enter the ID of the electronic item whose quantity you want to change: ");

        int electronicId = scanner.nextInt();
        int quantity = 0; // Initialize quantity
        String query = "select * from electronics where id = ?";
        String changeQuantityQuery = "update electronics set quantity = ? where id = ?";

        // SHOW THE FOOD CHOSEN
        try (ResultSet result = executeTheQuery(query, electronicId)) {

            System.out.println("                                  \n                           ELECTRONIC CHOSEN\n\n");
            System.out.printf("     %-5s%-20s%-10s%-10s%-15s\n", "ID", "NAME", "QUANTITY", "PRICE", "MEASURE");
            while (result.next()) {
                int dataId = result.getInt("id");
                String dataName = result.getString("name");
                int dataQuantity = result.getInt("quantity");
                double dataPrice = result.getInt("price");
                String dataQuantityMeasure = result.getString("quantityMeasure");

                System.out.printf("     %-5d%-20s%-10d%-10.2f%-15s\n", dataId, dataName, dataQuantity, dataPrice,
                        dataQuantityMeasure);

                // CHANGE THE QUANTITY
                System.out.println("\n\n     1.Add quantity| 2.Decrease quantity | 0.EXIT");
                System.out.print("     Enter your choice: ");
                int choice = scanner.nextInt();

                if (choice == 0) {
                    break;
                }

                if (choice == 1) {
                    System.out.print("     Enter the quantity to add: ");
                    int addQuantity = scanner.nextInt();
                    quantity = dataQuantity + addQuantity;
                }

                if (choice == 2) {
                    System.out.print("     Enter the quantity to decrease: ");
                    int decreaseQuantity = scanner.nextInt();
                    quantity = dataQuantity - decreaseQuantity;
                }

                try {
                    int updateResult = executeUpdate(changeQuantityQuery, quantity, electronicId);
                    System.out.println("     Now the quantity of " + dataName + " is: " + quantity + " pieces");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========================= CHANGE ELECTRONICS PRICE =========================
    public static void changeElectronicsPrice() throws ClassNotFoundException {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("                     CHANGE PRICE ELECTRONICS\n\n");
        System.out.print("     Enter the ID of the electronic item whose price you want to change: ");

        int electronicId = scanner.nextInt();
        double price = 0; // Initialize quantity
        String query = "select * from electronics where id = ?";
        String changePriceQuery = "update electronics set price = ? where id = ?";

        // SHOW THE FOOD CHOSEN
        try (ResultSet result = executeTheQuery(query, electronicId)) {

            System.out.println("                                  \n                           ELECTRONIC CHOSEN\n\n");
            System.out.printf("     %-5s%-20s%-10s%-10s%-15s\n", "ID", "NAME", "QUANTITY", "PRICE", "MEASURE");
            while (result.next()) {
                int dataId = result.getInt("id");
                String dataName = result.getString("name");
                int dataQuantity = result.getInt("quantity");
                double dataPrice = result.getInt("price");
                String dataQuantityMeasure = result.getString("quantityMeasure");

                System.out.printf("     %-5d%-20s%-10d%-10.2f%-15s\n", dataId, dataName, dataQuantity, dataPrice,
                        dataQuantityMeasure);

                // CHANGE THE QUANTITY
                System.out.println("\n\n     1.Set Price| 0.EXIT");
                System.out.print("     Enter your choice: ");
                int choice = scanner.nextInt();

                if (choice == 0) {
                    break;
                }

                if (choice == 1) {
                    System.out.print("     Enter the price to set: ");
                    double setPrice = scanner.nextDouble();
                    price = setPrice;
                }

                try {
                    int updateResult = executeUpdate(changePriceQuery, price, electronicId);
                    System.out.println("     Now the price of " + dataName + " is: " + price + " $");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========================= DELETE FOOD =========================
    public static void deleteElectronic() throws ClassNotFoundException {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("                     DELETE ELECTRONICS\n\n");
        System.out.print("     Enter the ID of the electronic item that you want to delete: ");

        int electronicId = scanner.nextInt();
        String query = "select * from electronics where id = ?";
        String deleteQuare = "delete from electronics where id = ?";

        // SHOW THE FOOD CHOSEN
        try (ResultSet result = executeTheQuery(query, electronicId)) {

            System.out.println("                                  \n                           ELECTRONIC CHOSEN\n\n");
            System.out.printf("     %-5s%-20s%-10s%-10s%-15s\n", "ID", "NAME", "QUANTITY", "PRICE", "MEASURE");
            while (result.next()) {
                int dataId = result.getInt("id");
                String dataName = result.getString("name");
                int dataQuantity = result.getInt("quantity");
                double dataPrice = result.getInt("price");
                String dataQuantityMeasure = result.getString("quantityMeasure");

                System.out.printf("     %-5d%-20s%-10d%-10.2f%-15s\n", dataId, dataName, dataQuantity, dataPrice,
                        dataQuantityMeasure);

                // CHANGE THE QUANTITY
                System.out.println("\n\n     1.Delete Item| 0.EXIT");
                System.out.print("     Enter your choice: ");
                int choice = scanner.nextInt();

                if (choice == 0) {
                    break;
                }

                if (choice == 1) {
                    try {
                        int updateResult = executeUpdate(deleteQuare, electronicId);
                        System.out.println("You deleted the " + dataName + " from database.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
