package com.devglan.rolebasedoauth2.config;

import com.devglan.rolebasedoauth2.dao.RoleDao;
import com.devglan.rolebasedoauth2.dao.UserDao;
import com.devglan.rolebasedoauth2.model.Role;
import com.devglan.rolebasedoauth2.model.RoleType;
import com.devglan.rolebasedoauth2.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Log4j2
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }


        // create initial user
        User user = createUserIfNotFound("Admin", "admin", "admin", "admin", "admin@gmail.com",new ArrayList<>(Arrays.asList("ADMIN")));
        alreadySetup = true;
    }

    @Transactional
    User createUserIfNotFound(String firstName, String lastName, String userName, String password, String email, List<String> roles) {
        User userWithDuplicateUsername = userDao.findByUsername(userName);
        if(userWithDuplicateUsername != null) {
            log.error("Duplicate username {}", userName);
            throw new RuntimeException("Duplicate username.");
        }
        User userWithDuplicateEmail = userDao.findByEmail(email);
        if(userWithDuplicateEmail != null) {
            log.error("Duplicate email {}", email);
            throw new RuntimeException("Duplicate email.");
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(userName);
        user.setPassword(passwordEncoder.encode(password));
        List<RoleType> roleTypes = new ArrayList<>();
        roles.stream().map(role -> roleTypes.add(RoleType.valueOf(role)));
        user.setRoles(roleDao.find(roles));
        userDao.save(user);
        return user;
    }

}
