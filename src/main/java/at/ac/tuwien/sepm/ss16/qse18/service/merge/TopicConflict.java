package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class TopicConflict {

    private final Logger logger = LogManager.getLogger();

    public void setQuestionConflicts(List<QuestionConflict> questionConflicts) {
        this.questionConflicts = questionConflicts;
    }

    private List<QuestionConflict> questionConflicts;
    private QuestionConflictDetection questionConflictDetection;
    private Topic existingTopic;
    private Topic importedTopic;

    public TopicConflict(Topic existingTopic, Topic importedTopic) {
        this.existingTopic = existingTopic;
        this.importedTopic = importedTopic;
    }

    public List<QuestionConflict> getQuestionConflicts() {
        return questionConflicts;
    }

    @Autowired
    public void setQuestionConflictDetection(QuestionConflictDetection questionConflictDetection) {
        this.questionConflictDetection = questionConflictDetection;
    }

    public void initialize(Topic existingTopic, Topic importedTopic) {
        this.existingTopic = existingTopic;
        this.importedTopic = importedTopic;
    }

    public List<QuestionConflict> getConflictingQuestions() throws ServiceException {
        List<Question> importedQuestions = new ArrayList<>();
        questionConflictDetection.initialize(existingTopic, importedQuestions);
        setQuestionConflicts(questionConflictDetection.getConflictingQuestions());
        return questionConflicts;
    }

    public List<Question> getNonConflictingImported() {
        List<Question> imported = new ArrayList<>();
        for (QuestionConflict questionConflict : questionConflicts) {
            imported.remove(questionConflict.getImportedQuestion());
        }
        return imported;
    }

    public void resolve() throws ServiceException {
        for (QuestionConflict questionConflict : questionConflicts) {
            questionConflict.resolve();
        }
    }

    public Topic getImportedTopic() {
        return importedTopic;
    }
}
