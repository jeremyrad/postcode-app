DROP TABLE IF EXISTS SUBURB;
DROP TABLE IF EXISTS POST_CODE;

CREATE TABLE POST_CODE (
    CODE VARCHAR(4) PRIMARY KEY
);

CREATE TABLE SUBURB (
    SUBURB_ID IDENTITY NOT NULL PRIMARY KEY,
    SUBURB_NAME VARCHAR(50) NOT NULL,
    CODE VARCHAR(4)
);