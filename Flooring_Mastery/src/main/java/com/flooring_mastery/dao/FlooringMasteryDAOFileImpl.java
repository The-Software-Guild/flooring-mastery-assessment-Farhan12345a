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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author farhanshahbaz
 */
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
    public static final String ROSTER_FILE = "SampleFileData/Orders/test.txt";
    
    //File of tax information
    public static final String TAX_FILE = "SampleFileData/Data/Taxes.txt";
    
    //File of product information
    public static final String PRODUCT_FILE = "SampleFileData/Data/Products.txt";
    
    //change the delimiter (",")************
    public static final String DELIMITER = ",";
    
    @Override
    public ArrayList<File> getAllFiles() throws FlooringMasteryPersistenceException{
        ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
        return listOfFiles;
    }
    
    @Override
    public Order addOrder(String orderNumber, Order order) throws FlooringMasteryPersistenceException {
           loadRoster();
           Order prevOrder = orders.put(orderNumber,order);
           writeRoster();
           return prevOrder;
    }

    @Override
    public List<Order> getAllOrders() throws FlooringMasteryPersistenceException{
        loadRoster();
        return new ArrayList<Order>(orders.values());
    }
    
    @Override
    public Order getOrder(String orderNumber) throws FlooringMasteryPersistenceException {
        loadRoster();
        return orders.get(orderNumber);
    }
    
    @Override
    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException{
        loadTaxes();
        return new ArrayList<Tax>(tax.values());
    }
    
    @Override
    public List<String> getAllStates() throws FlooringMasteryPersistenceException{
        loadTaxes();
        ArrayList<String> states = new ArrayList<String>();
        List<Tax> tList = getAllTaxes();
        for(Tax tax: tList){
            states.add(tax.getStateAbbreviation());
        }
        return states;
    }
    
    @Override
    public Tax getTax(String stateAB) throws FlooringMasteryPersistenceException {
        loadTaxes();
        return tax.get(stateAB);
    }
    
    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException{
        loadProducts();
        return new ArrayList<Product>(product.values());
    }
    
    @Override
    public Product getProduct(String productType) throws FlooringMasteryPersistenceException {
        loadProducts();
        return product.get(productType);
    }

    @Override
    public Order removeOrder(String orderNumber) throws FlooringMasteryPersistenceException{
        loadRoster();
        Order removedOrder = orders.remove(orderNumber);
        writeRoster();
        return removedOrder;
    }
    
    @Override
    public Order editOrder(Order editOrder, ArrayList<String> newValues) throws FlooringMasteryPersistenceException{
        loadRoster();
        Order editO = editOrder;
        String newCustomerName = newValues.get(0);
        String newState = newValues.get(1);
        String newProductType = newValues.get(2);
        String newArea = newValues.get(3);
        //System.out.println("New Area: " + newArea);

        if(!newCustomerName.equals(editO.getCustomerName())){
            editO.setCustomerName(newCustomerName);
        }//Maybe have else???
        
        if(!newState.equals(editO.getState())){
            editO.setState(newState);
        }//Maybe have else???
        
        if(!newProductType.equals(editO.getProductType())){
            editO.setProductType(newProductType);
        }//Maybe have else???
        
        //BIG DECIMAL CONVERSION
        //NOT CONVERTING AREA
        BigDecimal convertedArea = new BigDecimal(newArea);
        if(!newArea.equals(editO.getArea())){
            System.out.println("New Area: " + newArea);
           // editO.setArea(convertedArea);
        }//Maybe have else???
        editO.setArea(new BigDecimal(newArea));
        
        
        orders.replace(editO.getOrderNumber(), editO);
        writeRoster();
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
        theOrder.setTax(theTax);
        
        //Total 
        BigDecimal total = materialCost.add(laborCost);
        BigDecimal grandTotal = total.add(theTax);
        theOrder.setTotal(grandTotal);
        
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
    private void loadRoster() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ROSTER_FILE)));
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
    
    private void writeRoster() throws FlooringMasteryPersistenceException {
        
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(ROSTER_FILE));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not save student data.", e);
        }

        String orderAsText;
        List<Order> orderList = this.getAllOrders();
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
            
            tax.put(currentTax.getStateAbbreviation(), currentTax);
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
    public String generateNextOrderNumber(){
        //Retrive keys of orders
        ArrayList<String> orderNumbers = new ArrayList<>(orders.keySet());
        ArrayList<Integer> listOfInteger = new ArrayList<>();
        for(String num : orderNumbers){
            listOfInteger.add(Integer.parseInt(num));
        }
        //Convert List to Integers
        //List<Integer> listOfInteger = convertStringListToIntList(orderNumbers,Integer::parseInt);
        //Find maximum
        int newMax = Collections.max(listOfInteger);
        //int newMax = 2;
        //Incrementing Maximum
        newMax++;
        //Converting back to String and returning
        return String.valueOf(newMax);
        
    }
    
    // Generic function to convert List of
    // String to List of Integer
    public static <T, U> List<U> convertStringListToIntList(List<T> listOfString,
                               Function<T, U> function){
        return listOfString.stream()
            .map(function)
            .collect(Collectors.toList());
    }
    
    
    
    

   }
