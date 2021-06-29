package ru.xander.JavaFxChiaPlotterHelper;

import ru.xander.JavaFxChiaPlotterHelper.Controllers.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private MainWindowController mainWindowController;

    @Override
    public void start(Stage primaryStage) throws Exception{

        mainWindowController=new MainWindowController();
        mainWindowController.initWindow(primaryStage);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
