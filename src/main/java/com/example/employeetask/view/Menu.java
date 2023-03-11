package com.example.employeetask.view;

import java.util.Scanner;

public class Menu implements State {
    static final Scanner SCANNER = new Scanner(System.in);

    MenuItem[] items;

    public Menu(MenuItem... items) {
        this.items = items;
    }

    static String readLn() {
        return SCANNER.nextLine();
    }

    private State showMenu() {
        for (MenuItem item : items) {
            System.out.printf("%d. %s%n", item.getId(), item.getTitle());
        }

        int option = 0;
        try {
            option = Integer.parseInt(readLn());
        } catch (Exception e) {
            System.out.println("Please, enter a valid number.");
            return this;
        }

        boolean unValidOption = false;
        for (MenuItem item : items) {
            if (item.getId() == option) {
                return item.option;
            } else if (item.getId() != option) {
                unValidOption = true;
            }
        }

        if (unValidOption) {
            System.out.println("Please, enter a valid number.");
        }

        return this;
    }

    @Override
    public State get() {
        return showMenu();
    }
}
