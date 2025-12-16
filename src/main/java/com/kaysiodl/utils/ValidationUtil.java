package com.kaysiodl.utils;

import com.kaysiodl.dto.ResultsRequestDTO;

public class ValidationUtil {

    public static void validateUser(String login, String password) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Логин обязателен");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Пароль обязателен");
        }

    }

    public static void validatePoint(ResultsRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Данные точки отсутствуют");
        }

        if (dto.getX() == null) {
            throw new IllegalArgumentException("X обязателен");
        }

        if (dto.getY() == null) {
            throw new IllegalArgumentException("Y обязателен");
        }

        if (dto.getR() == null) {
            throw new IllegalArgumentException("R обязателен");
        }
    }
}

