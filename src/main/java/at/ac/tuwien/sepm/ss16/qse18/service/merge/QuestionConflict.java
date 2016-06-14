package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.merge.ConflictResolution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hans-Joerg Schroedl
 */
public class QuestionConflict {

    private static final Logger logger = LogManager.getLogger();

    private ConflictResolution resolution;

    private Question existingQuestion;

    private Question importedQuestion;

    public void setResolution(ConflictResolution resolution){
        this.resolution = resolution;
    }

    public QuestionConflict(Question existingQuestion, Question importedQuestion){
        this.existingQuestion = existingQuestion;
        this.importedQuestion = importedQuestion;
    }

    public void resolve() throws ServiceException {
        if(resolution == null){
            throw new ServiceException("Conflict has not been resolved.");
        }
        if(resolution == ConflictResolution.EXISTING){
            logger.debug("Keeping existing question");
            return;
        }
        if(resolution == ConflictResolution.IMPORTED){
            //Remove existing, write imported
        }
    }

    public Question getImportedQuestion() {
        return importedQuestion;
    }
}
