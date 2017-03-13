package at.ac.tuwien.sepm.ss16.qse18;

import at.ac.tuwien.sepm.ss16.qse18.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Dummy EntityFactory
 * contains methods that create test objects of the domain entities
 *
 * @author Hans-Joerg Schroedl
 */
public class DummyEntityFactory {

    public static Subject createDummySubject() {
        Subject s = new Subject();
        s.setSubjectId(1);
        s.setName("TESTING");
        s.setEcts(1.0f);
        s.setSemester("WS10");
        s.setAuthor("Author");
        return s;
    }

    public static List<Subject> createDummySubjects(){
        List<Subject> subjects = new ArrayList<>();
        subjects.add(createDummySubject());
        return subjects;
    }

    public static Topic createDummyTopic() {
        Topic topic = new Topic();
        topic.setTopic("Topic");
        topic.setTopicId(1);
        topic.setNumberOfQuestions("2");
        return topic;
    }

    public static List<Topic> createDummyTopics() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic());
        return topics;
    }

    public static Question createDummyQuestion(){
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestion("Question");
        question.setType(QuestionType.MULTIPLECHOICE);
        question.setQuestionTime(1);
        question.setTag(Tag.EASY);
        return question;
    }

    public static List<Question> createDummyQuestions(){
        List<Question> questions = new ArrayList<>();
        questions.add(createDummyQuestion());
        return questions;
    }

    public static Answer createDummyAnswer(){
        Answer answer = new Answer();
        answer.setAnswer("Answer");
        answer.setAnswerId(1);
        answer.setCorrect(false);
        answer.setQuestion(createDummyQuestion());
        answer.setType(QuestionType.MULTIPLECHOICE);
        return answer;
    }

    public static List<Answer> createDummyAnswers(){
        List<Answer> answers = new ArrayList<>();
        answers.add(createDummyAnswer());
        return answers;
    }

    public static List<ExerciseExam> createDummyExcerciseExams() {
        List<ExerciseExam> exerciseExams = new ArrayList<>();
        exerciseExams.add(new ExerciseExam());
        return exerciseExams;
    }

    public static Exam createDummyExam(){
        Exam exam = new Exam();
        return exam;
    }

    public static List<Exam> createDummyExams(){
        List<Exam> exams = new ArrayList<>();
        exams.add(createDummyExam());
        return exams;
    }

    public static Resource createDummyResource() {
        Resource resource = new Resource(1, ResourceType.NOTE,"name","reference");
        return resource;
    }

    public static List<Resource> createDummyResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(createDummyResource());
        return resources;
    }

    public static ExerciseExam createDummyExerciseExam() {
        ExerciseExam exerciseExam = new ExerciseExam();
        return exerciseExam;
    }

}
