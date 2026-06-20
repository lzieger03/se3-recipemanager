package de.lzieger03.dhbw.recipemanagement.service;

import de.lzieger03.dhbw.recipemanagement.model.Ingredient;
import de.lzieger03.dhbw.recipemanagement.model.Recipe;
import de.lzieger03.dhbw.recipemanagement.model.RecipeIngredient;
import de.lzieger03.dhbw.recipemanagement.repository.IngredientRepository;
import de.lzieger03.dhbw.recipemanagement.repository.RecipeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeService {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository _recipeRepo;
    private final IngredientRepository _ingredientRepo;

    @Autowired
    public RecipeService(RecipeRepository recipeRepo, IngredientRepository ingredientRepo) {
        _recipeRepo = recipeRepo;
        _ingredientRepo = ingredientRepo;
    }

    public List<Recipe> findAll() {
        return _recipeRepo.findAllByOrderByNameAsc();
    }

    public Optional<Recipe> findById(Long id) {
        return _recipeRepo.findById(id);
    }

    public Recipe save(Recipe recipe) {
        LOG.info("Saving recipe: {}", recipe.getName());
        return _recipeRepo.save(recipe);
    }

    public void deleteById(Long id) {
        LOG.info("Deleting recipe with id={}", id);
        _recipeRepo.deleteById(id);
    }

    @Transactional
    public void cookRecipe(Long id) {
        final Recipe recipe = _recipeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rezept nicht gefunden: " + id));

        for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
            final Ingredient ingredient = ri.getIngredient();
            if (ingredient.getAvailableAmount() < ri.getRequiredAmount()) {
                throw new IllegalStateException(
                        "Nicht genug " + ingredient.getName() + " vorhanden. "
                        + "Benötigt: " + ri.getRequiredAmount() + " " + ingredient.getUnit()
                        + ", vorhanden: " + ingredient.getAvailableAmount() + " " + ingredient.getUnit());
            }
        }

        for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
            final Ingredient ingredient = ri.getIngredient();
            ingredient.setAvailableAmount(ingredient.getAvailableAmount() - ri.getRequiredAmount());
            _ingredientRepo.save(ingredient);
        }

        LOG.info("Cooked recipe '{}', ingredients deducted from stock.", recipe.getName());
    }
}
