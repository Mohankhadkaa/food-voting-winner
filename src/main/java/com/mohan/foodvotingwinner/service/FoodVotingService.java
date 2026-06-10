package com.mohan.foodvotingwinner.service;

import com.mohan.foodvotingwinner.dto.FoodResult;
import com.mohan.foodvotingwinner.model.FoodItem;
import com.mohan.foodvotingwinner.model.Vote;
import com.mohan.foodvotingwinner.repository.FoodItemRepository;
import com.mohan.foodvotingwinner.repository.VoteRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FoodVotingService {

    private final FoodItemRepository foodItemRepository;
    private final VoteRepository voteRepository;

    public FoodVotingService(FoodItemRepository foodItemRepository, VoteRepository voteRepository) {
        this.foodItemRepository = foodItemRepository;
        this.voteRepository = voteRepository;
    }

    @PostConstruct
    public void seedFoods() {
        if (foodItemRepository.count() > 0) {
            return;
        }

        foodItemRepository.save(new FoodItem(
                "Chicken Momo",
                "Nepali",
                "Steamed dumplings served with spicy tomato chutney.",
                "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46"
        ));
        foodItemRepository.save(new FoodItem(
                "Korean Bibimbap",
                "Korean",
                "Mixed rice with sautéed vegetables, egg, and gochujang sauce.",
                "https://images.unsplash.com/photo-1553163147-622ab57be1c7"
        ));
        foodItemRepository.save(new FoodItem(
                "Nepali Thakali Set",
                "Nepali",
                "Traditional thali with rice, dal, curry, pickles, and sides.",
                "https://images.unsplash.com/photo-1606491956689-2ea866880c84"
        ));
        foodItemRepository.save(new FoodItem(
                "Ramen Bowl",
                "Japanese",
                "Noodles in rich broth with toppings and soft egg.",
                "https://images.unsplash.com/photo-1569718212165-3a8278d5f624"
        ));
        foodItemRepository.save(new FoodItem(
                "Tacos",
                "Mexican",
                "Soft tortilla filled with seasoned meat, slaw, and salsa.",
                "https://images.unsplash.com/photo-1565299585323-38d6b0865b47"
        ));
        foodItemRepository.save(new FoodItem(
                "Chicken Biryani",
                "Indian",
                "Layered aromatic rice with spiced chicken, herbs, and saffron.",
                "https://images.unsplash.com/photo-1563379091339-03246963d7d3"
        ));
    }

    public List<FoodItem> findAllFoods() {
        return foodItemRepository.findAll();
    }

    public FoodItem saveFood(FoodItem foodItem) {
        if (foodItem.getImageUrl() == null || foodItem.getImageUrl().isBlank()) {
            foodItem.setImageUrl("https://placehold.co/700x450/e0e0e0/333333?text=" + foodItem.getName().replace(" ", "+"));
        }
        return foodItemRepository.save(foodItem);
    }

    @Transactional
    public void deleteFood(Long id) {
        foodItemRepository.deleteById(id);
    }

    public void vote(Long foodId, int rating, String comment) {
        FoodItem foodItem = foodItemRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found"));
        voteRepository.save(new Vote(rating, comment, foodItem));
    }

    public List<FoodResult> getResults() {
        return foodItemRepository.findAll()
                .stream()
                .map(this::toResult)
                .sorted(Comparator
                        .comparing(FoodResult::getAverageRating).reversed()
                        .thenComparing(FoodResult::getVoteCount, Comparator.reverseOrder())
                        .thenComparing(FoodResult::getTotalScore, Comparator.reverseOrder()))
                .toList();
    }

    public Optional<FoodResult> getWinnerFood() {
        return getResults().stream()
                .filter(result -> result.getVoteCount() > 0)
                .findFirst();
    }

    private FoodResult toResult(FoodItem foodItem) {
        List<Vote> votes = voteRepository.findByFoodItemId(foodItem.getId());
        long voteCount = votes.size();
        int totalScore = votes.stream().mapToInt(Vote::getRating).sum();
        double average = voteCount == 0 ? 0.0 : (double) totalScore / voteCount;
        return new FoodResult(foodItem, average, voteCount, totalScore);
    }
}
