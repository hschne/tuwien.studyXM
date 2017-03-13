package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.QuestionNavigation;
import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.ResourceNavigation;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class ResourceOverviewController extends BaseController {

    @FXML public ListView<ObservableResource> resourceListView;
    @FXML public Button editButton;
    @FXML public Button deleteButton;
    @FXML public Label chooseText;
    @Autowired ApplicationContext applicationContext;
    private ObservableList<ObservableResource> resourceList;
    private ResourceService resourceService;
    private List inputs;
    @FXML private Label resourceLabel;
    private QuestionType questionTypeOfResource;

    @Autowired ResourceNavigation resourceNavigation;

    @Autowired QuestionNavigation questionNavigation;


    @Autowired public ResourceOverviewController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @FXML public void initialize() {
        try {
            logger.debug("Initializing resource table");
            initializeListView();
            resourceNavigation.refreshMainPane();
            if (inputs == null) {
                chooseText.setText("");
            }
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    @FXML public void handleNew() {
        logger.debug("Creating new resource");
        resourceNavigation.handleCreateResource(inputs, questionTypeOfResource);
    }

    public void addResource(ObservableResource resource) throws ServiceException {
        resourceService.createResource(resource.getResource());
        resourceList.add(resource);
    }

    private void initializeListView() throws ServiceException {
        List<ObservableResource> observableResources =
            resourceService.getResources().stream().map(ObservableResource::new)
                .collect(Collectors.toList());
        resourceList = FXCollections.observableArrayList(observableResources);
        resourceListView.setItems(resourceList);
        resourceListView.setCellFactory(listView -> applicationContext.getBean(ResourceCell.class));
    }

    public void setInput(List listOfInputs, Label resourceLabel, QuestionType questionType) {
        this.inputs = listOfInputs;
        this.resourceLabel = resourceLabel;
        this.questionTypeOfResource = questionType;

        if (inputs != null) {
            chooseText.setText(
                "Choose a resource by selecting it or add a new resource by clicking the button below!");

            resourceListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    inputs.remove(inputs.size() - 1); // Replace resource with newly created one
                    inputs.add(newValue);
                    switch (questionTypeOfResource) {
                        case MULTIPLECHOICE:
                            questionNavigation.handleMultipleChoiceQuestion(null, inputs);
                            break;
                        case SINGLECHOICE:
                            questionNavigation.handleSingleChoiceQuestion(null, inputs);
                            break;
                        case OPENQUESTION:
                            questionNavigation.handleOpenQuestion(null, inputs);
                            break;
                        case NOTECARD:
                            questionNavigation.handleImageQuestion(null, inputs);
                            break;
                        case SELF_EVALUATION:
                            questionNavigation.handleSelfEvalQuestion(null,inputs);
                            break;
                        default:
                            throw new InputMismatchException("Can not determine type of resource");
                    }
                });
        }
    }
}
