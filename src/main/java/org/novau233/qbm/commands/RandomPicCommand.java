package org.novau233.qbm.commands;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.apache.logging.log4j.LogManager;
import org.novau233.qbm.manager.BotManager;
import org.novau233.qbm.utils.SeXResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class RandomPicCommand implements Command{
    @Override
    public String getHead() {
        return "rpic";
    }

    @Override
    public void process(String[] args, Bot bot, Group target, GroupMessageEvent event) {
        MessageChainBuilder builder = new MessageChainBuilder();
        boolean r18 = args.length > 0 && "r18".equals(args[0]);

        String[] tags = null;
        if (args.length > 0){
            tags = new String[args.length-1];
            System.arraycopy(args, 1, tags, 0, args.length - 1);
        }

        SeXResponse response = null;
        try{
            if (tags!=null){
                response = SeXResponse.getNew(r18,tags);
            }else{
                response = SeXResponse.getNew(r18);
            }
        }catch (Exception e){
            LogManager.getLogger().error(e);
        }

        try{
            if (response.getData().length > 0){
                SeXResponse.Data[] dataArray = response.getData();
                for (SeXResponse.Data data : dataArray){
                    ByteArrayInputStream stream = new ByteArrayInputStream(data.urls.getBytes());
                    Contact.sendImage(target,stream);
                }
            }else{
                builder.add("指定图片未找到");
            }
        }catch (Exception e){
            builder.add("API Time out.Please try again"+"\n");
            builder.add("API连接超时,请重试");
        }
    }
}
