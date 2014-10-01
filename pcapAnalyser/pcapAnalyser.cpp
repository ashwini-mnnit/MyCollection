#include "packetHandler.h"
void InputValidation(int argc , char **argv)
{
    if (argc < 2) {
        cout<< "ERROR:Invalid Input"<<endl;
        cout << "Usage: %s <list of pcap files>"<<argv[0]<<endl;
        exit(1);
    }

}

int InvalidIPPktHdr    = 0;
int InvalidTCPPktHdr   = 0;
int NonTCPPacket       = 0;

void handleIPPacket( u_char *pPkt) {
    struct ip *pIP;
    struct tcphdr *pTcp;
    u_char *pPayload;

    pIP = (struct ip*)(pPkt +14);// ethernet header is 14.
    size_t ipPktSize = sizeof(struct ip); //Extract the size of Ip header.

    if(ipPktSize < 20){
        InvalidIPPktHdr++;
        return;
    }
    
    if(pIP->ip_p != IPPROTO_TCP){
        NonTCPPacket++;
        return;
    }

    pTcp = (struct tcphdr*)(pPkt +14 + ipPktSize);
    size_t tcpPktSize = pTcp->th_off*4;

    if(tcpPktSize <20){
        InvalidTCPPktHdr++;
        return;
    }

    pPayload = pPkt +14 + ipPktSize+ tcpPktSize;
    int payloadSize = ntohs(pIP->ip_len) - (tcpPktSize + ipPktSize);

    __u16 destPort = ntohs(pTcp->th_dport);
    __u16 sourcePort = ntohs(pTcp->th_sport);

    //Telnet
    if(destPort == 23 || sourcePort == 23)
    {
        PacketRecord *pkt = new PacketRecord( sourcePort,destPort , pTcp->th_seq , pTcp->th_ack, pPayload, payloadSize);
        GlobalTelnetList.push_back(pkt);
    }
    //FTP
    if(destPort == 21 || sourcePort == 21)
    {
        PacketRecord *pkt = new PacketRecord( sourcePort,destPort , pTcp->th_seq , pTcp->th_ack, pPayload, payloadSize);
        GlobalFTPList.push_back(pkt);
    }
    //HTTP
    if(destPort == 80)
    {
        PacketRecord *pkt = new PacketRecord( sourcePort,destPort , pTcp->th_seq , pTcp->th_ack, pPayload, payloadSize);
        GlobalRequestList.push_back(pkt);
    }
    if(sourcePort == 80)
    {
        PacketRecord *pkt = new PacketRecord( sourcePort,destPort , pTcp->th_seq , pTcp->th_ack,pPayload, payloadSize);
        GlobalResponseList.push_back(pkt);
    }

}

int processPcapFile(char *filename,char** errStatus)
{
    pcap_t *handler;
    char ebuff[PCAP_ERRBUF_SIZE];
    handler = pcap_open_offline(filename, ebuff);

    if (handler == NULL) {
        *errStatus = ebuff;
        return -1 ;
    }

    const u_char *pkt;
    struct pcap_pkthdr header;

    while((pkt = pcap_next(handler,&header)) !=NULL) {
        u_char *pkt_ptr = (u_char *)pkt;
        struct ether_header *eptr;
        u_short ether_type;
        eptr = (struct ether_header *) pkt_ptr;
        ether_type = ntohs(eptr->ether_type);
        if (ether_type == ETHERTYPE_IP) {
            handleIPPacket(pkt_ptr);
        }
        else {
            cout<<"Ethernet type is not IP: skiping..."<<endl;
        }
    }
    pcap_close(handler);
    return 0;
}


int main(int argc, char **argv)
{
    InputValidation(argc, argv);

    int fileCount = argc - 1;
    while(fileCount > 0)
    {
        char *err;
        char *filename = argv[fileCount];
        fileCount--;
        if(processPcapFile(filename,&err) < 0) {
            cout<< "ERROR: Unable to process the " <<argv[fileCount]<<"( "<< err<<" ) file "<<"skiping..."<<endl;
        }
    }
    cout<<"********************************************************************************************************"<<endl;
    cout<<"************************************ Summary Report ****************************************************"<<endl;
    cout<<"********************************************************************************************************\n\n"<<endl;
    cout<<"Overall Summary : "<<endl;
    cout<<"                 Number of Invalid IP header      : "<<InvalidIPPktHdr<<endl;
    cout<<"                 Number of Invalid TCP header     : "<<InvalidTCPPktHdr<<endl;
    cout<<"                 Number of Non TCP  Packet        : "<<NonTCPPacket<<endl;
    handleHTTPStream();
    handleFTPStream();
    handleTelnetStream();
    cout<<"****************************************DONE*************************************************************"<<endl;
}
