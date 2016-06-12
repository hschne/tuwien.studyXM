package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

/**
 * This is a controller for answering multiple and single choice questions
 * @author Philipp Ganiu
 */
@Component
public class AnswerChoiceQuestionController extends AnswerQuestionController {

    @Override
    @FXML public void initialize(ExerciseExam exam, Question question, Answer answer1, Answer answer2,
        Answer answer3, Answer answer4){
            super.initialize(exam,question,answer1,answer2,answer3,answer4);
            String s;
            if(question.getType() == QuestionType.MULTIPLECHOICE){
                s = "(MULTIPLECHOICE)";
            }
            else {
                s = "(SINGLECHOICE)";
            }
            questionLabel.setText(question.getQuestion() + " " + s);
        }

}
