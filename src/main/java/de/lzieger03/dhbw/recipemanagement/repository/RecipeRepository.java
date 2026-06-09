package de.lzieger03.dhbw.recipemanagement.repository;

import de.lzieger03.dhbw.recipemanagement.model.Recipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByOrderByNameAsc();
}
