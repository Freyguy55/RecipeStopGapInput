package frey.jimmy.recipeinput;

/**
 * Constants for use in RecipeTimer
 */
public class C {
    public static final double X_RESOLUTION = 1280;
    public static final double Y_RESOLUTION = 720;
    public static final double SMALL_MARGIN_PROPORTIONAL = X_RESOLUTION*.05;
    public static final double SMALL_MARING_FIXED = 5;
    public static final double LARGE_MARGIN = X_RESOLUTION*.1;

    // Save file locations
    public static final String SAVE_FILE = "save\\recipes.dat";

    // Recipe Categories
    public static final String CATEGORY_SWEET = "sweet";
    public static final String CATEGORY_SAVORY = "savory";
    public static final String CATEGORY_LIGHT = "light";
    public static final String CATEGORY_HEAVY = "heavy";

    // Ingredient List Sizes
    public static final double INGREDIENT_LIST_SIZE = 140;

    /*User notification style*/
    public static final String NOTIFICATION_STYLE = "-fx-background-color: YELLOW; -fx-border-color: BLACK; -fx-padding: 5";

    /*Intro styles*/
    public static final String LIGHT_BACKGROUND_COLOR = "-fx-background-color: #336699;";
    public static final String HEAVY_BACKGROUND_COLOR = "-fx-background-color: #900000;";
    public static final double INTRO_BUTTON_X = 70;
    public static final double INTRO_BUTTON_Y = 45;
    public static final String INTRO_BUTTON_STYLE = "-fx-font: 30 arial; -fx-base: LIGHTBLUE;";

    public static final String INVALID_FIELD_STYLE = " -fx-background-color: RED;";
    public static final String DEFAULT_FIELD_STYLE = "  -fx-background-color: WHITE;";
}
