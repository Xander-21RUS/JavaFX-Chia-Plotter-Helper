package ru.xander.JavaFxChiaPlotterHelper.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.xander.JavaFxChiaPlotterHelper.Helpers.AppSettings;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class AddPlotController {

    private AppSettings mainAppSettings;


    private ChoiceBox<String> plotSizeChoiceBox;
    private Label plotSizeLabel;
    private ChoiceBox<String> bucketsChoiceBox;
    private Label bucketsLabel;
    private TextField memoryTextField;
    private Label memoryLabel;
    private ChoiceBox<String> threadsChoiceBox;
    private Label threadsLabel;
    private Label temporaryDirLabel;
    private TextField temporaryDirTextField;
    private Button tempDirBrowseButton;
    private CheckBox secondTmpDirCheckBox;
    private TextField secondTmpDirTextField;
    private Button secondDirBrowseButton;
    private Label finalDirLabel;
    private TextField finalDirTextField;
    private Button finalDirBrowseButton;
    private Button cancelButton;
    private Button createButton;


    public void initWindow(Stage primaryStage, AppSettings appSettings) throws IOException {

        if (appSettings == null) {
            throw new IllegalArgumentException();
        }
        this.mainAppSettings = appSettings;

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL sceneResource = getClass().getClassLoader().getResource("ru/xander/JavaFxChiaPlotterHelper/FxScenes/Plot_Creation.fxml");
        fxmlLoader.setLocation(sceneResource);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 724, 511);


        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage.getScene().getWindow());
        stage.setScene(scene);
        stage.setTitle("Create a plot");
        stage.setResizable(false);
        stage.show();


        plotSizeChoiceBox = (ChoiceBox<String>) scene.lookup("#plotSizeChoiceBox");
        plotSizeLabel = (Label) scene.lookup("#plotSizeLabel");
        bucketsChoiceBox = (ChoiceBox<String>) scene.lookup("#bucketsChoiceBox");
        bucketsLabel = (Label) scene.lookup("#bucketsLabel");
        memoryTextField = (TextField) scene.lookup("#memoryTextField");
        memoryLabel = (Label) scene.lookup("#memoryLabel");
        threadsChoiceBox = (ChoiceBox<String>) scene.lookup("#threadsChoiceBox");
        threadsLabel = (Label) scene.lookup("#threadsLabel");
        temporaryDirLabel = (Label) scene.lookup("#temporaryDirLabel");
        temporaryDirTextField = (TextField) scene.lookup("#temporaryDirTextField");
        tempDirBrowseButton = (Button) scene.lookup("#tempDirBrowseButton");
        secondTmpDirCheckBox = (CheckBox) scene.lookup("#secondTmpDirCheckBox");
        secondTmpDirTextField = (TextField) scene.lookup("#secondTmpDirTextField");
        secondDirBrowseButton = (Button) scene.lookup("#secondDirBrowseButton");
        finalDirLabel = (Label) scene.lookup("#finalDirLabel");
        finalDirTextField = (TextField) scene.lookup("#finalDirTextField");
        finalDirBrowseButton = (Button) scene.lookup("#finalDirBrowseButton");
        cancelButton = (Button) scene.lookup("#cancelButton");
        createButton = (Button) scene.lookup("#createButton");


        ObservableList<String> plotSizeObservableList= FXCollections.observableList(new ArrayList<String>());
        plotSizeObservableList.add("K=32 (101.4 GiB)");
        plotSizeObservableList.add("K=33 (208.8 GiB)");
        plotSizeObservableList.add("K=34 (429.8 GiB)");
        plotSizeObservableList.add("K=35 (884.1 GiB)");
        plotSizeChoiceBox.setItems(plotSizeObservableList);
        plotSizeChoiceBox.getSelectionModel().selectFirst();

        ObservableList<String> numberOfBucketsObservableList= FXCollections.observableList(new ArrayList<String>());
        numberOfBucketsObservableList.add("16 (2^4)");
        numberOfBucketsObservableList.add("32 (2^5)");
        numberOfBucketsObservableList.add("64 (2^6)");
        numberOfBucketsObservableList.add("128 (2^7)");
        numberOfBucketsObservableList.add("256 (2^8)");
        bucketsChoiceBox.setItems(numberOfBucketsObservableList);
        bucketsChoiceBox.getSelectionModel().select(3);

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        memoryTextField.setTextFormatter(textFormatter);
        memoryTextField.setText("3200");

        ObservableList<String> threadsObservableList=FXCollections.observableList(new ArrayList<String>());

        for (int count=1; 128>=count;count+=1){
            threadsObservableList.add(Integer.toString(count));
        }
        threadsChoiceBox.setItems(threadsObservableList);
        threadsChoiceBox.getSelectionModel().select(1);

        tempDirBrowseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser=new DirectoryChooser();
                directoryChooser.setTitle("Chia plot temporary directory");
                File file=directoryChooser.showDialog(stage);
                if(file!=null){
                    temporaryDirTextField.setText(file.getAbsolutePath());
                }
            }
        });

        secondTmpDirCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(secondTmpDirCheckBox.isSelected()){
                    secondTmpDirTextField.setDisable(false);
                    secondDirBrowseButton.setDisable(false);
                }else {
                    secondTmpDirTextField.setText("");
                    secondTmpDirTextField.setDisable(true);
                    secondDirBrowseButton.setDisable(true);
                }
            }
        });

        secondDirBrowseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser=new DirectoryChooser();
                directoryChooser.setTitle("Chia plot second temporary directory");
                File file=directoryChooser.showDialog(stage);
                if(file!=null){
                    secondTmpDirTextField.setText(file.getAbsolutePath());
                }
            }
        });

        finalDirBrowseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser=new DirectoryChooser();
                directoryChooser.setTitle("Chia plot final directory");
                File file=directoryChooser.showDialog(stage);
                if(file!=null){
                    finalDirTextField.setText(file.getAbsolutePath());
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkTemporaryPath();
            }
        });



    }


    private void checkTemporaryPath(){
        String temporaryPath=temporaryDirTextField.getText();
        if(temporaryPath==null){
            temporaryPath="";
        }
        Path path= Paths.get(temporaryPath);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Please check temporary path");

        File file=new File(temporaryPath);
        if(!Files.exists(path)){
            alert.setContentText("Path does not exist");
            alert.showAndWait();
            return;
        }

        if(!Files.isDirectory(path)){
            alert.setContentText("Temporary path is not directory");
            alert.showAndWait();
            return;
        }

        if(!Files.isWritable(path)){
            alert.setContentText("Directory can't be written");
            alert.showAndWait();
            return;
        }

    }

}
