import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BookMyStay {

    // Inner class for centralized inventory management
    static class RoomInventory {

        private Map<String, Integer> inventory;

        // Constructor
        public RoomInventory() {
            inventory = new HashMap<>();
        }

        // Add room type
        public void addRoomType(String roomType, int count) {
            inventory.put(roomType, count);
        }

        // Get availability
        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        // Update availability
        public void updateAvailability(String roomType, int change) {
            int current = inventory.getOrDefault(roomType, 0);
            int updated = current + change;

            if (updated < 0) {
                System.out.println("❌ Not enough rooms available for " + roomType);
                return;
            }

            inventory.put(roomType, updated);
            System.out.println("✅ Updated " + roomType + " rooms. Current: " + updated);
        }

        // Display inventory
        public void displayInventory() {
            System.out.println("\n--- Current Room Inventory ---");
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Preload room types
        inventory.addRoomType("Single", 10);
        inventory.addRoomType("Double", 5);
        inventory.addRoomType("Suite", 2);

        int choice;

        do {
            System.out.println("\n===== Book My Stay =====");
            System.out.println("1. View Room Availability");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    inventory.displayInventory();
                    break;

                case 2:
                    System.out.print("Enter room type to book: ");
                    String bookType = scanner.nextLine();

                    System.out.print("Enter number of rooms: ");
                    int bookCount = scanner.nextInt();

                    inventory.updateAvailability(bookType, -bookCount);
                    break;

                case 3:
                    System.out.print("Enter room type to cancel: ");
                    String cancelType = scanner.nextLine();

                    System.out.print("Enter number of rooms: ");
                    int cancelCount = scanner.nextInt();

                    inventory.updateAvailability(cancelType, cancelCount);
                    break;

                case 4:
                    System.out.println("Exiting... Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 4);

        scanner.close();
    }
}