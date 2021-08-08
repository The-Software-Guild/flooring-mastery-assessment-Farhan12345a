/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flooring_mastery.service;

import com.flooring_mastery.dao.FlooringMasteryAuditDao;
import com.flooring_mastery.dao.FlooringMasteryDAO;
import com.flooring_mastery.dao.FlooringMasteryPersistenceException;
import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author farhanshahbaz
 */
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer{

    private FlooringMasteryDAO dao;
    private FlooringMasteryAuditDao auditDao;
   
    public FlooringMasteryServiceLayerImpl(FlooringMasteryDAO dao, FlooringMasteryAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }

    @Override
    public void createOrder(Order order) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException {
        validateOrderData(order);
        dao.addOrder(order.getCustomerName(), order);
        auditDao.writeAuditEntry("Student " + order.getOrderNumber() + " CREATED.");
    }

    @Override
    public List<Order> getAllOrders() throws FlooringMasteryPersistenceException {
        return dao.getAllOrders();
    }

    @Override
    public Order getOrder(String orderNumber) throws FlooringMasteryPersistenceException {
        return dao.getOrder(orderNumber);
    }
    
    public  Tax getTax(String stateAB) throws FlooringMasteryPersistenceException{
        return dao.getTax(stateAB);
    }
    
    public  Product getProduct(String productType) throws FlooringMasteryPersistenceException{
        return dao.getProduct(productType);
    }

    @Override
    public Order removeOrder(String orderNumber) throws FlooringMasteryPersistenceException {
        Order order = dao.removeOrder(orderNumber);
        auditDao.writeAuditEntry("Student " + orderNumber + " REMOVED.");
        return order;
    }
    
    public Order editOrder(Order editOrder, ArrayList<String> newValues) throws FlooringMasteryPersistenceException{
        return dao.editOrder(editOrder, newValues);
    }
    
    @Override
    public ArrayList<File> getAllFiles() throws FlooringMasteryPersistenceException{
        return dao.getAllFiles();
    }
    
    @Override
    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException{
        return dao.getAllTaxes();
    }
    
    @Override
    public List<String> getAllStates() throws FlooringMasteryPersistenceException{
        return dao.getAllStates();
    }
    
    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException{
        return dao.getAllProducts();
    }

    
    @Override
    public Order calculateOrderInfo(Order prevOrder, Tax specifcTax, Product specifcProduct) throws FlooringMasteryPersistenceException{
        return dao.calculateOrderInfo(prevOrder, specifcTax, specifcProduct);
    }
    
    @Override
    public String generateNextOrderNumber(){
        return dao.generateNextOrderNumber();
    }
    
    
    //VALIDATE DATA HERE
    private void validateOrderData(Order order) throws FlooringMasteryDataValidationException {
        
        //Checking if any of the feilds are empyt
        if (order.getCustomerName() == null || order.getCustomerName().trim().length() == 0
                || order.getState()== null
                || order.getState().trim().length() == 0
                || order.getProductType() == null
                || order.getProductType().trim().length() == 0
                || order.getArea() == null
                || order.getArea().toString().trim().length() == 0)
                 {

            throw new FlooringMasteryDataValidationException(
                    "ERROR: All fields [Name, State, Product Type, Area] are required.");
        }
        
//        if(order.getArea() < 0 || order.getArea() > 100){
//            throw new FlooringMasteryDataValidationException(
//                    "ERROR: All fields [Name, State, Product Type, Area] are required.");
//            
//        }
//        
        
        
        
    }
    
}
