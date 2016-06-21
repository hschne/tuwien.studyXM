package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.service.AnswerService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a controller for answering questions that have multiple or single choice answers
 * and an image or a question as a question
 *
 * @author Philipp Ganiu
 */
@Component
public class AnswerQuestionController extends BaseController {
    @FXML protected Label questionLabel;
    @FXML protected RadioButton answer1Button;
    @FXML protected RadioButton answer2Button;
    @FXML protected RadioButton answer3Button;
    @FXML protected RadioButton answer4Button;

    private static final String REPLACE_MATCH = "(.{100})";
    private static final String REPLACE_LITERAL = "$1\n";

    @Autowired protected AnswerService answerService;

    private ExerciseExam exam;
    private Question question;
    protected Answer answer1;
    protected Answer answer2;
    protected Answer answer3;
    protected Answer answer4;


    /**
     * initializes all fields
     * */
    @FXML public void initialize(ExerciseExam exam, Question question, Answer answer1, Answer answer2,
        Answer answer3, Answer answer4){
        this.exam = exam;
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;

        if(answer1 != null && question.getType() != QuestionType.SELF_EVALUATION) {
            String answer1Text = answer1.getAnswer().replaceAll(REPLACE_MATCH, REPLACE_LITERAL);
            answer1Button.setText(answer1Text);
        }
        else {
            answer1Button.setVisible(false);
        }
        if(answer2 != null) {
            String answer2Text = answer2.getAnswer().replaceAll(REPLACE_MATCH, REPLACE_LITERAL);
            answer2Button.setText(answer2Text);
        }
        else {
            answer2Button.setVisible(false);
        }
        if(answer3 != null) {
            String answer3Text = answer3.getAnswer().replaceAll(REPLACE_MATCH, REPLACE_LITERAL);
            answer3Button.setText(answer3Text);
        }
        else {
            answer3Button.setVisible(false);
        }
        if(answer4 != null) {
            String answer4Text = answer4.getAnswer().replaceAll(REPLACE_MATCH, REPLACE_LITERAL);
            answer4Button.setText(answer4Text);
        }
        else {
            answer4Button.setVisible(false);
        }
    }

    /**
     * checks whether the user answered the question correctly
     * */
    public boolean isCorrect() {
        Map<Answer, Boolean> answerBooleanMap = new HashMap<>();
        answerBooleanMap.put(answer1, answer1Button.isSelected());
        answerBooleanMap.put(answer2, answer2Button.isSelected());
        answerBooleanMap.put(answer3, answer3Button.isSelected());
        answerBooleanMap.put(answer4, answer4Button.isSelected());
        return answerService.checkIfAnswersAreCorrect(answerBooleanMap);

    }

    /**
     * checks if not button/answer was selected.
     *
     * @return true if no button/answer was selected, else false
     * */
    public boolean noButtonSelected(){
        return !answer1Button.isSelected() && !answer2Button.isSelected() && !answer3Button.isSelected()
            && !answer4Button.isSelected();
    }

    /**
     * checks if bothButtons were selected. Only relevant in AnswerSelfEvalQuestionController. Returns
     * false in all other controllers since selected all answers is not an issue
     *
     * @return false
     * */
    public boolean bothButtonsSelected(){
        return false;
    }
}
