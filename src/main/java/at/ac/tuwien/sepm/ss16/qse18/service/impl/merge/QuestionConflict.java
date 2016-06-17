package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Hans-Joerg Schroedl
 */

@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class QuestionConflict {

    private static final Logger logger = LogManager.getLogger();

    private ConflictResolution resolution;

    private Question existingQuestion;

    private ExportQuestion importedQuestion;

    public void setQuestions(Question existingQuestion, ExportQuestion importedQuestion) {
        this.existingQuestion = existingQuestion;
        this.importedQuestion = importedQuestion;
    }

    public void setResolution(ConflictResolution resolution) {
        this.resolution = resolution;
    }

    public void resolve() throws ServiceException {
        if (resolution == null) {
            String message = "No conflict resolution selected. Conflict has not been resolved";
            logger.error(message);
            throw new ServiceException(message);
        }
        if (resolution == ConflictResolution.EXISTING) {
            logger.debug("Keeping existing question");
            return;
        }
        if (resolution == ConflictResolution.IMPORTED) {
            //Remove existing, write imported
        }
    }

    public ExportQuestion getImportedQuestion() {
        return importedQuestion;
    }

    public Question getExistingQuestion() {
        return existingQuestion;
    }
}
