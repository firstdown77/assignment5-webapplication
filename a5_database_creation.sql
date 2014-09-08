 CREATE DATABASE disasterevacuationdb;

 USE disasterevacuationdb;

 CREATE TABLE users (
 user_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
 username VARCHAR(20) UNIQUE NOT NULL UNIQUE,
 password_hash VARCHAR(250) NOT NULL,
 firstname VARCHAR(20),
 lastname VARCHAR(20),
 joindate DATE
 );
 
 CREATE TABLE reports (
 report_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
 address VARCHAR(150),
 username VARCHAR(20),
 longitude DOUBLE,
 latitude DOUBLE,
 radius DOUBLE,
 title VARCHAR(100),
 textcontent TEXT,
 content MEDIUMBLOB,
 filename VARCHAR(100),
 CONSTRAINT foreignuser FOREIGN KEY (username)
      REFERENCES users (username) ON DELETE CASCADE
 );

 ALTER TABLE reports ADD INDEX(username);

 CREATE TABLE events (
 event_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
 date DATETIME,
 username VARCHAR(20),
 longitude DOUBLE,
 latitude DOUBLE,
 evacuation_means TEXT,
 capacity integer
 );

 CREATE TABLE events_users (
 event_id integer NOT NULL,
 user_id integer NOT NULL,
 PRIMARY KEY (event_id, user_id),
 CONSTRAINT foreignevent FOREIGN KEY (event_id)
      REFERENCES events (event_id) ON DELETE CASCADE
 );
   
 CREATE TABLE user_roles (
   username VARCHAR(20) NOT NULL,
   rolename VARCHAR(15) NOT NULL,
   PRIMARY KEY (username, rolename)
 );

-- CREATE USER 'tomcat'@'localhost' IDENTIFIED BY 'tomcat';
 GRANT SELECT ON disasterevacuationdb.* TO tomcat@localhost;