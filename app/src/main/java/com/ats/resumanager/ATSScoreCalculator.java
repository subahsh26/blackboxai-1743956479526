package com.ats.resumanager;

import com.ats.resumanager.models.Resume;
import java.util.Arrays;
import java.util.List;

public class ATSScoreCalculator {
    private static final List<String> KEYWORDS = Arrays.asList(
        "experience", "skills", "education", "certification",
        "project", "leadership", "achievement", "technical",
        "communication", "teamwork", "problem solving"
    );

    public static void calculateScore(Resume resume, String content) {
        // Calculate keyword matches (40% weight)
        int keywordCount = countKeywords(content.toLowerCase());
        resume.setKeywordMatches(keywordCount);
        double keywordScore = (keywordCount * 4.0); // Max 40 points

        // Check formatting (30% weight)
        boolean properFormatting = checkFormatting(content);
        resume.setProperFormatting(properFormatting);
        double formattingScore = properFormatting ? 30 : 15;

        // Check length (20% weight)
        boolean optimalLength = checkLength(content);
        double lengthScore = optimalLength ? 20 : 10;

        // Check contact info (10% weight)
        boolean hasContact = checkContactInfo(content);
        resume.setHasContactInfo(hasContact);
        double contactScore = hasContact ? 10 : 0;

        // Calculate total score
        double totalScore = keywordScore + formattingScore + lengthScore + contactScore;
        resume.setAtsScore(Math.min(100, totalScore)); // Cap at 100
    }

    private static int countKeywords(String content) {
        int count = 0;
        for (String keyword : KEYWORDS) {
            if (content.contains(keyword)) {
                count++;
            }
        }
        return count;
    }

    private static boolean checkFormatting(String content) {
        // Check for common formatting indicators
        return content.contains("\n\n") && // Paragraph breaks
               content.contains(":") &&    // Section headers
               content.contains("•");      // Bullet points
    }

    private static boolean checkLength(String content) {
        // Optimal length between 500-1500 words
        int wordCount = content.split("\\s+").length;
        return wordCount >= 500 && wordCount <= 1500;
    }

    private static boolean checkContactInfo(String content) {
        // Check for email pattern
        boolean hasEmail = content.matches(".*\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b.*");
        // Check for phone number pattern
        boolean hasPhone = content.matches(".*(\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}).*");
        return hasEmail && hasPhone;
    }
}