Welcome to The Library.

This is a school project made with IntelliJ Java, MySQL and JDBC.
To run this project you need to:
- Clone this repository.
- Copy and paste the pre-written database setup in MySQL (you'll find it below).
- Adjust your login and password in the class Database.

When you run the program you can choose to act as user or admin.
The admin got a few more options on hand to be able to keep up with their work.
However the user can still do a few things along their line of interrests.

Check it out for yourself! Hope you like it.

SQL-Script:


CREATE DATABASE library;
USE library;

CREATE TABLE users (
 id INT AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(100) NOT NULL,
 password VARCHAR(255) NOT NULL,
 email VARCHAR(100) NOT NULL UNIQUE,
 role ENUM('user', 'admin') NOT NULL
);

CREATE TABLE books (
 id INT AUTO_INCREMENT PRIMARY KEY,
 title VARCHAR(100) NOT NULL,
 author VARCHAR(100) NOT NULL,
 year INT,
 genre VARCHAR(100) NOT NULL,
 available BOOLEAN DEFAULT TRUE
);

CREATE TABLE loans (
 id INT AUTO_INCREMENT PRIMARY KEY,
 user_id INT,
 book_id INT,
 loan_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 return_date DATE,
 FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
-- Start user and admin

INSERT INTO users (name, password, email, role) VALUES
   ('Alice', 'password123', 'alice@example.com', 'USER'),
   ('Bob', 'password456', 'bob@example.com', 'ADMIN');
   
   -- Start library
   
   INSERT INTO books (title, author, year, genre, available) VALUES
('Dracula', 'Bram Stoker', 1897, 'Horror', TRUE),
('The Duke and I', 'Julia Quinn', 2000, 'Romance', TRUE),
('PS. I Love You', 'Cecelia Ahern', 2004, 'Romance', TRUE),
('Harry Potter and the Philosopher\'s Stone', 'J.K. Rowling', 1997, 'Fantasy', TRUE),
('Mastering the Art of French Cooking', 'Julia Child', 1961, 'Cookbook', TRUE),
('Sju sorters kakor', 'ICA Provkök', 1945, 'Cookbook', TRUE),
('The Very Hungry Caterpillar', 'Eric Carle', 1969, 'Children', TRUE),
('Where the Wild Things Are', 'Maurice Sendak', 1963, 'Children', TRUE),
('1984', 'George Orwell', 1949, 'Dystopian', TRUE),
('The Great Gatsby', 'F. Scott Fitzgerald', 1925, 'Classic', TRUE);

-- Start with borrowed books

INSERT INTO loans (user_id, book_id, loan_date, return_date) VALUES 
(1, 3, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 1 MONTH));
INSERT INTO loans (user_id, book_id, loan_date, return_date) VALUES 
(1, 7, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 1 MONTH));
