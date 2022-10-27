package com.itheima.reggie.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class mail {
    static Properties pro = new Properties();

    // 通过静态代码块加载配置文件
    static {
        InputStream inputStream = mail.class.getClassLoader().getResourceAsStream("mail.properties");
        try {
            pro.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        sendMail("test", "1234", "@qq.com");
//    }

    public static boolean sendMail(String subject, String content, String to) {
        //通过配置文件获取Session对象
        Session session = Session.getDefaultInstance(pro);
        // 设置日志输出
        session.setDebug(false);
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        try {
            //设置发件人
            message.setFrom(new InternetAddress("1792181243@qq.com"));
            //设置收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //设置邮件主题
            message.setSubject(subject);
            //设置内容
            message.setContent(content, "text/html;charset=UTF-8");
            //设置发送时间
            message.setSentDate(new Date());
            //保存改变
            message.saveChanges();
            //创建Transport对象
            Transport transport = session.getTransport();
            //获取连接到邮件服务器
            transport.connect("1792181243@qq.com", "onuqbiqbygudejhc");
            //发送信息
            transport.sendMessage(message, message.getAllRecipients());
            //关闭连接
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return true;
    }


}
