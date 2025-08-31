package gbthefatboy.gui;

import java.io.IOException;

import gbthefatboy.entry.GbTheFatBoy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for GbTheFatBoy using FXML.
 */
public class Main extends Application {

    private GbTheFatBoy gbTheFatBoy = new GbTheFatBoy("./data/Gbot.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("GbTheFatBoy Task Manager");
            fxmlLoader.<MainWindow>getController().setGbTheFatBoy(gbTheFatBoy);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
