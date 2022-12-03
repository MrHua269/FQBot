package org.novau233.qbm.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.novau233.qbm.utils.Utils;
import org.novau233.qbm.utils.Woc2Util;
import java.io.ByteArrayInputStream;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Random3PicCommand implements Command{
    private final Queue<String> lastWocUrls = new ConcurrentLinkedQueue<>();

    @Override
    public String getHead() {
        return "rpic3";
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
       if (this.lastWocUrls.isEmpty()){
           this.lastWocUrls.addAll(Objects.requireNonNull(Woc2Util.getNewWocPicList()));
       }
       final String currentPolledUrl = this.lastWocUrls.poll();
       Contact.sendImage(target, new ByteArrayInputStream(Objects.requireNonNull(Utils.getBytes(currentPolledUrl))));
    }
}
