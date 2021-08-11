/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flooring_mastery.dao;

import com.flooring_mastery.model.Order;
import com.flooring_mastery.model.Product;
import com.flooring_mastery.model.Tax;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.springframework.stereotype.Component;


//@Component
public class FlooringMasteryDAOFileImpl implements FlooringMasteryDAO {

    //HashMap<orderNumber,Order> order
    //Stores all orders by order number
    //OrderNumber has to be String ***
    private Map<String, Order> orders = new HashMap<>();
    //HashMap<productType,Product> product
    private Map<String, Product> product = new HashMap<>();
    //HashMap<StateAbrreviation, Tax> tax
    private Map<String, Tax> tax = new HashMap<>();
    
    private File folder = new File("SampleFileData/Orders");
    
    //CHANGE*****
    //Test file to place orders
    public String ROSTER_FILE;
    
    //File of tax information
    public static final String TAX_FILE = "SampleFileData/Data/Taxes.txt";
    
    //File of product information
    public static final String PRODUCT_FILE = "SampleFileData/Data/Products.txt";
    
    //change the delimiter (",")************
    public static final String DELIMITER = ",";
    
    public FlooringMasteryDAOFileImpl(){
        ROSTER_FILE = "SampleFileData/Orders/";
    }

    public FlooringMasteryDAOFileImpl(String rosterTextFile){
        ROSTER_FILE = rosterTextFile;
    }

    @Override
    public ArrayList<File> getAllFiles() throws FlooringMasteryPersistenceException{
        ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
        return listOfFiles;
    }
    
    @Override
    public Order addOrder(String orderNumber, Order order, String fileName) throws FlooringMasteryPersistenceException {
           //Check if file name is in the Orders directory
           ArrayList<File> files = getAllFiles();
           Order prevOrder;
            //CHANGE LOGIC AROUND
            for(File file: files){
                //if file exist in directory
                if(file.getName().contains(fileName)){
                     loadRoster(fileName);
                     prevOrder = orders.put(orderNumber,order);
                     writeRoster(fileName);
                     return prevOrder;
                }
            }
               
            //File Doesn't Exist
            //Create new file
            File newFile = new File(fileName);
            //add file
            files.add(newFile);
            //load roster
            //loadRoster(fileName);
            prevOrder = orders.put(orderNumber,order);
            writeRoster(fileName);
            return prevOrder;
     
    }

    @Override
    public List<Order> getAllOrders(String fileName) throws FlooringMasteryPersistenceException{
        loadRoster(fileName);
        return new ArrayList<Order>(orders.values());
    }
    
    @Override
    public Order getOrder(String orderNumber,String fileName) throws FlooringMasteryPersistenceException {
        loadRoster(fileName);
        return orders.get(orderNumber);
    }
    
    @Override
    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException{
        loadTaxes();
        return new ArrayList<Tax>(tax.values());
    }
    
    @Override
    public List<String> getAllStateAB() throws FlooringMasteryPersistenceException{
        loadTaxes();
        ArrayList<String> states = new ArrayList<String>();
        List<Tax> tList = getAllTaxes();
        for(Tax tax: tList){
            states.add(tax.getStateAbbreviation());
        }
        return states;
    }
    
    @Override
    public List<String> getAllStates() throws FlooringMasteryPersistenceException{
        loadTaxes();
        ArrayList<String> states = new ArrayList<String>();
        List<Tax> tList = getAllTaxes();
        for(Tax tax: tList){
            states.add(tax.getStateName());
        }
        return states;
    }
    
    @Override
    public Tax getTax(String state) throws FlooringMasteryPersistenceException {
        loadTaxes();
        return tax.get(state);
    }
    
    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException{
        loadProducts();
        return new ArrayList<Product>(product.values());
    }
    
    @Override
    public List<String> getAllProductTypes() throws FlooringMasteryPersistenceException{
        loadProducts();
        ArrayList<String> pType = new ArrayList<String>();
        List<Product> pList = getAllProducts();
        for(Product p: pList){
            pType.add(p.getProductType());
        }
        return pType;
    }
    
    @Override
    public Product getProduct(String productType) throws FlooringMasteryPersistenceException {
        loadProducts();
        return product.get(productType);
    }
    
    
    @Override
    public Order removeOrder(String orderNumber, String fileName) throws FlooringMasteryPersistenceException{
        loadRoster(fileName);
        Order removedOrder = orders.remove(orderNumber);
        writeRoster(fileName);
        return removedOrder;
    }
    
