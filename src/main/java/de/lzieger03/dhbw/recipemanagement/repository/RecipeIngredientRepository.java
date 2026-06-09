package de.lzieger03.dhbw.recipemanagement.repository;

import de.lzieger03.dhbw.recipemanagement.model.Recipe;
import de.lzieger03.dhbw.recipemanagement.model.RecipeIngredient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    List<RecipeIngredient> findByRecipe(Recipe recipe);
}
