package at.ac.tuwien.sepm.ss16.qse18.service;

import java.awt.image.BufferedImage;

/**
 * Implementors provide functionality to render latex code to an image.
 *
 * @author Hans-Joerg Schroedl
 */
public interface LatexRenderService {

    /**
     * Creates a {@link BufferedImage} from a latex string.
     *
     * @param latex The string containing latex syntax.
     * @return The rendered image.
     * @throws ServiceException
     */
    BufferedImage createImageFrom(String latex) throws ServiceException;

}
