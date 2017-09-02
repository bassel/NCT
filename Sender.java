/********************************
 * 	Author: Bassel Cheaib	*
 * 	Date:	May 2017	*
 ********************************/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;


public class Sender {

	public static boolean running = true;		//when started, the sender will be running
	public static boolean connected = false;	//but will not be connected
	public static String ReceiverIP = "";		//It asks the user to enter the receiver's IP address
	public static final int port = 7000;		//and works on a certain fixed port (7000 in our case)
	public static final Scanner inputLine = new Scanner(System.in);	//It takes a line input from the user using the Scanner
	public static final Scanner input = new Scanner(System.in);	//It takes input from the user using the Scanner

	public static void main(String[] args) throws Exception{
		printHeader();	//The header of the program
		while (running){
			//we first ask the user to enter the receiver's IP in order to be able to establish the connection
			System.out.print("Enter the other computer's IP to establish the connection (or q to quit): ");
			String ip = input.next();
			if (ip.equalsIgnoreCase("q")){
				running = false;	//exit the program
				continue;	//skip the rest of the loop
			} else if (isValidIP(ip)){
				//the IP format entered is valid, now we check if we can connect to it before we save it
				if (canConnect(ip)){
					ReceiverIP = ip;	//the IP is totally valid
					connected = true;	//now we are ready to establish the connection
				} else {
					System.out.println("Couldn't connect to the entered IP. Please try again...");
					continue;	//skip the rest of the loop
				}
			} else {
				System.out.println("Wrong IP format, please try again...");
				continue;	//skip the rest of the loop
			}
			
			printMenu();	//prints a list of the available commands
			
			//this loop won't be entered unless we provide a valid IP
			//then we keep repeating it until we exit or reset the connection
			while (connected){
				System.out.print("\nNCT >>> ");
				String s = input.next();
				int choice = 0;
				if (s.equals("")){
					continue;	//the user pressed enter without typing anything, we do nothing
				} else {
					try {
						choice = Integer.parseInt(s);
						//if the choice is invalid, retry
						if (choice < 0 || choice > 11){
							System.out.println(" !-! Please enter a valid command number !-!");
							continue;
						}
					} catch (Exception e){
						//if the choice isn't an integer, retry
						System.out.println(" !-! Please enter a valid command number !-!");
					}
				}
				try {
					execute(choiceToString(choice));
				} catch (Exception e){}
			}
		}
	}
	
	public static void printMenu(){
		System.out.println("\nPlease enter the number of a command to execute it:");
		System.out.println("    0. Print the list of available commands");
		System.out.println("    1. Send a Message (Receiver can't reply)");
		System.out.println("    2. Funny Message (notepad will pop up and message will be typed on it)");
		System.out.println("    3. Chat with Receiver (one message at a time, should wait for reply after sending the message)");
		System.out.println("    4. Freeze Mouse");
		System.out.println("    5. Crazy Jumping Mouse");
		System.out.println("    6. Get Receiver's IP");
		System.out.println("    7. Run the matrix");
		System.out.println("    8. Stream Star Wars");
		System.out.println("    9. Shutdown Receiver's Computer");
		System.out.println("    10. Reset Connection");
		System.out.println("    11. Quit");
	}
	
	public static boolean canConnect(String ip){
		try {
	    	Socket socket = new Socket();	//create the socket
	    	socket.connect(new InetSocketAddress(ip, port), 1000);	//timeout is 1s
	    	boolean result = socket.isConnected();
	    	socket.close();
	    	return result;
		} catch (Exception e){
			return false;	//connection timed out, can't connect to given ip
		}
	}
	
	public static String choiceToString(int choice){
		switch (choice){
		case 0:
			return "printMenu";
		case 1:
			return "sendMessage";
		case 2:
			return "funnyMessage";
		case 3:
			return "chat";
		case 4:
			return "freezeMouse";
		case 5:
			return "crazyMouse";
		case 6:
			return "getDeviceIP";
		case 7:
			return "runMatrix";
		case 8:
			return "streamStarWars";
		case 9:
			return "shutdown";
		case 10:
			return "resetConnection";
		case 11:
			return "quit";
		}
		return "";
	}
	
