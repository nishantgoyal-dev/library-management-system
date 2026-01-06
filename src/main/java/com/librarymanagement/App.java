package com.librarymanagement;

import java.util.Scanner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {
    // Main Entry Point
    static String mainMenu = """
            ======================================
                   LIBRARIAN COMMAND CENTER
            ======================================
            1. MEMBER MANAGEMENT
            2. INVENTORY CONTROL
            3. LOAN OPERATIONS
            4. REPORTS & ALERTS
            0. EXIT SYSTEM
            ======================================
            Select Module: """;

    // Module 1: Member Management
    static String memberMenu = """

            --- MEMBER MANAGEMENT ---
            1. Register New Member
            2. Search Member by Name
            3. Find Member by ID
            4. Update Member Info
            5. Delete Member
            0. Back to Main Menu
            Selection: """;

    // Module 2: Inventory Control
    static String inventoryMenu = """

            --- INVENTORY CONTROL ---
            1. Add New Author
            2. Add New Book
            3. Search Books by Title
            4. Find Book by ISBN
            5. View Author's Catalog
            6. Update/Delete Records
            0. Back to Main Menu
            Selection: """;

    // Module 3: Loan Operations
    static String loanMenu = """

            --- LOAN OPERATIONS ---
            1. Issue Book (Checkout)
            2. Return Book (Check-in)
            3. Extend Loan Period
            4. View Member's Loan History
            5. View Book's Loan History
            0. Back to Main Menu
            Selection: """;

    // Module 4: Reports & Statistics
    static String reportsMenu = """

            --- REPORTS & ALERTS ---
            1. View All Overdue Books
            2. List All Active Loans
            3. Library Statistics
            4. List All Available Books
            0. Back to Main Menu
            Selection: """;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("libraryPU");
        EntityManager em = emf.createEntityManager();
        System.out.println("Hello World!");
        String choice;
        Scanner sc = new Scanner(System.in);
        MemberDAO md = new MemberDAO(em);
        AuthorDAO ad = new AuthorDAO(em);
        BookDAO bd = new BookDAO(em);
        LoanDAO ld = new LoanDAO(em);
        outer: while (true) {
            System.out.println(mainMenu);
            choice = sc.nextLine();
            switch (choice) {
                case "1": {// 1. MEMBER MANAGEMENT
                    inner: while (true) {
                        System.out.println(memberMenu);
                        choice = sc.nextLine();
                        switch (choice) {

                            case "1": {// 1. Register New Member
                                System.out.println("Enter the name : ");
                                String name = sc.nextLine();
                                System.out.println("Enter the email : ");
                                String email = sc.nextLine();
                                md.registerMember(name, email);
                                break;
                            }
                            case "2": {// 2. Search Member by Name
                                System.out.println("Enter the name : ");
                                String name = sc.nextLine();
                                System.out.println("--- MEMBERS FOUND ---");
                                for (Member member : md.searchByName(name)) {
                                    System.out.println("Member ID : " + member.getId());
                                    System.out.println("Member name : " + member.getFullName());
                                    System.out.println("Member email : " + member.getEmail());
                                    System.out.println();
                                }
                                break;
                            }
                            case "3": {// 3. Find Member by ID
                                System.out.println("Enter the ID :");
                                long id = sc.nextLong();
                                sc.nextLine();
                                Member member = md.findByID(id);
                                if (member == null) {
                                    System.out.println("Member not found");
                                    break;
                                }
                                System.out.println("--- MEMBER FOUND ---");
                                System.out.println("Member ID : " + member.getId());
                                System.out.println("Member name : " + member.getFullName());
                                System.out.println("Member email : " + member.getEmail());
                                break;
                            }
                            case "4": {// 4. Update Member Info
                                System.out.println("Enter the ID :");
                                long id = sc.nextLong();
                                sc.nextLine();
                                System.out.println("Enter name : ");
                                String name = sc.nextLine();
                                System.out.println("Enter email : ");
                                String email = sc.nextLine();
                                md.updateMember(id, name, email);
                                break;
                            }
                            case "5": {// 5. Delete Member
                                System.out.println("Enter the ID :");
                                long id = sc.nextLong();
                                sc.nextLine();
                                md.deleteMember(id);
                                break;
                            }
                            case "0": {// 0. Back to Main Menu
                                break inner;
                            }
                            default:
                                System.out.println("Enter valid choice : ");
                                break;
                        }
                    }

                    break;
                }
                case "2": {// 2. INVENTORY CONTROL

                    inner: while (true) {
                        System.out.println(inventoryMenu);
                        choice = sc.nextLine();
                        switch (choice) {
                            case "1": {// 1. Add New Author
                                System.out.println("Enter Author name : ");
                                String name = sc.nextLine();
                                System.out.println("Enter authoe biography");
                                String bio = sc.nextLine();
                                ad.addAuthor(name, bio);
                                break;
                            }
                            case "2": {// 2. Add New Book
                                System.out.println("Enter book titile : ");
                                String titile = sc.nextLine();
                                System.out.println("Enter book isbn : ");
                                String isbn = sc.nextLine();
                                System.out.println("Enter add author id : ");
                                long author_id = sc.nextLong();
                                sc.nextLine();
                                bd.addBook(titile, isbn, author_id);
                                break;
                            }
                            case "3": {// 3. Search Books by Title
                                System.out.println("Enter book titile");
                                String title = sc.nextLine();
                                System.out.println("--- BOOKS FOUND ---");
                                for (Book book : bd.findByTitle(title)) {
                                    System.out.println("Book id : " + book.getId());
                                    System.out.println("Book title : " + book.getTitle());
                                    System.out.println("Book isbn : " + book.getIsbn());
                                    System.out.println();

                                }
                                break;
                            }
                            case "4": {// 4. Find Book by ISBN
                                System.out.println("Enter isbn : ");
                                String isbn = sc.nextLine();
                                Book book = bd.findByIsbn(isbn);
                                if (book==null) {
                                    break;
                                }
                                System.out.println("--- BOOK FOUND ---");
                                System.out.println("Book id : " + book.getId());
                                System.out.println("Book title : " + book.getTitle());
                                System.out.println("Book isbn" + book.getIsbn());
                                System.out.println();
                                break;
                            }
                            case "5": {// View Author's Catalog
                                System.out.println("Enter author id : ");
                                long id = sc.nextLong();
                                sc.nextLine();
                                System.out.println("--- BOOKS FOUND ---");
                                for (Book book : ad.getBooksByAuthor(id)) {
                                    System.out.println("Book id : " + book.getId());
                                    System.out.println("Book title : " + book.getTitle());
                                    System.out.println("Book isbn" + book.getIsbn());
                                    System.out.println();
                                }

                                break;
                            }
                            case "6": {// Update/Delete Records
                                System.out.println("This feature will be added later !");
                                break;
                            }
                            case "0": {// 0. Back to Main Menu
                                break inner;
                            }

                            default: {
                                System.out.println("Enter correct input ! ");
                                break;
                            }
                        }

                    }
                    break;
                }
                case "3": {// 3. LOAN OPERATIONS
                    inner: while (true) {
                        System.out.println(loanMenu);
                        choice = sc.nextLine();
                        switch (choice) {
                            case "1": {// 1. Issue Book (Checkout)
                                System.out.println("Enter member id : ");
                                long memberId = sc.nextLong();
                                sc.nextLine();
                                System.out.println("Enter book id : ");
                                long bookId = sc.nextLong();
                                sc.nextLine();
                                ld.issueBook(memberId, bookId);
                                break;
                            }
                            case "2": {// 2. Return Book (Check-in)
                                System.out.println("Enter loan id.");
                                long loanId = sc.nextLong();
                                sc.nextLine();
                                ld.returnBook(loanId);
                                break;
                            }
                            case "3": {// 3. Extend Loan Period
                                System.out.println("Enter loan id");
                                long loanId = sc.nextLong();
                                sc.nextLine();
                                System.out.println("Enter days to extend");
                                long days = sc.nextLong();
                                sc.nextLine();
                                ld.extendDueDate(loanId, days);
                                break;

                            }
                            case "4": {// 4. View Member's Loan History
                                System.out.println("Enter member ID : ");
                                long memberId = sc.nextLong();
                                sc.nextLine();
                                System.out.println("Found history ! ");
                                for (Loan loan : md.getBorrowingHistory(memberId)) {
                                    System.out.println("Loan id :" + loan.getId());
                                    System.out.println("Book " + loan.getBook().getTitle());
                                    System.out.println("Member : " + loan.getMember().getFullName());
                                    System.out.println("Borrow date : " + loan.getIssueDate());
                                    System.out.println("return date : " + loan.getReturnDate());
                                }
                                break;

                            }
                            case "5": {// 5. View Book's Loan History
                                System.out.println("Enter book id : ");
                                long bookId = sc.nextLong();
                                sc.nextLine();
                                System.out.println("Found history ! ");
                                for (Loan loan : ld.getLoansByBook(bookId)) {
                                    System.out.println("Loan id :" + loan.getId());
                                    System.out.println("Book " + loan.getBook().getTitle());
                                    System.out.println("Member : " + loan.getMember().getFullName());
                                    System.out.println("Borrow date : " + loan.getIssueDate());
                                    System.out.println("return date : " + loan.getReturnDate());
                                }
                                break;

                            }
                            case "0": {// 0. Back to Main Menu

                                break inner;

                            }

                            default:
                                System.out.println("Enter correct input !");
                                break;
                        }

                    }
                    break;
                }
                case "4": {// 4. REPORTS & ALERTS
                    inner: while (true) {
                        System.out.println(reportsMenu);
                        choice = sc.nextLine();
                        switch (choice) {
                            case "1": {// 1. View All Overdue Books
                                for (Loan loan : ld.findOverdueLoans()) {
                                    System.out.println("---ALL OVERDUE LOANS---");
                                    System.out.println("Loan id :" + loan.getId());
                                    System.out.println("Book " + loan.getBook().getTitle());
                                    System.out.println("Member : " + loan.getMember().getFullName());
                                    System.out.println("Borrow date : " + loan.getIssueDate());
                                    System.out.println("return date : " + loan.getReturnDate());
                                    System.out.println();
                                }
                                break;
                            }
                            case "2": {// 2. List All Active Loans
                                System.out.println("---ALL ACTIVE LOANS---");
                                for (Loan loan : ld.listAllActiveLoans()) {
                                    System.out.println("Loan id :" + loan.getId());
                                    System.out.println("Book " + loan.getBook().getTitle());
                                    System.out.println("Member : " + loan.getMember().getFullName());
                                    System.out.println("Borrow date : " + loan.getIssueDate());
                                    System.out.println("return date : " + loan.getReturnDate());
                                    System.out.println();
                                }
                                break;
                            }
                            case "3": {// 3. Library Statistics
                                System.out.println("--- LIBRARY STATISTICS ---");
                                System.out.println("Total Members: " + md.countTotalMembers());
                                System.out.println("Total Books:   " + bd.countTotalBooks());
                                System.out.println("Active Loans:  " + ld.listAllActiveLoans().size());
                                break;
                            }
                            case "4": {// 4. List All Available Books
                                System.out.println("--- AVAILABLE BOOKS ---");
                                for (Book book : bd.listAvailableBooks()) {
                                    System.out.println("Book id : " + book.getId());
                                    System.out.println("Book title : " + book.getTitle());
                                    System.out.println("Book isbn" + book.getIsbn());
                                    System.out.println();
                                }
                                break;
                            }
                            case "0": {// 0. Back to Main Menu
                                break inner;
                            }

                            default: {
                                System.out.println("Enter correct input ! ");
                                break;
                            }
                        }

                    }
                    break;
                }
                case "0":// 0. EXIT SYSTEM
                    break outer;
                default:
                    System.out.println("Enter correct input !");
                    break;
            }
        }
        sc.close();
        em.close();
        emf.close();
    }

}
