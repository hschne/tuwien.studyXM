package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExerciseExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.QuestionTopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.StatisticService;


/**
 * Implementation of the statistics service layer
 * @author Julian Strohmayer
 */

@Service
public class StatisticServiceImpl implements StatisticService {
    private static final Logger logger = LogManager.getLogger();
    private ExamService examService;
    private ExerciseExamService exerciseExamService;
    private ExamDaoJdbc examDaoJdbc;
    private ExerciseExamDaoJdbc exerciseExamDaoJdbc;
    private QuestionTopicDaoJdbc questionTopicDaoJdbc;
    private Map<Integer, Boolean> questions;
    private Topic topic;

    @Autowired
    StatisticServiceImpl(ExamService examService,
                         ExerciseExamService exerciseExamService,
                         ExamDaoJdbc examDaoJdbc,
                         ExerciseExamDaoJdbc exerciseExamDaoJdbc,
                         QuestionTopicDaoJdbc questionTopicDaoJdbc) {
        this.examService = examService;
        this.exerciseExamService = exerciseExamService;
        this.examDaoJdbc = examDaoJdbc;
        this.exerciseExamDaoJdbc = exerciseExamDaoJdbc;
        this.questionTopicDaoJdbc = questionTopicDaoJdbc;
    }

    @Override
    public double[] getCorrectFalseRatio(Topic topic) throws ServiceException {

        logger.debug("calculating correct/false ratio");
        this.topic = topic;
        try {
            getTopicQuestions();
            checkForCorrectQuestions();

        } catch (DaoException e) {
            logger.error("Unable to get questions for this topic ", e);
            throw new ServiceException(e);
        } catch (ServiceException e) {
            logger.error("Unable to get correct questions ", e);
            throw new ServiceException("Unable to get correct questions " + e);
        }

        int correct = 0;
        for (Map.Entry<Integer, Boolean> entry : questions.entrySet()) {
            if (entry.getValue()) {
                correct++;
            }
        }
        return new double[]{correct / questions.size(), correct, questions.size()};
    }

    /**
     * Fills the question Map with all questions in the topic
     */
    private void getTopicQuestions() throws DaoException {
        questions = new HashMap<>();
        for (Question q : questionTopicDaoJdbc.getQuestionToTopic(topic)) {
            questions.put(q.getQuestionId(), false);
        }
    }

    /**
     * Checks every question in the topic for correctness.
     */
    private void checkForCorrectQuestions() throws DaoException, ServiceException {
        for (Exam e : examDaoJdbc.getExams()) {
            for (Integer i : examService.getAllExerciseExamsOfExam(e)) {

                ExerciseExam ex = exerciseExamDaoJdbc.getExam(i);
                for (Integer id : exerciseExamService.getAnsweredQuestionsOfExam(ex.getExamid())) {
                    if (questions.containsKey(id)) {
                        questions.put(id, true);
                    }
                }
            }
        }
    }

}
