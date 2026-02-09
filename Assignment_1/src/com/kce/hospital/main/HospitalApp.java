package com.kce.hospital.main;

import com.kce.hospital.model.*;
import com.kce.hospital.service.*;
import com.kce.hospital.exception.InvalidOrderException;

import java.util.*;

public class HospitalApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        List<Patient> patients = new ArrayList<>();
        List<LabTest> tests = new ArrayList<>();
        List<TestOrder> orders = new ArrayList<>();
        List<Invoice> invoices = new ArrayList<>();

        OrderService orderService = new OrderService();
        InvoiceService invoiceService = new InvoiceService();

        int patientId = 1, testId = 1, orderId = 1;

        int choice;

        do {
            System.out.println("\n--- Hospital Lab Management ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Add Lab Test");
            System.out.println("3. Create Test Order");
            System.out.println("4. Collect Sample");
            System.out.println("5. Record Result");
            System.out.println("6. Generate Invoice");
            System.out.println("7. Record Payment");
            System.out.println("8. Display Orders");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Enter a number between 1-9: ");
                sc.next();
            }

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    sc.nextLine(); // consume newline
                    System.out.print("Enter patient name: ");
                    String pname = sc.nextLine().trim();
                    while (pname.isEmpty()) {
                        System.out.print("Name can't be empty. Enter again: ");
                        pname = sc.nextLine().trim();
                    }

                    System.out.print("Enter age: ");
                    while (!sc.hasNextInt()) {
                        System.out.print("Enter valid age: ");
                        sc.next();
                    }
                    int age = sc.nextInt();
                    if (age <= 0) {
                        System.out.println("Age must be positive.");
                        break;
                    }

                    patients.add(new Patient(patientId++, pname, age));
                    System.out.println("Patient added.");
                    break;

                case 2:
                    sc.nextLine(); 
                    System.out.print("Enter test name: ");
                    String tname = sc.nextLine().trim();
                    while (tname.isEmpty()) {
                        System.out.print("Test name can't be empty. Enter again: ");
                        tname = sc.nextLine().trim();
                    }

                    System.out.print("Enter test price: ");
                    while (!sc.hasNextDouble()) {
                        System.out.print("Enter valid price: ");
                        sc.next();
                    }
                    double price = sc.nextDouble();
                    if (price <= 0) {
                        System.out.println("Price must be positive.");
                        break;
                    }

                    tests.add(new LabTest(testId++, tname, price));
                    System.out.println("Lab Test added.");
                    break;

                case 3:
                    if (patients.isEmpty() || tests.isEmpty()) {
                        System.out.println("Add at least one patient and one lab test first.");
                        break;
                    }

                    System.out.println("Available Patients:");
                    for (Patient p : patients) {
                        System.out.println(p.getId() + ". " + p.getName());
                    }

                    System.out.print("Enter patient ID: ");
                    int pid = sc.nextInt();
                    Patient selectedPatient = null;
                    for (Patient p : patients) {
                        if (p.getId() == pid) selectedPatient = p;
                    }
                    if (selectedPatient == null) {
                        System.out.println("Invalid patient ID.");
                        break;
                    }

                    System.out.println("Available Lab Tests:");
                    for (LabTest t : tests) {
                        System.out.println(t.getId() + ". " + t.getName() + " (₹" + t.getPrice() + ")");
                    }

                    List<LabTest> selectedTests = new ArrayList<>();
                    System.out.println("Enter test IDs to add (0 to stop):");
                    int tid;
                    while ((tid = sc.nextInt()) != 0) {
                        LabTest selectedTest = null;
                        for (LabTest t : tests) {
                            if (t.getId() == tid) selectedTest = t;
                        }
                        if (selectedTest != null) {
                            selectedTests.add(selectedTest);
                        } else {
                            System.out.println("Invalid test ID.");
                        }
                    }

                    try {
                        TestOrder order = orderService.createOrder(orderId++, selectedPatient, selectedTests);
                        orders.add(order);
                        System.out.println("Test order created.");
                    } catch (InvalidOrderException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    if (orders.isEmpty()) {
                        System.out.println("No orders available.");
                        break;
                    }

                    for (TestOrder o : orders) {
                        System.out.println("Order ID: " + o.getId() + " - Patient: " + o.getPatient().getName());
                    }

                    System.out.print("Enter order ID to collect sample: ");
                    int orderIdToSample = sc.nextInt();
                    TestOrder orderToSample = null;
                    for (TestOrder o : orders) {
                        if (o.getId() == orderIdToSample) orderToSample = o;
                    }

                    if (orderToSample == null) {
                        System.out.println("Invalid order ID.");
                        break;
                    }

                    for (TestOrderItem item : orderToSample.getItems()) {
                        if (item.getSample() == null) {
                            Sample sample = orderService.collectSample(item);
                            System.out.println("Sample collected for: " + item.getTest().getName());
                        }
                    }
                    break;

                case 5:
                    if (orders.isEmpty()) {
                        System.out.println("No orders available.");
                        break;
                    }

                    for (TestOrder o : orders) {
                        for (TestOrderItem item : o.getItems()) {
                            Sample sample = item.getSample();
                            if (sample != null && sample.getResult() == null) {
                                sc.nextLine(); // clear buffer
                                System.out.print("Enter result for " + item.getTest().getName() + ": ");
                                String result = sc.nextLine().trim();
                                while (result.isEmpty()) {
                                    System.out.print("Result can't be empty. Enter again: ");
                                    result = sc.nextLine().trim();
                                }
                                orderService.recordResult(sample, result);
                                System.out.println("Result recorded.");
                            }
                        }
                    }
                    break;

                case 6:
                    if (orders.isEmpty()) {
                        System.out.println("No orders found.");
                        break;
                    }

                    for (TestOrder o : orders) {
                        System.out.println("Order ID: " + o.getId() + " - Patient: " + o.getPatient().getName());
                    }

                    System.out.print("Enter order ID to generate invoice: ");
                    int oid = sc.nextInt();
                    TestOrder selectedOrder = null;
                    for (TestOrder o : orders) {
                        if (o.getId() == oid) selectedOrder = o;
                    }
                    if (selectedOrder == null) {
                        System.out.println("Invalid order ID.");
                        break;
                    }

                    Invoice invoice = invoiceService.generateInvoice(selectedOrder);
                    invoices.add(invoice);
                    System.out.println("Invoice Total: ₹" + invoice.getTotal());
                    break;

                case 7:
                    if (invoices.isEmpty()) {
                        System.out.println("No invoices available.");
                        break;
                    }

                    System.out.println("Available Invoices:");
                    for (Invoice inv : invoices) {
                        System.out.println("Order ID: " + inv.getOrder().getId() +
                                ", Patient: " + inv.getOrder().getPatient().getName() +
                                ", Total: ₹" + inv.getTotal() +
                                ", Paid: " + (inv.isPaid() ? "Yes" : "No"));
                    }

                    System.out.print("Enter order ID to make payment: ");
                    int payId = sc.nextInt();
                    Invoice selectedInvoice = null;
                    for (Invoice inv : invoices) {
                        if (inv.getOrder().getId() == payId) selectedInvoice = inv;
                    }

                    if (selectedInvoice == null) {
                        System.out.println("Invalid order ID.");
                        break;
                    }

                    System.out.print("Enter payment amount: ");
                    while (!sc.hasNextDouble()) {
                        System.out.print("Enter valid amount: ");
                        sc.next();
                    }
                    double amt = sc.nextDouble();

                    invoiceService.recordPayment(selectedInvoice, amt);
                    System.out.println(selectedInvoice.isPaid() ? "Payment successful." : "Partial payment. Not fully paid.");
                    break;

                case 8:
                    if (orders.isEmpty()) {
                        System.out.println("No orders found.");
                        break;
                    }

                    for (TestOrder o : orders) {
                        System.out.println("\nOrder ID: " + o.getId() + ", Patient: " + o.getPatient().getName());
                        for (TestOrderItem item : o.getItems()) {
                            String sampleStatus = (item.getSample() != null) ? "Collected" : "Pending";
                            String resultStatus = (item.getSample() != null && item.getSample().getResult() != null)
                                    ? item.getSample().getResult().getValue() : "Pending";
                            System.out.println(" - " + item.getTest().getName() + " | ₹" + item.getTest().getPrice()
                                    + " | Sample: " + sampleStatus + " | Result: " + resultStatus);
                        }
                    }
                    break;

                case 9:
                    System.out.println("Exiting system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Enter between 1-9.");
            }
        } while (choice != 9);

        sc.close();
    }
}
