package com.radkoff26.springchatauth.services.declaration.email;

import org.springframework.http.ResponseEntity;

public interface RequestEmailSubmissionService {
    ResponseEntity<Void> requestEmailSubmission(long userId);
}
