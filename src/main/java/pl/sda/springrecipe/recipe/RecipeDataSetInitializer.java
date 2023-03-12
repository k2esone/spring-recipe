package pl.sda.springrecipe.recipe;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
class RecipeDataSetInitializer {

    private final RecipeRepository recipeRepository;

    RecipeDataSetInitializer(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @PostConstruct
    void init() {
        recipeRepository.saveAll(Arrays.asList(
                new Recipe("Gołąbki", "Gołąbki z kapustą", 120, 4, "Kapusta,Mięso wieprzowe,Ryż", Complexity.EASY),
                new Recipe("Pizza", "Pizza domowa", 60, 3, "Ser,Sos pomidorowy, szynka", Complexity.STANDARD),
                new Recipe("Gołąbki 1", "Gołąbki z kapustą 1", 120, 4, "Kapusta,Mięso wieprzowe,Ryż", Complexity.EASY),
                new Recipe("Pizza 1", "Pizza domowa 1", 60, 3, "Ser,Sos pomidorowy, szynka", Complexity.STANDARD),
                new Recipe("Gołąbki 2", "Gołąbki z kapustą 2", 120, 4, "Kapusta,Mięso wieprzowe,Ryż", Complexity.EASY),
                new Recipe("Pizza 2", "Pizza domowa 2", 60, 3, "Ser,Sos pomidorowy, szynka", Complexity.STANDARD)
        ));
    }
}
