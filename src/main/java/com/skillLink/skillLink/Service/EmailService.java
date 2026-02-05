package com.skillLink.skillLink.Service;

import com.skillLink.skillLink.Email.EmailContent;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private  final RedisService redisService;
    private final JavaMailSender mailSender;

    public JavaMailSender sendEmail(String to, String subject, EmailContent content) {
        // Implementation to send mail
      try{
          MimeMessage mimeMailMessage = mailSender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(
                  mimeMailMessage,
                  MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                  "UTF-8");
          helper.setTo(to);
          helper.setSubject(subject);
          helper.setText(content.EmailBody(),true);
          mailSender.send(mimeMailMessage);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
        return mailSender;
    }

}
