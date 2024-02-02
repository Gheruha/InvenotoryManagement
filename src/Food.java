import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.sql.*; // SQL
import java.util.logging.Level; // For errors.
import java.util.logging.Logger; // For errors.

public class Food extends Item {

    Food(int id, String name, int quantity, double price, String quantityMeasure, int quantityToDelete) {
        super(id, name, quantity, price, quantityMeasure);
    }

    static ArrayList<Food> foodList = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Electronics> electronicsList = new ArrayList<>();
    static PreparedStatement sqlPreparedStatement;
    static String output;
    static ResultSet result; // holds the result from SQL

    // ========================= ADD FOOD IN THE DATABASE =========================

    public static void addFood() throws ClassNotFoundException {
        String insertFoodQuery = "insert into food(id, name, quantity, price, quantityMeasure) values (? , ? , ? , ? , ?)";
        String quantityMeasure = "g";
        try {
            while (true) {
                System.out.println(
                        "-------------------------------------------------------------------------------------");
                System.out.print("     Enter food ID (or -1 to stop): ");
                int foodId = scanner.nextInt();

                // Check if the user wants to stop
                if (foodId == -1) {
                    break;
                }

                scanner.nextLine(); // Consume the newline character left by nextInt()
                System.out.print("     Enter food name: ");
                String foodName = scanner.nextLine();
                System.out.print("     Enter quantity: ");
                int quantity = scanner.nextInt();
                System.out.print("     Enter price: ");
                double price = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline character left by nextDouble()

                // Execute query and hold it in the resultSet variable

                int result = executeUpdate(insertFoodQuery, foodId, foodName, quantity, price, quantityMeasure);

                if (result > 0) {
                    System.out.println("     Food added successfully!");

                } else {
                    System.out.println("     Failed to add food.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // ========================= SEE FOOD FROM DATABASE =========================
    public static void seeFood() throws SQLException, ClassNotFoundException {
        String selectAll = "select * from food;";

        System.out.println(
                "-------------------------------------------------------------------------------------");
        try (
                ResultSet result = executeTheQuery(selectAll)) {

            System.out.println("                                  FOOD LIST\n\n");
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

    // ========================= CHANGE FOOD QUANTITY =========================
    public static void changeFoodQuantity() throws ClassNotFoundException {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("                     CHANGE QUANTITY FOOD\n\n");
        System.out.print("     Enter the ID of the food item whose quantity you want to change: ");

        int foodId = scanner.nextInt();
        int quantity = 0; // Initialize quantity
        String query = "select * from food where id = ?";
        String changeQuantityQuery = "update food set quantity = ? where id = ?";

        // SHOW THE FOOD CHOSEN
        try (ResultSet result = executeTheQuery(query, foodId)) {

            System.out.println("                                  \n                           FOOD CHOSEN\n\n");
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
                    int updateResult = executeUpdate(changeQuantityQuery, quantity, foodId);
                    System.out.println("     Now the quantity of " + dataName + " is: " + quantity + "g");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========================= CHANGE FOOD PRICE =========================
    public static void changeFoodPrice() throws ClassNotFoundException {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("                     CHANGE PRICE FOOD\n\n");
        System.out.print("     Enter the ID of the food item whose price you want to change: ");

        int foodId = scanner.nextInt();
        double price = 0; // Initialize quantity
        String query = "select * from food where id = ?";
        String changePriceQuery = "update food set price = ? where id = ?";

        // SHOW THE FOOD CHOSEN
        try (ResultSet result = executeTheQuery(query, foodId)) {

            System.out.println("                                  \n                           FOOD CHOSEN\n\n");
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
                    int updateResult = executeUpdate(changePriceQuery, price, foodId);
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
    public static void deleteFood() throws ClassNotFoundException {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("                     DELETE FOOD\n\n");
        System.out.print("     Enter the ID of the food item that you want to delete: ");

        int foodId = scanner.nextInt();
        String query = "select * from food where id = ?";
        String deleteQuare = "delete from food where id = ?";

        // SHOW THE FOOD CHOSEN
        try (ResultSet result = executeTheQuery(query, foodId)) {

            System.out.println("                                  \n                           FOOD CHOSEN\n\n");
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
                        int updateResult = executeUpdate(deleteQuare, foodId);
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
