package frey.jimmy.recipeinput;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import frey.jimmy.recipe.recipselector.Recipe;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.Executor;

/**
 * Displays recipes for selection.
 */
public class RecipeSelectionPane extends BorderPane {
    private static final String IMAGE_FILE = "C:\\Users\\James\\IdeaProjects\\RecipeInput\\save\\image\\";
    private Button btBackToAddRecipe = new Button("Back to Category Selection");
    private Button btChooseThisRecipe = new Button("Choose This Recipe!");
    private ListView<String> listViewOfRecipes;
    private HBox bottomHBox = new HBox(C.SMALL_MARGIN_PROPORTIONAL);
    private TextArea mServerTextArea = new TextArea();
    private Button mShutDownHTTPServer = new Button("Stop HTTP Server");
    private Button mStartHttpServer = new Button("Start HTTP Server");
    private HttpServer httpServer;

    public RecipeSelectionPane(Stage primaryStage, ArrayList<Recipe> arrayListRecipe) {
        // Get a list of names from the passed ArrayList of Recipes
        ArrayList<String> arrayListRecipeNames = new ArrayList<>();
        for (Recipe r : arrayListRecipe) {
            arrayListRecipeNames.add(r.getRecipeName());
        }
        if (arrayListRecipeNames.isEmpty()) arrayListRecipeNames.add("No recipes found");
        //Create textarea to display server info.
        this.setTop(mServerTextArea);


        // Create ListView that consists of the list of names.  Set it to display in center of BorderPane.
        listViewOfRecipes = new ListView<String>();
        listViewOfRecipes.getItems().setAll(arrayListRecipeNames);
        this.setCenter(listViewOfRecipes);

        // Configure and display buttons in bottom of BorderPane
        bottomHBox.getChildren().addAll(btBackToAddRecipe, btChooseThisRecipe,mStartHttpServer, mShutDownHTTPServer);
        this.setBottom(bottomHBox);

        mStartHttpServer.setOnAction(e->{
            if(httpServer != null) {
                httpServer.stop(1);
            }
            createHttpServer();
        });

        mShutDownHTTPServer.setOnAction(e->{
            httpServer.stop(1);
            mServerTextArea.appendText("Stopping httpServer \n");
        });

        btBackToAddRecipe.setOnAction(e -> {
            primaryStage.setScene(Main.createScene(new RecipeAddPane(primaryStage)));
        });

        btChooseThisRecipe.setOnAction(e -> {
            String recipeNameSelected = listViewOfRecipes.getSelectionModel().getSelectedItem();
            for (Recipe r : arrayListRecipe) {
                if (r.getRecipeName().equals(recipeNameSelected)) {
                    primaryStage.setScene(Main.createScene(new RecipeAddPane(primaryStage,r)));
                    break;
                }
            }

        });

    }

    private void createHttpServer() {
        try {
            File tempFile = new File(IMAGE_FILE);
            File imageFile = new File(tempFile.getAbsolutePath());
            httpServer = HttpServer.create(new InetSocketAddress(80),0);
            httpServer.setExecutor(null);
            httpServer.createContext("/save/image", new MyHandler());
            httpServer.start();
            mServerTextArea.appendText("Started server \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MyHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Headers headers = httpExchange.getResponseHeaders();
            headers.add("Content-Type","image/png");

            //Get requested number from httprequest
            String requestURI = httpExchange.getRequestURI().toString();
            String[] requestParts = requestURI.split("/");
            int imageNumberRequested = Integer.valueOf(requestParts[requestParts.length-1]);
            System.out.println(imageNumberRequested);

            //Read image file to byte array
            File imageFile = new File(IMAGE_FILE + imageNumberRequested + ".jpg");
            byte[] imageBytes = new byte[(int)imageFile.length()];
            FileInputStream inputStream = new FileInputStream(imageFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedInputStream.read(imageBytes,0,imageBytes.length);

            //Send image file
            httpExchange.sendResponseHeaders(200, imageBytes.length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(imageBytes);
            outputStream.flush();
            outputStream.close();
        }
    }
}
