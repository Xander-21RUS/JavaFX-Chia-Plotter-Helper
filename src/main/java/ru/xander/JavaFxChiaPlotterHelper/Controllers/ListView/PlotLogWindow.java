package ru.xander.JavaFxChiaPlotterHelper.Controllers.ListView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.xander.JavaFxChiaPlotterHelper.Helpers.AppSettings;

import java.io.IOException;
import java.net.URL;

public class PlotLogWindow {


    private TextArea logTextArea;


    public void initWindow() throws IOException {



        FXMLLoader fxmlLoader = new FXMLLoader();
        URL sceneResource = getClass().getClassLoader().getResource("ru/xander/JavaFxChiaPlotterHelper/FxScenes/Plot_Log.fxml");
        fxmlLoader.setLocation(sceneResource);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 724, 511);
        logTextArea=(TextArea) scene.lookup("#logTextArea");

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setTitle("Plot log");
        stage.setResizable(false);
        stage.show();
        logTextArea.setText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.appendText("kldsjfgfkldjgklfsd;gj dfsk;lgjkdsl;fgjdksfjg");
        logTextArea.setEditable(false);



    }
}
