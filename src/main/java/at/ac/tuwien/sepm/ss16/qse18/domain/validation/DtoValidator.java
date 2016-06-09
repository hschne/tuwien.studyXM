package at.ac.tuwien.sepm.ss16.qse18.domain.validation;

import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Philipp Ganiu
 */
public class DtoValidator {

    /**
     * Checks whether the subject is valid or not.
     * @param subject the subject that should be validated
     * @throws ServiceException
     *
     * */
    public static void validateSubject(Subject subject) throws DtoValidatorException{
        if(subject.getName().trim().isEmpty()) {
            throw new DtoValidatorException("Subject name must not be empty.");
        }
        if (subject.getName().length() > 80) {
            throw new DtoValidatorException("Subject name must not be longer than 80 characters");
        }
        if(subject.getSemester().trim().isEmpty()){
            throw new DtoValidatorException("Subject semester must not be empty.");
        }
        if (subject.getSemester().length() > 50) {
            throw new DtoValidatorException("Subject semester must not be longer than 50 characters");
        }
        if(subject.getAuthor().trim().isEmpty()){
            throw new DtoValidatorException("Author name must not be empty.");
        }
        if (subject.getAuthor().length() > 80) {
            throw new DtoValidatorException("Subject author must not be longer than 80 characters");
        }
        if(subject.getEcts() <= 0) {
            throw new DtoValidatorException("ECTS must be greater than 0.");
        }
    }

    /**
     * Checks if a question is valid. This means the question is not empty or null and shorter than 2000 chars.
     *
     * @param question Question that is validated
     * @exception DtoValidatorException
     */
    public static void validate(Question question) throws DtoValidatorException {
        if (question == null) {
            throw new DtoValidatorException("Question must not be null");
        }
        if (question.getQuestion() == null) {
            throw new DtoValidatorException("Question text must not be null");
        }
        if (question.getQuestion().length() > 2000) {
            throw new DtoValidatorException(
                "Question text must not be longer than 2000 characters");
        }
        if (question.getQuestion().trim().isEmpty()) {
            throw new DtoValidatorException(
                "Question text must not be empty (whitespaces are ignored)");
        }
        if (question.getQuestionTime() < 1) {
            throw new DtoValidatorException("Question time must be greater than 0");
        }
        if (question.getType() == null) {
            throw new DtoValidatorException("Question must have an questiontype");
        }
    }

    /**
     * Checks if an exerciseExam is valid. This means the exerciseExam is not null, the author is not empty and shorter than 80 chars.
     *
     * @param exerciseExam exerciseExam that is validated
     */
    public static void validate(ExerciseExam exerciseExam) throws DtoValidatorException {
        if (exerciseExam == null) {
            throw new DtoValidatorException("ExerciseExam must not be null.");
        }
        validateAuthor(exerciseExam);
        if (exerciseExam.getCreated() == null) {
            throw new DtoValidatorException("ExerciseExam timestamp must not be null.");
        }
        long examTime = exerciseExam.getExamTime();
        if (examTime <= 0) {
            throw new DtoValidatorException("ExamTime must at least be 1.");
        }
    }

    private static void validateAuthor(ExerciseExam exerciseExam) throws DtoValidatorException {
        if (exerciseExam.getAuthor() == null) {
            throw new DtoValidatorException("ExerciseExam author must not be null");
        }
        if (exerciseExam.getAuthor().isEmpty() || exerciseExam.getAuthor().trim().isEmpty()) {
            throw new DtoValidatorException(
                "ExerciseExam author must not be empty (leading or trailing whitespaces are ignored)");
        }
        if (exerciseExam.getAuthor().length() > 80) {
            throw new DtoValidatorException("ExerciseExam author must not be longer than 80 characters");
        }
    }

    public static void validate(List<Answer> answers) throws DtoValidatorException {
        if(answers == null){
            throw new DtoValidatorException("Answers must not be null");
        }
        if(answers.isEmpty()){
            throw new DtoValidatorException("There must be at least one answer.");
        }
        int correctAnswers = answers.stream().filter(Answer::isCorrect).collect(Collectors.toList()).size();
        if(correctAnswers < 1){
            throw new DtoValidatorException("There must be at least one correct answer.");
        }
    }

    public static void validate(Resource resource) throws DtoValidatorException {
        if (resource == null) {
            throw new DtoValidatorException("Resource must not be null");
        }
        if (resource.getType() == null) {
            throw new DtoValidatorException("Resource type must not be null");
        }
        validateFields(resource);
    }

    private static void validateFields(Resource resource) throws DtoValidatorException {
        if (resource.getName().isEmpty()|| resource.getName().trim().isEmpty()) {
            throw new DtoValidatorException(
                "Resource name must not be empty. (leading or trailing whitespaces are ignored)");
        }
        if(resource.getName().trim().length() > 200){
            throw new DtoValidatorException("Resource name must not be greater than 200.");
        }
        if (resource.getReference().isEmpty()) {
            throw new DtoValidatorException(
                "Resource reference must not be empty. Select a file to reference.");
        }

        File file = new File(resource.getReference());
        if (!file.exists() || file.isDirectory()) {
            throw new DtoValidatorException(
                "File referenced by resource does not exist. Make sure that the file selected is available.");
        }
    }
}
