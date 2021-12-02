package com.gmayer.ecomapi.repositories;

import com.gmayer.ecomapi.domains.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {}
