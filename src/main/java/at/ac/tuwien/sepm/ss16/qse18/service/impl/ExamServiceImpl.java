package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExamGenParams;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

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
    private SubjectTopicDao subjectTopicDao;
    private QuestionTopicDao questionTopicDao;
    private SubjectDao subjectDao;
    private ExamGenParams egp;


    @Autowired public ExamServiceImpl(ExamDao examDao, SubjectQuestionDao subjectQuestionDao,
        ExamQuestionDao examQuestionDao, QuestionDao questionDao, SubjectTopicDao subjectTopicDao,
        QuestionTopicDao questionTopicDao, SubjectDao subjectDao) {
        this.examDao = examDao;
        this.subjectQuestionDao = subjectQuestionDao;
        this.examQuestionDao = examQuestionDao;
        this.questionDao = questionDao;
        this.subjectTopicDao = subjectTopicDao;
        this.questionTopicDao = questionTopicDao;
        this.subjectDao = subjectDao;
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
        tryValidateExam(exam);

        if (examTime <= 0) {
            logger.error("ExamTime must at least be 1");
            throw new ServiceException("ExamTime must at least be 1");
        }

        try {
            exam.setExamTime(examTime);
            exam.setExamQuestions(getRightQuestions(exam, topic.getTopicId(), examTime));
            return this.examDao.create(exam, exam.getExamQuestions());
        } catch (DaoException e) {
            logger.error("Service Exception createExam {}", exam, e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Exam deleteExam(Exam exam) throws ServiceException {
        logger.debug("entering method deleteExam with parameters {}", exam);
        tryValidateExam(exam);

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

        this.egp = new ExamGenParams();

        try {
            this.egp.setNotAnsweredQuestionID(this.subjectQuestionDao.getAllQuestionsOfSubject(exam, topicID));
            egp.setQuestionBooleans(this.examQuestionDao.getAllQuestionBooleans(egp.getNotAnsweredQuestionID()));

            for (int e : egp.getNotAnsweredQuestionID()) {
                egp.getNotAnsweredQuestions().add(this.questionDao.getQuestion(e));
            }

            splitQuestions();

            selectQuestions(egp.getNotAnsweredQuestions(), examTime);
            selectQuestions(egp.getWrongAnsweredQuestions(), examTime);
            selectQuestions(egp.getRightAnsweredQuestions(), examTime);

        } catch (DaoException e) {
            logger.error("Service Exception getRightQuestions with parameters{}", exam, topicID,
                examTime, e);
            throw new ServiceException(e.getMessage());
        }

        if (egp.getExamQuestions().isEmpty()) {
            logger.error("Service Exception getRightQuestions with parameters{}", exam);
            throw new ServiceException(
                "Could not get Questions with subjectId " + exam.getSubjectID() + " and topicID "
                    + topicID + " or examTime " + examTime + " is too small");
        }

        if (egp.getQuestionTime() < examTime * 0.8) {
            logger.error("Service Exception getRightQuestions with parameters{}", exam);
            throw new ServiceException(
                "There aren't enough questions to cover the exam time " + examTime
                    + " ,please reduce the exam time");
        }

        return egp.getExamQuestions();
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

    public void splitQuestions(){
        logger.debug("entering splitQuestions()");
        List<Question> temp = new ArrayList<>();
        int counter = 0;

        for(Question q: egp.getNotAnsweredQuestions()){
            temp.add(q);
        }

        for (Map.Entry<Integer, Boolean> e : egp.getQuestionBooleans().entrySet()) {
            for (int i = 0; i < temp.size(); i++) {
                if (e.getKey() == temp.get(i).getQuestionId()) { // checks if the question has been answered already
                    if (e.getValue() == true) { // if the question has already been answered correctly
                        egp.getRightAnsweredQuestions().add(temp.get(counter));
                        egp.getNotAnsweredQuestions().remove(temp.get(counter));
                    } else { // if the question has already been answered incorrectly
                        egp.getWrongAnsweredQuestions().add(temp.get(counter));
                        egp.getNotAnsweredQuestions().remove(temp.get(counter));
                    }
                }
                counter++;
            }
            counter = 0;
        }
    }

    public void selectQuestions(List<Question> questionList, int examTime){
        logger.debug("entering selectQuestions with parameters {}", questionList, examTime);
        while(!questionList.isEmpty()){
            questionList.remove(chooseQuestions(questionList, examTime));
        }
    }

    public int chooseQuestions(List<Question> questionList, int examTime){
        logger.debug("entering chooseQuestions with parameters {}", questionList, examTime);
        int random = (int) (Math.random() * questionList.size());
        if ((this.egp.getQuestionTime() + questionList.get(random).getQuestionTime())
            <= examTime) { // if time of questions are still smaller or the same as examTime
            egp.getExamQuestions().add(questionList.get(random));
            egp.setQuestionTime(egp.getQuestionTime() + questionList.get(random).getQuestionTime());
        }

        return random;
    }

    private void tryValidateExam(Exam exam) throws ServiceException {
        try {
            validate(exam);
        } catch (DtoValidatorException e) {
            logger.error("Exam [" + exam + "] is invalid", e);
            throw new ServiceException("Exam [" + exam + "] is invalid: " + e);
        }
    }



    @Override public String[] gradeExam(Exam exam) throws ServiceException{
        logger.debug("entering gradeExam with parameters {}",exam);
        List <Integer> questionIDList = new ArrayList<>();
        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        String[] result = new String[3];

        tryValidateExam(exam);

        try{

            questionIDList = getAllQuestionsOfExam(exam.getExamid());
            questionBooleans = this.examQuestionDao.getAllQuestionBooleans(questionIDList);

            result = calculateResult(questionBooleans);


        }catch (DaoException e){
            logger.error("Dao Exception gradeExam with parameters {}", exam, e);
            throw new ServiceException(e.getMessage());
        }


        return result;
    }

    public String[] calculateResult(Map<Integer, Boolean> questionBooleans){
        logger.debug("entering calculateResult with parameters {}", questionBooleans);
        double incorrect = 0;
        double correct = 0;
        String[] result = new String[3];

        if(questionBooleans != null && questionBooleans.size() > 0){
            for(Map.Entry<Integer, Boolean> m: questionBooleans.entrySet()){
                if(m.getValue()){
                    correct++;
                }else{
                    incorrect++;
                }
            }
        }

        result[0] = correct + "";
        result[1] = incorrect + "";
        result[2] = getGrade(correct, incorrect);

        return result;
    }

    public String getGrade(double correct, double incorrect){
        logger.debug("entering getGrade with parameters {}", correct, incorrect);
        double per = correct/((correct+incorrect)/100);

        if(per >= 96){
            return "A";

        }else if( per >= 81){
            return "B";

        }else if(per >= 66){
            return "C";

        }else if(per >= 51){
            return "D";

        }else{
            return "F";
        }
    }

    @Override public Map<Topic, String[]> topicGrade(Exam exam) throws ServiceException{
        logger.debug("entering topicGrade with parameter {}", exam);
        List<Topic> topicList = new ArrayList<>();
        Map<Topic, List<Integer>> temp = new HashMap<>();
        Map<Topic, String[]> gradeMap = new HashMap<>();

        tryValidateExam(exam);

        try {
            topicList = this.subjectTopicDao.getTopicToSubject(this.subjectDao.getSubject(exam.getSubjectID()));

            for(Topic t: topicList){
                temp.put(t, turnToQuestionID(this.questionTopicDao.getQuestionToTopic(t)));
            }

            for(Map.Entry<Topic, List<Integer>> m: temp.entrySet()){
                gradeMap.put(m.getKey(),
                    calculateResult(this.examQuestionDao.getAllQuestionBooleans(m.getValue())));

            }


        }catch (DaoException e){
            logger.error("Dao Exception topicGrade with parameters {}", exam, e);
            throw new ServiceException(e.getMessage());
        }

        return gradeMap;

    }

    public List<Integer> turnToQuestionID(List<Question> questionList){
        logger.debug("entering turnToQuestionID with parameters {}", questionList);
        List<Integer> questionIDList = new ArrayList<>();
        for(Question q: questionList){
            questionIDList.add(q.getQuestionId());
        }

        return questionIDList;
    }
}
