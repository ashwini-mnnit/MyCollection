/* Filename:  hash.h

   Programmer:  Br. David Carlson

   Date:  November 10, 1997

   Modified:  May 23, 1999
      Changed to use bool.  Also char * for Error parameter.

   Modified:  March 4, 2001 to use const functions when possible.

   This header file sets up HashTableClass, a class which implements a
   file-based table by using a hash table.
*/

#include "table.h"


const long Prime = 101; // a hash table should have a prime number for size

const long MaxFile = 150; // max number of entries in hash table (101 plus
                         // room for items that overflow the regular table)

const long NilPtr = -1;    // a pseudopointer to be used instead of NULL


typedef struct
   {
   ItemType Info;
   long Next;     // a pseudopointer (the record number for the next node)
   } NodeType;


void Error(char * msg);


class HashTableClass: public TableBaseClass
   {
   public:
      HashTableClass(char Mode, char * FileName);
      ~HashTableClass(void);
      bool Empty(void) const;
      bool Insert(const ItemType & Item);
      bool Retrieve(KeyFieldType SearchKey, ItemType & Item);
   private:
      void HandleOpenRead(char * FileName);
      void HandleOpenWrite(char * FileName);
      long Hash(KeyFieldType Key) const;
      long OverflowIndex;   // index of first free record for overflow
      int NodeSize;   // stored here for convenience
   };

