package com.wedasoft.wedasoftFxTestBase.shared;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class Scene1Controller {

    @FXML
    Label scene1Label;
    @FXML
    TextField passedParameterTf;
    @FXML
    Button switchToScene2FxmlButton;

    public void init(String passedParamToScene1) {
        scene1Label.getScene().setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.A) {
                switchToScene2FxmlButton.fire();
            }
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                ((Stage) scene1Label.getScene().getWindow()).close();
            }
        });
        passedParameterTf.setText("");
        passedParameterTf.setText(passedParamToScene1);
    }

    public void onSwitchToScene2FxmlButtonClick(ActionEvent event) throws IOException {
        SceneUtil.switchSceneRoot(
                SceneUtil.getStageByActionEvent(event),
                getClass().getResource("/com/wedasoft/wedasoftFxTestBase/shared/scene2.fxml"),
                (Consumer<Scene2Controller>) controller -> controller.init("PassedParamToScene2"));
    }

}
