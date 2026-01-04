package com.librarymanagement;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {
    static String mainMenu = """
            ==== LIBRARY SYSTEM  ====
            1. Register New Member
            2. Add New Author
            3. Add New Book to Catalog
            4. Issue a Book (Borrow)
            5. Return a Book
            6. View All Overdue Books
            7. Search Book by Title
            0. Exit
            ======================================
            Enter choice: _
                        """;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("libraryPU");
        EntityManager em = emf.createEntityManager();
        System.out.println("Hello World!");
        String choice;
        Scanner sc = new Scanner(System.in);
        outer: while (true) {
            System.out.println(mainMenu);
            choice = sc.nextLine();
            switch (choice) {
                case "1": {// register new member
                    Member member = new Member();
                    em.getTransaction().begin();
                    System.out.println("Enter Name :");
                    member.setFullName(sc.nextLine());
                    System.out.println("Enter Email :");
                    member.setEmail(sc.nextLine());
                    member.setMembershipDate(LocalDate.now());
                    em.persist(member);
                    em.getTransaction().commit();
                    System.out.println("Member saved! Assigned ID: " + member.getId());
                    break;
                }
                case "2": {// Add New Author
                    Author author = new Author();
                    System.out.println("Enter name : ");
                    author.setName(sc.nextLine());
                    System.out.println("Enter biography ");
                    author.setBiography(sc.nextLine());
                    em.getTransaction().begin();
                    em.persist(author);
                    em.getTransaction().commit();
                    System.out.println("Author saved! Assigned ID: " + author.getId());
                    break;

                }
                case "3": {// Add New Book to Catalog
                    Book book = new Book();
                    System.out.println("Enter book title : ");
                    book.setTitle(sc.nextLine());
                    System.out.println("Enter ISBN : ");
                    book.setIsbn(sc.nextLine());
                    System.out.println("Enter Author id : ");
                    Author au = em.find(Author.class, sc.nextLong());
                    sc.nextLine();
                    if (au == null) {
                        System.out.println("Error: Author ID not found. Register the author first!");
                        break;
                    }
                    book.setAuthor(au);
                    book.setAvailable(true);
                    em.getTransaction().begin();
                    em.persist(book);
                    em.getTransaction().commit();
                    System.out.println("Book saved! Assigned ID: " + book.getId());
                    break;
                }
                case "4": { // Issue a Book (Borrow)
                    Loan loan = new Loan();
                    System.out.println("Enter Book id : ");
                    Book book = em.find(Book.class, sc.nextLong());
                    sc.nextLine();
                    System.out.println("Enter member ID : ");
                    Member member = em.find(Member.class, sc.nextLong());
                    sc.nextLine();
                    if (book == null || member == null) {
                        System.out.println("Book or member not found !");
                        break;
                    }
                    if (!book.isAvailable()) {
                        System.out.println("Error: This book is already borrowed by someone else!");
                        break;
                    }
                    loan.setBook(book);
                    loan.setMember(member);
                    loan.setIssueDate(LocalDate.now());
                    loan.setDueDate(loan.issueDate.plusDays(15));// 15 days default due time
                    book.setAvailable(false);
                    try {
                        em.getTransaction().begin();
                        em.persist(loan);
                        em.getTransaction().commit();
                    } catch (Exception e) {
                        if (em.getTransaction().isActive())
                            em.getTransaction().rollback();
                        System.out.println("Database error: " + e.getMessage());
                    }
                    System.out.println("Transaction saved! Assigned ID: " + loan.getId());
                    break;
                }
                case "5": { // return a book
                    System.out.println("Enter book id : ");
                    Book book = em.find(Book.class, sc.nextLong());
                    sc.nextLine();
                    if (book == null) {
                        System.out.println("Book not found ! ");
                        break;
                    }
                    if (book.isAvailable()) {
                        System.out.println("book was not issued");
                        break;
                    }
                    Loan loan = em.createQuery(
                            "SELECT l FROM Loan l WHERE l.book = :book AND l.returnDate IS NULL", Loan.class)
                            .setParameter("book", book).getSingleResult();
                    em.getTransaction().begin();
                    loan.setReturnDate(LocalDate.now());
                    book.setAvailable(true);
                    em.getTransaction().commit();
                    System.out.println("Book is returned ! ");
                    break;
                }
                case "6": {// View All Overdue Books
                    List<Loan> overdueLoans = em.createQuery(
                            "SELECT l FROM Loan l WHERE l.dueDate < CURRENT_DATE AND l.returnDate IS NULL",
                            Loan.class)
                            .getResultList();
                    if (overdueLoans.isEmpty()) {
                        System.out.println("No overdue books found ! ");
                    } else {
                        System.out.println("\n--- OVERDUE BOOKS ---");
                        for (Loan l : overdueLoans) {
                            System.out.println("Book: " + l.getBook().getTitle() +
                                    " | Member: " + l.getMember().getFullName() +
                                    " | Due Was: " + l.getDueDate());
                        }
                    }
                    break;
                }
                case "7": {// search book by title
                    System.out.println("Enter book title to search: ");
                    String searchInput = sc.nextLine();

                    // The Query
                    List<Book> foundBooks = em.createQuery(
                            "SELECT b FROM Book b WHERE b.title LIKE :search", Book.class)
                            .setParameter("search", "%" + searchInput + "%")
                            .getResultList();

                    // Handling the results
                    if (foundBooks.isEmpty()) {
                        System.out.println("No books found matching '" + searchInput + "'");
                    } else {
                        System.out.println("\n--- Search Results ---");
                        for (Book b : foundBooks) {
                            String status = b.isAvailable() ? "[Available]" : "[Borrowed]";
                            System.out.println("ID: " + b.getId() +
                                    " | Title: " + b.getTitle() +
                                    " | Author: " + b.getAuthor().getName() +
                                    " | Status: " + status);
                        }
                    }
                    break;

                }
                case "0": {
                    break outer;
                }
                default:
                    System.out.println("Enter correct input");
                    break;
            }
        }
        sc.close();

    }

}
