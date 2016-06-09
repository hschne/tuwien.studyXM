package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExamGenParams;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
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
 * Class ExerciseExamServiceImpl
 * concrete implementatioin of ExerciseExamService
 *
 * @author Zhang Haixiang
 */
@Service public class ExerciseExamServiceImpl implements ExerciseExamService {
    private static final Logger logger = LogManager.getLogger();
    private ExerciseExamDao exerciseExamDao;
    private SubjectQuestionDao subjectQuestionDao;
    private ExerciseExamQuestionDao exerciseExamQuestionDao;
    private QuestionDao questionDao;
    private SubjectTopicDao subjectTopicDao;
    private QuestionTopicDao questionTopicDao;
    private SubjectDao subjectDao;
    private ExerciseExamGenParams egp;


    @Autowired public ExerciseExamServiceImpl(ExerciseExamDao exerciseExamDao, SubjectQuestionDao subjectQuestionDao,
        ExerciseExamQuestionDao exerciseExamQuestionDao, QuestionDao questionDao, SubjectTopicDao subjectTopicDao,
        QuestionTopicDao questionTopicDao, SubjectDao subjectDao) {
        this.exerciseExamDao = exerciseExamDao;
        this.subjectQuestionDao = subjectQuestionDao;
        this.exerciseExamQuestionDao = exerciseExamQuestionDao;
        this.questionDao = questionDao;
        this.subjectTopicDao = subjectTopicDao;
        this.questionTopicDao = questionTopicDao;
        this.subjectDao = subjectDao;
    }


