# My Java Library Project

This is a simple Library Management System I built using Java and Hibernate/JPA. I made this to learn how to connect a Java app to a MySQL database and how to handle relationships between tables (like how a book has one author).

## What the app does
You can run it in the terminal and it gives you a menu to:
* Register a new member (saves to database).
* Add an author first, then add books linked to that author ID.
* Borrow a book (it checks if the book is available first so two people can't take it).
* Return a book (updates the return date and makes the book available again).
* Search for books by typing part of the title.
* See a list of books that are past their due date.

## How I built it
* **Java 25**: The main language.
* **Hibernate/JPA**: Used this so I didn't have to write raw SQL strings for everything. It maps my Java classes (`Book.java`, `Member.java`, etc.) to MySQL tables.
* **MySQL**: Where all the data is stored.
* **Maven**: For managing the dependencies like the MySQL connector and Hibernate.



## How to run this on your computer
1. **Setup the Database**:
   Open MySQL Workbench and run: `CREATE DATABASE library_db;`.
2. **Change your password**: 
   Go to `src/main/resources/META-INF/persistence.xml` and change the `root` password to whatever your MySQL password is.
3. **Run the App**:
   Open the project in VS Code and run `App.java`. Hibernate will automatically create the tables for you!

## Things I learned
* How to use `EntityManager` to save and find data.
* Writing JPQL queries (like searching with `LIKE %search%`).
* Fixing the "Scanner" bug where it skips lines if you use `nextInt()` then `nextLine()`.
* Handling `ManyToOne` relationships between books and authors.
