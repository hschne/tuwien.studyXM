package at.ac.tuwien.sepm.ss16.qse18.gui;

import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
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
public class SubjectViewController {

    private Logger LOG = LoggerFactory.getLogger(SubjectViewController.class);

    private SubjectService subjectService;

    @Autowired
    public SubjectViewController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @FXML
    public void getTheAnswerAction() {
        LOG.debug("Get The Answer pressed");
        new AlertBuilder()
                .alertType(Alert.AlertType.INFORMATION)
                .title("SEPM - SS16 - Spring/Maven/FXML Sample")
                .headerText("Subject retrieved")
                .contentText(subjectService.getSubject(1).getName())
                .modality(Modality.APPLICATION_MODAL)
                .build()
                .showAndWait();
    }

}
