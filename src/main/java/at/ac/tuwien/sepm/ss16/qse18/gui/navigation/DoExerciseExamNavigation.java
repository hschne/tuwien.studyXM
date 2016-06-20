package at.ac.tuwien.sepm.ss16.qse18.gui.navigation;

import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise.*;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DoExerciseExamNavigation extends SubviewNavigation {

    /**
     * Used for navigating between views of a running exercise exam.
     * @param fxmlLoader The fxml loader to inject
     * @param alertBuilder The alert builder used for displaying alerts
     */
    @Autowired
    public DoExerciseExamNavigation(SpringFXMLLoader fxmlLoader, AlertBuilder alertBuilder) {
        super(fxmlLoader, alertBuilder);
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }

    /**
     * Loads the matching view for the next question
     * @param type The question type
     * @return The controller for the next question view
     */
    public AnswerQuestionController loadQuestionView(QuestionType type){
        AnswerQuestionController controller = null;
        if(type == QuestionType.MULTIPLECHOICE || type == QuestionType.SINGLECHOICE){
            controller = handleMultipleChoice();
        }
        else if(type == QuestionType.NOTECARD){
            controller = handleNoteCard();
        }
        else if(type == QuestionType.OPENQUESTION){
            controller = handleOpenQuestion();
        }
        else if(type == QuestionType.SELF_EVALUATION){
            controller = handleSelfEvalQuestion();
        }
        return controller;
    }


    private  <T extends GuiController> T handleMultipleChoice(){
        T controller = null;
        try {
            controller =
                setSubView("/fxml/exam/answerChoiceQuestion.fxml",
                    AnswerChoiceQuestionController.class);

        }
        catch (IOException e){
            logger.error(e.getMessage());
            handleException(e);
        }
        return controller;
    }

    private  <T extends GuiController> T handleNoteCard(){
        T controller = null;
        try{
            controller = setSubView("/fxml/exam/answerImageQuestion.fxml",
                AnswerImageQuestionController.class);
        }
        catch (IOException e){
            logger.error(e.getMessage());
            handleException(e);
        }
        return controller;
    }

    private  <T extends GuiController> T handleOpenQuestion(){
        T controller = null;
        try{
            controller = setSubView("/fxml/exam/answerOpenQuestion.fxml",
                AnswerOpenQuestionController.class);
        }
        catch (IOException e){
            logger.error(e.getMessage());
            handleException(e);
        }
        return controller;
    }

    private  <T extends GuiController> T handleSelfEvalQuestion(){
        T controller = null;
        try{
            controller = setSubView("/fxml/exam/answerSelfEvalQuestion.fxml",
                AnswerSelfEvalQuestionController.class);
        }
        catch (IOException e){
            logger.error(e.getMessage());
            handleException(e);
        }
        return controller;
    }

}
