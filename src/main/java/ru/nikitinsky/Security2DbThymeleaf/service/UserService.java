package ru.nikitinsky.Security2DbThymeleaf.service;

import ru.nikitinsky.Security2DbThymeleaf.dto.UserDto;
import ru.nikitinsky.Security2DbThymeleaf.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
