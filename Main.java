import java.util.*;

class Order {
    private String orderId;
    private String itemName;
    private double totalPrice;
    private boolean isCancelled;

    public Order(String orderId, String itemName, double totalPrice, boolean isCancelled) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.totalPrice = totalPrice;
        this.isCancelled = isCancelled;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancelOrder() {
        isCancelled = true;
    }

    public void uncancelOrder() {
        isCancelled = false;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class ZomatoApp {
    private Order order;
    private Customer customer;

    public ZomatoApp(Order order, Customer customer) {
        this.order = order;
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public Customer getCustomer() {
        return customer;
    }
}

class ZomatoSystem {
    private List<Order> orders;
    private List<Customer> customers;
    private List<ZomatoApp> zomatoApps;

    public ZomatoSystem() {
        orders = new ArrayList<>();
        customers = new ArrayList<>();
        zomatoApps = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void cancelOrder(Order order, Customer customer) {
        if (!order.isCancelled()) {
            order.cancelOrder();
            zomatoApps.removeIf(zomatoApp -> zomatoApp.getOrder().equals(order));
            zomatoApps.add(new ZomatoApp(order, customer));
        } else {
            System.out.println("Order is already cancelled.");
        }
    }

    public void uncancelOrder(Order order) {
        order.uncancelOrder();
        ZomatoApp zomatoAppToRemove = null;
        for (ZomatoApp zomatoApp : zomatoApps) {
            if (zomatoApp.getOrder().equals(order)) {
                zomatoAppToRemove = zomatoApp;
                break;
            }
        }
        if (zomatoAppToRemove != null) {
            zomatoApps.remove(zomatoAppToRemove);
        } else {
            System.out.println("Order was not found in the cancelled list.");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("===== Zomato System =====");
            System.out.println("1. Place Order");
            System.out.println("2. Cancel Order");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("\n== Place Order ==\n");
                    System.out.print("Enter your name: ");
                    String customerName = scanner.nextLine();

                    System.out.println("\nAvailable orders:");
                    for (Order order : orders) {
                        if (!order.isCancelled()) {
                            System.out.println(order.getOrderId() + " - " + order.getItemName() + " " + order.getTotalPrice());
                        }
                    }
                    System.out.print("\nEnter the order ID you want to place: ");
                    String orderId = scanner.nextLine();

                    Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                    addCustomer(newCustomer);

                    Order selectedOrder = null;
                    for (Order order : orders) {
                        if (order.getOrderId().equals(orderId) && !order.isCancelled()) {
                            selectedOrder = order;
                            break;
                        }
                    }
                    if (selectedOrder != null) {
                        System.out.println("\n== Order Information ==\n");
                        System.out.println("Customer ID: " + newCustomer.getCustomerId());
                        System.out.println("Customer Name: " + newCustomer.getName());
                        System.out.printf("Total Price: $%.2f%n", selectedOrder.getTotalPrice());

                        System.out.print("\nConfirm Order (Y/N): ");
                        String confirm = scanner.nextLine();

                        if (confirm.equalsIgnoreCase("Y")) {
                            cancelOrder(selectedOrder, newCustomer);
                            System.out.println("\nOrder placed successfully.");
                        } else {
                            System.out.println("\nOrder canceled.");
                        }
                    } else {
                        System.out.println("\nInvalid order selection or order not available.");
                    }
                }
                case 2 -> {
                    System.out.println("\n== Cancel Order ==\n");
                    System.out.print("Enter the order ID you want to cancel: ");
                    String orderId = scanner.nextLine();

                    Order orderToCancel = null;
                    for (Order order : orders) {
                        if (order.getOrderId().equals(orderId) && !order.isCancelled()) {
                            orderToCancel = order;
                            break;
                        }
                    }

                    if (orderToCancel != null) {
                        uncancelOrder(orderToCancel);
                        System.out.println("Order cancelled successfully.");
                    } else {
                        System.out.println("Invalid order ID or order is not placed.");
                    }
                }
                case 3 -> {
                    System.out.println("Exiting the system...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ZomatoSystem zomatoSystem = new ZomatoSystem();

        // Adding some orders to the system for demonstration
        zomatoSystem.addOrder(new Order("O001", "Pizza", 12.99, false));
        zomatoSystem.addOrder(new Order("O002", "Burger", 8.99, false));
        zomatoSystem.addOrder(new Order("O003", "Pasta", 10.49, false));

        zomatoSystem.menu();
    }
}
