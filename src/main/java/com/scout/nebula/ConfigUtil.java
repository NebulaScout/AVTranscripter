/*
* This class loads properties from config.properties file
*/

package com.scout.nebula;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private Properties properties = new Properties();

    public ConfigUtil() {
        try(InputStream input = getClass().getClassLoader().getResourceAsStream(("config.properties"))) {
            if(input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            // Load a properties file
            properties.load(input);
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    public String getApiKey() {
        return  properties.getProperty("api.key");
    }
}
