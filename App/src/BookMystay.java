import java.util.*;

// Room Domain Model
class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
    }
}

// Centralized Inventory (Read-only access for search)
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// Search Service (STRICTLY READ-ONLY)
class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, List<Room> rooms) {

        System.out.println("\n=== Available Rooms ===");

        boolean found = false;

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getType());

            // Show only available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available Rooms: " + available);
                System.out.println("------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available.");
        }
    }
}

// MAIN CLASS (as required)
public class BookMystay {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 10);
        inventory.addRoomType("Double", 0);   // Will be filtered out
        inventory.addRoomType("Suite", 2);

        // Room details (domain model)
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room("Single", 2000, "WiFi, TV, AC"));
        rooms.add(new Room("Double", 3500, "WiFi, TV, AC, Mini Bar"));
        rooms.add(new Room("Suite", 6000, "WiFi, TV, AC, Jacuzzi, Sea View"));

        // Search service
        SearchService searchService = new SearchService();

        // Guest performs search
        searchService.searchAvailableRooms(inventory, rooms);
    }
}