package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.app.event.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Philipp Ganiu
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class DoExamController implements
    GuiController {
    @FXML private Label timeLeftLabel;
    @FXML private Label titleLabel;
    @FXML private Label progressLabel;
    @FXML AnchorPane subPane;
    @Autowired SpringFXMLLoader springFXMLLoader;
    private static final Logger logger = LogManager.getLogger(DoExamController.class);
    private static final int STARTTIME = 50;
    private IntegerProperty time;
    @FXML private Timeline timeline;

    @FXML private void initialize(){
        time = new SimpleIntegerProperty(STARTTIME);
        timeLeftLabel.textProperty().bind(Bindings.concat(time.asString())
                                                    .concat(new SimpleStringProperty(" min left")));
        countDown();
        titleLabel.setText("EXAM zu SEPM");
        progressLabel.setText("Progress 0/20");
        try {
            setSubView("/fxml/exam/answerMultipleChoiceQuestion.fxml",
                AnswerMultipleChoiceQuestionController.class);
        }
        catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    private void countDown(){
        if (timeline != null) {
            timeline.stop();
        }timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.minutes(STARTTIME+1),
                                                                            new KeyValue(time, 0)));
        timeline.playFromStart();
    }
    private <T extends GuiController> T setSubView(String fxmlPath, Class T) throws IOException {
        logger.debug("Loading view from " + fxmlPath);
        SpringFXMLLoader.FXMLWrapper<Object, T> wrapper = springFXMLLoader.loadAndWrap(fxmlPath, T);
        this.subPane.getChildren().clear();
        Pane newPane = (Pane) wrapper.getLoadedObject();
        newPane.setPrefWidth(this.subPane.getWidth());
        newPane.setPrefHeight(this.subPane.getHeight());
        AnchorPane.setTopAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        this.subPane.getChildren().add(newPane);
        return wrapper.getController();
    }

}
