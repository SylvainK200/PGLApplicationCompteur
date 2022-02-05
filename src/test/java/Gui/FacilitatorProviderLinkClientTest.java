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
    public void t4_create_contract() {
        //when set username
        clickOn("#identifiant").write("test0pro");
        //and set password
        clickOn("#mot_de_passe").write("test");
        //and select user type
        clickOn("#type_utilisateur");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#connect_button");
        WaitForAsyncUtils.waitForFxEvents();
        // expect:
        verifyThat("#Buttonrecherche", hasText("Recherche"));
        
        clickOn("#manage_contract");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn("#NumeroClient");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn("#combEAN");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        
        clickOn("#date_debut").write("8/9/2021");
        clickOn("#date_fin").write("11/9/2021");
        clickOn("#meter_rate").write("2");
        clickOn("#network_manager_cost").write("10000");
        clickOn("#tax_rate").write("33");
        clickOn("#over_tax_rate").write("40");
        clickOn("#portefeuille");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#meter_type");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#confirmation_ajout");
        WaitForAsyncUtils.waitForFxEvents();
        //verifyThat("#connect_button", hasText("Se connecter"));
    }

    @Test 
    public void t3_create_ean() {
        //when set username
        clickOn("#identifiant").write("test0pro");
        //and set password
        clickOn("#mot_de_passe").write("test");
        //and select user type
        clickOn("#type_utilisateur");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#connect_button");
        WaitForAsyncUtils.waitForFxEvents();
        // expect:
        verifyThat("#Buttonrecherche", hasText("Recherche"));
        
        clickOn("#manage_contract");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        clickOn("#newEAN").write("eantest0");

        clickOn("#newEnergy");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn("#budget").write("10000");
        clickOn("#budgetType").write("Standart");
        clickOn("#creer_compteur");
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test 
    public void t2_login_conso_should_work() {
        //when set username
        clickOn("#identifiant").write("test0conso");
        //and set password
        clickOn("#mot_de_passe").write("test");
        //and select user type
        clickOn("#type_utilisateur");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#connect_button");
        WaitForAsyncUtils.waitForFxEvents();
        // expect:
        verifyThat("#exporter_button", hasText("Exporter"));
    }

    @Test 
    public void t2_login_provider_should_work() {
        //when set username
        clickOn("#identifiant").write("test0pro");
        //and set password
        clickOn("#mot_de_passe").write("test");
        //and select user type
        clickOn("#type_utilisateur");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#connect_button");
        WaitForAsyncUtils.waitForFxEvents();
        // expect:
        verifyThat("#Buttonrecherche", hasText("Recherche"));
        
        clickOn("#combClient");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn("#combPortefeuille");

        clickOn("#compteur");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn("#compteur_importer");

        clickOn("#type_compteur");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn("#manage_donnee");
        type(KeyCode.RIGHT);
        type(KeyCode.RIGHT);
        //clickOn("#menu_deconnecter");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat("#connect_button", hasText("Se connecter"));
    }


    @Test 
    public void t1_signup_provider_should_work() {
        //Loading page
        clickOn("#creer_compte");
        WaitForAsyncUtils.waitForFxEvents();
        
        clickOn("#nom").write("test0pro");        
        clickOn("#number").write("690909090");
        clickOn("#identifiant").write("test0pro");
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
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#creer_compte");
        WaitForAsyncUtils.waitForFxEvents();
        // expect:
        verifyThat("#connect_button", hasText("Se connecter"));
    }

    @Test 
    public void t1_signup_conso_should_work() {
        //Loading page
        clickOn("#creer_compte");
        WaitForAsyncUtils.waitForFxEvents();
        
        clickOn("#nom").write("test0conso");        
        clickOn("#number").write("690909090");
        clickOn("#identifiant").write("test0conso");
        clickOn("#adresse_mail").write("mail@example.com");
        clickOn("#mot_de_passe").write("test");
        clickOn("#confirmation_mot_de_passe").write("test");
        clickOn("#question_secrete").write("Who");
        clickOn("#reponse_question_secrete").write("is");
        clickOn("#street").write("R0");
        clickOn("#city").write("Doul");
        clickOn("#postal_code").write("102");
        clickOn("#type_utilisateur");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#creer_compte");
        WaitForAsyncUtils.waitForFxEvents();
        // expect:
        verifyThat("#connect_button", hasText("Se connecter"));
    }

    @Test
    public void t0_should_contain_button() {
        // expect:
        verifyThat("#connect_button", hasText("Se connecter"));
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