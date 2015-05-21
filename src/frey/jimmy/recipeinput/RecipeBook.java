package frey.jimmy.recipeinput;



import frey.jimmy.recipe.recipselector.Ingredient;
import frey.jimmy.recipe.recipselector.Recipe;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by James on 5/17/2015.
 */
public class RecipeBook {
    public static final int CATEGORY_SWEET_SAVORY = 0;
    public static final int CATEGORY_LIGHT_HEAVY = 1;
    public static final int CATEGORY_REGION = 2;
    private static RecipeBook sRecipeBook;
    private ObjectOutputStream outputStream;
    private ArrayList<Recipe> mRecipes;
    private File saveFile;
    private ObjectInputStream inputStream;

    private RecipeBook() {

        initialize();

    }

    public static RecipeBook get() {
        if (sRecipeBook == null) {
            sRecipeBook = new RecipeBook();
        }
        return sRecipeBook;
    }

    public ArrayList<Recipe> getRecipes() {
        return mRecipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
    }

    public ArrayList<Recipe> getFilteredRecipes(boolean isSweet, boolean isLight) {
        ArrayList<Recipe> filteredList = new ArrayList<>();
        for (Recipe r : mRecipes) {
            if (r.isSweet() == isSweet && r.isLight() == isLight) {
                filteredList.add(r);
            }
        }
        return filteredList;
    }

    public ArrayList<String> getSpinnerList(int category) {
        Set<String> spinnerSet = new HashSet<>();
        switch (category) {
            case CATEGORY_SWEET_SAVORY:
                spinnerSet.add("Sweet");
                spinnerSet.add("Savory");
                break;
            case CATEGORY_LIGHT_HEAVY:
                spinnerSet.add("Light");
                spinnerSet.add("Heavy");
                break;
            case CATEGORY_REGION:
                spinnerSet.add("");
                for (Recipe r : mRecipes) {
                    spinnerSet.add(r.getRegion());
                }
                break;
        }
        ArrayList<String> list = new ArrayList<>(spinnerSet);
        Collections.sort(list);
        return list;
    }

    public ArrayList<String> getAllIngredientUnits() {
        ArrayList<String> listOfAllUnits = new ArrayList<>();
        listOfAllUnits.add("cups");
        for (Recipe r : mRecipes) {
            for (Ingredient i : r.getRecipeIngredientList()) {
                if (!listOfAllUnits.contains(i.getUnit())) {
                    listOfAllUnits.add(i.getUnit());
                }
            }
        }
        return listOfAllUnits;
    }


    public void addRecipe(Recipe recipe) {
        mRecipes.add(recipe);
        saveArrayListRecipe();
    }

    // Saves the Recipe ArrayList to file.
    public void saveArrayListRecipe() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(saveFile));
            outputStream.writeObject(mRecipes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Main.notifyUser("Error saving to file");
            e.printStackTrace();
        }
    }

    public void loadArrayListRecipe() {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(saveFile));
            mRecipes = (ArrayList<Recipe>) (inputStream.readObject());
            inputStream.close();
        } catch (IOException e) {
            Main.notifyUser("Error loading file");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Main.notifyUser("Class not found exception");
            e.printStackTrace();
        }
    }

    public void initialize() {
        try {
            saveFile = new File(C.SAVE_FILE);

            // Create save file if it does not exist
            if (!saveFile.exists()) {
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
                outputStream = new ObjectOutputStream(new FileOutputStream(saveFile));
                outputStream.writeObject(new ArrayList<Recipe>());
                outputStream.flush();
                outputStream.close();
            }

            // Read from the save file to initialize the Recipe ArrayList.
            loadArrayListRecipe();

        } catch (FileNotFoundException ex) {
            Main.notifyUser("File not found");
            ex.printStackTrace();
        } catch (IOException ex) {
            Main.notifyUser("Error writing to file");
            ex.printStackTrace();
        }

    }
}
