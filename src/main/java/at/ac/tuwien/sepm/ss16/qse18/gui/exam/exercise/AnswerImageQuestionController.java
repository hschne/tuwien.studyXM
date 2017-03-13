package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * This is a controller for answering notecard questions
 *
 * @author Philipp Ganiu
 */
@Component
public class AnswerImageQuestionController extends AnswerQuestionController {
    @FXML private ImageView image;

    @Override public void initialize(ExerciseExam exam, Question question, Answer answer1, Answer answer2,
        Answer answer3, Answer answer4) {
        super.initialize(exam, question, answer1, answer2, answer3, answer4);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(question.getQuestion()));
        image.setImage(new Image(fileChooser.getInitialDirectory().toURI().toString()));
    }
}
