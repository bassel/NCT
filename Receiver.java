/************************************
 * 		Author: Bassel Cheaib		*
 * 		Date:	May 2017			*
 ************************************/

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;


public class Receiver {

	public static boolean running = true;	//when started, the receiver will be running and waiting for commands
	public static String IP = "";			//it gets its IP on launch and saves it
	public static final int port = 7000;	//and works on port 7000, just like the sender
	public static final Scanner input = new Scanner(System.in);	//it takes input from user using scanner
	
	public static void main(String[] args){
		printHeader();	//The header of the program
		try {	//save the local IP 
			IP = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e){}
		
		System.out.println("Your IP is: "+IP);	//display it so that it becomes available for the sender
		
		while (running){
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				System.out.println("\nNCT >>> Waiting for commands...");
				Socket socket = serverSocket.accept();
				System.out.println("\nNCT >>> Connection received from "+socket.getRemoteSocketAddress());
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//read from socket
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);	//write to socket
				String s = br.readLine();
				execute(s, br, out);
				serverSocket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void execute(String s, BufferedReader br, PrintWriter out){
		if (s == null || s.equals(""))
			return;
		String[] toExecute = s.split(" ");
    	String command = toExecute[0].toLowerCase();
    	System.out.print("\nNCT >>> Requested command: ");
    	switch(command){
    	case "freezemouse":
    		System.out.println("Freeze mouse for " + toExecute[1]+" seconds");
    		freezeMouse(Integer.parseInt(toExecute[1]));
    		break;
    	case "crazymouse":
    		System.out.println("Crazy mouse for " + toExecute[1]+" seconds");
    		crazyMouse(Integer.parseInt(toExecute[1]));
    		break;
    	case "funnymessage":
    		System.out.println("Display surprise message ");
    		funnyMsg(toExecute[1]);
    		break;
    	case "shutdown":
    		System.out.println("Shutdown computer after " + toExecute[1]+" seconds");
    		shutdown(Integer.parseInt(toExecute[1]));
    		break;
    	case "quit":
    		quit();
    		break;
    	case "runmatrix":
    		System.out.println("Run matrix");
    		runMatrix();
    		break;
    	case "streamstarwars":
    		System.out.println("Stream Star Wars");
    		streamStarWars();
    		break;
    	case "sendmessage":
    		receiveMessage(toExecute[1]);
    		break;
    	case "chat":
    		try {
				chat(br, out);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		break;
    	case "getdeviceip":
    		System.out.println("Get this device IP");
    		getDeviceIP(out);
    		break;
    	default:
    		System.out.println("Command not found, please try again later...");
    		break;
    	}
	}
	
	public static void freezeMouse(int time){
		try {
			long to = System.currentTimeMillis() + time*1000;
			while (System.currentTimeMillis() < to){
				Robot robot = new Robot();
				robot.mouseMove(0 , 0);
			}
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
    }
	
	public static void crazyMouse(int time){
		try {
			Robot robot = new Robot();
			Random random = new Random();
			long to = System.currentTimeMillis() + time*1000;
			while (System.currentTimeMillis() < to){
				robot.mouseMove(random.nextInt(400), random.nextInt(400));
	            Thread.sleep(300);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void funnyMsg(String msg){
    	try {
    		msg = msg.replace('~', ' ');
    		Runtime rt = Runtime.getRuntime();
        	rt.exec("cmd /c start notepad");
        	Thread.sleep(1500);
        	Robot robot = new Robot();
        	for (int i = 0; i < msg.length(); i++) {
                char c = msg.charAt(i);
                if (Character.isUpperCase(c)) {
                    robot.keyPress(KeyEvent.VK_SHIFT);
                }
                robot.keyPress(Character.toUpperCase(c));
                robot.keyRelease(Character.toUpperCase(c));
                if (Character.isUpperCase(c)) {
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }
                robot.delay(150);
            }
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    public static void shutdown(int time){
    	Runtime rt = Runtime.getRuntime();
    	System.out.println("Your computer will shutdown in "+time+" seconds.");
    	try {
			rt.exec("cmd /c start cmd.exe /K \"c:\\Windows\\System32\\shutdown.exe\" /s /t "+time);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void quit(){
    	try {
    		System.out.print("\nProgram will quit in 3 ");
        	Thread.sleep(800);
        	System.out.print("2 ");
        	Thread.sleep(800);
        	System.out.print("1 ");
        	Thread.sleep(800);
        	System.out.print("0 ");
    	} catch (InterruptedException e) {}
    	running = false;
    }
    
    public static void runMatrix(){
    	Runtime rt = Runtime.getRuntime();
    	try {
			rt.exec("cmd /c start cmd.exe /K \"matrix.bat\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void streamStarWars(){
    	Runtime rt = Runtime.getRuntime();
    	try {
			rt.exec("cmd.exe /c start telnet Towel.blinkenlights.nl");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void receiveMessage(String msg){
    	System.out.println("\nNew message arrived!");
    	System.out.println("\tContent: "+msg.replace('~', ' '));
    }
    
    public static void chat(BufferedReader br, PrintWriter out) throws Exception{
    	boolean on = true;
    	System.out.println("\nChatting started! Type \"exit\" to exit the chat");
    	//on the receiving side, the sender has already sent the first message so we start by reading it
    	//then we reply to it
    	while (on){
    		String received = br.readLine();
    		System.out.println("him>>> "+received);
    		if (received.equalsIgnoreCase("bye!")){
    			//the sender has ended the chat
    			on = false;
    			break;
    		}
    		System.out.print("you>>> ");
    		String reply = input.nextLine();
    		if (reply.equalsIgnoreCase("exit")){
    			out.println("Bye!");
    			on = false;	//end the chat
    		}
    		out.println(reply);
    	}
    }
    
    public static void getDeviceIP(PrintWriter out){
    	out.println(IP);
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
		System.out.println("Please run the Sender on the sending side for the program to work...\n");
	}
}

