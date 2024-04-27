package com.engSpace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.engSpace.entity.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, Long> {

}
