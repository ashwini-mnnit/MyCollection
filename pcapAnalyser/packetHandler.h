#ifndef PACKETHANDLER_H
#define PACKETHANDLER_H
#include <iostream>
#include <vector>
#include <map>
#include <stdio.h>
#include <string.h>
#define __FAVOR_BSD
#include <pcap.h>
#include <stdlib.h>
#include <netinet/ip.h>
#include <errno.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netinet/if_ether.h>
#include <netinet/ip.h>
#include <netinet/tcp.h>
#include <net/ethernet.h>
#include <netinet/ether.h>
#include <arpa/inet.h>
#include <list>
#include <fstream>
#include <iomanip>
#include <sstream>
using namespace std;

typedef enum {
    HTTP = 0,
    FTP = 1,
    TELNET=2
}Protocol;

struct PacketRecord {
    __u16 sPort;
    __u16 dPort;
    u_long seq_num;
    u_long ack_seq;
    u_char* payload;
    int payloadSize ;
    PacketRecord(__u16 _sPort, __u16 _dPort,__u32 _seq_num, __u32 _ack_seq,u_char* _payload, int _payloadSize);
};

struct ComparePacketRecord {
    bool operator()(PacketRecord const * a, PacketRecord const * b) {
        return a->seq_num < b->seq_num;
    }
};

struct Session {
   int reqCount;
   int resCount;
   int reTransmission;
   string filename ;
};

extern list<PacketRecord*> GlobalRequestList;
extern list<PacketRecord*> GlobalResponseList;
extern list<PacketRecord*> GlobalFTPList;
extern list<PacketRecord*> GlobalTelnetList;


//Telnet Processing functions
void processTelnetPort (int port );
void handleTelnetStream();
void printTelnetPacketInFile(const char* filename , char *array , int size);

//Utility function
string getfilename (string dir , int name);
bool isPortProcessed( list<int> portList, int port);


//HTTP Processing 
void handleHTTPStream();

//FTP
void processFTPPort (int port );
void handleFTPStream();
void printDataInFile(char *array, char* filename , int size);

#endif
