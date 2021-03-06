DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS SLOT;

CREATE TABLE USER
(
ID INT PRIMARY KEY AUTO_INCREMENT,
USER_EMAIL VARCHAR NOT NULL UNIQUE,
USER_NAME VARCHAR NOT NULL,
HASHED_PASSWORD VARCHAR NOT NULL,
SALT VARCHAR NOT NULL
);

CREATE TABLE SLOT
(
SLOT_OWNER_ID INT NOT NULL,
SLOT_START TIMESTAMP NOT NULL,
SLOT_BOOKED_BY INT,
FOREIGN KEY (SLOT_OWNER_ID) REFERENCES USER(ID),
FOREIGN KEY (SLOT_BOOKED_BY) REFERENCES USER(ID),
PRIMARY KEY (SLOT_OWNER_ID, SLOT_START)
);

--insert into USER (USER_EMAIL, USER_NAME, HASHED_PASSWORD, SALT) values ('zafar@google.com', 'Zafar', 'zafa@123', 'qwerty');
--insert into USER (USER_EMAIL, USER_NAME, HASHED_PASSWORD, SALT) values ('hello@google.com', 'Ansari', 'zaf@123', 'poiuu');

--insert into SLOT (SLOT_OWNER_ID, SLOT_START, SLOT_BOOKED_BY) values ('1', '2020-04-01 12:00:00', '2');
--insert into SLOT (SLOT_OWNER_ID, SLOT_START) values ('1', '2020-04-01 13:00:00');
--insert into SLOT (SLOT_OWNER_ID, SLOT_START) values ('1', '2020-04-01 14:00:00');
