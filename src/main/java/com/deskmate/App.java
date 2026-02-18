package com.deskmate;

import com.deskmate.controller.BookingController;
import com.deskmate.utils.InputUtil;

public class App {

    public static void main(String[] args) {

        BookingController bookingController = new BookingController();

        while (true) {

            System.out.println("\n=== DeskMate System ===");
            System.out.println("1. Create Booking + Payment");
            System.out.println("0. Exit");

            int choice = InputUtil.readInt("Choose: ");

            switch (choice) {
                case 1 -> bookingController.createBooking();
                case 0 -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
