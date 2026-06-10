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

    private final IngredientRepository _ingredientRepo;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepo) {
        _ingredientRepo = ingredientRepo;
    }

    public List<Ingredient> findAll() {
        return _ingredientRepo.findAllByOrderByNameAsc();
    }

    public Optional<Ingredient> findById(Long id) {
        return _ingredientRepo.findById(id);
    }

    public Ingredient save(Ingredient ingredient) {
        LOG.info("Saving ingredient: {}", ingredient.getName());
        return _ingredientRepo.save(ingredient);
    }

    public void deleteById(Long id) {
        LOG.info("Deleting ingredient with id={}", id);
        _ingredientRepo.deleteById(id);
    }
}
