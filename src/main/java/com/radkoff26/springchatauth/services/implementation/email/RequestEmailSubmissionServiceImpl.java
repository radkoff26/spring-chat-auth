package com.radkoff26.springchatauth.services.implementation.email;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.radkoff26.springchatauth.domain.body.response.AsyncTaskCode;
import com.radkoff26.springchatauth.domain.entity.User;
import com.radkoff26.springchatauth.repositories.declaration.RedisRepository;
import com.radkoff26.springchatauth.services.declaration.email.RequestEmailSubmissionService;
import com.radkoff26.springchatauth.services.declaration.send.SendAsyncTaskService;
import com.radkoff26.springchatauth.services.declaration.user.UserService;
import com.radkoff26.springchatauth.utils.CodeGenerator;

@Service
public class RequestEmailSubmissionServiceImpl implements RequestEmailSubmissionService {
    private final RedisRepository<Long, String> userEmailSubmissionRepository;
    private final SendAsyncTaskService sendAsyncTaskService;
    private final UserService userService;
    private final CodeGenerator codeGenerator;
    @Value("${security.email-submit-code-length}")
    private int codeLength;

    public RequestEmailSubmissionServiceImpl(RedisRepository<Long, String> userEmailSubmissionRepository, SendAsyncTaskService sendAsyncTaskService, UserService userService, CodeGenerator codeGenerator) {
        this.userEmailSubmissionRepository = userEmailSubmissionRepository;
        this.sendAsyncTaskService = sendAsyncTaskService;
        this.userService = userService;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public ResponseEntity<Void> requestEmailSubmission(long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (user.isEmailVerified()) {
            return ResponseEntity.status(403).build();
        }
        String code = codeGenerator.generateTokenCode(codeLength);
        Map<String, String> map = new HashMap<>(3);
        map.put("userId", String.valueOf(userId));
        map.put("email", user.getEmail());
        map.put("code", code);
        ResponseEntity<Void> response = sendAsyncTaskService.sendAsyncTask(AsyncTaskCode.SEND_EMAIL_SUBMISSION, map);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        userEmailSubmissionRepository.add(userId, code);
        return ResponseEntity.ok().build();
    }
}
