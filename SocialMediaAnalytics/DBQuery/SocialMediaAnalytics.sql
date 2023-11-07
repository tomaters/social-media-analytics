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

-- add on delete cascade functions to foreign keys on users(user_id)
ALTER TABLE account_analytics
    DROP CONSTRAINT account_analytics_fk;
ALTER TABLE platforms
    DROP CONSTRAINT platforms_fk;
ALTER TABLE account_analytics
    ADD CONSTRAINT account_analytics_fk FOREIGN KEY(user_id) REFERENCES users(user_id)
    ON DELETE CASCADE;
ALTER TABLE platforms
    ADD CONSTRAINT platforms_fk FOREIGN KEY(user_id) REFERENCES users(user_id)
    ON DELETE CASCADE;    

-- procedure to delete user account
CREATE OR REPLACE PROCEDURE delete_user
    (username IN varchar2, statement_result OUT varchar2)
IS
    result_temp number;
BEGIN
    -- check whether username exists in users
    SELECT COUNT(*) INTO result_temp FROM users
    WHERE user_id = username;
    -- if it exists, should be 1
    IF result_temp = 1 THEN
        DELETE FROM users WHERE user_id = username;
        statement_result := 'User deleted successfully';
    ELSE
        statement_result := 'User not found';
    END IF
        COMMIT;
END;
/
-- procedure to add an acocunt
CREATE OR REPLACE PROCEDURE add_user(
    new_user_name IN users.user_name%type,
    new_user_id IN users.user_id%type,
    new_user_pass IN users.user_pass%type,
    new_user_email IN users.user_email%type,
    statement_result OUT varchar2)
IS
    -- variables
    result_temp number := 0;
BEGIN
    SELECT COUNT(*) INTO result_temp FROM users WHERE user_id = new_user_id;
    -- if username does not already exist, result_temp will be 0
    IF(result_temp = 0) THEN
        INSERT INTO users VALUES(new_user_name, new_user_id, new_user_pass, new_user_email);
        statement_result := 'User added successfully';
    ELSE
        statement_result := 'User failed to add. Try again';
    END IF
    COMMIT;
END;
/
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
-- updating account analytics values
UPDATE account_analytics 
    SET total_subscribers = (SELECT SUM(subscribers) from platforms),
    total_engagement = (SELECT SUM(engagement) from platforms),
    total_income = (SELECT SUM(income) from platforms)
WHERE user_id = 'a';
commit;
/
-- automated updating via trigger after insert
CREATE OR REPLACE TRIGGER analytics_after_insert
    AFTER INSERT ON platforms
BEGIN
    UPDATE account_analytics a SET 
        a.total_subscribers = (SELECT SUM(p.subscribers) FROM platforms p WHERE a.user_id = p.user_id),
        a.total_engagement = (SELECT SUM(p.engagement) FROM platforms p WHERE a.user_id = p.user_id),  
        a.total_income = (SELECT SUM(p.income) FROM platforms p WHERE a.user_id = p.user_id)
    WHERE a.user_id IN (SELECT p.user_id FROM platforms p);
END;
/
-- automated updating via trigger after delete
CREATE OR REPLACE TRIGGER analytics_after_delete
    AFTER DELETE ON platforms 
BEGIN
    UPDATE account_analytics a SET 
        a.total_subscribers = (SELECT SUM(p.subscribers) FROM platforms p WHERE a.user_id = p.user_id),
        a.total_engagement = (SELECT SUM(p.engagement) FROM platforms p WHERE a.user_id = p.user_id),  
        a.total_income = (SELECT SUM(p.income) FROM platforms p WHERE a.user_id = p.user_id)
    WHERE a.user_id IN (SELECT p.user_id FROM platforms p);
END;
/
-- automated updating via trigger after update
CREATE OR REPLACE TRIGGER analytics_after_update
    AFTER UPDATE ON platforms
BEGIN
    UPDATE account_analytics a SET 
        a.total_subscribers = (SELECT SUM(p.subscribers) FROM platforms p WHERE a.user_id = p.user_id),
        a.total_engagement = (SELECT SUM(p.engagement) FROM platforms p WHERE a.user_id = p.user_id),  
        a.total_income = (SELECT SUM(p.income) FROM platforms p WHERE a.user_id = p.user_id)
    WHERE a.user_id IN (SELECT p.user_id FROM platforms p);
END;
/
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

-- testing
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

-- delete platform procedure
CREATE OR REPLACE PROCEDURE edit_platform(
    new_subscribers IN platforms.subscribers%type,
    new_views IN platforms."VIEWS"%type,
    new_likes IN platforms.likes%type,
    new_comments IN platforms.comments%type,
    new_income IN platforms.income%type,
    v_user_id IN platforms.user_id%type,
    v_platform_name IN platforms.platform_name%type,
    statement_result OUT varchar2)   
IS
    result_temp number;
BEGIN
    -- check whether user's platform exists
    SELECT COUNT(*) INTO result_temp FROM platforms
    WHERE user_id = v_user_id AND platform_name = v_platform_name;
    -- if it exists, should be 1
    IF result_temp = 1 THEN
        UPDATE platforms SET subscribers = new_subscribers, "VIEWS" = new_views, 
            likes = new_likes, comments = new_comments, income = new_income
        WHERE user_id = v_user_id AND platform_name = v_platform_name;
        statement_result := 'Platform updated successfully';
    ELSE
        statement_result := 'Platform failed to update. Try again';
    END IF
    COMMIT;
END;
/
-- add platform procedure
CREATE OR REPLACE PROCEDURE add_platform(
    v_platform_name IN platforms.platform_name%type,
    v_user_id IN platforms.user_id%type,
    new_subscribers IN platforms.subscribers%type,
    new_views IN platforms."VIEWS"%type,
    new_likes IN platforms.likes%type,
    new_comments IN platforms.comments%type,
    new_income IN platforms.income%type,  
    statement_result OUT varchar2)
IS
    -- variables
    v_platform_id platforms.platform_id%type := platform_seq.nextval;
    v_engagement platforms.engagement%type;
    result_temp number := 0;
BEGIN
    SELECT COUNT(*) INTO result_temp FROM platforms 
        WHERE user_id = v_user_id AND platform_name = v_platform_name;
    -- if platform under username does not exist, result_temp will be 0
    IF(result_temp = 0) THEN
       v_engagement := COALESCE(new_views, 0) + COALESCE(new_likes, 0) + COALESCE(new_comments,0);
        INSERT INTO platforms (platform_id, platform_name, user_id, subscribers, 
            views, likes, comments, engagement, income)  
        VALUES(v_platform_id, v_platform_name, v_user_id, new_subscribers, 
            new_views, new_likes, new_comments, v_engagement, new_income);
        statement_result := 'Platform details added successfully';
    ELSE
        statement_result := 'That platform is already registered. Try again';
    END IF
    COMMIT;
END;
/

-- trigger to calculate engagement (sum of views, likes, comments)
-- DO NOT CREATE; causes recursive error because it modifies the same table
CREATE OR REPLACE TRIGGER calculate_engagement
    AFTER INSERT ON platforms FOR EACH ROW
BEGIN
    UPDATE platforms SET engagement = COALESCE(views, 0) + COALESCE(likes, 0) 
        + COALESCE(comments,0);
END;
/
DROP TRIGGER calculate_engagement;
