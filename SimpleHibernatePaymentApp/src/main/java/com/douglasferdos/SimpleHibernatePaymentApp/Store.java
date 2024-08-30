package com.douglasferdos.SimpleHibernatePaymentApp;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

// Table
@Entity
@Table(name = "Stores")
public class Store {

	// Tables columns
	@Id
	@Column(name = "EIN", nullable = false ,length = 9, columnDefinition = "INT")
	private int EIN;
	
	@Column(name = "fName", nullable = false, length = 255)
	private String fName;
	
	@Column(name = "email", nullable = false, length = 255, unique = true)
	private String email;

	@Column(name = "balance")
	private BigDecimal balance;
	
	@Column(name = "password", nullable = false, length = 64, columnDefinition = "TEXT")
	private String password;
	
	
	// Empty constructor to Hibernate create when fetching data from DB
	public Store() {}
	
	// Auxiliary constructor with parameters for Store
	public Store(int EIN, String fName, String email, BigDecimal balance, String password) {
		
		this.EIN = EIN;
		this.fName = fName;
		this.email = email;
		this.balance = balance;
		this.password = password;
		
	}
	
	
	// Create a Store (Insert a row in Stores Table)
	public String createStore(int EIN, String fName, String email, String password) {
		
		// instantiating Store object with a null value
		Store store;
		
		// BigDecimal Object with the default value of zero
		// used to set the account initial balance
		BigDecimal startBalance = new BigDecimal("0");
		
		
		// Try with resources block to create a Store
    	try (
    		EntityManagerFactory emf = Persistence.createEntityManagerFactory("PostgresPU");
    		EntityManager em = emf.createEntityManager();
    		) {

    		// Search the DB for the specified EIN
    		store = em.find(Store.class, EIN);
    		
    		// Create a store for the provided data if
    		// no store with the specified EIN is found
    		if (store == null) { 
    			
        		// if the store does not exists checks 
        		// for the email in the database
    			if (emailExists(email) == false){
    		    			
	    			// Overrides the store data with the provided data
	    			store = new Store(EIN, fName, email, startBalance, password);
	    			
		    		// Start the transaction
		    		em.getTransaction().begin();
		    		
		    		// Persist the transaction
		    		em.persist(store);
		    		
		    		// Commit the transaction
		    		em.getTransaction().commit();
	
    			} else {
    				return "Specified email already has an account";
    			}
    		} else {
    			
    			// return if failed: account with this EIN already exists
    			return "Specified EIN already has an account";
    		}
    		
    		// return if successful
    		return "Store account sucessfully created";
    		
		} catch (Exception e) {
			
			// prints the stack trace error to the console
			e.printStackTrace();
		}
		
		// return if failed to create the account
    	// executes if the try with resources block fails
		return "Error";
	}
	
	// Deletes a Store account
	public String deleteStore(int EIN, String password) {
		
		// instantiating Store object with a null value
		Store store;
		
		// Try with resources block to delete the specified Store
		try (
	    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("PostgresPU");
	    	EntityManager em = emf.createEntityManager();
	    	) {
			
			// Search the DB for the specified EIN 
			store = em.find(Store.class, EIN);
			
			// Check if not null first, else the get methods will fail
			// Delete the store if the EIN has been found
			if (store != null && store.getEIN() == EIN && store.getPassword().equals(password)) {
				
				// Start the transaction
	    		em.getTransaction().begin();
	    		
	    		// Delete the store
	    		em.remove(store);
	    		
	    		// Commit the transaction
	    		em.getTransaction().commit();
	    		
			} else {
    			
    			// return if failed
    			return "EIN or Password incorrect";
    		}
			
			// return if successful
			return "Store deleted";
			
		} catch (Exception e) {
			
			// prints the stack trace error to the console
			e.printStackTrace();
		}
		
		// return if failed to delete the Store
    	// executes if the try with resources block fails
		return "Error";
	}

	public boolean emailExists(String email) {
		
		// Try with resources block to check if email exists in db
		try (
	    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("PostgresPU");
	    	EntityManager em = emf.createEntityManager();
	    	) {
			
			// if the email exists this will execute and return the email else will throw an exception
			em.createQuery("SELECT s.email FROM Store s WHERE s.email = :email").setParameter("email", email).getSingleResult();
			
			// return true if the query is successful
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// return false if the try with resources fails
		return false;
	}
	
	// Getters and Setters
	
	public int getEIN() {
		return EIN;
	}

	public String getfName() {
		return fName;
	}

	public String getEmail() {
		return email;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public String getPassword() {
		return password;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	// toString method of all the store info except the password
	@Override
	public String toString() {
		return "Store [EIN=" + EIN + ", fName=" + fName + ", email=" + email + ", balance=" + balance + "]";
	}
	
	
	
}
