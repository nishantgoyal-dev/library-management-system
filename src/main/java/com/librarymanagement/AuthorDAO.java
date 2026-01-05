package com.librarymanagement;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;

public class AuthorDAO {
    EntityManager em;

    AuthorDAO(EntityManager em) {
        this.em = em;
    }

    public void addAuthor(String name, String about) {
        Author author = new Author();
        author.setName(name);
        author.setBiography(about);
        try {
            em.getTransaction().begin();
            em.persist(author);
            em.getTransaction().commit();
            System.out.println("Author added succesfully with author ID : " + author.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Database Error: " + e.getMessage());
        }

    }

    public Author findByID(long id) {
        Author author = em.find(Author.class, id);
        if (author == null) {
            System.out.println("Incorrect author ID!");
        }
        return author;
    }

    public void updateAuthor(long id, String newName, String newAbout) {
        Author author = em.find(Author.class, id);
        if (author == null) {
            System.out.println("Incorrect author ID!");
            return;
        }
        try {
            em.getTransaction().begin();
            author.setName(newName);
            author.setBiography(newAbout);
            em.merge(author);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Database Error: " + e.getMessage());
        }

    }

    public void deleteAuthor(long id) {
        Author author = em.find(Author.class, id);
        if (author == null) {
            System.out.println("Incorrect author ID!");
            return;
        }

        Long bookCount = em.createQuery("SELECT COUNT(b) FROM Book b WHERE b.author = :auth", Long.class)
                .setParameter("auth", author).getSingleResult();
        if (bookCount != 0) {

            System.out.println("Author already has books! can't perform delete operations!!!");
            return;
        }

        try {
            em.getTransaction().begin();
            em.remove(author);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public List<Author> listAllAuthors() {
        List<Author> authors = em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
        if (authors.isEmpty()) {
            System.out.println("No authors in database ! ");
        }
        return authors;
    }

    public List<Author> searchByName(String name) {
        List<Author> authors = em.createQuery("SELECT a FROM Author a WHERE a.name LIKE :name", Author.class)
                .setParameter("name", "%" + name + "%").getResultList();
        if (authors.isEmpty()) {
            System.out.println("No such authors ! ");
        }
        return authors;
    }

    public List<Book> getBooksByAuthor(long authorId) {
        Author author = em.find(Author.class, authorId);
        if (author == null) {
            System.out.println("Incorrect author ID!");
            return new ArrayList<Book>();
        }
        List<Book> authorBooks = em.createQuery("SELECT b from Book b WHERE b.author = :author", Book.class)
                .setParameter("author", author).getResultList();
        if (authorBooks.isEmpty()) {
            System.out.println("No books by this author !");
        }
        return authorBooks;
    }

    public long countBooksByAuthor(long authorId) {
        Author author = em.find(Author.class, authorId);
        if (author == null) {
            System.out.println("Incorrect author ID!");
            return 0;

        }
        return em.createQuery("SELECT COUNT(b) from Book b WHERE b.author = :author", Long.class)
                .setParameter("author", author).getSingleResult();
    }

}
