package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author Philipp Ganiu
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class DoExamController implements
    GuiController {
    @FXML private Label timeLeftLabel;
    @FXML private Label titleLabel;
    @FXML private Label progressLabel;
    @FXML GridPane subPane;
    @Autowired SpringFXMLLoader springFXMLLoader;
    private static final Logger logger = LogManager.getLogger(DoExamController.class);


    @FXML private void initialize(){
        timeLeftLabel.setText("200 min left");
        titleLabel.setText("EXAM zu SEPM");
        progressLabel.setText("Progress 0/20");

    }
    private <T extends GuiController> T setSubView(String fxmlPath, Class T) throws IOException {
        logger.debug("Loading view from " + fxmlPath);
        SpringFXMLLoader.FXMLWrapper<Object, T> mfWrapper = springFXMLLoader.loadAndWrap(fxmlPath, T);
        T controller = mfWrapper.getController();
        setSubPane(mfWrapper);
        return controller;
    }

    private <T extends GuiController> void setSubPane(
        SpringFXMLLoader.FXMLWrapper<Object, T> wrapper){
        this.subPane.getChildren().clear();
        Pane newPane = (Pane) wrapper.getLoadedObject();
        this.subPane.setPrefWidth(this.subPane.getWidth());
        this.subPane.setPrefHeight(this.subPane.getHeight());
        AnchorPane.setTopAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        this.subPane.getChildren().add(newPane);
    }
}
