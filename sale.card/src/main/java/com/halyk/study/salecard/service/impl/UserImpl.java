package com.halyk.study.salecard.service.impl;

import com.halyk.study.salecard.entity.User;
import com.halyk.study.salecard.repository.UserRepository;
import com.halyk.study.salecard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username);

        return user.map(User::getUserDetails).orElse(null);
    }
}
