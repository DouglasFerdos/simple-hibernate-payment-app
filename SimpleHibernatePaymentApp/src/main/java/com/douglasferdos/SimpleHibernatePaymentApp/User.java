package com.douglasferdos.SimpleHibernatePaymentApp;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

// Table Name
@Entity
@Table(name = "Users")
public class User {

	// Table Columns
	@Id
	@Column(name = "ssn", nullable = false ,length = 9, columnDefinition = "INT")
	private int SSN;
	
	@Column(name = "fname", nullable = false, length = 255)
	private String fName;
	
	@Column(name = "email", nullable = false, length = 255, unique = true)
	private String email;
	
	@Column(name = "balance") // length of 38,2 as the NUMERIC max value in PostgreSQL
	private BigDecimal balance;
	
	@Column(name = "passwordHash", nullable = false, length = 64, columnDefinition = "TEXT")
	private String password;
	
	// Empty constructor to Hibernate create when fetching data from DB
	public User() {}
	
	// Auxiliary constructor with parameters for User
	public User(int SSN, String fName, String email, BigDecimal balance, String password) {
		
		this.SSN = SSN;
		this.fName = fName;
		this.email = email;
		this.balance = balance;
		this.password = password;
		
	}
	
	// Create a user (Insert a row in Users Table)
	public String createUser(int SSN, String fName, String email, String password) {
		
		// instantiating User object with a null value
		User user;
		
		// BigDecimal Object with the default value of zero
		// used to set the account initial balance
		BigDecimal startBalance = new BigDecimal("0");
		
		
		// Try with resources block to create a User
    	try (
    		EntityManagerFactory emf = Persistence.createEntityManagerFactory("PostgresPU");
    		EntityManager em = emf.createEntityManager();
    		) {

    		// Search the DB for the specified SSN
    		user = em.find(User.class, SSN);
    		
    		// Create a user for the provided data if
    		// no user with the specified SSN is found
    		if (user == null) { 
    			
        		// if the user does not exists checks 
        		// for the email in the database
    			
    			if (emailExists(email) == false){
    		    			
	    			// Overrides the user data with the provided data
	    			user = new User(SSN, fName, email, startBalance, password);
	    			
		    		// Start the transaction
		    		em.getTransaction().begin();
		    		
		    		// Persist the transaction
		    		em.persist(user);
		    		
		    		// Commit the transaction
		    		em.getTransaction().commit();
	
    			} else {
    				return "Specified email already has an account";
    			}
    		} else {
    			
    			// return if failed: account with this SSN already exists
    			return "Specified SSN already has an account";
    		}
    		
    		// return if successful
    		return "User account sucessfully created";
    		
		} catch (Exception e) {
			
			// prints the stack trace error to the console
			e.printStackTrace();
		}
		
		// return if failed to create the account
    	// executes if the try with resources block fails
		return "Error";
	}
	
	public String deleteUser(int SSN, String password) {
		
		// instantiating User object with a null value
		User user;
		
		// Try with resources block to delete the specified user
		try (
	    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("PostgresPU");
	    	EntityManager em = emf.createEntityManager();
	    	) {
			
			// Search the DB for the specified SSN 
			user = em.find(User.class, SSN);
			
			// Check if not null first, else the get methods will fail
			// Delete the user if the SSN has been found
			if (user != null && user.getSSN() == SSN && user.getPassword().equals(password)) {
				
				// Start the transaction
	    		em.getTransaction().begin();
	    		
	    		// Delete the user
	    		em.remove(user);
	    		
	    		// Commit the transaction
	    		em.getTransaction().commit();
	    		
			} else {
    			
    			// return if failed
    			return "SSN or Password incorrect";
    		}
			
			// return if successful
			return "User deleted";
			
		} catch (Exception e) {
			
			// prints the stack trace error to the console
			e.printStackTrace();
		}
		
		// return if failed to delete the User
    	// executes if the try with resources block fails
		return "Error";
	}
	