    @Override
    public Order editOrder(Order editOrder, ArrayList<String> newValues, String fileName) throws FlooringMasteryPersistenceException{
        loadRoster(fileName);
        Order editO = editOrder;
        String newCustomerName = newValues.get(0);
        String newState = newValues.get(1);
        String newProductType = newValues.get(2);
        String newArea = newValues.get(3);
        //System.out.println("New Area: " + newArea);

        //DEAL WITH SITUATION OF BLANK INPUT**********
        editO.setCustomerName(newCustomerName);
        editO.setState(newState);
        editO.setProductType(newProductType);
        editO.setArea(new BigDecimal(newArea));
        
        orders.replace(editO.getOrderNumber(), editO);
        writeRoster(fileName);
        return editO;
    }
    
    public Order calculateOrderInfo(Order prevOrder, Tax specifcTax, Product specifcProduct){
        Order theOrder = prevOrder;
        BigDecimal orderArea = prevOrder.getArea();
        
        //Setting other values
        //Tax Rate:
       // BigDecimal tRate = specifcTax.getTaxRate();
        theOrder.setTaxRate(specifcTax.getTaxRate());
      
        //Cost per Square:
        //BigDecimal costPRate = specifcProduct.getCostPerSquareFoot();
        theOrder.setCostPerSquareFoot(specifcProduct.getCostPerSquareFoot());
        
        //Labor Cost Per:
        //BigDecimal laborCPer = specifcProduct.getLaborCostPerSquareFoot();
        theOrder.setLaborCostPerSquareFoot(specifcProduct.getLaborCostPerSquareFoot());
        
        //Material Cost:
        BigDecimal materialCost = orderArea.multiply(specifcProduct.getCostPerSquareFoot());
        theOrder.setMaterialCost(materialCost);
        
        //Labor Cost:
        BigDecimal laborCost = orderArea.multiply(specifcProduct.getLaborCostPerSquareFoot());
        theOrder.setLaborCost(laborCost);
        
        //Tax:
        BigDecimal costs = materialCost.add(laborCost);
        //Tax rate stored as whole numbers???
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal newTaxRate = specifcTax.getTaxRate().divide(hundred);
        BigDecimal theTax = costs.multiply(newTaxRate);
        theOrder.setTax(theTax.setScale(2, RoundingMode.HALF_UP));
        
        //Total 
        BigDecimal total = materialCost.add(laborCost);
        BigDecimal grandTotal = total.add(theTax);
        theOrder.setTotal(grandTotal.setScale(2, RoundingMode.HALF_UP));
        
        return theOrder;

    }
    
    

    
    private Order unmarshallOrder(String orderAsText){
        //Splits with delimeter
        String[] orderTokens = orderAsText.split(DELIMITER);

        // Given the pattern above, the student Id is in index 0 of the array.
        String orderId = orderTokens[0];

        // Which we can then use to create a new Student object to satisfy
        // the requirements of the Student constructor.
        Order orderFromFile = new Order(orderId);

        
        // Index 1 - customerName
        String customerName = orderTokens[1];
        //orderFromFile.setCustomerName(orderTokens[1]);

        // Index 2 - state
        String state = orderTokens[2];
        //orderFromFile.setState(orderTokens[2]);

        // Index 3 - taxRate
        BigDecimal taxRate = new BigDecimal(orderTokens[3]);
        
        // Index 4 - productType
        String productType = orderTokens[4];

        // Index 5 - area
        BigDecimal area = new BigDecimal(orderTokens[5]);

        
//        // Index 6 - costPerSquareFoot
        BigDecimal costPerSquareFoot = new BigDecimal(orderTokens[6]);
        
//         // Index 7 - laborCostPerSquareFoot
        BigDecimal laborCostPerSquareFoot = new BigDecimal(orderTokens[7]);

//
//        // Index 8 - materialCost
          BigDecimal materialCost = new BigDecimal(orderTokens[8]);
//
//        // Index 9 - laborCost
          BigDecimal laborCost = new BigDecimal(orderTokens[9]);
//        
//        // Index 10 - tax
          BigDecimal tax = new BigDecimal(orderTokens[10]);
//
//        // Index 11 - total
          BigDecimal total = new BigDecimal(orderTokens[11]);
        
        Order newOrder = new Order(orderId, customerName, state, taxRate,
        productType, area, costPerSquareFoot, laborCostPerSquareFoot,
        materialCost, laborCost, tax, total);

        return newOrder;
    }
    
