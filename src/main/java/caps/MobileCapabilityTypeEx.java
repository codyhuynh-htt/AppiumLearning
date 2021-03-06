package caps;

import io.appium.java_client.remote.MobileCapabilityType;

public interface MobileCapabilityTypeEx extends MobileCapabilityType {
    String APP_PACKAGE = "appPackage";
    String APP_ACTIVITY = "appActivity";
    String AVD_LAUNCH_TIMEOUT = "avdLaunchTimeout";
    String NEW_COMMAND_TIMEOUT = "newCommandTimeout";
    String AVD = "avd";
    String CHROME_DRIVER_EXECUTABLE = "chromedriverExecutable";
    String SYSTEM_PORT = "systemPort";
}
