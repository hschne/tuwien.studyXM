package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class QuestionConflictDetection {

    private QuestionTopicDao questionTopicDao;
    private Topic existing;
    private List<Question> importedQuestions;

    @Autowired public void setQuestionTopicDao(QuestionTopicDao questionTopicDao) {
        this.questionTopicDao = questionTopicDao;
    }

    public void initialize(Topic existing, List<Question> importedQuestions) {
        this.existing = existing;
        this.importedQuestions = importedQuestions;
    }

    public List<Duplicate<Question>> getConflictingQuestions() {
        List<Duplicate<Question>> result = new ArrayList<>();
        return result;
    }
}
