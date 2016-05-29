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
     * @param q Question that is validated
     * @return true if quesiton is valid
     */
    public static boolean validate(Question q) {
        return q != null && q.getQuestion() != null && !q.getQuestion().trim().isEmpty()
            && q.getQuestion().length() <= 2000;
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

    public static void validate(Resource resource) throws DtoValidationException {
        if (resource == null) {
            throw new DtoValidationException("Resource must not be null");
        }
        if (resource.getType() == null) {
            throw new DtoValidationException("Resource type must not be null");
        }
        validateFields(resource);
    }

    private static void validateFields(Resource resource) throws DtoValidationException {
        if (resource.getName().isEmpty()) {
            throw new DtoValidationException("Resource name must not be empty");
        }
        if (resource.getReference().isEmpty()) {
            throw new DtoValidationException("Resource reference must not be empty");
        }
        File file = new File(resource.getReference());
        if (!(file.exists() || file.isDirectory())) {
            throw new DtoValidationException("File referenced by resource does not exist");
        }
    }
}
