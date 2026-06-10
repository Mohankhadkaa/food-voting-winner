package com.mohan.foodvotingwinner.controller;

import com.mohan.foodvotingwinner.model.FoodItem;
import com.mohan.foodvotingwinner.service.FoodVotingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private final FoodVotingService foodVotingService;

    public WebController(FoodVotingService foodVotingService) {
        this.foodVotingService = foodVotingService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("foods", foodVotingService.findAllFoods());
        model.addAttribute("winner", foodVotingService.getWinnerFood().orElse(null));
        return "index";
    }

    @PostMapping("/vote")
    public String vote(@RequestParam Long foodId,
                       @RequestParam int rating,
                       @RequestParam(required = false) String comment,
                       RedirectAttributes redirectAttributes) {
        foodVotingService.vote(foodId, rating, comment);
        redirectAttributes.addFlashAttribute("message", "Vote added successfully!");
        return "redirect:/";
    }

    @GetMapping("/results")
    public String results(Model model) {
        model.addAttribute("results", foodVotingService.getResults());
        return "results";
    }

    @GetMapping("/winner")
    public String winner(Model model) {
        model.addAttribute("winner", foodVotingService.getWinnerFood().orElse(null));
        model.addAttribute("results", foodVotingService.getResults());
        return "winner";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("foodItem", new FoodItem());
        return "add-food";
    }

    @PostMapping("/add")
    public String addFood(@Valid @ModelAttribute FoodItem foodItem,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "add-food";
        }
        foodVotingService.saveFood(foodItem);
        redirectAttributes.addFlashAttribute("message", "Food added successfully!");
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteFood(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        foodVotingService.deleteFood(id);
        redirectAttributes.addFlashAttribute("message", "Food deleted successfully!");
        return "redirect:/";
    }
}
