package at.ac.tuwien.sepm.ss16.qseXX.gui;

import at.ac.tuwien.sepm.ss16.qseXX.service.AnswerService;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A controller for questionAnswerFrame, to deliver all the answers.
 *
 * @author Dominik Moser
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuestionAnswerFrameController {

    private Logger LOG = LoggerFactory.getLogger(QuestionAnswerFrameController.class);

    private AnswerService answerService;

    @Autowired
    public QuestionAnswerFrameController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @FXML
    private void getTheAnswerAction() {
        LOG.debug("Get The Answer pressed");
        new AlertBuilder()
                .alertType(Alert.AlertType.INFORMATION)
                .title("SEPM - SS16 - Spring/Maven/FXML Sample")
                .headerText("The answer to life the universe and everything")
                .contentText(answerService.getTheAnswer())
                .modality(Modality.APPLICATION_MODAL)
                .build()
                .showAndWait();
    }

}