	// Money deposit method
	public String depositMoney(int SSN, BigDecimal depositAmount) {

		// instantiating User object with a null value
		User user;
		
		// Try with resources block to deposit the amount
		try (
	    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("PostgresPU");
	    	EntityManager em = emf.createEntityManager();
	    	) {
			
			// Search the DB for the specified SSN
			user = em.find(User.class, SSN);
			
			// Check if the user exists
			if (user != null) {
							
				// Start the transaction
	    		em.getTransaction().begin();
				
	    		// Updates the user balance
	    		user.setBalance(user.getBalance().add(depositAmount));
				
				// Commit the transaction
	    		em.getTransaction().commit();
	    		
			} else {
				
				// return if user does not exists
				return "Wrong SSN";
			}
			
			// return if successful
			String confirmation = depositAmount + "$ was deposited successfully";
			return confirmation;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// return if failed to delete the User
    	// executes if the try with resources block fails
		return "Error";
		
	}
	
	// Transfer money when the receiver is another User	
	public String transferMoneyToAnotherUser(int SSN, String password, int receiverSSN, BigDecimal transferAmount) {
		
		// instantiating User object with a null value
		User user;
		User receiver;
		
		// Try with resources block to transfer the amount
		try (
	    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("PostgresPU");
	    	EntityManager em = emf.createEntityManager();
	    	) {
			
			// Search the DB for the specified SSN
			user = em.find(User.class, SSN);
			
			// Check if the balance is greater or equal to the transferAmount
			if (user.getBalance().compareTo(transferAmount) >= 0) {
				
				// Search the DB for the receiver SSN only if the balance is sufficient
				receiver = em.find(User.class, receiverSSN);
				
			} else {
				return "Insufficient Balance";
			}
			
			// Check if the user exists
			if (user != null && receiver != null ) {
							
				// Start the transaction
	    		em.getTransaction().begin();
				
	    		// Updates the user balance
	    		user.setBalance(user.getBalance().subtract(transferAmount));
	    		receiver.setBalance(user.getBalance().add(transferAmount));
				
				// Commit the transaction
	    		em.getTransaction().commit();
	    		
			} else {
				
				// return if user does not exists
				return "Could not found the destiny SSN";
			}
			
			// return if successful
			String confirmation = transferAmount + "$ Transferred to SSN = " + receiverSSN;;
			return confirmation;
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Error";
	}
	
	// Transfer money when the receiver is a Store
	public String transferMoneyToStore(int SSN, String password, int receiverEIN, BigDecimal transferAmount) {
		
		// instantiating User object with a null value
		User user;
		
		// instantiating the Store object with a null value
		Store receiver;
		
		// Try with resources block to transfer the amount
		try (
	    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("PostgresPU");
	    	EntityManager em = emf.createEntityManager();
	    	) {
			
			// Search the DB for the specified SSN
			user = em.find(User.class, SSN);
			
			// Check if the balance is greater or equal to the transferAmount
			if (user.getBalance().compareTo(transferAmount) >= 0) {
				
				// Search the DB for the receiver SSN only if the balance is sufficient
				receiver = em.find(Store.class, receiverEIN);
				
			} else {
				return "Insufficient Balance";
			}
			
			// Check if the user exists
			if (user != null && receiver != null ) {
							
				// Start the transaction
	    		em.getTransaction().begin();
				
	    		// Updates the user and store balance
	    		user.setBalance(user.getBalance().subtract(transferAmount));
	    		receiver.setBalance(receiver.getBalance().add(transferAmount));
				
				// Commit the transaction
	    		em.getTransaction().commit();
	    		
			} else {
				
				// return if store does not exists
				return "Could not found the destiny EIN";
			}
			
			// return if successful
			String confirmation = transferAmount + "$ Transferred to EIN = " + receiverEIN;;
			return confirmation;
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Error";
	}
	
	public boolean emailExists(String email) {
		
		// Try with resources block to check if email exists in db
		try (
	    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("PostgresPU");
	    	EntityManager em = emf.createEntityManager();
	    	) {
			
			// if the email exists this will execute and return the email else will throw an exception
			em.createQuery("SELECT u.email FROM User u WHERE u.email = :email").setParameter("email", email).getSingleResult();
			
			// return true if the query is successful
			return true;
			
		} catch(Exception e) {}
		
		// return false if the try with resources fails
		return false;
	}

	//Getters and Setters
	
	public String getFullName() {
		return fName;
	}

	public int getSSN() {
		return SSN;
	}

	public String getEmail() {
		return email;
	}

	public BigDecimal getBalance() {
		return balance;
	}
	
	// private methods for local use only
	private String getPassword() {
		return password;
	}
	
	// setter for depositMoney use
	private void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	
	@Override
	public String toString() {
		return "User [SSN=" + SSN + ", fName=" + fName + ", email=" + email + ", balance=" + balance + ", password="
				+ password + "]";
	}
		
}