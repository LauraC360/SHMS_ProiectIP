package com.restservice.authentication.sendgrid;

import com.restservice.authentication.model.User;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.concurrent.ThreadLocalRandom;

@Service
public class EmailService {

    private User user;
    private int code;

    @Value("${sendgrid.api.key}")
    private String apiKey;

    public void setUser(User user) {
        this.user = user;
    }

    public int getCode() {
        return code;
    }

    public String sendEmail() {
        code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        String fromEmail = "pauleusebiubejan2003@gmail.com";
        String toEmail = user.getEmail();

        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Mail mail = new Mail();
        mail.setFrom(from);

        Personalization personalization = new Personalization();
        personalization.addTo(to);

        mail.setTemplateId("d-b1ba818c3e83475086f50aaf4284ea5f");

        personalization.addDynamicTemplateData("name", user.getUsername());
        personalization.addDynamicTemplateData("code", String.valueOf(code));
        mail.addPersonalization(personalization);


        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            return response.getBody();
        } catch (Exception ex) {
            return "Error sending email: " + ex.getMessage();
        }
    }
        /*setRandomNum();
        String fromEmail = "pauleusebiubejan2003@gmail.com";
        String toEmail = user.getEmail();
        String subject = "Email Confirmation";
        String contentText = "Hello, \n\nFor registering please introduce the following code  " +
                " Your registration code is: "+ code +"\n\nFrom: SHMS Team";

        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            return response.getBody();
        } catch (Exception ex) {
            return "Error sending email: " + ex.getMessage();
        }
    }*/
}
