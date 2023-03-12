package pl.sda.springrecipe.recipe;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    //ryż,ser;ser,ryż
    List<Recipe> findAllByIngredientsContains(String ingredients, Pageable pageable);

    List<Recipe> findAllByComplexity(Complexity complexity, Pageable pageable);

    List<Recipe> findAllByDuration(int duration, Pageable pageable);

    Optional<Recipe> findByName(String name);//SELECT * FROM RECIPE WHERE name = 'name'
}
