package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableExam;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementors are controllers for views for creating exercise exams
 *
 * @author Hans-Joerg Schroedl, Zhang Haixiang
 */
abstract class NewExerciseExamBase extends BaseController {

    @FXML protected ListView<ObservableTopic> topicListView;
    @FXML protected ListView<ObservableTopic> addedTopicListView;
    @FXML protected TextField fieldAuthor;
    @FXML protected TextField fieldTime;
    @FXML protected Button buttonAddTopic;
    @FXML protected Button buttonAddAll;
    @FXML protected Button buttonDelete;

    private ExerciseExamService exerciseExamService;
    private SubjectService subjectService;
    private TopicService topicService;
    protected QuestionService questionService;
    protected Subject subject;
    private ObservableExam exam;
    ObservableList<ObservableTopic> firstTopicList;
    ObservableList<ObservableTopic> secondTopicList;
    private List<Topic> topicList = new ArrayList<>();

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
        this.exam = exam;
        this.subject = subjectService.getSubject(exam.getSubject());
        initializeTopicList();
    }

    protected void initializeTopicList() {
        logger.debug("Filling topic list");
        try {
            topicList.clear();
            fieldAuthor.clear();
            populateExamTime();
            List<ObservableTopic> observableTopics =
                topicService.getTopicsFromSubject(subject).stream().map(ObservableTopic::new)
                    .collect(Collectors.toList());
            firstTopicList =
                FXCollections.observableList(observableTopics);

            secondTopicList = FXCollections.observableList(new ArrayList<>());
            addedTopicListView.setItems(secondTopicList);


            topicListView.setItems(firstTopicList);


        } catch (ServiceException e) {
            logger.error("Initialize not successful", e);
            showError(e);
        }
    }

    private void populateExamTime() {
        fieldTime.clear();
        int minTime = Integer.MAX_VALUE;
        int maxTime = 0;
        try {
            for(Topic t : topicService.getTopicsFromSubject(subject)) {
                for(Question q : questionService.getQuestionsFromTopic(t)) {
                    maxTime += q.getQuestionTime();
                    if(q.getQuestionTime() < minTime) {
                        minTime = (int)q.getQuestionTime();
                    }
                }
            }
            fieldTime.setPromptText(maxTime == 0 ? "No questions for this subjects yet" :
                                                minTime + " - " + maxTime + " minutes");
        } catch(ServiceException e) {
            logger.error("Could not populate exam time range", e);
            showError("Could not determine minimum and maximum exam time.");
        }
    }

    protected abstract void handleCreate();

    ExerciseExam createExam() throws ServiceException{
        ExerciseExam exerciseExam = new ExerciseExam();
        exerciseExam.setExam(exam.getExamid());
        exerciseExam.setAuthor(fieldAuthor.getText());
        exerciseExam.setCreated(new Timestamp(new Date().getTime()));
        exerciseExam.setPassed(false);
        int examTime;

        exerciseExam.setSubjectID(subject.getSubjectId());
        try {
            examTime = Integer.parseInt(fieldTime.getText());
            exerciseExam.setExamTime(examTime);
            exerciseExam.setExamQuestions(questionService
                .getQuestionsFromTopic(this.topicList.get(0)));

            exerciseExamService.createExam(exerciseExam, this.topicList, examTime);

        } catch (NumberFormatException e) {
            logger.error("Could not create exerciseExam: ", e);
            showError("Could not parse exerciseExam time. " +
                "Make sure it only contains numbers and is lower than " + Integer.MAX_VALUE + ".");
        }
        return exerciseExam;
    }

    public void addTopic(){
        logger.debug("entering addTopic()");
        if (topicListView.getSelectionModel().getSelectedItem() == null) {
            logger.warn("No topic selected");
            showError(
                "No topic selected. You have to select the topic you want to create an exam to.");
        } else {
            this.topicList.add(topicListView.getSelectionModel().getSelectedItem().getT());
            secondTopicList.add(topicListView.getSelectionModel().getSelectedItem());
            firstTopicList.remove(topicListView.getSelectionModel().getSelectedItem());
        }
    }

    public void addAll(){
        logger.debug("entering addAll()");
        ObservableList<ObservableTopic> temp = FXCollections.observableArrayList(copy(firstTopicList));

        if(firstTopicList.isEmpty()) {
            logger.error("topic view is empty");
            showError("All topics have been added already");
        } else {
            for (ObservableTopic t : temp) {
                secondTopicList.add(t);
                firstTopicList.remove(t);
                this.topicList.add(t.getT());
            }
        }
    }

    public void delete(){
        logger.debug("entering delete()");
        if (addedTopicListView.getSelectionModel().getSelectedItem() == null) {
            logger.warn("No topic selected");
            showError(
                "No topic selected. You have to select the topic you want to delete.");
        } else {
            firstTopicList.add(addedTopicListView.getSelectionModel().getSelectedItem());
            this.topicList.remove(addedTopicListView.getSelectionModel().getSelectedItem().getT());
            secondTopicList.remove(addedTopicListView.getSelectionModel().getSelectedItem());
        }
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
        if (this.topicList.isEmpty()) {
            logger.error("No topic added");
            showError(
                "No topic added. You have to add at least one topic you want to create an exam to.");
            return true;
        }
        return false;
    }

    private ObservableList<ObservableTopic> copy(ObservableList<ObservableTopic> topicObservableList){
        ObservableList<ObservableTopic> temp = FXCollections.observableArrayList();
        for(ObservableTopic t: topicObservableList){
            temp.add(t);
        }
        return temp;
    }
}
