import java.io.*;
import java.util.*;

// Serializable Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private String roomType;
    private int roomsBooked;
    private double totalCost;

    public Reservation(String reservationId, String guestName, String roomType, int roomsBooked, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomsBooked = roomsBooked;
        this.totalCost = totalCost;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getRoomsBooked() { return roomsBooked; }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId + ", Guest: " + guestName
                + ", Room Type: " + roomType + ", Rooms: " + roomsBooked
                + ", Total: ₹" + totalCost);
    }
}

// Serializable Inventory
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceRoom(String type, int count) throws Exception {
        int available = getAvailability(type);
        if (count > available) throw new Exception("Not enough rooms for type " + type);
        inventory.put(type, available - count);
    }

    public void increaseRoom(String type, int count) {
        inventory.put(type, getAvailability(type) + count);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// Serializable BookingHistory
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
        System.out.println("✅ Reservation " + r.getReservationId() + " confirmed for " + r.getGuestName());
    }

    public void displayAllReservations() {
        System.out.println("\n=== Booking History ===");
        for (Reservation r : history) {
            r.displayReservation();
        }
    }

    public List<Reservation> getHistory() {
        return history;
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "bookmystay_data.ser";

    public static void saveData(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("\n💾 System state saved successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error saving system state: " + e.getMessage());
        }
    }

    public static Object[] loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("⚠️ No previous data found. Starting fresh.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("\n♻️ System state restored from previous session!");
            return new Object[]{inventory, history};
        } catch (Exception e) {
            System.out.println("❌ Error loading system state: " + e.getMessage());
            return null;
        }
    }
}

// MAIN CLASS
public class BookMystay {

    public static void main(String[] args) {

        // Try to restore previous state
        Object[] restored = PersistenceService.loadData();
        RoomInventory inventory;
        BookingHistory history;

        if (restored != null) {
            inventory = (RoomInventory) restored[0];
            history = (BookingHistory) restored[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();

            // Initialize default inventory
            inventory.addRoomType("Single", 5);
            inventory.addRoomType("Double", 3);
            inventory.addRoomType("Suite", 2);
        }

        // Simulate some bookings
        try {
            inventory.reduceRoom("Single", 2);
            history.addReservation(new Reservation("R001", "Alice", "Single", 2, 2000));

            inventory.reduceRoom("Double", 1);
            history.addReservation(new Reservation("R002", "Bob", "Double", 1, 3000));
        } catch (Exception e) {
            System.out.println("❌ Booking error: " + e.getMessage());
        }

        // Display current state
        history.displayAllReservations();
        inventory.displayInventory();

        // Save state before exit
        PersistenceService.saveData(inventory, history);
    }
}