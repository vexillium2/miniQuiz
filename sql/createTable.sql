-- -- 创建库
-- CREATE DATABASE IF NOT EXISTS miniQuiz;

-- 切换库
\c mini_quiz;

-- 用户表
CREATE TABLE IF NOT EXISTS "user"
(
    id              BIGINT PRIMARY KEY,
    userAccount     VARCHAR(256) NOT NULL,
    userPassword    VARCHAR(512) NOT NULL,
    unionId         VARCHAR(256) NULL,
    mpOpenId        VARCHAR(256) NULL,
    userName        VARCHAR(256) NULL,
    userAvatar      VARCHAR(1024) NULL,
    userProfile     VARCHAR(512) NULL,
    userRole        VARCHAR(256) DEFAULT 'user' NOT NULL,
    createTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updateTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    isDelete        SMALLINT DEFAULT 0 NOT NULL,
    CONSTRAINT idx_unionId UNIQUE (unionId)
);

-- 应用表
CREATE TABLE IF NOT EXISTS app
(
    id              BIGINT PRIMARY KEY,
    appName         VARCHAR(128) NOT NULL,
    appDesc         VARCHAR(2048) NULL,
    appIcon         VARCHAR(1024) NULL,
    appType         SMALLINT DEFAULT 0 NOT NULL,
    scoringStrategy SMALLINT DEFAULT 0 NOT NULL,
    reviewStatus    INT DEFAULT 0 NOT NULL,
    reviewMessage   VARCHAR(512) NULL,
    reviewerId      BIGINT NULL,
    reviewTime      TIMESTAMP NULL,
    userId          BIGINT NOT NULL,
    createTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updateTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    isDelete        SMALLINT DEFAULT 0 NOT NULL,
    CONSTRAINT idx_appName UNIQUE (appName)
);

-- 题目表
CREATE TABLE IF NOT EXISTS question
(
    id              BIGINT PRIMARY KEY,
    questionContent TEXT NULL,
    appId           BIGINT NOT NULL,
    userId          BIGINT NOT NULL,
    createTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updateTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    isDelete        SMALLINT DEFAULT 0 NOT NULL,
    CONSTRAINT idx_appId FOREIGN KEY (appId) REFERENCES app(id)
);

-- 评分结果表
CREATE TABLE IF NOT EXISTS scoring_result
(
    id               BIGINT PRIMARY KEY,
    resultName       VARCHAR(128) NOT NULL,
    resultDesc       TEXT NULL,
    resultPicture    VARCHAR(1024) NULL,
    resultProp       VARCHAR(128) NULL,
    resultScoreRange INT NULL,
    appId            BIGINT NOT NULL,
    userId           BIGINT NOT NULL,
    createTime       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updateTime       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    isDelete         SMALLINT DEFAULT 0 NOT NULL,
    CONSTRAINT idx_appId FOREIGN KEY (appId) REFERENCES app(id)
);

-- 用户答题记录表
CREATE TABLE IF NOT EXISTS user_answer
(
    id              BIGINT PRIMARY KEY,
    appId           BIGINT NOT NULL,
    appType         SMALLINT DEFAULT 0 NOT NULL,
    scoringStrategy SMALLINT DEFAULT 0 NOT NULL,
    choices         TEXT NULL,
    resultId        BIGINT NULL,
    resultName      VARCHAR(128) NULL,
    resultDesc      TEXT NULL,
    resultPicture   VARCHAR(1024) NULL,
    resultScore     INT NULL,
    userId          BIGINT NOT NULL,
    createTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updateTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    isDelete        SMALLINT DEFAULT 0 NOT NULL,
    CONSTRAINT idx_appId FOREIGN KEY (appId) REFERENCES app(id),
    CONSTRAINT idx_userId FOREIGN KEY (userId) REFERENCES "user"(id)
);

-- 用户答题记录表（分表 0）
CREATE TABLE IF NOT EXISTS user_answer_0
(
    id              BIGINT PRIMARY KEY,
    appId           BIGINT NOT NULL,
    appType         SMALLINT DEFAULT 0 NOT NULL,
    scoringStrategy SMALLINT DEFAULT 0 NOT NULL,
    choices         TEXT NULL,
    resultId        BIGINT NULL,
    resultName      VARCHAR(128) NULL,
    resultDesc      TEXT NULL,
    resultPicture   VARCHAR(1024) NULL,
    resultScore     INT NULL,
    userId          BIGINT NOT NULL,
    createTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updateTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    isDelete        SMALLINT DEFAULT 0 NOT NULL,
    CONSTRAINT idx_appId FOREIGN KEY (appId) REFERENCES app(id),
    CONSTRAINT idx_userId FOREIGN KEY (userId) REFERENCES "user"(id)
);

-- 用户答题记录表（分表 1）
CREATE TABLE IF NOT EXISTS user_answer_1
(
    id              BIGINT PRIMARY KEY,
    appId           BIGINT NOT NULL,
    appType         SMALLINT DEFAULT 0 NOT NULL,
    scoringStrategy SMALLINT DEFAULT 0 NOT NULL,
    choices         TEXT NULL,
    resultId        BIGINT NULL,
    resultName      VARCHAR(128) NULL,
    resultDesc      TEXT NULL,
    resultPicture   VARCHAR(1024) NULL,
    resultScore     INT NULL,
    userId          BIGINT NOT NULL,
    createTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updateTime      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    isDelete        SMALLINT DEFAULT 0 NOT NULL,
    CONSTRAINT idx_appId FOREIGN KEY (appId) REFERENCES app(id),
    CONSTRAINT idx_userId FOREIGN KEY (userId) REFERENCES "user"(id)
);
