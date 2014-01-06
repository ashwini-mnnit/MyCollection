
import javax.swing.*;
import javax.tools.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

class ClientIDE implements ActionListener
{
	File output;
	Socket socket;
	int X,Y;
	DataInputStream inputstream;
	DataOutputStream dataoutputstream;

	String s1,fname,backup;
	boolean flag=true;
	JFrame f;
	JMenu file,edit,help,environment;

	JMenuItem it0,it1,it2,it3,it4,it8,it9,it10,it11;

	TextArea ta1,ta2,ta3;

	ImageIcon i1,i2,i3,i4,i8,i9,i10,i11,i12;

	JLabel l1,l2,l3,j1,j2,j3,j4,j5,j6,j7,j8,j9,j10,j11,j12;

	JButton b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12;

	FileDialog fd,fd1;

	InputStream sin;
	OutputStream sout;

	public void show()
	{				//Start of show function
		JFrame f=new JFrame("My Project");
		f.setLayout(null);
		f.setTitle("CLIENT SIDE INTERFACE FOR REMOTE GCC COMPILER");
		JMenuBar mbar=new JMenuBar();				//menubar

		i1=new ImageIcon("Open24.gif");
		i2=new ImageIcon("Save24.gif");
		i3=new ImageIcon("SaveAs24.gif");
		i4=new ImageIcon("Stop24.gif");
		i8=new ImageIcon("About24.gif");
		i9=new ImageIcon("Compile24.gif");
		i10=new ImageIcon("Run24.gif");
		i11=new ImageIcon("Undo24.gif");
		i12=new ImageIcon("New24.gif");

		JMenu file=new JMenu("   File    ");
		file.setMnemonic(KeyEvent.VK_F);
		file.add(it1=new JMenuItem(" New..",i12));
		it1.addActionListener(this);
		file.add(it0=new JMenuItem(" Open..",i1));
		it0.addActionListener(this);
		file.add(it2=new JMenuItem("Save",i2));
		it2.addActionListener(this);
		file.add(it3=new JMenuItem("Save As",i3));
		it3.addActionListener(this);
		file.add(it4=new JMenuItem("Exit",i4));
		it4.addActionListener(this);
		mbar.add(file);

		JMenu environment=new JMenu(" Environment  ");
		environment.setMnemonic(KeyEvent.VK_E);
		environment.add(it9=new JMenuItem(" Connect",i9));
		it9.addActionListener(this);
		environment.add(it10=new JMenuItem("Submitcode ",i10));
		it10.addActionListener(this);
		 environment.add(it11=new JMenuItem("SendInput",i11));
                it11.addActionListener(this);

		mbar.add(environment);

		JMenu help=new JMenu("   Help   ");
		help.setMnemonic(KeyEvent.VK_H);
		help.add(it8=new JMenuItem("About",i8));
		it8.addActionListener(this);
		mbar.add(help);
		mbar.setBounds(0,0,1410,25);
		f.add(mbar);


		b1=new JButton(i1);
		b1.addActionListener(this);
		b2=new JButton(i2);
		b2.addActionListener(this);
		b3=new JButton(i3);
		b3.addActionListener(this);
		 b4=new JButton(i4);
                b4.addActionListener(this);

		b8=new JButton(i8);
		b8.addActionListener(this);
		b9=new JButton(i9);
		b9.addActionListener(this);
		b10=new JButton(i10);
		b10.addActionListener(this);
		b11=new JButton(i11);
		b11.addActionListener(this);
		b12=new JButton(i12);
		b12.addActionListener(this);

		b12.setBounds(2,27,50,30);
		j12=new JLabel("New");
		j12.setBounds(2,50,50,30);
		f.add(j12);

		b1.setBounds(57,27,50,30);
		j1=new JLabel("Open");
		j1.setBounds(57,50,50,30);
		f.add(j1);

		b2.setBounds(112,27,50,30);
		j2=new JLabel("Save");
		j2.setBounds(112,50,50,30);
		f.add(j2);

		b3.setBounds(167,27,50,30);
		j3=new JLabel("SaveAs");
		j3.setBounds(167,50,60,30);
		f.add(j3);

		b4.setBounds(222,27,50,30);
		j4=new JLabel("Exit");
		j4.setBounds(222,50,50,30);
		f.add(j4);

		b8.setBounds(280,27,50,30);
		j8=new JLabel("About");
		j8.setBounds(280,50,50,30);
		f.add(j8);

		b9.setBounds(340,27,50,30);
		j9=new JLabel("Connect");
		j9.setBounds(340,50,60,30);
		f.add(j9);

		b10.setBounds(410,27,80,30);
		j10=new JLabel("SubmitCode");
		j10.setBounds(410,50,100,30);
		f.add(j10);

		b11.setBounds(510,27,80,30);
		j11=new JLabel("SubmitInput");
		j11.setBounds(510,50,100,30);
		f.add(j11);

		f.add(b12);
		f.add(b1);
		f.add(b2);
		f.add(b3);
		f.add(b4);
		f.add(b8);
		f.add(b9);
		f.add(b10);
		f.add(b11);

		l1=new JLabel("WRITE YOUR PROGRAM HERE");
		l1.setBounds(350,80,400,15);
		ta1=new TextArea("Browse a file or open a new file");
		ta1.setBounds(2,95,800,450);
		l2=new JLabel("YOUR OUTPUT IS GIVEN BELOW");
		l2.setBounds(400,560,400,15);
		ta2=new TextArea();
		ta2.setBounds(2,575,1410,200);
		l3=new JLabel("Your input file");
		l3.setBounds(830,80,500,15);
		ta3=new TextArea();
		ta3.setBounds(810,95,200,450);
		
		f.add(l1);
		f.add(ta1); 
		f.add(l2);
		f.add(ta2);
		f.add(l3);
		f.add(ta3);
		f.setVisible(true);

		ta2.setEditable(false);
		Color c1=new Color(200,200,255);
		ta2.setBackground(c1);
		Color c2=new Color(240,230,200);
		ta1.setBackground(c2);
		ta3.setBackground(c2);

		f.setSize(1410,1410);
 

		ta1.addMouseListener(new MouseAdapter()
		{				//mouse Listener
			public void mouseClicked(MouseEvent me)
			{
				try
				{
					X=me.getX();
					Y=me.getY();
					String s=X +"," +Y;
					//ta2.setText(s);
				}

				catch(Exception e)
				{
				JOptionPane.showMessageDialog(null,"error occured" +e.getMessage());
				}
			}	
		});

}



public void actionPerformed(ActionEvent ae)				
{
	if(ae.getSource()==b1 || ae.getSource()==it0)
	{					//Open
		flag=false;
		fd=new FileDialog(new JFrame(),"file dialog");
		fd.setVisible(true);
		String s="";
		int i;
		try
		{
  			if(!fd.getFile().equals(""))
			{
            			File input = new File(fd.getDirectory() + fd.getFile());
  				FileInputStream  fin = new FileInputStream(input);
				output=input;
				fname=fd.getDirectory()+fd.getFile();
   				do
				{   
					i=fin.read();
					if(i!=-1)
					s+=(char)i;
				}while(i!=-1);

				ta1.setText(s);
				backup=s;
				ta2.setText(fname);
			}
		}

		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,"No file selected");
		}
	}

	if(((ae.getSource() == it2 || ae.getSource()==b2) && flag==true) || (ae.getSource() == it3 || ae.getSource()==b3))
{	
	flag=false;	
	fd1=new FileDialog(new JFrame(),"file dialog");
	fd1.setVisible(true);
	fd1.setMode(FileDialog.SAVE);
	try
	{
  		if(!fd1.getFile().equals(""))
		{
            		output = new File(fd1.getDirectory() + fd1.getFile());
  			FileOutputStream  fout = new FileOutputStream(output);
   			String s=ta1.getText();
			byte str[]=s.getBytes();
			fout.write(str);
			backup=s;
		}
	}

	catch(Exception e)
	{
		JOptionPane.showMessageDialog(null,"No name entered ,file not saved");
	}
}
if((ae.getSource() == it2 || ae.getSource()==b2) && flag== false)
{			
	try
	{
		FileOutputStream  fout = new FileOutputStream(output);
   		String s=ta1.getText();
		byte str[]=s.getBytes();
		fout.write(str);
		backup=s;
	}

	catch(Exception e)
	{
		JOptionPane.showMessageDialog(null,"Please select a file or open a new file then save");
	}
}

