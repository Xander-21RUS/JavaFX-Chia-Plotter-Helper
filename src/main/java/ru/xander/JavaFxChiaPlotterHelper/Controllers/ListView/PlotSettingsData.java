package ru.xander.JavaFxChiaPlotterHelper.Controllers.ListView;

import javafx.scene.control.Alert;

import java.io.File;

public class PlotSettingsData {
    public static final int minPlotSize=32;
    public static final int maxPlotSize=35;
    public static final int minimumBuckets=16;
    public static final int maximumBuckets=256;
    public static final int minimumRamUsage=512;
    public static final int maximumRamUsage=Integer.MAX_VALUE;
    public static final int minimumThreads=1;
    public static final int maximumThreads=128;



    private int plotSize=-1;
    private int buckets=-1;
    private int maxRamUsage=-1;
    private int threads=-1;
    private String temporaryPath=null;
    private boolean useSecondTemporary=false;
    private String secondTemporaryPath=null;
    private String finalPath=null;


    public PlotSettingsData(int plotSize,int buckets,int maxRamUsage,int threads,String temporaryPath,boolean useSecondTemporary,String secondTemporaryPath,String finalPath){
        checkPlotSize(plotSize);
        checkBucketsNumber(buckets);
        checkMaxRamUsage(maxRamUsage);
        checkThreadsNumber(threads);
        checkTemporaryPath(temporaryPath);
        checkSecondTemporaryPath(useSecondTemporary,secondTemporaryPath);
        checkFinalPath(finalPath);

        this.plotSize=plotSize;
        this.buckets=buckets;
        this.maxRamUsage=maxRamUsage;
        this.threads=threads;
        this.temporaryPath=temporaryPath;
        this.useSecondTemporary=useSecondTemporary;
        if(!useSecondTemporary){
            this.secondTemporaryPath="";
        }else {
            this.secondTemporaryPath=secondTemporaryPath;
        }
        this.finalPath=finalPath;
    }

    public int getPlotSize(){
        return this.plotSize;
    }

    public void setPlotSize(int plotSize){
        checkPlotSize(plotSize);
        this.plotSize=plotSize;
    }

    public int getBuckets(){
        return this.buckets;
    }

    public void setBuckets(int buckets){
        checkBucketsNumber(buckets);
        this.buckets=buckets;
    }

    public int getMaxRamUsage() {
        return this.maxRamUsage;
    }

    public void setMaxRamUsage(int maxRamUsage) {
        checkMaxRamUsage(maxRamUsage);
        this.maxRamUsage = maxRamUsage;
    }

    public int getThreads(){
        return this.threads;
    }

    public void setThreads(int threads){
        checkThreadsNumber(threads);
        this.threads=threads;
    }

    public String getTemporaryPath(){
        return this.temporaryPath;
    }

    public void setTemporaryPath(String temporaryPath){
        checkTemporaryPath(temporaryPath);
    }

    public boolean getUseSecondTemporary(){
        return this.useSecondTemporary;
    }

    public void setUseSecondTemporary(boolean useSecondTemporary){
        this.useSecondTemporary=useSecondTemporary;
        this.secondTemporaryPath="";
    }

    public String getSecondTemporaryPath(){
        return this.secondTemporaryPath;
    }

    public void setSecondTemporaryPath(String secondTemporaryPath){
        checkSecondTemporaryPath(true,secondTemporaryPath);
    }

    public String getFinalPath(){
        return this.finalPath;
    }

    public void setFinalPath(String finalPath){
        checkFinalPath(finalPath);
        this.finalPath=finalPath;
    }


    public static void checkPlotSize(int plotSize){
        if(plotSize<minPlotSize || maxPlotSize<plotSize){
            throw new IllegalArgumentException();
        }
    }

    public static void checkBucketsNumber(int buckets){
        if(buckets <minimumBuckets || maximumBuckets <buckets){
            throw new IllegalArgumentException();
        }

        double log2= Math.log(buckets)/Math.log(2);
        if (log2 % 1 == 0) {
            //целое

        }else {
            //не целое
            throw new IllegalArgumentException();
        }

    }

    public static void checkMaxRamUsage(int maxRamUsage){
        if(maxRamUsage<minimumRamUsage || maximumRamUsage<maxRamUsage){
            throw new IllegalArgumentException();
        }
    }

    public static void checkThreadsNumber(int threads){
        if (threads < minimumThreads || maximumThreads < threads) {

            throw new IllegalArgumentException();
        }
    }

    private static void checkDir(String path,String dirName){
        if(path==null){
            throw new IllegalArgumentException(dirName + " path is NULL");
        }


        File file=new File(path);

        if(!file.exists()){
            throw new IllegalArgumentException( dirName + " path does not exist");

        }

        if(!file.isDirectory()){
            throw new IllegalArgumentException(dirName + " path is not directory");
        }

        if(!file.canWrite()){
            throw new IllegalArgumentException(dirName+" directory can't be written");
        }
    }

    public static void checkTemporaryPath(String temporaryPath){
        checkDir(temporaryPath,"Temporary");
    }

    public static void checkSecondTemporaryPath(boolean useSecondTemporary,String secondTemporaryPath){
        if(!useSecondTemporary) return;
        checkDir(secondTemporaryPath,"Second temporary");
    }

    public static void checkFinalPath(String finalPath){
        checkDir(finalPath,"Final");
    }
}
