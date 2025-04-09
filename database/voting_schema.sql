-- Create the database
CREATE DATABASE IF NOT EXISTS voting_system;
USE voting_system;

-- Create the voters table
CREATE TABLE IF NOT EXISTS voters (
    voter_id INT AUTO_INCREMENT PRIMARY KEY,
    voter_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    has_voted BOOLEAN DEFAULT FALSE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the candidates table
CREATE TABLE IF NOT EXISTS candidates (
    candidate_id INT AUTO_INCREMENT PRIMARY KEY,
    candidate_name VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    description TEXT,
    votes_count INT DEFAULT 0
);

-- Create the votes table (to track votes)
CREATE TABLE IF NOT EXISTS votes (
    vote_id INT AUTO_INCREMENT PRIMARY KEY,
    voter_id INT NOT NULL,
    candidate_id INT NOT NULL,
    vote_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (voter_id) REFERENCES voters(voter_id),
    FOREIGN KEY (candidate_id) REFERENCES candidates(candidate_id)
);

-- Insert sample candidates
INSERT INTO candidates (candidate_name, position, description) VALUES
('John Doe', 'President', 'Experienced leader with 5 years of management experience'),
('Jane Smith', 'President', 'Innovative thinker with fresh ideas'),
('Bob Johnson', 'Vice President', 'Detail-oriented with strong organizational skills'),
('Alice Brown', 'Vice President', 'Team player with excellent communication skills');

-- Insert sample voters
INSERT INTO voters (voter_name, email, password) VALUES
('Chandan Kumar', 'chandan@example.com', 'password123'),
('Ravi Singh', 'ravi@example.com', 'password456'),
('Anjali Sharma', 'anjali@example.com', 'password789');
