package org.example;

import com.github.javafaker.Faker;
import org.example.entities.Customer;
import org.example.entities.Order;
import org.example.entities.Product;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application {

    static List<Product> magazzino = new ArrayList<>();
    static List<Customer> customers = new ArrayList<>();
    static List<Order> orders = new ArrayList<>();


    public static void main(String[] args) {
        initializeWarehouse();
        createCustomers();
        placeOrders();

        System.out.println("--------------------UTENTI--------------------");
        System.out.println(customers);

        System.out.println("-----------------------Ordini divisi per cliente-----------------------");

        Map<Customer, List<Order>> ordersPerCustomer = orders.stream().collect(Collectors.groupingBy(Order::getCustomer));
        ordersPerCustomer.forEach((customer, order) -> System.out.println("User: " + customer.getName() + ", " + order));

        System.out.println("-----------------------Totale costo ordini per cliente-----------------------");
        Map<Customer, Double> totalPerCustomer = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer,
                        Collectors.summingDouble(Order::getTotal)));
        totalPerCustomer.forEach((customer, total) ->
                System.out.println("User: " + customer.getName() + ", " + "Totale acquisti: " + total));


        System.out.println("----------------I 3 PRODOTTI PIU COSTOSI-----------------------");

        List<Product> expensiveProducts = magazzino.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).
                limit(3).toList();
        expensiveProducts.forEach(product -> System.out.println("Prodotto: " + product.getName() + ", " + product.getPrice()));


        System.out.println("----------------MEDIA IMPORTI DEGLI ORDINI-----------------------");

        double averageOrdersPrice = orders.stream().mapToDouble(order -> order.getProducts().stream().mapToDouble(Product::getPrice).sum()).average().getAsDouble();
        System.out.println(averageOrdersPrice);

        System.out.println("---------------PRODOTTI RAGGRUPPATI PER CATEGORIA + SOMMA DEGLI IMPORTI DI OGNI CATEGORIA-------------");

        Map<String, Double> filteredProdList = magazzino.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice)));

        filteredProdList.forEach((category, total) -> System.out.println("Categoria: " + category + ", " + "Totale: " + total));


    }


    public static void createCustomers() {

        Faker fakeName = new Faker(Locale.ITALY);
        Random rndm = new Random();

        Supplier<Customer> customerSupplier = () -> {
            return new Customer(fakeName.name().firstName(), rndm.nextInt(1, 4));
        };

        for (int i = 0; i < 4; i++) {
            customers.add(customerSupplier.get());
        }

    }

    public static void initializeWarehouse() {
        Product iPhone = new Product("IPhone", "Smartphones", 2000.0);
        Product lotrBook = new Product("LOTR", "Books", 101);
        Product itBook = new Product("IT", "Books", 2);
        Product davinciBook = new Product("Da Vinci's Code", "Books", 2);
        Product diapers = new Product("Pampers", "Baby", 3);
        Product toyCar = new Product("Car", "Boys", 15);
        Product toyPlane = new Product("Plane", "Boys", 25);
        Product lego = new Product("Lego Star Wars", "Boys", 500);

        magazzino.addAll(Arrays.asList(iPhone, lotrBook, itBook, davinciBook, diapers, toyCar, toyPlane, lego));
    }

    public static void placeOrders() {
        Order orderUser1 = new Order(customers.get(0));
        Order orderUser2 = new Order(customers.get(1));
        Order orderUser3 = new Order(customers.get(2));
        Order orderUser4 = new Order(customers.get(3));
        Order orderUser2_2 = new Order(customers.get(2));

        Product iPhone = magazzino.get(0);
        Product lotrBook = magazzino.get(1);
        Product itBook = magazzino.get(2);
        Product davinciBook = magazzino.get(3);
        Product diaper = magazzino.get(4);

        orderUser1.addProduct(iPhone);
        orderUser1.addProduct(lotrBook);
        orderUser1.addProduct(diaper);

        orderUser2.addProduct(itBook);
        orderUser2.addProduct(davinciBook);
        orderUser2.addProduct(iPhone);

        orderUser3.addProduct(lotrBook);
        orderUser3.addProduct(diaper);

        orderUser4.addProduct(diaper);

        orderUser2_2.addProduct(iPhone);

        orders.add(orderUser1);
        orders.add(orderUser2);
        orders.add(orderUser3);
        orders.add(orderUser4);
        orders.add(orderUser2_2);

    }


}


