-- CREATE DATABASE disasterevacuationdb;

USE disasterevacuationdb;

-- CREATE TABLE reports (
-- report_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
-- address VARCHAR(150),
-- username VARCHAR(20),
-- longitude DOUBLE,
-- latitude DOUBLE,
-- radius DOUBLE,
-- title VARCHAR(100),
-- textcontent TEXT,
-- content MEDIUMBLOB,
-- filename VARCHAR(100)
-- );
   
-- ALTER TABLE reports ADD INDEX(username);

-- CREATE TABLE users (
-- user_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
-- username VARCHAR(20) UNIQUE NOT NULL,
-- password_hash VARCHAR(250) NOT NULL,
-- firstname VARCHAR(20),
-- lastname VARCHAR(20),
-- joindate DATE
-- );
   
-- CREATE TABLE user_roles (
--   username VARCHAR(20) NOT NULL,
--   rolename VARCHAR(15) NOT NULL,
--   PRIMARY KEY (username, rolename)
-- );

-- CREATE USER 'tomcat'@'localhost' IDENTIFIED BY 'tomcat';
-- GRANT SELECT ON disasterevacuationdb.* TO tomcat@localhost;