	public static void execute(String command) throws Exception {
		//resetConnection and printMenu are local commands so we don't need to create a socket to execute them
		//that's why we check them before creating the socket
		if (command.equalsIgnoreCase("resetconnection")){
			connected = false;
		} else if (command.equalsIgnoreCase("printmenu")){
			printMenu();
		} else {
			Socket socket = new Socket(ReceiverIP, port);	//create socket
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//read from socket
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);	//write to socket
	    	switch(command.toLowerCase()){
	    	case "freezemouse":
	    		System.out.print("Time to freeze mouse (in seconds): ");
	    		try {
	    			int t = input.nextInt();
	    			command += " "+t;
	        		out.println(command);
	        		System.out.println("Command successfully sent.");
	    		} catch (Exception e){
	    			System.out.println("Time must be an integer. Command not sent.");
	    		}
	    		break;
	    	case "crazymouse":
	    		System.out.print("Time to make mouse crazy (in seconds): ");
	    		try {
	    			int t = input.nextInt();
	    			command += " "+t;
	        		out.println(command);
	        		System.out.println("Command successfully sent.");
	    		} catch (Exception e){
	    			System.out.println("Time must be an integer. Command not sent.");
	    		}
	    		break;
	    	case "funnymessage":
	    		System.out.print("Your message: ");
	    		String msg = inputLine.nextLine();
	    		msg = msg.replace(' ', '~');   //when sending the msg, I want it to be a single string, so I replaced the space with a rarely used character
	    									   //then on the receiving side, I do the inverse ( '~' to space)
	    		command += " "+msg;
	    		out.println(command);
	    		System.out.println("Message successfully sent.");
	    		break;
	    	case "shutdown":
	    		System.out.print("Time to wait before shutdown (in seconds): ");
	    		try {
	    			int t = input.nextInt();
	    			command += " "+t;
	        		out.println(command);
	        		System.out.println("Command successfully sent.");
	    		} catch (Exception e){
	    			System.out.println("Time must be an integer. Command not sent.");
	    		}
	    		break;
	    	case "quit":
	    		out.println(command);
	    		System.out.println("Command successfully sent.");
	    		quit();
	    		break;
	    	case "runmatrix":
	    		out.println(command);
	    		System.out.println("Command successfully sent.");
	    		break;
	    	case "getdeviceip":
	    		out.println(command);
	    		System.out.println("The receiving device IP is: " + br.readLine());
	    		break;
	    	case "streamstarwars":
	    		out.println(command);
	    		System.out.println("Command successfully sent.");
	    		break;
	    	case "sendmessage":
	    		System.out.print("Your message: ");
	    		String message = inputLine.nextLine();
	    		message = message.replace(' ', '~');	//same as funny message
	    		command += " "+message;
	    		out.println(command);
	    		System.out.println("Message successfully sent.");
	    		break;
	    	case "chat":
	    		out.println(command);
	    		chat(br, out);
	    		break;
	    	default:
	    		System.out.println("Command not available, please try again later...");
	    		break;
	    	}
	    	socket.close();
		}
    }
	
	
	public static void chat(BufferedReader br, PrintWriter out) throws Exception{
    	boolean on = true;
    	System.out.println("\nChatting started! Type \"exit\" to exit the chat");
    	//on the sending side, the sender must send the first message to start the chat
    	//then we get the reply to it
    	while (on){
    		System.out.print("you>>> ");
    		String toSend = inputLine.nextLine();
    		if (toSend.equalsIgnoreCase("exit")){
    			out.println("Bye!");
    			on = false;	//end the chat
    			continue;
    		}
    		out.println(toSend);
    		String reply = br.readLine();
    		System.out.println("him>>> "+reply);
    		if (reply.equalsIgnoreCase("bye!")){
    			//the receiver has ended the chat
    			on = false;
    			break;
    		}
    	}
    }
	
	public static void quit() throws InterruptedException{
    	System.out.print("\nProgram will quit in 3 ");
    	Thread.sleep(800);
    	System.out.print("2 ");
    	Thread.sleep(800);
    	System.out.print("1 ");
    	Thread.sleep(800);
    	System.out.print("0 ");
    	connected = false;
    	running = false;
    }
	
	public static boolean isValidIP (String ip) {
        try {
        	//an IP cannot be null or empty
            if (ip == null || ip.isEmpty())
                return false;
            //split the IP on the "."
            String[] parts = ip.split("\\.");
            //we must get four parts, otherwise its not an ip
            if (parts.length != 4)
                return false;
            //each part in the ip is a number between 0 and 255, otherwise its not an ip
            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ( (i < 0) || (i > 255) )
                    return false;
            }
            //an ip must not end with a "."
            if ( ip.endsWith(".") )
                return false;
            //the string passed all the tests, its an IP
            return true;
        } catch (Exception e) {
            return false;
        }
    }

	public static void printHeader(){
		System.out.println();
		System.out.println("  ##########################################################################");
		System.out.println("  ##                                                                      ##");
		System.out.println("  ##    ######          ###      ################   ##################    ##");
		System.out.println("  ##    #######         ###     ###############            ###            ##");
		System.out.println("  ##    ###   ##        ###     ###                        ###            ##");
		System.out.println("  ##    ###    ##       ###     ###                        ###            ##");
		System.out.println("  ##    ###     ##      ###     ###                        ###            ##");
		System.out.println("  ##    ###      ##     ###     ###                        ###            ##");
		System.out.println("  ##    ###       ##    ###     ###                        ###            ##");
		System.out.println("  ##    ###        ##   ###     ###                        ###            ##");
		System.out.println("  ##    ###         ##  ###     ###                        ###            ##");
		System.out.println("  ##    ###          ######     #################          ###            ##");
		System.out.println("  ##    ###           #####      #################         ###            ##");
		System.out.println("  ##                                                                      ##");
		System.out.println("  ##########################################################################");
		System.out.println("\nThis is the \"NCT\" remote commands executer.");
		System.out.println("Please run the Server on the receiving side for the program to work...\n");
	}
}
