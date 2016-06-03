package at.ac.tuwien.sepm.ss16.qse18.domain.validation;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Philipp Ganiu
 */
public class DtoValidator {

    /**
     * Checks if a question is valid. This means the question is not empty or null and shorter than 2000 chars.
     *
     * @param question Question that is validated
     * @exception DtoValidatorException
     */
    public static void validate(Question question) throws DtoValidatorException {
        if(question == null){
            throw new DtoValidatorException("Question must not be null.");
        }
        String questionText = question.getQuestion();
        if(questionText == null || questionText.trim().isEmpty()){
            throw new DtoValidatorException("Question text must not be null or empty.");
        }
        if(questionText.length() > 2000){
            throw new DtoValidatorException("Question text mut not be longer than 2000 characters.");
        }
    }

    /**
     * Checks if an exam is valid. This means the exam is not null, the author is not empty and shorter than 80 chars.
     *
     * @param e exam that is validated
     * @return true if exam is valid
     */
    public static boolean validate(Exam e) {
        return e != null && e.getAuthor().length() <= 80 && !e.getAuthor().trim().isEmpty();
    }

    public static void validate(List<Answer> answers) throws DtoValidatorException {
        if(answers == null){
            throw new DtoValidatorException("Answers must not be null");
        }
        if(answers.isEmpty()){
            throw new DtoValidatorException("There must be at least one answer");
        }


        int correctAnswers = answers.stream().filter(Answer::isCorrect).collect(Collectors.toList()).size();
        if(correctAnswers < 1){
            throw new DtoValidatorException("There must be at least one correct answer");
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
            throw new DtoValidatorException("Resource name must not be empty. Please enter a resource name.");
        }
        if (resource.getReference().isEmpty()) {
            throw new DtoValidatorException("Resource reference must not be empty. Select a file to reference.");
        }
        File file = new File(resource.getReference());
        if (!(file.exists() || file.isDirectory())) {
            throw new DtoValidatorException("File referenced by resource does not exist. Make sure that the file selected is available.");
        }
    }
}
