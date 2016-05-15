package at.ac.tuwien.sepm.ss16.qse18.gui;

import at.ac.tuwien.sepm.ss16.qse18.gui.question.WhichQuestionController;
import at.ac.tuwien.sepm.ss16.qse18.gui.subject.SubjectOverviewController;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The root controller, which loads subviews into the center pane
 *
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class MainFrameController {

    private final Logger logger = LogManager.getLogger();
    @FXML public Pane paneContent;
    private SpringFXMLLoader fxmlLoader;
    private AlertBuilder alertBuilder;
    private Stage primaryStage;

    @Autowired void setSpringFXMLLoader(SpringFXMLLoader loader) {
        this.fxmlLoader = loader;
    }

    @Autowired void setAlertBuilder(AlertBuilder alertBuilder) {
        this.alertBuilder = alertBuilder;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private <T extends GuiController> void setSubView(String fxmlPath, Class T) {
        logger.debug("Loading view from " + fxmlPath);
        SpringFXMLLoader.FXMLWrapper<Object, T> mfWrapper;
        try {
            mfWrapper = fxmlLoader.loadAndWrap(fxmlPath, T);
            T controller = mfWrapper.getController();
            controller.setPrimaryStage(primaryStage);
            paneContent.getChildren().clear();
            Pane pane = (Pane) mfWrapper.getLoadedObject();
            pane.setPrefWidth(paneContent.getWidth());
            pane.setPrefHeight(paneContent.getHeight());
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            paneContent.getChildren().add(pane);
        } catch (IOException e) {
            logger.error(e);
            Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
                .headerText("Could not load sub view").contentText("Please view logs for details")
                .build();
            alert.showAndWait();
        }
    }




    @FXML public void handleHome() {
        logger.debug("Loading home view");
        setSubView("/fxml/question/whichQuestion.fxml", WhichQuestionController.class);
    }

    @FXML public void handleSubjects() {
        logger.debug("Loading subject view");
        setSubView("/fxml/subject/subjectOverview.fxml", SubjectOverviewController.class);
    }

    @FXML public void handleResources() {
        //TODO: Display view here
    }

    @FXML public void handleStatistics() {
        //TODO: Display view here
    }
}
