package org.novau233.qbm;

import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.novau233.qbm.manager.BotManager;
import org.novau233.qbm.manager.ConfigManager;
import org.novau233.qbm.processors.CommandProcessor;
import org.novau233.qbm.utils.StringUtil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConfigManager.init();
        BotManager.init();
        try (Scanner scanner = new Scanner(System.in)){
            while (scanner.hasNext()){
                String read = scanner.nextLine();
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.add(read);
                BotManager.multiSender.send(builder.build(), ConfigManager.CONFIG_FILE_READ.getListeningGroup());
            }
        }
    }
}
