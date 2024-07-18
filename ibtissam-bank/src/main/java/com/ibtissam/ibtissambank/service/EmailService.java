package com.ibtissam.ibtissambank.service;

import com.ibtissam.ibtissambank.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
