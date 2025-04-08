from flask import Flask, render_template, request, redirect, url_for, session, jsonify
import subprocess
import json
import os
import mysql.connector
from werkzeug.security import generate_password_hash, check_password_hash

app = Flask(__name__)
app.secret_key = 'your_secret_key_here'  # Change this to a random secret key

# Database connection
def get_db_connection():
    return mysql.connector.connect(
        host="localhost",
        user="root",
        password="",
        database="voting_system"
    )

# Compile Java code
def compile_java():
    try:
        subprocess.run(['javac', '-d', './classes', './src/main/java/VotingSystem.java'], check=True)
        return True
    except subprocess.CalledProcessError as e:
        print(f"Compilation error: {e}")
        return False

# Run Java code and get output
def run_java_method(method_name, args=None):
    if args is None:
        args = []
    
    try:
        cmd = ['java', '-cp', './classes', 'VotingSystem', method_name] + args
        result = subprocess.run(cmd, capture_output=True, text=True, check=True)
        return result.stdout.strip()
    except subprocess.CalledProcessError as e:
        print(f"Runtime error: {e}")
        return None

# Routes
@app.route('/')
def index():
    if 'user_id' not in session:
        return redirect(url_for('login'))
    
    # Get candidates from database
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM candidates")
    candidates = cursor.fetchall()
    cursor.close()
    conn.close()
    
    return render_template('index.html', candidates=candidates)

@app.route('/login', methods=['GET', 'POST'])
def login():
    error = None
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']
        
        # Check if it's the admin
        if email == 'admin@voting.com' and password == 'admin123':
            session['is_admin'] = True
            return redirect(url_for('admin'))
        
        # Check regular user
        conn = get_db_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM voters WHERE email = %s", (email,))
        user = cursor.fetchone()
        cursor.close()
        conn.close()
        
        if user and user['password'] == password:  # In a real app, use check_password_hash
            session['user_id'] = user['voter_id']
            session['username'] = user['voter_name']
            session['has_voted'] = user['has_voted']
            return redirect(url_for('index'))
        else:
            error = 'Invalid email or password'
    
    return render_template('login.html', error=error)

@app.route('/register', methods=['GET', 'POST'])
def register():
    error = None
    if request.method == 'POST':
        name = request.form['name']
        email = request.form['email']
        password = request.form['password']
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # Check if email already exists
        cursor.execute("SELECT * FROM voters WHERE email = %s", (email,))
        if cursor.fetchone():
            error = 'Email already registered'
        else:
            # In a real app, hash the password
            cursor.execute(
                "INSERT INTO voters (voter_name, email, password) VALUES (%s, %s, %s)",
                (name, email, password)
            )
            conn.commit()
            cursor.close()
            conn.close()
            return redirect(url_for('login'))
        
        cursor.close()
        conn.close()
    
    return render_template('register.html', error=error)

@app.route('/vote', methods=['POST'])
def vote():
    if 'user_id' not in session:
        return redirect(url_for('login'))
    
    if session.get('has_voted', False):
        return redirect(url_for('results'))
    
    candidate_id = request.form.get('candidate_id')
    
    conn = get_db_connection()
    cursor = conn.cursor()
    
    # Update voter's status
    cursor.execute(
        "UPDATE voters SET has_voted = TRUE WHERE voter_id = %s",
        (session['user_id'],)
    )
    
    # Update candidate's votes
    cursor.execute(
        "UPDATE candidates SET votes_count = votes_count + 1 WHERE candidate_id = %s",
        (candidate_id,)
    )
    
    conn.commit()
    cursor.close()
    conn.close()
    
    session['has_voted'] = True
    return redirect(url_for('results'))

@app.route('/results')
def results():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM candidates ORDER BY votes_count DESC")
    candidates = cursor.fetchall()
    cursor.close()
    conn.close()
    
    total_votes = sum(candidate['votes_count'] for candidate in candidates)
    
    return render_template('results.html', candidates=candidates, total_votes=total_votes)

@app.route('/admin')
def admin():
    if not session.get('is_admin', False):
        return redirect(url_for('login'))
    
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    
    cursor.execute("SELECT * FROM candidates ORDER BY votes_count DESC")
    candidates = cursor.fetchall()
    
    cursor.execute("SELECT * FROM voters")
    voters = cursor.fetchall()
    
    cursor.close()
    conn.close()
    
    return render_template('admin.html', candidates=candidates, voters=voters)

@app.route('/admin/add_candidate', methods=['POST'])
def add_candidate():
    if not session.get('is_admin', False):
        return redirect(url_for('login'))
    
    name = request.form['name']
    position = request.form['position']
    description = request.form['description']
    
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO candidates (candidate_name, position, description) VALUES (%s, %s, %s)",
        (name, position, description)
    )
    conn.commit()
    cursor.close()
    conn.close()
    
    return redirect(url_for('admin'))

@app.route('/logout')
def logout():
    session.clear()
    return redirect(url_for('login'))

if __name__ == '__main__':
    app.run(debug=True)