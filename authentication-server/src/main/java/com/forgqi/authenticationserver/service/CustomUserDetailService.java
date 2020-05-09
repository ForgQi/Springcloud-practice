package com.forgqi.authenticationserver.service;

import com.forgqi.authenticationserver.repository.UserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUserName(username).or(() -> {
            if (StringUtils.isNumeric(username)) {
                return userRepository.findById(Long.valueOf(username));
            }
            return java.util.Optional.empty();
        }).orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));
    }
}
