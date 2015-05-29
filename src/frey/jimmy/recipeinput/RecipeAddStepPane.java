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
public class RecipeAddStepPane extends BorderPane {
    Recipe mRecipe;
    TextArea textAreaInstructions = new TextArea();
    Button btBackToListPane = new Button("Back to recipe list");
    Button btAddMoreIngredients = new Button("Add more ingredients");
    Button btDone = new Button("Save this recipe");
    Button btAddStep = new Button("Save this step and add another step");
    Button btPrevious = new Button("Previous Step");
    TextField tfRecipeImageId = new TextField(("<Enter recipe image ID>"));
    VBox topVBox = new VBox(C.SMALL_MARING_FIXED);
    VBox rightVBox = new VBox(C.SMALL_MARING_FIXED);
    HBox bottomHBox = new HBox(C.SMALL_MARING_FIXED);
    ArrayList<IngredientEditSubPane> ingredientEditSubPaneArrayList = new ArrayList<>();
    private ArrayList<RecipeStep> mRecipeStepArrayList;
    private int mPosition;
    private RecipeStep step;

    public RecipeAddStepPane(Stage primaryStage, Recipe r, int position) {
        this(primaryStage, r, null);
        mPosition = position;
        mRecipeStepArrayList = r.getRecipeStepList();
        if(mPosition < mRecipeStepArrayList.size()){
            step = mRecipeStepArrayList.get(mPosition);
            setStepData();
        }


        btAddStep.setText("Next step");
        btAddStep.setOnAction(e -> {
                primaryStage.setScene(Main.createScene(new RecipeAddStepPane(primaryStage, mRecipe, mPosition + 1)));
        });

        btDone.setText("Save this step");
        btDone.setOnAction(e -> {
            if(mPosition < mRecipeStepArrayList.size()) {
                updateStep();
            } else {
                addStep();
            }
        });
        btPrevious.setOnAction(e -> {
            if (position > 0) {
                primaryStage.setScene(Main.createScene(new RecipeAddStepPane(primaryStage, mRecipe, mPosition - 1)));
            } else {
                primaryStage.setScene(Main.createScene(new RecipeAddPane(primaryStage, mRecipe)));
            }
        });
        bottomHBox.getChildren().add(btPrevious);


    }

    private void setStepData() {
        tfRecipeImageId.setText(step.getImageId() + "");
        textAreaInstructions.setText(step.getInstructions());
        rightVBox.getChildren().clear();
        ingredientEditSubPaneArrayList = new ArrayList<>();
        for (Ingredient ingredient : step.getRecipeStepIngredientList()) {
            IngredientEditSubPane subPane = new IngredientEditSubPane(String.valueOf(ingredient.getQuantity()), ingredient.getUnit(), ingredient.getName());
            ingredientEditSubPaneArrayList.add(subPane);
            rightVBox.getChildren().add(subPane);
        }
        rightVBox.getChildren().add(btAddMoreIngredients);
    }

    private void updateStep() {
        step.setInstructions(textAreaInstructions.getText());
        step.setImageId(Integer.valueOf(tfRecipeImageId.getText()));
        step.setRecipeStepIngredientList(getIngredientAsArrayList());
        RecipeBook.get().saveArrayListRecipe();
    }

    public RecipeAddStepPane(Stage primaryStage, Recipe r, ArrayList<RecipeStep> recipeStepArrayList) {
        this.setPadding(new Insets(C.SMALL_MARING_FIXED));

        mRecipe = r;
        mRecipeStepArrayList = recipeStepArrayList;

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
            ArrayList<Ingredient> recipeStepIngredientList = getIngredientAsArrayList();
            int recipeStepImageId = Integer.valueOf(tfRecipeImageId.getText());
            String instructions = textAreaInstructions.getText();
            RecipeStep stepToAdd = new RecipeStep(recipeStepIngredientList, instructions, recipeStepImageId);
            mRecipeStepArrayList.add(stepToAdd);
            mRecipe.setRecipeStepList(mRecipeStepArrayList);
            System.out.println(mRecipe.getRecipeStepList().size());
            primaryStage.setScene(Main.createScene(new RecipeAddStepPane(primaryStage, mRecipe, mRecipeStepArrayList)));
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
        recipeInfoHBox.getChildren().addAll(tfRecipeImageId);
        topVBox.getChildren().add(recipeInfoHBox);
    }

    private void addRecipe() {
        if (!isValid()) {
            Main.notifyUser("Invalid.  Did not save");
            return;
        }
        ArrayList<Ingredient> recipeStepIngredientList = getIngredientAsArrayList();
        int recipeStepImageId = Integer.valueOf(tfRecipeImageId.getText());
        String instructions = textAreaInstructions.getText();
        RecipeStep stepToAdd = new RecipeStep(recipeStepIngredientList, instructions, recipeStepImageId);
        mRecipeStepArrayList.add(stepToAdd);
        mRecipe.setRecipeStepList(mRecipeStepArrayList);
        RecipeBook.get().addRecipe(mRecipe);
        Main.notifyUser("Saved!");
    }

    private void addStep() {
        if (!isValid()) {
            Main.notifyUser("Invalid.  Did not save");
            return;
        }
        ArrayList<Ingredient> recipeStepIngredientList = getIngredientAsArrayList();
        int recipeStepImageId = Integer.valueOf(tfRecipeImageId.getText());
        String instructions = textAreaInstructions.getText();
        RecipeStep stepToAdd = new RecipeStep(recipeStepIngredientList, instructions, recipeStepImageId);
        mRecipeStepArrayList.add(stepToAdd);
        mRecipe.setRecipeStepList(mRecipeStepArrayList);
        RecipeBook.get().saveArrayListRecipe();
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

