package com.radkoff26.springchatauth.services.declaration.user;

import com.radkoff26.springchatauth.domain.entity.User;

public interface UserService {
    User getUserByLogin(String login);
    User getUserById(long id);
    void submitUserEmail(long id);
}