    @Override public ExerciseExam getExam(int examID) throws ServiceException {
        logger.debug("entering method getExam with parameters {}", examID);
        if (examID <= 0) {
            logger.error("Service Exception getExam {}", examID);
            throw new ServiceException("Invalid ExerciseExam ID, please check your input");
        }

        try {
            return this.exerciseExamDao.getExam(examID);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public List<ExerciseExam> getExams() throws ServiceException {
        logger.debug("entering method getExams()");
        try {
            return this.exerciseExamDao.getExams();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public ExerciseExam createExam(ExerciseExam exerciseExam, Topic topic, int examTime)
        throws ServiceException {
        logger.debug("entering method createExam with parameters {}", exerciseExam);

        tryValidateExam(exerciseExam);

        if (examTime <= 0) {
            logger.error("ExamTime must at least be 1");
            throw new ServiceException("ExamTime must at least be 1");
        }

        try {
            exerciseExam.setExamTime(examTime);
            exerciseExam
                .setExamQuestions(getRightQuestions(exerciseExam, topic.getTopicId(), examTime));
            return this.exerciseExamDao.create(exerciseExam, exerciseExam.getExamQuestions());
        } catch (DaoException e) {
            logger.error("Service Exception createExam {}", exerciseExam, e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public ExerciseExam deleteExam(ExerciseExam exerciseExam) throws ServiceException {
        logger.debug("entering method deleteExam with parameters {}", exerciseExam);

        tryValidateExam(exerciseExam);

        try {
            return this.exerciseExamDao.delete(exerciseExam);
        } catch (DaoException e) {
            logger.error("Service Exception deleteExam {}", exerciseExam, e);
            throw new ServiceException(e.getMessage());
        }
    }


    /**
     * getRightQuestions
     * chooses the right questions for the exam and saves it into a list
     * @param exerciseExam for which the questions are chosen
     * @param topicID topic from which the questions are chosen
     * @param examTime duration of the exam
     * @return returns a List that contains the questions of the given exam
     * @throws ServiceException
     *
     * */
    public List<Question> getRightQuestions(ExerciseExam exerciseExam, int topicID, int examTime)
        throws ServiceException {
        logger
            .debug("entering method getRigthQuestions with parameters {}", exerciseExam, topicID, examTime);
        if (exerciseExam == null || topicID <= 0 || examTime <= 0) {
            logger.error("Service Exception getRightQuestions {}", exerciseExam, topicID, examTime);
            throw new ServiceException("Invalid values, please check your input");
        }

        this.egp = new ExerciseExamGenParams();

        try {
            this.egp.setNotAnsweredQuestionID(this.subjectQuestionDao.getAllQuestionsOfSubject(exerciseExam, topicID));
            egp.setQuestionBooleans(this.exerciseExamQuestionDao.getAllQuestionBooleans(egp.getNotAnsweredQuestionID()));

            for (int e : egp.getNotAnsweredQuestionID()) {
                egp.getNotAnsweredQuestions().add(this.questionDao.getQuestion(e));
            }

            splitQuestions();

            selectQuestions(egp.getNotAnsweredQuestions(), examTime);
            selectQuestions(egp.getWrongAnsweredQuestions(), examTime);
            selectQuestions(egp.getRightAnsweredQuestions(), examTime);

        } catch (DaoException e) {
            logger.error("Service Exception getRightQuestions with parameters{}", exerciseExam, topicID,
                examTime, e);
            throw new ServiceException(e.getMessage());
        }

        if (egp.getExamQuestions().isEmpty()) {
            logger.error("Service Exception getRightQuestions with parameters{}", exerciseExam);
            throw new ServiceException(
                "Could not get Questions with subjectId " + exerciseExam.getSubjectID() + " and topicID "
                    + topicID + " or examTime " + examTime + " is too small");
        }

        if (egp.getQuestionTime() < examTime * 0.8) {
            logger.error("Service Exception getRightQuestions with parameters{}", exerciseExam);
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
            return this.exerciseExamQuestionDao.getAllQuestionsOfExam(examID);
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

    private void tryValidateExam(ExerciseExam exerciseExam) throws ServiceException {
        try {
            validate(exerciseExam);
        } catch (DtoValidatorException e) {
            logger.error("Exam [" + exerciseExam + "] is invalid", e);
            throw new ServiceException("Exam [" + exerciseExam + "] is invalid: " + e);
        }
    }



    @Override public String[] gradeExam(ExerciseExam exerciseExam) throws ServiceException{
        logger.debug("entering gradeExam with parameters {}",exerciseExam);
        List <Integer> questionIDList = new ArrayList<>();
        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        String[] result = new String[3];

        tryValidateExam(exerciseExam);

        try{

            questionIDList = getAllQuestionsOfExam(exerciseExam.getExamid());
            questionBooleans = this.exerciseExamQuestionDao.getAllQuestionBooleans(questionIDList);

            result = calculateResult(questionBooleans);


        }catch (DaoException e){
            logger.error("Dao Exception gradeExam with parameters {}", exerciseExam, e);
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

    @Override public Map<Topic, String[]> topicGrade(ExerciseExam exerciseExam) throws ServiceException{
        logger.debug("entering topicGrade with parameter {}", exerciseExam);
        List<Topic> topicList = new ArrayList<>();
        Map<Topic, List<Integer>> temp = new HashMap<>();
        Map<Topic, String[]> gradeMap = new HashMap<>();

        tryValidateExam(exerciseExam);

        try {
            topicList = this.subjectTopicDao.getTopicToSubject(this.subjectDao.getSubject(exerciseExam.getSubjectID()));

            for(Topic t: topicList){
                temp.put(t, turnToQuestionID(this.questionTopicDao.getQuestionToTopic(t)));
            }

            for(Map.Entry<Topic, List<Integer>> m: temp.entrySet()){
                gradeMap.put(m.getKey(),
                    calculateResult(this.exerciseExamQuestionDao.getAllQuestionBooleans(m.getValue())));

            }


        }catch (DaoException e){
            logger.error("Dao Exception topicGrade with parameters {}", exerciseExam, e);
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

    @Override public List<Integer> getAnsweredQuestionsOfExam(int examID) throws ServiceException {
        logger.debug("entering getAnsweredQuestionsOfExam");

        if (examID <= 0) {
            logger.error("Service Exception getAnsweredQuestionsOfExam with parameters {}", examID);
            throw new ServiceException("Invalid examID, please check your input");
        }

        List<Integer> answeredQuestions = new ArrayList<>();

        try {
            answeredQuestions.addAll(this.exerciseExamQuestionDao.getAnsweredQuestionsPerExam(examID));
        } catch (DaoException e) {
            logger.error("Service Exception getAnsweredQuestionsOfExam with parameters", e);
            throw new ServiceException("Could not get answered questions of exam", e);
        }
        return answeredQuestions;
    }


    @Override public void update(int examid, int questionid, boolean questionPassed,
        boolean alreadyAnswered) throws ServiceException {
        logger.debug("Entering method update with parameters {}",examid,questionid,questionPassed,alreadyAnswered);
        try{
            this.exerciseExamQuestionDao.update(examid,questionid,questionPassed,alreadyAnswered);
        }
        catch (DaoException e){
            logger.error(e.getMessage(),e);
            throw new ServiceException(e.getMessage(),e);
        }
    }


}

