drop database baseball_season;
create database baseball_season;
use baseball_season;

create table PLAYER (
	 ID INT NOT NULL auto_increment,
   LAST_NAME VARCHAR(25) default NOT NULL,
   FIRST_NAME VARCHAR(25) default NULL,
   TIMES_IN_PREMIUM  INT  default NULL,
   PRIMARY KEY (id)
);

create table GAME (
  ID INT NOT NULL auto_increment,
  DATE_PLAYER NOT NULL DATE,
  LOCATION VARCHAR(25) NULL,
  PRIMARY KEY (id)
);

create table INNING (
	 INNING_NUMBER INT NOT NULL,
   GAME_ID INT NOT NULL,
   PLAYER_ID INT NOT NULL,
   FIELD_POSITION VARCHAR(20) NOT NULL,
   PRIMARY KEY (INNING_NUMBER, GAME_ID, PLAYER_ID),
   FOREIGN KEY (GAME_ID) REFERENCES GAME(ID),
   FOREIGN KEY (PLAYER_ID) REFERENCES PLAYER(ID)
);