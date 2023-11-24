package com.bedmanagement.bedtracker;

import com.bedmanagement.bedtracker.io.entity.User;
import com.bedmanagement.bedtracker.io.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialUserSetUp {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

@EventListener
@Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){

    User user=userRepository.findByEmail("testing@gmail.com");
    if(user==null) {
        User adminUser = new User();
        adminUser.setIdentifier("12345");
        adminUser.setFirstName("admin");
        adminUser.setLastName("user");
        adminUser.setEmail("testing@gmail.com");
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345"));
        adminUser.setRole("ROLE_ADMIN");
        userRepository.save(adminUser);
    }




    }

}
