package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.ResourceType;
import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.NoSuchFileException;
import java.util.*;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class ResourceEditController
    extends BaseController {

    @FXML TextField resourceName;
    @FXML TextField filePath;

    @Autowired ResourceOverviewController overviewController;

    private File out;

    private File in;

    private List inputs;

    private QuestionType questionTypeOfResource;

    @FXML public void handleSelectFile() {
        FileChooser fileChooser = new FileChooser();
        String defaultPath = "src/main/resources/resources/";
        File defaultDirectory = new File(defaultPath);
        fileChooser.setInitialDirectory(defaultDirectory);

        fileChooser.setTitle("Add resource");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            logger.debug("A File was selected");
            out = new File(defaultPath + generateFileName(selectedFile.getName()));
            in = selectedFile;
            filePath.setText(defaultPath + selectedFile.getName());
        }
    }

    @FXML public void handleOk() {
        try {
            copyFile(in, out);
            Resource resource = createResourceFromFields();
            overviewController.addResource(new ObservableResource(resource));

            openRightWindowNext(new ObservableResource(resource));
        } catch (Exception e) {
            logger.error(e);
            showError(e);
        }

    }

    @FXML public void handleCancel() {
        openRightWindowNext(null);
    }

    private void openRightWindowNext(ObservableResource resource) {
        if (inputs != null) {
            if (resource != null) {
                inputs.add(resource);
            }

            switch (questionTypeOfResource) {
                case MULTIPLECHOICE:
                    mainFrameController.handleMultipleChoiceQuestion(null, inputs);
                    break;
                case OPENQUESTION:
                    mainFrameController.handleOpenQuestion(null, inputs);
                    break;
                case SINGLECHOICE:
                    mainFrameController.handleSingleChoiceQuestion(null, inputs);
                    break;
                case NOTECARD:
                    mainFrameController.handleCreateImageQuestion(null, inputs);
                    break;
            }
        } else {
            mainFrameController.handleResources();
        }
    }

    public void setInput(List inputs, QuestionType questionType) {
        this.inputs = inputs;
        this.questionTypeOfResource = questionType;
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (sourceFile == null || destFile == null) {
            throw new NoSuchFileException(
                "Can not create a new resource. Please select a file first");
        }
        try (FileInputStream source = new FileInputStream(sourceFile);
            FileOutputStream destination = new FileOutputStream(destFile)) {
            FileChannel sourceChannel = source.getChannel();
            FileChannel destinationChannel = destination.getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        }
    }


    private Resource createResourceFromFields() {
        Resource resource = new Resource();
        resource.setType(getResourceType());
        resource.setName(resourceName.getText());
        resource.setReference(out.getAbsolutePath());
        return resource;
    }

    private ResourceType getResourceType() {
        String fileName = out.getName();
        String extension = getExtension(fileName);
        switch (extension) {
            case "pdf":
                return ResourceType.PDF;
            default:
                return ResourceType.NOTE;
        }
    }

    private String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        String extension = "";
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    private String generateFileName(String fileName) {
        logger.debug("Checking for duplicate image files");
        File dir = new File("src/main/resources/resources/");
        String[] files = dir.list();
        Set<String> fileSet = new HashSet<>();
        fileSet.addAll(Arrays.asList(files));

        if (fileSet.contains(fileName)) {
            logger.debug("Duplicate found, generating new file name");
            String extension = getExtension(fileName);
            return UUID.randomUUID().toString() + "." + extension;
        } else {
            logger.debug("No duplicate found");
            return fileName;
        }
    }


}
