/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flooring_mastery.view;

import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import com.flooring_mastery.userio.UserIO;
import com.sg.classroster.io.UserIOConsoleImpl;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author farhanshahbaz
 */
public class FlooringMasteryView {
    
    private UserIO io;
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }
    
    public int printMenuAndGetSelection() {
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("*");
        
        //Might have to get change with removed export
        return io.readInt("Please select from the above choices.", 1, 6);
    }
    
    //Get information from user to create Order object
    public Order getNewOrderInfo(String orderNumber, List<String> states, List<Product> products) {
        boolean valid = true;
        
        //Must be in the future
        String orderDate = io.readString("Please enter Order Date:");
        
        //May not be blank, allowed to contain [a-z][0-9] as well as periods and comma characters.
        //"Acme, Inc." is a valid name.
        //HAVENT HANDLES COMMAS IN STRINGS!!!
        String customerName = io.readString("Please enter the Customer Name:");
//        if(isEmptyString(customerName)){
//            displayUnknownCommandBanner();
//        }
        
        //Entered states must be checked against the tax file. If the state does not exist in the tax file we 
        //cannot sell there. If the tax file is modified to include the state, it should be allowed
        //without changing the application code.
        //CHECKING STATE ABBREVIATION (MIGHT HAVE TO CHANGE THiS
        String state = io.readString("Please enter State Name:");
        if(!states.contains(state)){
            //Have a loop to ask again...
            io.print("Cannot sell in the unknown state. Please try again");
        }
            
        
        //Show a list of available products and pricing information to choose from. Again, if a product is added to
        //the file it should show up in the application without a code change.
        displayProductList(products);
        String productType = io.readString("Please enter Product Type");
        
        //The area must be a positive decimal. Minimum order size is 100 sq ft.
        //LAMBDA***
        double area = io.readInt("Please enter the Area (Minimum is 100 sqft)");
        if(area < 0 || area > 100){
            io.print("Error Area if not in range. Please try again!");
             //Have a loop to ask again...
        }
        
        
        Order unfinishedOrder = new Order(orderNumber, customerName, state, productType, new BigDecimal(area));
        return unfinishedOrder;
    }
    
    public void displayCreateOrderBanner() {
        io.print("=== Create Order ===");
    }
    
    public boolean createConfirmation(){
        String confirm = io.readString("Do you want to place the order? (Y/N)");
        if(confirm.equals("y".toLowerCase())){
            return true;
        }
        return false;
    }
    
    public void displayCreateSuccessBanner() {
        io.readString("Order successfully created.  Please hit enter to continue");
    }
    
    public void displayStudentList(List<Order> orderList) {
        for (Order currentOrder : orderList) {
            String orderInfo = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                currentOrder.getOrderNumber(),
                currentOrder.getCustomerName(),
                currentOrder.getState(),
                currentOrder.getTaxRate().toString(),
                currentOrder.getProductType(),
                currentOrder.getArea().toString(),
                currentOrder.getCostPerSquareFoot().toString(),
                currentOrder.getLaborCostPerSquareFoot().toString(),
                currentOrder.getMaterialCost().toString(),
                currentOrder.getLaborCost().toString(),
                currentOrder.getTax().toString(),
                currentOrder.getTotal().toString());
                  
            io.print(orderInfo);
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayTaxList(List<Tax> orderList) {
        for (Tax currentTax : orderList) {
            String taxInfo = String.format("%s,%s,%s",
                currentTax.getStateAbbreviation(),
                currentTax.getStateName(),
                currentTax.getTaxRate().toString());
            io.print(taxInfo);
        }
        io.readString("Please hit enter to continue.");
    }
    
    
    
    public void displayProductList(List<Product> orderList) {
        for (Product currentProduct : orderList) {
            String productInfo = String.format("%s,%s,%s",
                currentProduct.getProductType(),
                currentProduct.getCostPerSquareFoot().toString(),
                currentProduct.getLaborCostPerSquareFoot().toString());
            io.print(productInfo);
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayFileList(ArrayList<File> fileList) {
        for (File currentFile : fileList) {
            io.print(currentFile.getName());
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayDisplayAllBanner() {
        io.print("=== Display All Orders ===");
    }
    
    public void displayDisplayOrderBanner() {
        io.print("=== Display Order ===");
    }

    public String getOrderIdChoice() {
        return io.readString("Please enter the Order ID:");
    }
    
    public String getOrderDate(){
        return io.readString("Please enter the Order Date:");
    }

    public void displayOrder(Order order) {
        if (order != null) {
            io.print("Order Number: " + order.getOrderNumber());
            io.print("Customer Name: " + order.getCustomerName());
            io.print("State: " + order.getState());
            io.print("Tax Rate: " + order.getTaxRate().toString());
            io.print("Product Type: " + order.getProductType());
            io.print("Area: " + order.getArea().toString());
            io.print("Cost Per Square Foot: " + order.getCostPerSquareFoot().toString());
            io.print("Labor Cost Per Square Foot: " + order.getLaborCostPerSquareFoot().toString());
            io.print("Material Cost: " + order.getMaterialCost().toString());
            io.print("Labor Cost: " + order.getLaborCost().toString());
            io.print("Tax: " + order.getTax().toString());
            io.print("Total: " + order.getTotal().toString());
            io.print("");
        } else {
            io.print("No such order.");
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayRemoveOrderBanner() {
        io.print("=== Remove Order ===");
    }

    public void displayRemoveResult(Order order) {
        if(order != null){
          io.print("Order successfully removed.");
        }else{
          io.print("No such order.");
        }
        io.readString("Please hit enter to continue.");
    }
    
    public boolean displayRemoveConfirmation() {
        String confirm = io.readString("Are you sure you want to remove the order? (y/n)");
        if(confirm.equals("y")){
            return true;
        }
        //return false for any other output
        return false;
    }
    
    public void displayEditDVDBanner(){
        io.print("=== Edit Order ===");
    }
    
    //Checks to see if DVD is contained in collection
    public boolean displayEditResult(Order order) {
        if(order != null){
          io.print("Order #" + order.getOrderNumber() + " Selected");
          return true;
        }
          io.print("No such DVD.");
          io.readString("Please hit enter to continue.");
          return false;
    }
    
    public ArrayList<String> editMenu(String prevCustomerName, String prevState, String prevProductType, BigDecimal prevArea){
        //Have condition (if statement) if user enters ENTER**
        String newCustomerName = io.readString("Enter customer name (" + prevCustomerName + "):");
        String newstateName = io.readString("Enter state name (" + prevState + "):");
        String newProductType = io.readString("Enter product type (" + prevProductType + "):");
        String newArea = io.readString("Enter area (" + prevArea + "):");
        ArrayList<String> newValues = new ArrayList<String>();
        newValues.add(newCustomerName);
        newValues.add(newstateName);
        newValues.add(newProductType);
        newValues.add(newArea);
        
        return newValues;
    }
    
    public boolean isEmptyString(String string) {
        return string == null || string.isEmpty();
    }


    public void displaySuccessEdit(){
        io.print("Edit Successfully Completed!");
    }
    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!! Try Again");
    }
    
    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }
    


}
