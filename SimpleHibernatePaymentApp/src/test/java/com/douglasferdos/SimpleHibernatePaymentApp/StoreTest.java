package com.douglasferdos.SimpleHibernatePaymentApp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreTest {
	
	// Preparations for the testing
	Store store;
	int EIN = 227777777;
	int failEIN = 772222222;
	String fname = "FooStore";
	String email = "FooStore@mail.com";
	String password = "FooPassword";

	@BeforeEach
	public void beforeEach() {
		store = new Store();
	}
	
	@Test
	@Order(1)
	public void createStore_SucessfullyCreate_Test() {
		
		String expected = "Store account sucessfully created";
	
		String actual = store.createStore(EIN, fname, email, password);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(2)
	public void createStore_EINAlreadyExists_Test() {
		
		String expected = "Specified EIN already has an account";

		String actual = store.createStore(EIN, fname, email, password);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(3)
	public void createStore_emailAlreadyExists_Test() {
		
		String expected = "Specified email already has an account";

		String actual = store.createStore(failEIN, fname, email, password);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(4)
	public void emailExists_True_Test() {
		
		boolean test = store.emailExists(email);
		
		Assertions.assertTrue(test);
	}
	
	@Test
	@Order(5)
	public void emailExists_False_Test() {
		
		boolean test = store.emailExists("emailThatWillNotExistInDB@mail.com");
		
		Assertions.assertFalse(test);
	}
	
	@Test
	@Order(97)
	public void deleteStore_WrongEIN_Test() {
		
		String expected = "EIN or Password incorrect";
		
		String actual = store.deleteStore(failEIN, password);
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(98)
	public void deleteEIN_WrongPassword_Test() {
		
		String expected = "EIN or Password incorrect";
		
		String actual = store.deleteStore(EIN, "password123");
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	@Order(99)
	public void deleteStore_Successfully_Test() {
		
		String expected = "Store deleted";

		String actual = store.deleteStore(EIN, password);

		Assertions.assertEquals(expected, actual);
	}
}
