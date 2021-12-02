package com.gmayer.ecomapi.repositories;

import com.gmayer.ecomapi.domains.ItemView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemViewRepository extends JpaRepository<ItemView, UUID> {

    @Query("SELECT i FROM ItemView i WHERE i.itemId = ?1")
    List<ItemView> findAllByItemId(UUID itemId);
}
