package at.ac.tuwien.sepm.ss16.qse18.gui.navigation;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Hans-Joerg Schroedl
 */
public abstract class Navigation {

    protected final Logger logger = LogManager.getLogger();
    protected Pane pane;
    protected SpringFXMLLoader fxmlLoader;
    private AlertBuilder alertBuilder;

    public Navigation(SpringFXMLLoader fxmlLoader, AlertBuilder alertBuilder){
        this.fxmlLoader = fxmlLoader;
        this.alertBuilder = alertBuilder;
    }

    protected <T extends GuiController> T setSubView(String fxmlPath, Class T)
        throws IOException {
        logger.debug("Loading view from " + fxmlPath);
        SpringFXMLLoader.FXMLWrapper<Object, T> mfWrapper = fxmlLoader.loadAndWrap(fxmlPath, T);
        T controller = mfWrapper.getController();
        configureSubPane(mfWrapper);
        return controller;
    }

    private <T extends GuiController> void configureSubPane(
        SpringFXMLLoader.FXMLWrapper<Object, T> mfWrapper) {
        pane.getChildren().clear();
        Pane subPane = (Pane) mfWrapper.getLoadedObject();
        subPane.setPrefWidth(pane.getWidth());
        subPane.setPrefHeight(pane.getHeight());
        AnchorPane.setTopAnchor(subPane, 0.0);
        AnchorPane.setRightAnchor(subPane, 0.0);
        AnchorPane.setLeftAnchor(subPane, 0.0);
        AnchorPane.setBottomAnchor(subPane, 0.0);
        pane.getChildren().add(subPane);
    }


    protected void handleException(Exception e) {
        logger.error("Exception thrown", e);
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("Could not load sub view.")
            .contentText("Unexpected Error. Please view logs for details.").build();
        alert.showAndWait();
    }

}
