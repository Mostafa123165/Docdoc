package com.spring.Docdoc.service;

import com.spring.Docdoc.dto.NotificationEmailDto;
import com.spring.Docdoc.entity.Otp;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.mapper.OtpMapper;
import com.spring.Docdoc.repository.OtpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Service
@Slf4j
@AllArgsConstructor
public class mailService {

    private final JavaMailSender mailSender;
    private final OtpRepository otpRepository;
    private final OtpMapper otpMapper;
    private TemplateEngine templateEngine;

    public void sendOtp(NotificationEmailDto notificationEmail , User user,Long oldOtpId) {
        String otp = generateOTP() ;

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage) ;
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(buildContentMail(otp),true);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent successfully");
        }catch (MailException e) {
            throw new MailSendException(e.toString());
        }

        Otp otpEntity = otpMapper.mapToOtp(user,otp) ;

        // in case : refresh otp
        otpEntity.setId(oldOtpId);

        save(otpEntity);
    }


    @Async
    public void save(Otp otp) {
        otpRepository.save(otp) ;
    }

    public String generateOTP() {
        Random random = new Random() ;
        int otp  = random.nextInt(100000,999999) ;
        return String.valueOf(otp) ;
    }


    public Otp getByUser(User user) {

        return otpRepository.findByUser(user) ;
    }


    private String buildContentMail(String otp) {
        Context context = new Context() ;
        context.setVariable("otp",otp);
        return templateEngine.process("OtpTemplate",context);
    }

}
