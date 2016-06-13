package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.StatisticService;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;

/**
 * Created by Julian on 14.06.2016.
 */
@Service
class StatisticServiceImpl implements StatisticService {
    private static final Logger logger = LogManager.getLogger();
    @Autowired ExamService examService;
    @Autowired ExamDaoJdbc examDaoJdbc;

    @Override
    public double getCorrectFalseRatio(Topic topic) throws ServiceException {
        int countCorrect=0;
        int countFalse=0;

        List<Integer> exerciseExams;
        try {
            List<Exam> exams = examDaoJdbc.getExams();

            for(Exam e: exams)
            {
                exerciseExams  = examService.getAllExerciseExamsOfExam(e);

            }


        } catch (DaoException e) {
            logger.error("Unable to get questions for this topic ", e);
            throw new ServiceException(e.getMessage());
        }

        return 0.5;
    }
}
