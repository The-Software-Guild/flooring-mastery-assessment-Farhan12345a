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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author farhanshahbaz
 */
public class FlooringMasteryServiceLayerImplTest {
    
    private FlooringMasteryServiceLayer service;
    
    public FlooringMasteryServiceLayerImplTest(){
        FlooringMasteryDAO dao = new FlooringMasteryDaoStubImpl();
        //Added a constructor ofr auditDAOStubImpl
        FlooringMasteryAuditDao auditDao = new FlooringMasteryAuditDaoStubImpl();

        service = new FlooringMasteryServiceLayerImpl(dao, auditDao);
    
    }
    
    @Test
    public void testCreateOrderInvalidData() throws Exception {
        // ARRANGE
        Order order = new Order("3", "John", "CA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 
       
        String fileName = "Orders_06022013.txt";   
        
        try {
            service.createOrder(order,fileName);
            //("Expected ValidationException was not thrown.");
        } catch (FlooringMasteryPersistenceException e) {
            
        // ASSERT
            //fail("Incorrect exception was thrown.");
        } catch (FlooringMasteryDataValidationException e){
            return;
        }  
    }
    
    //Testing Add/Get All
     @Test
     public void testAddGetAllOrders() throws Exception {
         // Creating order
        Order order = new Order("3", "John", "CA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 
        
        String fileName = "Orders_06022013.txt";   

        //testDao.addOrder(order.getOrderNumber(), order, fileName);
        
        service.createOrder(order, fileName);
         
        List<Order> listOrders = service.getAllOrders(fileName);
         
        assertTrue(service.getAllOrders(fileName).contains(order),
                "The list of students should include first order.");
        assertTrue(service.getAllOrders(fileName).contains(order), "List should include second order"); 
     }
     
     @Test
    public void testGetOrder() throws Exception {
        // ARRANGE
        Order testClone = new Order("3", "John", "CA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 
        
        String fileName = "Orders_06022013.txt";   

        // ACT & ASSERT
        Order shouldBeAda = service.getOrder(testClone.getOrderNumber(), fileName);
        
        assertNotNull(shouldBeAda, "Getting 0001 should be not null.");
        assertEquals( testClone, shouldBeAda,
                                       "Student stored under 0001 should be Ada.");

        Order shouldBeNull = service.getOrder("34", fileName);    
        assertNull( shouldBeNull, "Getting 34 should be null.");

    }
    
    @Test
    public void testRemoveOrder() throws Exception {
        // ARRANGE
        Order testClone = new Order("3", "John", "CA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 
        
        String fileName = "Orders_06022013.txt";   

        // ACT & ASSERT
        Order shouldBeAda = service.removeOrder(testClone.getOrderNumber(), fileName);
        assertNotNull( shouldBeAda, "Removing 3 should be not null.");
        assertEquals( testClone, shouldBeAda, "Student removed from 3 should be Ada.");

        Order shouldBeNull = service.removeOrder("99", fileName);  
        assertNull( shouldBeNull, "Removing 99 should be null.");

    }
    
    }
    
    
    












//    @Test
//    public void testEditOrder() throws Exception {
//        // ARRANGE
//        Order testClone = new Order("3", "John", "CA", new BigDecimal("3.50"),
//            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
//            new BigDecimal("4.99"), new BigDecimal("12.50"), 
//            new BigDecimal("13.90"), new BigDecimal("8.50"),
//            new BigDecimal("13.00")); 
//        
//        String fileName = "Orders_06022013.txt";   
//
//         //Creating new values
//        ArrayList<String> newValues = new ArrayList<String>();
//        newValues.add("Bob");
//        newValues.add("KY");
//        newValues.add("Carpet");
//        newValues.add("99.00");
//
//        service.editOrder(testClone, newValues, fileName);
//        
//        assertEquals(testClone.getCustomerName(),"Bob","Customer name should be Bob");
//        assertEquals(testClone.getState(),"KY","State name should be KY");
//        assertEquals(testClone.getProductType(),"Carpet","Product type should be Carpet");
//        assertEquals(testClone.getArea(),new BigDecimal("99.00"),"Area should be 99");
//
//
//
//    }
     
     
    
    

