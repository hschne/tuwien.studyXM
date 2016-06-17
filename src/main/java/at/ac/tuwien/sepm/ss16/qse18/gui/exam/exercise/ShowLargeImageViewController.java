package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.gui.BaseController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableQuestion;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by Zhang Haixiang on 17.06.2016.
 */
@Component public class ShowLargeImageViewController extends BaseController{
    @FXML protected ImageView view;

    @FXML public void initialize(ObservableQuestion question){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(question.getQuestion()));
        view.setImage(new Image(fileChooser.getInitialDirectory().toURI().toString()));
    }

    public void dsf(){
        mainFrameController.handleShowDetail();
    }
}
