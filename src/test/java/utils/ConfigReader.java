package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;




public class ConfigReader {
	
	private static Properties prop=new Properties();
	

    /**
     * Load configuration based on environment name (e.g., "qa", "dev", "stage", "prod").
     * Primary file: src/test/resources/config/{env}.properties
     * Fallback:     src/test/resources/config/config.properties
     */
	
 
	
	//Load the config.properties file
	public static void loadConfig(String env) {
		String envFilePath = "src/test/resources/" + env + ".properties";
        String defaultFilePath = "src/test/resources/config.properties";

        try {
            if (Files.exists(Paths.get(envFilePath))) {
                try (FileInputStream fis = new FileInputStream(envFilePath)) {
                    prop.load(fis);
                    LogHelper.info("✅ Config file loaded successfully from: " + envFilePath);
                }
            } else {
                LogHelper.warn("⚠️ Env config file not found: " + envFilePath + ". Falling back to default config.properties");

                if (Files.exists(Paths.get(defaultFilePath))) {
                    try (FileInputStream fis = new FileInputStream(defaultFilePath)) {
                        prop.load(fis);
                        LogHelper.info("✅ Default config file loaded from: " + defaultFilePath);
                    }
                } else {
                    String msg = "❌ No valid config file found! Checked: " + envFilePath + " and " + defaultFilePath;
                    LogHelper.error(msg);
                    throw new RuntimeException(msg);
                }
            }
        } catch (IOException e) {
            String msg = "❌ Failed to load configuration file";
            LogHelper.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
	
	
	//Get value by key
	public static String get(String key) {
		String value=prop.getProperty(key);
		if(value==null) {
			LogHelper.warn("⚠️ Property '" + key + "' not found in loaded config file");
		}
		return value;
	}

}
