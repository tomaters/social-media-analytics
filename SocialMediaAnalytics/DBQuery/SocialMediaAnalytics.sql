CREATE TABLE users(
    user_name varchar2(20) not null,
    user_id varchar2(20) not null,
    user_pass varchar2(20) not null,
    user_email varchar2(20) not null,
    CONSTRAINT user_pk PRIMARY KEY(user_id)
);
DROP TABLE users;
SELECT * FROM users;
DELETE FROM users where user_id = '123123';
INSERT INTO users VALUES('a', 'a', 'a', 'a');
INSERT INTO users VALUES('b', 'b', 'b', 'b');
INSERT INTO users VALUES('c', 'c', 'c', 'c');
INSERT INTO users VALUES('d', 'd', 'd', 'd');
SELECT user_id, user_email FROM users ORDER BY user_id;
SELECT * FROM users WHERE user_id = 'a';
commit;
UPDATE users SET user_name = 'qwerty' WHERE user_id = 'qweqwe';
------------------------------------------------------------------------------------
CREATE TABLE account_analytics(
    analytics_id number not null,
    user_id varchar2(15) not null,
    total_subscribers number(12),
    total_engagement number(12),
    total_income number(12),
    CONSTRAINT account_analytics_fk FOREIGN KEY(user_id) REFERENCES users(user_id),
    CONSTRAINT account_analytics_pk PRIMARY KEY(analytics_id)
);
DROP TABLE account_analytics;

CREATE SEQUENCE account_analytics_seq
START WITH 1
INCREMENT BY 1;
DROP SEQUENCE account_analytics_seq;
SELECT * FROM account_analytics;

SELECT total_engagement, total_subscribers, total_income FROM account_analytics 
    WHERE user_id = 'aaaaaa';

SELECT a.user_id, platform_name, engagement, income, subscribers 
FROM account_analytics a INNER JOIN platforms p
ON a.user_id = p.user_id
WHERE a.user_id = 'a';

-- when calculating aggregate subscribers, engagement, and income, use the following:
INSERT INTO account_analytics VALUES(
    account_analytics_seq.nextval,
    'a',
    0, 0, 0
);

UPDATE account_analytics 
    SET total_subscribers = (SELECT SUM(subscribers) from platforms),
    total_engagement = (SELECT SUM(engagement) from platforms),
    total_income = (SELECT SUM(income) from platforms)
WHERE user_id = 'a';
commit;


------------------------------------------------------------------------------------
CREATE TABLE platforms(
    platform_id number not null,
    platform_name varchar2 (12) not null,
    user_id varchar2(15) not null,
    subscribers number(12) not null,
    views number(12) default 0,
    likes number(12) default 0,
    comments number(12) default 0,
    engagement number(12) default 0,
    income number(12) not null,
    CONSTRAINT platforms_ck CHECK(platform_name IN('YouTube', 'TikTok', 'Facebook', 'Instagram', 'Twitter')),
    CONSTRAINT platforms_fk FOREIGN KEY(user_id) REFERENCES users(user_id),
    CONSTRAINT platforms_pk PRIMARY KEY(platform_id)
);
DROP TABLE platforms;

CREATE SEQUENCE platform_seq
START WITH 1
INCREMENT BY 1;
DROP SEQUENCE platform_seq;

UPDATE platforms
SET engagement = COALESCE(views, 0) + COALESCE(likes, 0) + COALESCE(comments,0); -- adds non-null views, likes, comments to calculate engagement

DELETE FROM platforms;
SELECT * FROM platforms; 
SELECT * FROM platforms WHERE platform_name = 'YouTube' AND user_id = 'aaaaaa';
SELECT platform_name FROM platforms WHERE user_id = 'a';
SELECT * FROM platforms WHERE platform_name = 'Facebook';

UPDATE platforms SET subscribers = 125, views = 123, likes = 123, comments = 123, income = 123 WHERE user_id = 'aaaaaa' AND platform_name = 'Twitter';

INSERT INTO platforms(platform_id, platform_name, user_id, subscribers, views, likes, comments, income) 
VALUES(platform_seq.nextval, 'YouTube', 'a', 1, 1, 1, 1, 1);
INSERT INTO platforms(platform_id, platform_name, user_id, subscribers, likes, comments, income)
VALUES(platform_seq.nextval, 'Instagram', 'a', 2, 2, 2, 2);
INSERT INTO platforms(platform_id, platform_name, user_id, subscribers, likes, income)
VALUES(platform_seq.nextval, 'Facebook', 'a', 3, 3, 3);
INSERT INTO platforms(platform_id, platform_name, user_id, subscribers, likes, income)
VALUES(platform_seq.nextval, 'Twitter', 'a', 4, 4, 4);
INSERT INTO platforms(platform_id, platform_name, user_id, subscribers, views, likes, income)
VALUES(platform_seq.nextval, 'TikTok', 'a', 5, 5, 5, 5);

DELETE FROM platforms WHERE user_id = 'aaaaaa' AND platform_name = 'Twitter';
commit;

SELECT * FROM account_analytics a, users u, platforms p
WHERE a.user_id = u.user_id and u.user_id = p.user_id 
AND a.user_id = 'a';
