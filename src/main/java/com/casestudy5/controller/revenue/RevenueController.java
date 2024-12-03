package com.casestudy5.controller.revenue;

import com.casestudy5.model.entity.product.ProductRevenueDTO;
import com.casestudy5.service.product.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/revenue")
public class RevenueController {

    @Autowired
    private RevenueService revenueService;


    @GetMapping
    public ResponseEntity<List<ProductRevenueDTO>> getRevenueByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // Create a more robust date formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        try {
            // Parse dates with time included
            LocalDateTime start = LocalDateTime.parse(startDate + " 00:00", formatter);
            LocalDateTime end = LocalDateTime.parse(endDate + " 23:59", formatter);

            // Log the parsed dates for debugging
            System.out.println("Start Date: " + start);
            System.out.println("End Date: " + end);

            // Retrieve revenue data
            List<ProductRevenueDTO> revenueData = revenueService.getRevenueByDateRange(start, end);

            // Check if data is returned
            if (revenueData.isEmpty()) {
                System.out.println("No data found for the given date range.");
            }

            // Return the data with OK status
            return ResponseEntity.ok(revenueData);
        } catch (Exception e) {
            // If an error occurs, return an error response with the exception details
            System.out.println("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ArrayList<>());
        }
    }
}