import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.SubjectServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleAnwerServiceTest {

    private SubjectService answerService;

    @Before
    public void before() {
        answerService = new SubjectServiceImpl();
    }

    @Test
    public void testIfTheSimpleAnswerServiceReturnsTheCorrectAnswer() {
        assertThat(answerService.getSubjects().size(), is(0));
    }

}
