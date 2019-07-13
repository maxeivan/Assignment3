/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcgill.cccs425.assignment3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Maxim
 */
public class Repository {

    private List<Book> books;

    /**
     *
     * constructor
     */
    public Repository() {
        books = new CopyOnWriteArrayList<Book>();
    }

    /**
     * method adding a new Book into Repository
     *
     * @param b
     */
    public void add(Book b) {
        books.add(b.clone());
    }

    /**
     * @return the books
     */
    
    public List<Book> getBooks() {
        return books;
    }
     
    /**
     * @param books the books to set
     */
    
    public void setBooks(List<Book> books) {
        this.books = books;
    }
     
    /**
     * method transforming Repository object into Array of Books
     *
     * @return result
     */
    public List<Book> list() throws Exception {
        ArrayList<Book> result = new ArrayList(books);
        for (int i = 0; i < result.size(); i++) {
            result.set(i, result.get(i).clone());
        }
        result.sort((b1, b2) -> ("" + b1.getId()).compareTo("" + b2.getId()));
        return result;
    }

    /**
     * method finding a Book in Repository
     *
     * @param id
     * @return b
     */
    public Book find(int id) {
        Book book = null;

        for (Book b : books) {
            if (b.getId() == id) {
                book = b;
                break;
            }
        }
        return book;
    }

    public void update(Book b) throws Exception {
        for (Book book : books) {
            if (book.getId() == b.clone().getId()) {
                books.remove(book);
                books.add(b.clone());
                break;
            }
        }
        throw new Exception("key not found");
    }

    /**
     *
     * method deleting a Book from Repository
     *
     * @param b
     * @throws java.lang.Exception
     */

    public void delete(Book b) throws Exception {
        books.remove(b);
        /*
        for (Book book: books){
            if (book.getId()==b.getId()){
                books.remove(book);
                break;
            }
        }
         throw new Exception("key not found");*/
    }
}
