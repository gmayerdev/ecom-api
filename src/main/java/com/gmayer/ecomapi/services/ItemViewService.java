package com.gmayer.ecomapi.services;

import com.gmayer.ecomapi.domains.ItemView;
import com.gmayer.ecomapi.repositories.ItemViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemViewService {

    @Autowired
    private ItemViewRepository itemViewRepository;

    private final int VIEW_COUNT_MIN_THRESHOLD = 60;

    /**
     * Create Item View
     * @param itemId Id of Item
     */
    public ItemView createItemView(UUID itemId){
        ItemView itemView = new ItemView();
        itemView.setItemId(itemId);
        itemView.setViewedOn(LocalDateTime.now());
        itemViewRepository.save(itemView);
        return itemView;
    }

    /**
     * Counts the amount of views an item has in the last hour
     * @param itemId Id of Item
     * @return Amount of ItemViews
     */
    public Integer getLastHourItemViewCountByItemId(UUID itemId){
        // Find all ItemView By Item Id created in the last hour
        List<ItemView> itemViews = itemViewRepository.findAllByItemId(itemId);

        LocalDateTime lastHourFromNow = LocalDateTime.now().minusMinutes(VIEW_COUNT_MIN_THRESHOLD);

        List<ItemView> filteredItemViews = itemViews.stream().filter(itemView ->
                itemView.getViewedOn().isAfter(lastHourFromNow)
        ).collect(Collectors.toList());

        return filteredItemViews.size();
    }
}
