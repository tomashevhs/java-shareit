package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.EmailValidException;
import ru.practicum.shareit.validation.NotFoundException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public UserDto createUser(UserDto userDto) {
        if (emails.contains(userDto.getEmail())) {
            throw new EmailValidException("Email уже используется");
        }
        User user = UserMapper.toUser(userDto);
        user.setId(getNextId());
        log.info("Создан объект User - {}", user);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(long id, UserDto newUserDto) {
        User newUser = UserMapper.toUser(newUserDto);
        if (users.containsKey(id) && newUser.getEmail() != null && !emails.contains(newUser.getEmail())) {
            User oldUser = users.get(id);
            oldUser.setEmail(newUser.getEmail());
            oldUser.setName(newUser.getName());
            return UserMapper.toUserDto(oldUser);
        } else if (users.containsKey(id) && newUser.getEmail() == null) {
            User oldUser = users.get(id);
            oldUser.setName(newUser.getName());
            return UserMapper.toUserDto(oldUser);
        } else if (emails.contains(newUser.getEmail())) {
            throw new EmailValidException("Юзер с таким email уже создан.");
        } else {
            throw new NotFoundException("Юзер с id = " + newUser.getId() + " не найден");
        }
    }

    @Override
    public UserDto getUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Такого пользователя нет");
        }
        return UserMapper.toUserDto(users.get(userId));
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
