package com.leaning.JournalApp.repository;

import com.leaning.JournalApp.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Object> {
    User findByUserName(String userName);
    void deleteByUserName(String userName);
}
