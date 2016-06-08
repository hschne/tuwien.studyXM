package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
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
 * @author Philipp Ganiu
 */
@Component
public class AnswerQuestionController extends BaseController {
    @FXML protected Label questionLabel;
    @FXML protected RadioButton answer1Button;
    @FXML protected RadioButton answer2Button;
    @FXML protected RadioButton answer3Button;
    @FXML protected RadioButton answer4Button;

    @Autowired protected AnswerService answerService;

    private ExerciseExam exam;
    private Question question;
    protected Answer answer1;
    protected Answer answer2;
    protected Answer answer3;
    protected Answer answer4;

    @FXML public void initialize(ExerciseExam exam, Question question, Answer answer1, Answer answer2,
        Answer answer3, Answer answer4){
        this.exam = exam;
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;

        if(answer1 != null) {
            answer1Button.setText(answer1.getAnswer());
        }
        else {
            answer1Button.setVisible(false);
        }
        if(answer2 != null) {
            answer2Button.setText(answer2.getAnswer());
        }
        else {
            answer2Button.setVisible(false);
        }
        if(answer3 != null) {
            answer3Button.setText(answer3.getAnswer());
        }
        else {
            answer3Button.setVisible(false);
        }
        if(answer4 != null) {
            answer4Button.setText(answer4.getAnswer());
        }
        else {
            answer4Button.setVisible(false);
        }
    }

    public boolean isCorrect() {
        /*
        boolean isCorrect = true;

        if((answer1Button.isSelected() && !answer1.isCorrect()) || (!answer1Button.isSelected() &&
            answer1.isCorrect())){
            isCorrect = false;
        }
        if(answer2 != null) {
            if ((answer2Button.isSelected() && !answer2.isCorrect()) || (!answer2Button.isSelected()
                && answer2.isCorrect())) {
                isCorrect = false;
            }
        }

        if(answer3 != null) {
            if ((answer3Button.isSelected() && !answer3.isCorrect()) || (!answer3Button.isSelected()
                && answer3.isCorrect())) {
                isCorrect = false;
            }
        }

        if(answer4 != null) {
            if ((answer4Button.isSelected() && !answer4.isCorrect()) || (!answer4Button.isSelected()
                && answer4.isCorrect())) {
                isCorrect = false;
            }
        }
        */
        Map<Answer, Boolean> answerBooleanMap = new HashMap<>();
        answerBooleanMap.put(answer1, answer1Button.isSelected());
        answerBooleanMap.put(answer2, answer2Button.isSelected());
        answerBooleanMap.put(answer3, answer3Button.isSelected());
        answerBooleanMap.put(answer4, answer4Button.isSelected());
        return answerService.checkIfAnswersAreCorrect(answerBooleanMap);

    }


    public boolean noButtonSelected(){
        return !answer1Button.isSelected() && !answer2Button.isSelected() && !answer3Button.isSelected()
            && !answer4Button.isSelected();
    }
}
