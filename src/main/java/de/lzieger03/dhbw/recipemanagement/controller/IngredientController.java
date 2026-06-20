package de.lzieger03.dhbw.recipemanagement.controller;

import de.lzieger03.dhbw.recipemanagement.model.Ingredient;
import de.lzieger03.dhbw.recipemanagement.service.IngredientService;
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
@RequestMapping("/ingredients")
public class IngredientController {

    private static final Logger LOG = LoggerFactory.getLogger(IngredientController.class);

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("ingredients", ingredientService.findAll());
        return "ingredients/list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("ingredient", new Ingredient());
        return "ingredients/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        final var ingredientOptional = ingredientService.findById(id);
        if (ingredientOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Zutat nicht gefunden.");
            return "redirect:/ingredients";
        }
        model.addAttribute("ingredient", ingredientOptional.get());
        return "ingredients/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(required = false) Long ingredientId,
                       @RequestParam String name,
                       @RequestParam double availableAmount,
                       @RequestParam String unit,
                       RedirectAttributes redirectAttributes) {
        final Ingredient ingredient;
        if (ingredientId != null) {
            ingredient = ingredientService.findById(ingredientId).orElse(new Ingredient());
        } else {
            ingredient = new Ingredient();
        }
        ingredient.setName(name);
        ingredient.setAvailableAmount(availableAmount);
        ingredient.setUnit(unit);
        ingredientService.save(ingredient);
        LOG.info("Saved ingredient: {}", name);
        redirectAttributes.addFlashAttribute("successMessage", "Zutat gespeichert.");
        return "redirect:/ingredients";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ingredientService.deleteById(id);
        LOG.info("Deleted ingredient with id={}", id);
        redirectAttributes.addFlashAttribute("successMessage", "Zutat gelöscht.");
        return "redirect:/ingredients";
    }
}
