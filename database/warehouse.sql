CREATE DATABASE IF NOT EXISTS crm_warehouse;

USE crm_warehouse;


CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product VARCHAR(100) NOT NULL,
    series VARCHAR(100),
    sales_price DECIMAL(12,2)
);


CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    account VARCHAR(150) NOT NULL,
    sector VARCHAR(100),
    year_established INT,
    revenue DECIMAL(15,2),
    employees INT,
    office_location VARCHAR(150),
    subsidiary_of VARCHAR(150)
);


CREATE TABLE sales_teams (
    sales_agent_id    INT AUTO_INCREMENT PRIMARY KEY,
    sales_agent VARCHAR(150) NOT NULL,
    manager VARCHAR(150),
    regional_office VARCHAR(100)
);


CREATE TABLE sales_pipeline (
    opportunity_id VARCHAR(20) PRIMARY KEY,
    sales_agent_id    INT,
    product_id        INT,
    account_id        INT,
    deal_stage VARCHAR(30),
    engage_date DATE,
    close_date DATE,
    close_value DECIMAL(12,2),

    CONSTRAINT fk_opp_agent   FOREIGN KEY (sales_agent_id) REFERENCES sales_teams(sales_agent_id),
    CONSTRAINT fk_opp_product FOREIGN KEY (product_id)     REFERENCES products(product_id),
    CONSTRAINT fk_opp_account FOREIGN KEY (account_id)     REFERENCES accounts(account_id)
);