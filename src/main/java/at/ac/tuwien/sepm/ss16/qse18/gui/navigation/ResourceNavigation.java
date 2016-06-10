package at.ac.tuwien.sepm.ss16.qse18.gui.navigation;

import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.resource.ResourceEditController;
import at.ac.tuwien.sepm.ss16.qse18.gui.resource.ResourceOverviewController;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ResourceNavigation extends SubviewNavigation {

    @Autowired
    public ResourceNavigation(SpringFXMLLoader fxmlLoader, AlertBuilder alertBuilder) {
        super(fxmlLoader, alertBuilder);
    }

    public void handleCreateResource(List inputs, QuestionType questionType) {
        logger.debug("Loading create resource with input list");
        try {
            ResourceEditController resourceEditController =
                setSubView("/fxml/resource/resourceEditView.fxml", ResourceEditController.class);
            resourceEditController.setInput(inputs, questionType);
        } catch (IOException e) {
            handleException(e);
        }
    }

}
