USE disasterevacuationdb;

CREATE TABLE users (
user_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(100) UNIQUE,
password_hash VARCHAR(250),
secretkey VARBINARY(250),
firstname VARCHAR(100),
lastname VARCHAR(100),
joindate DATE
);