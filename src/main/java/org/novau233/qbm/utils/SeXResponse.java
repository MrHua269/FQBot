package org.novau233.qbm.utils;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.novau233.qbm.utils.Utils.getRandomUserAgent;
import static org.novau233.qbm.utils.Utils.readInputStreamToByte;

public class SeXResponse {
    private static final String API_URL = "https://api.lolicon.app/setu/v2?proxy=i.pixiv.cat";
    private static final Gson gson = new Gson();

    private static final AtomicInteger threadId = new AtomicInteger();
    private static final ThreadFactory WORKER_FACTORY = r->{
        Thread worker = new Thread(r,"SXDownload-worker # "+threadId.getAndIncrement());
        worker.setPriority(3);
        worker.setDaemon(true);
        return worker;
    };
    private static final Executor HTTPS_EXECUTOR = Executors.newCachedThreadPool(WORKER_FACTORY);

    private final String error;
    private final Data[] data;

    public SeXResponse(String error, Data[] data){
        this.error = error;
        this.data = data;
    }

    public Data[] getData(){
        return this.data;
    }

    public String getError(){
        return this.error;
    }

    public static  final class Data{
        public int pid;
        public int p;
        public int uid;
        public String title;
        public String author;
        public boolean r18;
        public int width;
        public int height;
        public String[] tags;
        public String ext;
        public long updateDate;
        public URLs urls;
    }

    public static class URLs{
        public String original;
        private static final AtomicInteger currentId = new AtomicInteger();
        private static final File CACHE = new File("ACaches");

        static{
            if (!CACHE.exists()){
                CACHE.mkdirs();
            }
            currentId.set(CACHE.listFiles().length+1);
        }

        public static File getCache(){
            return CACHE;
        }

        public File save() {
            try {
                File file = new File(CACHE, currentId.getAndIncrement() + ".jpg");
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(getBytes());
                stream.flush();
                stream.close();
                LogManager.getLogger().info("saved pic:{}", file.getPath());
                return file;
            } catch (Exception e) {
                LogManager.getLogger().error(e);
            }
            return null;
        }

        public byte[] getBytes(){
            try{
                final URL url = new URL(this.original);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", getRandomUserAgent());
                connection.setReadTimeout(30000);
                connection.setConnectTimeout(3000);
                connection.connect();
                try{
                    if (connection.getResponseCode() == 200){
                        return readInputStreamToByte(connection.getInputStream());
                    }
                }finally {
                    connection.disconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        public CompletableFuture<byte[]> getBytesAsync(){
            return CompletableFuture.supplyAsync(this::getBytes,HTTPS_EXECUTOR);
        }

        public CompletableFuture<?> saveAsync(){
            return CompletableFuture.runAsync(this::save,HTTPS_EXECUTOR);
        }
    }

    public static SeXResponse getNew(boolean r18) throws IOException {
        String origanl = API_URL;
        if (r18) {
            origanl += "?r18=1";
        }
        final URL url = new URL(origanl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", getRandomUserAgent());
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);
        connection.connect();
        if (connection.getResponseCode() == 200) {
            byte[] buffer = readInputStreamToByte(connection.getInputStream());
            connection.disconnect();
            LogManager.getLogger().info("Json response:{}", new String(buffer));
            return gson.fromJson(new String(buffer), SeXResponse.class);
        }
        throw new IOException();
    }

    public static SeXResponse getNew(boolean r18, String[] tags) throws IOException {
        String origanl = API_URL;
        if (r18) {
            origanl += "?r18=1";
        }

        final StringBuilder tagsBuilder = new StringBuilder();
        int index = 0;
        for (String tag : tags) {
            if (!r18 && index == 0) {
                tagsBuilder.append("?tag=").append(URLEncoder.encode(tag, "UTF-8"));
                continue;
            }
            tagsBuilder.append("&tag=").append(URLEncoder.encode(tag, "UTF-8"));
            index++;
        }

        final URL url = new URL(origanl + tagsBuilder);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", getRandomUserAgent());
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);
        connection.connect();
        if (connection.getResponseCode() == 200) {
            byte[] buffer = readInputStreamToByte(connection.getInputStream());
            connection.disconnect();
            LogManager.getLogger().info("Json response:{}", new String(buffer));
            return gson.fromJson(new String(buffer), SeXResponse.class);
        }
        throw new IOException();
    }

    public static CompletableFuture<SeXResponse> getNewAsync(boolean r18,String[] tags){
        return CompletableFuture.supplyAsync(()->{
            try {
                return SeXResponse.getNew(r18,tags);
            } catch (IOException e) {
                LogManager.getLogger().error(e);
            }
            return null;
        },HTTPS_EXECUTOR);
    }

    public static CompletableFuture<SeXResponse> getNewAsync(boolean r18){
        return CompletableFuture.supplyAsync(()->{
            try {
                return SeXResponse.getNew(r18);
            } catch (IOException e) {
                LogManager.getLogger().error(e);
            }
            return null;
        },HTTPS_EXECUTOR);
    }
}
