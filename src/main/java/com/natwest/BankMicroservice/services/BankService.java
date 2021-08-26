package com.natwest.BankMicroservice.services;

import java.util.List;

import com.natwest.BankMicroservice.exceptions.BankAccountAlreadyPresentException;
import com.natwest.BankMicroservice.exceptions.BankNotPresentException;
import com.natwest.BankMicroservice.exceptions.UserAlreadyPresentException;
import com.natwest.BankMicroservice.exceptions.UserNotPresentException;
import com.natwest.BankMicroservice.model.Bank;
import com.natwest.BankMicroservice.model.User;

public interface BankService {
	// User Operations
	User addUser(User usrObj) throws BankAccountAlreadyPresentException, UserAlreadyPresentException;	
	List<User> viewUsers();
	boolean deleteBus(String userid);
	User updateUser(User userObj);

	// Bank related operations
	public Bank addBank(User userObj) throws BankAccountAlreadyPresentException;
	public Bank updateBank(Bank bankObj, String userId) throws UserNotPresentException;
	public void deleteBank(String userId) throws UserNotPresentException, BankNotPresentException;
}
