package com.radkoff26.springchatauth.services.implementation.email;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.radkoff26.springchatauth.domain.body.request.EmailSubmissionBody;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;
import com.radkoff26.springchatauth.services.declaration.email.SubmitEmailService;
import com.radkoff26.springchatauth.services.declaration.user.UserService;

@Service
public class SubmitEmailServiceImpl implements SubmitEmailService {
    private final RedisRepository<Long, String> userEmailSubmissionRepository;
    private final UserService userService;

    public SubmitEmailServiceImpl(RedisRepository<Long, String> userEmailSubmissionRepository, UserService userService) {
        this.userEmailSubmissionRepository = userEmailSubmissionRepository;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Void> submitEmail(EmailSubmissionBody emailSubmissionBody) {
        long userId = emailSubmissionBody.userId();
        String code = userEmailSubmissionRepository.get(userId); // TODO: make it expired after some time
        if (code != null && code.equals(emailSubmissionBody.submissionCode())) {
            userEmailSubmissionRepository.delete(userId);
            userService.submitUserEmail(userId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }
}
