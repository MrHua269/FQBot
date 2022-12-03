package org.novau233.qbm.processors;

import org.novau233.qbm.commands.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
public class CommandManager {
    public static final Set<Command> registedSystemCommands = ConcurrentHashMap.newKeySet();

    static{
        registedSystemCommands.add(new TestCommand());
        registedSystemCommands.add(new ScheduleCommand());
        registedSystemCommands.add(new RandomPicCommand());
        registedSystemCommands.add(new GetOutCommand());
        registedSystemCommands.add(new ReloadCommand());
        registedSystemCommands.add(new RandomPic2Command());
        registedSystemCommands.add(new Random3PicCommand());
    }
}
