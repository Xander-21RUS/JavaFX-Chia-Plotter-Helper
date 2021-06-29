package ru.xander.JavaFxChiaPlotterHelper.Helpers;

import javafx.scene.control.Alert;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class AppSettings {

    private final String CHIA_CLI_FILE_PATH = "ChiaCliFilePath";
    private final String CHIA_CLI_VERSION = "ChiaCliVersion";
    private final String CHIA_FINGERPRINT = "ChiaFingerprint";
    private final String CHIA_FARMER_PUBLIC_KEY = "ChiaFarmerKey";
    private final String CHIA_POOL_PUBLIC_KEY = "ChiaPoolKey";

    public String chiaCliFilePath=null;
    public String detectedChiaVersion=null;
    public String fingerprint=null;
    public String farmerPublicKey=null;
    public String poolPublicKey=null;


    public AppSettings(){}
    public AppSettings(AppSettings toCopy){
        if(toCopy==null){
            throw new IllegalArgumentException();
        }
        if(toCopy.chiaCliFilePath!=null){
            this.chiaCliFilePath=new String(toCopy.chiaCliFilePath);
        }
        if(toCopy.detectedChiaVersion!=null){
            this.detectedChiaVersion=new String(toCopy.detectedChiaVersion);
        }
        if(toCopy.fingerprint!=null){
            this.fingerprint=new String(toCopy.fingerprint);
        }
        if(toCopy.farmerPublicKey!=null){
            this.farmerPublicKey=new String(toCopy.farmerPublicKey);
        }
        if(toCopy.poolPublicKey!=null){
            this.poolPublicKey=new String(toCopy.poolPublicKey);
        }
    }


    public String getChiaCliFilePath(){
        if(this.chiaCliFilePath==null) return null;
        return new String(this.chiaCliFilePath);
    }
    public String getDetectedChiaVersion(){
        if(this.detectedChiaVersion==null) return null;
        return new String(this.detectedChiaVersion);
    }
    public String getFingerprint(){
        if(this.fingerprint==null) return null;
        return new String(this.fingerprint);
    }
    public String getFarmerPublicKey(){
        if(this.farmerPublicKey==null) return null;
        return new String(this.farmerPublicKey);
    }
    public String getPoolPublicKey(){
        if(this.poolPublicKey==null) return null;
        return new String(this.poolPublicKey);
    }


    public String applySettings(AppSettings newDataAppSettings){
        if(newDataAppSettings==null){
            throw new IllegalArgumentException();
        }
        String checkChiaFilePath=checkChiaCliFile(newDataAppSettings.chiaCliFilePath);
        if(checkChiaFilePath!=null){
            return checkChiaFilePath;
        }
        if(newDataAppSettings.detectedChiaVersion==null){
            throw new IllegalArgumentException();
        }

        String checkFarmerKey=checkFarmerPublicKey(newDataAppSettings.farmerPublicKey);
        if(checkFarmerKey!=null){
            return checkFarmerKey;
        }

        String checkPoolKey=checkPoolPublicKey(newDataAppSettings.poolPublicKey);
        if(checkPoolKey!=null){
            return checkPoolKey;
        }

        this.chiaCliFilePath= newDataAppSettings.chiaCliFilePath;
        this.detectedChiaVersion=newDataAppSettings.detectedChiaVersion;
        this.fingerprint=newDataAppSettings.fingerprint;
        this.farmerPublicKey= newDataAppSettings.farmerPublicKey;
        this.poolPublicKey= newDataAppSettings.poolPublicKey;

        return null;
    }

    public String checkChiaCliFile(String chiaCliFilePath){
        if(chiaCliFilePath==null){
            throw new IllegalArgumentException();
        }
        File file=new File(chiaCliFilePath);

        if(!file.exists()){
            return "Wrong path! File is not exist.";
        }

        //region /Проверяем явлеется ли фаил исполняемым, если нет выходим/
        if(!file.canExecute()){
            return "File is not executable";
        }
        //endregion

        //region /Проверяем, является ли фаил Chia фаилом, сам фаил при этом запускается и проверяется его текстовый ответ, если не тот фаил (не тот ответ) то выходим/
        String command=file.getAbsolutePath();
        final boolean[] isChiaCli = {false};
        String chiaSubstring="Manage chia blockchain infrastructure";

        CliThreadInputHandler inputHandler=new CliThreadInputHandler() {
            @Override
            public void handleStreamInput(String readLine) {
                if(readLine!=null){
                    if(readLine.contains(chiaSubstring)){
                        isChiaCli[0] =true;
                    }
                }

            }
        };

        CliThread cliThread=new CliThread(inputHandler,command);
        try {
            cliThread.start(true);
        }catch (IOException ex){
            ex.printStackTrace();
        }

        if(!isChiaCli[0]){
            return "verifying selected file is failed";
        }
        //endregion
        return null; // null значит все хорошо, нет текста описывающий ошибку
    }

    public String checkFileAndStorePathAndVersion(String chiaCliFilePath){
        String checkResponse=checkChiaCliFile(chiaCliFilePath);
        if(checkResponse!=null){
            return checkResponse;
        }

        File file=new File(chiaCliFilePath);

        String versionCommand= file.getAbsolutePath() + " version";

        StringBuffer stringBuffer=new StringBuffer();

        CliThreadInputHandler versionInputHandler=new CliThreadInputHandler() {
            @Override
            public void handleStreamInput(String readLine) {
                if(readLine!=null){
                    stringBuffer.append(readLine);
                }
            }
        };

        CliThread versionThread=new CliThread(versionInputHandler,versionCommand);
        try {
            versionThread.start(true);
        } catch (IOException e) {
            return e.toString();
        }
        String chiaVersion=stringBuffer.toString();
        this.chiaCliFilePath=chiaCliFilePath;
        this.detectedChiaVersion=chiaVersion;
        return null;
    }

    public void setFingerprint(String fingerprint){
        throw new NotImplementedException();
    }

    public String checkFingerPrint(String fingerprint){
        throw new NotImplementedException();
    }

    public void setFarmerPublicKey(String farmerPublicKey){
        if(checkFarmerPublicKey(farmerPublicKey)!=null){
            throw new IllegalArgumentException();
        }
        this.farmerPublicKey=farmerPublicKey;
    }

    public String checkFarmerPublicKey(String farmerPublicKey){
        if(farmerPublicKey==null){
            return "Farmer public key is null(empty)";
        }

        int length=farmerPublicKey.length();
        if(length!=96){
            return "Farmer public key must be equal 96 chars";
        }


        for ( int i = 1 ; i < farmerPublicKey.length() ; i+=1 ){
            if ( Character.digit(farmerPublicKey.charAt(i), 16) == -1 ){
                return "Farmer public key must contain only hex value";
            }
        }
        return null;

    }

    public void setPoolPublicKey(String poolPublicKey){
        if(checkPoolPublicKey(poolPublicKey)!=null){
            throw new IllegalArgumentException();
        }
        this.poolPublicKey=poolPublicKey;
    }

    public String checkPoolPublicKey(String poolPublicKey){
        if(poolPublicKey==null){
            return "Pool public key is null(empty)";
        }

        int length=poolPublicKey.length();
        if(length!=96){
            return "Pool public key must be equal 96 chars";
        }


        for ( int i = 1 ; i < poolPublicKey.length() ; i+=1 ){
            if ( Character.digit(poolPublicKey.charAt(i), 16) == -1 ){
                return "Pool public key must contain only hex value";
            }
        }
        return null;
    }

    public void clearChiaFilePathAndVersion(){
        this.detectedChiaVersion=null;
        this.chiaCliFilePath=null;
    }

    public void clearKeys(){
        this.fingerprint=null;
        this.farmerPublicKey=null;
        this.poolPublicKey=null;
    }


    public boolean isAllDataSetted(){
        if(!isChiaFileSetted()){
            return false;
        }
        if(!isKeysSetted()){
            return false;
        }
        return true;
    }

    public boolean isChiaFileSetted(){
        if(this.chiaCliFilePath==null){
            return false;
        }
        if(this.detectedChiaVersion==null){
            return false;
        }
        return true;
    }

    public boolean isKeysSetted(){
        if(this.farmerPublicKey==null){
            return false;
        }
        if(this.poolPublicKey==null){
            return false;
        }
        return true;
    }

    public void load(){
        Preferences prefs = Preferences.userNodeForPackage(ru.xander.JavaFxChiaPlotterHelper.Helpers.AppSettings.class);
        String defaultValue = null;
        this.chiaCliFilePath = prefs.get(CHIA_CLI_FILE_PATH, defaultValue);
        this.detectedChiaVersion = prefs.get(CHIA_CLI_VERSION, defaultValue);
        this.fingerprint = prefs.get(CHIA_FINGERPRINT, defaultValue);
        this.farmerPublicKey = prefs.get(CHIA_FARMER_PUBLIC_KEY, defaultValue);
        this.poolPublicKey = prefs.get(CHIA_POOL_PUBLIC_KEY, defaultValue);
        if(this.chiaCliFilePath.equals("")) this.chiaCliFilePath=null;
        if(this.detectedChiaVersion.equals("")) this.detectedChiaVersion=null;
        if(this.fingerprint.equals("")) this.fingerprint=null;
        if(this.farmerPublicKey.equals("")) this.farmerPublicKey=null;
        if(this.poolPublicKey.equals("")) this.poolPublicKey=null;
    }

    public void save(){
        Preferences prefs = Preferences.userNodeForPackage(ru.xander.JavaFxChiaPlotterHelper.Helpers.AppSettings.class);

        String chiaCliFilePathVal=this.chiaCliFilePath;
        String detectedChiaVersionVal=this.detectedChiaVersion;
        String fingerprintVal=this.fingerprint;
        String farmerPublicKeyVal=this.farmerPublicKey;
        String poolPublicKeyVal=this.poolPublicKey;

        if(chiaCliFilePathVal==null) chiaCliFilePathVal="";
        if(detectedChiaVersionVal==null) detectedChiaVersionVal="";
        if(fingerprintVal==null) fingerprintVal="";
        if(farmerPublicKeyVal==null) farmerPublicKeyVal="";
        if(poolPublicKeyVal==null) poolPublicKeyVal="";

        prefs.put(CHIA_CLI_FILE_PATH, chiaCliFilePathVal);
        prefs.put(CHIA_CLI_VERSION, detectedChiaVersionVal);
        prefs.put(CHIA_FINGERPRINT, fingerprintVal);
        prefs.put(CHIA_FARMER_PUBLIC_KEY, farmerPublicKeyVal);
        prefs.put(CHIA_POOL_PUBLIC_KEY, poolPublicKeyVal);


    }


}
