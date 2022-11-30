package org.novau233.qbm.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.novau233.qbm.commands.Command;
import org.novau233.qbm.commands.JavaScriptCommand;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class JavaScriptCommandLoader {
    private static final Set<Invocable> loadedScripts = ConcurrentHashMap.newKeySet();
    private static final Set<Command> registedJSCommands = ConcurrentHashMap.newKeySet();
    private static final Logger logger = LogManager.getLogger();
    private static File currentScriptDir;

    public static void loadSingleJavaScript(byte[] dataArray){
        try{
            final ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            engine.eval(new String(dataArray, StandardCharsets.UTF_8));
            Invocable inv = (Invocable) engine;
            registedJSCommands.add(new JavaScriptCommand(inv));
            loadedScripts.add(inv);
        }catch (Exception e){
            logger.error("Error in loading script!",e);
            e.printStackTrace();
        }
    }

    public static synchronized Set<Command> getRegistedJSCommands(){
        return registedJSCommands;
    }

    public synchronized static void loadAll(File scriptsDir){
        if (scriptsDir.exists()){
            CompletableFuture.allOf(Arrays.stream(scriptsDir.listFiles())
                    .map(singleFile-> CompletableFuture.runAsync(()->{
                        try {
                            if (singleFile.getName().endsWith(".js")){
                                byte[] read = Files.readAllBytes(singleFile.toPath());
                                loadSingleJavaScript(read);
                            }
                        }catch (Exception e){
                            logger.error("Error in reading file!",e);
                            e.printStackTrace();
                        }
                    })).toArray(CompletableFuture[]::new)).join();
            logger.info("Load {} javascripts!",loadedScripts.size());
            currentScriptDir = scriptsDir;
        }
    }

    public synchronized static Set<Invocable> getLoadedScripts(){
        return loadedScripts;
    }

    public synchronized static void reload(){
        registedJSCommands.clear();
        loadedScripts.clear();
        loadAll(currentScriptDir);
    }
}
