package at.ac.tuwien.sepm.util;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

/**
 * @author Philipp Ganiu
 */
public class DTOValidator {

    /**
    * Checks if a question is valid. This means the question is not empty or null and shorter than 2000 chars.
     *
     * @param q Question that is validated
     * @return true if quesiton is valid
    * */
    public static boolean validate(Question q){
        return q!= null && q.getQuestion() != null &&  !q.getQuestion().trim().isEmpty() && q.getQuestion().length() <= 2000;
    }

    /**
     * Checks if an exam is valid. This means the exam is not null, the author is not empty and shorter than 80 chars.
     *
     * @param e exam that is validated
     * @return true if exam is valid
     * */
    public static boolean validate(Exam e){
        return e != null && e.getAuthor().length() <= 80 && !e.getAuthor().trim().isEmpty()
            && e.getExamQuestions().size() > 0;
    }
}
