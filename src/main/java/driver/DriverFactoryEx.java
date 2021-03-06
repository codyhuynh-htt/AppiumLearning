package driver;

import caps.MobileCapabilityTypeEx;
import flags.AndroidServerFlagEx;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.Constant;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactoryEx {

    private static final String KILL_NODE_WINDOW_CMD = "taskkill /F /IM node.exe";
    private static final String KILL_NODE_LINUX_CMD = "killall node";
    /*private static final String CLOSE_AVD_CMD = "adb -s 05157df58940201b emu kill"; // Use for S6 Edge */
    private static final String CURRENT_OS = System.getProperty("os.name").toLowerCase();
    private static AppiumDriverLocalService appiumServer;

    private AppiumDriver<MobileElement> appiumDriver;

    public AppiumDriver<MobileElement> getAppiumDriver() {
        if (appiumDriver == null)
            appiumDriver = initAppiumDriver();
        return appiumDriver;
    }

    public AppiumDriver<MobileElement> getAppiumDriver(String udid, String port, String systemPort, String deviceName) {
        if (appiumDriver == null)
            appiumDriver = initAppiumDriver(udid, port, systemPort);
        return appiumDriver;
    }

    private AppiumDriver<MobileElement> initAppiumDriver() {
        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumServiceBuilder.withIPAddress("127.0.0.1").usingAnyFreePort();
        /* Use AndroidServerFlagEx, extended from AndroidSeverFlag, for automatically discovering compatible Chrome driver */
        appiumServiceBuilder.withArgument(AndroidServerFlagEx.ALLOW_INSECURE, "chromedriver_autodownload");
        appiumServer = AppiumDriverLocalService.buildService(appiumServiceBuilder);
        appiumServer.start();

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.AUTOMATION_NAME, "UiAutomator2");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.DEVICE_NAME, "Z5 Prem");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.UDID, "CB5A2BZKHF");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.APP_PACKAGE, "com.wdiodemoapp");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.APP_ACTIVITY, "com.wdiodemoapp.MainActivity");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.AVD_LAUNCH_TIMEOUT, 120_000);
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.NEW_COMMAND_TIMEOUT, 120);
        appiumDriver = new AndroidDriver<>(appiumServer.getUrl(), desiredCapabilities);
        appiumDriver.manage().timeouts().implicitlyWait(Constant.TIME_TO_INIT_APPIUM_DRIVER, TimeUnit.SECONDS);

        System.out.println("Session ID: " + appiumDriver.getSessionId());
        return appiumDriver;
    }

    public AppiumDriver<MobileElement> getAppiumDriver(String udid, String port, String systemPort) {
        if (appiumDriver == null)
            appiumDriver = initAppiumDriver(udid, port, systemPort);
        return appiumDriver;
    }

    private AppiumDriver<MobileElement> initAppiumDriver(String udid, String port, String systemPort) {
//        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
//        appiumServiceBuilder.withIPAddress("127.0.0.1").usingAnyFreePort();
//        /* Use AndroidServerFlagEx, extended from AndroidSeverFlag, for automatically discovering compatible Chrome driver */
//        appiumServiceBuilder.withArgument(AndroidServerFlagEx.ALLOW_INSECURE, "chromedriver_autodownload");
//        appiumServer = AppiumDriverLocalService.buildService(appiumServiceBuilder);
//        appiumServer.start();

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.AUTOMATION_NAME, "uiautomator2");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.UDID, udid);
        desiredCapabilities.setCapability("systemPort", Integer.parseInt(systemPort));
//        desiredCapabilities.setCapability("mjpegServerPort", Integer.parseInt(port));
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.APP_PACKAGE, "com.wdiodemoapp");
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.APP_ACTIVITY, "com.wdiodemoapp.MainActivity");
        //desiredCapabilities.setCapability(MobileCapabilityTypeEx.AVD_LAUNCH_TIMEOUT, 120_000);
        desiredCapabilities.setCapability(MobileCapabilityTypeEx.NEW_COMMAND_TIMEOUT, 120);
        String localAppium = System.getenv("localAppium");
        String hub = System.getProperty("hub");

        String targetServer;
        if (localAppium != null) {
            targetServer = localAppium + "/wd/hub";
        } else if (hub != null) {
            targetServer = hub + ":4444/wd/hub";
        } else {
            throw new IllegalArgumentException("Required localAppium or hub");
        }
        try {
            URL appiumServerPath = new URL(targetServer);
            appiumDriver = new AndroidDriver<>(appiumServerPath, desiredCapabilities);
//            appiumDriver = new AndroidDriver<>(appiumServer.getUrl(), desiredCapabilities);
            appiumDriver.manage().timeouts().implicitlyWait(Constant.TIME_TO_INIT_APPIUM_DRIVER, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Session ID: " + appiumDriver.getSessionId());
        return appiumDriver;
    }

    public void quitAppiumSession() {
        if (appiumDriver != null) {
            appiumDriver.quit();
            appiumDriver = null;
        }
    }

    private static void stopAppiumServer() {
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

}
