package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableQuestion;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class QuestionOverviewController implements GuiController {

    private static final Logger logger = LogManager.getLogger();

    @FXML public ListView<ObservableQuestion> questionListView;

    @Autowired private AlertBuilder alertBuilder;
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
            showAlert(e);
        }
    }

    private void showAlert(ServiceException e) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("An error occured").contentText(e.getMessage()).build();
        alert.showAndWait();
    }
}
