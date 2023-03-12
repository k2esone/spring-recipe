package pl.sda.springrecipe.recipe;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class RecipeService {

    private final RecipeRepository recipeRepository;

    RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    List<Recipe> getRecipes(
            String ingredients,
            Complexity complexity,
            Integer duration,
            SortType sortType,
            Integer page,
            Integer size) {
        Pageable pageable = providePageable(page, size, sortType);
        if (ingredients != null) {
            return recipeRepository.findAllByIngredientsContains(ingredients, pageable);
        } else if(complexity != null) {
            return recipeRepository.findAllByComplexity(complexity, pageable);
        } else if(duration != null) {
            return recipeRepository.findAllByDuration(duration, pageable);
        }
        return recipeRepository.findAll(pageable).toList();

//        if (sortType != null && page != null && size != null) {
//            Sort.Direction direction = SortType.DESC == sortType ? Sort.Direction.DESC : Sort.Direction.ASC;
//            Sort sort = Sort.by(direction, "name");
//            Pageable pageable = PageRequest.of(page,size, sort);
//            return recipeRepository.findAll(pageable).toList();
//        } else if(page != null && size != null) {
//            Pageable pageable = PageRequest.of(page,size);
//            return recipeRepository.findAll(pageable).toList();
//        } else if(sortType != null) {
//            Sort.Direction direction = SortType.DESC == sortType ? Sort.Direction.DESC : Sort.Direction.ASC;
//            Sort sort = Sort.by(direction, "name");
//            return recipeRepository.findAll(sort);
//        } else {
//            return recipeRepository.findAll();
//        }
        //return recipeRepository.findAll(pageable).toList();
    }

    Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(
                ()->new NoRecipeFoundException(id)
        );
    }

    Recipe addRecipe(Recipe recipe) {
        String recipeName = recipe.getName();
        checkIfRecipeNameIsUnique(recipeName);
        return recipeRepository.save(recipe);
    }

    Recipe deleteRecipe(Long id) {
        Recipe recipeFromDb = getRecipeById(id);
        recipeRepository.delete(recipeFromDb);
        return recipeFromDb;
    }

    Recipe updateRecipe(Long id, Recipe recipe) {
        Recipe recipeToUpdate = getRecipeById(id);
        String recipeName = recipe.getName();
        if (recipeName != null && !recipeName.equals(recipeToUpdate.getName())) {
            checkIfRecipeNameIsUnique(recipeName);
            recipeToUpdate.setName(recipeName);
        }
        String recipeDescription = recipe.getDescription();
        if (recipeDescription != null && !recipeDescription.equals(recipeToUpdate.getDescription())) {
            recipeToUpdate.setDescription(recipeDescription);
        }
        Integer recipeDuration = recipe.getDuration();
        if (recipeDuration != null && !recipeDuration.equals(recipeToUpdate.getDuration())) {
            recipeToUpdate.setDuration(recipeDuration);
        }
        Integer recipeNumberOfPeople = recipe.getNumberOfPeople();
        if (recipeNumberOfPeople != null && !recipeNumberOfPeople.equals(recipeToUpdate.getNumberOfPeople())) {
            recipeToUpdate.setNumberOfPeople(recipeNumberOfPeople);
        }
        String recipeIngredients = recipe.getIngredients();
        if (recipeIngredients != null && !recipeIngredients.equals(recipeToUpdate.getIngredients())) {
            recipeToUpdate.setIngredients(recipeIngredients);
        }
        return recipeRepository.save(recipeToUpdate);
    }

    private void checkIfRecipeNameIsUnique(String recipeName) {
        recipeRepository.findByName(recipeName).ifPresent(r -> {
            throw new RecipeAlreadyExistsException(recipeName);
        });
    }

    Pageable providePageable(Integer page, Integer size, SortType sortType) {
        //4 warianty do wsparcia:
        //- paginacja
        //- sortowanie
        //- paginacja + sortowanie
        //- brak paginacji i sortowania
        Sort.Direction direction = SortType.DESC == sortType ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "name");
        return PageRequest.of(
                page != null && size != null ? page : 0,
                page != null && size != null ? size : (int) recipeRepository.count(), sort);
    }
}
