package com.ats.resumanager.models;

import java.util.Date;

public class Resume {
    private String fileName;
    private String filePath;
    private String fileType;
    private double atsScore;
    private Date uploadDate;
    private int keywordMatches;
    private boolean hasContactInfo;
    private boolean properFormatting;

    public Resume(String fileName, String filePath, String fileType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.uploadDate = new Date();
    }

    // Getters and Setters
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public String getFileType() { return fileType; }
    public double getAtsScore() { return atsScore; }
    public Date getUploadDate() { return uploadDate; }
    public int getKeywordMatches() { return keywordMatches; }
    public boolean hasContactInfo() { return hasContactInfo; }
    public boolean hasProperFormatting() { return properFormatting; }

    public void setAtsScore(double score) { this.atsScore = score; }
    public void setKeywordMatches(int matches) { this.keywordMatches = matches; }
    public void setHasContactInfo(boolean hasContact) { this.hasContactInfo = hasContact; }
    public void setProperFormatting(boolean properFormat) { this.properFormatting = properFormat; }

    @Override
    public String toString() {
        return fileName + " - Score: " + atsScore;
    }
}