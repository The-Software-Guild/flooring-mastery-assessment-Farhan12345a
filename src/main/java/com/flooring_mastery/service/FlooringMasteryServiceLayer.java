
package com.flooring_mastery.service;

import com.flooring_mastery.dao.FlooringMasteryPersistenceException;
import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public interface FlooringMasteryServiceLayer {
    
    void createOrder(Order order, String fileName) throws
            FlooringMasteryDataValidationException,
            FlooringMasteryPersistenceException;
 
    List<Order> getAllOrders(String fileName) throws
            FlooringMasteryPersistenceException;
 
    Order getOrder(String orderNumber, String fileName) throws
            FlooringMasteryPersistenceException;
 
    Order removeOrder(String orderNumber, String fileName) throws
            FlooringMasteryPersistenceException;
    
    ArrayList<File> getAllFiles() throws FlooringMasteryPersistenceException;
    
    List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;
    
    List<String> getAllStateAB() throws FlooringMasteryPersistenceException;
    
    Order editOrder(Order editOrder, ArrayList<String> newValues, String fileName) throws FlooringMasteryPersistenceException;
    
    List<String> getAllStates() throws FlooringMasteryPersistenceException;
    
    List<String> getAllProductTypes() throws FlooringMasteryPersistenceException;
            
    Tax getTax(String stateAB) throws FlooringMasteryPersistenceException;
    
    Product getProduct(String productType) throws FlooringMasteryPersistenceException;

    List<Product> getAllProducts() throws FlooringMasteryPersistenceException;

    Order calculateOrderInfo(Order prevOrder, Tax specifcTax, Product specifcProduct) throws FlooringMasteryPersistenceException;
     
    String generateNextOrderNumber(String fileName) throws FlooringMasteryPersistenceException;
}
