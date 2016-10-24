package Administrative;

import java.sql.ResultSet;
import java.sql.SQLException;

import system.jdbc_connectors;

public class As {
	
	public boolean ALLOWTOUSE; 
	public int getId ; 
	private ResultSet set; 
	

	
	public As(String Agent_ID , jdbc_connectors connectors)
	{
		
		
		ALLOWTOUSE = false ; 
		set = null ; 
		
		try {
			set = connectors.SelectField("Employees", "EmployeeId" , Agent_ID+"") ;
			
			if(set.next())
			{
				ALLOWTOUSE = true ; 
				getId = set.getInt("EmployeeId"); 
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		
		
		
		
	}
}
