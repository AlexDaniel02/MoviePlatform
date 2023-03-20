package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    private static Accounts accounts = new Accounts();
    private static Scanner input = new Scanner(System.in);
    private static Platform platform = new Platform("movies.csv");

    public static void main(String[] args) {
        accounts.actualizeUsers();
        int choice = 0;
        do {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    User loggedUser = login();
                    if(loggedUser!=null)
                    {
                        if(loggedUser.getRole().equals("admin"))
                        {
                            adminMenu(loggedUser);
                        }
                        else
                        {
                            userMenu(loggedUser);
                        }
                    }

                    else{
                        System.out.println("Login failed");
                    }

                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 3);
    }
    private static void adminMenu(User loggedUser)
    {
        int choice = 0;
        do {
            System.out.println("1. Add movie");
            System.out.println("2. Logout");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    platform.addMovie();
                    break;

                case 2:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }
    private static void userMenu(User loggedUser)
    {
        int choice = 0;
        do {
            System.out.println("1. Search a movie.");
            System.out.println("2. Show feed.");
            System.out.println("3. Logout.");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter the movie title: ");
                    input.nextLine();
                    String movieName = input.nextLine();
                    System.out.println(movieName);
                    Movie movie = Platform.searchMovie(movieName);
                    if(movie!=null) {
                        movie.printMovieInfo();
                        System.out.println("1. Like the movie.");
                        System.out.println("2.Go back.");
                        System.out.print("Enter your choice: ");
                        int choice1 = input.nextInt();
                        if (choice1 == 1) {
                            platform.likeMovie(loggedUser, movie);;
                        }
                        break;
                    }
                case 2:
                    platform.showFeed(loggedUser);
                    break;
                case 3:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");

            }
        } while (choice != 4);
    }
    private static void register() {
        System.out.print("Enter username: ");
        String username = input.next();
        if (accounts.isUsernameTaken(username)) {
            System.out.println("Username already taken.");
            return;
        }
        System.out.print("Enter password: ");
        String password = input.next();
        User user = new User(username, password, "user");
        accounts.addUser(user);
    }

    private static User login() {
        System.out.print("Enter username: ");
        String username = input.next();
        System.out.print("Enter password: ");
        String password = input.next();
        User user = accounts.login(username, password);
        if (user == null) {
            System.out.println("Login failed. Invalid username or password.");
            return null;
        } else {
            System.out.println("Login successful.");
            System.out.println("Role: " + user.getRole());
            return user;

        }
    }
}