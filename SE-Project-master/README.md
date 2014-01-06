SE-Project
==========
Source Folder : Remote_GCC_Compiler

Client can be launched from any machine irrespective of operating system but Server must be executed on a Linux machine.

Changes required in server code – 
  Its taking the absolute path of the home directory of underlying Linux machine so it should be replaced in the code.

Command to replace the home directory – 
    Open the Server.java file in vi/vim editor and execute the below command twice – 
    1,$s/home/Mohit/___required home directory__

Steps to execute Server  – 
      1.	Compile the Server code using below command – 
            Javac Server.java   
      2.	Launch the server with below command – 
            Java Server
      3.	Click on start button
      
Steps to execute Client – 
      1.	Compile the Client code with below command – 
            Javac ClientIDE.java
      2.	Launch the Client IDE with below command – 
            Java ClientIDE
      3.	Click on connect button and enter the IP and port of Server 


Note - Server is running on port 1436
