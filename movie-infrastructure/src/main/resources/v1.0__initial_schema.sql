CREATE TABLE movie (
                       id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                       title           VARCHAR(150) NOT NULL,
                       genre           TINYINT UNSIGNED NOT NULL,
                       content_rating  CHAR(1) NOT NULL,
                       release_date    DATE NOT NULL,
                       runtime_minutes SMALLINT UNSIGNED NOT NULL,
                       thumbnail_url   VARCHAR(255) NOT NULL,
                       created_at      DATETIME(6) NOT NULL,
                       created_by      VARCHAR(50) NOT NULL,
                       modified_at     DATETIME(6) NOT NULL,
                       modified_by     VARCHAR(50) NOT NULL
);

CREATE TABLE theater (
                         id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                         name        VARCHAR(50) NOT NULL,
                         created_at  DATETIME(6) NOT NULL,
                         created_by  VARCHAR(50) NOT NULL,
                         modified_at DATETIME(6) NOT NULL,
                         modified_by VARCHAR(50) NOT NULL
);

CREATE TABLE screening (
                           id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                           movie_id    INT UNSIGNED NOT NULL,
                           theater_id  INT UNSIGNED NOT NULL,
                           start_time  TIME(6) NOT NULL,
                           end_time    TIME(6) NOT NULL,
                           created_at  DATETIME(6) NOT NULL,
                           created_by  VARCHAR(50) NOT NULL,
                           modified_at DATETIME(6) NOT NULL,
                           modified_by VARCHAR(50) NOT NULL
--                            CONSTRAINT FK_screening_theater FOREIGN KEY (theater_id) REFERENCES theater (id),
--                            CONSTRAINT FK_screening_movie FOREIGN KEY (movie_id) REFERENCES movie (id)
);

CREATE TABLE seat (
                      id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                      theater_id  INT UNSIGNED NOT NULL,
                      seat_row    VARCHAR(1) NOT NULL,  -- A ~ Z 로 제한 가능
                      seat_column TINYINT UNSIGNED NOT NULL,  -- 1 ~ 99 범위 사용
                      created_at  DATETIME(6) NOT NULL,
                      created_by  VARCHAR(50) NOT NULL,
                      modified_at DATETIME(6) NOT NULL,
                      modified_by VARCHAR(50) NOT NULL
--                       CONSTRAINT FK_seat_theater FOREIGN KEY (theater_id) REFERENCES theater (id)
);

CREATE TABLE member (
                        id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                        name        VARCHAR(50) NOT NULL,
                        email       VARCHAR(100) NOT NULL UNIQUE,
                        created_at  DATETIME(6) NOT NULL,
                        created_by  VARCHAR(50) NOT NULL,
                        modified_at DATETIME(6) NOT NULL,
                        modified_by VARCHAR(50) NOT NULL
);

CREATE TABLE reservation (
                             id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                             screening_id INT UNSIGNED NOT NULL,
                             member_id   INT UNSIGNED NOT NULL,
                             created_at  DATETIME(6) NOT NULL,
                             created_by  VARCHAR(50) NOT NULL,
                             modified_at DATETIME(6) NOT NULL,
                             modified_by VARCHAR(50) NOT NULL
--                              CONSTRAINT FK_reservation_screening FOREIGN KEY (screening_id) REFERENCES screening(id) ON DELETE CASCADE,
--                              CONSTRAINT FK_reservation_member FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
);

CREATE TABLE seat_reservation (
                                  id             INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                  reservation_id INT UNSIGNED NOT NULL,
                                  seat_id        INT UNSIGNED NOT NULL,
                                  screening_id   INT UNSIGNED NOT NULL
--                                 CONSTRAINT FK_seat_reservation_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE CASCADE,
--                                 CONSTRAINT FK_seat_reservation_seat FOREIGN KEY (seat_id) REFERENCES seat(id) ON DELETE CASCADE,
--                                 CONSTRAINT FK_seat_reservation_screening FOREIGN KEY (screening_id) REFERENCES screening(id) ON DELETE CASCADE
);


-- 인덱스 설정
-- title 과 genre 로 검색 기능을 제공하기 때문에 복합 인덱스 설정
CREATE INDEX idx_title_genre ON dev_database.movie (title, genre);
-- screening 테이블의 풀 스캔을 막기 위해 movie_id에 인덱스 생성
CREATE INDEX idx_screening_movie_id ON screening (movie_id);
