package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ImportService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service public class ImportServiceImpl implements ImportService {

    private static final Logger logger = LogManager.getLogger();
    private static final String OUTPUT_PATH = "src/main/resources/import";

    @Override public void importSubject(File zippedFile) throws ServiceException {
        logger.debug("Importing subject from file " + zippedFile);

        unzipFile(zippedFile.getAbsolutePath(), zippedFile.getName());
        parseSubject();
        //parseTopics();
        //parseQuestions();
    }

    private void unzipFile(String inputPath, String fileName) throws ServiceException {
        File destDir = new File(OUTPUT_PATH + File.separator + fileName.substring(0, fileName.length()-4));

        if (!destDir.exists()) {
            destDir.mkdir();
        }

        ZipInputStream zipIn = null;
        ZipEntry zipEntry = null;

        try {
            zipIn = new ZipInputStream(new FileInputStream(inputPath));
            zipEntry = zipIn.getNextEntry();

            while (zipEntry != null) {
                String filePath = destDir + File.separator + zipEntry.getName();

                extractFile(zipIn, filePath);
                zipIn.closeEntry();
                zipEntry = zipIn.getNextEntry();
            }
        } catch (FileNotFoundException e) {
            logger.error("Zip file does not exist", e);
            throw new ServiceException("Zip file does not exist", e);
        } catch (IOException e) {
            logger.error("Could not get zip entry", e);
            throw new ServiceException("Could not get zip entry", e);
        } finally {
            if (zipIn != null) {
                try {
                    zipIn.close();
                } catch (IOException e) {
                    logger.error("Could not close zipInputStream", e);
                    throw new ServiceException("Could not close zipInputStream", e);
                }
            }
        }
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[1024];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    private Subject parseSubject() {
        Subject result = null;



        return result;
    }

    private List<Topic> parseTopics() {
        return null;
    }

    private List<Question> parseQuestions() {
        return null;
    }
}
