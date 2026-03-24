import java.util.*;

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

// Booking History Service
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // Add confirmed reservation to history
    public void addReservation(Reservation r) {
        history.add(r);
        System.out.println("✅ Reservation " + r.getReservationId() + " added to booking history.");
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(history); // defensive copy
    }

    // Display all reservations
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

    // Generate simple summary report
    public void generateReport() {
        System.out.println("\n=== Booking Summary Report ===");
        if (history.isEmpty()) {
            System.out.println("No bookings to report.");
            return;
        }

        Map<String, Integer> roomTypeCount = new HashMap<>();
        double totalRevenue = 0;

        for (Reservation r : history) {
            roomTypeCount.put(r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + r.getRoomsBooked());
            totalRevenue += r.getTotalCost();
        }

        System.out.println("Room Type Summary:");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " : " + roomTypeCount.get(type) + " rooms booked");
        }
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }
}

// MAIN CLASS
public class BookMystay {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BookingHistory history = new BookingHistory();

        int choice;

        do {
            System.out.println("\n===== Book My Stay: Booking History & Reports =====");
            System.out.println("1. Add Confirmed Reservation");
            System.out.println("2. View All Booking History");
            System.out.println("3. Generate Booking Summary Report");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    System.out.print("Enter Reservation ID: ");
                    String id = scanner.nextLine();

                    System.out.print("Enter Guest Name: ");
                    String guest = scanner.nextLine();

                    System.out.print("Enter Room Type: ");
                    String type = scanner.nextLine();

                    System.out.print("Enter Number of Rooms: ");
                    int rooms = scanner.nextInt();

                    System.out.print("Enter Total Cost (including add-ons): ");
                    double cost = scanner.nextDouble();
                    scanner.nextLine(); // consume newline

                    Reservation r = new Reservation(id, guest, type, rooms, cost);
                    history.addReservation(r);
                    break;

                case 2:
                    history.displayAllReservations();
                    break;

                case 3:
                    history.generateReport();
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