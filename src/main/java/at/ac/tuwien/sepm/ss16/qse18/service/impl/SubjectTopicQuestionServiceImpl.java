package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectTopicQuestionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Ganiu
 */
@Service
public class SubjectTopicQuestionServiceImpl implements SubjectTopicQuestionService {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private SubjectTopicDao subjectTopicDao;
    private QuestionTopicDao questionTopicDao;

    @Autowired
    public SubjectTopicQuestionServiceImpl(SubjectTopicDao subjectTopicDao,QuestionTopicDao questionTopicDao) {
        this.subjectTopicDao = subjectTopicDao;
        this.questionTopicDao = questionTopicDao;
    }
    @Override
    public List<Topic> getTopicToSubjectWithNumberOfQuestions(Subject subject) throws ServiceException {
        try{
            List<Topic> topics = new ArrayList<>();
            for(Topic t : subjectTopicDao.getTopicToSubject(subject)){
                List<Question> questions = questionTopicDao.getQuestionToTopic(t);
                t.setNumberOfQuestions("questions: "+ questions.size());
                topics.add(t);
            }
            return topics;
        }
        catch (DaoException e){
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }
}
