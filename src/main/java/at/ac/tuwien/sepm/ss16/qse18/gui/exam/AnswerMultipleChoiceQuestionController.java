package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Philipp Ganiu
 */
@Component
public class AnswerMultipleChoiceQuestionController extends BaseController{
    @FXML private Label questionLabel;
    @FXML private RadioButton answer1Button;
    @FXML private RadioButton answer2Button;
    @FXML private RadioButton answer3Button;
    @FXML private RadioButton answer4Button;

    private static final Logger logger = LogManager.getLogger(AnswerMultipleChoiceQuestionController.class);
    private Exam exam;
    private Question question;
    private Answer answer1;
    private Answer answer2;
    private Answer answer3;
    private Answer answer4;

    @FXML public void initialize(Exam exam, Question question, Answer answer1, Answer answer2,
        Answer answer3, Answer answer4){
        this.exam = exam;
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        String s = "";
        if(question.getType() == QuestionType.MULTIPLECHOICE){
            s = "(MULTIPLECHOICE)";
        }
        else {
            s = "(SINGLECHOICE)";
        }
        questionLabel.setText(question.getQuestion() + " " + s);
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

    public boolean isCorrect(){
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
        return isCorrect;
    }

}
