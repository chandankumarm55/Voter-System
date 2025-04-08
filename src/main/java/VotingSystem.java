package main;

import dao.CandidateDAO;
import dao.VoterDAO;
import models.Candidate;
import models.Voter;

import java.util.List;

public class VotingSystem {
    private CandidateDAO candidateDAO;
    private VoterDAO voterDAO;

    public VotingSystem() {
        this.candidateDAO = new CandidateDAO();
        this.voterDAO = new VoterDAO();
    }

    public List<Candidate> getAllCandidates() {
        return candidateDAO.getAllCandidates();
    }

    public Candidate getCandidateById(int candidateId) {
        return candidateDAO.getCandidateById(candidateId);
    }

    public boolean registerVoter(String name, String email, String password) {
        Voter voter = new Voter();
        voter.setVoterName(name);
        voter.setEmail(email);
        voter.setPassword(password); // In a real system, password should be hashed
        return voterDAO.registerVoter(voter);
    }

    public Voter authenticateVoter(String email, String password) {
        return voterDAO.authenticateVoter(email, password);
    }

    public boolean castVote(int voterId, int candidateId) {
        if (voterDAO.updateVoteStatus(voterId)) {
            return candidateDAO.updateVotesCount(candidateId);
        }
        return false;
    }

    public List<Voter> getAllVoters() {
        return voterDAO.getAllVoters();
    }

    public boolean addCandidate(String name, String position, String description) {
        Candidate candidate = new Candidate();
        candidate.setCandidateName(name);
        candidate.setPosition(position);
        candidate.setDescription(description);
        return candidateDAO.addCandidate(candidate);
    }
}