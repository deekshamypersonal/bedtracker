package com.bedmanagement.bedtracker.io.repository;

import com.bedmanagement.bedtracker.io.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    @Query("select user from User user where user.email=:email")
    User findByEmail(@Param("email") String email);

    User findUserByEmailVerificationToken(String token);

}
