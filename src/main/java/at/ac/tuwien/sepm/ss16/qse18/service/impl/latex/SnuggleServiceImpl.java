package at.ac.tuwien.sepm.ss16.qse18.service.impl.latex;

import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;

import java.io.IOException;

/**
 * Service provides access to the Snuggle library. The library is used to convert latex strings to xml documents.
 *
 * @see <a href=http://www2.ph.ed.ac.uk/snuggletex/documentation/overview-and-features.html></a>
 * @author Hans-Joerg Schroedl
 */
@Service public class SnuggleServiceImpl {

    private final Logger logger = LogManager.getLogger();

    private SnuggleSession session;

    /**
     * Default constructor.
     *
     * Initializes snuggle engine and session.
     */
    public SnuggleServiceImpl() {
        SnuggleEngine snuggleEngine = new SnuggleEngine();
        session = snuggleEngine.createSession();
    }

    /**
     * Converts latex strings to XHtml / MathML representation.
     *
     * @param latexString A string containing a math formula in latex syntax
     * @return XHtml document containing MathML sections
     * @throws ServiceException
     */
    public String createMathMlFrom(String latexString) throws ServiceException {
        SnuggleInput input = new SnuggleInput("$" + latexString + "$");
        try {
            session.parseInput(input);
            return session.buildXMLString();
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException("Could not convert text to mathml document.");
        }
    }

}
