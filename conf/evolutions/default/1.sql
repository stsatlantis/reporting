# DC SCHEMA

# --- !Ups


CREATE TABLE PERSON (
  ID       INTEGER      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  NAME     VARCHAR(255) NOT NULL,
  EMAIL    VARCHAR(255) NOT NULL,
  PASSWORD VARCHAR(255) NOT NULL
);

CREATE TABLE FEEDBACK (
  ID        INTEGER  NOT NULL AUTO_INCREMENT PRIMARY KEY,
  REPORTER  INTEGER  NOT NULL,
  REPORTEE  INTEGER  NOT NULL,
  REPORT    TEXT     NOT NULL,
  FILL_DATE DATETIME NOT NULL,
  FOREIGN KEY (REPORTER) REFERENCES PERSON (ID),
  FOREIGN KEY (REPORTEE) REFERENCES PERSON (ID)
);

CREATE TABLE PEER_REVIEW (
  ID        INTEGER  NOT NULL AUTO_INCREMENT PRIMARY KEY,
  REPORTEE  INTEGER  NOT NULL,
  REPORT    TEXT     NOT NULL,
  FILL_DATE DATETIME NOT NULL,
  FOREIGN KEY (REPORTEE) REFERENCES PERSON (ID)
);

CREATE TABLE SELF_REPORT (
  ID        INTEGER  NOT NULL AUTO_INCREMENT PRIMARY KEY,
  REPORTER  INTEGER  NOT NULL,
  MENTOR    INTEGER  NOT NULL,
  REPORT    TEXT     NOT NULL,
  FILL_DATE DATETIME NOT NULL,
  FOREIGN KEY (REPORTER) REFERENCES PERSON (ID),
  FOREIGN KEY (MENTOR) REFERENCES PERSON (ID)
);


# --- !Downs

DROP TABLE SELF_REPORT;
DROP TABLE PEER_REVIEW;
DROP TABLE FEEDBACK;
DROP TABLE PERSON;
