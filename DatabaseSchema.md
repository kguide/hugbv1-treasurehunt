# Introduction #

This is the database schema for the android project.
If you wish to change anything contact the Hord!


# Database schema #
<pre>
drop table if exists users;<br>
CREATE TABLE users (<br>
userId INT PRIMARY KEY AUTO_INCREMENT,<br>
username VARCHAR(100) NOT NULL UNIQUE,<br>
password CHAR(32) NOT NULL,<br>
lastLogin DATE NOT NULL,<br>
registerDate DATE NOT NULL,<br>
registered BOOL<br>
) TYPE=MyISAM COLLATE=utf8_icelandic_ci;<br>
<br>
drop table if exists games;<br>
CREATE TABLE games (<br>
gameId INT PRIMARY KEY AUTO_INCREMENT,<br>
gameName VARCHAR(20) NOT NULL,<br>
ownerId INT NOT NULL,<br>
created DATE NOT NULL,<br>
expires DATE DEFAULT NULL,<br>
active BOOL DEFAULT TRUE,<br>
FOREIGN KEY (ownerId) REFERENCES users(userId)<br>
) TYPE=MyISAM COLLATE=utf8_icelandic_ci;<br>
<br>
drop table if exists gameCoordinates;<br>
CREATE TABLE gameCoordinates (<br>
coordinateId INT NOT NULL,<br>
gameId INT NOT NULL,<br>
latitude DOUBLE NOT NULL,<br>
longitude DOUBLE NOT NULL,<br>
FOREIGN KEY (gameId) REFERENCES games(gameId)<br>
) TYPE=MyISAM COLLATE=utf8_icelandic_ci;<br>
<br>
drop table if exists  gameMembers;<br>
CREATE TABLE gameMembers (<br>
gameId INT NOT NULL,<br>
userId INT NOT NULL,<br>
joined DATE NOT NULL,<br>
finished BOOL DEFAULT FALSE NOT NULL,<br>
FOREIGN KEY (gameId) REFERENCES games(gameId),<br>
FOREIGN KEY (userId) REFERENCES users(userId)<br>
) TYPE=MyISAM COLLATE=utf8_icelandic_ci;<br>
<br>
drop table if exists gameHints;<br>
CREATE TABLE gameHints (<br>
hintId INT NOT NULL,<br>
gameId INT NOT NULL,<br>
coordinateId INT NOT NULL,<br>
hintText TEXT,<br>
FOREIGN KEY(coordinateId) REFERENCES gameCoordinates(coordinateId),<br>
FOREIGN KEY (gameId) REFERENCES games(gameId)<br>
) TYPE=MyISAM COLLATE=utf8_icelandic_ci;<br>
<br>
drop table if exists gameScores;<br>
CREATE TABLE gameScores (<br>
gameId INT NOT NULL,<br>
score INT DEFAULT '0',<br>
FOREIGN KEY(gameId) REFERENCES games(gameId)<br>
) TYPE=MyISAM COLLATE=utf8_icelandic_ci;<br>
<br>
drop table if exists userScores;<br>
CREATE TABLE userScores (<br>
userId INT NOT NULL,<br>
score INT DEFAULT '0',<br>
locations  INT DEFAULT '0',<br>
FOREIGN KEY(userId) REFERENCES users(userId)<br>
) TYPE=MyISAM COLLATE=utf8_icelandic_ci;<br>
</pre>


# Sample Insert Data #
<pre>
INSERT INTO users (username,password,lastLogin,registerDate)VALUES ('test@test.is','098f6bcd4621d373cade4e832627b4f6','2009-10-15','2009-10-15');<br>
INSERT INTO userScores(userId,score,locations) VALUES (1,0,0);<br>
INSERT INTO games (gameName,ownerId,created,expires) VALUES ('Test Game',1,'2009-10-15','2010-10-15');<br>
INSERT INTO gameCoordinates (coordinateId,gameId,latitude,longitude) VALUES (1,1,64.138252,-21.926658);<br>
INSERT INTO gameCoordinates (coordinateId,gameId,latitude,longitude) VALUES (2,1,64.142052,-21.927243);<br>
INSERT INTO gameScores(gameId,score) VALUES (1,2000);<br>
INSERT INTO gameMembers(gameId,userId,joined,finished) VALUES (1,1,'2009-10-15',0);<br>
INSERT INTO gameHints(hintId,gameId,coordinateId,hintText) VALUES (1,1,1,'Bygging kennd vid ljodskald');<br>
INSERT INTO gameHints(hintId,gameId,coordinateId,hintText) VALUES (1,1,2,'Til hamingju!');<br>
<br>
</pre>