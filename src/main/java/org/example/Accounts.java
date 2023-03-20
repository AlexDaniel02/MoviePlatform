package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Accounts {
    private static List<User> users = new ArrayList<>();

    public static void addUser(User user) {
        users.add(user);
        try {
            FileWriter fileWriter = new FileWriter("accounts.csv", true);
            fileWriter.write(user.getUsername() + "," + user.getPassword() + "," + "user" + "\n");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error: Could not write to file.");
        }
    }
    public static void actualizeUsers() {
        boolean adminExists = false;
        try (Scanner scanner = new Scanner(new File("accounts.csv"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                if (values[2].equals("admin")) {
                    adminExists = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
        if (!adminExists) {
            users.add(new User("admin", "admin", "admin"));
            try {
                FileWriter fileWriter = new FileWriter("accounts.csv", true);
                fileWriter.write("admin" + "," + "admin" + "," + "admin" + "\n");
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error: Could not write to file.");
            }
        }
        try {
            Scanner fileScanner = new Scanner(new File("accounts.csv"));
            while (fileScanner.hasNextLine()) {
                String[] accountDetails = fileScanner.nextLine().split(",");
                User user = new User(accountDetails[0], accountDetails[1], accountDetails[2]);
                users.add(user);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }
    public List<User> getUsers() {
        return users;
    }

    public boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
