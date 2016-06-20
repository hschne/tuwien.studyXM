package at.ac.tuwien.sepm.ss16.qse18.gui.question;

import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Philipp Ganiu
 */
@Component public class CreateNotecardQuestionController extends CreateImageQuestionController {
    @Autowired public CreateNotecardQuestionController(QuestionService questionService,
        ResourceQuestionService resourceQuestionService) {
        super(questionService, resourceQuestionService);
    }
}
