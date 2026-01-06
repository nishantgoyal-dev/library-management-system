package com.librarymanagement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import com.librarymanagement.util.InputValidator;;

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
        MemberDAO md = new MemberDAO(em);
        AuthorDAO ad = new AuthorDAO(em);
        BookDAO bd = new BookDAO(em);
        LoanDAO ld = new LoanDAO(em);
        outer: while (true) {
            System.out.println(mainMenu);
            choice = InputValidator.getNonEmptyString("selection : ");
            switch (choice) {
                case "1": {// 1. MEMBER MANAGEMENT
                    inner: while (true) {
                        System.out.println(memberMenu);
                        choice = InputValidator.getNonEmptyString("selection : ");
                        switch (choice) {

                            case "1": {// 1. Register New Member
                                String name = InputValidator.getNonEmptyString( "Enter the name : ");
                                String email = InputValidator.getValidEmail("Enter the email :");
                                md.registerMember(name, email);
                                break;
                            }
                            case "2": {// 2. Search Member by Name
                                String name = InputValidator.getNonEmptyString("Enter the name : ");
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
                                long id = InputValidator.getLongInput("Enter member id : ");
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
                                long id = InputValidator.getLongInput("Enter the ID : ");
                                String name = InputValidator.getNonEmptyString("Enter the new name : ");
                                String email = InputValidator.getValidEmail("Enter email : ");
                                md.updateMember(id, name, email);
                                break;
                            }
                            case "5": {// 5. Delete Member
                                long id = InputValidator.getLongInput("Enter the ID : ");
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
                        choice = InputValidator.getNonEmptyString("selection : ");
                        switch (choice) {
                            case "1": {// 1. Add New Author
                                String name = InputValidator.getNonEmptyString("Enter Author name : ");
                                String bio = InputValidator.getNonEmptyString("Enter Author biography : ");
                                ad.addAuthor(name, bio);
                                break;
                            }
                            case "2": {// 2. Add New Book
                                String titile = InputValidator.getNonEmptyString("Enter book title : ");
                                String isbn = InputValidator.getValidISBN("Enter book isbn : ");
                                long author_id = InputValidator.getLongInput("Enter author id : ");
                                bd.addBook(titile, isbn, author_id);
                                break;
                            }
                            case "3": {// 3. Search Books by Title
                                String title = InputValidator.getNonEmptyString("Enter book title : ");
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
                                String isbn = InputValidator.getValidISBN("Enter book isbn : ");
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
                                long id = InputValidator.getLongInput("Enter author id : ");
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
                        choice = InputValidator.getNonEmptyString("selection : ");
                        switch (choice) {
                            case "1": {// 1. Issue Book (Checkout)
                                long memberId = InputValidator.getLongInput("Enter member id : ");
                                long bookId = InputValidator.getLongInput("Enter book id : ");
                                ld.issueBook(memberId, bookId);
                                break;
                            }
                            case "2": {// 2. Return Book (Check-in)
                                long loanId = InputValidator.getLongInput("Enter loan id : ");
                                ld.returnBook(loanId);
                                break;
                            }
                            case "3": {// 3. Extend Loan Period
                                long loanId = InputValidator.getLongInput("Enter loan id : ");
                                long days = InputValidator.getLongInput("Enter days to extend: ");
                                ld.extendDueDate(loanId, days);
                                break;

                            }
                            case "4": {// 4. View Member's Loan History
                                long memberId = InputValidator.getLongInput("Enter member id : ");
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
                                long bookId = InputValidator.getLongInput("Enter book id : ");
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
                        choice = InputValidator.getNonEmptyString("selection : ");
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
        em.close();
        emf.close();
    }

}
