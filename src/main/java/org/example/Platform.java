package org.example;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Platform {
    private static List<Movie> movies = new ArrayList<>();

    public static Movie searchMovie(String title) {
        for (Movie movie : movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }
    public Platform(String fileName) {
        String line;
        String cvsSplitBy = ",";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM DD yyyy");
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {

                String[] movieData = line.split(cvsSplitBy);
                String title = movieData[0];
                String genre = movieData[1];
                String releaseDate = movieData[2];
                Calendar releaseDateCalendar=new Calendar.Builder().setDate(Integer.parseInt(releaseDate.split(" ")[2]),Integer.parseInt(releaseDate.split(" ")[0])-1, Integer.parseInt(releaseDate.split(" ")[1])).build();
                String description = movieData[3];
                int index=3;
                if(description.contains("\""))
                {
                    index++;
                    while(!movieData[index].contains("\""))
                    {
                        description = description + "," + movieData[index];
                        index++;
                    }
                    description = description + "," + movieData[index];
                    description = description.substring(1,description.length()-1);
                }
                String director = movieData[index+1];
                String movieStudio = movieData[index+2];
                int countLikes = 0;
                try {
                    Scanner fileScanner = new Scanner(new File("liked_movies.csv"));
                    while (fileScanner.hasNextLine()) {
                        String[] movieDetails = fileScanner.nextLine().split(",");
                        if (movieDetails[1].equals(title)) {
                            countLikes++;
                        }
                    }
                    fileScanner.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Error: File not found.");
                }
                movies.add(new Movie(title, genre, releaseDateCalendar, description, director, movieStudio,countLikes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PrintMovies() {
        for (Movie movie : movies) {
            movie.printMovieInfo();
        }
    }
    public void likeMovie(User loggedUser, Movie movie)
    {
        boolean isLiked = false;
        String searchUsername = loggedUser.getUsername();
        String searchMovie = movie.getTitle();

        try (BufferedReader br = new BufferedReader(new FileReader("liked_movies.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(searchUsername) && values[1].equals(searchMovie)) {
                    isLiked = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(isLiked)
        {
            System.out.println("You already liked this movie.");
        }
        else {
            movies.get(movies.indexOf(movie)).setLikes(movies.get(movies.indexOf(movie)).getLikes() + 1);
            try {
                FileWriter fileWriter = new FileWriter("liked_movies.csv", true);
                fileWriter.append(searchUsername + "," + searchMovie + "\n");
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error: Could not write to file.");
            }
        }
    }

    public int compare(Movie m1, Movie m2) {
        return Integer.compare(m2.getLikes(), m1.getLikes());
    }
    public static void showFeed(User user) {
        List<String> likedMovies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("liked_movies.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(user.getUsername())) {
                    likedMovies.add(values[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> recommendedCategories = new ArrayList<>();
        for (String likedMovie : likedMovies) {
            if(!recommendedCategories.contains(searchMovie(likedMovie).getGenre()))
            {
                recommendedCategories.add(searchMovie(likedMovie).getGenre());
            }
        }

        System.out.println("Do you want the recommended movies sorted by release date or by rating?");
        System.out.println("1. Release date");
        System.out.println("2. Rating");
        Scanner input = new Scanner(System.in);
        List <Movie> recommendedMovies = new ArrayList<>();
        List<Movie> notRecommendedMovies = new ArrayList<>();
        int choice = input.nextInt();
        if(choice==1)
        {
            movies.sort((o1, o2) -> o1.getReleaseDate().compareTo(o2.getReleaseDate()));

            for (Movie movie : movies) {
                if(recommendedCategories.contains(movie.getGenre()) &&!likedMovies.contains(movie.getTitle()))
                {
                    System.out.println(movie.getTitle());
                    recommendedMovies.add(movie);
                }
                else if(!recommendedCategories.contains(movie.getGenre()))
                {
                    notRecommendedMovies.add(movie);
                }
            }

        }
        else if(choice==2) {
            movies.sort((o1, o2) -> Integer.compare(o2.getLikes(), o1.getLikes()));

            for (Movie movie : movies) {
                if (recommendedCategories.contains(movie.getGenre()) && !likedMovies.contains(movie.getTitle())) {
                    System.out.println(movie.getTitle());
                    recommendedMovies.add(movie);
                }
                else if(!recommendedCategories.contains(movie.getGenre()))
                {
                    notRecommendedMovies.add(movie);
                }
            }
        }
        else
        {
            System.out.println("Invalid choice.");
        }
        Collections.shuffle(notRecommendedMovies);
        for(Movie movie:notRecommendedMovies)
        {
            System.out.println(movie.getTitle());
        }
    }
    public void addMovie() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM DD yyyy");
        System.out.println("Enter the movie title:");
        Scanner input = new Scanner(System.in);
        String title = input.nextLine();
        System.out.println("Enter the movie genre:");
        String genre = input.nextLine();
        System.out.println("Enter the movie release date in MM DD YYYY format:");
        String releaseDate = input.nextLine();
        Calendar releaseDateCalendar=new Calendar.Builder().setDate(Integer.parseInt(releaseDate.split(" ")[2]),Integer.parseInt(releaseDate.split(" ")[0])-1, Integer.parseInt(releaseDate.split(" ")[1])).build();
        System.out.println("Enter the movie description:");
        String description = input.nextLine();
        System.out.println("Enter the movie director:");
        String director = input.nextLine();
        System.out.println("Enter the movie studio:");
        String movieStudio = input.nextLine();
        movies.add(new Movie(title, genre, releaseDateCalendar, description, director, movieStudio,0));
        if(description.contains(","))
        {
            description="\""+description+"\"";
        }
        try {
            FileWriter fileWriter = new FileWriter("movies.csv", true);
            fileWriter.write(title + "," + genre + "," + (releaseDateCalendar.get(Calendar.MONTH)+1)+" "+releaseDateCalendar.get(Calendar.DAY_OF_MONTH)+" "+releaseDateCalendar.get(Calendar.YEAR) + "," + description + "," + director + "," + movieStudio + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}