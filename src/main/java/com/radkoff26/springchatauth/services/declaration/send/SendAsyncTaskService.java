package com.radkoff26.springchatauth.services.declaration.send;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.radkoff26.springchatauth.domain.body.response.AsyncTaskCode;

public interface SendAsyncTaskService {
    ResponseEntity<Void> sendAsyncTask(AsyncTaskCode taskCode, Map<?, ?> args);
}
