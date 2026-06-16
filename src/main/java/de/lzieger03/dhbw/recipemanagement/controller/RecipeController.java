package de.lzieger03.dhbw.recipemanagement.controller;

import de.lzieger03.dhbw.recipemanagement.model.Ingredient;
import de.lzieger03.dhbw.recipemanagement.model.Recipe;
import de.lzieger03.dhbw.recipemanagement.model.RecipeIngredient;
import de.lzieger03.dhbw.recipemanagement.service.IngredientService;
import de.lzieger03.dhbw.recipemanagement.service.RecipeService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeController.class);

    private final RecipeService _recipeService;
    private final IngredientService _ingredientService;

    @Autowired
    public RecipeController(RecipeService recipeService, IngredientService ingredientService) {
        _recipeService = recipeService;
        _ingredientService = ingredientService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("recipes", _recipeService.findAll());
        return "recipes/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        final var recipeOptional = _recipeService.findById(id);
        if (recipeOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Recipe not found.");
            return "redirect:/recipes";
        }
        model.addAttribute("recipe", recipeOptional.get());
        return "recipes/detail";
    }

    @GetMapping("/{id}/cook")
    public String cookMode(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        final var recipeOptional = _recipeService.findById(id);
        if (recipeOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Recipe not found.");
            return "redirect:/recipes";
        }
        final Recipe recipe = recipeOptional.get();
        final boolean canCook = recipe.getRecipeIngredients().stream()
                .allMatch(ri -> ri.getIngredient().getAvailableAmount() >= ri.getRequiredAmount());
        model.addAttribute("recipe", recipe);
        model.addAttribute("canCook", canCook);
        return "recipes/cook";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("allIngredients", _ingredientService.findAll());
        return "recipes/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        final var recipeOptional = _recipeService.findById(id);
        if (recipeOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Recipe not found.");
            return "redirect:/recipes";
        }
        model.addAttribute("recipe", recipeOptional.get());
        model.addAttribute("allIngredients", _ingredientService.findAll());
        return "recipes/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(required = false) Long recipeId,
                       @RequestParam String name,
                       @RequestParam(required = false) String description,
                       @RequestParam(required = false) String instructions,
                       @RequestParam int portions,
                       @RequestParam(required = false) List<String> ingredientNames,
                       @RequestParam(required = false) List<String> ingredientUnits,
                       @RequestParam(required = false) List<Double> requiredAmounts,
                       RedirectAttributes redirectAttributes) {

        final Recipe recipe;
        if (recipeId != null) {
            recipe = _recipeService.findById(recipeId).orElse(new Recipe());
        } else {
            recipe = new Recipe();
        }

        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setInstructions(instructions);
        recipe.setPortions(portions);

        recipe.getRecipeIngredients().clear();

        if (ingredientNames != null && requiredAmounts != null) {
            for (int i = 0; i < ingredientNames.size(); i++) {
                final String ingredientName = ingredientNames.get(i);
                if (ingredientName == null || ingredientName.isBlank()) {
                    continue;
                }
                final String unit = (ingredientUnits != null && i < ingredientUnits.size())
                        ? ingredientUnits.get(i) : "g";
                final var ingredient = _ingredientService.findOrCreate(ingredientName, unit);
                final RecipeIngredient ri = new RecipeIngredient();
                ri.setRecipe(recipe);
                ri.setIngredient(ingredient);
                ri.setRequiredAmount(requiredAmounts.get(i));
                recipe.getRecipeIngredients().add(ri);
            }
        }

        _recipeService.save(recipe);
        LOG.info("Saved recipe: {}", name);
        redirectAttributes.addFlashAttribute("successMessage", "Recipe saved.");
        return "redirect:/recipes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        _recipeService.deleteById(id);
        LOG.info("Deleted recipe with id={}", id);
        redirectAttributes.addFlashAttribute("successMessage", "Recipe deleted.");
        return "redirect:/recipes";
    }

    @PostMapping("/{id}/cook")
    public String cook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            _recipeService.cookRecipe(id);
            redirectAttributes.addFlashAttribute("successMessage", "Gekocht! Zutaten wurden abgezogen.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/recipes/" + id;
    }
}
