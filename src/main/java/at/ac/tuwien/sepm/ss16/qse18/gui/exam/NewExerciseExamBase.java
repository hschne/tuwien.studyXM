package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hans-Joerg Schroedl
 */
abstract class NewExerciseExamBase extends BaseController {

    @FXML protected ListView<ObservableTopic> topicListView;
    @FXML protected TextField fieldAuthor;
    @FXML protected TextField fieldTime;
    private ExerciseExamService exerciseExamService;
    private SubjectService subjectService;
    private TopicService topicService;
    protected QuestionService questionService;
    protected Subject subject;

    NewExerciseExamBase(ExerciseExamService exerciseExamService, SubjectService subjectService,
        TopicService topicService, QuestionService questionService) {
        this.exerciseExamService = exerciseExamService;
        this.subjectService = subjectService;
        this.topicService = topicService;
        this.questionService = questionService;
    }

    /**
     * Returns to the exams view
     */
    @FXML public void handleCancel() {
        mainFrameController.handleExams();
    }

    public void setExam(ObservableExam exam) throws ServiceException {
        this.subject = subjectService.getSubject(exam.getSubject());
        initializeTopicList();
    }

    private void initializeTopicList() {
        logger.debug("Filling topic list");
        try {
            List<ObservableTopic> observableTopics =
                topicService.getTopicsFromSubject(subject).stream().map(ObservableTopic::new)
                    .collect(Collectors.toList());
            ObservableList<ObservableTopic> topicList =
                FXCollections.observableList(observableTopics);
            topicListView.setItems(topicList);
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
        } catch (ServiceException e) {
            logger.error("Initialize not successful", e);
            showError(e);
        }
    }

    protected abstract void handleCreate();

    ExerciseExam createExam() {
        ExerciseExam exerciseExam = new ExerciseExam();
        exerciseExam.setAuthor(fieldAuthor.getText());
        exerciseExam.setCreated(new Timestamp(new Date().getTime()));
        exerciseExam.setPassed(false);

        int examTime;

        exerciseExam.setSubjectID(subject.getSubjectId());

        try {
            examTime = Integer.parseInt(fieldTime.getText());

            exerciseExam.setExamQuestions(questionService
                .getQuestionsFromTopic(topicListView.getSelectionModel().getSelectedItem().getT()));

            exerciseExamService.createExam(exerciseExam,
                topicListView.getSelectionModel().getSelectedItem().getT(), examTime);

        } catch (ServiceException e) {
            logger.error("Could not create exerciseExam: ", e);
            showError("Check if the choosen topic has already questions to answer."
                + "\nCheck if the length of the author do not exceed 80 characters."
                + "\nCheck if there are enough questions in this topic to cover the exerciseExam time.");
        } catch (NumberFormatException e) {
            logger.error("Could not create exerciseExam: ", e);
            showError("Could not parse exerciseExam time. " +
                "Make sure it only contains numbers and is lower than " + Integer.MAX_VALUE + ".");
        }
        return exerciseExam;
    }

    boolean validateFields() {
        if (fieldAuthor.getText().isEmpty()) {
            logger.error("TextField \'author\' is empty");
            showError("No author given. Textfield author must not be empty.");
            return true;
        }
        if (fieldTime.getText().isEmpty() || !fieldTime.getText().matches("\\d*")) {
            logger.error("No valid time has been given");
            showError(
                "No valid time has been given. Make sure to fill the Time textfield with only whole numbers.");
            return true;
        }
        if (topicListView.getSelectionModel().getSelectedItem() == null) {
            logger.warn("No topic selected");
            showError(
                "No topic selected. You have to select the topic you want to create an exam to.");
            return true;
        }
        return false;
    }
}
