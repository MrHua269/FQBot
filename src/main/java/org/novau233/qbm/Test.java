package org.novau233.qbm;

import org.novau233.qbm.utils.Utils;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        String json = new String(Utils.getBytes("https://yingtall.com/wp-json/wp/v2/posts?page=1"));
        String[] split = json.split("src=");
        List<String> done = new ArrayList<>();
        int counter = 0;
        for (String singlePart :split){
            if (counter > 0){
                String removeHead = singlePart.substring(2);
                final String retainArg = removeHead.substring(0,removeHead.indexOf("\\\""));
                done.add(URLDecoder.decode(retainArg,"UTF-8").replace("\\/\\/","//").replace("\\/","/"));
            }
            ++counter;
        }
    }
}
