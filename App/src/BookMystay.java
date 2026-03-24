import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int roomsBooked;

    public Reservation(String reservationId, String guestName, String roomType, int roomsBooked) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomsBooked = roomsBooked;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getRoomsBooked() {
        return roomsBooked;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Guest: " + guestName);
        System.out.println("Room Type: " + roomType);
        System.out.println("Rooms Booked: " + roomsBooked);
    }
}

// Add-On Service class
class Service {
    private String name;
    private double price;

    public Service(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    public void displayService() {
        System.out.println("- " + name + " (₹" + price + ")");
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Attach a service to a reservation
    public void addServiceToReservation(String reservationId, Service service) {
        reservationServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
        System.out.println("✅ Added service '" + service.getName() + "' to reservation " + reservationId);
    }

    // Get services for a reservation
    public List<Service> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalCost(String reservationId) {
        double total = 0;
        for (Service s : getServices(reservationId)) {
            total += s.getPrice();
        }
        return total;
    }

    // Display all services for a reservation
    public void displayServices(String reservationId) {
        List<Service> services = getServices(reservationId);
        if (services.isEmpty()) {
            System.out.println("No add-on services for this reservation.");
            return;
        }
        System.out.println("Add-On Services for reservation " + reservationId + ":");
        for (Service s : services) {
            s.displayService();
        }
        System.out.println("Total Additional Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// MAIN CLASS
public class BookMystay {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Sample Reservations (would normally come from booking system)
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation("R001", "John", "Single", 2));
        reservations.add(new Reservation("R002", "Alice", "Suite", 1));

        // Sample Services
        List<Service> availableServices = new ArrayList<>();
        availableServices.add(new Service("Breakfast", 500));
        availableServices.add(new Service("Airport Pickup", 1200));
        availableServices.add(new Service("Spa Package", 1500));
        availableServices.add(new Service("Extra Bed", 800));

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        int choice;
        do {
            System.out.println("\n===== Add-On Services Menu =====");
            System.out.println("1. View Reservations");
            System.out.println("2. View Available Services");
            System.out.println("3. Add Service to Reservation");
            System.out.println("4. View Reservation Services");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    System.out.println("\n--- Reservations ---");
                    for (Reservation r : reservations) {
                        r.displayReservation();
                        System.out.println("------------------");
                    }
                    break;

                case 2:
                    System.out.println("\n--- Available Services ---");
                    for (Service s : availableServices) {
                        s.displayService();
                    }
                    break;

                case 3:
                    System.out.print("Enter Reservation ID: ");
                    String resId = scanner.nextLine();

                    System.out.println("Enter Service Name to Add: ");
                    String serviceName = scanner.nextLine();

                    // Find service by name
                    Service selected = null;
                    for (Service s : availableServices) {
                        if (s.getName().equalsIgnoreCase(serviceName)) {
                            selected = s;
                            break;
                        }
                    }

                    if (selected != null) {
                        serviceManager.addServiceToReservation(resId, selected);
                    } else {
                        System.out.println("Service not found!");
                    }
                    break;

                case 4:
                    System.out.print("Enter Reservation ID: ");
                    String rid = scanner.nextLine();
                    serviceManager.displayServices(rid);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        scanner.close();
    }
}