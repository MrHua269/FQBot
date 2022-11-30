package org.novau233.qbm.manager;

import com.google.gson.Gson;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.novau233.qbm.bot.BotConfigEntry;
import org.novau233.qbm.bot.BotEntry;
import org.novau233.qbm.processors.CommandProcessor;
import org.novau233.qbm.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.*;

public class BotManager {
    public static final List<BotConfigEntry> botEntries = new Vector<>();
    public static final List<Bot> bots = new Vector<>();
    public static final Set<BotEntry> entries = ConcurrentHashMap.newKeySet();
    public static final Logger LOGGER = LogManager.getLogger();
    private static final Executor executor = Executors.newCachedThreadPool();
    public static final MessageManager multiSender= new MessageManager();

    public static void init(){
        try {
            final File configFolder = new File("configs");
            if (!configFolder.exists()){
                configFolder.mkdir();
            }
            File file = new File(configFolder,"bots.json");
            if (!file.exists()) {
                LOGGER.error("Bot config not found!Exiting..");
                System.exit(0);
            }
            FileInputStream stream = new FileInputStream(file);
            byte[] buffer = Utils.readInputStreamToByte(stream);
            stream.close();
            BotConfigEntryArray array = BotConfigEntryArray.botConfigEntryArrayFromString(new String(buffer));
            CompletableFuture.allOf(Arrays.stream(array.entries)
                    .map(value->CompletableFuture.supplyAsync(()->{
                        BotEntry entry = new BotEntry() {
                            @Override
                            public void processEvent(Event event) {
                                if (event instanceof GroupMessageEvent){
                                    CommandProcessor.processAsync(((GroupMessageEvent) event));
                                }
                                if (event instanceof NewFriendRequestEvent){
                                    NewFriendRequestEvent event1 = ((NewFriendRequestEvent) event);
                                    event1.accept();
                                }
                            }
                        };
                        entry.runBot(value);
                        return entry;
                    }).whenComplete((value1,cause)->{
                        if (cause==null){
                            bots.add(value1.getBot());
                            entries.add(value1);
                            botEntries.add(value1.getConfigEntry());
                        }else{
                            cause.printStackTrace();;
                        }
                    })).toArray(CompletableFuture[]::new)).join();
            multiSender.doInit(entries);
        }catch (Exception e){
            LOGGER.error(e);
        }
    }

    private static class BotConfigEntryArray{
        private static final Gson GSON = new Gson();
        public BotConfigEntry[] entries;

        public String getJson(){
            return GSON.toJson(this);
        }

        public static BotConfigEntryArray botConfigEntryArrayFromString(String jsonIn){
            return GSON.fromJson(jsonIn,BotConfigEntryArray.class);
        }
    }
}
