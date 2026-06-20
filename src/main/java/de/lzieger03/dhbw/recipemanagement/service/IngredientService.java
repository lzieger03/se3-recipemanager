package de.lzieger03.dhbw.recipemanagement.service;

import de.lzieger03.dhbw.recipemanagement.model.Ingredient;
import de.lzieger03.dhbw.recipemanagement.repository.IngredientRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {

    private static final Logger LOG = LoggerFactory.getLogger(IngredientService.class);

    private final IngredientRepository ingredientRepo;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepo) {
        ingredientRepo = ingredientRepo;
    }

    public List<Ingredient> findAll() {
        return ingredientRepo.findAllByOrderByNameAsc();
    }

    public Optional<Ingredient> findById(Long id) {
        return ingredientRepo.findById(id);
    }

    public Ingredient save(Ingredient ingredient) {
        LOG.info("Saving ingredient: {}", ingredient.getName());
        return ingredientRepo.save(ingredient);
    }

    public void deleteById(Long id) {
        LOG.info("Deleting ingredient with id={}", id);
        ingredientRepo.deleteById(id);
    }

    public Ingredient findOrCreate(String name, String unit) {
        final String trimmedName = name.trim();
        return ingredientRepo.findByNameIgnoreCase(trimmedName).orElseGet(() -> {
            LOG.info("Creating new ingredient on-the-fly: {}", trimmedName);
            final Ingredient i = new Ingredient();
            i.setName(trimmedName);
            i.setUnit(unit != null && !unit.trim().isEmpty() ? unit.trim() : "g");
            i.setAvailableAmount(0.0);
            return ingredientRepo.save(i);
        });
    }
}
