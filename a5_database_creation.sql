CREATE DATABASE disasterevacuationdb;

USE disasterevacuationdb;

CREATE TABLE reports (
report_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
address VARCHAR(150),
username VARCHAR(100),
longitude DOUBLE,
latitude DOUBLE,
radius DOUBLE,
title VARCHAR(100),
content LONGBLOB,
filename VARCHAR(100)
);

CREATE TABLE users (
user_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(100) UNIQUE,
password_hash VARCHAR(250),
secretkey VARBINARY(250),
firstname VARCHAR(100),
lastname VARCHAR(100),
joindate DATE
);