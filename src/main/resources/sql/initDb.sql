drop database if exists baseball_season;
create database baseball_season;
use baseball_season;

create table PLAYER (
	 ID INT auto_increment NOT NULL,
   LAST_NAME VARCHAR(25)  NOT NULL,
   FIRST_NAME VARCHAR(25)  NULL,
   TIMES_IN_PREMIUM  INT   NULL,
   PRIMARY KEY (id)
);

create table GAME (
  ID INT NOT NULL auto_increment,
  DATE_PLAYER DATE NOT NULL ,
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

INSERT INTO PLAYER (LAST_NAME, FIRST_NAME, TIMES_IN_PREMIUM) VALUES ('Player1', 'John', 1);
INSERT INTO PLAYER (LAST_NAME, FIRST_NAME, TIMES_IN_PREMIUM) VALUES ('Player2', 'Sam', 1);
INSERT INTO PLAYER (LAST_NAME, FIRST_NAME, TIMES_IN_PREMIUM) VALUES ('Player3', 'Chris', 1);
INSERT INTO PLAYER (LAST_NAME, FIRST_NAME, TIMES_IN_PREMIUM) VALUES ('Player4', 'Tim', 1);
INSERT INTO PLAYER (LAST_NAME, FIRST_NAME, TIMES_IN_PREMIUM) VALUES ('Player5', 'David', 1);
INSERT INTO PLAYER (LAST_NAME, FIRST_NAME, TIMES_IN_PREMIUM) VALUES ('Player6', 'Jason', 1);
INSERT INTO PLAYER (LAST_NAME, FIRST_NAME, TIMES_IN_PREMIUM) VALUES ('Player7', 'Alex', 1);
INSERT INTO PLAYER (LAST_NAME, FIRST_NAME, TIMES_IN_PREMIUM) VALUES ('Player8', 'James', 1);
INSERT INTO PLAYER (LAST_NAME, FIRST_NAME, TIMES_IN_PREMIUM) VALUES ('Player9', 'Steve', 1);
INSERT INTO PLAYER (LAST_NAME, FIRST_NAME, TIMES_IN_PREMIUM) VALUES ('Player10', 'Dylan', 1);

commit;