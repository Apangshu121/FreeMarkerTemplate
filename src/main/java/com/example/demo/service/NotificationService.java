package com.example.demo.service;

import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.entity.UserNotification;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserNotificationRepository;
import com.example.demo.repository.UserRepository;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {


    private final Configuration freemarkerConfig;
    private final UserNotificationRepository userNotificationrepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Scheduled(cron = "0 */1 * * * *") // run every 1 minute
    public void generateNotifications() throws IOException, TemplateException {

        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        List<Transaction> transactions = transactionRepository.findByDateAfterAndStatus(oneMinuteAgo,"approved");
        System.out.println(transactions.size());

        for(Transaction transaction:transactions)
        {
            String userId = transaction.getUser1();
            String vendorId = transaction.getUser2();

            User user = userRepository.findByUserId(userId).orElseThrow();
            User vendor = userRepository.findByUserId(vendorId).orElseThrow();
            generateNotificationForUser(user, vendor, transaction);
            generateNotificationForVendor(user, vendor, transaction);
        }
    }

    private void generateNotificationForVendor(User user, User vendor, Transaction transaction) throws IOException, TemplateException {
        Map<String,Object> model = new HashMap<>();
        model.put("user",user);
        model.put("vendor",vendor);
        model.put("transaction", transaction);

        Template template = freemarkerConfig.getTemplate("vendor_transaction_notification.ftl");
        StringWriter writer = new StringWriter();
        template.process(model, writer);
        String notification = writer.toString();

        var vendorNotification = UserNotification.builder()
                .username(vendor.getName())
                .notification(notification)
                .build();

        System.out.println(vendorNotification);
        userNotificationrepository.save(vendorNotification);
    }

    private void generateNotificationForUser(User user, User vendor, Transaction transaction) throws IOException, TemplateException {
        Map<String,Object> model = new HashMap<>();
        model.put("user",user);
        model.put("vendor",vendor);
        model.put("transaction", transaction);

        Template template = freemarkerConfig.getTemplate("user_transaction_notification.ftl");
        StringWriter writer = new StringWriter();
        template.process(model, writer);
        String notification = writer.toString();

        var userNotification = UserNotification.builder()
                .username(user.getName())
                .notification(notification)
                .build();

        System.out.println(userNotification);
        userNotificationrepository.save(userNotification);
    }
}
