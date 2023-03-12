package pl.sda.springrecipe.example_1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AppInitializer {

    private final MovieRepository movieRepository;


    @Autowired
    public AppInitializer(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    void init() {
        movieRepository.save(new Movie(1, "Star Wars Force Awaken", "J.J Ambrams", "Action", 2015));
        movieRepository.save(new Movie(2, "Batman", "Ch. Nolan", "Comics", 2008));
    }
}
