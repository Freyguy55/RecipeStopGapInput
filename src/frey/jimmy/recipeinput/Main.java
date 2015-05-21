package frey.jimmy.recipeinput;

import javafx.application.Application;



import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {
    private static int notificationCount = 0;

    @Override
    public void start(Stage primaryStage) {
        //TODO MAKE NEW RECIPEBOOK
  //      RecipeBook.initialize();  // check for save file.  load information.

        Scene scene = Main.createScene(new RecipeAddPane(primaryStage));  //Main.createScene returns a scene with default x,y values
        primaryStage.setScene(scene);
        primaryStage.setTitle("Recipe Timer");
        primaryStage.show();
    }

    private void initialize() {

    }

    /* Displays a notification message to the user.  Similar to a toast notification */
    public static void notifyUser(String msg) {
        final Stage notificationStage = new Stage(StageStyle.UNDECORATED);
        Label lbMsg = new Label(msg);
        lbMsg.setStyle(C.NOTIFICATION_STYLE);
        notificationStage.setScene(new Scene(new StackPane(lbMsg)));
        FadeTransition closeNotification = new FadeTransition(Duration.seconds(3), lbMsg);
        closeNotification.setToValue(0);
        notificationStage.show();
        closeNotification.play();
        closeNotification.setOnFinished(e -> {
            notificationStage.hide();
        });
    }

    public static Scene createScene(Pane pane) {
        Scene scene = new Scene(pane, C.X_RESOLUTION, C.Y_RESOLUTION);
        return scene;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
