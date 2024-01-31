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

    // =================================== ADD FOOD IN THE DATABASE
    // ===================================
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

    // =================================== SEE FOOD FROM DATABASE
    // ===================================
    public static void seeFood() throws SQLException, ClassNotFoundException {
        String selectAll = "select * from food;";

        System.out.println(
                "-------------------------------------------------------------------------------------");
        try (
                ResultSet result = executeTheQuery(selectAll)) {

            System.out.println("                                  FOOD LIST\n\n");
            while (result.next()) {
                int dataId = result.getInt("id");
                String dataName = result.getString("name");
                int dataQuantity = result.getInt("quantity");
                double dataPrice = result.getInt("price");
                String dataQuantityMeasure = result.getString("quantityMeasure");

                System.out.println(
                        dataId + " " + dataName + " " + dataQuantity + " " + dataPrice + " " + dataQuantityMeasure);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =================================== CHANGE FOOD QUANTITY
    // ===================================
    public static void changeFoodQuantity() {
        System.out.println(
                "-------------------------------------------------------------------------------------");
        System.out.println("                     CHANGE QUANTITY FOOD\n\n");
        System.out.print("     Enter the ID of the food item whose quantity you want to change: ");
        int foodId = scanner.nextInt();

        // FIND FOOD ITEM , ADD OR DELETE
        for (Food food : foodList) {
            if (food.get_id() == foodId) {
                while (true) {
                    System.out.println("\n     YOU SELECTED: " + food.get_name() + ". CURRENT QUANTITY: "
                            + food.get_quantity() + food.get_quantitityMeasure() + "\n");
                    System.out.println("     1.Add quantity| 2.Delete quantity| 0.CLOSE QUANTITY CHANGE\n");
                    System.out.print("     Enter choice: ");
                    int choice = scanner.nextInt();

                    // Check if the user wants to exit.
                    if (choice == 0) {
                        break;
                    }

                    // Add quantity
                    if (choice == 1) {
                        System.out.print("     How many " + food.get_quantitityMeasure() + " to add: ");
                        double quantityToAdd = scanner.nextDouble();
                        food.add_quantity(quantityToAdd);
                        System.out.println(
                                "     CURRENT QUANTITY: " + food.get_quantity() + food.get_quantitityMeasure());
                    }

                    // Delete quantity
                    else if (choice == 2) {
                        System.out.print("     How many " + food.get_quantitityMeasure() + " to Delete: ");
                        double quantityToDelete = scanner.nextDouble();
                        food.delete_quantity(quantityToDelete);
                        System.out.println(
                                "     CURRENT QUANTITY: " + food.get_quantity() + food.get_quantitityMeasure());
                    }
                }
            }
        }
    }

    // =================================== CHANGE FOOD PRICE
    // ===================================
    public static void changeFoodPrice() {
        System.out.println(
                "-------------------------------------------------------------------------------------");
        System.out.println("                               CHANGE PRICE FOOD\n\n");
        System.out.print("     Enter the ID of the food item whose price you want to change: ");
        int foodId = scanner.nextInt();

        // FIND FOOD ITEM
        for (Food food : foodList) {
            if (food.get_id() == foodId) {
                while (true) {
                    System.out.println("\n     YOU SELECTED: " + food.get_name() + ". CURRENT PRICE: "
                            + food.get_price() + " $\n");
                    System.out.println("     1.Change Price| 0.CLOSE QUANTITY CHANGE\n");
                    System.out.print("     Enter choice: ");
                    int choice = scanner.nextInt();

                    // Check if the user wants to exit.
                    if (choice == 0) {
                        break;
                    }

                    // Add quantity
                    if (choice == 1) {
                        System.out.print("     New price: ");
                        double newPrice = scanner.nextDouble();
                        food.change_price(newPrice);
                        System.out.println(
                                "     CURRENT PRICE: " + food.get_price());
                    }
                }
            }
        }
    }

    // =================================== DELETE FOOD
    // ===================================
    public static void deleteFood() {
        System.out.println(
                "-------------------------------------------------------------------------------------");
        System.out.println("                                  DELETE FOOD\n\n");
        System.out.print("     Enter the ID of the food item that you want to delete: ");
        int foodId = scanner.nextInt();

        // FIND FOOD ITEM
        for (Iterator<Food> iterator = foodList.iterator(); iterator.hasNext();) {
            Food food = iterator.next();
            if (food.get_id() == foodId) {
                while (true) {
                    System.out.println("\n     YOU SELECTED: " + food.get_name());
                    System.out.println("     1.Delete item| 0.CLOSE DELETE FOOD\n");
                    System.out.print("     Enter choice: ");
                    int choice = scanner.nextInt();

                    // Check if the user wants to exit.
                    if (choice == 0) {
                        break;
                    }

                    // Change price
                    if (choice == 1) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

}
