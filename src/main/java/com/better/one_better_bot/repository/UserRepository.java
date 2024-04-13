package com.better.one_better_bot.repository;

import com.better.one_better_bot.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
}
