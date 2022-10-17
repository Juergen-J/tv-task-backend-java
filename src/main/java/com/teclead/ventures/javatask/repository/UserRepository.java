package com.teclead.ventures.javatask.repository;

import com.teclead.ventures.javatask.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findUserById(Long id);

}
