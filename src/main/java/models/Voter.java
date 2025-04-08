package models;

import java.util.Date;

public class Voter {
    private int voterId;
    private String voterName;
    private String email;
    private String password;
    private boolean hasVoted;
    private Date registrationDate;

    public Voter() {
    }

    public Voter(int voterId, String voterName, String email, String password, boolean hasVoted,
            Date registrationDate) {
        this.voterId = voterId;
        this.voterName = voterName;
        this.email = email;
        this.password = password;
        this.hasVoted = hasVoted;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public int getVoterId() {
        return voterId;
    }

    public void setVoterId(int voterId) {
        this.voterId = voterId;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Voter{" +
                "voterId=" + voterId +
                ", voterName='" + voterName + '\'' +
                ", email='" + email + '\'' +
                ", hasVoted=" + hasVoted +
                ", registrationDate=" + registrationDate +
                '}';
    }
}