/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flooring_mastery.controller;

import com.flooring_mastery.dao.FlooringMasteryDAO;
import com.flooring_mastery.dao.FlooringMasteryPersistenceException;
import com.flooring_mastery.dao.FlooringMasteryDAOFileImpl;
import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import com.flooring_mastery.service.FlooringMasteryDataValidationException;
import com.flooring_mastery.service.FlooringMasteryServiceLayer;
import com.flooring_mastery.userio.UserIO;
import com.flooring_mastery.view.FlooringMasteryView;
import com.sg.classroster.io.UserIOConsoleImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author farhanshahbaz
 */
public class FlooringMasteryController {
    private FlooringMasteryView view;
    private FlooringMasteryServiceLayer service;

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
                        viewOrder();
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
            String orderNumber = service.generateNextOrderNumber();
            //Possibly make method out of list states??
            Order newOrder = view.getNewOrderInfo(orderNumber, service.getAllStates(),service.getAllProducts());

            //Get Specifc Tax/Product Information Based on Keys
            Tax specificTax = service.getTax(newOrder.getState());
            Product specificProduct = service.getProduct(newOrder.getProductType());

            //Another method to make calculations
            Order finishedOrder = service.calculateOrderInfo(newOrder,specificTax,specificProduct);

            //Find where to add other Order fields (by calculations)*****
            //CHANGES****
            try{
                service.createOrder(finishedOrder);
                view.displayCreateSuccessBanner();
                hasErrors = false;
            } catch (FlooringMasteryDataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
          
    }
    
    private void listOrders() throws FlooringMasteryPersistenceException{
        view.displayDisplayAllBanner();
        List<Order> orderList = service.getAllOrders();
        view.displayStudentList(orderList);
    }
    
    private ArrayList<File> listFiles() throws FlooringMasteryPersistenceException{
        ArrayList<File> fileList = service.getAllFiles();
        view.displayFileList(fileList);
        return fileList;
    }
    
    
    private List<Tax> listTaxes() throws FlooringMasteryPersistenceException{
        List<Tax> taxList = service.getAllTaxes();
        view.displayTaxList(taxList);
        return taxList;
    }
    
//    private List<Product> listProducts() throws FlooringMasteryPersistenceException{
//        List<Product> productList = dao.getAllProducts();
//        view.displayProductList(productList);
//        return productList;
//    }
    
    private void viewOrder() throws FlooringMasteryPersistenceException{
        view.displayDisplayOrderBanner();
        String orderId = view.getOrderIdChoice();
        Order order = service.getOrder(orderId);
        view.displayOrder(order);
    }
    
    private void removeOrder() throws FlooringMasteryPersistenceException{
        view.displayRemoveOrderBanner();
        String orderID = view.getOrderIdChoice();
        Order order = service.getOrder(orderID);
        //LocalDate Type?? (Implement Later)
        String date = view.getOrderDate();
        
        //If order exists, display information
        view.displayOrder(order);
        //Ask confirmation if they want to remove order
        if(view.displayRemoveConfirmation()){
            Order removedOrder = service.removeOrder(orderID);
            view.displayRemoveResult(removedOrder);
        }
    }
    
    //CHANGES START HERE FOR EDITING****
    private void editOrder() throws FlooringMasteryPersistenceException{
        view.displayEditDVDBanner();
        //Ask for Order ID
        String orderID = view.getOrderIdChoice();
        //Get the order
        Order order = service.getOrder(orderID);
        //LocalDate Type?? (Implement Later)
        String date = view.getOrderDate();
       
        //Confirmation the Order exists
        boolean result2 = view.displayEditResult(order);
        if(result2){
            ArrayList<String> newValues = view.editMenu(order.getCustomerName(), order.getState(),order.getProductType(),order.getArea());
            //int choice = view.editHelper();
            //String newVal = view.getEditValue(choice);
            
            //Display summary and ask if the user wants to save edits
            //Another flag/boolean here
            //Parameters might have to be tweaked
            service.editOrder(order, newValues);
            view.displaySuccessEdit();
        }
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
    
}
