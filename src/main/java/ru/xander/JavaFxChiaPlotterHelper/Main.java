package ru.xander.JavaFxChiaPlotterHelper;

import com.sun.deploy.config.OSType;
import ru.xander.JavaFxChiaPlotterHelper.Controllers.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static boolean isWindows=false;
    private static boolean isMac=false;
    private static boolean isUnix=false;

    private MainWindowController mainWindowController;

    @Override
    public void start(Stage primaryStage) throws Exception{

        int osType=OSType.getOSType();
        switch (osType){
            case OSType.WINDOWS:
                isWindows=true;
                break;
            case OSType.MACOSX:
                isMac=true;
                break;
            case OSType.UNIX:
                isUnix=true;
                break;
            default:
                throw new IllegalStateException();
        }
        mainWindowController=new MainWindowController();
        mainWindowController.initWindow(primaryStage);

    }


    public static boolean isWindows(){
        return Main.isWindows;
    }

    public static boolean isMac(){
        return Main.isMac;
    }

    public static boolean isUnix(){
        return Main.isUnix;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
