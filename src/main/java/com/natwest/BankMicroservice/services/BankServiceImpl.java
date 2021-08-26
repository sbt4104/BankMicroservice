package com.natwest.BankMicroservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natwest.BankMicroservice.exceptions.BankAccountAlreadyPresentException;
import com.natwest.BankMicroservice.exceptions.BankNotPresentException;
import com.natwest.BankMicroservice.exceptions.UserAlreadyPresentException;
import com.natwest.BankMicroservice.exceptions.UserNotPresentException;
import com.natwest.BankMicroservice.model.Bank;
import com.natwest.BankMicroservice.model.User;
import com.natwest.BankMicroservice.repository.BankRepo;
import com.natwest.BankMicroservice.repository.UserRepo;

@Service
public class BankServiceImpl implements BankService{

	@Autowired
	BankRepo bankrepo;

	@Autowired
	UserRepo userrepo;
	
	@Override
	public User addUser(User usrObj) throws BankAccountAlreadyPresentException, UserAlreadyPresentException {
		Optional<User> useropt= userrepo.findById(usrObj.getUserid());
		
		if(useropt.isPresent())
		{		    	
			throw new UserAlreadyPresentException("User Already present");
		}
		else {
			if(usrObj.getBank() != null) {
				try {
					addBank(usrObj);
				} catch (BankAccountAlreadyPresentException e) {
					throw new BankAccountAlreadyPresentException("Bank Acount number already belongs to someone else");
				}
			}
			userrepo.save(usrObj);
			return usrObj;
		}
	}

	@Override
	public List<User> viewUsers() {
		return userrepo.findAll();
	}

	@Override
	public boolean deleteBus(String userid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User updateUser(User userObj) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Bank addBank(User userObj) throws BankAccountAlreadyPresentException {
		Bank currBank = userObj.getBank();
		Optional<Bank> bankopt= bankrepo.findById(currBank.getAccountNumber());
		
		if(bankopt.isPresent())
		{		    	
			throw new BankAccountAlreadyPresentException("Bank Acount number already belongs to someone else");
		}
		else {
			currBank.setUser(userObj.getUserid());
			bankrepo.save(currBank);
			return currBank;
		}
	}


	@Override
	public Bank updateBank(Bank bankObj, String userId) throws UserNotPresentException {
		Optional<User> useropt= userrepo.findById(userId);

		if(useropt.isPresent())
		{
			User currUser = useropt.get();
			
			// remove the already present present account
			if(currUser.getBank() != null && currUser.getBank().getAccountNumber() != bankObj.getAccountNumber()) {
				bankrepo.deleteById(currUser.getBank().getAccountNumber());
			}

			currUser.setBank(bankObj);
			bankObj.setUser(userId);
			bankrepo.save(bankObj);
		
			currUser.setBank(bankObj);
			userrepo.save(currUser);
			return bankObj;
		} else {
			throw new UserNotPresentException("User not present");
		}
	}

	@Override
	public void deleteBank(String userId) throws UserNotPresentException, BankNotPresentException {
		Optional<User> useropt= userrepo.findById(userId);

		if(useropt.isPresent())
		{
			User currUser = useropt.get();
			Bank currBank = currUser.getBank();
			if(currBank == null) {
				throw new BankNotPresentException("Bank is already deleted");
			}
			// delete bank from Bank Table
			bankrepo.deleteById(currBank.getAccountNumber());

			// set Bank to null in details
			currUser.setBank(null);
			userrepo.save(currUser);
		} else {
			throw new UserNotPresentException("User not present");
		}
	}



}
