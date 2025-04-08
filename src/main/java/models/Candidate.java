package models;

public class Candidate {
    private int candidateId;
    private String candidateName;
    private String position;
    private String description;
    private int votesCount;
    
    public Candidate() {
    }
    
    public Candidate(int candidateId, String candidateName, String position, String description, int votesCount) {
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.position = position;
        this.description = description;
        this.votesCount = votesCount;
    }
    
    // Getters and Setters
    public int getCandidateId() {
        return candidateId;
    }
    
    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }
    
    public String getCandidateName() {
        return candidateName;
    }
    
    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getVotesCount() {
        return votesCount;
    }
    
    public void setVotesCount(int votesCount) {
        this.votesCount = votesCount;
    }
    
    @Override
    public String toString() {
        return "Candidate{" +
                "candidateId=" + candidateId +
                ", candidateName='" + candidateName + '\'' +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                ", votesCount=" + votesCount +
                '}';
    }
}