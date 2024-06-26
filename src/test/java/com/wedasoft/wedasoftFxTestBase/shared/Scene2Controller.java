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

public class Scene2Controller {

    @FXML
    Label scene2Label;
    @FXML
    TextField passedParameterTf;
    @FXML
    Button switchToScene1FxmlButton;

    public void init(String passedParamToScene2) {
        scene2Label.getScene().setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.A) {
                switchToScene1FxmlButton.fire();
            }
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                ((Stage) scene2Label.getScene().getWindow()).close();
            }
        });
        passedParameterTf.setText("");
        passedParameterTf.setText(passedParamToScene2);
    }

    public void onSwitchToScene1FxmlButtonClick(ActionEvent event) throws IOException {
        SceneUtil.switchSceneRoot(
                SceneUtil.getStageByActionEvent(event),
                getClass().getResource("/com/wedasoft/wedasoftFxTestBase/shared/scene1.fxml"),
                (Consumer<Scene1Controller>) controller -> controller.init("PassedParamToScene1"));
    }

}
