package com.pixivic.service;

import com.pixivic.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService {
    private final EmailUtil emailUtil;

    public Mono<MimeMessage> sendEmail(String emailAddr, String to, String from, String content, String link) throws MessagingException {
        return emailUtil.sendEmail(emailAddr, to, from, content, link);
    }
}
