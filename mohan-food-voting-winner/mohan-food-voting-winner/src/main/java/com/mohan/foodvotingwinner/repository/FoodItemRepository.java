package com.mohan.foodvotingwinner.repository;

import com.mohan.foodvotingwinner.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
}
