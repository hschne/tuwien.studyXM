package at.ac.tuwien.sepm.ss16.qseXX.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * A simple service answering all your questions.
 *
 * @author Dominik Moser
 */
@Service
public class SimpleAnswerService implements AnswerService {

    private Logger LOG = LoggerFactory.getLogger(SimpleAnswerService.class);

    @Override
    public String getTheAnswer() {
        LOG.info("Calculating the answer");
        return "42";
    }

}
