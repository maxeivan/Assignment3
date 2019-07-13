/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcgill.cccs425.assignment3;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * REST Web Service
 *
 * @author Maxim
 */
@Path("/books")
public class LibraryJson {

    //Declare variables context and list of books set in populate()
    @Context
    private ServletContext context;
    private static Repository blist;

    /**
     * Creates a new instance of LibraryJson
     */
    public LibraryJson() {
    }

    /**
     * There are two operations annotated with GET. The first GET operation
     * returns the entire list of books in json format
     *
     * @return
     * @throws java.lang.Exception
     *
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/allbooks")
    public List<Book> ListAllBooks() throws Exception {
        checkContext(context);
        return blist.list();

    }

    /**
     *
     * The second GET method returning a Book as per id
     *
     * @param id
     * @return
     */
    @GET
    @Path("/plain/{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book GetBookInfo(@PathParam("id") int id) {
        checkContext(context);
        Book book = blist.find(id);
        return book;
    }

    /**
     * POST method for adding a new book into the list
     *
     * @param b
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Result AddBook(Book b) {

        if (b.getTitle() == null || b.getDescription() == null
                || b.getIsbn() == null || b.getAuthor() == null || b.getPublisher() == null) {
            Result result = new Result("error", "One or many properties missing");
            return result;
        } else {
            Result result = new Result("ok", b.getId());
            checkContext(context);
            //calling method adding a new book into Repository
            blist.add(b);
            //calling method adding a new book into the file
            addBookInFile(b);
            return result;
        }
    }

    /**
     * The operation annotated with PUT implements update CRUD operation
     *
     * @param b
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public Result UpdateBookInfo(Book b) throws Exception {
        String msg = null;
        if (b.getTitle() == null && b.getDescription() == null
                && b.getIsbn() == null && b.getAuthor() == null && b.getPublisher() == null) {
            msg = "No new piece of information is given: nothing to edit";

        }
        Book book = blist.find(b.getId());

        if (book == null) {
            Result result = new Result("error", "There is no book with this ID");
            return result;

        } else {
            deleteBookInFile(book);
            addBookInFile(b);
            Result result = new Result("ok", b.getId());
//updating 
            if (b.getTitle() != null) {
                book.setTitle(b.getTitle());
            }
            if (b.getDescription() != null) {
                book.setDescription(b.getDescription());
            }
            if (b.getIsbn() != null) {
                book.setIsbn(b.getIsbn());
            }
            if (b.getAuthor() != null) {
                book.setAuthor(b.getAuthor());
            }
            if (b.getPublisher() != null) {
                book.setPublisher(b.getPublisher());
            }
            return result;
        }

    }

    /**
     * The operation annotated with DELETE implements delete CRUD operation
     *
     * @param id
     * @return
     */
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/delete/{id: \\d+}")
    public Result deleteBook(@PathParam("id") int id) throws Exception {
        Book book = blist.find(id);
        checkContext(context);

        if (book == null) {
            Result result = new Result("error", "There is no book with this ID");
            return result;
        } else {
            
            blist.delete(book);
            deleteBookInFile(book);
            Result result = new Result("deleted", book.getId());
            return result;
        }

    }

    private void checkContext(ServletContext context) {
        if (blist == null && context != null) {
            populate();
        }
    }

    //method creating instance of books list and populating the list with data from the file
    private void populate() {

        //creating new and only one instance of the Repository when RestService starts
        blist = new Repository();
        String filename = "C:\\Users\\Maxim\\Documents\\NetBeansProjects\\Assignment3\\src\\java\\com\\mcgill\\cccs425\\assignment3\\bookslist.json";

        try {
            //parsing file "list.json" into object
            Object obj = new JSONParser().parse(new FileReader(filename));
            //casting obj to JSONArray
            JSONArray ja = (JSONArray) obj;
            //looping through JSONArray to retrieve each JSONObject, create a new Book and add it to Repository instance
            for (int i = 0; i < ja.size(); i++) {
                Book b = new Book();
                JSONObject jo = (JSONObject) ja.get(i);
                Long lid = (Long) jo.get("id");
                int id = lid.intValue();
                b.setId(id);
                String title = (String) jo.get("title");
                b.setTitle(title);
                String description = (String) jo.get("description");
                b.setDescription(description);
                String isbn = (String) jo.get("isbn");
                b.setIsbn(isbn);
                String author = (String) jo.get("author");
                b.setAuthor(author);
                String publisher = (String) jo.get("publisher");
                b.setPublisher(publisher);
                blist.add(b);
            }
        } catch (IOException ex) {
            Logger.getLogger(LibraryJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(LibraryJson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //method writing a book into the file
    private void addBookInFile(Book b) {
        JSONParser jsonParser = new JSONParser();
        try {

            Object obj = jsonParser.parse(new FileReader("C:\\Users\\Maxim\\Documents\\NetBeansProjects\\Assignment3\\src\\java\\com\\mcgill\\cccs425\\assignment3\\bookslist.json"));
            JSONArray jsonArray = (JSONArray) obj;

            JSONObject jo = new JSONObject();

            jo.put("id", b.getId());
            jo.put("title", b.getTitle());
            jo.put("description", b.getDescription());
            jo.put("isbn", b.getIsbn());
            jo.put("author", b.getAuthor());
            jo.put("publisher", b.getPublisher());
            /*
            jo.put("title", jo.get("title"));
            jo.put("description", jo.get("description"));
            jo.put("isbn", jo.get("isbn"));
            jo.put("author", jo.get("author"));
            jo.put("publisher", jo.get("publisher"));
             */
            jsonArray.add(jo);
            FileWriter file = new FileWriter("C:\\Users\\Maxim\\Documents\\NetBeansProjects\\Assignment3\\src\\java\\com\\mcgill\\cccs425\\assignment3\\bookslist.json");
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();
            /*
            } catch (FileNotFoundException ex) {
            Logger.getLogger(LibraryJson.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LibraryJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(LibraryJson.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void deleteBookInFile(Book b) {
        JSONParser jsonParser = new JSONParser();
        try {

            Object obj = jsonParser.parse(new FileReader("C:\\Users\\Maxim\\Documents\\NetBeansProjects\\Assignment3\\src\\java\\com\\mcgill\\cccs425\\assignment3\\bookslist.json"));
            JSONArray jsonArray = (JSONArray) obj;
            for (int i = 0; i < jsonArray.size(); i++) {
                //Book book = new Book();
                JSONObject jo = (JSONObject) jsonArray.get(i);
                Long lid = (Long) jo.get("id");
                int id = lid.intValue();
                //book.setId(id);
                if (id == b.getId()) {
                    jsonArray.remove(i);
                    //break;
                }
            }
            /*
            JSONObject jo = new JSONObject();
            //jo.remove("id", b.getId());
            jo.put("id", b.getId());
            jo.put("title", b.getTitle());
            jo.put("description", b.getDescription());
            jo.put("isbn", b.getIsbn());
            jo.put("author", b.getAuthor());
            jo.put("publisher", b.getPublisher());
            jsonArray.remove(jo);*/
            FileWriter file = new FileWriter("C:\\Users\\Maxim\\Documents\\NetBeansProjects\\Assignment3\\src\\java\\com\\mcgill\\cccs425\\assignment3\\bookslist.json");
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LibraryJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(LibraryJson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
