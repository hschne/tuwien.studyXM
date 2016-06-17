package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportResource;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.SubjectConflict;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.SubjectConflictDetection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImportServiceImpl.class, File.class, FileInputStream.class, ZipInputStream.class,
    ZipEntry.class, BufferedOutputStream.class}) @PowerMockIgnore("javax.management.*")
public class ImportServiceImplTest extends ImportExportBaseTest {
    private ImportServiceImpl importService;
    private File zipFile = new File("any path");
    @Mock private SubjectDao mockSubjectDao;
    @Mock private TopicDao mockTopicDao;
    @Mock private AnswerDao mockAnswerDao;
    @Mock private QuestionDao mockQuestionDao;
    @Mock private ImportUtil mockImportUtil;
    @Mock private ResourceDao mockResourceDao;
    @Mock private SubjectConflictDetection mockSubjectConflictDetection;
    @Mock private SubjectConflict mockSubjectConflict;
    @Mock private FileInputStream mockFileInputStream;
    @Mock private ZipInputStream mockZipInputStream;
    @Mock private ZipEntry mockZipEntry;
    @Mock private BufferedOutputStream mockBufferedOutputStream;
    @Mock private ObjectInputStream mockObjectInputStream;

    @Mock private File mockFile;

    @Before public void setUp() throws Exception {
        importService = new ImportServiceImpl();

        createDummySubjectWithDummyRelations();

        importService.setSubjectDao(mockSubjectDao);
        importService.setAnswerDao(mockAnswerDao);
        importService.setQuestionDao(mockQuestionDao);
        importService.setResourceDao(mockResourceDao);
        importService.setImportUtil(mockImportUtil);
        importService.setTopicDao(mockTopicDao);
        importService.setSubjectConflictDetection(mockSubjectConflictDetection);
        importService.setSubjectConflict(mockSubjectConflict);
        importService.setImportUtil(mockImportUtil);
    }

    @Test(expected = ServiceException.class)
    public void test_importSubject_withInvalidZipFilePath_Fail() throws Exception {
        whenNew(File.class).withArguments(anyString()).thenThrow(FileNotFoundException.class);

        importService.importSubject(zipFile);
    }

    @Test(expected = ServiceException.class)
    public void test_importSubject_errorWhileGettingZipEntry() throws Exception {
        whenNew(FileInputStream.class).withParameterTypes(File.class).withArguments(any(File.class))
            .thenReturn(mockFileInputStream);
        whenNew(ZipInputStream.class).withParameterTypes(InputStream.class)
            .withArguments(any(InputStream.class)).thenReturn(mockZipInputStream);
        when(mockZipInputStream.getNextEntry()).thenThrow(IOException.class);

        importService.importSubject(zipFile);
    }

    @Test(expected = ServiceException.class)
    public void test_importSubject_errorWhileTryingToCloseZipInputStream() throws Exception {
        whenNew(FileInputStream.class).withParameterTypes(File.class).withArguments(any(File.class))
            .thenReturn(mockFileInputStream);
        whenNew(ZipInputStream.class).withParameterTypes(InputStream.class)
            .withArguments(any(InputStream.class)).thenReturn(mockZipInputStream);
        when(mockZipInputStream.getNextEntry()).thenReturn(mockZipEntry).thenReturn(null);
        when(mockZipEntry.isDirectory()).thenReturn(true);
        doThrow(IOException.class).when(mockZipInputStream).close();

        importService.importSubject(zipFile);
    }

    @Test(expected = ServiceException.class)
    public void test_importSubject_errorWhileFillingDatabase_Fail() throws Exception {
        setUpMocksForSuccessfulUnzipping();
        setUpMocksForSuccessfulDeserializing();

        setUpDaoMocks();

        when(mockResourceDao.createResource(any(Resource.class))).thenThrow(DaoException.class);

        importService.importSubject(zipFile);
    }

    @Test(expected = ServiceException.class)
    public void test_importSubject_errorWhileDeserializing_Fail() throws Exception {
        setUpMocksForSuccessfulUnzipping();

        whenNew(FileInputStream.class).withParameterTypes(String.class)
            .withArguments(any(String.class)).thenReturn(mockFileInputStream);
        whenNew(ObjectInputStream.class).withParameterTypes(InputStream.class)
            .withArguments(any(FileInputStream.class)).thenReturn(mockObjectInputStream);
        when(mockObjectInputStream.readObject()).thenThrow(IOException.class);

        importService.importSubject(zipFile);
    }

