package ru.xander.JavaFxChiaPlotterHelper.Controllers.ListView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import ru.xander.JavaFxChiaPlotterHelper.Controllers.MainWindowController;
import ru.xander.JavaFxChiaPlotterHelper.Main;


public class PlotTaskCell {


    private PlotSettingsData plotSettingsData=null;

    public int count=-1;

    public HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button paramsButton=new Button("Params");
    Label phaseLabel=new Label("Phase 1/4");
    Label lastLogReportLabel=new Label("last response 00:44:44 ago");
    ProgressBar progressBar=new ProgressBar();
    Button logButton= new Button("Logs");
    Button rerunButton = new Button("Rerun");
    Button stopButton = new Button("Stop!");


    String lastItem;




    public PlotTaskCell(PlotSettingsData plotSettingsData) {
        super();
        if(plotSettingsData==null){
            throw new IllegalArgumentException();
        }
        this.plotSettingsData=plotSettingsData;
        initialization();

    }

    private void initialization(){
        hbox.setSpacing(20);
        hbox.getChildren().addAll(paramsButton,pane, phaseLabel,lastLogReportLabel,progressBar,logButton,rerunButton,stopButton);
        HBox.setHgrow(pane, Priority.ALWAYS);

        rerunButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rerunTask();
            }
        });

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stopTask();
            }
        });

        logButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PlotLogWindow plotLogWindow=new PlotLogWindow();
                try {
                    plotLogWindow.initWindow();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        });

        paramsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainWindowController.currentMainWindowController.showPlotParams(plotSettingsData);
            }
        });
    }


    public void rerunTask(){
        System.out.println("rerun task pressed");
    }

    public void stopTask(){
        System.out.println("stop task pressed");
        MainWindowController.currentMainWindowController.removePlotTaskCell(this);
    }


}
