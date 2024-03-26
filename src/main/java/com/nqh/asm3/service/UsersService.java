package com.nqh.asm3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nqh.asm3.pojo.Users;
import com.nqh.asm3.repository.UsersRepository;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> userInfo = usersRepository.findByEmail(email);
        return userInfo.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + email));
    }

    public String addUser(Users userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfo.setCreatedAt(java.time.LocalDateTime.now());
        userInfo.setActive(true);
        usersRepository.save(userInfo);
        return "User added successfully";
    }

    public List<Users> getAllUser() {
        List<Users> userList = usersRepository.findAll();
        return userList;
    }

    public Users getUser(Integer id) {
        if (usersRepository.findById(id).isPresent()) {
            return usersRepository.findById(id).get();
        } else {
            return null;
        }
    }

    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email).orElse(null);
    }
}