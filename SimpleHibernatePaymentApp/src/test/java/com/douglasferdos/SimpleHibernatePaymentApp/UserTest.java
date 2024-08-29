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
	String password = "FooPassword";
	BigDecimal money = new BigDecimal("100");
	BigDecimal largeAmount = new BigDecimal("999999999999");
	
	@BeforeAll
	public static void createTheTransferenceReceiver() {
		
		User user = new User();		
		user.createUser(333222111, "Receiver", "Receiver@mail.com", "ReceiverPassword");
	}
	
	@AfterAll
	public static void deleteTheTransferenceReceiver() {
		
		User user = new User();	
		user.deleteUser(333222111, "ReceiverPassword");
	}
	
	@BeforeEach
	public void beforeEach() {
		user = new User();
	}
	
	//Tests
	
	@Test
	@Order(1)
	public void createUser_SucessfullyCreate_Test() {
		
		String expected = "User account sucessfully created";
	
		String actual = user.createUser(SSN, "FooBang", "Foo@mail.com", password);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(2)
	public void createUser_AlreadyExists_Test() {
		
		String expected = "Specified SSN already has an account";

		String actual = user.createUser(SSN, "FooBang", "Foo@mail.com", password);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(3)
	public void depositMoney_WrongSSN_Test() {
		
		String expected = "Wrong SSN";

		String actual = user.depositMoney(failSSN, money);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(4)
	public void depositMoney_SuccessfullyDeposit_Test() {
		
		String expected = money + "$ was deposited successfully";
		
		String actual = user.depositMoney(SSN, money);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(5)
	public void transferMoney_ToAnotherUser_WrongSSN_Test() {
		
		String expected = "Could not found the destiny SSN";
		
		String actual = user.transferMoney(SSN, password, failSSN, money);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(6)
	public void transferMoney_InsufficientBalance_Test() {
		
		String expected = "Insufficient Balance";
		
		String actual = user.transferMoney(SSN, password, receiverSSN, largeAmount);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(7)
	public void transferMoney_ToAnotherUser_Success_Test() {
		
		String expected = money + "$ Transferred to SSN = " + receiverSSN;
		
		String actual = user.transferMoney(SSN, password, receiverSSN, money);
		
		Assertions.assertEquals(expected, actual);
	}
	
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
	public void deleteUser_Test() {
		
		String expected = "User deleted";

		String actual = user.deleteUser(SSN, password);

		Assertions.assertEquals(expected, actual);
	}

}
