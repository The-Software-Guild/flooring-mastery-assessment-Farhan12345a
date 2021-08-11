/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flooring_mastery.controller;

import com.flooring_mastery.dao.FlooringMasteryPersistenceException;
import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import com.flooring_mastery.service.FlooringMasteryDataValidationException;
import com.flooring_mastery.service.FlooringMasteryServiceLayer;
import com.flooring_mastery.view.FlooringMasteryView;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author farhanshahbaz
 */
//@Component
public class FlooringMasteryController {
    private FlooringMasteryView view;
    private FlooringMasteryServiceLayer service;

    //@Autowired
    public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }
    
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        try{
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        listOrders();
                        //listTaxes();
                        //listProducts(); 
                        //listFiles();
                        break;
                    case 2:
                        createOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        //io.print("EXPORT");
                        //Option 5 is being changed to view for time being
                        //viewOrder();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            }
            exitMessage();
        } catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    
    private void createOrder() throws FlooringMasteryPersistenceException{
        view.displayCreateOrderBanner();
        boolean hasErrors = false;
        do{
            
            //Possibly make method out of list states??
            HashMap<String,Order> dateAndOrder = new HashMap<>();
            dateAndOrder = view.getNewOrderInfo(service.getAllStates(),service.getAllStateAB(),service.getAllProducts(), service.getAllProductTypes());
            
            //Obtaining Date and Order
            Map.Entry<String,Order> entry = dateAndOrder.entrySet().iterator().next();
            String date = entry.getKey();
            Order newOrder = entry.getValue();
            
          
            String fileName = date.toString().replace("/", "");
            fileName = "Orders_" + fileName + ".txt";
            //SET ORDER NUMBER BASED ON DATE!!!
            String orderNumber = service.generateNextOrderNumber(fileName);
            //SET OBTAINED ORDER NUMBER!!!
            newOrder.setOrderNumber(orderNumber);
            
          
            Tax specificTax = service.getTax(newOrder.getState());
           
            
            Product specificProduct = service.getProduct(newOrder.getProductType());

            //Another method to make calculations
            Order finishedOrder = service.calculateOrderInfo(newOrder,specificTax,specificProduct);

            view.displayOrder(finishedOrder);
            try{
                if(view.displayConfirmation("create")){
                    service.createOrder(finishedOrder, fileName);
                    view.displayCreateSuccessBanner();
                    hasErrors = false;
                }
            } catch (FlooringMasteryDataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
          
    }
    
    private void listOrders() throws FlooringMasteryPersistenceException{
        view.displayDisplayAllBanner();
        //Ask User for date
        String date = view.getDate();
        ArrayList<File> files = service.getAllFiles();
        boolean flag = true;
        do{
            if(!files.contains(date)){
                view.displayErrorMessage("Wrong date try again");
                flag = false;
            }
        }while(flag);
        List<Order> orderList = service.getAllOrders(date);
        view.displayOrderList(orderList);
    }

    private void removeOrder() throws FlooringMasteryPersistenceException{
        view.displayRemoveOrderBanner();
        String date = view.getDate();
        String orderID = view.getOrderIdChoice();
        
        Order order = service.getOrder(orderID, date);
        
        
        //If order exists, display information
        view.displayOrder(order);
        //Ask confirmation if they want to remove order
        if(view.displayConfirmation("remove")){
            Order removedOrder = service.removeOrder(orderID, date);
            view.displayRemoveResult(removedOrder);
        }
    }
    
    //CHANGES START HERE FOR EDITING****
    private void editOrder() throws FlooringMasteryPersistenceException{
        view.displayEditDVDBanner();
        String date = view.getDate();
        String orderID = view.getOrderIdChoice();
        //Get the order
        Order order = service.getOrder(orderID,date);
     
        //Confirmation the Order exists
        boolean result2 = view.displayEditResult(order);
        if(result2){
            ArrayList<String> newValues = view.editMenu(order.getCustomerName(), order.getState(),order.getProductType(),order.getArea());
            service.editOrder(order, newValues, date);
            view.displayOrder(order);
            if(view.displayConfirmation("edit")){
                
                
                view.displaySuccessEdit();
            }
    }
    }
 

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
    
        //    private List<Product> listProducts() throws FlooringMasteryPersistenceException{
//        List<Product> productList = dao.getAllProducts();
//        view.displayProductList(productList);
//        return productList;
//    }
    
    //NOT IN REQUIREMENT BUT ADD DATE/FILENAME IF YOU HAVE TIme!!!!!
//    private void viewOrder() throws FlooringMasteryPersistenceException{
//        view.displayDisplayOrderBanner();
//        String orderId = view.getOrderIdChoice();
//        Order order = service.getOrder(orderId);
//        view.displayOrder(order);
//    }
    
    //    private List<Tax> listTaxes() throws FlooringMasteryPersistenceException{
//        List<Tax> taxList = service.getAllTaxes();
//        view.displayTaxList(taxList);
//        return taxList;
//    }
//    
    
}
