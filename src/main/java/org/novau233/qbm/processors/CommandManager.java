package org.novau233.qbm.processors;

import org.novau233.qbm.commands.Command;
import org.novau233.qbm.commands.RandomPicCommand;
import org.novau233.qbm.commands.ScheduleCommand;
import org.novau233.qbm.commands.TestCommand;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
public class CommandManager {
    public static final Set<Command> registedCommands = ConcurrentHashMap.newKeySet();

    static{
        registedCommands.add(new TestCommand());
        registedCommands.add(new ScheduleCommand());
        registedCommands.add(new RandomPicCommand());
    }
}
