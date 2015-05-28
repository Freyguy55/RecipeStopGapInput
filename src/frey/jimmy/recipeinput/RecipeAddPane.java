package frey.jimmy.recipeinput;

import frey.jimmy.recipe.recipselector.Ingredient;
import frey.jimmy.recipe.recipselector.Recipe;
import frey.jimmy.recipe.recipselector.RecipeStep;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Screen for adding recipes to the saved recipe list.
 */
public class RecipeAddPane extends BorderPane {
    Recipe recipe;
    TextArea textAreaInstructions = new TextArea();
    Button btBackToListPane = new Button("Back to recipe list");
    Button btAddMoreIngredients = new Button("Add more ingredients");
    Button btDone = new Button("Done");
    Button btAddStep = new Button("Add a step to this recipe");
    ComboBox<String> cbCategoryLightness = new ComboBox<>();
    ComboBox<String> cbCategorySweetness = new ComboBox<>();
    ComboBox<String> cbCategoryRegion = new ComboBox<>();
    TextField tfRecipeName = new TextField("<Enter Recipe Name>");
    TextField tfTimerMinutes = new TextField("<Enter cook time>");
    TextField tfRecipeDescription = new TextField("<Brief description>");
    TextField tfRecipeServesNumber = new TextField("<Enter number of people it serves");
    TextField tfRecipeImageId = new TextField("<Enter final imageID>");
    TextField tfRecipeStepImageId = new TextField("<Enter step ImageID>");
    VBox topVBox = new VBox(C.SMALL_MARING_FIXED);
    HBox recipeCategoriesHBox = new HBox(C.SMALL_MARING_FIXED);
    VBox rightVBox = new VBox(C.SMALL_MARING_FIXED);
    HBox bottomHBox = new HBox(C.SMALL_MARING_FIXED);
    ArrayList<IngredientEditSubPane> ingredientEditSubPaneArrayList = new ArrayList<>();

    public RecipeAddPane(Stage primaryStage, Recipe r){
        this(primaryStage);
        recipe = r;
        setRecipeData();
        btAddStep.setText("Go to next step");
        btDone.setText("Update recipe");
        btAddStep.setOnAction(e -> {
            primaryStage.setScene(Main.createScene(new RecipeAddStepPane(primaryStage, recipe, 0)));
        });

        btDone.setOnAction(e -> {
            updateRecipe();
        });
    }
    //Set everything in the recipe
    private void updateRecipe() {
        recipe.setRecipeName(tfRecipeName.getText());
        RecipeBook.get().saveArrayListRecipe();
    }

    private void setRecipeData() {
        tfRecipeName.setText(recipe.getRecipeName());
        tfRecipeDescription.setText(recipe.getRecipeDescription());
        tfTimerMinutes.setText(recipe.getTotalMinutes() +"");
        tfRecipeServesNumber.setText(recipe.getServesNumber()+"");
        tfRecipeImageId.setText(recipe.getRecipeImageId()+"");
        tfRecipeStepImageId.setText(recipe.getRecipeStepImageId()+"");
        textAreaInstructions.setText(recipe.getInstructions());

        for (Ingredient ingredient : recipe.getRecipeIngredientList()) {
            rightVBox.getChildren().add(new IngredientDisplaySubPane(ingredient));
        }
    }


    public RecipeAddPane(Stage primaryStage) {
        this.setPadding(new Insets(C.SMALL_MARING_FIXED));

        //Configure center of BorderPane
        this.setCenter(textAreaInstructions);

        //Configure top of BorderPane
        this.setTop(topVBox);
        initializeTopVBox();

        //Configure right of BorderPane
        this.setRight(rightVBox);
        initializeRightVBox();

        //Configure bottom of BorderPane
        this.setBottom(bottomHBox);
        initializeBottomHBox();

        //Configure Button Actions
        btBackToListPane.setOnAction(e -> {
            primaryStage.setScene(Main.createScene(new RecipeSelectionPane(primaryStage, RecipeBook.get().getRecipes())));
        });

        btAddMoreIngredients.setOnAction(e -> {
            addIngredientPane();
        });

        btAddStep.setOnAction(e -> {
            String recipeName = tfRecipeName.getText();
            String recipeDescription = tfRecipeDescription.getText();
            int servesNumber = Integer.valueOf(tfRecipeServesNumber.getText());
            boolean isSweet = cbCategorySweetness.getValue().equals("Sweet");
            boolean isLight = cbCategoryLightness.getValue().equals("Light");
            int totalMinutes = Integer.valueOf(tfTimerMinutes.getText());
            String region = cbCategoryRegion.getValue();
            ArrayList<RecipeStep> recipeStepList = new ArrayList<RecipeStep>();
            ArrayList<Ingredient> recipeIngredientList = getIngredientAsArrayList();
            int recipeImageId = Integer.valueOf(tfRecipeImageId.getText());
            int recipeStepImageId = Integer.valueOf(tfRecipeStepImageId.getText());
            int isGood = 2;
            String instructions = textAreaInstructions.getText();


            recipe = new Recipe(recipeName, recipeDescription, servesNumber,
                    isSweet, isLight, totalMinutes, region, recipeStepList,
                    recipeIngredientList, recipeImageId, recipeStepImageId, isGood, instructions);
            primaryStage.setScene(Main.createScene(new RecipeAddStepPane(primaryStage, recipe, recipeStepList)));
        });

        btDone.setOnAction(e -> {
            addRecipe();
        });


    }

