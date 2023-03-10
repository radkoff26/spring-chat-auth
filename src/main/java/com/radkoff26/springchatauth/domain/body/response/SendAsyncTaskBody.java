package com.radkoff26.springchatauth.domain.body.response;

import java.util.Map;

public record SendAsyncTaskBody(String type, Map<?, ?> arguments) {
}
