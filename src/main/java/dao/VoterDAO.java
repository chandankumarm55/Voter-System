package dao;

import models.Voter;
import utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoterDAO {

    public boolean registerVoter(Voter voter) {
        String query = "INSERT INTO voters (voter_name, email, password) VALUES (?, ?, ?)";

        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, voter.getVoterName());
            statement.setString(2, voter.getEmail());
            statement.setString(3, voter.getPassword()); // In a real system, password should be hashed
            int rowsAffected = statement.executeUpdate();
            statement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error registering voter: " + e.getMessage());
            return false;
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public Voter authenticateVoter(String email, String password) {
        String query = "SELECT * FROM voters WHERE email = ? AND password = ?"; // In a real system, would verify hashed
                                                                                // password

        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            Voter voter = null;
            if (resultSet.next()) {
                voter = new Voter();
                voter.setVoterId(resultSet.getInt("voter_id"));
                voter.setVoterName(resultSet.getString("voter_name"));
                voter.setEmail(resultSet.getString("email"));
                voter.setHasVoted(resultSet.getBoolean("has_voted"));
                voter.setRegistrationDate(resultSet.getTimestamp("registration_date"));
            }

            resultSet.close();
            statement.close();
            return voter;
        } catch (SQLException e) {
            System.err.println("Error authenticating voter: " + e.getMessage());
            return null;
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public boolean updateVoteStatus(int voterId) {
        String query = "UPDATE voters SET has_voted = TRUE WHERE voter_id = ? AND has_voted = FALSE";

        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, voterId);
            int rowsAffected = statement.executeUpdate();
            statement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating vote status: " + e.getMessage());
            return false;
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public List<Voter> getAllVoters() {
        List<Voter> voters = new ArrayList<>();
        String query = "SELECT * FROM voters";

        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Voter voter = new Voter();
                voter.setVoterId(resultSet.getInt("voter_id"));
                voter.setVoterName(resultSet.getString("voter_name"));
                voter.setEmail(resultSet.getString("email"));
                voter.setHasVoted(resultSet.getBoolean("has_voted"));
                voter.setRegistrationDate(resultSet.getTimestamp("registration_date"));
                voters.add(voter);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving voters: " + e.getMessage());
        } finally {
            DatabaseUtil.closeConnection(connection);
        }

        return voters;
    }
}