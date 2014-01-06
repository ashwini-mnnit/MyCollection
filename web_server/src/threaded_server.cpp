#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>

typedef struct{
  int sockfd;
}_targ;

void error(const char *msg){
    perror(msg);
    exit(1);
}

void* process_request(void *param){
    int n, i=0, j=0;
	char req_buffer[10240];
    _targ *data = param;

	if (data->sockfd < 0)  	error("ERROR on accept");
    bzero(req_buffer,10240);

    n = read(data->sockfd,req_buffer,10240);
    if (n < 0) error("ERROR reading from socket");

    /* parse the incoming request to get the requested filename */
    char *get = strstr(req_buffer, "GET");
    int get_pos = get - req_buffer;

    char *http = strstr(req_buffer, "HTTP");
    int http_pos = http - req_buffer;

    char filename[20];
    bzero(filename,20);
	strncpy(filename, get+5, http_pos-get_pos-6);
    strcpy(&filename[http_pos-get_pos-5], "\0");

	/* hackish */
	if((strcmp(filename, "")==0) || (strcmp(filename, "threaded_server.c")==0))
		strcpy(filename,"index.html");

	/* fetch contents of the requested file, if the file is present */
    FILE *fd;
	char err_msg[] = "<h2 style=\"color:navy;\">Jed-i webserver could not locate the file you requested for</h2><h3 style=\"color:navy;\">Check the filename and try again!</h3>";
	int fsize=strlen(err_msg);
    if((fd = fopen(filename,"r")) != NULL){
		fseek(fd, 0, SEEK_END);
		fsize = ftell(fd);
		rewind(fd);
	}

	char rsp_file_buffer[fsize];
    bzero(rsp_file_buffer,fsize);

	if(fsize == strlen(err_msg))
	    strcpy(rsp_file_buffer,err_msg);
	else{
		fread(rsp_file_buffer, fsize-1, sizeof(char), fd);
		fclose(fd);
	}

	/* put the HTTP header and payload into response buffer */
    char rsp_http_header[] = "HTTP/1.1 200 OK\r\nServer: Jed-i Webserver/0.0.0 (Unix/Linux)\r\nConnection: close\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n";
    char rsp_buffer[((strlen(rsp_http_header)+fsize))];
	bzero(rsp_buffer,(strlen(rsp_http_header)+fsize));
	strcpy(rsp_buffer, rsp_http_header);
	strcat(rsp_buffer, rsp_file_buffer);

	//sleep(1);
    n = write(data->sockfd, rsp_buffer, strlen(rsp_buffer));
    if (n < 0) error("ERROR writing to socket");
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
