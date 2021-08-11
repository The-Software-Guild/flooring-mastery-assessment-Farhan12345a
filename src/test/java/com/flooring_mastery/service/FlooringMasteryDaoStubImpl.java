/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flooring_mastery.service;

import com.flooring_mastery.dao.FlooringMasteryDAO;
import com.flooring_mastery.dao.FlooringMasteryPersistenceException;
import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author farhanshahbaz
 */
public class FlooringMasteryDaoStubImpl implements FlooringMasteryDAO{

    public Order onlyOrder;

    public FlooringMasteryDaoStubImpl() {
        onlyOrder = new Order("3", "John", "CA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 
        
    }

    public FlooringMasteryDaoStubImpl(Order onlyOrder) {
        this.onlyOrder = onlyOrder;
    }
    
    @Override
    public Order addOrder(String orderNumber, Order order, String fileName) throws FlooringMasteryPersistenceException {
        if (orderNumber.equals(onlyOrder.getOrderNumber())) {
            return onlyOrder;
        } else {
            return null;
        }
    }
    
    @Override
    public List<Order> getAllOrders(String fileName) throws FlooringMasteryPersistenceException {
        List<Order> orderList = new ArrayList<>();
        orderList.add(onlyOrder);
        return orderList;
    }
    
    @Override
    public Order getOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException {
        if (orderNumber.equals(onlyOrder.getOrderNumber())) {
            return onlyOrder;
        } else {
            return null;
        }       
    }
    
    @Override
    public Order removeOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException {
        if (orderNumber.equals(onlyOrder.getOrderNumber())) {
            return onlyOrder;
        } else {
            return null;
        }
    }   
    
    
    @Override
    public ArrayList<File> getAllFiles() throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    

    @Override
    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    @Override
    public Tax getTax(String stateAB) throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getAllStateAB() throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     @Override
    public List<String> getAllStates() throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<String> getAllProductTypes() throws FlooringMasteryPersistenceException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Product getProduct(String productType) throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order calculateOrderInfo(Order prevOrder, Tax specifcTax, Product specifcProduct) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order editOrder(Order editOrder, ArrayList<String> newValues, String fileName) throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String generateNextOrderNumber(String fileName) throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
