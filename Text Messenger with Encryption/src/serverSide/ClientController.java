package serverSide;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientController extends Thread{
	
	Socket socketForClient;
	private String clientName;
	final DataInputStream is;
	final DataOutputStream os;
	boolean isLogIn;
	private int clientIndex;

	public ClientController(Socket socketForClient, String clientName, DataInputStream is, DataOutputStream os,int clientIndex) {
		this.socketForClient=socketForClient;
		this.clientName=clientName;
		this.is=is;
		this.os=os;
		this.isLogIn = true;
		this.clientIndex=clientIndex;
	}

	@Override
	public void run() {
		String receiveAll;
		
		while(true) {
			try {
				receiveAll = is.readUTF();
				
				
				//Logout............................................................................
				if(receiveAll.equals("logout")) {
					this.isLogIn = false;
					Server sr = new Server();
					
					
					
					
					for(ClientController ct: Server.ar)
				    {
				    	ct.os.writeUTF(" #!#remove@@@@"+clientName);
				    }
					//
					int i=0;
					
					
					for(ClientController ct: Server.ar) {
						if(ct.clientName.equals(clientName))
						{	
							sr.ar.remove(i);
							sr.userList.remove(i);
							break;
						}
						else i++;
					}
					
					//
					
					
					os.close();
					is.close();
					this.socketForClient.close();
					
					//System.out.println("log out successfully...");	
					
				    Thread stopCurrentThread = currentThread();
				    stopCurrentThread.stop();
				    
						
				}
				//....logout..end........................................................................
				
				//....Sending messege to targeted user..............................................
				else {
				
						StringTokenizer sti = new StringTokenizer(receiveAll, "#!#");  // break string two part... 1)Message 2)Receiver
						
						String msgToSend = sti.nextToken();
						String msgReceiver = sti.nextToken();
						
						//Searching receiver............
						for(ClientController ct: Server.ar) { 
							if(ct.clientName.equals(msgReceiver) && (ct.isLogIn==true)) {
								String str[] = clientName.split("               ");
								ct.os.writeUTF(str[0]+"::"+msgToSend+"#!# ");
								break;
							}
						}
						
						//end............................
			
				}
				//...message sending...end................................................................................
				
			}catch(IOException ex){
				  
				for(ClientController ct: Server.ar)
			    {
			    	try {
						ct.os.writeUTF(" #!#remove@@@@"+clientName);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
				
				int i=0;
				Server sr = new Server();
				
				for(ClientController ct: Server.ar) {
					if(ct.clientName.equals(clientName))
					{	
						sr.ar.remove(i);
						sr.userList.remove(i);
						break;
					}
					else i++;
				}
				
				System.out.println("problem occurrs in thread client");
				break;
			}
		}
		
		//closing stream..............................................................
		try {
			os.close();
			is.close();
			this.socketForClient.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//closing stream..............................................................
		
	}

}
