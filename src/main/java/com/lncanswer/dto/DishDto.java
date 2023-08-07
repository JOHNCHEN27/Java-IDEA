package com.lncanswer.dto;

import com.lncanswer.entitly.Dish;
import com.lncanswer.entitly.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
