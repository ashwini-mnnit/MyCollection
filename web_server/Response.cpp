#include "common.h"
#define SIZE 4096
using namespace std;
string getResponse(string filename )
{
   int port = 80;
   int conn;
   string recvStr = "";
   const char *method = "GET";
   const char *version = "1.1";
   string url;
   static struct sockaddr_in to_addr;
   ostringstream  msg;
   string host = "192.168.1.100";
   if ((conn = socket(PF_INET, SOCK_STREAM, 0)) < 0) {
        cout<< __FUNCTION__ <<"::Can not create socket."<< endl;
        return recvStr;
   }
   to_addr.sin_family = AF_INET;
   inet_pton(AF_INET, (const char*)host.c_str(), &(to_addr.sin_addr));
   to_addr.sin_port = htons(port);
   if (connect(conn, (struct sockaddr *) &to_addr, sizeof(struct sockaddr_in)) < 0)
   {
       cout << __FUNCTION__ <<"::Can not connect to original server"<< endl;
       close(conn);
       return recvStr;
   }
   cout<<"sending req"<<endl;
   char buf[SIZE];
  cout<<msg.str()<<endl;
   msg << method << " " <<"/"<< filename << " " << "HTTP/" << version << "\r\nHost: " << host << "\r\n\r\n";
  cout<<msg.str()<<endl;
   int  sendBytes = send(conn, msg.str().c_str(), msg.str().length(), 0);
   if (sendBytes < 0) {
       close(conn);
       cout<<__FUNCTION__<<"::not able to send request."<<endl;
       return recvStr;
   } else if (sendBytes != (int)msg.str().length()) {
       close(conn);
       cout<<__FUNCTION__<<"::not able to send request."<<endl;
       return recvStr;
   }
   int len = 0;
   while((len = recv( conn, buf, sizeof(buf), 0)) > 0){
        buf[len]='\0';
        recvStr.append(buf,len);
   }
   close(conn);
   return recvStr;
}
#if 0
int main()
{
   string filename = "IMG_0183.JPG";
   cout<<getResponse(filename);
}
#endif
