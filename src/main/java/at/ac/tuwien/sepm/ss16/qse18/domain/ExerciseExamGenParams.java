package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  Zhang Haixiang on 01.06.2016.
 */
public class ExerciseExamGenParams {
    private List<Question> examQuestions = new ArrayList<>();
    private List<Integer> notAnsweredQuestionID = new ArrayList<>(); // conatins all questionID's
    private List<Question> wrongAnsweredQuestions = new ArrayList<>(); // contains the quesitions which have been answered incorrectly
    private List<Question> rightAnsweredQuestions = new ArrayList<>(); // contains the questions which have been answered correctly
    private Map<Integer, Boolean> questionBooleans = new HashMap<>(); // contains the booleans(answered correctly/incorrectly) of already answred questions
    private List<Question> notAnsweredQuestions = new ArrayList<>();
    private long questionTime = 0;

    public List<Question> getExamQuestions() {
        return examQuestions;
    }

    public void setExamQuestions(List<Question> examQuestions) {
        this.examQuestions = examQuestions;
    }

    public List<Integer> getNotAnsweredQuestionID() {
        return notAnsweredQuestionID;
    }

    public void setNotAnsweredQuestionID(List<Integer> notAnsweredQuestionID) {
        this.notAnsweredQuestionID = notAnsweredQuestionID;
    }

    public List<Question> getWrongAnsweredQuestions() {
        return wrongAnsweredQuestions;
    }

    public void setWrongAnsweredQuestions(List<Question> wrongAnsweredQuestions) {
        this.wrongAnsweredQuestions = wrongAnsweredQuestions;
    }

    public List<Question> getRightAnsweredQuestions() {
        return rightAnsweredQuestions;
    }

    public void setRightAnsweredQuestions(List<Question> rightAnsweredQuestions) {
        this.rightAnsweredQuestions = rightAnsweredQuestions;
    }

    public Map<Integer, Boolean> getQuestionBooleans() {
        return questionBooleans;
    }

    public void setQuestionBooleans(Map<Integer, Boolean> questionBooleans) {
        this.questionBooleans = questionBooleans;
    }

    public List<Question> getNotAnsweredQuestions() {
        return notAnsweredQuestions;
    }

    public void setNotAnsweredQuestions(List<Question> notAnsweredQuestions) {
        this.notAnsweredQuestions = notAnsweredQuestions;
    }

    public long getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(long questionTime) {
        this.questionTime = questionTime;
    }
}
