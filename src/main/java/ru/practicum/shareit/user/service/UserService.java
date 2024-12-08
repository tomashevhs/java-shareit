package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUser(long userId);

    UserDto updateUser(long id, UserDto newUserDto);

    void deleteUser(long userId);
}
