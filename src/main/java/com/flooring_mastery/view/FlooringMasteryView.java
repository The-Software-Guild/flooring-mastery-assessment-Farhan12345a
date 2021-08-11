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

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class FlooringMasteryView {
    
    private UserIO io;
    
    //@Autowired
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
  
        return io.readInt("Please select from the above choices.", 1, 6);
    }
    
    //Get information from user to create Order object
    public HashMap<String,Order> getNewOrderInfo(List<String> states, List<String> stateAB, List<Product> products, List<String> productTypes) {
        HashMap<String,Order> answer= new HashMap<String,Order>();
        //Must be in the future
        String orderDate = getDateCreate();
        String customerName = "";
        boolean flag = false;
        do{
        //HAVENT HANDLES COMMAS IN STRINGS!!!
        customerName = io.readString("Please enter the Customer Name:");
        if(customerName == null || customerName.trim().isEmpty()){
            io.print("Name Can't be Blank, Try Again");
            flag = true;
        }else{
            flag = false;
        }
        
        }while(flag);
        
        String state;
        do{
            state = io.readString("Please enter State Name:");
            if(state == null || state.trim().isEmpty()){
                io.print("State Can't be Blank, Try Again");
                flag = true;
            }else if(!states.contains(state)){
                io.print("STATE: "+ state);
                io.print("State Doesn't Exist, Try Again");
                flag = true;
            }else{
                flag=false;
            }
            
        }while(flag);
     
        
        displayProductList(products);
        String productType;
        do{
            productType = io.readString("Please enter Product Name:");
            if(productType == null || productType.trim().isEmpty()){
                io.print("Product Can't be Blank, Try Again");
                flag = true;
            }else if(!productTypes.contains(productType)){
                io.print("Product Types Doesn't Exist, Try Again");
                flag = true;
            }else{
                flag=false;
            }
        }while(flag);
                
        
        String area;
        do{
            area = io.readString("Please enter the Area (Minimum is 100 sqft)");
            if(Double.parseDouble(area) < 0 || Double.parseDouble(area) > 100){
                io.print("Area is not Within Range, Try Again");
                flag = true;
            }else{
                flag=false;
            }
        }while(flag);
        
        BigDecimal newarea = new BigDecimal(area);
        
        Order unfinishedOrder = new Order(customerName, state, productType, newarea);
        
        answer.put(orderDate, unfinishedOrder);
        return answer;
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
    
    public String getDateCreate(){
        LocalDate date = null;
        boolean flag = true;
        
        while(flag){
            String date1 = io.readString("Please enter the date (MM/DD/YYYY)");
            
            date = LocalDate.parse(date1, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            LocalDate currentDate = LocalDate.now();
                if(date.compareTo(currentDate) > 0){
                    //CHANGE
                    flag=false;
                }else{
                    io.print("Date not in the future. Try Again");
                }
            }
        //Attempting to format date
        //GETTING RID OF DATE TIME HERE!!!
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM/d/uuuu");
        String text = date.format(formatters);
       
        io.print("DATE: "+ text);
        
        return text;
    }
    
   
    public String getDate(){
        //CHANGE
        String date1 = io.readString("Please enter the date (MM/DD/YYYY)");
        //LocalDate date = LocalDate.parse(date1, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String newDate = date1.replace("/", "");
        String order = "Orders_";
        return order + newDate + ".txt";
        
    }
    
    public void displayCreateSuccessBanner() {
        io.readString("Order successfully created.  Please hit enter to continue");
    }
    
    public void displayOrderList(List<Order> orderList) {
        if(orderList.size() == 0){
            displayErrorMessage("There are no orders for that date!");
        }
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
    
    public boolean displayConfirmation(String action) {
        String confirm = io.readString("Are you sure you want to " + action + " the order? (y/n)");
        if(confirm.equals("y")){
            return true;
        }
        //return false for any other output
        return false;
    }
    
    
    public void displayEditDVDBanner(){
        io.print("=== Edit Order ===");
    }
    
    //Checks to see if Order is contained in collection
    public boolean displayEditResult(Order order) {
        if(order != null){
          io.print("Order #" + order.getOrderNumber() + " Selected");
          return true;
        }
          io.print("No such Order.");
          io.readString("Please hit enter to continue.");
          return false;
    }
    
    
    public ArrayList<String> editMenu(String prevCustomerName, String prevState, String prevProductType, BigDecimal prevArea){
        //Have condition (if statement) if user enters ENTER**
        String newCustomerName = io.readString("Enter customer name (" + prevCustomerName + "):");
        newCustomerName = newCustomerName.equals("") ? prevCustomerName : newCustomerName;
        
        String newstateName = io.readString("Enter state name (" + prevState + "):");
        newstateName = newstateName.equals("") ? prevState : newstateName;

        String newProductType = io.readString("Enter product type (" + prevProductType + "):");
        newProductType = newProductType.equals("") ? prevProductType : newProductType;

        
        String newArea = io.readString("Enter area (" + prevArea + "):");
        newArea = newArea.toString().equals("") ? prevArea.toString() : newArea;

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
