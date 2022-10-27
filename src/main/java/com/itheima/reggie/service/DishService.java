package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据，虚同时操作两张表dish，dish_flavors
    void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味
    DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新口味信息
    void updateWithFlavor(DishDto dishDto);
}
