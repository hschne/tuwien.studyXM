package at.ac.tuwien.sepm.ss16.qse18.gui.navigation;

import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public abstract class SubviewNavigation extends Navigation {

    @Autowired private MainFrameController mainFrameController;

    public SubviewNavigation(SpringFXMLLoader fxmlLoader, AlertBuilder alertBuilder) {
        super(fxmlLoader, alertBuilder);
    }

    /**
     * This method refreshes the root pane for this navigator. Must be called at one point for each controller,
     * as the main initialization via FXML happens after the constructor call of spring.
     */
    public void refreshMainPane() {
        if (pane == null) {
            pane = mainFrameController.getPaneContent();
        }
    }
}
