package at.ac.tuwien.sepm.ss16.qse18.domain.validation;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;

import java.io.File;

/**
 * @author Philipp Ganiu
 */
public class DtoValidator {

    /**
     * Checks if a question is valid. This means the question is not empty or null and shorter than 2000 chars.
     *
     * @param question Question that is validated
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
     * Checks if an exam is valid. This means the exam is not null, the author is not empty and shorter than 80 chars.
     *
     * @param exam exam that is validated
     */
    public static void validate(Exam exam) throws DtoValidatorException {
        if (exam == null) {
            throw new DtoValidatorException("Exam must not be null");
        }
        if (exam.getAuthor() == null) {
            throw new DtoValidatorException("Exam author must not be null");
        }
        if (exam.getAuthor().isEmpty() || exam.getAuthor().trim().isEmpty()) {
            throw new DtoValidatorException(
                "Exam author must not be empty (leading or trailing whitespaces are ignored)");
        }
        if (exam.getAuthor().length() > 80) {
            throw new DtoValidatorException("Exam author must not be longer than 80 characters");
        }
        if (exam.getCreated() == null) {
            throw new DtoValidatorException("Exam timestamp must not be null");
        }
        if (exam.getExamTime() < 1) {
            throw new DtoValidatorException("Exam time must at least be 1");
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
        if (resource.getName().isEmpty()) {
            throw new DtoValidatorException(
                "Resource name must not be empty. Please enter a resource name.");
        }
        if (resource.getReference().isEmpty()) {
            throw new DtoValidatorException(
                "Resource reference must not be empty. Select a file to reference.");
        }
        File file = new File(resource.getReference());
        if (!(file.exists() || file.isDirectory())) {
            throw new DtoValidatorException(
                "File referenced by resource does not exist. Make sure that the file selected is available.");
        }
    }
}
