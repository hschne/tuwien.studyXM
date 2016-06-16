package at.ac.tuwien.sepm.ss16.qse18.gui.statistic;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.navigation.SubjectNavigation;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A controller for the statistics overview, accessible by the statistics button.
 *
 * @author Julian Strohmayer
 */
@Component public class StatisticOverviewController extends BaseController {

    @FXML private ListView<ObservableSubject> subjectListView;
    private ObservableList<ObservableSubject> subjectList;
    private SubjectService subjectService;
    @Autowired SubjectNavigation subjectNavigator;

    @Autowired ApplicationContext applicationContext;

    @Autowired public StatisticOverviewController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @FXML public void initialize() {
        try {
            logger.debug("Initializing subject table");
            //Lambdas to create a new observable subject for each subject
            initializeListView();
            subjectNavigator.refreshMainPane();
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

    /**
     * ActionHandler for creating a new subject
     *
     * @throws IOException
     */
    @FXML public void handleNew() throws IOException {
        logger.debug("Create new subject");
        subjectNavigator.handleCreateSubject(null);
    }

    /**
     * Updates a subject in the list and in the database
     *
     * @param observableSubject The current subject in the list
     * @param subject           The new subject, containing new values
     */
    void updateSubject(ObservableSubject observableSubject, Subject subject) {
        updateEntry(observableSubject, subject);
    }

    private void initializeListView() throws ServiceException {
        List<ObservableSubject> observableSubjects =
                subjectService.getSubjects().stream().map(ObservableSubject::new)
                        .collect(Collectors.toList());
        subjectList = FXCollections.observableArrayList(observableSubjects);
        subjectListView.setItems(subjectList);
        subjectListView.setCellFactory(listView -> applicationContext.getBean(StatisticCell.class));
    }


    private void updateEntry(ObservableSubject observableSubject, Subject subject) {
        observableSubject.setName(subject.getName());
        observableSubject.setEcts(subject.getEcts());
        observableSubject.setSemester(subject.getSemester());
        observableSubject.setAuthor(subject.getAuthor());
    }


}
