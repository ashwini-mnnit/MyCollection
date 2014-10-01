CC=g++
CFLAGS=-c -Wall 

all: pcap_analyser

pcap_analyser: pcapAnalyser.o packetHandler.o 
	$(CC) pcapAnalyser.o packetHandler.o -o pcap_analyser -lpcap

packetHandler.o: packetHandler.cpp
	$(CC) $(CFLAGS) packetHandler.cpp

pcapAnalyser.o: pcapAnalyser.cpp
	$(CC) $(CFLAGS) pcapAnalyser.cpp



clean:
	rm -rf *o pcap_analyser 
