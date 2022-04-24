package Gui;

import org.junit.BeforeClass;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.stage.Stage;

/**
 * 
 */
@ExtendWith(MockitoExtension.class)
public class FacilitatorProviderLinkClientTest extends ApplicationTest {
    
    /**
     * Use glass robot.
     * Note: this must happen once before the class is loaded. Otherwise,
     * the very first test run uses the awt robot
     */
    @BeforeClass
    public static void config() throws Exception {
        System.getProperties().put("testfx.robot", "glass");
    }

    @Override public void start(Stage stage) throws Exception{
        FacilitatorProviderLinkClient.startForTests(stage);
    }
}