package org.novau233.qbm.processors;

import org.novau233.qbm.commands.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
public class CommandManager {
    public static final Set<Command> registedCommands = ConcurrentHashMap.newKeySet();

    static{
        registedCommands.add(new TestCommand());
        registedCommands.add(new ScheduleCommand());
        registedCommands.add(new RandomPicCommand());
        registedCommands.add(new GetOutCommand());
    }
}
