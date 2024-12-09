package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.validation.NotFoundException;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> itemIdKey = new HashMap<>();
    private final Map<Long, List<Item>> itemOwnerIdKey = new HashMap<>();

    @Override
    public ItemDto createItem(Item item, long userId) {
        item.setId(getNextId());
        item.setOwner(userId);
        log.info("Создан объект Item - {}", item);
        itemIdKey.put(item.getId(), item);
        List<Item> items = itemOwnerIdKey.getOrDefault(userId, new ArrayList<>());
        items.add(item);
        itemOwnerIdKey.put(item.getOwner(), items);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto newItemDto, long userId, long itemId) {
        Item item = itemIdKey.get(itemId);

        if (Objects.nonNull(newItemDto.getName()) && !newItemDto.getName().isBlank()) {
            item.setName(newItemDto.getName());
            log.debug("Присвоение нового имени вещи");
        }
        if (Objects.nonNull(newItemDto.getDescription()) && !newItemDto.getDescription().isBlank()) {
            item.setDescription(newItemDto.getDescription());
            log.debug("Присвоение нового описания вещи");
        }
        if (Objects.nonNull(newItemDto.getAvailable())) {
            item.setAvailable(newItemDto.getAvailable());
            log.debug("Присвоение нового статуса бронирования вещи");
        }
        itemIdKey.put(item.getId(), item);
        List<Item> items = itemOwnerIdKey.get(userId);
        itemOwnerIdKey.put(item.getOwner(), items.stream()
                .filter(item1 -> item1.getId() == itemId)
                .peek(item1 -> {
                    item1.setName(item.getName());
                    item1.setDescription(item.getDescription());
                    item1.setAvailable(item.getAvailable());
                })
                .toList());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Item getItem(long itemId) {
        if (!itemIdKey.containsKey(itemId)) {
            throw new NotFoundException("Вещь не найдена.");
        }
        return itemIdKey.get(itemId);
    }

    @Override
    public List<ItemDto> getAllItems(long userId) {
        return itemOwnerIdKey.get(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemIdKey.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                        item.getAvailable())
                .map(ItemMapper::toItemDto)
                .toList();
    }


    private long getNextId() {
        long currentMaxId = itemIdKey.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
