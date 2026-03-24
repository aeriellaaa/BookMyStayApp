import java.util.*;

// Custom exception for invalid bookings
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
    public double getTotalCost() { return totalCost; }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Guest: " + guestName);
        System.out.println("Room Type: " + roomType);
        System.out.println("Rooms Booked: " + roomsBooked);
        System.out.println("Total Cost: ₹" + totalCost);
        System.out.println("-------------------------------");
    }
}

// Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) throws InvalidBookingException {
        if (!inventory.containsKey(type)) {
            throw new InvalidBookingException("Room type '" + type + "' does not exist.");
        }
        return inventory.get(type);
    }

    public void reduceRoom(String type, int count) throws InvalidBookingException {
        int available = getAvailability(type);
        if (count > available) {
            throw new InvalidBookingException("Not enough rooms available for type '" + type + "'. Requested: "
                    + count + ", Available: " + available);
        }
        inventory.put(type, available - count);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }
}

// Booking History
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
        System.out.println("✅ Reservation " + r.getReservationId() + " added to booking history.");
    }

    public void displayAllReservations() {
        if (history.isEmpty()) {
            System.out.println("No bookings in history.");
            return;
        }
        System.out.println("\n=== Booking History ===");
        for (Reservation r : history) {
            r.displayReservation();
        }
    }
}

// MAIN CLASS
public class BookMystay {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Initialize inventory
        inventory.addRoomType("Single", 3);
        inventory.addRoomType("Double", 2);
        inventory.addRoomType("Suite", 1);

        int choice;

        do {
            System.out.println("\n===== Book My Stay: Error Handling & Validation =====");
            System.out.println("1. Add Confirmed Reservation");
            System.out.println("2. View Booking History");
            System.out.println("3. View Inventory");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    try {
                        System.out.print("Enter Reservation ID: ");
                        String id = scanner.nextLine();

                        System.out.print("Enter Guest Name: ");
                        String guest = scanner.nextLine();

                        System.out.print("Enter Room Type: ");
                        String type = scanner.nextLine();

                        if (!inventory.isValidRoomType(type)) {
                            throw new InvalidBookingException("Invalid room type: " + type);
                        }

                        System.out.print("Enter Number of Rooms: ");
                        int rooms = scanner.nextInt();
                        scanner.nextLine();

                        if (rooms <= 0) {
                            throw new InvalidBookingException("Number of rooms must be greater than 0.");
                        }

                        int available = inventory.getAvailability(type);
                        if (rooms > available) {
                            throw new InvalidBookingException("Requested rooms exceed available inventory. Available: " + available);
                        }

                        System.out.print("Enter Total Cost: ");
                        double cost = scanner.nextDouble();
                        scanner.nextLine();

                        if (cost < 0) {
                            throw new InvalidBookingException("Total cost cannot be negative.");
                        }

                        // Update inventory safely
                        inventory.reduceRoom(type, rooms);

                        // Create reservation
                        Reservation r = new Reservation(id, guest, type, rooms, cost);
                        history.addReservation(r);

                        System.out.println("✅ Booking successfully confirmed for " + guest);

                    } catch (InvalidBookingException e) {
                        System.out.println("❌ Booking Failed: " + e.getMessage());
                    } catch (InputMismatchException e) {
                        System.out.println("❌ Invalid input type. Please enter numbers where required.");
                        scanner.nextLine(); // consume invalid input
                    }
                    break;

                case 2:
                    history.displayAllReservations();
                    break;

                case 3:
                    inventory.displayInventory();
                    break;

                case 4:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);

        scanner.close();
    }
}