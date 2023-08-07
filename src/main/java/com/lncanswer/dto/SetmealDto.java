package com.lncanswer.dto;


import com.lncanswer.entitly.Setmeal;
import com.lncanswer.entitly.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
