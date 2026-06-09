package de.lzieger03.dhbw.recipemanagement.repository;

import de.lzieger03.dhbw.recipemanagement.model.Ingredient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAllByOrderByNameAsc();
}
