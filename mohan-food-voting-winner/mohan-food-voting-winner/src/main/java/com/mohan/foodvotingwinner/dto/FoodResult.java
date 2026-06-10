package com.mohan.foodvotingwinner.dto;

import com.mohan.foodvotingwinner.model.FoodItem;

public class FoodResult {

    private final FoodItem foodItem;
    private final double averageRating;
    private final long voteCount;
    private final int totalScore;

    public FoodResult(FoodItem foodItem, double averageRating, long voteCount, int totalScore) {
        this.foodItem = foodItem;
        this.averageRating = averageRating;
        this.voteCount = voteCount;
        this.totalScore = totalScore;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getAverageText() {
        return String.format("%.2f", averageRating);
    }
}
