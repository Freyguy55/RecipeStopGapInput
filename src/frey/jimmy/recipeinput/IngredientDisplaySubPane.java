package frey.jimmy.recipeinput;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 *
 */
public class IngredientDisplaySubPane extends HBox {
    private Label lbIngredientQuantity = new Label();
    private ComboBox<String> comboBoxIngredientUnit = new ComboBox<>();
    private Label lbIngredientName = new Label();
    Ingredient ingredient;

    public IngredientDisplaySubPane(Ingredient ingredient) {
        super(C.SMALL_MARING_FIXED);
        this.setAlignment(Pos.BASELINE_LEFT);

        this.ingredient = ingredient;

        lbIngredientQuantity.setText(String.valueOf(ingredient.getQuantity()));
        lbIngredientQuantity.setPrefWidth(C.INGREDIENT_LIST_SIZE);
        lbIngredientQuantity.setAlignment(Pos.BASELINE_CENTER);

        comboBoxIngredientUnit.getItems().addAll(RecipeBook.getAllUnits());
        comboBoxIngredientUnit.getSelectionModel().select(ingredient.getUnit());
        comboBoxIngredientUnit.setPrefWidth(C.INGREDIENT_LIST_SIZE);

        lbIngredientName.setText(ingredient.getName());
        lbIngredientName.setPrefWidth(C.INGREDIENT_LIST_SIZE);
        lbIngredientName.setAlignment(Pos.BASELINE_CENTER);

        this.getChildren().addAll(lbIngredientQuantity, comboBoxIngredientUnit, lbIngredientName);

    }

}
