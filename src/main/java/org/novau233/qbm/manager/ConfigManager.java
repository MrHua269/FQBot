package org.novau233.qbm.manager;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ConfigManager {
    private static final String CONFIGNAME = "config.yml";
    private static final Logger LOGGER = LogManager.getLogger();
    public static ConfigFile CONFIG_FILE_READ;

    public static void init(){
        LOGGER.info("Reading config...");
        final File file = new File("configs");
        if (file.exists() && file.isDirectory()){
            CONFIG_FILE_READ = ConfigFile.readFromFile(file,"groupconfig.json");
        }
        if(CONFIG_FILE_READ == null){
            LOGGER.info("Config file does not found!Creating...");
            ConfigFile configFile = new ConfigFile(0000,1145);
            configFile.writeToFile(file,"groupconfig.json");
            CONFIG_FILE_READ = configFile;
        }
        LOGGER.info("Config init successful!");
    }
}
