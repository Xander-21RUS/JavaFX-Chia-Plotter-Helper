package ru.xander.JavaFxChiaPlotterHelper.Controllers;
//ru.xander.JavaFxChiaPlotterHelper.Controllers.MainWindowController
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.xander.JavaFxChiaPlotterHelper.Controllers.ListView.PlotSettingsData;
import ru.xander.JavaFxChiaPlotterHelper.Controllers.ListView.PlotTaskCell;
import ru.xander.JavaFxChiaPlotterHelper.Helpers.AppSettings;

import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;

public class MainWindowController {

    public static MainWindowController currentMainWindowController=null;


    private AppSettings appSettings;

    @FXML
    private Button settingsButton;
    @FXML
    private Button addPlotButton;
    @FXML
    private ListView<PlotTaskCell> plotsListView;

    private SettingsController settingsController;
    private AddPlotController addPlotController;
    private Stage mainWindowStage=null;

    public void initWindow(Stage primaryStage) throws IOException {
        MainWindowController.currentMainWindowController=this;
        this.appSettings=new AppSettings();
        this.appSettings.load();

        if(primaryStage==null){
            primaryStage=new Stage();
        }
        mainWindowStage=primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL sceneResource=getClass().getClassLoader().getResource("ru/xander/JavaFxChiaPlotterHelper/FxScenes/Main_Window.fxml");
        fxmlLoader.setLocation(sceneResource);
        Parent root = fxmlLoader.load();


        //Parent root = FXMLLoader.load(getClass().getResource("Main_Window.fxml"));
        primaryStage.setTitle("Chia Plotter Helper");
        Scene scene=new Scene(root, 724, 511);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();


        settingsButton=(Button) scene.lookup("#settingsButton");
        addPlotButton=(Button) scene.lookup("#addPlotButton");
        plotsListView=(ListView<PlotTaskCell>) scene.lookup("#plotsListView");


        Stage finalPrimaryStage = primaryStage;
        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(settingsController==null){
                    settingsController=new SettingsController();
                }
                try {
                    settingsController.initWindow(finalPrimaryStage,appSettings);
                }catch (Exception ex){

                }
            }
        });

        Stage finalPrimaryStage1 = primaryStage;
        addPlotButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(addPlotController==null){
                    addPlotController=new AddPlotController();
                }

                try {
                    addPlotController.initWindow(finalPrimaryStage1,appSettings);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ObservableList<PlotTaskCell> list = FXCollections.observableArrayList();

        plotsListView.setItems(list);
        plotsListView.setCellFactory(new Callback<ListView<PlotTaskCell>, ListCell<PlotTaskCell>>() {
            @Override
            public ListCell<PlotTaskCell> call(ListView<PlotTaskCell> param) {





               ListCell<PlotTaskCell> listCell= new ListCell<PlotTaskCell>(){
                    @Override
                    public void updateItem(PlotTaskCell item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);  // No text in label of super class
                        if (empty) {
                            setGraphic(null);
                        } else {


                            setGraphic(item.hbox);
                        }
                    }
                };

                return listCell;


            }
        });

        checkSettingsActualityAndLockAndUnlockPlotButton();
    }


    public void checkSettingsActualityAndLockAndUnlockPlotButton(){
        boolean isDataActual=appSettings.checkActualityOfDAta();
        if(!isDataActual){
            addPlotButton.setDisable(true);
            return;
        }
        addPlotButton.setDisable(false);
    }
    public void addPlotTask(PlotSettingsData plotSettingsData){
        if(plotSettingsData==null){
            throw new IllegalArgumentException();
        }

        PlotTaskCell plotTaskCell=new PlotTaskCell(plotSettingsData);
        plotsListView.getItems().add(plotTaskCell);
    }

    public void showPlotParams(PlotSettingsData plotSettingsData){
        if(addPlotController==null){
            addPlotController=new AddPlotController();
        }

        try {
            addPlotController.initWindow(mainWindowStage,appSettings);
            addPlotController.setParamsModeView(plotSettingsData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removePlotTaskCell(PlotTaskCell plotTaskCell){
        boolean success=plotsListView.getItems().remove(plotTaskCell);
        if(!success){
            throw new NoSuchElementException();
        }
    }
}
