package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, long id) {
        userStorage.getUser(id);
        Item item = ItemMapper.toItem(itemDto);
        return itemStorage.createItem(item, id);
    }

    @Override
    public ItemDto updateItem(ItemDto newItemDto, long userId, long itemId) {
        userStorage.getUser(userId);
        Item item = itemStorage.getItem(itemId);
        if (item.getOwner() != userId) {
            throw new ValidationException("Вы не владелец этой вещи");
        }
        return itemStorage.updateItem(newItemDto, userId, itemId);
    }

    @Override
    public ItemDto getItem(long itemId) {
        return ItemMapper.toItemDto(itemStorage.getItem(itemId));
    }

    @Override
    public List<ItemDto> getAllItems(long userId) {
        userStorage.getUser(userId);
        return itemStorage.getAllItems(userId);
    }

    @Override
    public List<ItemDto> searchItems(String text, long userId) {
        userStorage.getUser(userId);
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemStorage.searchItems(text);
    }
}
