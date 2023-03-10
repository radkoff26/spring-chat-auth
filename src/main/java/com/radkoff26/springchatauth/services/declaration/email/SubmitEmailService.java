package com.radkoff26.springchatauth.services.declaration.email;

import org.springframework.http.ResponseEntity;

import com.radkoff26.springchatauth.domain.body.request.EmailSubmissionBody;

public interface SubmitEmailService {
    ResponseEntity<Void> submitEmail(EmailSubmissionBody emailSubmissionBody);
}
