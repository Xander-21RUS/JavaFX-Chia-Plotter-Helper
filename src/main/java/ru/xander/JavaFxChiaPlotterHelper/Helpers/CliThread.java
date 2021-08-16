package ru.xander.JavaFxChiaPlotterHelper.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CliThread {

    private boolean stopped=false;
    private Process process;
    private Thread thread;
    private CliThreadInputHandler inputHandler;
    private String command;

    public CliThread(CliThreadInputHandler inputHandler,String command){
        if(inputHandler==null){
            throw new IllegalArgumentException();
        }
        if(command==null){
            throw new IllegalArgumentException();
        }
        this.inputHandler=inputHandler;
        this.command=command;

    }

    public void start(boolean waitFor) throws IOException {

        if(stopped){
            throw new IllegalStateException("Process stopped");
        }


        this.process=Runtime.getRuntime().exec(command);
        this.thread=new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                try {
                    String line = null;
                    while ((line = input.readLine()) != null){
                        inputHandler.handleStreamInput(line);
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        this.thread.start();

        if(waitFor){
            try {
                this.process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public void stop(){
        this.process.destroy();
        this.thread.interrupt();
        this.stopped=true;
    }


}
