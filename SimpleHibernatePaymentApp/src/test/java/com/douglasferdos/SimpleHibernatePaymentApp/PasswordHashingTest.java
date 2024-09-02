package com.douglasferdos.SimpleHibernatePaymentApp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class PasswordHashingTest {
	
	PasswordHashing PHash = new PasswordHashing();
	
	@Test
	@Order(1)
	public void checkPassword_Test() {
		
		String password = "StrongPasswordHerePlease123!@#";
		
		Object[] PassHashAndSalt = PHash.passwordHashing(password);
		
		String passHash = (String) PassHashAndSalt[0];
		
		byte[] salt = (byte[]) PassHashAndSalt[1];
		
		boolean actual = PHash.passwordCheck(password, passHash, salt);
		
		Assertions.assertTrue(actual);
	}
}