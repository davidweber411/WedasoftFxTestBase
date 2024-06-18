package com.wedasoft.wedasoftFxTestBase.testBase;


import com.wedasoft.wedasoftFxTestBase.WedasoftFxTestBase;
import com.wedasoft.wedasoftFxTestBase.WedasoftFxTestBaseException;
import com.wedasoft.wedasoftFxTestBase.shared.JfxDialogUtil;
import com.wedasoft.wedasoftFxTestBase.shared.Scene1Controller;
import javafx.geometry.Dimension2D;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.function.Consumer;

import static com.wedasoft.wedasoftFxTestBase.WedasoftFxTestBaseImpl.PRL_TIMEOUT_SECONDS_TO_WAIT;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimeOutAndStageClosingTest extends WedasoftFxTestBase {

    private Stage stage;

    private int valueChangedByCallback;

    @Test
    @Order(1)
    void openAndCloseStage_oneTime_shallCheckForChangedValue() throws Exception {
        valueChangedByCallback = 0;
        runOnJavaFxThreadAndJoin(() -> stage = JfxDialogUtil.createFxmlDialog(
                "xxxxxxxx",
                true,
                true,
                getClass().getResource("/com/wedasoft/wedasoftFxTestBase/shared/scene1.fxml"),
                new Dimension2D(600, 500),
                (Consumer<Scene1Controller>) consumer -> consumer.init("myparamter1"),
                () -> valueChangedByCallback = 52));

        pressKeyAsyncInOtherThread(1000, KeyCode.ESCAPE);
        runOnJavaFxThreadAndJoin(() -> stage.showAndWait());

        assertNotNull(stage);
        assertEquals(52, valueChangedByCallback);
    }

    @Test
    @Order(2)
    void openAndCloseStage_multipleTimes_shallCheckForChangedValue() throws Exception {
        valueChangedByCallback = 0;
        runOnJavaFxThreadAndJoin(() -> stage = JfxDialogUtil.createFxmlDialog(
                "xxxxxxxx",
                true,
                true,
                getClass().getResource("/com/wedasoft/wedasoftFxTestBase/shared/scene1.fxml"),
                new Dimension2D(600, 500),
                (Consumer<Scene1Controller>) consumer -> consumer.init("myparamter1"),
                () -> valueChangedByCallback = 52));

        pressKeyAsyncInOtherThread(1000, KeyCode.ESCAPE);
        runOnJavaFxThreadAndJoin(() -> stage.showAndWait());

        pressKeyAsyncInOtherThread(1000, KeyCode.ESCAPE);
        runOnJavaFxThreadAndJoin(() -> stage.showAndWait());

        pressKeyAsyncInOtherThread(1000, KeyCode.ESCAPE);
        runOnJavaFxThreadAndJoin(() -> stage.showAndWait());

        assertNotNull(stage);
        assertEquals(52, valueChangedByCallback);
    }

    @Test
    @Order(3)
    void runLater_dontRunIntoTimeout_shallNotThrow() {
        assertDoesNotThrow(() -> runOnJavaFxThreadAndJoin(() -> sleep(100)));
    }

    @Test
    @Order(4)
    void runLater_runIntoTimeout_shallThrow() {
        // sleep 2 seconds longer than the timeout setting.
        assertThrows(WedasoftFxTestBaseException.class, () -> runOnJavaFxThreadAndJoin(() -> sleep((PRL_TIMEOUT_SECONDS_TO_WAIT * 1000) + 2000)));
    }
}
