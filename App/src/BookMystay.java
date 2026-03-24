import java.util.*;

// Reservation (Booking Request)
class Reservation {
    private String guestName;
    private String roomType;
    private int roomsRequested;

    public Reservation(String guestName, String roomType, int roomsRequested) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomsRequested = roomsRequested;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getRoomsRequested() { return roomsRequested; }
}

// Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceRoom(String type, int count) {
        inventory.put(type, inventory.get(type) - count);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// Booking Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("✅ Request added for " + r.getGuestName());
    }

    public Reservation getNextRequest() {
        return queue.poll(); // dequeue
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service (Allocation Logic)
class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomTypeToIds = new HashMap<>();

    // Process queue and allocate rooms
    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        while (!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();

            String type = r.getRoomType();
            int requested = r.getRoomsRequested();

            System.out.println("\nProcessing request for " + r.getGuestName());

            int available = inventory.getAvailability(type);

            if (available >= requested) {

                Set<String> allocatedSet = roomTypeToIds.getOrDefault(type, new HashSet<>());

                for (int i = 0; i < requested; i++) {

                    String roomId;

                    // Generate unique room ID
                    do {
                        roomId = type.substring(0, 1).toUpperCase() + (100 + new Random().nextInt(900));
                    } while (allocatedRoomIds.contains(roomId));

                    // Store unique ID
                    allocatedRoomIds.add(roomId);
                    allocatedSet.add(roomId);

                    System.out.println("Allocated Room ID: " + roomId);
                }

                // Update mappings
                roomTypeToIds.put(type, allocatedSet);

                // Update inventory immediately
                inventory.reduceRoom(type, requested);

                System.out.println("✅ Booking CONFIRMED for " + r.getGuestName());

            } else {
                System.out.println("❌ Booking FAILED for " + r.getGuestName() +
                        " (Not enough rooms)");
            }
        }
    }

    // Display allocated rooms
    public void displayAllocations() {
        System.out.println("\n=== Room Allocations ===");
        for (String type : roomTypeToIds.keySet()) {
            System.out.println(type + " -> " + roomTypeToIds.get(type));
        }
    }
}

// MAIN CLASS
public class BookMystay {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService service = new BookingService();

        // Initialize inventory
        inventory.addRoomType("Single", 3);
        inventory.addRoomType("Double", 2);
        inventory.addRoomType("Suite", 1);

        int choice;

        do {
            System.out.println("\n===== Book My Stay =====");
            System.out.println("1. Add Booking Request");
            System.out.println("2. Process Bookings");
            System.out.println("3. View Inventory");
            System.out.println("4. View Allocations");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Guest Name: ");
                    String name = sc.nextLine();

                    System.out.print("Room Type: ");
                    String type = sc.nextLine();

                    System.out.print("Rooms Required: ");
                    int count = sc.nextInt();

                    queue.addRequest(new Reservation(name, type, count));
                    break;

                case 2:
                    service.processBookings(queue, inventory);
                    break;

                case 3:
                    inventory.displayInventory();
                    break;

                case 4:
                    service.displayAllocations();
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        sc.close();
    }
}