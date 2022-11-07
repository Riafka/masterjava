package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.thymeleaf.util.StringUtils;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.dao.EmailResultDao;
import ru.javaops.masterjava.service.mail.model.EmailResult;

import java.util.List;

@Slf4j
public class MailSender {
    private static final Config mailConfig = Configs.getConfig("mail.conf", "mail");
    private static final EmailResultDao emailResultDao = DBIProvider.getDao(EmailResultDao.class);

    static {
        DBIMailProvider.initDBI();
    }

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) throws EmailException {
        log.info("Send mail to '" + to + "' cc '" + cc + "' subject '" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        Email email = new SimpleEmail();
        email.setHostName(mailConfig.getString("host"));
        email.setSmtpPort(mailConfig.getInt("port"));
        email.setAuthenticator(new DefaultAuthenticator(mailConfig.getString("username"), mailConfig.getString("password")));
        email.setSSLOnConnect(mailConfig.getBoolean("useSSL"));
        email.setStartTLSEnabled(mailConfig.getBoolean("useTLS"));
        email.setDebug(mailConfig.getBoolean("debug"));

        email.setFrom(mailConfig.getString("fromName"));
        email.setSubject(subject);
        email.setMsg(body);
        for (Addressee addressee : to) {
            email.addTo(addressee.getEmail());
        }
        for (Addressee addressee : cc) {
            email.addCc(addressee.getEmail());
        }

        val result = email.send();

        emailResultDao.insert(
                new EmailResult(
                        StringUtils.join(to, ","),
                        StringUtils.join(cc, ","),
                        subject,
                        body,
                        result
                )
        );
    }
}
