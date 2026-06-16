package fr.fil.masterclass_back.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(String toEmail, String token) {
        try {
            String confirmLink = "http://localhost:8080/api/users/confirm?token=" + token;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Confirmation de votre compte");
            helper.setText("""
                <html>
                <body>
                    <h2>Bienvenue !</h2>
                    <p>Cliquez sur le bouton ci-dessous pour confirmer votre inscription :</p>
                    <a href="%s" style="
                        background-color: #00786f;
                        color: white;
                        padding: 12px 24px;
                        text-decoration: none;
                        border-radius: 6px;
                        display: inline-block;
                    ">
                        Confirmer mon compte
                    </a>
                    <p style="color: #999; font-size: 12px; margin-top: 16px;">
                        Ou copiez ce lien : <a href="%s">%s</a>
                    </p>
                </body>
                </html>
            """.formatted(confirmLink, confirmLink, confirmLink), true); // true = HTML

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi du mail", e);
        }
    }
}
