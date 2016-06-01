package at.ac.tuwien.sepm.ss16.qse18.application;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * The starting point of Study XM
 *
 * @author Hans-Joerg Schroedl
 */
@Configuration @ComponentScan("at.ac.tuwien.sepm") public class MainApplication
    extends Application {

    private final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    private AlertBuilder alertBuilder = new AlertBuilder();
    private AnnotationConfigApplicationContext applicationContext = null;

    public static void main(String[] args) {

        Application.launch(args);
    }

    @Override public void start(Stage primaryStage) throws IOException {
        logger.info("Starting Application");
        applicationContext = new AnnotationConfigApplicationContext(MainApplication.class);
        SpringFXMLLoader springFXMLLoader = applicationContext.getBean(SpringFXMLLoader.class);
        SpringFXMLLoader.FXMLWrapper<Object, MainFrameController> mfWrapper =
            springFXMLLoader.loadAndWrap("/fxml/mainFrame.fxml", MainFrameController.class);
        primaryStage.setTitle("Study XM");
        Scene scene = new Scene((Parent) mfWrapper.getLoadedObject(), 1280, 720);
        String css = this.getClass().getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        //TODO revert
        primaryStage.setResizable(true);
        primaryStage.setOnCloseRequest(this::closeWithConfirmation);
        primaryStage.show();

        try {
            new ConnectionH2().getConnection();
        } catch (SQLException e) {
            logger.error("Unable to connect to database. ", e);
            primaryStage.close();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to connect to database.",
                "The application wasn't able to get a connection to the database. "
                    + "Please make sure h2.bat is running and try again.");

        }
    }

    private void closeWithConfirmation(Event e) {
        Optional<ButtonType> closing =
            showAlert(Alert.AlertType.CONFIRMATION, "Closing application", "Closing application",
                "Are you sure you want to close the application?");

        if (closing.get() != ButtonType.OK) {
            e.consume();
        }
    }

    @Override public void stop() throws Exception {
        logger.info("Stopping Application");

        if (this.applicationContext != null && applicationContext.isRunning()) {
            this.applicationContext.close();
            new ConnectionH2().closeConnection();
        }
        super.stop();
    }

    /**
     * Creates and shows a new Alert.
     *
     * @param type       alert type
     * @param title      alert title
     * @param headerText alert header text
     */
    private Optional<ButtonType> showAlert(Alert.AlertType type, String title, String headerText,
        String contentText) {
        Alert alert = alertBuilder.alertType(type).title(title).headerText(headerText)
            .contentText(contentText).setResizable(true).build();
        ObservableList<ButtonType> buttons =  alert.getButtonTypes();

        for (ButtonType b : buttons) {
            if (b == ButtonType.CANCEL) {
                buttons.remove(b);
                buttons.add(new ButtonType("Cancel"));
            }
        }
        return alert.showAndWait();
    }
}
