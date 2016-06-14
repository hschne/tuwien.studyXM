package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
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
    private QuestionConflictDetection questionConflictDetection;
    private QuestionTopicDao questionTopicDao;
    private Duplicate<Topic> duplicate;

    @Autowired public void setQuestionConflictDetection(QuestionConflictDetection questionConflictDetection) {
        this.questionConflictDetection = questionConflictDetection;
    }

    public void initialize(Duplicate<Topic> duplicate) {
        this.duplicate = duplicate;
    }

    public List<Duplicate<Question>> getConflictingQuestions() {
        Topic existing = duplicate.getExisting();
        Topic imported = duplicate.getImported();
        List<Question> importedQuestions = new ArrayList<>();
        questionConflictDetection.initialize(existing, importedQuestions);
        return questionConflictDetection.getConflictingQuestions();
    }
}
