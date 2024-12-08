package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    ItemDto createItem(Item item, long userId);

    ItemDto updateItem(ItemDto newItemDto, long userId, long itemId);

    Item getItem(long itemId);

    List<ItemDto> getAllItems(long userId);

    List<ItemDto> searchItems(String text);
}
