package com.wedasoft.wedasoftFxTestBase.shared;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;


public class JfxDialogUtil {

    public static Stage createFxmlDialog(
            String title,
            boolean dialogIsModal,
            boolean dialogIsResizeable,
            URL absoluteFxmlFileUrl,
            Dimension2D sceneSize,
            @SuppressWarnings("rawtypes") Consumer initMethodOfController,
            Runnable callbackOnDialogClose)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(absoluteFxmlFileUrl);
        Parent root = loader.load(); // this calls the constructor and after that initialize from jfx()
        Object viewController = loader.getController();
        Scene scene = sceneSize == null ? new Scene(root) : new Scene(root, sceneSize.getWidth(), sceneSize.getHeight());

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(dialogIsModal ? Modality.APPLICATION_MODAL : Modality.NONE);
        stage.setResizable(dialogIsResizeable);
        stage.setScene(scene);
        stage.setOnHidden(event -> {
            event.consume();
            if (callbackOnDialogClose != null) {
                callbackOnDialogClose.run();
            }
            stage.close();
        });

        if (initMethodOfController != null) {
            //noinspection unchecked
            initMethodOfController.accept(viewController);
        }
        return stage;
    }

}