    private void initializeBottomHBox() {
        HBox rightHBox = new HBox(C.SMALL_MARING_FIXED);
        bottomHBox.setHgrow(rightHBox, Priority.ALWAYS);
        rightHBox.setAlignment(Pos.CENTER_RIGHT);
        rightHBox.getChildren().add(btAddStep);
        rightHBox.getChildren().add(btDone);
        bottomHBox.getChildren().addAll(btBackToListPane, rightHBox);
    }

    private void addIngredientPane() {
        ingredientEditSubPaneArrayList.add(new IngredientEditSubPane());
        ObservableList<Node> listOfNodes = rightVBox.getChildren();
        rightVBox.getChildren().add(rightVBox.getChildren().size() - 1, ingredientEditSubPaneArrayList.get(ingredientEditSubPaneArrayList.size() - 1));
    }

    private void initializeRightVBox() {
        HBox labelHBox = new HBox(C.SMALL_MARING_FIXED);
        labelHBox.setAlignment(Pos.BASELINE_LEFT);

        Label lbQuantity = new Label("Qty");
        lbQuantity.setPrefWidth(C.INGREDIENT_LIST_SIZE);
        lbQuantity.setAlignment(Pos.BASELINE_CENTER);

        Label lbUnits = new Label("Units");
        lbUnits.setPrefWidth(C.INGREDIENT_LIST_SIZE);
        lbUnits.setAlignment(Pos.BASELINE_CENTER);

        Label lbIngredient = new Label("Ingredient");
        lbIngredient.setPrefWidth(C.INGREDIENT_LIST_SIZE);
        lbIngredient.setAlignment(Pos.BASELINE_CENTER);

        labelHBox.getChildren().addAll(lbQuantity, lbUnits, lbIngredient);
        rightVBox.getChildren().add(labelHBox);


        for (int i = 0; i < 10; i++) {
            ingredientEditSubPaneArrayList.add(new IngredientEditSubPane());
            rightVBox.getChildren().add(ingredientEditSubPaneArrayList.get(i));
        }
        rightVBox.getChildren().add(btAddMoreIngredients);
    }

    private void initializeTopVBox() {
        HBox recipeInfoHBox = new HBox(C.SMALL_MARING_FIXED);
        recipeInfoHBox.setPadding(new Insets(C.SMALL_MARING_FIXED));
        recipeInfoHBox.getChildren().addAll(tfRecipeName, tfRecipeDescription, tfTimerMinutes, tfRecipeServesNumber, tfRecipeImageId, tfRecipeStepImageId);
        topVBox.getChildren().add(recipeInfoHBox);

        recipeCategoriesHBox.setAlignment(Pos.BASELINE_LEFT);
        cbCategorySweetness.setEditable(true);
        cbCategorySweetness.getItems().addAll(RecipeBook.get().getSpinnerList(RecipeBook.CATEGORY_SWEET_SAVORY));

        recipeCategoriesHBox.setAlignment(Pos.BASELINE_LEFT);
        cbCategoryLightness.setEditable(true);
        cbCategoryLightness.getItems().addAll(RecipeBook.get().getSpinnerList(RecipeBook.CATEGORY_LIGHT_HEAVY));

        recipeCategoriesHBox.setAlignment(Pos.BASELINE_LEFT);
        cbCategoryRegion.setEditable(true);
        cbCategoryRegion.getItems().addAll(RecipeBook.get().getSpinnerList(RecipeBook.CATEGORY_REGION));

        recipeCategoriesHBox.getChildren().addAll(new Label("Sweetness: "), cbCategorySweetness, new Label(" Lighntess: "), cbCategoryLightness, new Label(" Region: "), cbCategoryRegion);
        topVBox.getChildren().add(recipeCategoriesHBox);
    }

    private void addRecipe() {
        if (!isValid()) {
            Main.notifyUser("Invalid.  Did not save");
            return;
        }
        String recipeName = tfRecipeName.getText();
        String recipeDescription = tfRecipeDescription.getText();
        int servesNumber = Integer.valueOf(tfRecipeServesNumber.getText());
        boolean isSweet = cbCategorySweetness.getValue().equals("Sweet");
        boolean isLight = cbCategoryLightness.getValue().equals("Light");
        int totalMinutes = Integer.valueOf(tfTimerMinutes.getText());
        String region = cbCategoryRegion.getValue();
        ArrayList<RecipeStep> recipeStepList = null;
        ArrayList<Ingredient> recipeIngredientList = getIngredientAsArrayList();
        int recipeImageId = Integer.valueOf(tfRecipeImageId.getText());
        int recipeStepImageId = Integer.valueOf(tfRecipeStepImageId.getText());
        int isGood = 2;
        String instructions = textAreaInstructions.getText();


        recipe = new Recipe(recipeName, recipeDescription, servesNumber,
                isSweet, isLight, totalMinutes, region, recipeStepList,
                recipeIngredientList, recipeImageId, recipeStepImageId, isGood, instructions);
        RecipeBook.get().addRecipe(recipe);
        Main.notifyUser("Saved!");

    }

    private ArrayList<Ingredient> getIngredientAsArrayList() {
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        for (IngredientEditSubPane i : ingredientEditSubPaneArrayList) {
            Ingredient ingredient = i.getIngredient();
            if (ingredient != null) {
                ingredientArrayList.add(ingredient);
            }
        }
        return ingredientArrayList;
    }

    private boolean isValid() {
        return true;
    }

}

