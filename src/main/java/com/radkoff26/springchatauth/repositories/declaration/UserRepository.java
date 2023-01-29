package com.radkoff26.springchatauth.repositories.declaration;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.radkoff26.springchatauth.domain.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByLogin(String login);

}
