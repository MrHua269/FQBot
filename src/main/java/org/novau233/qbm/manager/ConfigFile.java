package org.novau233.qbm.manager;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigFile {
    private static final Gson GSON = new Gson();
    private final long listeningGroup;
    private final long masterName;

    public ConfigFile(long listeningGroup,long masterName){
        this.listeningGroup = listeningGroup;
        this.masterName = masterName;
    }

    public long getListeningGroup(){
        return this.listeningGroup;
    }

    public long getMasterName() {
        return masterName;
    }

    public void writeToFile(File file, String name){
        final String content = GSON.toJson(this);
        final File file1 = new File(file,name);
        if (!file1.exists()){
            try {
                file1.createNewFile();
            } catch (IOException e) {
                LogManager.getLogger().error(e);
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(file1)){
            outputStream.write(content.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            LogManager.getLogger().error(e);
        }
    }

    public static ConfigFile readFromFile(File file,String name){
        final File file1 = new File(file,name);
        try (FileInputStream stream = new FileInputStream(file1)){
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            final String content = new String(buffer);
            return GSON.fromJson(content,ConfigFile.class);
        } catch (FileNotFoundException e){
            return null;
        }catch (IOException e) {
            LogManager.getLogger().error(e);
        }
        return null;
    }
}
