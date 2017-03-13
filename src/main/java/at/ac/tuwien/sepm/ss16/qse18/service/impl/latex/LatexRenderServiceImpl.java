package at.ac.tuwien.sepm.ss16.qse18.service.impl.latex;

import at.ac.tuwien.sepm.ss16.qse18.service.LatexRenderService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * Implementation of {@link LatexRenderService}. This implementation uses SnuggleTex and
 * JEuclid to create an Image from Latex code.
 *
 * @author Hans-Joerg Schroedl
 * @see <a href = http://tex.stackexchange.com/questions/41609/tex-rendering-in-a-java-application></a>
 */
@Service public class LatexRenderServiceImpl implements LatexRenderService {

    private SnuggleServiceImpl snuggleService;

    private JEuclidServiceImpl jEuclidService;

    @Autowired public void setSnuggleService(SnuggleServiceImpl snuggle) {
        this.snuggleService = snuggle;
    }

    @Autowired public void setJEuclidService(JEuclidServiceImpl jEuclidService) {
        this.jEuclidService = jEuclidService;
    }

    @Override public BufferedImage createImageFrom(String latexString) throws ServiceException {
        String xmlString = snuggleService.createMathMlFrom("$" + latexString + "$");
        return jEuclidService.createBufferedImageFrom(xmlString);
    }


}
