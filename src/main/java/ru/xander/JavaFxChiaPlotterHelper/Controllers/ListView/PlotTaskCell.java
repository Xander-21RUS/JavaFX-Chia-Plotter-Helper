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

public class PlotTaskCell extends ListCell<PlotTaskCell> {






    HBox hbox = new HBox();
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

    public PlotTaskCell() {
        super();
        initialization();
    }

    private void initialization(){
        hbox.setSpacing(20);
        hbox.getChildren().addAll(paramsButton,pane, phaseLabel,lastLogReportLabel,progressBar,logButton,rerunButton,stopButton);
        HBox.setHgrow(pane, Priority.ALWAYS);
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(lastItem + " : " + event);
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
    }

    @Override
    protected void updateItem(PlotTaskCell item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {


            setGraphic(hbox);
        }
    }
}
