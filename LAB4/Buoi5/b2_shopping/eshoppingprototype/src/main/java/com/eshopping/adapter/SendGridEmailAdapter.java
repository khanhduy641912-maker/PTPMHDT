package com.eshopping.adapter;

public class SendGridEmailAdapter implements IEmailAdapter {
    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        System.out.println("[Email Service] Đã gửi mail tới: " + toEmail + " | Tiêu đề: " + subject);
    }
}