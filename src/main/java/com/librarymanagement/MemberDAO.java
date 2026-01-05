package com.librarymanagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;

public class MemberDAO {
    private EntityManager em;

    MemberDAO(EntityManager em) {
        this.em = em;
    }

    public void registerMember(String name, String email) {
        Member member = new Member();
        member.setEmail(email);
        member.setFullName(name);
        member.setMembershipDate(LocalDate.now());
        try {
            em.getTransaction().begin();
            em.persist(member);
            em.getTransaction().commit();
            System.out.println("Success: Member saved with ID: " + member.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Database Error: " + e.getMessage());
        }

    }

    public void deleteMember(long id) {
        Member member = em.find(Member.class, id);
        if (member == null) {
            System.out.println("Enter correct id");
            return;
        }
        try {
            List<Loan> loanlList = em
                    .createQuery("SELECT l FROM Loan l WHERE (l.member = :member AND l.returnDate is null)", Loan.class)
                    .setParameter("member", member)
                    .getResultList();
            if (!loanlList.isEmpty()) {
                System.out.println("There are active loans on this account ! Can't delete this account");
                return;
            } else {
                try {
                    em.getTransaction().begin();
                    em.remove(member);
                    em.getTransaction().commit();
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                        System.out.println("Error : " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    public Member findByID(long id) {
        Member member = em.find(Member.class, id);
        if (member != null) {
            System.out.println("Member found: " + member.getFullName());
        } else {
            System.out.println("Member not found!");
        }
        return member;
    }

    public void updateMember(long id, String name, String email) {
        Member member = em.find(Member.class, id);
        if (member == null) {
            System.out.println("Incorrect member ID !");
            return;
        }
        member.setFullName(name);
        member.setEmail(email);
        try {
            em.getTransaction().begin();
            em.merge(member);
            em.getTransaction().commit();
            System.out.println("Success: Member details updated : ");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Database Error: " + e.getMessage());
        }

    }

    public List<Member> searchByName(String name) {
        try {

            List<Member> memberList = em
                    .createQuery(" SELECT m FROM Member m WHERE (m.fullName LIKE :name) ", Member.class)
                    .setParameter("name", "%" + name + "%").getResultList();
            return memberList;
        } catch (Exception e) {
            System.out.println("Error searching for members: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // find by email
    public Member searchByEmail(String email) {
        try {
            Member member = em
                    .createQuery(" SELECT m FROM Member m WHERE (m.email = :email) ", Member.class)
                    .setParameter("email", email).getSingleResult();
            return member;
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        } catch (Exception e) {
            System.out.println("Error searching for members: " + e.getMessage());
            return null;
        }
    }

    // get borrowing history
    public List<Loan> getBorrowingHistory(long memberId) {
        try {

            Member member = em.find(Member.class, memberId);
            if (member == null) {
                System.out.println("No members found for this ID !");
                return new ArrayList<>();
            }
            List<Loan> loanlList = em.createQuery("SELECT l FROM Loan l WHERE l.member = :member", Loan.class)
                    .setParameter("member", member)
                    .getResultList();
            return loanlList;
        } catch (Exception e) {
            System.out.println("Error fetching history: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // count active loans
    public long countActiveLoans(long id) {
        Member member = em.find(Member.class, id);
        if (member == null) {
            System.out.println("Enter correct id");
            return 0;
        }

        try {
            Long totalCount = em
                    .createQuery("SELECT COUNT(l) FROM Loan l WHERE (l.member = :member AND l.returnDate is null)",
                            Long.class)
                    .setParameter("member", member)
                    .getSingleResult();
            return totalCount;
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            return 0;
        }

    }

    // list all members
    public List<Member> listAllMembers() {
        try {
            // Use the Entity name "Member", not the table name "members"
            return em.createQuery("SELECT m FROM Member m", Member.class)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Error fetching members: " + e.getMessage());
            return new ArrayList<>(); // Return empty list to prevent NullPointerExceptions
        }
    }

    // find member with overdue books
    public List<Member> findMembersWithOverdueBooks() {
        try {
            String jpql = "SELECT DISTINCT m FROM Member m " +
                    "JOIN Loan l ON l.member = m " +
                    "WHERE l.returnDate IS NULL " +
                    "AND l.dueDate < CURRENT_DATE";

            return em.createQuery(jpql, Member.class)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Error finding overdue members: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
