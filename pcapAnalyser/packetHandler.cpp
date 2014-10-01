
#include "packetHandler.h"
using namespace std;

list<PacketRecord*> GlobalRequestList;
list<PacketRecord*> GlobalResponseList;
list<PacketRecord*> GlobalFTPList;
list<PacketRecord*> GlobalTelnetList;

list<Session> GlobalHttpSessionList;
list<Session> GlobalFtpSessionList;
list<Session> GlobalTelnetSessionList;

PacketRecord :: PacketRecord(__u16 _sPort, __u16 _dPort,__u32 _seq_num, __u32 _ack_seq, u_char* _payload, int _payloadSize)
{
    sPort     = _sPort;
    dPort     = _dPort;
    seq_num   = _seq_num;
    ack_seq   = _ack_seq;
    payload   = (u_char*)malloc(strlen((const char*)_payload)+1);
    strcpy((char*)payload,(const char*)_payload);
    payloadSize = _payloadSize;
}


//UTIL
string getfilename (string dir , int name)
{
    ostringstream filename;
    filename << dir;
    filename << name ;
    filename << ".txt";

    return filename.str();
}

bool isPortProcessed( list<int> portList, int port) {
    list<int>::const_iterator iterator;
    for (iterator = portList.begin(); iterator != portList.end(); ++iterator) {
        if(port == *iterator)
            return true;
    }
    return false ;
}

//TELNET
void printTelnetPacketInFile(const char* filename , char *array , int size)
{
    int i;
    FILE *file = fopen(filename,"a+");
    static int flag;
    for (i=0; i<size; i++) {
        if(isprint(*(array+1)))
            fprintf(file,"%c",*(array+i));

        if((*array == '\r' || *array == '\n') && flag != 1 ){
            fprintf(file,"\n");
            flag  = 1;
        }else{
            flag = 0;
        }
    }
    fclose(file);
}

void processTelnetPort (int port )
{
    string ftpDir = "output_telnet_";
    static int count = 0;
    string filename  = getfilename(ftpDir,++count);
    Session sessionData;
    sessionData.reqCount = 0;
    sessionData.resCount = 0;
    sessionData.reTransmission = 0;
    sessionData.filename = filename;
    ofstream  dumpfile;
    dumpfile.open (filename.c_str());
    dumpfile<<"****************************************************************************\n";
    dumpfile<<"****************************************************************************\n";
    dumpfile<<"******************************Telnet Stream*********************************\n";
    dumpfile<<"****************************************************************************\n";
    dumpfile<<"****************************************************************************\n\n";
    dumpfile.close();
    
    list<PacketRecord*> tmpTelnetList;

    list<PacketRecord* >::iterator it;
    for (it = GlobalTelnetList.begin(); it != GlobalTelnetList.end(); ++it) {
        if((*it)->payloadSize <=0)continue;
        if((*it)->dPort == port || (*it)->sPort == port) {
            printTelnetPacketInFile(filename.c_str() , (char*)(*it)->payload,(*it)->payloadSize);
            if((*it)->dPort == 23) sessionData.reqCount++;
            if((*it)->sPort == 23) sessionData.resCount++;
            tmpTelnetList.push_back((*it));
        }
    }

    tmpTelnetList.sort(ComparePacketRecord());
    u_long lcount =0;
    for (it = tmpTelnetList.begin(); it != tmpTelnetList.end(); ++it) {
        if((*it)->dPort == port || (*it)->sPort == port) {

           if(lcount == (*it)->seq_num)sessionData.reTransmission++;
           lcount = (*it)->seq_num;
        }
    }
    GlobalTelnetSessionList.push_back(sessionData);
}


void handleTelnetStream()
{
    list<int> donePorts;
    list<PacketRecord* >::iterator it;
    for (it = GlobalTelnetList.begin(); it != GlobalTelnetList.end(); ++it) {
        if((*it)->dPort == 23) {
            int rport =  (*it)->sPort;
            if(!isPortProcessed(donePorts, rport)) {
                donePorts.push_back(rport);
                processTelnetPort(rport);
            }
        }
    }
         //Display the stats for data
    cout<<"################################### Telnet statistics ################################"<<endl;
    cout<<"               Number of Telnet Sessions :"<<GlobalFtpSessionList.size()<<"\n\n";

    list<Session>::iterator it3;
    int lcount = 0;
    for (it3 = GlobalTelnetSessionList.begin(); it3 != GlobalTelnetSessionList.end(); ++it3) {
        cout<<"Session :"<<++lcount<<endl;
        cout<<"     Request Count           :"<<(*it3).reqCount<<endl;
        cout<<"     Response Count          :"<<(*it3).resCount<<endl;
        cout<<"     ReTransmission Count    :"<<(*it3).reTransmission<<endl;
        cout<<"     Output Filename         :"<<(*it3).filename<<endl;
    }
    cout<<"#######################################################################################\n\n"<<endl;


}

