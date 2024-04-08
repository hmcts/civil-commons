package uk.gov.hmcts.reform.civil.notify;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationClient notificationClient;

    public void sendMail(
        String targetEmail,
        String emailTemplate,
        Map<String, String> parameters,
        String reference
    ) {
        try {
            SendEmailResponse sendEmailResponse = notificationClient.sendEmail(
                emailTemplate,
                targetEmail,
                parameters,
                reference
            );
            if (sendEmailResponse != null) {
                log.info(
                    "Email Response body:: {} reference {}",
                    sendEmailResponse.getBody(),
                    sendEmailResponse.getReference()
                );
            }
        } catch (NotificationClientException e) {
            log.info("Notification Service error {}", e.getMessage());
            throw new NotificationException(e);
        }
    }

    public void sendLetter(String letterTemplate, Map<String, ?> personalisation, String reference) {
        try {
            notificationClient.sendLetter(letterTemplate, personalisation, reference);
        } catch (NotificationClientException e) {
            throw new NotificationException(e);
        }
    }
}
