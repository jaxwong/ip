package gbthefatboy.gui;

import gbthefatboy.entry.GbTheFatBoy;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private GbTheFatBoy gbTheFatBoy;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/legitness"
            + ".jpg"));
    private Image gbTheFatBoyImage = new Image(this.getClass().getResourceAsStream("/images/fatgb"
            + ".jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the GbTheFatBoy instance */
    public void setGbTheFatBoy(GbTheFatBoy gb) {
        gbTheFatBoy = gb;
        // Show welcome message when the application starts
        dialogContainer.getChildren().addAll(
                DialogBox.getGbTheFatBoyDialog(gbTheFatBoy.getWelcomeMessage(), gbTheFatBoyImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing GbTheFatBoy's reply
     * and then appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = gbTheFatBoy.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getGbTheFatBoyDialog(response, gbTheFatBoyImage)
        );
        userInput.clear();

        if (input.trim().equalsIgnoreCase("bye")) {
            // Add a small delay to let the user see the goodbye message
            Platform.runLater(() -> {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Platform.exit();
            });
        }
    }
}
