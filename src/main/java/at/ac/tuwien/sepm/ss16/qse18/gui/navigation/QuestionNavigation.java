package at.ac.tuwien.sepm.ss16.qse18.gui.navigation;

import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableTopic;
import at.ac.tuwien.sepm.ss16.qse18.gui.question.*;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class QuestionNavigation
    extends SubviewNavigation {

    @Autowired public QuestionNavigation(SpringFXMLLoader fxmlLoader, AlertBuilder alertBuilder) {
        super(fxmlLoader, alertBuilder);
    }

    public void handleMultipleChoiceQuestion(ObservableTopic topic) {
        handleMultipleChoiceQuestion(topic, null);
    }

    /**
     * Load Multiple Choice Question screen with saved input
     *
     * @param topic  The topic for which the multiplechoice question is created for
     *               if null then the topic is already set
     * @param inputs This list contains all inputs of the user
     */
    public void handleMultipleChoiceQuestion(ObservableTopic topic, List inputs) {
        logger.debug("Loading Multiple Choice question screen");
        try {
            CreateMultipleChoiceQuestionController multipleChoiceQuestionController =
                setSubView("/fxml/question/createMultipleChoiceQuestion.fxml",
                    CreateMultipleChoiceQuestionController.class);

            if (topic != null) {
                multipleChoiceQuestionController.setTopic(topic);
            }

            multipleChoiceQuestionController.setInput(inputs);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleSingleChoiceQuestion(ObservableTopic topic) {
        handleSingleChoiceQuestion(topic, null);
    }

    /**
     * Load Single Choice Question screen with saved input
     *
     * @param topic  The topic for which the singlechoice question is created for
     *               if null then the topic is already set
     * @param inputs This list contains all inputs of the user
     */
    public void handleSingleChoiceQuestion(ObservableTopic topic, List inputs) {
        logger.debug("Loading Single Choice question screen ");
        try {
            CreateSingleChoiceQuestionController singleChoiceQuestionController =
                setSubView("/fxml/question/createSingleChoiceQuestion.fxml",
                    CreateSingleChoiceQuestionController.class);

            if (topic != null) {
                singleChoiceQuestionController.setTopic(topic);
            }

            singleChoiceQuestionController.setInput(inputs);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleOpenQuestion(ObservableTopic topic) {
        handleOpenQuestion(topic, null);
    }

    /**
     * Load Open Question screen with saved input
     *
     * @param topic  The topic for which the open question is created for
     *               if null then the topic is already set
     * @param inputs This list contains all inputs of the user
     */
    public void handleOpenQuestion(ObservableTopic topic, List inputs) {
        logger.debug("Loading Open question screen ");
        try {
            CreateOpenQuestionController openQuestionController =
                setSubView("/fxml/question/createOpenQuestion.fxml",
                    CreateMultipleChoiceQuestionController.class);

            if (topic != null) {
                openQuestionController.setTopic(topic);
            }

            openQuestionController.setInput(inputs);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleImageQuestion(ObservableTopic topic) {
        handleImageQuestion(topic, null);
    }

    /**
     * Load Image Question screen with saved input
     *
     * @param topic  The topic for which the image question is created for
     *               if null then the topic is already set
     * @param inputs This list contains all inputs of the user
     */
    public void handleImageQuestion(ObservableTopic topic, List inputs) {
        logger.debug("Loading Image question screen ");
        try {
            CreateImageQuestionController imageQuestionController =
                setSubView("/fxml/question/createImageQuestion.fxml",
                    CreateImageQuestionController.class);
            imageQuestionController.setTopic(topic);
            imageQuestionController.setInput(inputs);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleCreateQuestion(ObservableTopic topic) {
        logger.debug("Loading create question screen");
        try {
            WhichQuestionController whichQuestionController =
                setSubView("/fxml/question/whichQuestion.fxml", WhichQuestionController.class);
            whichQuestionController.setTopic(topic);
        } catch (Exception e) {
            handleException(e);
        }
    }
}
