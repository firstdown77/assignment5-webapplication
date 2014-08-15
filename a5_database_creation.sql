-- CREATE DATABASE disasterevacuationdb;

USE disasterevacuationdb;

-- CREATE TABLE reports (
-- report_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
-- address VARCHAR(150),
-- username VARCHAR(100),
-- longitude DOUBLE,
-- latitude DOUBLE,
-- radius DOUBLE,
-- title VARCHAR(100),
-- textcontent TINYTEXT,
-- content MEDIUMBLOB,
-- filename VARCHAR(100)
-- );
   
-- ALTER TABLE reports ADD INDEX(username);

-- CREATE TABLE users (
-- user_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
-- username VARCHAR(50) UNIQUE NOT NULL,
-- password_hash VARCHAR(250) NOT NULL,
-- firstname VARCHAR(100),
-- lastname VARCHAR(100),
-- joindate DATE
-- );
   
-- CREATE TABLE user_roles (
--   username VARCHAR(50) NOT NULL,
--   rolename VARCHAR(15) NOT NULL,
--   PRIMARY KEY (username, rolename)
-- );

-- INSERT INTO users (username, password_hash, secretkey, firstname, lastname,
-- joindate) VALUES ("tomcat", "tomcat", null, null, null, null);
-- INSERT INTO user_roles (username, rolename) VALUES ("tomcat", "normal");
-- INSERT INTO user_roles (username, rolename) VALUES ("firstdown77", "normal");

-- CREATE USER 'tomcat'@'localhost' IDENTIFIED BY 'tomcat';
-- GRANT SELECT ON disasterevacuationdb.* TO tomcat@localhost;