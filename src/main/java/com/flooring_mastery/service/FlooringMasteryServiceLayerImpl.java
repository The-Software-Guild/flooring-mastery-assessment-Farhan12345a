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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author farhanshahbaz
 */

//@Component
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer{

    private FlooringMasteryDAO dao;
    private FlooringMasteryAuditDao auditDao;
   
    //@Autowired
    public FlooringMasteryServiceLayerImpl(FlooringMasteryDAO dao, FlooringMasteryAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }

    @Override
    public void createOrder(Order order, String fileName) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException {
        //validateOrderData(order);
        dao.addOrder(order.getOrderNumber(), order, fileName);
        auditDao.writeAuditEntry("Student " + order.getOrderNumber() + " CREATED.");
    }

    @Override
    public List<Order> getAllOrders(String fileName) throws FlooringMasteryPersistenceException {
        return dao.getAllOrders(fileName);
    }

    @Override
    public Order getOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException {
        return dao.getOrder(orderNumber, fileName);
    }
    
    public  Tax getTax(String stateAB) throws FlooringMasteryPersistenceException{
        return dao.getTax(stateAB);
    }
    
    public  Product getProduct(String productType) throws FlooringMasteryPersistenceException{
        return dao.getProduct(productType);
    }

    @Override
    public Order removeOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException {
        Order order = dao.removeOrder(orderNumber, fileName);
        auditDao.writeAuditEntry("Student " + orderNumber + " REMOVED.");
        return order;
    }
    
    public Order editOrder(Order editOrder, ArrayList<String> newValues, String fileName) throws FlooringMasteryPersistenceException{
        return dao.editOrder(editOrder, newValues,fileName);
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
    public List<String> getAllStateAB() throws FlooringMasteryPersistenceException{
        return dao.getAllStateAB();
    }
    
    public List<String> getAllStates() throws FlooringMasteryPersistenceException{
        return dao.getAllStates();
    }
    
    public List<String> getAllProductTypes() throws FlooringMasteryPersistenceException{
        return dao.getAllProductTypes();
    }
    
    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException{
        return dao.getAllProducts();
    }

    
    @Override
    public Order calculateOrderInfo(Order prevOrder, Tax specifcTax, Product specifcProduct) throws FlooringMasteryPersistenceException{
        //validateCalculateData(prevOrder,specificTax,specificProduct); 
        return dao.calculateOrderInfo(prevOrder, specifcTax, specifcProduct);
    }
    
    private void validateCalculateData(Order prevOrder,Tax specificTax,Product specificProduct) throws FlooringMasteryPersistenceException, FlooringMasteryDataValidationException{
        List<Product> products = getAllProducts();
        List<Tax> taxInfo = getAllTaxes();
        String message = "";
//        if(!products.contains(prevOrder.getProductType())) {
//            message += "Invalid Product Type. TRY AGAIN.\n";
//        }
//        if (!taxInfo.contains(prevOrder.getTaxRate())){
//                message += "State is required.\n";
//        }
        
        if (!message.isEmpty()) {
                throw new FlooringMasteryDataValidationException(message);
        }
    }
    
    @Override
    public String generateNextOrderNumber(String fileName) throws FlooringMasteryPersistenceException{
        return dao.generateNextOrderNumber(fileName);
    }
    
    
    //VALIDATE DATA HERE
    private void validateOrderData(Order order) throws FlooringMasteryDataValidationException, FlooringMasteryPersistenceException {
        List<String> states = getAllStateAB();
        
        String message = "";
        if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty()){
            message += "Customer name is required. Try Again!\n";
            
        }
        if (order.getState().trim().isEmpty() || order.getState() == null) {
                message += "State is required.\n";
        }
        if(states.contains(order.getState())) {
          //System.out.println("WORK");
        }else{
            System.out.println("HELLOOO");
            message += "Invalid Product Type. TRY AGAIN.\n";
            throw new FlooringMasteryDataValidationException(message);
        }
        
        if (order.getProductType().trim().isEmpty() || order.getProductType() == null) {
            message += "Product type is required.\n";
        }

        
        if (order.getArea().compareTo(BigDecimal.ZERO) == 0 || order.getArea() == null) {
            message += "Area square footage is required.";
        }else if(order.getArea().compareTo(BigDecimal.ZERO) < 0 || order.getArea().compareTo(new BigDecimal("100")) > 0) {
            message += "Area Invalid not in range. TRY AGAIN";
        }
        
        if (!message.isEmpty()) {
                throw new FlooringMasteryDataValidationException(message);
        }
}
    
//     private void validateDate(String fileName) throws FlooringMasteryDataValidationException{
//        String fileName = "";
//        if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty()){
//            message += "Customer name is required. Try Again!\n";
//        }
//        if (!message.isEmpty()) {
//                throw new FlooringMasteryDataValidationException(message);
//        }
//     }
        
           
    
    
}
