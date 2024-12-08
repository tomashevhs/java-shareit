package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto createUser(UserDto userDto) {
        return userStorage.createUser(userDto);
    }

    @Override
    public UserDto updateUser(long userId, UserDto newUserDto) {
        return userStorage.updateUser(userId, newUserDto);
    }

    @Override
    public UserDto getUser(long userId) {
        return userStorage.getUser(userId);
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
    }
}