    //CHANGE HERE******
    private void loadRoster(String fileName) throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            //Concatenating file name
                            new FileReader(ROSTER_FILE + fileName)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "-_- Could not load roster data into memory.", e);
        }
        
        String currentLine;
        
        Order currentOrder;
        
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a Student
            currentOrder = unmarshallOrder(currentLine);

            // We are going to use the student id as the map key for our student object.
            // Put currentStudent into the map using student id as the key
            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }
        // close scanner
        scanner.close();
    }
    
    private String marshallOrder(Order aOrder){
       
        String orderAsText = aOrder.getOrderNumber() + DELIMITER;

        // customerName
        orderAsText += aOrder.getCustomerName() + DELIMITER;

        // State
        orderAsText += aOrder.getState() + DELIMITER;
        
        // Tax Rate
        orderAsText += aOrder.getTaxRate() + DELIMITER;

        // Product Type - don't forget to skip the DELIMITER here.
        orderAsText += aOrder.getProductType()+ DELIMITER;
        
        // Area
        orderAsText += aOrder.getArea() + DELIMITER;

        // Cost Per Square Foot
        orderAsText += aOrder.getCostPerSquareFoot() + DELIMITER;

        // Get Labor Cost Per Square Foot
        orderAsText += aOrder.getLaborCostPerSquareFoot() + DELIMITER;
        
        // Material Cost
        orderAsText += aOrder.getMaterialCost()+ DELIMITER;

        // Labor Cost
        orderAsText += aOrder.getLaborCost() + DELIMITER;

        // Get Labor Cost Per Square Foot
        orderAsText += aOrder.getTax() + DELIMITER;
        
        // Total
        orderAsText += aOrder.getTotal();

        // We have now turned a student to text! Return it!
        return orderAsText;
    }
    
    private void writeRoster(String fileName) throws FlooringMasteryPersistenceException {
        
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(ROSTER_FILE  + fileName));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not save student data.", e);
        }

        String orderAsText;
        List<Order> orderList = this.getAllOrders(fileName);
        for (Order currentOrder : orderList) {
            // turn a Student into a String
            orderAsText = marshallOrder(currentOrder);
            // write the Student object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }
    
    //Method to load Tax Files
    private void loadTaxes() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "-_- Could not load roster data into memory.", e);
        }
        
        String currentLine;
        Tax currentTax;
        
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            //Make seperate unmarshall Tax Method
            currentTax = unmarshallTax(currentLine);
            
            tax.put(currentTax.getStateName(), currentTax);
        }
        // close scanner
        scanner.close();
    }
    
    private Tax unmarshallTax(String taxAsText){
        //Splits with delimeter
        String[] taxTokens = taxAsText.split(DELIMITER);

        // Given the pattern above, the student Id is in index 0 of the array.
        String stateAB = taxTokens[0];

        Tax taxFromFile = new Tax(stateAB);

        // Index 1 - State Name
        String stateName = taxTokens[1];

        // Index 2 - Tax Rate
        BigDecimal taxRate = new BigDecimal(taxTokens[2]);
        
        Tax newTax = new Tax(stateAB, stateName, taxRate);
        
        return newTax;
    }
    
    //Method to load Product File
    private void loadProducts() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "-_- Could not load roster data into memory.", e);
        }
        
        String currentLine;
        Product currentProduct;
        
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            //Make seperate unmarshall Tax Method
            currentProduct = unmarshallProduct(currentLine);
            
            product.put(currentProduct.getProductType(), currentProduct);
        }
        // close scanner
        scanner.close();
    }
    
    private Product unmarshallProduct(String productAsText){
        //Splits with delimeter
        String[] productTokens = productAsText.split(DELIMITER);

        // Given the pattern above, the student Id is in index 0 of the array.
        String productType = productTokens[0];

        Product productFromFile = new Product(productType);

        // Index 1 - costPerSquareFoot
        BigDecimal costPerSquareFoot = new BigDecimal(productTokens[1]);

        // Index 2 - Labor Cost Per Square Foot
        BigDecimal laborCostPerSquareFoot = new BigDecimal(productTokens[2]);
        
        Product newProduct= new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);
        
        return newProduct;
    }
    

    
    @Override
    public String generateNextOrderNumber(String fileName) throws FlooringMasteryPersistenceException{
        //Retrive keys of orders
        ArrayList<File> files = getAllFiles();
        int orderNum = 0;
        for(File file: files){
            //File exits
            if(file.getName().contains(fileName)){
                //Load file
                loadRoster(fileName);
                //Retrive order numbers
                ArrayList<String> orderNumbers = new ArrayList<>(orders.keySet());
                for(String key : orderNumbers){
                    System.out.println("ORDER NUMBER: " + key);
                }
                String last = orderNumbers.get(orderNumbers.size() - 1);
                System.out.println("LAST!!" + last);

                orderNum = Integer.parseInt(last);
                
                orderNum++;
                return String.valueOf(orderNum);   
            }
        }
      
        //Return new order Number of 1
        return String.valueOf("1");
        
    }


   }
