package frey.jimmy.recipeinput;

import frey.jimmy.recipe.recipselector.Ingredient;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 */
public class IngredientEditSubPane extends HBox {
    private TextField tfIngredientQuantity = new TextField();
    private ComboBox<String> comboBoxIngredientUnit = new ComboBox<>();
    private TextField tfIngredientName = new TextField();

    public IngredientEditSubPane() {
        this("", "", "");
    }

    public IngredientEditSubPane(String ingredientQuantity, String ingredientUnit, String ingredientName) {
        super(C.SMALL_MARING_FIXED);

        tfIngredientQuantity.setText(ingredientQuantity);
        tfIngredientQuantity.setPrefWidth(C.INGREDIENT_LIST_SIZE);

        comboBoxIngredientUnit.getItems().addAll(RecipeBook.get().getAllIngredientUnits());
        comboBoxIngredientUnit.setEditable(true);
        comboBoxIngredientUnit.setPrefWidth(C.INGREDIENT_LIST_SIZE);

        tfIngredientName.setText(ingredientName);
        tfIngredientName.setPrefWidth(C.INGREDIENT_LIST_SIZE);

        this.getChildren().addAll(tfIngredientQuantity, comboBoxIngredientUnit, tfIngredientName);
    }

    public Ingredient getIngredient() {
        if (!isValid()) {
            return null;
        }
        double quantity = Double.parseDouble(tfIngredientQuantity.getText());
        String unit = comboBoxIngredientUnit.getValue();
        String ingredientName = tfIngredientName.getText();
        return new Ingredient(quantity, unit, ingredientName);
    }

    private boolean isValid() {
        tfIngredientName.setStyle(null);
        tfIngredientQuantity.setStyle(null);
        try {
            //If quantity and name fields are empty return false without notifications.
            if (tfIngredientQuantity.getText().length() == 0 && tfIngredientName.getText().length() == 0) {
                return false;
            }

            double quantity = Double.parseDouble(tfIngredientQuantity.getText());
            String ingredientName = tfIngredientName.getText();

            // If quantity field has a number but name field is empty, notify user of missing name field. return false.
            if (quantity != 0 && ingredientName.trim().length() == 0) {
                Main.notifyUser("No ingredient name");
                tfIngredientName.setStyle(C.INVALID_FIELD_STYLE);
                return false;
            }
        // if quantity field has an invalid number notify user and return false.
        } catch (NumberFormatException ex) {
            Main.notifyUser("Missing or invalid number");
            tfIngredientQuantity.setStyle(C.INVALID_FIELD_STYLE);
            return false;
        }
        return true;
    }
}
