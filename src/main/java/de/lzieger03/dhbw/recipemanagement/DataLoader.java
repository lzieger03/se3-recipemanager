package de.lzieger03.dhbw.recipemanagement;

import de.lzieger03.dhbw.recipemanagement.model.Ingredient;
import de.lzieger03.dhbw.recipemanagement.model.Recipe;
import de.lzieger03.dhbw.recipemanagement.model.RecipeIngredient;
import de.lzieger03.dhbw.recipemanagement.repository.IngredientRepository;
import de.lzieger03.dhbw.recipemanagement.repository.RecipeRepository;
import java.util.HashMap;
import java.util.Map;
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
        final Map<String, Ingredient> ing = seedIngredients();
        seedPancakes(ing);
        seedBolognese(ing);
        seedRuehrei(ing);
        LOG.info("Seeded {} ingredients and {} recipes.", _ingredientRepo.count(), _recipeRepo.count());
    }

    /**
     * Creates and persists all seed ingredients.
     * @return map of shorthand key → saved entity for use in recipe seeding
     */
    private Map<String, Ingredient> seedIngredients() {
        final Map<String, Ingredient> ing = new HashMap<>();
        ing.put("mehl",        saveIngredient("Mehl",              "g",      1000.0));
        ing.put("eier",        saveIngredient("Eier",              "Stück",    12.0));
        ing.put("milch",       saveIngredient("Milch",             "ml",     1000.0));
        ing.put("butter",      saveIngredient("Butter",            "g",       250.0));
        ing.put("salz",        saveIngredient("Salz",              "g",       200.0));
        ing.put("zucker",      saveIngredient("Zucker",            "g",       500.0));
        ing.put("olivenoel",   saveIngredient("Olivenöl",          "ml",      200.0));
        ing.put("nudeln",      saveIngredient("Spaghetti",         "g",       500.0));
        ing.put("tomaten",     saveIngredient("Tomaten (gehackt)", "g",       800.0));
        ing.put("parmesan",    saveIngredient("Parmesan",          "g",       150.0));
        ing.put("zwiebeln",    saveIngredient("Zwiebeln",          "Stück",     5.0));
        ing.put("hackfleisch", saveIngredient("Hackfleisch",       "g",       400.0));
        return ing;
    }

    /** Seeds the Pancakes recipe with its ingredients. */
    private void seedPancakes(Map<String, Ingredient> ing) {
        final Recipe r = recipe("Pancakes", "Fluffige amerikanische Pfannkuchen",
            "1. Mehl, Zucker und Salz in einer Schüssel mischen.\n"
            + "2. Eier und Milch unterrühren bis ein glatter Teig entsteht.\n"
            + "3. Butter in einer beschichteten Pfanne bei mittlerer Hitze schmelzen.\n"
            + "4. Jeweils eine Kelle Teig hineingeben und goldbraun backen.\n"
            + "5. Wenden und weitere 1-2 Minuten fertigbacken.", 4);
        addIngredient(r, ing.get("mehl"),   200.0);
        addIngredient(r, ing.get("eier"),     2.0);
        addIngredient(r, ing.get("milch"),  300.0);
        addIngredient(r, ing.get("butter"),  30.0);
        addIngredient(r, ing.get("salz"),     2.0);
        addIngredient(r, ing.get("zucker"),  20.0);
        _recipeRepo.save(r);
    }

    /** Seeds the Spaghetti Bolognese recipe with its ingredients. */
    private void seedBolognese(Map<String, Ingredient> ing) {
        final Recipe r = recipe("Spaghetti Bolognese",
            "Klassische Bolognese mit selbstgemachter Tomatensauce",
            "1. Zwiebeln fein würfeln und in Olivenöl glasig dünsten.\n"
            + "2. Hackfleisch dazugeben und krümelig anbraten.\n"
            + "3. Tomaten unterrühren, mit Salz würzen.\n"
            + "4. 20 Minuten köcheln lassen.\n"
            + "5. Spaghetti nach Packungsanleitung kochen.\n"
            + "6. Mit Parmesan servieren.", 2);
        addIngredient(r, ing.get("nudeln"),       200.0);
        addIngredient(r, ing.get("hackfleisch"),  300.0);
        addIngredient(r, ing.get("tomaten"),      400.0);
        addIngredient(r, ing.get("zwiebeln"),       1.0);
        addIngredient(r, ing.get("olivenoel"),     20.0);
        addIngredient(r, ing.get("salz"),           3.0);
        addIngredient(r, ing.get("parmesan"),      40.0);
        _recipeRepo.save(r);
    }

    /** Seeds the Rührei recipe with its ingredients. */
    private void seedRuehrei(Map<String, Ingredient> ing) {
        final Recipe r = recipe("Rührei", "Schnelles Frühstück für zwei",
            "1. Eier mit Milch und Salz verquirlen.\n"
            + "2. Butter in der Pfanne bei niedriger Hitze schmelzen.\n"
            + "3. Eiermasse hineingeben und langsam unter Rühren stocken lassen.\n"
            + "4. Vom Herd nehmen, wenn das Ei noch leicht cremig ist.", 2);
        addIngredient(r, ing.get("eier"),    3.0);
        addIngredient(r, ing.get("milch"),  30.0);
        addIngredient(r, ing.get("butter"), 15.0);
        addIngredient(r, ing.get("salz"),    1.0);
        _recipeRepo.save(r);
    }

    // --- helpers ---

    /** Creates a new {@link Ingredient} and persists it immediately. */
    private Ingredient saveIngredient(String name, String unit, double amount) {
        final Ingredient i = new Ingredient();
        i.setName(name);
        i.setUnit(unit);
        i.setAvailableAmount(amount);
        return _ingredientRepo.save(i);
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
