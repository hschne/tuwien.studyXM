package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ImportService;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class QuestionMerge {

    private QuestionService questionService;

    private ImportService importService;

    private Logger logger = LogManager.getLogger();

    @Autowired public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Autowired public void setImportService(ImportService importService) {
        this.importService = importService;
    }

    public void merge(QuestionConflict questionConflict, Topic existingTopic)
        throws ServiceException {
        ConflictResolution resolution = questionConflict.getResolution();
        if (resolution == null) {
            String message = "No conflict resolution selected. Conflict has not been resolved";
            logger.error(message);
            throw new ServiceException(message);
        }
        if (resolution == ConflictResolution.EXISTING
            || resolution == ConflictResolution.DUPLICATE) {
            logger.debug("Keeping existing question");
            return;
        }
        if (resolution == ConflictResolution.IMPORTED) {
            questionService.deleteQuestion(questionConflict.getExistingQuestion());
            importService.importQuestion(questionConflict.getImportedQuestion(), existingTopic);
        }
    }


}
