/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fshahbaz.flooring_mastery;

import com.flooring_mastery.controller.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//import com.springframework.context.ApplicationContext;

/**
 *
 * @author farhanshahbaz
 */
public class App {
    public static void main(String[] args) {
        
        //Object that holds application context
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        FlooringMasteryController controller = appContext.getBean("controller", FlooringMasteryController.class);
        controller.run();
      }   
}

   
