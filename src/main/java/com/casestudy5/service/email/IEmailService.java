package com.casestudy5.service.email;

import com.casestudy5.service.IGenerateService;

public interface IEmailService extends IGenerateService<IEmailService> {

    void sendEmail(String to, String subject, String text);

}
