DROP TABLE IF EXISTS SSH_LIB_USER;

CREATE TABLE SSH_LIB_USER(
      USER_SEQ    IDENTITY        PRIMARY KEY
    , USER_NAME   VARCHAR(255)    NOT NULL
    , USER_ID     VARCHAR(255)    NOT NULL
    , PASSWORD    VARCHAR(255)    NOT NULL
);

/*INSERT INTO SSH_LIB_USER (USER_NAME, USER_ID) values ('서승환', 'save7412');
INSERT INTO SSH_LIB_USER (USER_NAME, USER_ID) values ('스승환', 'sense');
INSERT INTO SSH_LIB_USER (USER_NAME, USER_ID) values ('사상환', '4up');
INSERT INTO SSH_LIB_USER (USER_NAME, USER_ID) values ('서성환', 'westCastle');
INSERT INTO SSH_LIB_USER (USER_NAME, USER_ID) values ('개성환', 'dogCastle');*/
