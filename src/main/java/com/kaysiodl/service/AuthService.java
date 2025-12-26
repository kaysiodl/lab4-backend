package com.kaysiodl.service;

import com.kaysiodl.database.User;
import com.kaysiodl.database.UserRepository;
import com.kaysiodl.utils.PasswordUtil;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Stateless
public class AuthService {
    @Inject
    UserRepository userRepository;

    private final Map<String, User> sessions = new ConcurrentHashMap<>();

    public void register(String login, String password) {
        if (exists(login)) {
            throw new RuntimeException("Такой пользователь уже существует");
        }
        User user = new User();
        user.setLogin(login);
        String hashed =  PasswordUtil.encryptPassword(password);
        user.setPassword(hashed);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String login(String login, String password) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
            throw new RuntimeException("Пароль неверный");
        }
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user);
        return sessionId;
    }

    public void logout(String sessionId) {
        sessions.remove(sessionId);
    }

    public User getUserBySession(String sessionId) {
        User user = sessions.get(sessionId);
        System.out.println("Found user: " + user);
        if (user == null) {
            throw new RuntimeException("Сессия не найдена или истекла");
        }
        return user;
    }

    public boolean exists(String login) {
        return userRepository.findByLogin(login) != null;
    }
}
