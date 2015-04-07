// JDBC Example - printing a database's metadata
// Coded by Chen Li/Kirill Petrov Winter, 2005
// Slightly revised for ICS185 Spring 2005, by Norman Jacobson


import java.sql.*;                              // Enable SQL processing
import java.util.Scanner;

public class JDBC1
{

	static Connection connection = null;

	public static void main(String[] arg) throws Exception
	{

		// Incorporate mySQL driver
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		// Connect to the test database

		while(true){
			while(connection == null){
				//connection = login();
				connection = quickLogin();
			}
			printMenu();
			menuInput();
		}


		// Create an execute an SQL statement to select all of table"Stars" records
		//       Statement select = connection.createStatement();
		//       ResultSet result = select.executeQuery("Select * from stars");
		//
		//       // Get metatdata from stars; print # of attributes in table
		//       System.out.println("The results of the query");
		//       ResultSetMetaData metadata = result.getMetaData();
		//       System.out.println("There are " + metadata.getColumnCount() + " columns");
		//
		//       // Print type of each attribute
		//       for (int i = 1; i <= metadata.getColumnCount(); i++)
		//               System.out.println("Type of column "+ i + " is " + metadata.getColumnTypeName(i));
		//
		//       // print table's contents, field by field
		//       while (result.next())
		//       {
		//                   System.out.println("Id = " + result.getInt(1));
		//                   System.out.println("Name = " + result.getString(2) + result.getString(3));
		//                   System.out.println("DOB = " + result.getString(4));
		//                   System.out.println("photoURL = " + result.getString(5));
		//                       System.out.println();
		//           }
	}

	public static Connection quickLogin(){
		try {
			return DriverManager.getConnection("jdbc:mysql:///moviedb","root", "robin");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Connection login(){
		Scanner s = new Scanner(System.in);

		System.out.println("User name: ");
		String user = s.nextLine();

		System.out.println("Password: ");
		String password = s.nextLine();

		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql:///moviedb",user, password);
			return connection;
		} catch (SQLException e) {
			System.out.println("Wrong user name or password.");
		}

		return null;
	}

	public static void printMenu(){
		System.out.println("1 - look up star");					//1 Choose a star, print all attributes of movies with this star
		System.out.println("2 - insert new star");				//2 Add a new star in
		System.out.println("3 - insert customer");				//3 Insert customer
		System.out.println("4 - delete customer");				//4 Delete customer
		System.out.println("5 - metadata");						//5 Provide metadata of database
		System.out.println("6 - SQL command");					//6 Run query
		System.out.println("7 - exit to login");				//7 Exit menu
		System.out.println("8 - exit program");					//8 Exit program
	}

	public static void menuInput(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Choose from the menu");
		int select = scanner.nextInt();

		switch(select){
		case 1:
			break;
		case 2:
			insertStar();
			break;
		case 3:
			break;
		case 4:
			deleteCustomer();
			break;
		case 5:
			metadata();
			break;
		case 6:
			sqlCommand();
			break;
		case 7:
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			login();
			break;
		case 8:
			System.exit(0);
			break;
		default:
			System.out.println("Not a valid choice.");
		}
	}

	public static void insertStar(){

		Scanner s = new Scanner(System.in);
		System.out.println("Enter star id(required): ");
		int id = s.nextInt();
		s.nextLine();

		System.out.println("Enter star name (required): ");

		String name = s.nextLine();
		String last = "";
		String first = "";

		String[] sArray = null;
		while(sArray == null || sArray.length == 0){
			sArray = name.split(" ");
			if(sArray.length == 1){
				last = sArray[0];
			}else{
				first = sArray[0];
				last = sArray[sArray.length-1];
				for(int i = 1; i < sArray.length-1; i++){
					first += " " + sArray[i];
				}
			}
		}

		System.out.println("Enter star date of birth (not required): ");
		String date ="";
		date = s.nextLine();
		System.out.println("Enter photo url (not required): ");
		String url ="";
		url = s.nextLine();

		//String statement = "INSERT INTO stars VALUES(788001, 'Meg', 'Ryan', '1961/11/19', 'http://movies.yahoo.com/shop?d=hc&id=1800017434&cf=pg&photoid=35058&intl=us')";
		String statement = "";
		if(url != "" && date != "")
			statement = "INSERT INTO stars VALUES(" + id + ",'" + first + "','" + last + "','" + date + "','" + url + "')";
		//		else if(url == "" && date != "")
		//			statement = "INSERT INTO stars VALUES(" + id + ",'" + first + "','" + last + "','" + date + "','')";
		//		else if(date == "" && url != "")
		//			statement = "INSERT INTO stars VALUES(" + id + ",'" + first + "','" + last + "','null','" + url + "')";
		//		else if(url == "" && date == "")
		//			statement = "INSERT INTO stars VALUES(" + id + ",'" + first + "','" + last + "','null','')";

		else if(url == "" && date != "")
			statement = "INSERT INTO stars VALUES(" + id + ",'" + first + "','" + last + "','" + date + "','')";
		else if(date == "" && url != "")
			statement = "INSERT INTO stars VALUES(" + id + ",'" + first + "','" + last + "','null','" + url + "')";
		else if(url == "" && date == "")
			statement = "INSERT INTO stars VALUES(" + id + ",'" + first + "','" + last + "','null','')";
		try {
			Statement update = connection.createStatement();
			int retID = update.executeUpdate(statement);
		} catch (SQLException e) {
			System.out.println("Bad inputs, please try again");
			insertStar();
		}
	}

	public static void metadata(){
		try {
			Statement select = connection.createStatement();

			ResultSet result = select.executeQuery("Select * from stars");
			// Get metatdata from stars; print # of attributes in table
			System.out.println("The results of the query");

			ResultSetMetaData metadata = result.getMetaData();
			System.out.println("There are " + metadata.getColumnCount() + " columns");

			// Print type of each attribute
			for (int i = 1; i <= metadata.getColumnCount(); i++)
				System.out.println("Type of column "+ i + " is " + metadata.getColumnTypeName(i));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteCustomer(){
		System.out.println("Enter customer id to delete: ");
		Scanner s = new Scanner(System.in);
		int id = s.nextInt();
		try {
			Statement update = connection.createStatement();
			int retID = update.executeUpdate("delete from customers where id = "+id);
			System.out.println("retID = " + retID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void sqlCommand(){
		System.out.println("Enter a valid SQL command: ");
		Scanner s = new Scanner(System.in);
		String statement = s.nextLine();
		String[] arr = statement.split(" ");
		String first = arr[0];
		//System.out.println(statement);

		try {
			if(first.equalsIgnoreCase("select")){
				Statement select = connection.createStatement();
				ResultSet result = select.executeQuery(statement);
				ResultSetMetaData metadata = result.getMetaData();
				for (int i = 1; i <= metadata.getColumnCount(); i++){
					System.out.println("Type of column "+ i + " is " + metadata.getColumnTypeName(i));
				}
				while (result.next())
				{
					for (int i = 1; i <= metadata.getColumnCount(); i++){
						System.out.println(result.getString(i));
					}
					System.out.println("");
				}
			}else{
				Statement update = connection.createStatement();
				int retID = update.executeUpdate(statement);
				System.out.println("retID = " + retID);
			}

		} catch (SQLException e) {
			System.out.println("bad command, try again.");
			sqlCommand();
		}
	}
}
