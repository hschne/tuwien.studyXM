package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.merge.ConflictResolution;

/**
 * @author Hans-Joerg Schroedl
 */
public class QuestionConflict {

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
            throw new ServiceException("Conflict has not been resolved!");
        }
    }

}
