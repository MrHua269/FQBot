package org.novau233.qbm.utils;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class RandECPicResp {
    private static final Gson GSON = new Gson();
    public final int code;
    public final String url;

    public RandECPicResp(int code, String url){
        this.code = code;
        this.url = url;
    }

    public static RandECPicResp getNew(){
        try{
            String api = randomAPI();
            URL url1 = new URL(api);
            LogManager.getLogger().info("Chose api:{}",api);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setReadTimeout(30000);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.connect();
            if (connection.getResponseCode() == 200){
                byte[] buffer = readInputStream(connection.getInputStream());
                String read = new String(buffer);
                connection.disconnect();
                return GSON.fromJson(read, RandECPicResp.class);
            }
        }catch (Exception e){
            LogManager.getLogger().error(e.getMessage());
        }
        return null;
    }

    private static final Random random = new Random();

    public static String randomAPI(){
        switch (random.nextInt(2)){
            case 1:
                return "https://api.likepoems.com/img/upyun/pixiv/?type=json";
            case 2:
                return "https://api.likepoems.com/img/sina/pixiv/?type=json";
            case 0:
                return "https://api.likepoems.com/img/upyun/pc/?type=json";
            default:
                return "https://api.likepoems.com/img/sina/pixiv/?type=json";
        }
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte['Ѐ'];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public void saveToFile(File parent,String name){
        try{
            File file = new File(parent,name);
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            URL url1 = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setReadTimeout(30000);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.connect();
            if (connection.getResponseCode() == 200){
                byte[] buffer = readInputStream(connection.getInputStream());
                connection.disconnect();
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(buffer);
                stream.flush();
                stream.close();
            }
        }catch (Exception e){
            LogManager.getLogger().error(e);
        }
    }

    public byte[] getBytes(){
        try{
            URL url1 = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setReadTimeout(30000);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.connect();
            if (connection.getResponseCode() == 200){
                byte[] buffer = readInputStream(connection.getInputStream());
                connection.disconnect();
                return buffer;
            }
        }catch (Exception e){
            LogManager.getLogger().error(e);
        }
        return null;
    }
}