//HTTP
void handleHTTPStream()
{
    string httpDir = "output_http_";
    int count = 0;
    list<PacketRecord* >::iterator it;
    for (it = GlobalRequestList.begin(); it != GlobalRequestList.end(); ++it) {
        if((*it)->dPort == 80) {
            Session sessionData;
            sessionData.reqCount = 1;
            sessionData.resCount = 0;
            sessionData.reTransmission = 0;

            if((*it)->payloadSize <= 0) continue;

            string filename  = getfilename(httpDir,++count);
            sessionData.filename = filename;

            ofstream  dumpfile;
            dumpfile.open (filename.c_str());

            list <PacketRecord*> resList  ;
            list<PacketRecord *>::iterator it2;
            for (it2 = GlobalResponseList.begin(); it2 != GlobalResponseList.end(); ++it2) {
                if((*it2)->dPort == (*it)->sPort) {

                    if((*it2)->payloadSize <= 0) continue;
                    sessionData.resCount++;
                    resList.push_back((*it2));
                }
            }
            dumpfile<< "**************************************Request********************************\n\n";
            dumpfile<<(*it)->payload<<endl;
            resList.sort(ComparePacketRecord());

            dumpfile<< "\n\n**********************************Response*******************************\n\n";
            
            u_long rCount = 0;
            for (it2 = resList.begin(); it2 != resList.end(); ++it2) {
                dumpfile<<(*it2)->payload <<endl;
                if(rCount ==(*it2)->seq_num)  sessionData.reTransmission++;
                rCount =(*it2)->seq_num;
            }
            dumpfile.close();
            GlobalHttpSessionList.push_back(sessionData);
        }
    }
    //Display the stats for HTTP data
    cout<<"##################################### HTTP statistics ################################"<<endl;
    cout<<"               Number of HTTP Sessions :"<<GlobalHttpSessionList.size()<<"\n\n";
    
    list<Session>::iterator it3;
    int lcount = 0;
    for (it3 = GlobalHttpSessionList.begin(); it3 != GlobalHttpSessionList.end(); ++it3) {
    	cout<<"Session :"<<++lcount<<endl;
        cout<<"     Request Count           :"<<(*it3).reqCount<<endl;
        cout<<"     Response Count          :"<<(*it3).resCount<<endl;
        cout<<"     ReTransmission Count    :"<<(*it3).reTransmission<<endl;
        cout<<"     Output Filename         :"<<(*it3).filename<<endl;
    }
    cout<<"#######################################################################################\n\n"<<endl;
}

//FTP

void processFTPPort (int port )
{
    string ftpDir = "output_ftp_";
    static int count = 0;
    string filename  = getfilename(ftpDir,++count);
    
    Session sessionData;
    sessionData.reqCount = 0;
    sessionData.resCount = 0;
    sessionData.reTransmission = 0;
    sessionData.filename = filename;
    ofstream  dumpfile;
    dumpfile.open (filename.c_str());
    dumpfile<<"*************************************************\n";
    dumpfile<<"*************************************************\n";
    dumpfile<<"*******************FTP Stream********************\n";
    dumpfile<<"*************************************************\n";
    dumpfile<<"*************************************************\n\n";
    dumpfile.close();
    list<PacketRecord*>tmpFTPList;
    list<PacketRecord* >::iterator it;
    for (it = GlobalFTPList.begin(); it != GlobalFTPList.end(); ++it) {
        if((*it)->dPort == port || (*it)->sPort == port) {
            if((*it)->payloadSize <=0)continue;
            printDataInFile((char*)(*it)->payload,(char*)filename.c_str(),(*it)->payloadSize);
            tmpFTPList.push_back(*it);
            if((*it)->dPort == 21) sessionData.reqCount++;
            if((*it)->sPort == 21) sessionData.resCount++;
        }
    }
   
    tmpFTPList.sort(ComparePacketRecord());
    u_long lcount =0;
    for (it = tmpFTPList.begin(); it != tmpFTPList.end(); ++it) {
        if((*it)->dPort == port || (*it)->sPort == port) {
           
           if(lcount == (*it)->seq_num)sessionData.reTransmission++;
           lcount = (*it)->seq_num;
        }
    }
    GlobalFtpSessionList.push_back(sessionData);
}

void handleFTPStream()
{
    list<int> donePorts;
    list<PacketRecord* >::iterator it;
    for (it = GlobalFTPList.begin(); it != GlobalFTPList.end(); ++it) {
        if((*it)->dPort == 21) {
            int rport =  (*it)->sPort;
            if(!isPortProcessed(donePorts, rport)) {
                donePorts.push_back(rport);
                processFTPPort(rport);
            }
        }
    }
       //Display the stats for data
    cout<<"############################### FTP statistics ########################################"<<endl;
    cout<<"               Number of FTP Sessions :"<<GlobalFtpSessionList.size()<<"\n\n";

    list<Session>::iterator it3;
    int lcount = 0;
    for (it3 = GlobalFtpSessionList.begin(); it3 != GlobalFtpSessionList.end(); ++it3) {
        cout<<"Session :"<<++lcount<<endl;
        cout<<"     Request Count           :"<<(*it3).reqCount<<endl;
        cout<<"     Response Count          :"<<(*it3).resCount<<endl;
        cout<<"     ReTransmission Count    :"<<(*it3).reTransmission<<endl;
        cout<<"     Output Filename         :"<<(*it3).filename<<endl;
    }
    cout<<"#######################################################################################\n\n"<<endl;
}

void printDataInFile(char *array, char* filename , int size)
{
    int i;
    FILE *file = fopen(filename,"a+");
    for (i=0; i<size; i++) {
        fprintf(file,"%c",*(array+i));
    }
    fprintf(file,"\n");
    fclose(file);
}

