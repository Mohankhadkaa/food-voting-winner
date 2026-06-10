package com.mohan.foodvotingwinner.controller;

import com.mohan.foodvotingwinner.dto.FoodResult;
import com.mohan.foodvotingwinner.model.FoodItem;
import com.mohan.foodvotingwinner.service.FoodVotingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final FoodVotingService foodVotingService;

    public ApiController(FoodVotingService foodVotingService) {
        this.foodVotingService = foodVotingService;
    }

    @GetMapping("/foods")
    public List<FoodItem> foods() {
        return foodVotingService.findAllFoods();
    }

    @PostMapping("/foods")
    public FoodItem createFood(@Valid @RequestBody FoodItem foodItem) {
        return foodVotingService.saveFood(foodItem);
    }

    @DeleteMapping("/foods/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodVotingService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/votes")
    public ResponseEntity<Map<String, String>> vote(@RequestBody Map<String, String> body) {
        Long foodId = Long.valueOf(body.get("foodId"));
        int rating = Integer.parseInt(body.get("rating"));
        String comment = body.getOrDefault("comment", "");
        foodVotingService.vote(foodId, rating, comment);
        return ResponseEntity.ok(Map.of("message", "Vote saved"));
    }

    @GetMapping("/results")
    public List<FoodResult> results() {
        return foodVotingService.getResults();
    }

    @GetMapping("/winner")
    public ResponseEntity<FoodResult> winner() {
        return foodVotingService.getWinnerFood()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
