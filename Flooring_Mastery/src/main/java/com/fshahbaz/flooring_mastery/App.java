/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fshahbaz.flooring_mastery;

import com.flooring_mastery.controller.FlooringMasteryController;
import com.flooring_mastery.dao.FlooringMasteryAuditDao;
import com.flooring_mastery.dao.FlooringMasteryAuditDaoFileImpl;
import com.flooring_mastery.dao.FlooringMasteryDAO;
import com.flooring_mastery.dao.FlooringMasteryDAOFileImpl;
import com.flooring_mastery.service.FlooringMasteryServiceLayer;
import com.flooring_mastery.service.FlooringMasteryServiceLayerImpl;
import com.flooring_mastery.userio.UserIO;
import com.flooring_mastery.view.FlooringMasteryView;
import com.sg.classroster.io.UserIOConsoleImpl;

/**
 *
 * @author farhanshahbaz
 */
public class App {
    public static void main(String[] args) {
        //FlooringMasteryController controller = new FlooringMasteryController();
        UserIO myIo = new UserIOConsoleImpl();
        FlooringMasteryView myView = new FlooringMasteryView(myIo);
        FlooringMasteryDAO myDao = new FlooringMasteryDAOFileImpl();
        FlooringMasteryAuditDao myAuditDao = new FlooringMasteryAuditDaoFileImpl();
        FlooringMasteryServiceLayer myService = new FlooringMasteryServiceLayerImpl(myDao, myAuditDao);     
        FlooringMasteryController controller = new FlooringMasteryController(myService, myView);
        controller.run();
    }   
}
