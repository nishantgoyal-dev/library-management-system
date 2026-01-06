package com.librarymanagement;

import java.util.List;

import jakarta.persistence.EntityManager;

public class BookDAO {
    private EntityManager em;

    BookDAO(EntityManager em) {
        this.em = em;
    }

    public void addBook(String name, String isbn, long author_id) {
        Author author = em.find(Author.class, author_id);
        if (author == null) {
            System.out.println("Wrong author ID!");
            return;
        }
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(name);
        book.setIsbn(isbn);
        try {
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error : " + e.getMessage());
        }
    }

    public Book findByID(long id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            System.out.println("Book found: " + book.getTitle());
        } else {
            System.out.println("Book not found!");
        }
        return book;
    }

    public void updateBook(long id, String title, String isbn, long author_id) {
        Author author = em.find(Author.class, author_id);
        if (author == null) {
            System.out.println("Wrong author ID!");
            return;
        }
        Book book = em.find(Book.class, id);
        if (book == null) {
            System.out.println("Book with ID " + id + " not found!");
            return;
        }
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setAuthor(author);
        try {
            em.getTransaction().begin();
            em.merge(book);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error : " + e.getMessage());
        }
    }

    public void deleteBook(long id) {
        Book book = findByID(id);
        if (!book.isAvailable()) {
            System.out.println("Book is borrowed by some one, can't delete now! ");
            return;
        }
        try {
            em.getTransaction().begin();
            em.remove(book);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Database Error: Cannot delete book because it has historical loan records.");
            System.out.println("Error : " + e.getMessage());
        }
    }

    public List<Book> listAllBooks() {
        List<Book> bookList = em.createQuery("SELECT b FROM Book b ", Book.class).getResultList();
        if (bookList.isEmpty()) {
            System.out.println("No books in database !");
        }
        return bookList;
    }

    public List<Book> findByTitle(String title) {
        List<Book> bookList = em.createQuery("SELECT b FROM Book b WHERE b.title like :field1", Book.class)
                .setParameter("field1", "%" + title + "%").getResultList();
        if (bookList.isEmpty()) {
            System.out.println("No such books!");
        }
        return bookList;
    }

    public Book findByIsbn(String isbn) {
        try {
            return em.createQuery("SELECT b FROM Book b WHERE b.isbn = :field1", Book.class)
                    .setParameter("field1", isbn)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            System.out.println("No such book with ISBN: " + isbn);
            return null;
        }
    }

    public List<Book> listAvailableBooks() {
        List<Book> bookList = em.createQuery("SELECT b FROM Book b WHERE b.isAvailable = true", Book.class)
                .getResultList();
        if (bookList.isEmpty()) {
            System.out.println("No Available books !");
        }
        return bookList;
    }

    public boolean isBookAvailable(long id) {
        Book book = em.find(Book.class, id);
        if (book == null) {
            System.out.println("Wrong book id!");
            return false;
        }
        return book.isAvailable();
    }

    public long countTotalBooks() {
        long bookCount = em.createQuery("SELECT COUNT(b) FROM Book b ", Long.class).getSingleResult();
        if (bookCount == 0) {
            System.out.println("No books in database !");
        }
        return bookCount;
    }

}
