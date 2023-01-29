package com.radkoff26.springchatauth.domain.body;

import java.util.Map;

public record SendEmailCodeRequestBody(String type, Map<String, String> arguments) {
}
