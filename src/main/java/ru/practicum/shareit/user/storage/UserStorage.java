package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserStorage {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(long id, UserDto newUserDto);

    UserDto getUser(long id);

    void deleteUser(long id);
}
