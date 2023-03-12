package pl.sda.springrecipe.recipe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.*;

@RequestMapping("/recipes")
@RestController
class RecipeController {

    private final RecipeService recipeService;

    RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    List<Recipe> getRecipes(
            @RequestParam(required = false) String ingredients,
            @RequestParam(required = false) Complexity complexity,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) SortType sortType,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return recipeService.getRecipes(ingredients, complexity, duration, sortType, page, size);
    }

    @GetMapping("/{id}")
    Recipe getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping
    Recipe addRecipes(@Validated({AddRecipe.class}) @RequestBody Recipe recipe) {
        return recipeService.addRecipe(recipe);
    }

    @DeleteMapping("/{id}")
    Recipe deleteRecipes(@PathVariable Long id) {
        return recipeService.deleteRecipe(id);
    }

    @PatchMapping("/{id}")
    Recipe updateRecipe(@PathVariable Long id,
                        @Validated({UpdateRecipe.class}) @RequestBody Recipe recipe) {
        return recipeService.updateRecipe(id, recipe);
    }

    @ExceptionHandler(NoRecipeFoundException.class)
    private ResponseEntity<Error<String>> mapNoRecipeFoundException(NoRecipeFoundException ex) {
        return new ResponseEntity<>(
                new Error<>(HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecipeAlreadyExistsException.class)
    private ResponseEntity<Error<String>> mapRecipeAlreadyExistsException(RecipeAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new Error<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Error<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(new Error<>(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                ErrorType.VALIDATION
        ), HttpStatus.BAD_REQUEST);
    }
}
