/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flooring_mastery.dao;

import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author farhanshahbaz
 */
public interface FlooringMasteryDAO {
    
    ArrayList<File> getAllFiles() throws FlooringMasteryPersistenceException;
    
    Order addOrder(String orderNumber, Order order, String fileName) throws FlooringMasteryPersistenceException;

   
    List<Order> getAllOrders(String fileName) throws FlooringMasteryPersistenceException;

   
    List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;
    
    List<Product> getAllProducts() throws FlooringMasteryPersistenceException;
    
    
    Order getOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException;
    
    Tax getTax(String stateAB) throws FlooringMasteryPersistenceException;
    
    List<String> getAllStateAB() throws FlooringMasteryPersistenceException;
    
    List<String> getAllStates() throws FlooringMasteryPersistenceException;
    
    Product getProduct(String productType) throws FlooringMasteryPersistenceException;
    
    List<String> getAllProductTypes() throws FlooringMasteryPersistenceException;
    
    Order removeOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException;
    
    Order calculateOrderInfo(Order prevOrder, Tax specifcTax, Product specifcProduct);
    
    /**
     * Edits the given Order to the collection and associates it with the given
     * order number. 
     *
     * @param editOrder Order that is going to be edited
     * @param choice which feature of DVD that's going to change
     * @param replace the new value
     * @return the Order object previously associated with the given  
     * order number if it exists, null otherwise
     */
    public Order editOrder(Order editOrder, ArrayList<String> newValues, String fileName) throws FlooringMasteryPersistenceException;
    
    public String generateNextOrderNumber(String fileName) throws FlooringMasteryPersistenceException;
    
}
