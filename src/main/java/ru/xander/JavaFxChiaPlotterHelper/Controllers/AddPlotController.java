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
import ru.xander.JavaFxChiaPlotterHelper.Controllers.ListView.PlotSettingsData;
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
                if(!checkTemporaryPathAndShowAlert()){
                    return;
                }
                boolean secondTemporary=secondTmpDirCheckBox.isSelected();
                if(secondTemporary){
                    if(!checkSecondTemporaryPathAndShowAlert()){
                        return;
                    }
                }
                if(!checkFinalPathAndShowAlert()){
                    return;
                }

                int plotSize=getPlotSize();
                int buckets=getNumberOfBuckets();
                int memoryUsage=getMemoryMaxUsage();
                int threads=getNumberOfThreads();

                String temporaryPath=getTemporaryPath();
                boolean useSecondTemporary=getUseSecondTemporary();
                String secondTemporaryPath=getSecondTemporaryPath();
                String finalPath=getFinalPath();

                PlotSettingsData plotSettingsData=new PlotSettingsData(plotSize,buckets,memoryUsage,threads,temporaryPath,useSecondTemporary,secondTemporaryPath,finalPath);

                MainWindowController.currentMainWindowController.addPlotTask(plotSettingsData);
                stage.close();

            }
        });

    }


    public void setParamsModeView(PlotSettingsData plotSettingsData){
        setPlotSettings(plotSettingsData);
        plotSizeChoiceBox.setDisable(true);
        bucketsChoiceBox.setDisable(true);
        memoryTextField.setDisable(true);
        threadsChoiceBox.setDisable(true);
        temporaryDirTextField.setDisable(true);
        tempDirBrowseButton.setDisable(true);
        secondTmpDirCheckBox.setDisable(true);
        secondTmpDirTextField.setDisable(true);
        secondDirBrowseButton.setDisable(true);
        finalDirTextField.setDisable(true);
        finalDirBrowseButton.setDisable(true);
        cancelButton.setDisable(false);
        createButton.setDisable(true);
    }

    private boolean checkTemporaryPathAndShowAlert(){
        String temporaryPath=temporaryDirTextField.getText();
        return checkDirectoryPathAndShowAlert(temporaryPath,"Temporary");
    }

    private boolean checkSecondTemporaryPathAndShowAlert(){
        String secondTemporaryPath=secondTmpDirTextField.getText();
        return checkDirectoryPathAndShowAlert(secondTemporaryPath,"2nd temporary");
    }

    private boolean checkFinalPathAndShowAlert(){
        String finalPath=finalDirTextField.getText();
        return checkDirectoryPathAndShowAlert(finalPath,"Final");
    }

    private boolean checkDirectoryPathAndShowAlert(String path,String dirName){
        if(path==null){
            path="";
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Please check temporary path");

        File file=new File(path);

        if(!file.exists()){
            alert.setContentText( dirName + " path does not exist");
            alert.showAndWait();
            return false;
        }

        if(!file.isDirectory()){
            alert.setContentText(dirName + " path is not directory");
            alert.showAndWait();
            return false;
        }

        if(!file.canWrite()){
            alert.setContentText(dirName+" directory can't be written");
            alert.showAndWait();
            return false;
        }

        return true;
    }


    private int getPlotSize(){


       int index=plotSizeChoiceBox.getSelectionModel().getSelectedIndex();
       if(index<0) throw new IllegalStateException();
       int plotSize= 32 + index;
        return plotSize;
    }

    private int getNumberOfBuckets(){
        int index= bucketsChoiceBox.getSelectionModel().getSelectedIndex();
        if(index<0) throw new IllegalStateException();
        int buckets= (int) Math.pow(2,(4 + index) );
        return buckets;
    }

    private int getMemoryMaxUsage(){
        String memoryString= memoryTextField.getText();
        int memory=Integer.parseUnsignedInt(memoryString);
        return memory;
    }

    private int getNumberOfThreads(){
        String threadsString= threadsChoiceBox.getValue();
        int threads=Integer.parseUnsignedInt(threadsString);
        return threads;
    }

    private String getTemporaryPath(){
        String temporaryPath=temporaryDirTextField.getText();
        if(temporaryPath==null){
            temporaryPath="";
        }
        return temporaryPath;
    }


    private boolean getUseSecondTemporary(){
        return secondTmpDirCheckBox.isSelected();
    }

    private String getSecondTemporaryPath(){
        String secondTemporaryPath= secondTmpDirTextField.getText();
        if(secondTemporaryPath==null){
            secondTemporaryPath="";
        }
        return secondTemporaryPath;
    }

    private String getFinalPath(){
        String finalPath = finalDirTextField.getText();
        if(finalPath==null){
            finalPath="";
        }
        return finalPath;
    }

    private boolean isSecondPathActive(){
        return secondTmpDirCheckBox.isSelected();
    }

    private void setPlotSize(int plotSize){
        if(plotSize<32 || plotSize>35){
            throw new IllegalArgumentException();
        }
        int index=plotSize-32;
        plotSizeChoiceBox.getSelectionModel().select(index);
    }

    private void setNumberOfBuckets(int numberOfBuckets){
        if(numberOfBuckets <PlotSettingsData.minimumBuckets || PlotSettingsData.maximumBuckets <numberOfBuckets){
            throw new IllegalArgumentException();
        }

        double log2= Math.log(numberOfBuckets)/Math.log(2);
        if (log2 % 1 == 0) {
            //целое

        }else {
            //не целое
            throw new IllegalArgumentException();
        }

        int bucketExp= (int) log2;
        // должно получится от 4 до 8
        if(bucketExp<4 || 8<bucketExp){
            throw new IllegalStateException();
        }

        int index=bucketExp-4;

        bucketsChoiceBox.getSelectionModel().select(index);
    }

    private void setRamMaxUsage(int ramMaxUsage){
        if(ramMaxUsage< PlotSettingsData.minimumRamUsage){
            throw new IllegalArgumentException();
        }
        memoryTextField.setText(Integer.toString(ramMaxUsage));
    }

    private void setNumberOfThreads(int threads){
        if(threads<PlotSettingsData.minimumThreads){
            throw new IllegalArgumentException();
        }
        if(threads>PlotSettingsData.maximumThreads){
            throw new IllegalArgumentException();
        }
        int index=threads-1;
        threadsChoiceBox.getSelectionModel().select(index);
    }

    private void setTemporaryDirectory(String directoryPath){
        if(directoryPath==null){
            directoryPath="";
        }
        temporaryDirTextField.setText(directoryPath);
    }

    private void setUseSecondTemporary(boolean secondTemporary){
        secondTmpDirCheckBox.setSelected(secondTemporary);
    }

    private void setSecondTemporaryPath(String secondTemporaryPath){
        if(secondTemporaryPath==null){
            secondTemporaryPath="";
        }
        secondTmpDirTextField.setText(secondTemporaryPath);
    }

    private void setFinalPath(String finalPath){
        if(finalPath==null){
            finalPath="";
        }
        finalDirTextField.setText(finalPath);
    }

    public void setPlotSettings(PlotSettingsData plotSettings){
        if(plotSettings==null){
            throw new IllegalArgumentException();
        }
        setPlotSize(plotSettings.getPlotSize());
        setNumberOfBuckets(plotSettings.getBuckets());
        setRamMaxUsage(plotSettings.getMaxRamUsage());
        setNumberOfThreads(plotSettings.getThreads());
        setTemporaryDirectory(plotSettings.getTemporaryPath());
        setUseSecondTemporary(plotSettings.getUseSecondTemporary());
        setSecondTemporaryPath(plotSettings.getSecondTemporaryPath());
        setFinalPath(plotSettings.getFinalPath());

    }


}
