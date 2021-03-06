package driver;

import caps.MobileCapabilityTypeEx;
import flags.AndroidServerFlagEx;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.Constant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DriverFactoryOld {

    private static final String KILL_NODE_WINDOW_CMD = "taskkill /F /IM node.exe";
    private static final String KILL_NODE_LINUX_CMD = "killall node";
    private static final String CLOSE_AVD_CMD = "adb -s 05157df58940201b emu kill"; // Use for S6 Edge
    private static final String CURRENT_OS = System.getProperty("os.name").toLowerCase();

    private static AppiumDriverLocalService appiumServer;
    private static AndroidDriver<MobileElement> appiumDriver;

    public static void startAppiumServer() {
        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumServiceBuilder.withIPAddress("127.0.0.1").usingAnyFreePort();
        /* Use AndroidServerFlagEx, extended from AndroidSeverFlag, for automatically discovering compatible Chrome driver */
        appiumServiceBuilder.withArgument(AndroidServerFlagEx.ALLOW_INSECURE, "chromedriver_autodownload");
        appiumServer = AppiumDriverLocalService.buildService(appiumServiceBuilder);
        appiumServer.start();
    }

    public static void stopAppiumServer() {
        String killNodeCmd = CURRENT_OS.startsWith("windows") ? KILL_NODE_WINDOW_CMD : KILL_NODE_LINUX_CMD;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(killNodeCmd);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Failed to close node");
        } finally {
            appiumServer.stop();
        }
    }

    public static AndroidDriver<MobileElement> getAndroidDriver() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.AUTOMATION_NAME, "UiAutomator2");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.DEVICE_NAME, "Z5 Prem");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.UDID, "CB5A2BZKHF");
//        desiredCapabilities.setCapability(MobileCapabilityTypeEx.DEVICE_NAME, "S6 Edge");
//        desiredCapabilities.setCapability(MobileCapabilityTypeEx.UDID, "05157df58940201b");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.APP_PACKAGE, "com.wdiodemoapp");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.APP_ACTIVITY, "com.wdiodemoapp.MainActivity");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.AVD_LAUNCH_TIMEOUT, 120_000);
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.NEW_COMMAND_TIMEOUT, 120);
        AndroidDriver<MobileElement> androidDriver = new AndroidDriver<>(appiumServer.getUrl(), desiredCapabilities);
        androidDriver.manage().timeouts().implicitlyWait(Constant.TIME_TO_INIT_APPIUM_DRIVER, TimeUnit.SECONDS);

        System.out.println("Session ID: " + androidDriver.getSessionId());
        androidDriver.quit();
        return androidDriver;
    }

}
