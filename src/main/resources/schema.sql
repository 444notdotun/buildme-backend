-- BuildMe Backend — Database Schema
-- Run this once on your PostgreSQL database

-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- User Profile (single user app)
CREATE TABLE IF NOT EXISTS user_profile (
    id VARCHAR(50) PRIMARY KEY DEFAULT 'dotman',
    name VARCHAR(100) DEFAULT 'Adedotun',
    full_name VARCHAR(200) DEFAULT 'Adedotun (Dotman) Adewole Stephen',
    handle VARCHAR(50) DEFAULT '@notdotun_',
    github VARCHAR(100) DEFAULT '444notdotun',
    level VARCHAR(50) DEFAULT 'JUNIOR',
    target_level VARCHAR(50) DEFAULT 'MID_LEVEL',
    phase INTEGER DEFAULT 1,
    chevening_hours INTEGER DEFAULT 0,
    chevening_target INTEGER DEFAULT 2800,
    stack_json TEXT DEFAULT '["Java","Spring Boot","Spring Security","PostgreSQL","Docker","React"]',
    goals_json TEXT,
    job_profiles_json TEXT DEFAULT '[]',
    pin_hash VARCHAR(200),
    failed_pin_attempts INTEGER DEFAULT 0,
    pin_locked BOOLEAN DEFAULT false,
    lock_until TIMESTAMP,
    first_login BOOLEAN DEFAULT true,
    last_updated TIMESTAMP DEFAULT NOW()
);

-- Insert default profile if not exists
INSERT INTO user_profile (id) VALUES ('dotman') ON CONFLICT DO NOTHING;

-- Opportunities (jobs, scholarships, fellowships)
CREATE TABLE IF NOT EXISTS opportunities (
    id VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    title VARCHAR(500) NOT NULL,
    company VARCHAR(200),
    location VARCHAR(100),
    salary VARCHAR(200),
    url VARCHAR(1000),
    skills TEXT,
    type VARCHAR(50) NOT NULL, -- JOB, SCHOLARSHIP, FELLOWSHIP, OPEN_SOURCE
    seen BOOLEAN DEFAULT false,
    applied_at VARCHAR(100),
    match_score INTEGER DEFAULT 0,
    fetched_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_opportunities_seen ON opportunities(seen);
CREATE INDEX IF NOT EXISTS idx_opportunities_type ON opportunities(type);

-- Open Source (BUILD or PAID bounties)
CREATE TABLE IF NOT EXISTS open_source (
    id VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    name VARCHAR(500) NOT NULL,
    url VARCHAR(1000),
    description TEXT,
    bounty VARCHAR(100),
    language VARCHAR(50),
    level VARCHAR(50),
    type VARCHAR(20) NOT NULL, -- BUILD, PAID
    seen BOOLEAN DEFAULT false,
    contributed BOOLEAN DEFAULT false,
    fetched_at TIMESTAMP DEFAULT NOW()
);

-- Progress Logs
CREATE TABLE IF NOT EXISTS progress_logs (
    id VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    energy_level VARCHAR(20),
    available_minutes INTEGER,
    shipped TEXT,
    plan_focus TEXT,
    concept_studied VARCHAR(200),
    reflection_note TEXT,
    chevening_day_counted BOOLEAN DEFAULT false,
    logged_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_progress_date ON progress_logs(date);

-- DSA Entries
CREATE TABLE IF NOT EXISTS dsa_entries (
    id VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    problem VARCHAR(500) NOT NULL,
    difficulty VARCHAR(20),
    time_complexity VARCHAR(50),
    space_complexity VARCHAR(50),
    pattern VARCHAR(200),
    attempts INTEGER DEFAULT 1,
    solved_at DATE NOT NULL DEFAULT CURRENT_DATE,
    logged_at TIMESTAMP DEFAULT NOW()
);

-- Challenges (weekly, monthly, era, interest)
CREATE TABLE IF NOT EXISTS challenges (
    id VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    type VARCHAR(50) NOT NULL, -- WEEKLY, MONTHLY, ERA_REVIEW, INTEREST_OF_WEEK
    questions_json TEXT NOT NULL,
    week_key VARCHAR(20),
    month_key VARCHAR(20),
    era_key VARCHAR(20),
    answers_json TEXT,
    evaluations_json TEXT,
    completed BOOLEAN DEFAULT false,
    generated_at TIMESTAMP DEFAULT NOW(),
    completed_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_challenges_type_week ON challenges(type, week_key);

-- Mentor Messages
CREATE TABLE IF NOT EXISTS mentor_messages (
    id VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    role VARCHAR(20) NOT NULL, -- USER, ASSISTANT
    content TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT NOW()
);

-- Session Commits
CREATE TABLE IF NOT EXISTS session_commits (
    id VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    summary TEXT,
    open_loops_json TEXT,
    next_steps_json TEXT,
    copyable_prompt TEXT NOT NULL,
    auto_generated BOOLEAN DEFAULT false,
    generated_at TIMESTAMP DEFAULT NOW()
);

-- Notifications (from scheduler)
CREATE TABLE IF NOT EXISTS notifications (
    id VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    type VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    read BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_notifications_type_date
    ON notifications(type, DATE(created_at));

-- Search Logs
CREATE TABLE IF NOT EXISTS search_logs (
    id VARCHAR(50) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    search_type VARCHAR(50),
    results_found INTEGER DEFAULT 0,
    new_results INTEGER DEFAULT 0,
    success BOOLEAN DEFAULT true,
    error_message TEXT,
    searched_at TIMESTAMP DEFAULT NOW()
);
