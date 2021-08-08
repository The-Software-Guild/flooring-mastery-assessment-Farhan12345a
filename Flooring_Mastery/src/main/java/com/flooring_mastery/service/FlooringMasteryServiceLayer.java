/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flooring_mastery.service;

import com.flooring_mastery.dao.FlooringMasteryPersistenceException;
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
public interface FlooringMasteryServiceLayer {
    
    void createOrder(Order order) throws
            FlooringMasteryDataValidationException,
            FlooringMasteryPersistenceException;
 
    List<Order> getAllOrders() throws
            FlooringMasteryPersistenceException;
 
    Order getOrder(String orderNumber) throws
            FlooringMasteryPersistenceException;
 
    Order removeOrder(String orderNumber) throws
            FlooringMasteryPersistenceException;
    
    ArrayList<File> getAllFiles() throws FlooringMasteryPersistenceException;
    
    List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;
    
    List<String> getAllStates() throws FlooringMasteryPersistenceException;
    
    Order editOrder(Order editOrder, ArrayList<String> newValues) throws FlooringMasteryPersistenceException;
    
    Tax getTax(String stateAB) throws FlooringMasteryPersistenceException;
    
    Product getProduct(String productType) throws FlooringMasteryPersistenceException;

    List<Product> getAllProducts() throws FlooringMasteryPersistenceException;

    Order calculateOrderInfo(Order prevOrder, Tax specifcTax, Product specifcProduct) throws FlooringMasteryPersistenceException;
     
    String generateNextOrderNumber();
}
