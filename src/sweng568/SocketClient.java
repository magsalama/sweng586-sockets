package sweng568;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;


/**
 * Simple client for the Registration Server
 */
public class SocketClient {
	private int port = 9090;
	private String serverAddress = "localhost";

	public SocketClient() throws Exception {
		sendSchemaInfo();
		sendStudentInfo();
		exitServer();
	}

	private void exitServer() throws Exception {
		// establish socket connection to server
		Socket socket = openSocket(serverAddress, port);

		ObjectOutputStream oos = null;

		// write to socket using ObjectOutputStream
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());

			// close server
			System.out.println("[Client] Sending Exit Signal to Socket Server");
			oos.writeObject("exit");
			oos.flush();

		} finally {
			// close resources
			oos.close();
			socket.close();
		}

	}

	private void sendSchemaInfo() throws Exception {
		// establish socket connection to server
		Socket socket = openSocket(serverAddress, port);
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		String message = null;

		// write to socket using ObjectOutputStream
		oos = new ObjectOutputStream(socket.getOutputStream());

		System.out.println("[Client] Sending Student Table Schema to Socket Server");
		oos.writeObject(getStudentTableSchema());
		// read the server response message
		ois = new ObjectInputStream(socket.getInputStream());

		message = (String) ois.readObject();
		System.out.println(message);

		// close resources
		ois.close();
		oos.close();
		socket.close();

	}

	
	/**
	 * Runs the client as an application.
	 * Basically I just call the constructor of class SocketClient()
	 * 
	 * 
	 */
	public static void main(String[] args) throws Exception {
		new SocketClient();
	}

	
	
	/**
	 * Sends the XML string that has the new student information first it will open
	 * a socket and will use ObjectOuptputSteam and ObjectInputStream to send data
	 * instead of sending bytes
	 * 
	 * @throws Exception
	 */
	public void sendStudentInfo() throws Exception {
		Socket socket = openSocket(serverAddress, port);
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		String message = null;
		// establish socket connection to server

		// write to socket using ObjectOutputStream
		oos = new ObjectOutputStream(socket.getOutputStream());
		System.out.println("[Client] Sending Student Information to Socket Server");
		oos.writeObject(getNewStudentsData());
		// read the server response message
		ois = new ObjectInputStream(socket.getInputStream());

		message = (String) ois.readObject();
		System.out.println(message);

		// close resources
		ois.close();
		oos.close();
		socket.close();

	}

	/**
	 * Open a socket connection to the given server on the given port. This method
	 * currently sets the socket timeout value to 10 seconds. (A second version of
	 * this method could allow the user to specify this timeout.)
	 * 
	 * @throws Exception
	 */
	private Socket openSocket(String server, int port) throws Exception {
		Socket socket;

		// create a socket with a timeout
		try {
			InetAddress inteAddress = InetAddress.getByName(server);
			SocketAddress socketAddress = new InetSocketAddress(inteAddress, port);

			// create a socket
			socket = new Socket();

			// this method will block no more than timeout ms.
			int timeoutInMs = 10 * 1000; // 10 seconds
			socket.connect(socketAddress, timeoutInMs);

			return socket;
		} catch (SocketTimeoutException ste) {
			System.err.println("Timed out waiting for the socket.");
			ste.printStackTrace();
			throw ste;
		}
	}

	/**
	 * This method will return a string containing the schema for table studentinfo
	 * This contains the data type for each column that could be used to validate 
	 * student information that will be passed to registration server
	 * or could be used to create an .XSD file.
	 */
	public String getStudentTableSchema() {
		/*
		 */
		StringBuilder xmlBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xmlBuilder.append("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");
		xmlBuilder.append( "<xs:element name=\"studentinfo\">\n");
		xmlBuilder.append(  "<xs:complexType>\n");
		xmlBuilder.append(   "<xs:sequence>\n");
		xmlBuilder.append(     "<xs:element name=\"StudID\" type=\"xs:positiveInteger\"/>\n");
		xmlBuilder.append(     "<xs:element name=\"Name\" type=\"xs:string\"/>\n");
		xmlBuilder.append(     "<xs:element name=\"SSN\" type=\"xs:string\"/>\n");
		xmlBuilder.append(     "<xs:element name=\"EmailAddress\" type=\"xs:string\"/>\n");
		xmlBuilder.append(     "<xs:element name=\"HomePhone\" type=\"xs:string\"/>\n");
		xmlBuilder.append(     "<xs:element name=\"HomeAddr\" type=\"xs:string\"/>\n");
		xmlBuilder.append(     "<xs:element name=\"LocalAddr\" type=\"xs:string\"/>\n");
		xmlBuilder.append(     "<xs:element name=\"EmergencyContact\" type=\"xs:string\"/>\n");
		xmlBuilder.append(     "<xs:element name=\"ProgramID\" type=\"xs:positiveInteger\"/>\n");
		xmlBuilder.append(     "<xs:element name=\"PaymentID\" type=\"xs:string\"/>\n");
		xmlBuilder.append(     "<xs:element name=\"AcademicStatus\" type=\"xs:positiveInteger\"/>\n");
		xmlBuilder.append(   "</xs:sequence>\n");
		xmlBuilder.append(  "</xs:complexType>\n");
		xmlBuilder.append( "</xs:element>\n");		
		xmlBuilder.append("</xs:schema>\n");
		return (xmlBuilder.toString());
	}

	
	
	/**
	 * This method should in real life use the database connection and get any newly
	 * added students and construct an XML string for all of them. for simplicity we
	 * have this method just return an XML string of the data of one student Bob
	 * Smith
	 */
	public String getNewStudentsData() {
		/*
		 */
		StringBuilder xmlBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xmlBuilder.append("<NewDataSet>\n");
		xmlBuilder.append(   "<studentinfo>\n");
		xmlBuilder.append(      "<StudID>1111 </StudID>\n");	  		
		xmlBuilder.append(      "<Name> Bob Smith </Name>\n"); 		 	
		xmlBuilder.append(      "<SSN>222-333-1111 </SSN>\n");		 		
		xmlBuilder.append(      "<EmailAddress> bsmith@yahoo.com </EmailAddress>\n"); 	
		xmlBuilder.append(      "<HomePhone>215-777-8888 </HomePhone>\n"); 		
		xmlBuilder.append(      "<HomeAddr>123 Tulip Road, Ambler, PA 19002 </HomeAddr>\n"); 		
		xmlBuilder.append(      "<LocalAddr>321 Maple Avenue, Lion Town, PA 16800 </LocalAddr>\n"); 		
		xmlBuilder.append(      "<EmergencyContact>John Smith (215-222-6666) </EmergencyContact>\n");  
		xmlBuilder.append(      "<ProgramID>206 </ProgramID>\n"); 		 
		xmlBuilder.append(      "<PaymentID>1111-206 </PaymentID>\n"); 
		xmlBuilder.append(      "<AcademicStatus>1 </AcademicStatus>\n"); 	 
		xmlBuilder.append(   "</studentinfo>\n");        
		xmlBuilder.append("</NewDataSet>\n");			  		

		return (xmlBuilder.toString());
	}

}
