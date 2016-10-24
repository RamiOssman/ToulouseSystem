package system;

import java.sql.*;
import java.util.ArrayList;

import resources.R;

public class jdbc_connectors {
	Connection con;

	private Driver driver;
	private String protocole;
	private String host;
	private String port;
	private String dataBaseName;
	private String ConnectionUrl;
	private String UserName;
	private String UserPass;
	public boolean connectionIsReady;
	public String EXP = "";

	public jdbc_connectors(String defaultDB) {

		try {

			Class c = null;
			try {
				c = Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block

				System.out.println("CLASS NOT FOUND EXCEPTION ");
				connectionIsReady = false;
				e.printStackTrace();
				EXP = "CLASS NOT FOUND EXCEPTION";

				e.printStackTrace();
			}
			try {
				driver = (Driver) c.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DriverManager.registerDriver(driver);

			protocole = R.MYSQL_PROTOCOLENAME;

			host = R.MYSQL_HOSTNAME;
			port = R.MYSQL_PORTNUMBER;

			dataBaseName = defaultDB;

			ConnectionUrl = protocole + "//" + host + ":" + port + "/" + dataBaseName+"?useUnicode=yes&characterEncoding=UTF-8";

			UserName = R.MYSQL_USERNAME;
			UserPass = R.MYSQP_USERPASS;

			con = DriverManager.getConnection(ConnectionUrl, UserName, UserPass);
			connectionIsReady = true;

			//
		} catch (SQLException se) {

			System.out.println("SQL exception");
			connectionIsReady = false;
			se.printStackTrace();

		}

	}

	public void createTable(table table) {
		if (!connectionIsReady)
			return;

		String Query = "CREATE TABLE `" + dataBaseName + "`.`" + table.TableName + "` (";
		int PrimaryKey = 999999;
		for (int x = 0; x < table.TableLenght; x++) {

			String isNull = table.columns[x].Null ? "NULL" : " NOT NULL ";
			String isAI = table.columns[x].AI ? "AUTO_INCREMENT" : " ";
			String DEFAULTF0 = table.columns[x].AI ? "" : " DEFAULT '" + table.columns[x].Default + "' ";
			String DEFAULTF = table.columns[x].FieldType == "VARCHAR" ? DEFAULTF0 : " ";

			Query += "`" + table.columns[x].FieldName + "` " + table.columns[x].FieldType + "("
					+ table.columns[x].FieldLenght + ") " + table.columns[x].CharType + table.columns[x].Collation + " "
					+ isNull + DEFAULTF + isAI + " COMMENT '" + table.columns[x].Comments + "'";
			if (table.columns[x].AI)
				PrimaryKey = x;
			if (x != table.TableLenght - 1)
				Query += " , ";
			else {
				// ADD PRIMARY KEY (`ca`)
			}

		}
		if (PrimaryKey != 999999)
			Query += " , PRIMARY KEY (`" + table.columns[PrimaryKey].FieldName + "`)";
		Query += " ) ENGINE = InnoDB;";

		try {
			createStatement().executeUpdate(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean insertData(String TableName, String[] where, String[] Values) {

		if (!connectionIsReady)
			return false;

		if (where.length != Values.length)
			return false;

		String S1 = "", S2 = "";

		for (int x = 0; x < where.length; x++) {
			S1 += where[x];
			S2 += "'" + Values[x] + "'";
			if (x != where.length - 1) {
				S1 += " , ";
				S2 += " , ";
			}
		}

		String SQLQUERY = "INSERT INTO " + TableName + " (" + S1 + ") VALUES (" + S2 + ")";
		System.out.println(SQLQUERY);
		try {
			createStatement().executeUpdate(SQLQUERY);

		} catch (SQLException x) {

			x.printStackTrace();

			return false;
		}

		return true;

	}

	public boolean insertMultipleData(String TableName, String[] where, String[] Values) {

		ArrayList<String> f = new ArrayList<String>();
		int at = 0;

		if (Values.length > 36) {
			for (int x = 0; x < Values.length; x++) {

				if (x > 35)
					if (x % 36 == 0) {

						String[] VS = f.toArray(new String[0]);
						if (insertMultipleDataFunction(TableName, where, VS) == false)
							return false;
						f = new ArrayList<String>();
						at = 0;
					}
				if (x == Values.length - 1) {
					f.add(at, Values[x]);
					String[] VS = f.toArray(new String[0]);
					if (insertMultipleDataFunction(TableName, where, VS) == false) {
						return false;
					}
					return true;

				}

				f.add(at, Values[x]);
				at++;
			}

			return true;
		} else

			return insertMultipleDataFunction(TableName, where, Values);
	}
	public int[] insertRecord(String stmt) throws SQLException {
		
	ArrayList<Integer> res = new ArrayList<Integer>() ;  
	
		try (
	        
	        PreparedStatement statement = con.prepareStatement(stmt,Statement.RETURN_GENERATED_KEYS);
	    ) {



	        int affectedRows = statement.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating user failed, no rows affected.");
	        }

	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                 res.add((int) generatedKeys.getLong(1)); 
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
	    }
		return conversion.convertIntegers(res) ; 
	}

	public boolean insertMultipleDataFunction(String TableName, String[] where, String[] Values) {

		if (!connectionIsReady)
			return false;

		String S1 = "";

		for (int x = 0; x < where.length; x++) {
			S1 += where[x];
			if (x != where.length - 1) {
				S1 += " , ";
			}
		}

		if (Values.length % where.length != 0)
			return false;

		String SQLQUERY = "INSERT INTO " + TableName + " (" + S1 + ") VALUES ";
		for (int l = 0; l < Values.length / where.length; l++) {
			int AT = l * 3;
			int LNG = where.length;
			String St = "";

			for (int m = 0; m < LNG; m++) {
				int B = AT + m;

				St += "'" + Values[B] + "'";

				if (m != LNG - 1)
					St += ",";

			}
			SQLQUERY += "(" + St + ")";

			if (l != Values.length / where.length - 1)
				SQLQUERY += ",";
		}
		System.out.println(SQLQUERY);
		try {
			createStatement().executeUpdate(SQLQUERY);

		} catch (SQLException x) {

			x.printStackTrace();

			return false;
		}

		return true;

	}

	public boolean createDB(String DBName, String CharSet, String Collate) {

		if (!connectionIsReady)
			return false;

		if (DBName == null || DBName == "")
			return false;

		if (modifyDB(DBName))
			dropDB(DBName);

		try {

			Statement stmt = this.createStatement();

			String sql = "CREATE DATABASE " + DBName + "  CHARACTER SET " + CharSet + "  COLLATE " + Collate;

			stmt.executeUpdate(sql);
			modifyDB(DBName);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public boolean dropDB(String DBName) {

		if (!connectionIsReady)
			return false;

		String DBNow = dataBaseName;

		if (modifyDB(DBName))
			try {
				createStatement().executeUpdate("DROP DATABASE " + DBName);
				modifyDB(DBNow);
			} catch (SQLException ex) {
				ex.getStackTrace();
			}
		else
			return false;
		return true;

	}

	public boolean modifyDB(String newDBName) {
		if (!connectionIsReady)
			return false;

		String newConnectionUrl = protocole + "//" + host + ":" + port + "/" + newDBName;
		try {
			con = DriverManager.getConnection(newConnectionUrl, UserName, UserPass);

			dataBaseName = newDBName;
		} catch (SQLException x) {
			x.printStackTrace();
			return false;

		}
		return true;

	}

	public Statement createStatement() {

		if (!connectionIsReady)
			return null;

		try {
			return this.con.createStatement();
		} catch (SQLException x) {

			x.printStackTrace();

		}

		return null;

	}

	public boolean dropTable(String TableName) {

		if (!connectionIsReady)
			return false;

		String sql = "DROP TABLE IF EXISTS " + TableName + " CASCADE;";
		try {
			createStatement().executeUpdate(sql);
		} catch (SQLException xe) {
			xe.printStackTrace();
			return false;
		}
		return true;

	}

	public boolean dropTable(table table) {

		if (!connectionIsReady)
			return false;

		String sql = "DROP TABLE IF EXISTS " + table.TableName + " CASCADE;";
		try {
			createStatement().executeUpdate(sql);
		} catch (SQLException xe) {
			xe.printStackTrace();
			return false;
		}
		return true;

	}

	public boolean addColumnTo(Column clmn, table table) {
		if (!connectionIsReady)
			return false;

		String isNull = clmn.Null ? "NULL" : " NOT NULL ";
		String isAI = clmn.AI ? "AUTO_INCREMENT" : " ";
		String DEFAULTF0 = clmn.AI ? "" : " DEFAULT '" + clmn.Default + "' ";
		String DEFAULTF = clmn.FieldType == "VARCHAR" ? DEFAULTF0 : " ";

		String Query = "IF (EXISTS (ALTER TABLE `" + table.TableName + "` ADD `" + clmn.FieldName + "` "
				+ clmn.FieldType + "(" + clmn.FieldLenght + ") " + clmn.CharType + clmn.Collation + " " + isNull
				+ DEFAULTF + isAI + " COMMENT '" + clmn.Comments + "'))";
		try {
			createStatement().executeUpdate(Query);
		} catch (SQLException x) {
			x.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean addColumn(Column clmn, String TableName) {
		if (!connectionIsReady)
			return false;

		String isNull = clmn.Null ? "NULL" : " NOT NULL ";
		String isAI = clmn.AI ? "AUTO_INCREMENT" : " ";
		String DEFAULTF0 = clmn.AI ? "" : " DEFAULT '" + clmn.Default + "' ";
		String DEFAULTF = clmn.FieldType == "VARCHAR" ? DEFAULTF0 : " ";

		String Query = "ALTER TABLE `" + TableName + "` ADD `" + clmn.FieldName + "` " + clmn.FieldType + "("
				+ clmn.FieldLenght + ") " + clmn.CharType + clmn.Collation + " " + isNull + DEFAULTF + isAI
				+ " COMMENT '" + clmn.Comments + "'";

		try {
			createStatement().executeUpdate(Query);
		} catch (SQLException x) {
			x.printStackTrace();

			return false;
		}

		return true;
	}

	protected boolean dropColumn(Column clmn, String TableName) {
		// ALTER TABLE `tb5` DROP `cb`;

		return false;
	}

	public ResultSet SelectField(String table, String FieldCondition, String ConditionIs) throws SQLException {

		if (!connectionIsReady)
			return null;

		ResultSet set = createStatement()
				.executeQuery("SELECT * FROM " + table + " WHERE " + FieldCondition + " = '" + ConditionIs + "'");

		return set;

	}

	public ResultSet SelectField(String table, String[] conditions, String[] values) {
		// TODO Auto-generated method stub
		if (!connectionIsReady)
			return null;

		String query = "SELECT * FROM " + table + " WHERE ";

		for (int x = 0; x < conditions.length; x++) {
			query += conditions[x] + " = '" + values[x] + "'";

			if (x != conditions.length - 1)
				query += " and ";
		}

		try {
			return createStatement().executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}

class table {
	String TableName;
	int TableLenght;
	Column[] columns;
	int nbOfColumnsStored;

	public table(String TableNameR, int TableLenghtR) {
		TableName = TableNameR;
		TableLenght = TableLenghtR;
		columns = new Column[TableLenghtR];
		nbOfColumnsStored = 0;

	}

	public void newColumn(Column column, int Position) {
		if (nbOfColumnsStored > TableLenght)
			return;
		columns[Position - 1] = column;

		nbOfColumnsStored++;

	}

	public table excuteWhenReady() {
		if (nbOfColumnsStored != TableLenght)
			;
		TableLenght = nbOfColumnsStored;

		return this;

	}

}

class Column {

	String FieldName, FieldType, Default, Collation, CharType, Attributes, Comments;
	int FieldLenght;
	boolean Null, AI;

	public Column(String FieldNameR, String FieldTypeR, int FieldLenghtR, String DefaultR, String CharTypeR,
			String CollationR, String AttributeR, boolean NullR, boolean AIR, String CommentR) {
		// String initialing .
		if (FieldNameR == "" || FieldNameR == null || FieldTypeR == "" || FieldTypeR == null || FieldLenghtR < 1) {
			FieldNameR = "NoName";
			FieldTypeR = "VARCHAR";
			FieldLenghtR = 1;
		}
		if (true) {
			// by default : Attributes will be disabled if Attributes.isempty()
			// or !Attributes.isempty() in other word if true

		}

		FieldName = FieldNameR;
		FieldType = FieldTypeR;
		Default = DefaultR == null ? " " : DefaultR;
		CharType = CharTypeR == null || CharTypeR == "" ? " " : " CHARACTER SET " + CharTypeR;
		Collation = CollationR == null || CollationR == "" ? " " : " COLLATE " + CollationR;
		Attributes = AttributeR;
		Comments = CommentR == null ? "" : CommentR;

		// ints initialing .
		FieldLenght = FieldLenghtR;

		// boolean initialing .
		Null = NullR;
		AI = AIR;

	}

}
class conversion
{
	public static int[] convertIntegers(ArrayList<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
	    return ret;
	}
	
	

}
