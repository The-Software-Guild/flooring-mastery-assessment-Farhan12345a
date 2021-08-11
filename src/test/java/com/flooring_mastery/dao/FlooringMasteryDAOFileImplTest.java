/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flooring_mastery.dao;

import java.io.FileWriter;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.flooring_mastery.model.Order;
import java.util.ArrayList;
import java.util.List;

public class FlooringMasteryDAOFileImplTest {
    
    FlooringMasteryDAO testDao;
    
    public FlooringMasteryDAOFileImplTest() {
        
    }
    

    @BeforeEach
    public void setUp() throws Exception{
        String testFile = "testroster.txt";
        // Use the FileWriter to quickly blank the file
        new FileWriter(testFile);
        testDao = new FlooringMasteryDAOFileImpl(testFile);
    }
    
    String fileName = "Orders_09102099.txt";
    
    
    //Testing Get/Add
    @Test
    public void testAddGetOrders() throws Exception {
        // Creating order
        Order order = new Order("3", "John", "CA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 

         testDao.addOrder(order.getOrderNumber(), order, fileName);
         
         //Get Order
         Order retrivedOrder = testDao.getOrder(order.getOrderNumber(), fileName);
         
         //Checking all values
         assertEquals(order.getCustomerName(), retrivedOrder.getCustomerName(),"Checking Customer Name");
         assertEquals(order.getState(), retrivedOrder.getState(),"Checking State Name");
         assertEquals(order.getTaxRate(), retrivedOrder.getTaxRate(),"Checking Tax Rate Name");
         assertEquals(order.getProductType(), retrivedOrder.getProductType(),"Checking Customer Name");
         assertEquals(order.getArea(), retrivedOrder.getArea(),"Checking Customer Name");
         assertEquals(order.getCostPerSquareFoot(), retrivedOrder.getCostPerSquareFoot(),"Checking Customer Name");
         assertEquals(order.getLaborCostPerSquareFoot(), retrivedOrder.getLaborCostPerSquareFoot(),"Checking Customer Name");
         assertEquals(order.getMaterialCost(), retrivedOrder.getMaterialCost(),"Checking Customer Name");
         assertEquals(order.getLaborCost(), retrivedOrder.getLaborCost(),"Checking Customer Name");
         assertEquals(order.getTax(), retrivedOrder.getTax(),"Checking Customer Name");
         assertEquals(order.getTotal(), retrivedOrder.getTotal(),"Checking Customer Name");
       
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

         testDao.addOrder(order.getOrderNumber(), order, fileName);
         
         // Creating order
        Order order2 = new Order("32", "John C", "WA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 

         
         testDao.addOrder(order2.getOrderNumber(), order2, fileName);
         
         List<Order> listOrders = testDao.getAllOrders(fileName);
         
         assertTrue(testDao.getAllOrders(fileName).contains(order),
                "The list of students should include first order.");
        assertTrue(testDao.getAllOrders(fileName).contains(order2), "List should include second order"); 
     }
     
    //Testing Add/Remove Order
     @Test
     public void testAddRemoveOrders() throws Exception {
         // Creating order
        Order order = new Order("3", "John", "CA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 

        testDao.addOrder(order.getOrderNumber(), order, fileName);
         
         // Creating 2nd order
        Order order2 = new Order("32", "John C", "WA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 
        
        testDao.addOrder(order2.getOrderNumber(), order2, fileName);
        
        //Remove first order
        Order removedOrder = testDao.removeOrder(order.getOrderNumber(),fileName);

        // Check that the correct object was removed.
        assertEquals(removedOrder, order, "The removed order should be 3.");
         
        List<Order> listOrders = testDao.getAllOrders(fileName);
        
        assertNotNull( listOrders, "All students list should be not null.");
        assertEquals( 1, listOrders.size(), "All orders should only have 1 order.");
        
        assertFalse( listOrders.contains(order), "All orders should NOT include 3.");
        assertTrue( listOrders.contains(order2), "All students should NOT include 32."); 
        
        // Remove the second student
        removedOrder = testDao.removeOrder(order2.getOrderNumber(),fileName);
        // Check that the correct object was removed.
        assertEquals( removedOrder, order2, "The removed order should be 32.");
        
        listOrders = testDao.getAllOrders(fileName);

        // Check the contents of the list - it should be empty
        assertTrue( listOrders.isEmpty(), "The retrieved list of orders should be empty.");
        
        Order retrievedOrder = testDao.getOrder(order.getOrderNumber(),fileName);
        assertNull(retrievedOrder, "3 was removed, should be null.");
        
        retrievedOrder = testDao.getOrder(order2.getOrderNumber(), fileName);
        assertNull(retrievedOrder, "32 was removed, should be null.");
     }
     
     //Testing Edit Order
     @Test
     public void testEditOrders() throws Exception {
         // Creating order
        Order order = new Order("3", "John", "CA", new BigDecimal("3.50"),
            "Wood", new BigDecimal("12"), new BigDecimal("5.00"), 
            new BigDecimal("4.99"), new BigDecimal("12.50"), 
            new BigDecimal("13.90"), new BigDecimal("8.50"),
            new BigDecimal("13.00")); 

        testDao.addOrder(order.getOrderNumber(), order, fileName);
        
        //Creating new values
        ArrayList<String> newValues = new ArrayList<String>();
        newValues.add("Bob");
        newValues.add("KY");
        newValues.add("Carpet");
        newValues.add("99.00");

        testDao.editOrder(order, newValues, fileName);
        
        assertEquals(order.getCustomerName(),"Bob","Customer name should be Bob");
        assertEquals(order.getState(),"KY","State name should be KY");
        assertEquals(order.getProductType(),"Carpet","Product type should be Carpet");
        assertEquals(order.getArea(),new BigDecimal("99.00"),"Area should be 99");

        
     }
     
     
   
    
}
