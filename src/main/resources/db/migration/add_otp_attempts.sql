-- Migration script to add otp_attempts column to account table
-- This adds security feature to prevent OTP brute force attacks

-- Add otp_attempts column with default value 0
ALTER TABLE account 
ADD COLUMN otp_attempts INT DEFAULT 0 AFTER expiry_otp;

-- Update existing records to have 0 attempts
UPDATE account 
SET otp_attempts = 0 
WHERE otp_attempts IS NULL;

-- Add comment to document the column
ALTER TABLE account 
MODIFY COLUMN otp_attempts INT DEFAULT 0 COMMENT 'Number of failed OTP attempts (max 5)';
