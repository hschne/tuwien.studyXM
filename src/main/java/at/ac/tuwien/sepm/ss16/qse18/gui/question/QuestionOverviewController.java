package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableQuestion;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class QuestionOverviewController extends BaseController {

    @FXML public ListView<ObservableQuestion> questionListView;

    @Autowired private QuestionService questionService;
    @Autowired private ApplicationContext applicationContext;

    @FXML public void initialize() {
        try {
            logger.debug("Initializing subject table");
            List<ObservableQuestion> observableQuestions =
                questionService.getQuestion().stream().map(ObservableQuestion::new)
                    .collect(Collectors.toList());
            ObservableList<ObservableQuestion> questionList =
                FXCollections.observableArrayList(observableQuestions);
            questionListView.setItems(questionList);
            questionListView
                .setCellFactory(listView -> applicationContext.getBean(QuestionCell.class));
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }

}
