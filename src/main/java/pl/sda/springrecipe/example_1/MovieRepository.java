package pl.sda.springrecipe.example_1;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findAllByTitleContains(String text);//SELECT * FROM MOVIE WHERE title LIKE %text%
}
