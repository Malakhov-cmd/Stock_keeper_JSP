package com.stock.keeper.stockkeeper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stock {
    private String id;

    private String name;
    private String description;
    private String date_init;
    private String img_link;
    private String index;

    private User owner;

    private List<Price> priceList = new ArrayList<>();

    private List<Purpose> purposeList = new ArrayList<>();
}
