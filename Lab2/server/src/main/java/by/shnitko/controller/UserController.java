package by.shnitko.controller;

import by.shnitko.controller.dto.UserCreds;
import by.shnitko.service.UserService;
import by.shnitko.util.Encryptor;
import by.shnitko.util.PublicKeyCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Slf4j
@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController extends AbstractController {
    private final UserService userService;
    private final ObjectMapper objectMapper;

//    @PostMapping(produces = APPLICATION_JSON_VALUE)
//    public String createUser(@RequestBody UserCreatingInfo creatingInfo) {
//        userService.create(creatingInfo);
//        return "OK";
//    }

    @PostMapping(path = "/login", produces = TEXT_PLAIN_VALUE)
    public String login(@RequestBody UserCreds creds) {
        log.info("login {}", creds);
        var userId = userService.getUser(creds).getId().toString();
        log.info(new String(userId.getBytes(StandardCharsets.UTF_8)));
        return Encryptor.encryptRSA(userId, creds.getPublicKey());
    }

    @PostMapping(path = "/logout/{userId}", produces = APPLICATION_JSON_VALUE)
    public String logout(@PathVariable UUID userId) {
        log.info("logout {}", userId);
        PublicKeyCache.deleteKey(userId);
        return "OK";
    }
}
