### Description

A library that simplifies the integration of JavaFX into your tests.

Effortlessly run unit tests requiring JavaFX components by extending your test class with a single class.

Eliminate common errors such as "Toolkit not found," "Toolkit already initialized," "Location is not set," and "Not on
FX application thread" during testing.

### Requirements

| Technology | Version |
|------------|---------|
| Java       | 17      |
| JavaFX     | 17.0.8  |

### Dependencies to add

##### Maven

    <!-- Maven looks in the central repository by default. -->
    <dependency>
      <groupId>com.wedasoft</groupId>
      <artifactId>wedasoftfxtestbase</artifactId>
      <version>1.0.0</version>
    </dependency>

##### Gradle

    repositories {
      mavenCentral()
    }
    dependencies {
      implementation("com.wedasoft:wedasoftfxtestbase:1.0.0")
    }

### Documentation

##### Step 1: Prepare the test classes

Extend your wished test classes with the class <code>WedasoftFxTestBase</code>. That's it.

    class UnitTests extends WedasoftFxTestBase { 
    }

##### Step 2: Write a unit test

1. Simply write a standard unit test method in your test class.
2. To run code on the JavaFX thread, just invoke <code>runOnJavaFxThreadAndJoin()</code> and pass the code.<br>
   The main thread will wait until the passed code is executed.<br>
   You can invoke <code>runOnJavaFxThreadAndJoin()</code> as often as you like.

<b>IMPORTANT:</b> <br>
Do not use assertions in <code>runOnJavaFxThreadAndJoin()</code>.<br>
JUnit will not recognize failed assertions in the JavaFX thread.

    @Test
    void myTest1() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            // run this code on the JavaFX thread
            button = new Button("buttonlabel");
            // wait for the JavaFX thread to complete
        });
        assertEquals("buttonlabel", button.getText());
    }

##### Example test class 1

    public class UnitTests1 extends WedasoftFxTestBase {
   
       Button button;
       Alert alert;
   
       @Test
       void testButton() throws Exception {
           runOnJavaFxThreadAndJoin(() -> {
               button = new Button("buttonLabel");
           });
           assertEquals("buttonLabel", button.getText());
           assertNotEquals("wrongLabel", button.getText());
       }
   
       @Test
       void testDialogBuilding() throws Exception {
           runOnJavaFxThreadAndJoin(() -> {
               alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Old");
               alert.show();
           });
           assertNotNull(alert);
           assertEquals("Old", alert.getTitle());
           runOnJavaFxThreadAndJoin(() -> {
               alert.setTitle("N.........");
               alert.setTitle("New");
               alert.hide();
           });
           assertEquals("New", alert.getTitle());
       }

}

##### Example test class 2

    import javafx.geometry.Dimension2D;
    import javafx.scene.control.Button;
    import org.junit.jupiter.api.*;
    import static org.junit.jupiter.api.Assertions.*;

    class UnitTests2 extends WedasoftFxTestBase {
    
        FxmlDialog.Builder builder;
        Button button;
    
        @Test
        void testButton() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                button = new Button("buttonLabel");
            });
            assertEquals("buttonLabel", button.getText());
            assertNotEquals("wrongLabel", button.getText());
        }

        @Test
        void testDialogBuilding() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                builder = new FxmlDialog.Builder(getClass().getResource("/com/wedasoft/abcView.fxml"), null);
                builder.setStageTitle("Old StageTitle");
            });
            assertEquals("Old StageTitle", builder.get().getStage().getTitle());
            assertNotNull(builder.get());
            builder.setStageTitle("New StageTitle");
            assertEquals(600, builder.get().getStage().getScene().getWidth());
            assertEquals(500, builder.get().getStage().getScene().getHeight());
            assertEquals("New StageTitle", builder.get().getStage().getTitle());
        }

        @Test
        void multiRunsOnJavaFxThread() throws Exception {
            // step 1
            runOnJavaFxThreadAndJoin(() -> {
                builder = new FxmlDialog.Builder(getClass().getResource("/com/wedasoft/abcView.fxml"), null);
                builder.setStageTitle("Old StageTitle");
            });
            assertNotNull(builder.get());
            assertEquals("Old StageTitle", builder.get().getStage().getTitle());
            assertEquals(600, builder.get().getStage().getScene().getWidth());
            assertEquals(500, builder.get().getStage().getScene().getHeight());

            // step 2
            builder.setStageTitle("New StageTitle");
            assertEquals("New StageTitle", builder.get().getStage().getTitle());
    
            // step 3
            assertThrows(Exception.class, () -> runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder(getClass().getResource("/path/does/not/exist/view.fxml"), null)));
    
            // step 4
            runOnJavaFxThreadAndJoin(() -> {
                builder = new FxmlDialog.Builder(getClass().getResource("/com/wedasoft/abcView.fxml"), null);
                builder.setStageTitle("Another StageTitle");
                button2 = new Button("second init button");
            });
            assertEquals(1000, builder.get().getStage().getScene().getWidth());
            assertEquals(1000, builder.get().getStage().getScene().getHeight());
            assertEquals("Another StageTitle", builder.get().getStage().getTitle());
            
            assertNotNull(button2);
            assertEquals("second init button", button2.getText());
        }

    }
  
