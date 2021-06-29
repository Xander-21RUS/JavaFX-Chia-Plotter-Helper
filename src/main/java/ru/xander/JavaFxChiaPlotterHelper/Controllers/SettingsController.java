package ru.xander.JavaFxChiaPlotterHelper.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.xander.JavaFxChiaPlotterHelper.Helpers.AppSettings;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SettingsController {

    private Label chiaFileLabel;
    private TextField chiaFileTextField;
    private Button chiaFileBrowseButton;
    private Button chiaFileCheckButton;
    private Button chiaFileAutoDetectButton;
    private Button chiaFileClearButton;
    private Label chiaFileVersionLabel;
    private Label keysTitleLabel;
    private Button selectKeysButton;
    private Label fingerPrintLabel;
    private TextField fingerPrintTextField;
    private Label farmerPublicKeyLabel;
    private TextField farmerPublicKeyTextField;
    private Label poolPublicKeyLabel;
    private TextField poolPublicKeyTextField;
    private Button cancelButton;
    private Button doneButton;


    private AppSettings mainAppSettings;
    private AppSettings tempAppSettings;



    public void initWindow(Stage primaryStage, AppSettings appSettings) throws IOException {

        if(appSettings==null){
            throw new IllegalArgumentException();
        }
        this.mainAppSettings=appSettings;
        this.tempAppSettings=new AppSettings(appSettings);

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL sceneResource=getClass().getClassLoader().getResource("ru/xander/JavaFxChiaPlotterHelper/FxScenes/Plotting_settings.fxml");
        fxmlLoader.setLocation(sceneResource);
        Parent root = fxmlLoader.load();

        Scene scene=new Scene(root, 724, 511);

        //primaryStage.setTitle("Plotter Settings");
        //primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        //primaryStage.show();


        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage.getScene().getWindow());
        stage.setScene(scene);
        stage.setTitle("Plotter Settings");
        stage.setResizable(false);
        stage.show();

        chiaFileLabel=(Label) scene.lookup("#chiaFileLabel");
        chiaFileTextField=(TextField) scene.lookup("#chiaFileTextField");
        chiaFileBrowseButton=(Button) scene.lookup("#chiaFileBrowseButton");
        chiaFileCheckButton=(Button) scene.lookup("#chiaFileCheckButton");
        chiaFileAutoDetectButton=(Button) scene.lookup("#chiaFileAutoDetectButton");
        chiaFileClearButton=(Button) scene.lookup("#chiaFileClearButton");
        chiaFileVersionLabel=(Label) scene.lookup("#chiaFileVersionLabel");
        keysTitleLabel=(Label) scene.lookup("#keysTitleLabel");
        selectKeysButton=(Button) scene.lookup("#selectKeysButton");
        fingerPrintLabel=(Label) scene.lookup("#fingerPrintLabel");
        fingerPrintTextField=(TextField) scene.lookup("#fingerPrintTextField");
        farmerPublicKeyLabel=(Label) scene.lookup("#farmerPublicKeyLabel");
        farmerPublicKeyTextField=(TextField) scene.lookup("#farmerPublicKeyTextField");
        poolPublicKeyLabel=(Label) scene.lookup("#poolPublicKeyLabel");
        poolPublicKeyTextField=(TextField) scene.lookup("#poolPublicKeyTextField");
        cancelButton=(Button) scene.lookup("#cancelButton");
        doneButton=(Button) scene.lookup("#doneButton");


        if(!tempAppSettings.isAllDataSetted()){
            chiaFileTextField.setText("/Applications/Chia.app/Contents/Resources/app.asar.unpacked/daemon/chia");
            farmerPublicKeyTextField.setText("993ed1f01096e9becbe4cacff6f368a556d6b841c55013e99b2f27b5cec16b62a8bbfa5d627e00a6c176c71185eb6a13");
            poolPublicKeyTextField.setText("a505654ede0387f8c358c0070152766485f62a62840c9bba324c47e08b04c6849e0678ae844b051585edeb3c98a5b3dc");
        }else {
            lockAll(tempAppSettings);
        }


        fingerPrintTextField.setDisable(true);


        chiaFileBrowseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser=new FileChooser();
                fileChooser.setTitle("Open Chia CLI");
                File file=fileChooser.showOpenDialog(stage);
                if(file!=null){
                    chiaFileTextField.setText(file.getAbsolutePath());
                }
            }
        });

        chiaFileCheckButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String chiaFileTextFieldValue=chiaFileTextField.getText();
                if(chiaFileTextFieldValue==null){
                    chiaFileTextFieldValue="";
                }
                String checkChiaFileResult=tempAppSettings.checkFileAndStorePathAndVersion(chiaFileTextFieldValue);
                if(checkChiaFileResult!=null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Chia CLI File check");
                    alert.setHeaderText("Failed");
                    alert.setContentText(checkChiaFileResult);
                    alert.showAndWait();
                    return;
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Chia CLI File check");
                alert.setHeaderText("Success");
                alert.setContentText("Chia v" + tempAppSettings.getDetectedChiaVersion());
                alert.showAndWait();

                lockChiaCliFile(tempAppSettings);


            }
        });

        chiaFileAutoDetectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //String osName=System.getProperty("os.name");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Not developed");
                alert.setHeaderText("feature not developed yet ");
                alert.setContentText("Я забил на разработку чтобы другое успеть сделать");
                alert.showAndWait();

            }
        });

        selectKeysButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Not developed");
                alert.setHeaderText("feature not developed yet ");
                alert.setContentText("Я забил на разработку чтобы другое успеть сделать. Вводите вручную");
                alert.showAndWait();
            }
        });

        chiaFileClearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                unlockAndClearChiaCliFile(tempAppSettings);
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        doneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String farmerPublicKeyValue=farmerPublicKeyTextField.getText();
                if(farmerPublicKeyValue==null) farmerPublicKeyValue="";
                String poolPublicKeyValue=poolPublicKeyTextField.getText();
                if(poolPublicKeyValue==null) poolPublicKeyValue="";



                if(tempAppSettings.getDetectedChiaVersion()==null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Check fields");
                    alert.setHeaderText("please correct these fields:");
                    alert.setContentText("Please point to correct chia CLI file path");
                    alert.showAndWait();
                    return;
                }


                String farmingKeyError=tempAppSettings.checkFarmerPublicKey(farmerPublicKeyValue);
                String poolKeyError=tempAppSettings.checkPoolPublicKey(poolPublicKeyValue);
                String error=null;
                if(farmingKeyError!=null){
                    error=farmingKeyError;
                }else if(poolKeyError!=null){
                    error=poolKeyError;
                }

                if(error!=null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Check fields");
                    alert.setHeaderText("please correct these fields:");
                    alert.setContentText(error);
                    alert.showAndWait();
                    return;
                }

                tempAppSettings.setFarmerPublicKey(farmerPublicKeyValue);
                tempAppSettings.setPoolPublicKey(poolPublicKeyValue);

                mainAppSettings.applySettings(tempAppSettings);
                mainAppSettings.save();
                stage.close();



            }
        });


    }

    public void lockChiaCliFile(AppSettings appSettings){
        if(appSettings==null){
            throw new IllegalArgumentException();
        }
        if(appSettings.detectedChiaVersion==null){
            throw new IllegalArgumentException();
        }

        this.chiaFileTextField.setText(appSettings.getChiaCliFilePath());
        this.chiaFileTextField.setDisable(true);

        this.chiaFileBrowseButton.setDisable(true);

        this.chiaFileCheckButton.setDisable(true);

        this.chiaFileAutoDetectButton.setDisable(true);

        this.doneButton.setDisable(false);

        this.chiaFileVersionLabel.setText("Chia v" + appSettings.detectedChiaVersion);
    }

    public void unlockAndClearChiaCliFile(AppSettings appSettings){
        if(appSettings==null){
            throw new IllegalArgumentException();
        }

        this.chiaFileTextField.setText("");
        this.chiaFileTextField.setDisable(false);

        this.chiaFileBrowseButton.setDisable(false);

        this.chiaFileCheckButton.setDisable(false);

        this.chiaFileAutoDetectButton.setDisable(false);

        this.chiaFileVersionLabel.setText("");

        this.doneButton.setDisable(true);



        appSettings.clearChiaFilePathAndVersion();
        unlockAndClearKeys(appSettings);
    }

    public void unlockAndClearKeys(AppSettings appSettings){
        this.fingerPrintTextField.setText("");
        this.farmerPublicKeyTextField.setText("");
        this.poolPublicKeyTextField.setText("");
        appSettings.clearKeys();
    }

    public void lockAll(AppSettings appSettings){

        if(appSettings==null){
            throw new IllegalArgumentException();
        }

        if(appSettings.getFarmerPublicKey()==null || appSettings.getPoolPublicKey()==null){
            throw new IllegalArgumentException();
        }

        lockChiaCliFile(appSettings);



        this.selectKeysButton.setDisable(true);
        this.farmerPublicKeyTextField.setDisable(true);
        this.poolPublicKeyTextField.setDisable(true);
        this.doneButton.setDisable(true);




        this.farmerPublicKeyTextField.setText(appSettings.getFarmerPublicKey());
        this.poolPublicKeyTextField.setText(appSettings.getPoolPublicKey());
    }


}
