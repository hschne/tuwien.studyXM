package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

/**
 * @author Philipp Ganiu
 */
@Component
public class AnswerImageQuestionController extends AnswerQuestionController {
    @FXML private ImageView image;

    @Override public void initialize(Exam exam, Question question, Answer answer1, Answer answer2,
        Answer answer3, Answer answer4) {
        super.initialize(exam, question, answer1, answer2, answer3, answer4);
        image.setImage(new Image(question.getQuestion().substring(19)));
    }
}
