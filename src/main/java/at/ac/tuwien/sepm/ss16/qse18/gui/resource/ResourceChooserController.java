package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;

@Component public class ResourceChooserController {
    @FXML public Button newButton;
    @FXML public Button cancelButton;
    @FXML public Button chooseButton;
    @FXML public ListView<Resource> resourceListView;

    private ObservableList<ObservableResource> resourcesList;

    @FXML public void initialize() {

    }
}
