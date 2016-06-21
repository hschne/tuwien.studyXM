package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.service.impl.latex.JEuclidServiceImpl;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.latex.LatexRenderServiceImpl;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.latex.SnuggleServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.image.BufferedImage;
import java.nio.Buffer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class LatexRenderServiceImplTest {


    private LatexRenderServiceImpl latexRenderService;

    @Mock private JEuclidServiceImpl jEuclidServiceMock;

    @Mock private SnuggleServiceImpl snuggleServiceMock;

    @Before public void setUp(){

        latexRenderService = new LatexRenderServiceImpl();
        latexRenderService.setJEuclidService(jEuclidServiceMock);
        latexRenderService.setSnuggleService(snuggleServiceMock);
    }

    @Test public void test_createImageFrom_validString() throws Exception {
        String text = "Testing";
        BufferedImage expected = new BufferedImage(1,1,1);
        when(snuggleServiceMock.createMathMlFrom(anyString())).thenReturn("MathMl");
        when(jEuclidServiceMock.createBufferedImageFrom(anyString())).thenReturn(expected);

        BufferedImage result =latexRenderService.createImageFrom(text);

        verify(snuggleServiceMock).createMathMlFrom("$"+text+"$");
        verify(jEuclidServiceMock).createBufferedImageFrom("MathMl");
        assertEquals(expected, result);

    }



}
