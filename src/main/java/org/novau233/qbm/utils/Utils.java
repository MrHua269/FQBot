package org.novau233.qbm.utils;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static ByteArrayInputStream readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte['Ѐ'];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return new ByteArrayInputStream(bos.toByteArray());
    }

    public static byte[] readInputStreamToByte(InputStream inputStream) throws IOException {
        byte[] buffer = new byte['Ѐ'];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static Member searchMember(Group group,String nickName){
        for (Member member : group.getMembers()){
            if (member.getNameCard().equals(nickName)){
                return member;
            }
        }
        return null;
    }
}