if(ae.getSource() == it1 || ae.getSource()==b12)
{					//New
	try
	{
		ta1.setText("");
		backup=null;
		flag=true;
	}

	catch(Exception e)
	{
		JOptionPane.showMessageDialog(null,"error occured" +e.getMessage());
	}
}

  
if(ae.getSource()==b9 || ae.getSource()==it9)
{	
	try
	{				//compile
	String ip=JOptionPane.showInputDialog(null, "Enter ip");
	String port=JOptionPane.showInputDialog(null, "Enter port");
	socket = new Socket(ip,Integer.parseInt(port));
	
        inputstream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	dataoutputstream = new DataOutputStream(socket.getOutputStream());
       JOptionPane.showMessageDialog(null,inputstream.readLine());

	}
	catch(Exception e)
        {
                JOptionPane.showMessageDialog(null,"error occured" +e);
        }
}


if(ae.getSource() == it4 || ae.getSource()==b4)
{			//Exit
	try
	{
		System.exit(0);
	}
	catch(Exception e)
	{
		JOptionPane.showMessageDialog(null,"error occured" +e);
	}
}
if(ae.getSource()==b11 || ae.getSource()==it11)			//Undo
{
	try
	{
                dataoutputstream.writeBytes(ta3.getText()+"\n");
                dataoutputstream.writeBytes("INPUT END\n");

                String Message ="",Output="";
                Message = inputstream.readLine();
                if(Message.equals("ERROR"))
                    while(!(Message= inputstream.readLine()).equals("ERROR END"))
                        Output += Message+"\n";
                else
                    while(!(Message= inputstream.readLine()).equals("OUTPUT END"))
                        Output += Message+"\n";
                ta2.setText(Output);
	
		socket.close();
	}


	 catch(Exception e)
        {
                JOptionPane.showMessageDialog(null,"error occured" +e);
        }

}

if((ae.getSource() == it10 || ae.getSource()==b10))
{
	try
	{
		dataoutputstream.writeBytes(ta1.getText()+"\n");
                dataoutputstream.writeBytes("FILE END\n");


	}

	  catch(Exception e)
        {
                JOptionPane.showMessageDialog(null,"error occured" +e);
        }

}







if(ae.getSource()==b8 || ae.getSource()==it8)
{
	try
	{
	JOptionPane.showMessageDialog(null,"Remote GCC Compiler\nCSCE 665 Software Engineering\n");
	}

	catch(Exception e)
	{
		JOptionPane.showMessageDialog(null,"error occured" +e);
	}
}

}

								//end of show function


public static void main(String[] gk)				//Main()
{
	SwingUtilities.invokeLater(new Runnable()
	{
		public void run()
		{ 
			ClientIDE ob=new ClientIDE();
			ob.show();
		}
	});
}
}

