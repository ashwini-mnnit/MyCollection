
all: tserver 
CacheManager.o: CacheManager.cpp
	g++ -o CacheManager.o -c CacheManager.cpp
Response.o: Response.cpp
	g++ -o Response.o -c Response.cpp

threaded_server.o: threaded_server.cpp
	g++ -o threaded_server.o -c threaded_server.cpp 

tserver: Response.o threaded_server.o CacheManager.o
	g++ threaded_server.o Response.o -o tserver -lpthread


clean:
	rm -rf *.o tserver
