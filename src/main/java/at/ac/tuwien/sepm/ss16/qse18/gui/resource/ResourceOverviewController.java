package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class ResourceOverviewController extends BaseController {

    @FXML public ListView<ObservableResource> resourceListView;
    @FXML public Button editButton;
    @FXML public Button deleteButton;
    @Autowired ApplicationContext applicationContext;
    private ObservableList<ObservableResource> resourceList;
    private ResourceService resourceService;


    @Autowired public ResourceOverviewController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @FXML public void initialize() {
        try {
            logger.debug("Initializing subject table");
            initializeListView();
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    public void addResource(ObservableResource resource) throws ServiceException {
        resourceService.createResource(resource.getResource());
        resourceList.add(resource);
    }

    @FXML public void handleNew() {
        logger.debug("Creating new resource");
        mainFrameController.handleCreateResource(null, null);
    }

    private void initializeListView() throws ServiceException {
        List<ObservableResource> observableResources =
            resourceService.getResources().stream().map(ObservableResource::new)
                .collect(Collectors.toList());
        resourceList = FXCollections.observableArrayList(observableResources);
        resourceListView.setItems(resourceList);
        resourceListView.setCellFactory(listView -> applicationContext.getBean(ResourceCell.class));
    }

}
