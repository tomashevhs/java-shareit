package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, long id);

    ItemDto updateItem(ItemDto newItemDto, long userId, long itemId);

    ItemDto getItem(long itemId);

    List<ItemDto> getAllItems(long userId);

    List<ItemDto> searchItems(String text, long userId);
}
