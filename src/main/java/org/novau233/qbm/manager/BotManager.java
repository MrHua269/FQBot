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
import java.io.File;
import java.io.FileInputStream;
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
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            BotConfigEntryArray array = BotConfigEntryArray.botConfigEntryArrayFromString(new String(buffer));
            CountDownLatch latch = new CountDownLatch(array.entries.length);
            for (BotConfigEntry configEntry : array.entries){
                executor.execute(()->{
                    try{
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
                        entry.runBot(configEntry);
                        bots.add(entry.getBot());
                        entries.add(entry);
                        botEntries.add(configEntry);
                    }finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
            stream.close();
            ((ThreadPoolExecutor)executor).shutdownNow();
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
