package tests;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import Gui.FacilitatorProviderLinkClient;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.testfx.util.WaitForAsyncUtils;
import javafx.scene.input.KeyCode;

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

    @Test 
    public void t2_login_should_work() {
        //when set username
        clickOn("#identifiant").write("test");
        //and set password
        clickOn("#mot_de_passe").write("test");
        //and select user type
        clickOn("#type_utilisateur");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#connect_button");
        WaitForAsyncUtils.waitForFxEvents();
        // expect:
        verifyThat("#Buttonrecherche", hasText("Recherche"));
    }
    
    @Test
    public void t1_should_contain_button() {
        // expect:
        verifyThat("#connect_button", hasText("Se connecter"));
    }


    @Test 
    public void t0_signup_should_work() {
        //Loading page
        clickOn("#creer_compte");
        WaitForAsyncUtils.waitForFxEvents();
        
        clickOn("#nom").write("test");        
        clickOn("#number").write("690909090");
        clickOn("#identifiant").write("test");
        clickOn("#adresse_mail").write("mail@example.com");
        clickOn("#mot_de_passe").write("test");
        clickOn("#confirmation_mot_de_passe").write("test");
        clickOn("#question_secrete").write("test");
        clickOn("#reponse_question_secrete").write("test");
        clickOn("#street").write("test");
        clickOn("#city").write("test");
        clickOn("#postal_code").write("012");
        clickOn("#type_utilisateur");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#creer_compte");
        WaitForAsyncUtils.waitForFxEvents();
        // expect:
        verifyThat("#Buttonrecherche", hasText("Recherche"));
    }

    /*
        @Test public void should_click_on_button() {
            // when:
            clickOn(".button");
            WaitForAsyncUtils.waitForFxEvents();
            // then:
            verifyThat(".button", hasText("clicked!"));
        }
    */
}