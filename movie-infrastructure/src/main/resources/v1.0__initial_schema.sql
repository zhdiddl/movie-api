CREATE TABLE movie (
                       id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,  -- BIGINT 대신 UNSIGNED INT 로 리소스 절약
                       title           VARCHAR(255) NOT NULL,
                       genre           VARCHAR(255) NOT NULL,
                       content_rating  VARCHAR(255) NOT NULL,
                       release_date    DATE         NOT NULL,
                       runtime_minutes INT          NOT NULL,
                       thumbnail_url   VARCHAR(255) NOT NULL,
                       created_at      DATETIME(6)  NOT NULL,
                       created_by      VARCHAR(255) NOT NULL,
                       modified_at     DATETIME(6)  NOT NULL,
                       modified_by     VARCHAR(255) NOT NULL
);

CREATE TABLE theater (
                         id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,  
                         name        VARCHAR(255) NOT NULL,
                         created_at  DATETIME(6)  NOT NULL,
                         created_by  VARCHAR(255) NOT NULL,
                         modified_at DATETIME(6)  NOT NULL,
                         modified_by VARCHAR(255) NOT NULL
);

CREATE TABLE screening (
                           id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,  
                           movie_id    INT UNSIGNED NOT NULL,                   
                           theater_id  INT UNSIGNED NOT NULL,                   
                           start_time  TIME(6) NOT NULL,
                           end_time    TIME(6) NOT NULL,
                           created_at  DATETIME(6)  NOT NULL,
                           created_by  VARCHAR(255) NOT NULL,
                           modified_at DATETIME(6)  NOT NULL,
                           modified_by VARCHAR(255) NOT NULL,
--                            CONSTRAINT FK_screening_theater FOREIGN KEY (theater_id) REFERENCES theater (id),
--                            CONSTRAINT FK_screening_movie FOREIGN KEY (movie_id) REFERENCES movie (id)
);

CREATE TABLE seat (
                      id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,  
                      theater_id  INT UNSIGNED NOT NULL,                   
                      seat_row    VARCHAR(255) NULL,
                      seat_column INT NULL,
                      created_at  DATETIME(6)  NOT NULL,
                      created_by  VARCHAR(255) NOT NULL,
                      modified_at DATETIME(6)  NOT NULL,
                      modified_by VARCHAR(255) NOT NULL,
--                       CONSTRAINT FK_seat_theater FOREIGN KEY (theater_id) REFERENCES theater (id)
);
