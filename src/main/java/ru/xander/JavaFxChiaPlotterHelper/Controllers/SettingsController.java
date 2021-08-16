package ru.xander.JavaFxChiaPlotterHelper.Controllers;

import javafx.collections.ObservableList;
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
import ru.xander.JavaFxChiaPlotterHelper.Main;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SettingsController {

    private Label chiaFileLabel;
    private TextField chiaFileTextField;
    private Button chiaFileBrowseButton;
    private Button chiaFileCheckButton;
    private Button chiaFileAutoDetectButton;
    private Button chiaFileClearButton;
    private Label chiaFileVersionLabel;
    private Label keysTitleLabel;
    private Button refreshKeysButton;
    private Label fingerPrintLabel;
    private ChoiceBox<String> fingerprintChoiceBox;
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
        refreshKeysButton=(Button) scene.lookup("#refreshKeysButton");
        fingerPrintLabel=(Label) scene.lookup("#fingerPrintLabel");
        fingerprintChoiceBox=(ChoiceBox<String>) scene.lookup("#fingerprintChoiceBox");
        farmerPublicKeyLabel=(Label) scene.lookup("#farmerPublicKeyLabel");
        farmerPublicKeyTextField=(TextField) scene.lookup("#farmerPublicKeyTextField");
        poolPublicKeyLabel=(Label) scene.lookup("#poolPublicKeyLabel");
        poolPublicKeyTextField=(TextField) scene.lookup("#poolPublicKeyTextField");
        cancelButton=(Button) scene.lookup("#cancelButton");
        doneButton=(Button) scene.lookup("#doneButton");


        if(!isChiaPathActual()){
            firstState();
        }else if(!isSelectedFingerprintActual()){
            secondState();
        }else {
            //значит все актуально
            //надо установить путь и ключи

            //установка пути
            //так как путь уже проверен его нужно только установить и залочить
            lockChiaCliFile(this.mainAppSettings);

            //так как ключи актуальны, точнее fingerprint действующий надо его выбрать
            int fingerPrintIndex=this.mainAppSettings.getFingerprintIndex();
            if(fingerPrintIndex<0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Internal Error");
                alert.setHeaderText("App internal code error");
                alert.setContentText("");
                alert.showAndWait();
                throw new IllegalStateException();
            }


        }

        /*
        if(!tempAppSettings.isAllDataSetted()){

        }else {
            lockAll(tempAppSettings);
        }
        */




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

                //Alert alert = new Alert(Alert.AlertType.WARNING);
                //alert.setTitle("Not developed");
                //alert.setHeaderText("feature not developed yet ");
                //alert.setContentText("Я забил на разработку чтобы другое успеть сделать");
               //alert.showAndWait();
                String suggestedPath=autoDetectChiaCliPath();
                if(suggestedPath!=null){
                    chiaFileTextField.setText(suggestedPath);
                }

            }
        });

        refreshKeysButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                updateKeys();

                /*
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Not developed");
                alert.setHeaderText("feature not developed yet ");
                alert.setContentText("Я забил на разработку чтобы другое успеть сделать. Вводите вручную");
                alert.showAndWait();
                 */
            }
        });

        fingerprintChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index=fingerprintChoiceBox.getSelectionModel().getSelectedIndex();
                if(index<0){
                    farmerPublicKeyTextField.setText("");
                    poolPublicKeyTextField.setText("");
                    return;
                }
                String[] keysArray=tempAppSettings.keysArrayList.get(index);
                String farmerKey=keysArray[AppSettings.FARMER_KEY_INDEX];
                String poolKey=keysArray[AppSettings.POOL_KEY_INDEX];
                farmerPublicKeyTextField.setText(farmerKey);
                poolPublicKeyTextField.setText(poolKey);
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


                String fingerprintValue=fingerprintChoiceBox.getValue();
                int fingerprintIndex= tempAppSettings.getFingerprintIndex(fingerprintValue);
                if(fingerprintChoiceBox.getSelectionModel().getSelectedIndex()!=fingerprintIndex){
                    throw new IllegalStateException();
                }
                if(fingerprintIndex<0){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Check fields");
                    alert.setHeaderText("fingerprint not selected:");
                    alert.setContentText("Please select or refresh and select fingerprint");
                    alert.showAndWait();
                    return;
                }


                tempAppSettings.setFingerprint(fingerprintValue);
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

        this.doneButton.setDisable(true);

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
        this.fingerprintChoiceBox.getItems().clear();
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



        this.refreshKeysButton.setDisable(false);
        this.farmerPublicKeyTextField.setDisable(true);
        this.poolPublicKeyTextField.setDisable(true);
        this.doneButton.setDisable(true);




        this.farmerPublicKeyTextField.setText(appSettings.getFarmerPublicKey());
        this.poolPublicKeyTextField.setText(appSettings.getPoolPublicKey());
    }

    private void updateKeys(){
        tempAppSettings.updateFingerprintAndKeys();
        ObservableList<String> fingerprintsObservableList=fingerprintChoiceBox.getItems();
        fingerprintsObservableList.clear();
        int keysSize=tempAppSettings.keysArrayList.size();
        for(int count=0;keysSize>count;count+=1){
            String[] keysArray=tempAppSettings.keysArrayList.get(count);
            String fingerprint=keysArray[0];
            fingerprintsObservableList.add(fingerprint);
        }

        fingerprintChoiceBox.setDisable(false);

    }

    private String autoDetectChiaCliPath(){
        if(Main.isUnix()){
            return null;
        }
        String slash="\\";
        slash=System.getProperty("file.separator");

        String userHome=System.getProperty("user.home");

        if(Main.isWindows()){
            String relativeChiaBlockchain=slash+"AppData"+slash+"Local"+slash+"chia-blockchain";
            String pathToBlockChain=userHome+relativeChiaBlockchain;
            File chiaBlockPath=new File(pathToBlockChain);
            if(!chiaBlockPath.isDirectory()){
                return null;
            }
            FilenameFilter filenameFilter=new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if(name.contains("app-")){
                        return true;
                    }
                    return false;
                }
            };
            String[] dirs=chiaBlockPath.list(filenameFilter);
            String selectedDirectory=null;
            if(dirs.length==0){
                return null;
            }
            if(dirs.length==1){
                selectedDirectory=dirs[0];
            }else{
                // больше одного
                // я обнаружил что при обновлении старые версии не остаются. только одна папка остается
                // смысла нет искать папку с более новай версией
                return null;

            }
            if(selectedDirectory==null) throw new IllegalStateException();

            String fullPath=pathToBlockChain + slash + selectedDirectory + slash + "resources" + slash + "app.asar.unpacked" +
                    slash + "daemon" + slash + "chia.exe";

            return fullPath;
        }
        if(Main.isMac()){
            String relativePackagePath="Chia.app"+slash+"Contents"+slash+"Resources"+slash+"app.asar.unpacked"+slash+"daemon"+slash+"chia";
            String defaultMacAppPath=slash+"Applications"+slash+relativePackagePath;
            File defaultMacAppFile=new File(defaultMacAppPath);
            if(defaultMacAppFile.exists()){
                return defaultMacAppPath;
            }
            // верхний путь не существует, значит пробуем вариант, когда приложение установленно в пользовательскую папку программы
            String defaultUserAppPath=userHome+slash+"Applications"+slash+relativePackagePath;
            File defaultUserAppFile=new File(defaultUserAppPath);
            if(defaultUserAppFile.exists()){
                return defaultUserAppPath;
            }
            return null;
        }

        throw new IllegalStateException();

    }

    /**
     * когда ничего не выбрано.(ни путь до chia cli, ни fingerprint с ключами)
     * при таком состоянии, доступен для редактирования только выбор пути до chia cli
     */
    private void firstState(){

        //делаем кнопки и поля выбора пути chia cli активными
        this.chiaFileTextField.setDisable(false);
        this.chiaFileBrowseButton.setDisable(false);
        this.chiaFileCheckButton.setDisable(false);
        this.chiaFileAutoDetectButton.setDisable(false);
        this.chiaFileVersionLabel.setText("");
        //деактивируем и очещаем поля и кнопки для выбора ключей(fingerprint)

        keysTitleLabel.setDisable(true);
        refreshKeysButton.setDisable(true);
        fingerPrintLabel.setDisable(true);
        fingerprintChoiceBox.getItems().clear();
        fingerprintChoiceBox.setDisable(true);
        farmerPublicKeyLabel.setDisable(true);
        farmerPublicKeyTextField.setText("");
        farmerPublicKeyTextField.setDisable(true);
        poolPublicKeyLabel.setDisable(true);
        poolPublicKeyTextField.setText("");
        poolPublicKeyTextField.setDisable(true);
        doneButton.setDisable(true);
    }

    /**
     * при таком состоянии путь до chia cli выбран и заблокирован, редактирование пути не доступно уже, остается только выбор
     * ключей (fingerprint)
     */
    private void secondState(){
        //деактивируем кнопки и поля выбора пути chia cli
        this.chiaFileTextField.setDisable(true);
        this.chiaFileBrowseButton.setDisable(true);
        this.chiaFileCheckButton.setDisable(true);
        this.chiaFileAutoDetectButton.setDisable(true);
        //активируем поля и кнопки для выбора ключей(fingerprint)
        keysTitleLabel.setDisable(false);
        refreshKeysButton.setDisable(false);
        fingerPrintLabel.setDisable(false);
        fingerprintChoiceBox.getItems().clear();
        fingerprintChoiceBox.setDisable(false);
        farmerPublicKeyLabel.setDisable(false);
        farmerPublicKeyTextField.setText("");
        farmerPublicKeyTextField.setDisable(false);
        poolPublicKeyLabel.setDisable(false);
        poolPublicKeyTextField.setText("");
        poolPublicKeyTextField.setDisable(false);
        //doneButton.setDisable(false);
    }

    private boolean isChiaPathActual(){
        return mainAppSettings.isChiaCliFilePathActual();
    }

    private boolean isSelectedFingerprintActual(){
        return mainAppSettings.isFingerprintActual();
    }

    private boolean setFingerprintAndKeys(AppSettings appSettings){
        if(appSettings==null){
            throw new IllegalArgumentException();
        }
        String[] fingerprintKeyArray=appSettings.getFingerprintPublicKeysArray(appSettings.getFingerprint());
        if(fingerprintKeyArray==null){
            return false;
        }
        /*
         * FINGERPRINT_INDEX=0;
         * MASTER_KEY_INDEX=1;
         * FARMER_KEY_INDEX=2;
         * POOL_KEY_INDEX=3;
         * FIRST_WALLET_INDEX=4;
         */
        String fingerprint=fingerprintKeyArray[AppSettings.FINGERPRINT_INDEX];
        //String masterKey=fingerprintKeyArray[AppSettings.MASTER_KEY_INDEX];
        String farmerKey=fingerprintKeyArray[AppSettings.FARMER_KEY_INDEX];
        String poolKey=fingerprintKeyArray[AppSettings.POOL_KEY_INDEX];
        //String wallet=fingerprintKeyArray[AppSettings.FIRST_WALLET_INDEX];

        String[] fingerprintArray=appSettings.getFingerprintOnlyArray();
        int fingerprintIndex= appSettings.getFingerprintIndex(fingerprint);
        if(fingerprintIndex<0){
            throw new IllegalStateException();
        }
        if(!fingerprint.equals(fingerprintArray[fingerprintIndex])){
            throw new IllegalStateException();
        }

        ObservableList<String> fingerprintObservableList=fingerprintChoiceBox.getItems();
        fingerprintObservableList.clear();

        for(int count = 0 ; )

    }


}
