package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Philipp Ganiu
 */
@Component public class AnswerSelfEvalQuestionController extends AnswerQuestionController {
    @FXML private Text infoText;
    @FXML private Button showAnswerButton;
    @FXML private ImageView answerImage;
    private boolean answered = false;

    @Override
    public void initialize(ExerciseExam exam, Question question, Answer answer1, Answer answer2,
        Answer answer3, Answer answer4) {
        super.initialize(exam, question, answer1, answer2, answer3, answer4);
        String questionText = question.getQuestion().replaceAll("(.{120})", "$1\n");
        questionLabel.setText(questionText);
    }

    @FXML public void handleShowAnswerButton(){
        this.infoText.setVisible(false);
        this.showAnswerButton.setVisible(false);
        this.answer1Button.setVisible(true);
        this.answer2Button.setVisible(true);
        this.answerImage.setVisible(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(answer1.getAnswer()));
        this.answerImage.setImage(new Image(fileChooser.getInitialDirectory().toURI().toString()));

    }

    @Override public boolean noButtonSelected(){
        return !answer1Button.isSelected() && !answer2Button.isSelected();
    }

    @Override public boolean isCorrect() {
        return answer1Button.isSelected();
    }

    @Override public boolean bothButtonsSelected(){
        return answer1Button.isSelected() && answer2Button.isSelected();
    }
}
