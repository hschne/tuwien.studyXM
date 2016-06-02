package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class ExamServiceImpl
 * concrete implementatioin of ExamService
 *
 * @author Zhang Haixiang
 */
@Service public class ExamServiceImpl implements ExamService {
    private static final Logger logger = LogManager.getLogger();
    private ExamDao examDao;
    private SubjectQuestionDao subjectQuestionDao;
    private ExamQuestionDao examQuestionDao;
    private QuestionDao questionDao;


    @Autowired public ExamServiceImpl(ExamDao examDao, SubjectQuestionDao subjectQuestionDao,
        ExamQuestionDao examQuestionDao, QuestionDao questionDao) {
        this.examDao = examDao;
        this.subjectQuestionDao = subjectQuestionDao;
        this.examQuestionDao = examQuestionDao;
        this.questionDao = questionDao;
    }


    @Override public Exam getExam(int examID) throws ServiceException {
        logger.debug("entering method getExam with parameters {}", examID);
        if (examID <= 0) {
            logger.error("Service Exception getExam {}", examID);
            throw new ServiceException("Invalid Exam ID, please check your input");
        }

        try {
            return this.examDao.getExam(examID);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public List<Exam> getExams() throws ServiceException {
        logger.debug("entering method getExams()");
        try {
            return this.examDao.getExams();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public Exam createExam(Exam exam, Topic topic, int examTime) throws ServiceException {
        logger.debug("entering method createExam with parameters {}", exam);
        if (!DtoValidator.validate(exam)) {
            logger.error("Service Exception createExam {}", exam);
            throw new ServiceException("Invalid values, please check your input");
        }

        try {
            exam.setExamQuestions(getRightQuestions(exam, topic.getTopicId(), examTime));
            return this.examDao.create(exam, exam.getExamQuestions());
        } catch (DaoException e) {
            logger.error("Service Exception createExam {}", exam, e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Exam deleteExam(Exam exam) throws ServiceException {
        logger.debug("entering method deleteExam with parameters {}", exam);
        if (!DtoValidator.validate(exam)) {
            logger.error("Service Exception deleteExam {}", exam);
            throw new ServiceException("Invalid values, please check your input");
        }

        try {
            return this.examDao.delete(exam);
        } catch (DaoException e) {
            logger.error("Service Exception deleteExam {}", exam, e);
            throw new ServiceException(e.getMessage());
        }
    }


    /**
     * getRightQuestions
     * chooses the right questions for the exam and saves it into a list
     * @param exam exam for which the questions are chosen
     * @param topicID topic from which the questions are chosen
     * @param examTime duration of the exam
     * @return returns a List that contains the questions of the given exam
     * @throws ServiceException
     *
     * */
    public List<Question> getRightQuestions(Exam exam, int topicID, int examTime)
        throws ServiceException {
        logger
            .debug("entering method getRigthQuestions with parameters {}", exam, topicID, examTime);
        if (exam == null || topicID <= 0 || examTime <= 0) {
            logger.error("Service Exception getRightQuestions {}", exam, topicID, examTime);
            throw new ServiceException("Invalid values, please check your input");
        }

        int counter = 0;
        long questionTimeCounter = 0;
        int random;
        List<Question> examQuestions = new ArrayList<>();
        List<Integer> notAnsweredQuestionID; // conatins all questionID's
        List<Question> wrongAnsweredQuestions = new ArrayList<>(); // contains the quesitions which have been answered incorrectly
        List<Question> rightAnsweredQuestions = new ArrayList<>(); // contains the questions which have been answered correctly
        Map<Integer, Boolean> questionBooleans; // contains the booleans(answered correctly/incorrectly) of already answred questions
        List<Question> notAnsweredQuestions = new ArrayList<>(); // contains all questions to a certain topic and subject

        try {
            notAnsweredQuestionID = this.subjectQuestionDao.getAllQuestionsOfSubject(exam, topicID);
            questionBooleans = this.examQuestionDao.getAllQuestionBooleans(notAnsweredQuestionID);

            for (int e : notAnsweredQuestionID) {
                notAnsweredQuestions.add(this.questionDao.getQuestion(e));
            }

            List<Question> temp = new ArrayList<>();
            for (Question q : notAnsweredQuestions) {
                temp.add(q);
            }

            for (Map.Entry<Integer, Boolean> e : questionBooleans.entrySet()) {
                for (int i = 0; i < notAnsweredQuestions.size(); i++) {
                    if (e.getKey() == notAnsweredQuestions.get(i).getQuestionId()) { // checks if the question has been answered already
                        if (e.getValue() == true) { // if the question has already been answered correctly
                            rightAnsweredQuestions.add(temp.get(counter));
                            notAnsweredQuestions.remove(temp.get(counter));
                        } else { // if the question has already been answered incorrectly
                            wrongAnsweredQuestions.add(temp.get(counter));
                            notAnsweredQuestions.remove(temp.get(counter));
                        }
                        counter++;
                    }
                }
            }

            while (!wrongAnsweredQuestions.isEmpty() || !notAnsweredQuestions.isEmpty()
                || !rightAnsweredQuestions.isEmpty()) {

                if (!notAnsweredQuestions.isEmpty()) {
                    random = (int) (Math.random() * notAnsweredQuestions.size());
                    if ((questionTimeCounter + notAnsweredQuestions.get(random).getQuestionTime())
                        <= examTime) { // if time of questions are still smaller or the same as examTime
                        examQuestions.add(notAnsweredQuestions.get(random));
                        questionTimeCounter += notAnsweredQuestions.get(random).getQuestionTime();
                    }
                    notAnsweredQuestions.remove(random);
                }

                if (!wrongAnsweredQuestions.isEmpty() && notAnsweredQuestions.isEmpty()) {
                    random = (int) (Math.random() * wrongAnsweredQuestions.size());
                    if ((questionTimeCounter + wrongAnsweredQuestions.get(random).getQuestionTime())
                        <= examTime) { // if time of questions are still smaller or the same as examTime
                        examQuestions.add(wrongAnsweredQuestions.get(random));
                        questionTimeCounter += wrongAnsweredQuestions.get(random).getQuestionTime();
                    }

                    wrongAnsweredQuestions.remove(random);
                }


                if (!rightAnsweredQuestions.isEmpty() && notAnsweredQuestions.isEmpty()
                    && wrongAnsweredQuestions.isEmpty()) {
                    random = (int) (Math.random() * rightAnsweredQuestions.size());
                    if ((questionTimeCounter + rightAnsweredQuestions.get(random).getQuestionTime())
                        <= examTime) { // if time of questions are still smaller or the same as examTime
                        examQuestions.add(rightAnsweredQuestions.get(random));
                        questionTimeCounter += rightAnsweredQuestions.get(random).getQuestionTime();
                    }
                    rightAnsweredQuestions.remove(random);
                }
            }

        } catch (DaoException e) {
            logger.error("Service Exception getRightQuestions with parameters{}", exam, topicID,
                examTime, e);
            throw new ServiceException(e.getMessage());
        }

        if (examQuestions.isEmpty()) {
            logger.error("Service Exception getRightQuestions with parameters{}", exam);
            throw new ServiceException(
                "Could not get Questions with subjectId " + exam.getSubjectID() + " and topicID "
                    + topicID + " or examTime " + examTime + " is too small");
        }

        if (questionTimeCounter < examTime * 0.8) {
            logger.error("Service Exception getRightQuestions with parameters{}", exam);
            throw new ServiceException(
                "There aren't enough questions to cover the exam time " + examTime
                    + " ,please reduce the exam time");
        }

        return examQuestions;
    }

    @Override public List<Integer> getAllQuestionsOfExam(int examID) throws ServiceException {
        logger.debug("entering getAllQuestionsOfExam with parameters {}", examID);

        if (examID <= 0) {
            logger.error("Service Exception getAllQuestionsOfExam with parameters {}", examID);
            throw new ServiceException("Invalid examID, please check your input");
        }

        try {
            return this.examQuestionDao.getAllQuestionsOfExam(examID);
        } catch (DaoException e) {
            logger.error("Service Exception getAllQuestionsOfExam with parameters {}", examID, e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public void update(int examid, int questionid, boolean questionPassed,
        boolean alreadyAnswered) throws ServiceException {
        logger.debug("Entering method update with parameters {}",examid,questionid,questionPassed,alreadyAnswered);
        try{
            this.examQuestionDao.update(examid,questionid,questionPassed,alreadyAnswered);
        }
        catch (DaoException e){
            logger.error(e.getMessage(),e);
            throw new ServiceException(e.getMessage(),e);
        }
    }

}
