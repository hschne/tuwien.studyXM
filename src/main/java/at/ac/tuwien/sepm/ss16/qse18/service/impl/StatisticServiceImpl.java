package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExerciseExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.QuestionTopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.StatisticService;


/**
 * Implementation of the statistics service layer
 *
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
    public double[] getCorrectQuestionsForTopic(Topic topic) throws ServiceException {
        if (topic == null) {
            logger.error("topic is null");
            throw new ServiceException("topic must not be null");
        }

        logger.debug("calculating number of correct questions for ID:" + topic.getTopicId());
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
        return questions.size() == 0 ? new double[]{0, 0, 0}
                : new double[]{correct / questions.size(), correct, questions.size()};
    }

    @Override
    public double[] gradeAllExamsForSubject(Subject subject) throws ServiceException {
        if (subject == null) {
            logger.error("subject is null");
            throw new ServiceException("subject must not be null");
        }
        logger.debug("calculating average exercise exam result for " + subject.getName());

        double gradeSum = 0;
        int examCount = 0;

        try {
            for (Exam e : examDaoJdbc.getExams()) {
                if (e.getSubject() == subject.getSubjectId()) {
                    for (Integer i : examService.getAllExerciseExamsOfExam(e)) {
                        examCount++;
                        ExerciseExam ex = exerciseExamDaoJdbc.getExam(i);
                        gradeSum += getIntValueOfGrade(exerciseExamService.gradeExam(ex)[2].charAt(0));
                    }
                }
            }
        } catch (DaoException e) {
            logger.debug("Unable to calculate grade for exercise exams ", e);
            throw new ServiceException("Unable to calculate grade for exercise exams ", e);
        }
        return examCount == 0 ? new double[]{-1, 0} : new double[]{gradeSum / examCount, examCount};
    }

    /**
     * Fills the question Map with all questions in the topic
     */
    private void getTopicQuestions() throws DaoException {
        questions = new TreeMap<>();
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
    /**
     * Returns the int value for a given grade
     *
     * @param c grade as character
     * @return 'A'=1,'B'=2,'C'=3,'D'=4,'F'=5
     */
    private int getIntValueOfGrade(char c) {
        switch (c) {
            case 'A':
                return 1;
            case 'B':
                return 2;
            case 'C':
                return 3;
            case 'D':
                return 4;
            default:
                return 5;
        }
    }

}
