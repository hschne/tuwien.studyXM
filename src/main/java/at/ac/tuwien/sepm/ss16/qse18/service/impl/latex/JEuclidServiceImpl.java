package at.ac.tuwien.sepm.ss16.qse18.service.impl.latex;

import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.converter.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Service provides access to the JEuclid library. The library is used to XHtml documents with
 * MathML tags to images.
 *
 * @author Hans-Joerg Schroedl
 */
public class JEuclidServiceImpl {

    private Logger logger = LogManager.getLogger();

    private Converter converter;

    /**
     * Default constructor.
     * <p>
     * Initializes {@link Converter}
     */
    public JEuclidServiceImpl() {
        converter = Converter.getInstance();
    }


    /**
     * This method takes a XHtml String and converts it to an image.
     *
     * @param xmlString The XHtml String. Must contain MathML tags.
     * @return The rendered image.
     * @throws ServiceException Thrown if an error occurs during rendering.
     */
    public BufferedImage createBufferedImageFrom(String xmlString) throws ServiceException {
        try {
            Document document = MathMLParserSupport.parseString(xmlString);
            return converter.render(document, LayoutContextImpl.getDefaultLayoutContext());
        } catch (SAXException | ParserConfigurationException e) {
            logger.error(e);
            throw new ServiceException(
                "Syntax errors while converting to latex image. Please view logs for details.", e);
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException("Could not convert xml to image.", e);
        }

    }

}
