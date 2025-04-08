package dao;

import models.Candidate;
import utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAO {

    public List<Candidate> getAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        String query = "SELECT * FROM candidates";

        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Candidate candidate = new Candidate();
                candidate.setCandidateId(resultSet.getInt("candidate_id"));
                candidate.setCandidateName(resultSet.getString("candidate_name"));
                candidate.setPosition(resultSet.getString("position"));
                candidate.setDescription(resultSet.getString("description"));
                candidate.setVotesCount(resultSet.getInt("votes_count"));
                candidates.add(candidate);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving candidates: " + e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(connection);
        }

        return candidates;
    }

    public Candidate getCandidateById(int candidateId) {
        String query = "SELECT * FROM candidates WHERE candidate_id = ?";
        Candidate candidate = null;

        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, candidateId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                candidate = new Candidate();
                candidate.setCandidateId(resultSet.getInt("candidate_id"));
                candidate.setCandidateName(resultSet.getString("candidate_name"));
                candidate.setPosition(resultSet.getString("position"));
                candidate.setDescription(resultSet.getString("description"));
                candidate.setVotesCount(resultSet.getInt("votes_count"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving candidate: " + e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(connection);
        }

        return candidate;
    }

    public boolean updateVotesCount(int candidateId) {
        String query = "UPDATE candidates SET votes_count = votes_count + 1 WHERE candidate_id = ?";

        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, candidateId);
            int rowsAffected = statement.executeUpdate();
            statement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating votes count: " + e.getMessage());
            return false;
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public boolean addCandidate(Candidate candidate) {
        String query = "INSERT INTO candidates (candidate_name, position, description) VALUES (?, ?, ?)";

        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, candidate.getCandidateName());
            statement.setString(2, candidate.getPosition());
            statement.setString(3, candidate.getDescription());
            int rowsAffected = statement.executeUpdate();
            statement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding candidate: " + e.getMessage());
            return false;
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }
}