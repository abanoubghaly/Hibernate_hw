package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class App {
	static private SessionFactory factory;
	static private BufferedReader reader;

	public static void main(String[] args)  {
		
		factory = new Configuration()
				.configure("hibernate_hw.cfg.xml")
				.addAnnotatedClass(Employee.class)
				.buildSessionFactory();
				
				 reader = new BufferedReader(new InputStreamReader(System.in));		
				
				try
				{
					// loop condition
					boolean end = false;
					
					// Getting info from user
					while (!end) {
						
						System.out.println("Enter 0 to exit\r"
								+ "1 to add an employee\r"
								+ "2 to get employee's information\r"
								+ "3 to delete an employee\r"
								+ "4 to list all employees from a company");
						int operation = Integer.parseInt(reader.readLine());						

						if (operation == 0) {
							System.out.println("Exiting..");
							reader.close();
							factory.close();
							System.exit(0);	
						}
						if (operation == 1) {	
							addEmployee();
						} else if (operation == 2) {
							getEmployee();
						} else if (operation == 3) {
							deleteEmployee();
						} else if (operation == 4) {
							getCompanyEmployees();
						}
					}
					System.exit(0);
					
				} catch (Exception e) {
					System.out.println("Something went wrong");
					e.printStackTrace();
				} 
				finally
				{
					factory.close();
				}

	}
	
	static private void addEmployee() 
	{
		try {
			//getting input from user
			System.out.println("First name: ");
			String firstName = reader.readLine();
			System.out.println("Last name: ");
			String lastName = reader.readLine();
			System.out.println("Company: ");
			String company = reader.readLine();
			System.out.println("Salary: ");
			float salary = Float.parseFloat((reader.readLine()));
			
			System.out.println("Join Date (mm/dd/yyyy): ");
			String dateString = reader.readLine();
			Date joinDate = new SimpleDateFormat("mm/dd/yyyy").parse(dateString);
						
			// Saving to the database
			Session session = factory.getCurrentSession();
			Employee emp1 = new Employee(firstName, lastName, 
					company, salary, joinDate);
			session.beginTransaction();	
			session.save(emp1);		
			System.out.println(emp1);	
			session.getTransaction().commit();
			session.close();
			
			System.out.println("\r" + firstName +  "'s information is saved to the databse.\r"
					+ "Type 1 to add another employee or any other value to exit:");
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			e.printStackTrace();
		}
	}
	
	static private void getEmployee() 
	{
		try {
			//reading id 
			System.out.println("Employee's id: ");
			int id = Integer.parseInt(reader.readLine());
			System.out.println(id);
			//resetting reader
			
			// retrieving info
			Session session = factory.getCurrentSession();
			session.beginTransaction();	
			Employee emp = session.get(Employee.class, id);	
			System.out.println(emp);	
			session.getTransaction().commit();
			session.close();
		}
		catch (Exception e) {
			System.out.println("Something went wrong!");
			e.printStackTrace();
		}
	}
	
	static private void deleteEmployee()
	{
		try {
			System.out.println("Employee's id: ");
			int id = Integer.parseInt(reader.readLine());
			
			// deleting from db
			Session session = factory.getCurrentSession();
			session.beginTransaction();	
			Employee emp = session.get(Employee.class, id);
			session.delete(emp);
			System.out.println(emp.getLastName() + " is deleted.");	
			session.getTransaction().commit();
			session.close();
			System.out.println(emp);		
		}
		catch (Exception e) {
			System.out.println("Something went wrong!");
			e.printStackTrace();
		}
	}
	
	static private void getCompanyEmployees()
	{
		try {
			System.out.println("Enter a company name:");
			String company = reader.readLine();
			
			Session session = factory.getCurrentSession();
			session.beginTransaction();	
			List<Employee> employees = session.
					createQuery("from Employee e where e.company='" + company + "'").
					getResultList();
			session.getTransaction().commit();
			session.close();
			
			//printing employees
			for (Employee employee: employees) {
				System.out.println(employee);
			}
			
		} catch (Exception e) {
			System.out.println("Something went wrong!");
			e.printStackTrace();
		}
		
		
	}
	
}
