/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcgill.cccs425.assignment3;

/**
 * REST Web Service
 *
 * @author Maxim
 */
public class Book implements Cloneable{

    private int id;
    private String title;
    private String description;
    private String isbn;
    private String author;
    private String publisher;

    /**
     * Creates a new instance of Book
     */
    public Book() {
    }
/*
    private Book(int id) {
        this.id = id;
    }
*/
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }
    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    /**
     * @return the isbn
     */
    public String getIsbn() {
        return this.isbn;
    }
    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    /**
     * @return the author
     */
    public String getAuthor() {
        return this.author;
    }
    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    /**
     * @return the publisher
     */
    public String getPublisher() {
        return this.publisher;
    }
    
    public Book clone() {
        Book b = new Book();
        b.setId(this.id);
        b.setTitle(this.title);
        b.setDescription(this.description);
        b.setIsbn(this.isbn);
        b.setAuthor(this.author);
        b.setPublisher(this.publisher);
        return b;
    }
}
