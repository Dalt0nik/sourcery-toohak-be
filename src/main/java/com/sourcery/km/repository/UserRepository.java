package com.sourcery.km.repository;

import com.sourcery.km.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    @Insert("INSERT INTO app_users (id, auth0_id, email, username) VALUES (#{id}, #{auth0Id}, #{email}, #{username})")
    void insertUser(User user);

    @Select("SELECT * FROM app_users WHERE auth0_id=#{auth0Id}")
    Optional<User> getUserWithAuth0ID(String auth0Id);
}
