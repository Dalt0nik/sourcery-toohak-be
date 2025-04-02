package com.sourcery.km.repository;

import com.sourcery.km.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    @Insert("INSERT INTO app_users (id, auth0_id, email, username) VALUES (#{id}, #{auth0_id}, #{email}, #{username})")
    void insertUser(User user);

    @Select("SELECT * FROM app_users WHERE auth0_id=#{auth0_id}")
    List<User> getUserWithAuth0ID(String auth0Id);

    @Select("SELECT * FROM app_users WHERE id=#{id}")
    Optional<User> getUserById(UUID id);
}
