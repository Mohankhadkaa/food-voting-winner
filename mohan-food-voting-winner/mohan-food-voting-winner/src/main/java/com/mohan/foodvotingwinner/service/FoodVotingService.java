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
                "https://placehold.co/700x450/f6d365/333333?text=Chicken+Momo"
        ));
        foodItemRepository.save(new FoodItem(
                "Kimchi Fried Rice",
                "Korean",
                "Fried rice cooked with kimchi, vegetables, and egg.",
                "https://placehold.co/700x450/fda085/333333?text=Kimchi+Fried+Rice"
        ));
        foodItemRepository.save(new FoodItem(
                "Veg Biryani",
                "Indian",
                "Aromatic rice with vegetables, herbs, and warm spices.",
                "https://placehold.co/700x450/a1c4fd/333333?text=Veg+Biryani"
        ));
        foodItemRepository.save(new FoodItem(
                "Ramen Bowl",
                "Japanese",
                "Noodles in rich broth with toppings and soft egg.",
                "https://placehold.co/700x450/c2e9fb/333333?text=Ramen+Bowl"
        ));
        foodItemRepository.save(new FoodItem(
                "Fish Taco",
                "Mexican",
                "Soft tortilla filled with seasoned fish, slaw, and sauce.",
                "https://placehold.co/700x450/d4fc79/333333?text=Fish+Taco"
        ));
        foodItemRepository.save(new FoodItem(
                "Paneer Butter Masala",
                "Indian",
                "Paneer cubes cooked in creamy tomato butter gravy.",
                "https://placehold.co/700x450/ffecd2/333333?text=Paneer+Butter+Masala"
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
