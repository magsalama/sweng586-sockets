package sweng568;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



/**
 * A TCP server that runs on port 9090. When a client connects, it
 * 
 * 
 * 
 */
public class SocketServer {

	public SocketServer() throws ClassNotFoundException, IOException {
		ServerSocket server = new ServerSocket(9090);
		boolean schemaMode = false;
		// keep listens indefinitely until receives 'exit' call or program terminates
		while (true) {
			System.out.println("[Server] Waiting for client request");

			// creating socket and waiting for client connection
			Socket socket = server.accept();

			// read from socket to ObjectInputStream object			
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			
			// convert ObjectInputStream object to String
			String dataReceived = (String) ois.readObject();
			
			//check if the string received represent the table schema or student information, or exit request 			
			try {
				
				// terminate the server if client sends exit request
				if (dataReceived.equalsIgnoreCase("exit")) {
					System.out.println("[Server] Exit Request Received: \n");
					System.out.println("-------------------------------");
					break;
				}
				
				if (dataReceived.contains("<xs:schema")) //received the table schema
				{
					System.out.println("[Server] Table Schema Received: \n");
					System.out.println("-------------------------------");
					schemaMode = true;
				} else //received students data
				{
					System.out.println("[Server] Student Info Received: \n");
					System.out.println("-------------------------------");
					schemaMode = false;
				}
				// create ObjectOutputStream object
//				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				// write object to Socket
				if (schemaMode) {
					oos.writeObject("[Server] XML Schema Received: \n" + dataReceived);
				} else {
					// parse the XML string and get the name of the student!
					String studentName = getStudentName(dataReceived);
					oos.writeObject("[Server] Parsing XML Data\nData Received for student: " + studentName
							+ "\nFull Data Record Received:\n" + dataReceived);
				}
				oos.flush();

			} finally {
				// close resources
				ois.close();
				oos.close();
				socket.close();			}
		}
		System.out.println("Shutting down Socket server!!");
		// close the ServerSocket object
		server.close();
	}

	/**
	 * Runs the server.
	 * 
	 * @throws ClassNotFoundException
	 */

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		new SocketServer();

	}
	

	
	String getStudentName(String xmlRecords)
	{
		String StudentName = "";
		
		DocumentBuilder db = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    InputSource is = new InputSource();
	    is.setCharacterStream(new StringReader(xmlRecords));

	    Document doc = null;
		try {
			doc = db.parse(is);
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		NodeList nodes = doc.getElementsByTagName("studentinfo");

	    for (int i = 0; i < nodes.getLength(); i++) {
	      Element element = (Element) nodes.item(i);

	      NodeList name = element.getElementsByTagName("Name");
	      Element line = (Element) name.item(0);
	      StudentName = getCharacterDataFromElement(line);
	      System.out.println("Name: " + getCharacterDataFromElement(line));

	    }
		
		
		return StudentName;
	}
	
	String getCharacterDataFromElement(Element e) {
		    Node child = e.getFirstChild();
		    if (child instanceof CharacterData) {
		      CharacterData cd = (CharacterData) child;
		      return cd.getData();
		    }
		    return "";
		  }
}
