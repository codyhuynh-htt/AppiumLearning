package models.components.login_page_component;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import models.base.PageModel;

public class SignUpFormComponent extends PageModel {

    @AndroidFindBy(accessibility = "input-email")
    private MobileElement emailInputFieldElem;
    @AndroidFindBy(accessibility = "input-password")
    private MobileElement passwordFieldElem;
    @AndroidFindBy(accessibility = "input-repeat-password")
    private MobileElement repeatPasswordFieldElem;
    @AndroidFindBy(accessibility = "button-SIGN UP")
    private MobileElement signUpBtnElem;
    @AndroidFindBy(xpath = "//*[@content-desc='button-SIGN UP']/preceding-sibling::android.widget.TextView[3]")
    private MobileElement wrongEmailTextElem;
    @AndroidFindBy(xpath = "//*[@content-desc='button-SIGN UP']/preceding-sibling::android.widget.TextView[2]")
    private MobileElement wrongPasswordTextElem;
    @AndroidFindBy(xpath = "//*[@content-desc='button-SIGN UP']/preceding-sibling::android.widget.TextView[1]")
    private MobileElement errRepeatPwMessageElem;

    public SignUpFormComponent(AppiumDriver<MobileElement> appiumDriver) {
        super(appiumDriver);
    }

    public MobileElement signUpBtnElem() {
        waitForVisibility(signUpBtnElem);
        return signUpBtnElem;
    }

    public MobileElement wrongEmailTextElem() {
        waitForVisibility(wrongEmailTextElem);
        return wrongEmailTextElem;
    }

    public MobileElement wrongPasswordTextElem() {
        waitForVisibility(wrongPasswordTextElem);
        return wrongPasswordTextElem;
    }

    public MobileElement errRepeatPwMessageElem() {
        waitForVisibility(errRepeatPwMessageElem);
        return errRepeatPwMessageElem;
    }

    @Step("Input email as {email}")
    public SignUpFormComponent inputEmailField(String email) {
        clearElementInputField(emailInputFieldElem);
        sendKeysToElement(emailInputFieldElem, email);
        return this;
    }

    @Step("Input password as {password}")
    public SignUpFormComponent inputPasswordField(String password) {
        clearElementInputField(passwordFieldElem);
        sendKeysToElement(passwordFieldElem, password);
        return this;
    }

    @Step("Input repeat password as {password}")
    public SignUpFormComponent inputRepeatPasswordField(String password) {
        clearElementInputField(repeatPasswordFieldElem);
        sendKeysToElement(repeatPasswordFieldElem, password);
        return this;
    }

    @Step("Click on Sign Up button")
    public DialogComponent clickOnSignUpBtn() {
        clickElement(signUpBtnElem);
        return new DialogComponent(appiumDriver);
    }

}
