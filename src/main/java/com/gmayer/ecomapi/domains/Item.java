package com.gmayer.ecomapi.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity(name = "item")
public class Item {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String description;
    private Double price;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ItemView> itemViews;
}
