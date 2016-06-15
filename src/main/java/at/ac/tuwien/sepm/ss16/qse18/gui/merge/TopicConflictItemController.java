package at.ac.tuwien.sepm.ss16.qse18.gui.merge;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopicConflict;
import javafx.fxml.FXML;
import javafx.scene.Node;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl
 */

@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class TopicConflictItemController
    extends BaseController {

    @FXML private Node root;

    public void initialize(ObservableTopicConflict topicConflict) {

    }

    public Node getRoot() {
        return root;
    }
}
