package ru.xander.JavaFxChiaPlotterHelper.Helpers;

import javafx.scene.control.Alert;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

public class AppSettings {


    public static final int FINGERPRINT_INDEX=0;
    public static final int MASTER_KEY_INDEX=1;
    public static final int FARMER_KEY_INDEX=2;
    public static final int POOL_KEY_INDEX=3;
    public static final int FIRST_WALLET_INDEX=4;

    private final String CHIA_CLI_FILE_PATH = "ChiaCliFilePath";
    private final String CHIA_CLI_VERSION = "ChiaCliVersion";
    private final String CHIA_FINGERPRINT = "ChiaFingerprint";
    private final String CHIA_FARMER_PUBLIC_KEY = "ChiaFarmerKey";
    private final String CHIA_POOL_PUBLIC_KEY = "ChiaPoolKey";

    public String chiaCliFilePath=null;
    public String detectedChiaVersion=null;
    public String fingerprint=null;
    public String farmerPublicKey=null; // уже не используется, надо избавится со временем от этой
    public String poolPublicKey=null; // уже не используется, надо избавится со временем от этой

    private String unusualResponseErrorText="seems chia command \"keys show\" response is changed";
    private String[] containsKeyNames = new String[]{"Fingerprint","Master public key","Farmer public key","Pool public key","First wallet address"};
    public final List<String[]> keysArrayList=new ArrayList<>();






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
        //this.keysArrayList.clear();
        int listLength=toCopy.keysArrayList.size();
        for(int index=0; listLength>index;index+=1){
            String[] keysArray= Arrays.copyOf(toCopy.keysArrayList.get(index),this.containsKeyNames.length);
            this.keysArrayList.add(keysArray);
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


    public void applySettings(AppSettings newDataAppSettings){
        if(newDataAppSettings==null){
            throw new IllegalArgumentException();
        }
        String checkChiaFilePath=checkChiaCliFile(newDataAppSettings.chiaCliFilePath);
        if(checkChiaFilePath!=null){
            throw new IllegalArgumentException(checkChiaFilePath);
        }
        if(newDataAppSettings.detectedChiaVersion==null){
            throw new IllegalArgumentException();
        }

        String checkFarmerKey=checkFarmerPublicKey(newDataAppSettings.farmerPublicKey);
        if(checkFarmerKey!=null){
            throw new IllegalArgumentException(checkFarmerKey);
        }

        String checkPoolKey=checkPoolPublicKey(newDataAppSettings.poolPublicKey);
        if(checkPoolKey!=null){
            throw new IllegalArgumentException(checkPoolKey);
        }

        this.chiaCliFilePath= newDataAppSettings.chiaCliFilePath;
        this.detectedChiaVersion=newDataAppSettings.detectedChiaVersion;
        this.fingerprint=newDataAppSettings.fingerprint;
        this.farmerPublicKey= newDataAppSettings.farmerPublicKey;
        this.poolPublicKey= newDataAppSettings.poolPublicKey;
        this.keysArrayList.clear();
        int listLength=newDataAppSettings.keysArrayList.size();
        for(int index=0; listLength>index;index+=1){
            String[] keysArray= Arrays.copyOf(newDataAppSettings.keysArrayList.get(index),this.containsKeyNames.length);
            this.keysArrayList.add(keysArray);
        }

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


    public void updateFingerprintAndKeys(){
        List<String[]> tmpKeysList=getFingerprintsAndKeys(this.chiaCliFilePath);

        keysArrayList.clear();
        int listLength=tmpKeysList.size();
        for(int index=0; listLength>index;index+=1){
            String[] keysArray= Arrays.copyOf(tmpKeysList.get(index),this.containsKeyNames.length);
            this.keysArrayList.add(keysArray);
        }
    }

    private List<String[]> getFingerprintsAndKeys(String chiaCliFilePath){

        if(chiaCliFilePath==null){
            throw new IllegalArgumentException();
        }
        File file=new File(chiaCliFilePath);

        if(!file.exists()){
            throw new IllegalStateException("Wrong path! File is not exist.");
        }

        //region /Проверяем явлеется ли фаил исполняемым, если нет выходим/
        if(!file.canExecute()){
            throw new IllegalStateException("File is not executable");
        }
        //endregion

        //region /Проверяем, является ли фаил Chia фаилом, сам фаил при этом запускается и проверяется его текстовый ответ, если не тот фаил (не тот ответ) то выходим/
        String command=file.getAbsolutePath() + " keys show";

        final String[] introText = {"Showing all public keys derived from your private keys:"};
        final List<String[]> keysArrayList=new ArrayList<>();
        final int[] keyCount=new int[]{0};
        CliThreadInputHandler inputHandler=new CliThreadInputHandler() {
            @Override
            public void handleStreamInput(String readLine) {
                if(readLine!=null){
                    if(introText[0] !=null){
                        if(!readLine.contains(introText[0])){
                            throw new IllegalStateException(unusualResponseErrorText);
                        }else {
                            introText[0] =null;
                        }
                    }

                    if(readLine.contains("Fingerprint")){
                        keyCount[0]=1;
                        String[] keyArray=new String[5];
                        String[] result = readLine.split(":");
                        if(result.length!=2){
                            throw new IllegalStateException();
                        }
                        String keyValue=result[1].trim();
                        keyArray[0]=keyValue;
                        keysArrayList.add(keyArray);
                        return;

                    }else {

                        if(readLine.length()==0){
                            return;
                        }

                        if(containsKeyNames.length<=keyCount[0]){
                            throw new IllegalStateException(unusualResponseErrorText);
                        }
                        int size= keysArrayList.size();
                        if(size==0){
                            return;
                        }
                        String[] keysArray=keysArrayList.get(size-1);
                        if(keysArray[keyCount[0]]!=null)
                        {throw new IllegalStateException();}
                        if(readLine.contains(containsKeyNames[keyCount[0]])){
                            //все хорошо, имя ключа совпали

                            String[] result = readLine.split(":");
                            if(result.length!=2){
                                throw new IllegalStateException();
                            }
                            String keyValue=result[1].trim();
                            keysArray[keyCount[0]]=keyValue;

                            keyCount[0]+=1;
                            return;
                        }else{
                            //все плохо, так не долно быть
                            throw new IllegalStateException(unusualResponseErrorText);
                        }
                    }


                }

            }
        };

        CliThread cliThread=new CliThread(inputHandler,command);
        try {
            cliThread.start(true);
        }catch (IOException ex){
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
        return keysArrayList;

    }

    public void setFingerprint(String fingerprint){
        String error=checkFingerprint(fingerprint);
        if(error!=null){
            throw new IllegalArgumentException(error);
        }
        this.fingerprint=fingerprint;
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

    @Deprecated
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
    public String checkFingerprint(String fingerprint){
        if(fingerprint==null){
            return "fingerprint value is NULL";
        }
        if(fingerprint.length()<9){
            return "fingerprint value low then 9 chars";
        }
        if(!fingerprint.matches("[0-9]+")){
            return "fingerprint value contains not number chars";
        }
        return null;


    }


    public void setPoolPublicKey(String poolPublicKey){
        if(checkPoolPublicKey(poolPublicKey)!=null){
            throw new IllegalArgumentException();
        }
        this.poolPublicKey=poolPublicKey;
    }

    @Deprecated
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


    public int getFingerprintIndex(){
        return getFingerprintIndex(this.fingerprint);
    }

    public int getFingerprintIndex(String fingerprint){
        if(fingerprint==null){
            return -1;
        }
        if(fingerprint.length()==0){
            return -1;
        }

        for(int count=0;count<keysArrayList.size();count+=1){
            String[] keyArray=keysArrayList.get(count);
            if(keyArray.length!=containsKeyNames.length){
                throw new IllegalStateException();
            }

            if(fingerprint.equals(keyArray[FINGERPRINT_INDEX])){
                return count;
            }
        }
        return -1;
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


    @Deprecated
    public boolean isAllDataSetted(){
        if(!isChiaFileSetted()){
            return false;
        }
        if(!isKeysSetted()){
            return false;
        }
        return true;
    }

    @Deprecated
    public boolean isChiaFileSetted(){
        if(this.chiaCliFilePath==null){
            return false;
        }
        if(this.detectedChiaVersion==null){
            return false;
        }
        return true;
    }

    @Deprecated
    public boolean isKeysSetted(){
        if(this.farmerPublicKey==null){
            return false;
        }
        if(this.poolPublicKey==null){
            return false;
        }
        return true;
    }

    public boolean checkActualityOfDAta(){
        //проверяем в начале путь до файла действителен или нет
        if(!isChiaCliFilePathActual()) return false;
        //  ну еще надо проверить указанный ранее fingerprint
        if(!isFingerprintActual()) return false;
        //значит все актуально
        return true;
    }

    public boolean isChiaCliFilePathActual(){
        String checkFileError=checkChiaCliFile(chiaCliFilePath);
        if(checkFileError!=null){
            return false;
        }
        return true;
    }

    public boolean isFingerprintActual(){
       try {
           updateFingerprintAndKeys();
       }catch (Exception ex){
           return false;
       }
        int fingerprintIndex=getFingerprintIndex(fingerprint);
        if(fingerprintIndex<0){
            return false;
        }
        return true;
    }

    public String[] getFingerprintPublicKeysArray(String fingerprint){
        if(fingerprint==null){
            return null;
        }
        for (int count=0;this.keysArrayList.size()>count;count+=1){
            String[] keyArrayAtIndex=this.keysArrayList.get(count);
            if(fingerprint.equals(keyArrayAtIndex[FINGERPRINT_INDEX])){
                return keyArrayAtIndex;
            }
        }
        return null;
    }

    public String[] getFingerprintOnlyArray(){
        int preSize=this.keysArrayList.size();
        List<String> fingerprintArrayList=new ArrayList<>(preSize); // на такое иду чтобы от других потоков небыло сбоев

        for(int count=0;this.keysArrayList.size()>count;count+=1){
            String[] entry=keysArrayList.get(count);
            String fingerprintEntry =entry[AppSettings.FINGERPRINT_INDEX];
            if(fingerprintEntry==null){
                throw new IllegalStateException();
            }
            fingerprintArrayList.add(fingerprintEntry);
        }
        String[] returnArray= new String[fingerprintArrayList.size()];
        fingerprintArrayList.toArray(returnArray);
        return returnArray;

    }

    public void load(){
        Preferences prefs = Preferences.userNodeForPackage(ru.xander.JavaFxChiaPlotterHelper.Helpers.AppSettings.class);
        String defaultValue = null;
        this.chiaCliFilePath = prefs.get(CHIA_CLI_FILE_PATH, defaultValue);
        this.detectedChiaVersion = prefs.get(CHIA_CLI_VERSION, defaultValue);
        this.fingerprint = prefs.get(CHIA_FINGERPRINT, defaultValue);
        this.farmerPublicKey = prefs.get(CHIA_FARMER_PUBLIC_KEY, defaultValue);
        this.poolPublicKey = prefs.get(CHIA_POOL_PUBLIC_KEY, defaultValue);
        if(this.chiaCliFilePath!=null){
            if(this.chiaCliFilePath.equals("")) this.chiaCliFilePath=null;
        }
        if(this.detectedChiaVersion!=null){
            if(this.detectedChiaVersion.equals("")) this.detectedChiaVersion=null;
        }
        if(this.fingerprint!=null){
            if(this.fingerprint.equals("")) this.fingerprint=null;
        }
        if(this.farmerPublicKey!=null){
            if(this.farmerPublicKey.equals("")) this.farmerPublicKey=null;
        }
        if(this.poolPublicKey!=null){
            if(this.poolPublicKey.equals("")) this.poolPublicKey=null;
        }
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
