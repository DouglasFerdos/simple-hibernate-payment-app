package com.douglasferdos.SimpleHibernatePaymentApp;


import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {
	
	// Preparations for the testing
	User user;
	int SSN = 111222333;
	int failSSN = 999999999;
	int receiverSSN = 333222111;
	int EIN = 227777777;
	int failEIN = 772222222;
	int receiverEIN = 338888888;
	String email = "Foo@mail.com";
	String password = "FooPassword";
	BigDecimal depositMoney = new BigDecimal("200");
	BigDecimal money = new BigDecimal("100");
	BigDecimal largeAmount = new BigDecimal("999999999999");
	
	@BeforeAll
	public static void createTheTransferenceReceiver() {
		
		// Creates receiver test User and Store
		User user = new User();		
		user.createUser(333222111, "Receiver", "Receiver@mail.com", "ReceiverPassword");
		
		Store store = new Store();
		store.createStore(338888888, "Receiver Store", "ReceiverStore@mail.com", "ReceiverStorePass");
	}
	
	@AfterAll
	public static void deleteTheTransferenceReceiver() {
		
		// Deletes receiver test User and Store
		User user = new User();	
		user.deleteUser(333222111, "ReceiverPassword");
		
		Store store = new Store();
		store.deleteStore(338888888, "ReceiverStorePass");
	}
	
	@BeforeEach
	public void beforeEach() {
		user = new User();
	}
	
	//Tests
	
	@Test
	@Order(1)
	public void emailExists_True_Test() {
		
		boolean test = user.emailExists("Receiver@mail.com");
		
		Assertions.assertTrue(test);
	}
	
	@Test
	@Order(2)
	public void emailExists_False_Test() {
		
		boolean test = user.emailExists("emailThatWillNotExistInDB@mail.com");
		
		Assertions.assertFalse(test);
	}
	
	@Test
	@Order(3)
	public void createUser_SucessfullyCreate_Test() {
		
		String expected = "User account sucessfully created";
	
		String actual = user.createUser(SSN, "FooBang", email, password);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(4)
	public void createUser_SSNAlreadyExists_Test() {
		
		String expected = "Specified SSN already has an account";

		String actual = user.createUser(SSN, "FooBang", email, password);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(5)
	public void createUser_emailAlreadyExists_Test() {
		
		String expected = "Specified email already has an account";

		String actual = user.createUser(failSSN, "FooBang", email, password);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(6)
	public void depositMoney_WrongSSN_Test() {
		
		String expected = "Wrong SSN";

		String actual = user.depositMoney(failSSN, money);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(7)
	public void depositMoney_SuccessfullyDeposit_Test() {
		
		String expected = depositMoney + "$ was deposited successfully";
		
		String actual = user.depositMoney(SSN, depositMoney);
		
		Assertions.assertEquals(expected, actual);
	}
	
	// To another user transfer tests
	@Test
	@Order(8)
	public void transferMoneyToAnotherUser_WrongSSN_Test() {
		
		String expected = "Could not find the destiny SSN";
		
		String actual = user.transferMoneyToAnotherUser(SSN, password, failSSN, money);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(9)
	public void transferMoneyToAnotherUser_InsufficientBalance_Test() {
		
		String expected = "Insufficient Balance";
		
		String actual = user.transferMoneyToAnotherUser(SSN, password, receiverSSN, largeAmount);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(10)
	public void transferMoneyToAnotherUser_Success_Test() {
		
		String expected = money + "$ Transferred to SSN = " + receiverSSN;
		
		String actual = user.transferMoneyToAnotherUser(SSN, password, receiverSSN, money);
		
		Assertions.assertEquals(expected, actual);
	}
	
	// To Store transfer tests
	@Test
	@Order(11)
	public void transferToMoney_WrongEIN_Test() {
		
		String expected = "Could not find the destiny EIN";
		
		String actual = user.transferMoneyToStore(SSN, password, failEIN, money);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(12)
	public void transferMoneyToStore_InsufficientBalance_Test() {
		
		String expected = "Insufficient Balance";
		
		String actual = user.transferMoneyToStore(SSN, password, receiverEIN, largeAmount);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(13)
	public void transferMoneyToStore_Success_Test() {
		
		String expected = money + "$ Transferred to EIN = " + receiverEIN;
		
		String actual = user.transferMoneyToStore(SSN, password, receiverEIN, money);
		
		Assertions.assertEquals(expected, actual);
	}
	
	// user delete tests
	@Test
	@Order(97)
	public void deleteUser_WrongSSN_Test() {
		
		String expected = "SSN or Password incorrect";
		
		String actual = user.deleteUser(failSSN, password);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(98)
	public void deleteUser_WrongPassword_Test() {
		
		String expected = "SSN or Password incorrect";
		
		String actual = user.deleteUser(SSN, "password123");
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(99)
	public void deleteUser_Successfully_Test() {
		
		String expected = "User deleted";

		String actual = user.deleteUser(SSN, password);

		Assertions.assertEquals(expected, actual);
	}

}
