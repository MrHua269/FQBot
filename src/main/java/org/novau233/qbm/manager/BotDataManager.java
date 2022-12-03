package org.novau233.qbm.manager;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class BotDataManager {
    public static class BotData{
        public int rpic3Id;
    }

    public static BotData currentData = new BotData();
    private static final Logger logger = LogManager.getLogger();
    private static final Gson gson = new Gson();

    public static void init(){
        try {
            File dataFile = new File("botdata.json");
            if(dataFile.exists()){
                String jsonContent = new String(Files.readAllBytes(dataFile.toPath()));
                currentData = gson.fromJson(jsonContent,BotData.class);
            }else{
                FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
                fileOutputStream.write(gson.toJson(currentData).getBytes(StandardCharsets.UTF_8));
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }catch (Exception e){
            logger.error(e);
            e.printStackTrace();
        }
    }

    public static void saveCurrent() {
        try {
            File dataFile = new File("botdata.json");
            dataFile.delete();
            FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
            fileOutputStream.write(gson.toJson(currentData).getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }
}