    @Test(expected = ServiceException.class)
    public void test_importSubject_errorWhileDeserializing_classNotFound_Fail() throws Exception {
        setUpMocksForSuccessfulUnzipping();

        whenNew(FileInputStream.class).withParameterTypes(String.class)
            .withArguments(any(String.class)).thenReturn(mockFileInputStream);
        whenNew(ObjectInputStream.class).withParameterTypes(InputStream.class)
            .withArguments(any(FileInputStream.class)).thenReturn(mockObjectInputStream);
        when(mockObjectInputStream.readObject()).thenThrow(ClassNotFoundException.class);

        importService.importSubject(zipFile);
    }

    @Test public void test_importSubject_withValidInputs() throws Exception {
        setUpMocksForSuccessfulUnzipping();
        setUpMocksForSuccessfulDeserializing();

        SubjectConflict testSubjectConflict = importService.importSubject(zipFile);
        assertEquals("There should be no conflicts", testSubjectConflict, null);
    }

    private void setUpMocksForSuccessfulUnzipping() throws Exception {
        whenNew(FileInputStream.class).withParameterTypes(File.class).withArguments(any(File.class))
            .thenReturn(mockFileInputStream);
        whenNew(ZipInputStream.class).withParameterTypes(InputStream.class)
            .withArguments(any(InputStream.class)).thenReturn(mockZipInputStream);
        when(mockZipInputStream.getNextEntry()).thenReturn(mockZipEntry).thenReturn(null);
        when(mockZipEntry.isDirectory()).thenReturn(false);
        when(mockZipInputStream.read(any())).thenReturn(0).thenReturn(-1);
        whenNew(BufferedOutputStream.class).withParameterTypes(OutputStream.class)
            .withArguments(any()).thenReturn(mockBufferedOutputStream);
    }

    private void setUpMocksForSuccessfulDeserializing() throws Exception {
        whenNew(FileInputStream.class).withParameterTypes(String.class)
            .withArguments(any(String.class)).thenReturn(mockFileInputStream);
        whenNew(ObjectInputStream.class).withParameterTypes(InputStream.class)
            .withArguments(any(FileInputStream.class)).thenReturn(mockObjectInputStream);
        when(mockObjectInputStream.readObject()).thenReturn(getExportSubjectFromTestSubject());

    }

    private ExportSubject getExportSubjectFromTestSubject() {
        ExportSubject result;
        List<ExportTopic> topics = new ArrayList<>();
        List<ExportQuestion> questions1 = new ArrayList<>();
        List<ExportQuestion> questions2 = new ArrayList<>();
        ExportResource resource = new ExportResource(testResource, null);

        questions1.add(new ExportQuestion(testQuestions1.get(0), resource, testAnswers1));
        questions2.add(new ExportQuestion(testQuestions2.get(0), resource, testAnswers2));

        topics.add(new ExportTopic(testTopics.get(0), questions1));
        topics.add(new ExportTopic(testTopics.get(1), questions2));

        result = new ExportSubject(testSubject, topics);
        result.setSubject(testSubject);
        return result;
    }

    private void setUpDaoMocks() throws DaoException {
        when(mockSubjectDao.createSubject(any(Subject.class))).thenReturn(testSubject);
        when(mockTopicDao.createTopic(any(Topic.class), any(Subject.class)))
            .thenReturn(testTopics.get(0)).thenReturn(testTopics.get(1));
        when(mockQuestionDao.createQuestion(any(Question.class), any(Topic.class)))
            .thenReturn(testQuestions1.get(0)).thenReturn(testQuestions2.get(0));
        when(mockAnswerDao.createAnswer(any(Answer.class))).thenReturn(testAnswers1.get(0))
            .thenReturn(testAnswers1.get(1)).thenReturn(testAnswers2.get(0))
            .thenReturn(testAnswers2.get(1));
    }

    @After public void tearDown() throws Exception {
        // nothing to tear down
    }

}
