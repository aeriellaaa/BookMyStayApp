import java.util.*;

// Custom exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
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

// Thread-safe Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public synchronized void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public synchronized int getAvailability(String type) throws InvalidBookingException {
        if (!inventory.containsKey(type)) {
            throw new InvalidBookingException("Room type '" + type + "' does not exist.");
        }
        return inventory.get(type);
    }

    public synchronized void reduceRoom(String type, int count) throws InvalidBookingException {
        int available = getAvailability(type);
        if (count > available) {
            throw new InvalidBookingException("Not enough rooms available for type '" + type + "'. Requested: "
                    + count + ", Available: " + available);
        }
        inventory.put(type, available - count);
    }

    public synchronized void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// Booking History (thread-safe)
class BookingHistory {
    private List<Reservation> history = Collections.synchronizedList(new ArrayList<>());

    public void addReservation(Reservation r) {
        history.add(r);
        System.out.println("✅ Reservation " + r.getReservationId() + " confirmed for " + r.getGuestName());
    }

    public void displayAllReservations() {
        System.out.println("\n=== Booking History ===");
        synchronized (history) {
            for (Reservation r : history) {
                r.displayReservation();
            }
        }
    }
}

// Booking Request class
class BookingRequest {
    public String reservationId;
    public String guestName;
    public String roomType;
    public int rooms;
    public double totalCost;

    public BookingRequest(String reservationId, String guestName, String roomType, int rooms, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.rooms = rooms;
        this.totalCost = totalCost;
    }
}

// Concurrent Booking Processor
class BookingProcessor implements Runnable {
    private Queue<BookingRequest> bookingQueue;
    private RoomInventory inventory;
    private BookingHistory history;

    public BookingProcessor(Queue<BookingRequest> queue, RoomInventory inventory, BookingHistory history) {
        this.bookingQueue = queue;
        this.inventory = inventory;
        this.history = history;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                request = bookingQueue.poll();
            }

            try {
                inventory.reduceRoom(request.roomType, request.rooms);
                Reservation r = new Reservation(request.reservationId, request.guestName,
                        request.roomType, request.rooms, request.totalCost);
                history.addReservation(r);
            } catch (InvalidBookingException e) {
                System.out.println("❌ Booking failed for " + request.guestName + ": " + e.getMessage());
            }
        }
    }
}

// MAIN CLASS
public class BookMystay {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        Queue<BookingRequest> bookingQueue = new LinkedList<>();

        // Initialize inventory
        inventory.addRoomType("Single", 5);
        inventory.addRoomType("Double", 3);
        inventory.addRoomType("Suite", 2);

        // Simulate multiple booking requests
        bookingQueue.add(new BookingRequest("R001", "Alice", "Single", 2, 2000));
        bookingQueue.add(new BookingRequest("R002", "Bob", "Double", 1, 3000));
        bookingQueue.add(new BookingRequest("R003", "Charlie", "Single", 3, 3000));
        bookingQueue.add(new BookingRequest("R004", "David", "Suite", 1, 5000));
        bookingQueue.add(new BookingRequest("R005", "Eve", "Double", 2, 6000));

        // Start multiple threads to process bookings concurrently
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory, history));
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory, history));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display final state
        history.displayAllReservations();
        inventory.displayInventory();
    }
}