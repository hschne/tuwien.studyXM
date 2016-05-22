package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableSubject;
import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.*;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller of the create exam window in which in which new exams can be saved into the database
 * @author Zhang Haixiang, Bicer Cem
 */
@Component public class InsertExamValuesController implements GuiController {
    private Logger logger = LoggerFactory.getLogger(InsertExamValuesController.class);
    private AlertBuilder alertBuilder;
    private ObservableList<ObservableSubject> subjectList;
    private ObservableList<ObservableTopic> topicList;
    private ExamService examService;
    private SubjectService subjectService;
    private TopicService topicService;
    private QuestionService questionService;

    @Autowired MainFrameController mainFrameController;

    @FXML public Button buttonCreate;
    @FXML public Button buttonCancel;

    @FXML public Label topicText;

    @FXML public ListView<ObservableSubject> subjectListView;
    @FXML public ListView<ObservableTopic> topicListView;

    @FXML public TextField fieldAuthor;
    @FXML public TextField fieldTime;

    @Autowired
    public InsertExamValuesController(ExamService examService, SubjectService subjectService,
        TopicService topicService, QuestionService questionService, AlertBuilder alertBuilder) {
        this.examService = examService;
        this.subjectService = subjectService;
        this.topicService = topicService;
        this.questionService = questionService;
        this.alertBuilder = alertBuilder;
    }

    @FXML public void initialize() {
        logger.debug("Filling subject list");

        try {
            List<ObservableSubject> observableSubjects =
                subjectService.getSubjects().stream().map(ObservableSubject::new)
                    .collect(Collectors.toList());
            subjectList = FXCollections.observableList(observableSubjects);
            subjectListView.setItems(subjectList);
            subjectListView.setCellFactory(lv -> new ListCell<ObservableSubject>() {
                @Override public void updateItem(ObservableSubject item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        String text =
                            item.getSubject().getSubjectId() + ": " + item.getName() + " (" + item
                                .getSemester() + ")";
                        setText(text);
                    }
                }
            });
            subjectListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    topicText.setOpacity(1);
                    topicListView.setDisable(false);
                    try {
                        List<ObservableTopic> observableTopics =
                            topicService.getTopicsFromSubject(newValue.getSubject()).stream()
                                .map(ObservableTopic::new).collect(Collectors.toList());

                        topicList = FXCollections.observableList(observableTopics);
                        topicListView.setItems(topicList);

                    } catch (ServiceException e) {
                        logger.error("Could not get observable topics", e);
                        topicListView.setItems(null);
                    }
                });
            topicListView.setCellFactory(lv -> new ListCell<ObservableTopic>() {
                @Override public void updateItem(ObservableTopic item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        String text = item.getT().getTopicId() + ": " + item.getTopic();
                        setText(text);
                    }
                }
            });
        } catch (Exception e) {
            logger.error("Initialize in InsertExamValuesController not successful", e);
        }
    }

    @FXML public void create() {
        logger.debug("Create button pressed. Entering create method.");

        if (fieldAuthor.getText().isEmpty()) {
            logger.error("TextField \'author\' is empty");
            showAlert(Alert.AlertType.WARNING, "Textfield author must not be empty");
        } else if (fieldTime.getText().isEmpty() || !fieldTime.getText().matches("\\d*")) {
            logger.error("No valid time has been given");
            showAlert(Alert.AlertType.ERROR,
                "No valid time has been given. Make sure to fill the Time textfield with only whole numbers.");
        } else if (topicListView.getSelectionModel().getSelectedItem() == null) {
            logger.warn("No topic selected");
            showAlert(Alert.AlertType.WARNING, "No topic selected.");
        } else {
            Exam exam = new Exam();
            exam.setAuthor(fieldAuthor.getText());
            exam.setCreated(new Timestamp(new Date().getTime()));
            exam.setPassed(false);

            int examTime = 0;

            exam.setSubjectID(
                subjectListView.getSelectionModel().getSelectedItem().getSubject().getSubjectId());

            try {
                examTime = Integer.parseInt(fieldTime.getText());

                exam.setExamQuestions(questionService.getQuestionsFromTopic(
                    topicListView.getSelectionModel().getSelectedItem().getT()));

                examService
                    .createExam(exam, topicListView.getSelectionModel().getSelectedItem().getT(),
                        examTime);
                showAlert(Alert.AlertType.CONFIRMATION, "Exam created");
                mainFrameController.handleExams();
            } catch (ServiceException e) {
                logger.error("Could not create exam: ", e);
                showAlert(Alert.AlertType.ERROR,
                    "Could not create exam. Check logs for more information." + "\n\nHints: "
                        + "\nCheck if the choosen topic has already questions to answer."
                        + "\nCheck if the length of the author do not exceed 80 characters."
                        + "\nCheck if there are enough questions in this topic to cover the exam time.");
            } catch (NumberFormatException e) {
                logger.error("Could not create exam: ", e);
                showAlert(Alert.AlertType.ERROR,
                    "Could not parse exam time. Make sure it only contains numbers and is lower than "
                        + Integer.MAX_VALUE + ".");
            }
        }
    }


    @FXML public void cancel() {
        mainFrameController.handleExams();
    }

    private void showAlert(Alert.AlertType type, String contentMsg) {
        String header = "";

        if (type == Alert.AlertType.ERROR) {
            header = "Error";
        } else if (type == Alert.AlertType.WARNING) {
            header = "Warning";
        } else if (type == Alert.AlertType.CONFIRMATION) {
            header = "Success";
        }

        Alert alert =
            alertBuilder.alertType(type).headerText(header).contentText(contentMsg).build();
        alert.showAndWait();
    }
}
