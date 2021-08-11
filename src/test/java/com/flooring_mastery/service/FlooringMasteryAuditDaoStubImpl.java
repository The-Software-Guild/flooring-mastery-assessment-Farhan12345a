/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flooring_mastery.service;

import com.flooring_mastery.dao.FlooringMasteryAuditDao;
import com.flooring_mastery.dao.FlooringMasteryPersistenceException;

/**
 *
 * @author farhanshahbaz
 */
public class FlooringMasteryAuditDaoStubImpl implements FlooringMasteryAuditDao{

    public FlooringMasteryAuditDaoStubImpl() {
    }
    
    
    @Override
    public void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException {
        //do nothing . . .
    }
}
