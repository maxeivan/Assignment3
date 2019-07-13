/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcgill.cccs425.assignment3;

/**
 *
 * @author Maxim
 */
public class Result {

    public int id;
    public String result;
    public String details;

    /**
     * Creates a new instance of Result
     */
    
    public Result() {
    }

    public Result(String result, int id) {
        this.result = result;
        this.id = id;
    }

    public Result(String result, String details) {
        this.result = result;
        this.details = details;
    }
}
