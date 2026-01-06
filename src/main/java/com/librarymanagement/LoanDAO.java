package com.librarymanagement;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;

/*

public void extendDueDate(long loanId, int extraDays){}
Purpose: Allows a member to keep a book longer by updating the dueDate.
*/
public class LoanDAO {
    EntityManager em;

    LoanDAO(EntityManager em) {
        this.em = em;
    }

    public void issueBook(long memberId, long bookId) {
        Member member = em.find(Member.class, memberId);
        if (member == null) {
            System.out.println("incorrect member ID !");
            return;
        }
        Book book = em.find(Book.class, bookId);
        if (book == null) {
            System.out.println("Incorrect Book id !");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Sorry, this book is already checked out!");
            return;
        }
        try {
            em.getTransaction().begin();
            Loan loan = new Loan();
            loan.setBook(book);
            loan.setMember(member);
            loan.setIssueDate(LocalDate.now());
            loan.setDueDate(LocalDate.now().plusDays(15));
            book.setAvailable(false);
            member.setBorrowedBooksCount(member.getBorrowedBooksCount()+1);
            em.persist(loan);
            em.merge(book);
            em.merge(member);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error : " + e.getMessage());
        }
    }

    public void returnBook(long loanId) {
        Loan loan = em.find(Loan.class, loanId);
        if (loan == null) {
            System.out.println("Wrong loan id");
            return;
        }
        if (loan.getReturnDate() != null) {
            System.out.println("This loan record is already closed (Book already returned).");
            return;
        }
        Book book = loan.getBook();
        try {
            em.getTransaction().begin();
            book.setAvailable(true);
            loan.setReturnDate(LocalDate.now());
            long daysBetween = ChronoUnit.DAYS.between(loan.getIssueDate(), loan.getReturnDate());
            long overdueDays = daysBetween - 15;
            if (daysBetween > 15) {
                System.out.println("Book is late by " + overdueDays + " days.");
                System.out.println("Overdue fine will be : " + overdueDays * 10 + "₹");
            }
            Member member =loan.getMember();
            member.setBorrowedBooksCount(member.getBorrowedBooksCount()-1);
            em.merge(loan);
            em.merge(book);
            em.merge(member);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error : " + e.getMessage());
        }

    }

    public Loan findByID(long id) {
        Loan loan = em.find(Loan.class, id);
        if (loan == null) {
            System.out.println("Wrong loan ID!");
        }
        return loan;
    }

    public List<Loan> listAllActiveLoans() {
        List<Loan> loanlList = em.createQuery("SELECT l FROM Loan l WHERE l.returnDate IS NULL ", Loan.class)
                .getResultList();
        if (loanlList.isEmpty()) {
            System.out.println("No active loans!");
        }
        return loanlList;
    }

    public List<Loan> getLoansByMember(long memberId) {
        Member member = em.find(Member.class, memberId);
        if (member == null) {
            System.out.println("Wrong member ID");
            return new ArrayList<>();

        }
        List<Loan> loanlList = em.createQuery("SELECT l FROM Loan l WHERE l.member = :member ", Loan.class)
                .setParameter("member", member).getResultList();
        if (loanlList.isEmpty()) {
            System.out.println("No loans for this member ! ");
        }
        return loanlList;

    }

    public List<Loan> getLoansByBook(long bookId) {
        Book book = em.find(Book.class, bookId);
        if (book == null) {
            System.out.println("Wrong book id");
            return new ArrayList<>();
        }
        List<Loan> loanlList = em.createQuery("SELECT l FROM Loan l WHERE l.book = :book ", Loan.class)
                .setParameter("book", book).getResultList();
        if (loanlList.isEmpty()) {
            System.out.println("no loans for this book");
        }
        return loanlList;
    }

    public List<Loan> findOverdueLoans() {
        List<Loan> overdueLoans = em.createQuery(
                "SELECT l FROM Loan l WHERE l.returnDate IS NULL AND l.dueDate < :today", Loan.class)
                .setParameter("today", LocalDate.now())
                .getResultList();

        if (overdueLoans.isEmpty()) {
            System.out.println("No overdue loans found. Everything is on time!");
        }

        return overdueLoans;
    }

    public void extendDueDate(long loanId, long extraDays) {
        Loan loan = em.find(Loan.class, loanId);
        if (loan == null) {
            System.out.println("Incorrect loan id ");
            return;
        }
        try {
            em.getTransaction().begin();
            loan.setDueDate(loan.getDueDate().plusDays(extraDays));
            em.merge(loan);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error : " + e.getMessage());
        }

    }
}
