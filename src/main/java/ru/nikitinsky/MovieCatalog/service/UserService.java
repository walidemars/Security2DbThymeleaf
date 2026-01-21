package ru.nikitinsky.MovieCatalog.service;

import ru.nikitinsky.MovieCatalog.dto.UserDto;
import ru.nikitinsky.MovieCatalog.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);
    
    User findUserByUsername(String username);

    void deleteUserById(Long id);

    List<UserDto> findAllUsers();
}
