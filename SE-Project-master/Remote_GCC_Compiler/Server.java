import javax.swing.*;
//import javax.tools.*;
import java.io.*;
import java.awt.event.*;
//import java.awt.*;
import java.net.*;

   class ConnectionHandler implements Runnable{
     private Socket socket;
	private int i;
        public ConnectionHandler(Socket socket,int i)
        {   this.socket=socket;
	    this.i=i;
            Thread t = new Thread(this);
            t.start();
        }
        public void run()
        {
            try{
                DataOutputStream  dataoutputstream = new DataOutputStream(socket.getOutputStream());
                DataInputStream inputstream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                dataoutputstream.writeBytes("You are now connected\n");
             
                 String Message="";

                      	FileOutputStream  fout = new FileOutputStream("/home/Mohit/test"+i+".c");
                        while(!((Message=inputstream.readLine()).equals("FILE END")))
			{
			String Mess=Message+"\n";
			byte[] str=Mess.getBytes();
                        fout.write(str);
			}
                        fout.close();
                   	System.out.println("Hello\n");
                 
                      	FileOutputStream  fout1 = new FileOutputStream("/home/Mohit/input"+i);
                        while(!(Message=inputstream.readLine()).equals("INPUT END"))
			{
			String Mess1=Message+"\n";
			byte[] str1=Mess1.getBytes();
                        fout1.write(str1);
			}
                        fout1.close();
                   
               
              
                  
              
                 String[] psCmd =
                    {
                        "sh",
                        "-c",
                        "gcc -o /home/Mohit/test"+i+" /home/Mohit/test"+ i +".c"
                    };
                  String sout="",serr="",s="";

                  Process p = Runtime.getRuntime().exec(psCmd);

                  BufferedReader stdInput = new BufferedReader(new
                  InputStreamReader(p.getInputStream()));

                  BufferedReader stdError = new BufferedReader(new
                  InputStreamReader(p.getErrorStream()));

            // read the output from the command

                  while ((s= stdInput.readLine()) != null) {
                    sout =sout +s;
                  }

            // read any errors from the attempted command

                  while ((s = stdError.readLine()) != null) {
                    serr=serr +s;
                  }
                  if(!(serr.equals("")))
                  {
                        dataoutputstream.writeBytes("ERROR\n");
                        dataoutputstream.writeBytes(serr+"\n");
                        dataoutputstream.writeBytes("ERROR END\n");
                  }
                  else
                  {   String[] psCmd1 =
                    {
                        "sh",
                        "-c",
                        "/home/Mohit/test"+i+" < /home/Mohit/input"+i
                    };
                    sout="";serr="";s="";

                    Process p1 = Runtime.getRuntime().exec(psCmd1);

                    BufferedReader stdIn = new BufferedReader(new
                    InputStreamReader(p1.getInputStream()));

                    BufferedReader stdErr = new BufferedReader(new
                    InputStreamReader(p1.getErrorStream()));

            // read the output from the command

                    while ((s= stdIn.readLine()) != null) {
                      sout =sout +s+"\n";
                    }
		System.out.println(sout);

            // read any errors from the attempted command

                    while ((s = stdErr.readLine()) != null) {
                    serr=serr +s+"\n";
                    }
                    if(!serr.equals(""))
                     {
                        dataoutputstream.writeBytes("ERROR\n");
                         dataoutputstream.writeBytes(serr+"\n");
                         dataoutputstream.writeBytes("ERROR END\n");
                     }
                    else{
                       dataoutputstream.writeBytes("OUTPUT\n");
                        dataoutputstream.writeBytes(sout+"\n");
                        dataoutputstream.writeBytes("OUTPUT END\n");
                     }

                }
		socket.close();
            }
           
            catch (Exception e)
            {e.getStackTrace();
            }
        }
    };

public class Server {
        JButton b1,b2;int i=0;
        public void show(){
            final JFrame f =new JFrame("SERVER");
            f.setLayout(null);
            f.setTitle("SERVER");
            b1= new JButton();
            b2= new JButton();
            b1.setBounds(2, 5, 200, 70);
            b2.setBounds(204, 5, 200, 70);
                        b1.setText("Start Server");
            b2.setText("Stop Server");
            f.add(b1);
            f.add(b2);
            b1.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                 try
                 {      ServerSocket server = new ServerSocket(1436);
                        while(true)
                     {          try{
                                      Socket socket = server.accept();
                                      //JOptionPane.showMessageDialog(null, " You have received a connection");
                                        new ConnectionHandler(socket,i);i++;
                                    }
                                 catch(Exception e1)
                                    {    e1.getStackTrace();
                                     }
                      }
                  }
                catch(Exception e2)
                { e2.getStackTrace();
                }
            }
          });
            b2.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                   f.setVisible(false); f.dispose();
                 }
            });

            f.setVisible(true);
            f.setSize(425,105);



        }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run(){
                    Server ob= new Server();
                    ob.show();
            }
        });
        // TODO code application logic here
    }

}
