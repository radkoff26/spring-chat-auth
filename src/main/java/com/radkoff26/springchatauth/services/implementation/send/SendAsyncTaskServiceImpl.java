package com.radkoff26.springchatauth.services.implementation.send;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.radkoff26.springchatauth.domain.body.response.AsyncTaskCode;
import com.radkoff26.springchatauth.domain.body.response.SendAsyncTaskBody;
import com.radkoff26.springchatauth.services.declaration.send.SendAsyncTaskService;

@Service
public class SendAsyncTaskServiceImpl implements SendAsyncTaskService {
    private final RestTemplate restTemplate;
    @Value("${async-task-url}")
    private String sendAsyncTaskUrl;

    public SendAsyncTaskServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<Void> sendAsyncTask(AsyncTaskCode taskCode, Map<?, ?> args) {
        SendAsyncTaskBody body = new SendAsyncTaskBody(taskCode.getCode(), args);
        return restTemplate.postForEntity(sendAsyncTaskUrl, body, void.class);
    }
}
