package com.gmayer.ecomapi.services;

import com.gmayer.ecomapi.domains.Item;
import com.gmayer.ecomapi.dtos.ItemDto;
import com.gmayer.ecomapi.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemViewService itemViewService;

    @Autowired
    private ItemRepository itemRepository;

    private final static Integer ITEM_VIEW_THRESHOLD = 10;
    private final static Integer PERCENTAGE_INCREASE = 10;

    /**
     * Find all Items
     * @return List of ItemDto
     *
     */
    //TODO: Implement Pagination
    public List<ItemDto> findAllItems(){
        List<Item> items = itemRepository.findAll();

        List<ItemDto> itemDtoList = items.stream().map(item ->
            recalculateItemPrice(item)
        ).collect(Collectors.toList());

        return itemDtoList;
    }

    /**
     * Find Item By Id
     * @param id Item Id
     * @return ItemDto
     *
     */
    public ItemDto findById(UUID id){
        // Find Item by Id
        Optional<Item> itemOptional = itemRepository.findById(id);

        // Handle Item does not exist
        if(!itemOptional.isPresent()){
            throw new NoSuchElementException("Item does not exist");
        }
        Item item = itemOptional.get();

        // Create Item View Count
        itemViewService.createItemView(item);

        // Count Item Views and recalculate item price
        ItemDto itemDto = recalculateItemPrice(item);
        return itemDto;
    }

    /**
     * Create Item
     * @param itemDto ItemDto
     *
     * @return ItemDto
     *
     */
    public ItemDto createItem(ItemDto itemDto){
        Item newItem = fromDtoToEntity(itemDto);
        newItem = itemRepository.save(newItem);
        itemDto.setItemId(newItem.getId());
        return itemDto;
    }

    /**
     * Recalculate Item Price
     * @param item Item
     * @return ItemDto
     *
     */
    private ItemDto recalculateItemPrice(Item item){
        // Count Item Views
        Integer totalItemViews = itemViewService.getLastHourItemViewCountByItemId(item.getId());

        // Recalculate Item Price based on views
        ItemDto itemDto = fromEntityToDto(item);
        if(totalItemViews >= ITEM_VIEW_THRESHOLD){
            Double newPrice = (item.getPrice() / PERCENTAGE_INCREASE) + item.getPrice();
            itemDto.setItemPrice(newPrice);
        }
        return itemDto;
    }

    /**
     * A utility method to convert a DTO of Item entity to the Item entity
     * @param itemDto ItemDto
     *
     * @return Item
     */
    private Item fromDtoToEntity(ItemDto itemDto){
        Item item = new Item();
        item.setName(itemDto.getItemName());
        item.setDescription(itemDto.getItemDescription());
        item.setPrice(itemDto.getItemPrice());
        return item;
    }

    /**
     * A utility method to convert an Item entity to a DTO
     * @param item Item
     * @return ItemDto
     *
     */
    private ItemDto fromEntityToDto(Item item){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(item.getId());
        itemDto.setItemName(item.getName());
        itemDto.setItemDescription(item.getDescription());
        itemDto.setItemPrice(item.getPrice());
        return itemDto;
    }
}
