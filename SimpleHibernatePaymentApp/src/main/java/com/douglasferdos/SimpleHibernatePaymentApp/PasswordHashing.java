package com.douglasferdos.SimpleHibernatePaymentApp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class PasswordHashing {
	
	// Generates a 16 Byte salt utilizing the inbuilt java security
	private byte[] generateSalt16Byte() {
		
		// Instantiating the Secure Random java class
	    SecureRandom secureRandom = new SecureRandom();
	    
	    // Creating the 16 bytes array for salting storage
	    // The size recommended by Argon2 authors
	    byte[] salt = new byte[16];
	    
	    // Fill the array with secure random bytes
	    secureRandom.nextBytes(salt);
	    return salt;
	}
	
	// Creates the password
	protected Object[] passwordHashing(String password){
		
		// Use the generateSalt16Byte method to generate a salt
		byte[] salt = generateSalt16Byte();
		
		// Pepper will add to the strength of the password hash
		// if the database get compromised but the application does not
		final byte[] PEPPER = {60, 84, -127, 28, 41, -7, 39, 101,
						    -44, 22, -52, 124, 53, 66, -99, -105,
						  -76, -39, -103, -45, -56, -95, 14, 101,
						  -46, -13, 78, -19, 118, 118, -31, -29};
		
		// initialize a byte array with the necessary length
		byte[] spicedSalt = new byte[salt.length + PEPPER.length];
		
		// Concatenates the salt and pepper
		try(ByteArrayOutputStream concatByte = new ByteArrayOutputStream();) {
			concatByte.write(PEPPER);
			concatByte.write(salt);
			spicedSalt = concatByte.toByteArray();
		} catch (IOException e) {}
		
		// for every parameter the greater the better,
		// but keep in mind that this will directly impact the UX 
		
		// Number of iterations over the memory.
		// Execution time correlates linearly with this parameter.
		// Computational cost required to calculate one hash.
	    int iterations = 4;

	    // The the memory cost. (64 MB)
	    int memory = 65536;
	    
	    // The hash length (in bytes).
	    int hashLength = 32;
	    
	    // The number of threads to use
	    int parallelism = 2;
		
	    // Defining the algorithm with the specified values  
	    Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
	    	      .withVersion(Argon2Parameters.ARGON2_VERSION_13)
	    	      .withIterations(iterations)
	    	      .withMemoryAsKB(memory)
	    	      .withParallelism(parallelism)
	    	      .withSalt(spicedSalt);
	    
	    // Generating the hash
	    // Instantiating the Argon generator from Bouncy Castle Library
	    Argon2BytesGenerator generator = new Argon2BytesGenerator();
	    
	    // Initialize the Argon2BytesGenerator from the parameters set in builder.
	    generator.init(builder.build());
	    
	    // Creating the bytes array for storing the hash
	    byte[] result = new byte[hashLength];
	    
	    // Hashing the password
	    generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), result);
	    
	    // Get the string from the hash to store in the database 
	    String hashedPassword = Base64.getEncoder().encodeToString(result);
	    
	    // create the Object with the info to store in the database
	    Object[] hashedPasswordandSalt = new Object[2]; 
	    

	    
	    hashedPasswordandSalt[0] = hashedPassword;
	    hashedPasswordandSalt[1] = salt;
	    
	    // return the Object
	    return hashedPasswordandSalt;
	}
	
	// check if the input password is correct
	protected boolean passwordCheck(String passwordToCheck, String storedHash, byte[] salt) {
		
		// Pepper will add to the strength of the password hash
		// if the database get compromised but the application does not
		final byte[] PEPPER = {60, 84, -127, 28, 41, -7, 39, 101,
						    -44, 22, -52, 124, 53, 66, -99, -105,
						  -76, -39, -103, -45, -56, -95, 14, 101,
						  -46, -13, 78, -19, 118, 118, -31, -29};
		
		// initialize a byte array with the necessary length
		byte[] spicedSalt = new byte[salt.length + PEPPER.length];
		
		// Concatenates the salt and pepper
		try(ByteArrayOutputStream concatByte = new ByteArrayOutputStream();) {
			concatByte.write(PEPPER);
			concatByte.write(salt);
			spicedSalt = concatByte.toByteArray();
		} catch (IOException e) {}
		
		// Must have the same configuration of the original hashing
		// Or be store with the salt and hashed password
		
		// Number of iterations over the memory.
		// Execution time correlates linearly with this parameter.
		// Computational cost required to calculate one hash.
	    int iterations = 4;

	    // The the memory cost. (64 MB)
	    int memory = 65536;
	    
	    // The hash length (in bytes).
	    int hashLength = 32;
	    
	    // The number of threads to use
	    int parallelism = 2;
		
	    // Defining the algorithm with the specified values  
	    Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
	    	      .withVersion(Argon2Parameters.ARGON2_VERSION_13)
	    	      .withIterations(iterations)
	    	      .withMemoryAsKB(memory)
	    	      .withParallelism(parallelism)
	    	      .withSalt(spicedSalt);
	    
	    // Generating the hash
	    // Instantiating the Argon generator from Bouncy Castle Library
	    Argon2BytesGenerator generator = new Argon2BytesGenerator();
	    
	    // Initialize the Argon2BytesGenerator from the parameters set in builder.
	    generator.init(builder.build());
	    
	    // Creating the bytes array for storing the hash
	    byte[] result = new byte[hashLength];
	    
	    // Hashing the password
	    generator.generateBytes(passwordToCheck.getBytes(StandardCharsets.UTF_8), result);
	    // Convert to String to compare with the stored hash
	    String newHash = Base64.getEncoder().encodeToString(result);
	    
	    // Compare the two rest and return a boolean
		return newHash.equals(storedHash);
	}

}
