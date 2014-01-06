
g++ -o Response.o -c  Response.cpp
g++ -o threaded_server.o -c  threaded_server.cpp 

g++ Response.o threaded_server.o -o tserver -lpthread

