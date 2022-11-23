package org.novau233.qbm.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.novau233.qbm.manager.BotManager;
import org.novau233.qbm.manager.MessageManager;

public interface Command {
    MessageManager multiSender = BotManager.multiSender;
    public String getHead();
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event);
}
