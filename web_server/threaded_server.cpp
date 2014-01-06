#include<stdlib.h>
#include "common.h"
#define HOST  "192.168.1.100"
#define PORT 80
typedef struct{
  int sockfd;
}_targ;

void error(const char *msg){
    perror(msg);
    exit(1);
}


int create_tcp_socket()
{
        int sock=0;
        if ((sock = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP)) < 0) {
                perror("Can't create a socket");
        }
        return sock;
}

void* process_request(void *param){
    int n, i=0, j=0;
    int sock;
    int tmpres;
    struct sockaddr_in *remote;
	char req_buffer[10240];
    _targ *data =(_targ*) param;

	if (data->sockfd < 0)  	error("ERROR on accept");
    bzero(req_buffer,10240);

    n = read(data->sockfd,req_buffer,10240);
    if (n < 0) error("ERROR reading from socket");


    sock = create_tcp_socket();
    fprintf (stderr,"Remote Server IP is %s", HOST);
    printf ("Connect to remote host now\n");
    remote = (struct sockaddr_in *) malloc (sizeof(struct sockaddr_in ));
        remote->sin_family = AF_INET;
        tmpres = inet_pton(AF_INET,HOST,(void *)(&(remote->sin_addr.s_addr)));
        if (tmpres < 0) {
                perror("Can't get remote->sin_addr.s_addr");
                exit(1);
        } else if(tmpres == 0) {
                fprintf(stderr,"%s is not a valid IPaddres\n",HOST);
        }
        remote->sin_port = htons(PORT);
        if (connect (sock,(struct sockaddr *)remote, sizeof(struct sockaddr)) < 0)      
        {
                perror("Couldn't connect");
                exit(1);
        }
	printf("Write to remote sockfd\n");

        n  = write(sock, req_buffer, strlen(req_buffer));
        if (n < 0) error("ERROR writing to server socket");
	
	int chunkWritten=0;
	int totalbytesread = 0;
	int totalbyteswritten=0;
	int chunkRead;
	char rsp_data[10240];

	while((chunkRead = read(sock,rsp_data,sizeof(data))) != (size_t)NULL)
	{
	  printf("Got some response, write into client socket now..\n");
	  chunkWritten = write(data->sockfd, rsp_data,strlen(rsp_data));
          if (chunkWritten < 0) error("ERROR writing to client socket");
	  totalbytesread += chunkRead;
	  totalbyteswritten += chunkWritten;
	}
	close(sock);

    close(data->sockfd);
    pthread_exit(0);
}



int main(int argc, char *argv[])
{
    int sockfd, newsockfd, portno;
    socklen_t clilen;
    struct sockaddr_in serv_addr, cli_addr;
    int n;
    if (argc < 2) {
        fprintf(stderr,"ERROR, no port provided\n");
        exit(1);
    }

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0)	error("ERROR opening socket");
    bzero((char *) &serv_addr, sizeof(serv_addr));
    portno = atoi(argv[1]);
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portno);

    if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) 
		error("ERROR on binding");
    clilen = sizeof(cli_addr);
    listen(sockfd,30);

    while(1){
	    newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);

	    _targ *tdata = (_targ*)malloc(sizeof(_targ));
	    tdata->sockfd = newsockfd;

	    pthread_t tid;
	    pthread_attr_t attr;
    	    pthread_attr_init(&attr);
	    pthread_create(&tid,&attr,process_request,tdata);
	}
        close(sockfd);
	return 0; 
}
