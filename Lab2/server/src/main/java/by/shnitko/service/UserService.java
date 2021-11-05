package by.shnitko.service;

import by.shnitko.controller.dto.UserCreatingInfo;
import by.shnitko.controller.dto.UserCreds;
import by.shnitko.error.BadCredentialsException;
import by.shnitko.error.NotFoundException;
import by.shnitko.error.UserAlreadyExistsException;
import by.shnitko.repository.UserRepository;
import by.shnitko.repository.entity.UserEntity;
import by.shnitko.util.PublicKeyCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

//    @Transactional
//    public void create(UserCreatingInfo creatingInfo) {
//        var username = creatingInfo.getName();
//        var isUserExists = userRepository.findByName(username).isPresent();
//        if(isUserExists) {
//            throw new UserAlreadyExistsException("User with username "+username+" already exists");
//        }
//        var entity = UserEntity.toInstance(creatingInfo);
//        userRepository.save(entity);
//    }

    @Transactional
    public UserEntity getUser(UserCreds creds) {
        var user = userRepository.findByName(creds.getUsername())
                .orElseThrow(() -> new NotFoundException("User "+creds.getUsername()+" not found"));
        if(!passwordEncoder.matches(creds.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Bad user credentials");
        }
        PublicKeyCache.saveKey(user.getId(), creds.getPublicKey());
        return user;
    }
}
