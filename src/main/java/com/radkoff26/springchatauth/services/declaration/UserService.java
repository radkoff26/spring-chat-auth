package com.radkoff26.springchatauth.services.declaration;

import com.radkoff26.springchatauth.domain.entity.User;

public interface UserService {
    User getUserByLogin(String login);
    User getUserById(long id);
}
