package com.devglan.rolebasedoauth2.service.impl;

import com.devglan.rolebasedoauth2.dao.RoleDao;
import com.devglan.rolebasedoauth2.dao.UserDao;
import com.devglan.rolebasedoauth2.dto.UserDto;
import com.devglan.rolebasedoauth2.exceptions.DatabaseException;
import com.devglan.rolebasedoauth2.exceptions.DuplicateInfoFoundException;
import com.devglan.rolebasedoauth2.model.Role;
import com.devglan.rolebasedoauth2.model.RoleType;
import com.devglan.rolebasedoauth2.model.User;
import com.devglan.rolebasedoauth2.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service(value = "userService")
@Log4j2
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userDao.findByUsername(userId);
        if(user == null){
            log.error("Invalid username or password.");
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Set<GrantedAuthority> grantedAuthorities = getAuthorities(user);


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<Role> roleByUserId = user.getRoles();
        final Set<GrantedAuthority> authorities = roleByUserId.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString().toUpperCase())).collect(Collectors.toSet());
        return authorities;
    }

    public List<UserDto> findAll() {
        List<UserDto> users = new ArrayList<>();
        try {
            userDao.findAll().iterator().forEachRemaining(user -> users.add(user.toUserDto()));
        }catch (Exception e){
            throw new DatabaseException("Exception occured while searching for users in databse",e);
        }
        return users;
    }

    @Override
    public User findOne(long id) {
        try {
            return userDao.findById(id).get();
        }catch (Exception e){
            throw new DatabaseException("No such user found",e);
        }
    }

    @Override
    public void delete(long id) {
        try {
            userDao.deleteById(id);
        }catch (Exception e){
            throw new DatabaseException("User not found to perform delete operation",e);
        }
    }

    @Override
    public UserDto save(UserDto userDto) {
        User userWithDuplicateUsername = userDao.findByUsername(userDto.getUsername());
        if(userWithDuplicateUsername != null && userDto.getId() != userWithDuplicateUsername.getId()) {
            log.error("Duplicate username {}", userDto.getUsername());
            throw new DuplicateInfoFoundException("Duplicate username.");
        }
        User userWithDuplicateEmail = userDao.findByEmail(userDto.getEmail());
        if(userWithDuplicateEmail != null && userDto.getId() != userWithDuplicateEmail.getId()) {
            log.error("Duplicate email {}", userDto.getEmail());
            throw new DuplicateInfoFoundException("Duplicate email.");
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        List<RoleType> roleTypes = new ArrayList<>();
        userDto.getRole().stream().map(role -> roleTypes.add(RoleType.valueOf(role)));
        user.setRoles(roleDao.find(userDto.getRole()));
        try {
            userDao.save(user);
        }catch (Exception e){
            throw new DatabaseException("Exception occured while saving into the Database", e);
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userDto;
    }
}
