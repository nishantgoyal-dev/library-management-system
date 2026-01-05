package com.librarymanagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }


@Test
public void testMemberLoanIntegration() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("libraryPU");
    EntityManager em = emf.createEntityManager();
    MemberDAO md = new MemberDAO(em);

    try {
        em.getTransaction().begin();

        // 1. Setup: Create a Member
        Member alice = new Member();
        alice.setFullName("Alice Test");
        alice.setEmail("alice.integration@test.com");
        alice.setMembershipDate(java.time.LocalDate.now());
        em.persist(alice);

        // 2. Setup: Create a Book 
        Book book = new Book();
        book.setTitle("Test-Driven Development");
        book.setAvailable(true);
        em.persist(book);

        // 3. Setup: Create a Loan
        Loan loan = new Loan();
        loan.setMember(alice);
        loan.setBook(book);
        loan.setIssueDate(java.time.LocalDate.now());
        loan.setDueDate(java.time.LocalDate.now().plusDays(14));
        em.persist(loan);

        em.getTransaction().commit();
        System.out.println("✅ Setup Complete: Member, Book, and Loan created.");

        // --- THE ACTUAL DAO TESTS ---

        // Test 4: Count Active Loans
        long count = md.countActiveLoans(alice.getId());
        assertEquals("Should have exactly 1 active loan", 1, count);
        System.out.println("✅ countActiveLoans works: " + count);

        // Test 5: Borrowing History
        List<Loan> history = md.getBorrowingHistory(alice.getId());
        assertEquals("History should contain 1 record", 1, history.size());
        System.out.println("✅ getBorrowingHistory works: Book is " + history.get(0).getBook().getTitle());

        // Test 6: Safe Delete Check (Should fail to delete because of the loan)
        // We expect the message "There are active loans on this account!" in console
        md.deleteMember(alice.getId());
        assertNotNull("Member should NOT be deleted while they have a loan", md.findByID(alice.getId()));
        System.out.println("✅ Safe Delete logic works (Delete prevented)");

        // --- CLEANUP ---
        em.getTransaction().begin();
        em.remove(loan);
        em.remove(book);
        em.remove(alice);
        em.getTransaction().commit();
        System.out.println("✅ Cleanup Complete.");

    } catch (Exception e) {
        if (em.getTransaction().isActive()) em.getTransaction().rollback();
        fail("Integration test failed: " + e.getMessage());
    } finally {
        em.close();
        emf.close();
    }
}
}
