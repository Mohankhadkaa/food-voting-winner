package com.mohan.foodvotingwinner.repository;

import com.mohan.foodvotingwinner.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByFoodItemId(Long foodItemId);
}
