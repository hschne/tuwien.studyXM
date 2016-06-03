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
 * This is a controller for answering multiple and single choice questions
 * @author Philipp Ganiu
 */
@Component
public class AnswerChoiceQuestionController extends AnswerQuestionController {
    @FXML private Label questionLabel;



    @Override
    @FXML public void initialize(Exam exam, Question question, Answer answer1, Answer answer2,
        Answer answer3, Answer answer4){
            super.initialize(exam,question,answer1,answer2,answer3,answer4);
            String s = "";
            if(question.getType() == QuestionType.MULTIPLECHOICE){
                s = "(MULTIPLECHOICE)";
            }
            else {
                s = "(SINGLECHOICE)";
            }
            questionLabel.setText(question.getQuestion() + " " + s);
        }

}
