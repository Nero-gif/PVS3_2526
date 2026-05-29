package exams.maps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieMaps {

    public static void printDirectorMinutes(List<Movie> movies, String name) {
        int totalMinutes = movies.stream()
                .filter(m -> m.getDirector().equals(name))
                .mapToInt(Movie::getTotalMinutes)
                .sum();
        System.out.println("Celkova delka vsech filmu od rezisera " + name + ": " + totalMinutes + " minut");
    }

    public static void main(String[] args) throws IOException {
        List<Movie> movies = Files.lines(Paths.get("data/movies.csv"))
                .skip(1)
                .map(line -> line.split(",", 3))
                .map(parsed -> new Movie(
                        Integer.parseInt(parsed[0]),
                        parsed[1],
                        parsed[2]
                )).toList();

        List<Scene> scenes = Files.lines(Paths.get("data/scenes.csv"))
                .skip(1)
                .map(line -> line.split(",", 4))
                .map(split -> new Scene(
                        Integer.parseInt(split[1]),
                        split[2],
                        Integer.parseInt(split[3])
                )).toList();

        // Propojte scény s filmy podle movieId.
        Map<Integer, List<Scene>> scenesByMovie = scenes.stream()
                .collect(Collectors.groupingBy(Scene::getMovieId));

        movies.forEach(movie ->
                movie.setScenes(scenesByMovie.getOrDefault(movie.getMovieId(), new ArrayList<>()))
        );

        // Implementujte metodu printDirectorMinutes.
        System.out.println("--- Delka filmu vybraneho rezisera ---");
        // Testovaci volani (jmeno muzete zmenit podle realnych dat)
        printDirectorMinutes(movies, "Christopher Nolan");
        System.out.println();

        // Vypište, kolik filmů natočil každý režisér.
        System.out.println("--- Pocet filmu, ktere natocil kazdy reziser ---");
        Map<String, Long> moviesPerDirector = movies.stream()
                .collect(Collectors.groupingBy(Movie::getDirector, Collectors.counting()));
        moviesPerDirector.forEach((director, count) ->
                System.out.println(director + ": " + count)
        );
        System.out.println();

        // Vypište top 5 režisérů dle celkové délky jejich filmů v minutách.
        System.out.println("--- Top 5 reziseru dle celkove delky jejich filmu v minutach ---");
        Map<String, Integer> minutesPerDirector = movies.stream()
                .collect(Collectors.groupingBy(Movie::getDirector, Collectors.summingInt(Movie::getTotalMinutes)));

        minutesPerDirector.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue() + " minut"));
    }
}

class Movie {
    private int movieId;
    private String title;
    private String director;
    private List<Scene> scenes = new ArrayList<>();

    public Movie(int movieId, String title, String director) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.scenes = new ArrayList<>();
    }

    public int getTotalMinutes() {
        return scenes.stream().mapToInt(Scene::getDurationMinutes).sum();
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", totalMinutes=" + getTotalMinutes() +
                '}';
    }
}

class Scene {
    private int movieId;
    private String title;
    private int durationMinutes;

    public Scene(int movieId, String title, int durationMinutes) {
        this.movieId = movieId;
        this.title = title;
        this.durationMinutes = durationMinutes;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String toString() {
        return "Scene{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}
