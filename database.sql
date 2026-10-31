-- 1. Створення бази даних
CREATE DATABASE IF NOT EXISTS kursova_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE kursova_db;

-- Дозволяє повторно створити таблиці з нуля.
-- Видаляє наявні таблиці та їхні дані.
DROP TABLE IF EXISTS assignments;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;

-- 2. Таблиця слухачів

CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Таблиця навчальних курсів

CREATE TABLE courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    duration_hours INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_course_duration
        CHECK (duration_hours > 0)
);

--  4. Таблиця зарахувань

CREATE TABLE enrollments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,

    status ENUM(
        'REQUESTED',
        'APPROVED',
        'ACTIVE',
        'COMPLETED'
    ) NOT NULL DEFAULT 'REQUESTED',

    f1 DECIMAL(5,2) NOT NULL DEFAULT 0,
    f2 DECIMAL(5,2) NOT NULL DEFAULT 0,
    f3 DECIMAL(5,2) NOT NULL DEFAULT 0,

    integral_score DECIMAL(5,2)
        GENERATED ALWAYS AS (
            ROUND(0.40 * f1 + 0.35 * f2 + 0.25 * f3, 2)
        ) STORED,

    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_enrollment_student
        FOREIGN KEY (student_id)
        REFERENCES students(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_enrollment_course
        FOREIGN KEY (course_id)
        REFERENCES courses(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_f1
        CHECK (f1 BETWEEN 0 AND 100),

    CONSTRAINT chk_f2
        CHECK (f2 BETWEEN 0 AND 100),

    CONSTRAINT chk_f3
        CHECK (f3 BETWEEN 0 AND 100),

    CONSTRAINT uq_student_course
        UNIQUE (student_id, course_id)
);

-- 5. Таблиця навчальних завдань

CREATE TABLE assignments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    max_score DECIMAL(5,2) NOT NULL DEFAULT 100,
    due_date DATE,

    CONSTRAINT fk_assignment_course
        FOREIGN KEY (course_id)
        REFERENCES courses(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_assignment_max_score
        CHECK (max_score > 0)
);

-- 6. Демонстраційні дані: слухачі

INSERT INTO students (first_name, last_name, email)
VALUES
    ('Максим', 'Банановий', 'maks.banana@example.com'),
    ('Марія', 'Яблученко', 'maria.iablunenko@example.com'),
    ('Тарас', 'Шевченко', 'taras.shevchenko@example.com');

-- 7. Демонстраційні дані: курси

INSERT INTO courses (title, description, duration_hours)
VALUES
    (
        'Основи програмування Java',
        'Вивчення основ мови програмування Java.',
        60
    ),
    (
        'Бази даних MySQL',
        'Проєктування та робота з реляційними базами даних.',
        40
    );

-- 8. Демонстраційні дані: зарахування

INSERT INTO enrollments
    (student_id, course_id, status, f1, f2, f3)
VALUES
    (1, 1, 'ACTIVE', 80, 90, 70),
    (2, 1, 'APPROVED', 60, 75, 80),
    (3, 2, 'REQUESTED', 0, 0, 0);

-- 9. Демонстраційні дані: навчальні завдання

INSERT INTO assignments
    (course_id, title, description, max_score, due_date)
VALUES
    (
        1,
        'Програма Java',
        'Створити та запустити програму Java.',
        100,
        '2026-11-15'
    ),
    (
        1,
        'Робота з класами',
        'Створити класи та реалізувати їхні методи.',
        100,
        '2026-11-25'
    ),
    (
        2,
        'Створення таблиць',
        'Створити таблиці та визначити зв’язки між ними.',
        100,
        '2026-11-20'
    );

-- 10. Перевірка створених таблиць

SHOW TABLES;

SELECT * FROM students;
SELECT * FROM courses;
SELECT * FROM enrollments;
SELECT * FROM assignments;
