import java.util.*;

// Reservation represents a booking request
class Reservation {
    private String guestName;
    private String roomType;
    private int numberOfRooms;

    public Reservation(String guestName, String roomType, int numberOfRooms) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void display() {
        System.out.println("Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Rooms Requested: " + numberOfRooms);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("✅ Request added for " + reservation.getGuestName());
    }

    // View all requests
    public void viewRequests() {
        System.out.println("\n=== Booking Requests Queue ===");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek next request (FIFO)
    public void peekNextRequest() {
        System.out.println("\nNext Request (FIFO):");

        Reservation r = queue.peek();

        if (r == null) {
            System.out.println("No requests in queue.");
        } else {
            r.display();
        }
    }
}

// MAIN CLASS (UPDATED NAME)
public class BookMystay {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        int choice;

        do {
            System.out.println("\n===== Book My Stay - Booking Requests =====");
            System.out.println("1. Add Booking Request");
            System.out.println("2. View All Requests");
            System.out.println("3. View Next Request (FIFO)");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    System.out.print("Enter Guest Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Room Type: ");
                    String roomType = scanner.nextLine();

                    System.out.print("Enter Number of Rooms: ");
                    int count = scanner.nextInt();

                    Reservation reservation = new Reservation(name, roomType, count);
                    requestQueue.addRequest(reservation);
                    break;

                case 2:
                    requestQueue.viewRequests();
                    break;

                case 3:
                    requestQueue.peekNextRequest();
                    break;

                case 4:
                    System.out.println("Exiting... Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);

        scanner.close();
    }
}