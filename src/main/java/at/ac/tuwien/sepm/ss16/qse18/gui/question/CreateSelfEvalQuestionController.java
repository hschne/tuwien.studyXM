package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Philipp Ganiu
 */
public class CreateSelfEvalQuestionController extends CreateImageQuestionController {
    @FXML public TextField textFieldImagePath;

    @Autowired public CreateSelfEvalQuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService) {
        super(questionService, resourceQuestionService);
    }
}
