package de.lzieger03.dhbw.recipemanagement;

import de.lzieger03.dhbw.recipemanagement.model.Ingredient;
import de.lzieger03.dhbw.recipemanagement.model.Recipe;
import de.lzieger03.dhbw.recipemanagement.model.RecipeIngredient;
import de.lzieger03.dhbw.recipemanagement.repository.IngredientRepository;
import de.lzieger03.dhbw.recipemanagement.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);

    private final IngredientRepository _ingredientRepo;
    private final RecipeRepository _recipeRepo;

    @Autowired
    public DataLoader(IngredientRepository ingredientRepo, RecipeRepository recipeRepo) {
        _ingredientRepo = ingredientRepo;
        _recipeRepo = recipeRepo;
    }

    @Override
    public void run(String... args) {
        if (_ingredientRepo.count() > 0) {
            LOG.info("Test data already present – skipping seed.");
            return;
        }

        LOG.info("Seeding test data...");

        // --- Ingredients ---
        Ingredient mehl      = ingredient("Mehl",             "g",     1000.0);
        Ingredient eier      = ingredient("Eier",             "Stück",   12.0);
        Ingredient milch     = ingredient("Milch",            "ml",    1000.0);
        Ingredient butter    = ingredient("Butter",           "g",      250.0);
        Ingredient salz      = ingredient("Salz",             "g",      200.0);
        Ingredient zucker    = ingredient("Zucker",           "g",      500.0);
        Ingredient olivenoel = ingredient("Olivenöl",         "ml",     200.0);
        Ingredient nudeln    = ingredient("Spaghetti",        "g",      500.0);
        Ingredient tomaten   = ingredient("Tomaten (gehackt)","g",      800.0);
        Ingredient parmesan  = ingredient("Parmesan",         "g",      150.0);
        Ingredient zwiebeln  = ingredient("Zwiebeln",         "Stück",    5.0);
        Ingredient hackfleisch = ingredient("Hackfleisch",    "g",      400.0);

        _ingredientRepo.save(mehl);
        _ingredientRepo.save(eier);
        _ingredientRepo.save(milch);
        _ingredientRepo.save(butter);
        _ingredientRepo.save(salz);
        _ingredientRepo.save(zucker);
        _ingredientRepo.save(olivenoel);
        _ingredientRepo.save(nudeln);
        _ingredientRepo.save(tomaten);
        _ingredientRepo.save(parmesan);
        _ingredientRepo.save(zwiebeln);
        _ingredientRepo.save(hackfleisch);

        // --- Recipes ---

        // Pancakes
        Recipe pancakes = recipe(
            "Pancakes",
            "Fluffige amerikanische Pfannkuchen",
            "1. Mehl, Zucker und Salz in einer Schüssel mischen.\n" +
            "2. Eier und Milch unterrühren bis ein glatter Teig entsteht.\n" +
            "3. Butter in einer beschichteten Pfanne bei mittlerer Hitze schmelzen.\n" +
            "4. Jeweils eine Kelle Teig hineingeben und goldbraun backen.\n" +
            "5. Wenden und weitere 1-2 Minuten fertigbacken.",
            4
        );
        addIngredient(pancakes, mehl,   200.0);
        addIngredient(pancakes, eier,     2.0);
        addIngredient(pancakes, milch,  300.0);
        addIngredient(pancakes, butter,  30.0);
        addIngredient(pancakes, salz,     2.0);
        addIngredient(pancakes, zucker,  20.0);
        _recipeRepo.save(pancakes);

        // Spaghetti Bolognese
        Recipe bolognese = recipe(
            "Spaghetti Bolognese",
            "Klassische Bolognese mit selbstgemachter Tomatensauce",
            "1. Zwiebeln fein würfeln und in Olivenöl glasig dünsten.\n" +
            "2. Hackfleisch dazugeben und krümelig anbraten.\n" +
            "3. Tomaten unterrühren, mit Salz würzen.\n" +
            "4. 20 Minuten köcheln lassen.\n" +
            "5. Spaghetti nach Packungsanleitung kochen.\n" +
            "6. Mit Parmesan servieren.",
            2
        );
        addIngredient(bolognese, nudeln,      200.0);
        addIngredient(bolognese, hackfleisch, 300.0);
        addIngredient(bolognese, tomaten,     400.0);
        addIngredient(bolognese, zwiebeln,      1.0);
        addIngredient(bolognese, olivenoel,    20.0);
        addIngredient(bolognese, salz,          3.0);
        addIngredient(bolognese, parmesan,     40.0);
        _recipeRepo.save(bolognese);

        // Rührei
        Recipe ruehrei = recipe(
            "Rührei",
            "Schnelles Frühstück für zwei",
            "1. Eier mit Milch und Salz verquirlen.\n" +
            "2. Butter in der Pfanne bei niedriger Hitze schmelzen.\n" +
            "3. Eiermasse hineingeben und langsam unter Rühren stocken lassen.\n" +
            "4. Vom Herd nehmen, wenn das Ei noch leicht cremig ist.",
            2
        );
        addIngredient(ruehrei, eier,   3.0);
        addIngredient(ruehrei, milch, 30.0);
        addIngredient(ruehrei, butter,15.0);
        addIngredient(ruehrei, salz,   1.0);
        _recipeRepo.save(ruehrei);

        LOG.info("Seeded {} ingredients and {} recipes.", _ingredientRepo.count(), _recipeRepo.count());
    }

    // --- helpers ---

    private Ingredient ingredient(String name, String unit, double amount) {
        final Ingredient i = new Ingredient();
        i.setName(name);
        i.setUnit(unit);
        i.setAvailableAmount(amount);
        return i;
    }

    private Recipe recipe(String name, String description, String instructions, int portions) {
        final Recipe r = new Recipe();
        r.setName(name);
        r.setDescription(description);
        r.setInstructions(instructions);
        r.setPortions(portions);
        return r;
    }

    private void addIngredient(Recipe recipe, Ingredient ingredient, double amount) {
        final RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipe(recipe);
        ri.setIngredient(ingredient);
        ri.setRequiredAmount(amount);
        recipe.getRecipeIngredients().add(ri);
    }
}
