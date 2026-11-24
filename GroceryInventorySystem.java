
import java.util.*;
import java.io.*;

class GroceryItem {
    String id, name;
    int quantity, price;

    GroceryItem(String id, String name, int quantity, int price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}

public class GroceryInventorySystem {

    static ArrayList<GroceryItem> inventory = new ArrayList<>();
    static HashMap<String, GroceryItem> map = new HashMap<>();
    static int idCount = 101;

    static Scanner sc = new Scanner(System.in);

    static void loadData() {
        try {
            File file = new File("inventory.txt");

            if (!file.exists()) return;

            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split("\\|");

                if (data.length == 4) {
                    GroceryItem item = new GroceryItem(
                        data[0], data[1],
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3])
                    );

                    inventory.add(item);
                    map.put(item.id, item);

                    int num = Integer.parseInt(item.id.substring(1));
                    if (num >= idCount) idCount = num + 1;
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }

    static void saveData() {
        try {
            FileWriter writer = new FileWriter("inventory.txt");

            for (GroceryItem item : inventory) {
                writer.write(item.id + "|" + item.name + "|" +
                    item.quantity + "|" + item.price + "\n");
            }

            writer.close();

        } catch (Exception e) {
            System.out.println("Error saving file.");
        }
    }

    static void addItem() {
        System.out.print("Enter item name: ");
        String name = sc.nextLine();

        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();

        System.out.print("Enter price: ");
        int price = sc.nextInt();
        sc.nextLine();

        if (qty <= 0 || price < 0 || name.trim().isEmpty()
                || name.contains("|")) {
            System.out.println("Invalid input.");
            return;
        }

        String id = "G" + idCount++;

        GroceryItem item = new GroceryItem(id, name, qty, price);

        inventory.add(item);
        map.put(id, item);

        System.out.println("Item added. ID: " + id +
            " | " + name + " | Qty: " + qty + " | Price: " + price);
    }

    static void sellItem() {
        System.out.print("Enter item ID: ");
        String id = sc.nextLine();

        GroceryItem item = map.get(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.print("Enter quantity to sell: ");
        int qty = sc.nextInt();
        sc.nextLine();

        if (qty <= 0) {
            System.out.println("Invalid quantity.");
        }
        else if (qty > item.quantity) {
            System.out.println("Insufficient stock. Available: "
                + item.quantity);
        }
        else {
            item.quantity -= qty;

            System.out.print("Sold " + qty + " of " + item.name +
                ". Stock: " + item.quantity);

            if (item.quantity == 0) {
                System.out.print(" (Out of stock)");
            }

            System.out.println();
        }
    }

    static void displaySorted() {
        ArrayList<GroceryItem> list = new ArrayList<>(inventory);

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {

                if (list.get(j).name.compareToIgnoreCase(
                    list.get(j + 1).name) > 0) {

                    GroceryItem temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }

        System.out.println("\nInventory Sorted by Name:");

        for (GroceryItem item : list) {
            System.out.println(item.id + " | " + item.name +
                " | Qty: " + item.quantity +
                " | Price: " + item.price);
        }
    }

    static void lowStockReport() {
        System.out.println("\nLow Stock Report:");

        boolean found = false;

        for (GroceryItem item : inventory) {
            if (item.quantity < 5) {
                System.out.println("Low stock: " + item.id +
                    " | " + item.name +
                    " | Qty: " + item.quantity);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No low-stock items.");
        }
    }

    public static void main(String[] args) {

        loadData();

        while (true) {
            System.out.println("\n===== GROCERY INVENTORY SYSTEM =====");
            System.out.println("1. Add Item");
            System.out.println("2. Sell Item");
            System.out.println("3. Display Sorted by Name");
            System.out.println("4. Low Stock Report");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addItem();
                    break;

                case 2:
                    sellItem();
                    break;

                case 3:
                    displaySorted();
                    break;

                case 4:
                    lowStockReport();
                    break;

                case 5:
                    saveData();
                    System.out.println("Data saved successfully!");
                    System.out.println("Thank you!");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
