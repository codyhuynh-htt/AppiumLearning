package AppiumLearning;

import driver.DriverFactoryOld;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import models.components.BottomNavBarComponent;
import models.components.login_page_component.DialogComponent;
import models.components.login_page_component.LoginFormComponent;
import models.components.login_page_component.SignUpFormComponent;
import models.pages.LoginPage;
import org.json.JSONObject;
import utils.TestUtils;

public class TestLab_14 {

    private static String result_01 = null;
    private static String result_02 = null;
    private static String result_03 = null;
    private final static String systemPath = System.getProperty("user.dir");

    public static void main(String[] args) {

        /* Start appium server automatically */
        DriverFactoryOld.startAppiumServer();
        AndroidDriver<MobileElement> androidDriver = DriverFactoryOld.getAndroidDriver();
        /* Initialize JSONReader object */
        TestUtils testUtils = new TestUtils();
        /* Initialize loginData basing invalidLoginCreds.json file */
        JSONObject loginData = testUtils.readJSONFile("data/authentication/invalidLoginCreds.json");

        LoginPage loginPage = new LoginPage(androidDriver);
        BottomNavBarComponent bottomNavBarComp = loginPage.bottomNavBarComponent();
        LoginFormComponent loginFormComp = loginPage.loginFormComponent();
        SignUpFormComponent signUpFormComp = loginPage.signUpFormComponent();
        DialogComponent dialogComponent;

        bottomNavBarComp.clickOnLoginLabel(); // Navigate to Login Page

        if (loginPage.selectLoginForm()) {
            dialogComponent = loginFormComp
                    .inputEmailField(loginData.getJSONObject("validUser").getString("username"))
                    .inputPasswordField(loginData.getJSONObject("validUser").getString("password"))
                    .clickOnLoginBtn();

            if (dialogComponent.isDialogTemplateDisplay()) {
                dialogComponent.clickDialogBtn();
                result_01 = "TC_001_Login_App is PASSED";
            } else { result_01 = "TC_001_Login_App is FAILED"; }
        }

        if (loginPage.selectSignUpForm()) {
            dialogComponent = signUpFormComp.inputEmailField(loginData.getJSONObject("validUser").getString("username"))
                                            .inputPasswordField(loginData.getJSONObject("validUser").getString("password"))
                                            .inputRepeatPasswordField(loginData.getJSONObject("validUser").getString("password"))
                                            .clickOnSignUpBtn();

            if (dialogComponent.isDialogTemplateDisplay()) {
                dialogComponent.clickDialogBtn();
                result_02 = "TC_002_SignUp_App is PASSED";
            } else { result_02 = "TC_002_SignUp_App is FAILED"; }

            signUpFormComp.inputEmailField(loginData.getJSONObject("validUser").getString("username"))
                          .inputPasswordField(loginData.getJSONObject("validUser").getString("password"))
                          .inputRepeatPasswordField(loginData.getJSONObject("invalidPassword").getString("password"))
                          .clickOnSignUpBtn();

            String errPasswordConfirm = signUpFormComp.invalidRepeatPwMessageElem().getText();
            if (errPasswordConfirm.equalsIgnoreCase("Please enter the same password")) {
                result_03 = "TC_003_Fail_SignUp_App is PASSED";
            } else { result_03 = "TC_003_Fail_SignUp_App is FAILED"; }
        }

        androidDriver.closeApp();
        DriverFactoryOld.stopAppiumServer();
        System.out.println(result_01 + "\n" + result_02 + "\n" + result_03);
    }
}
