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

//        // create initial privileges
//        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_ADVERTISEMENT");
//        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_ADVERTISEMENT");
//
//        // create initial roles
//        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege));
//        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege));
//        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
//        final Role userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);

        // create initial user
        User user = createUserIfNotFound("Admin", "admin", "admin", "admin", "admin@gmail.com",new ArrayList<>(Arrays.asList("ADMIN")));
      //  createUserIfNotFound("User", "user", "user", "user", "user@gmail.com", new ArrayList<>(Arrays.asList("USER")));
        alreadySetup = true;
    }

//    @Transactional
//    Privilege createPrivilegeIfNotFound(final String name) {
//        Privilege privilege = privilegeRepository.findByName(name);
//        if (privilege == null) {
//            privilege = new Privilege(name);
//            privilege = privilegeRepository.save(privilege);
//        }
//        return privilege;
//    }

//    @Transactional
//    Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
//        Role role = roleRepository.findByName(name);
//        if (role == null) {
//            role = new Role(name);
//        }
//        role.setPrivileges(privileges);
//        role = roleRepository.save(role);
//        return role;
//    }

    @Transactional
    User createUserIfNotFound(String firstName, String lastName, String userName, String password, String email, List<String> roles) {
        User userWithDuplicateUsername = userDao.findByUsername(userName);
        if(userWithDuplicateUsername != null) {
            log.error(String.format("Duplicate username {}", userName));
            throw new RuntimeException("Duplicate username.");
        }
        User userWithDuplicateEmail = userDao.findByEmail(email);
        if(userWithDuplicateEmail != null) {
            log.error(String.format("Duplicate email {}", email));
            throw new RuntimeException("Duplicate email.");
        }
//        Role role = new Role();
//        role.setName(RoleType.valueOf("ADMIN"));
//        role.setDescription("Admin");
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
