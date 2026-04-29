-- Add qr_code_url column to orders table
ALTER TABLE orders ADD COLUMN qr_code_url VARCHAR(500) NULL;

-- Add comment
ALTER TABLE orders MODIFY COLUMN qr_code_url VARCHAR(500) NULL COMMENT 'VietQR code URL for bank transfer payment';
