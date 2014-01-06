/* Filename:  hashread.cpp

   Programmer:  Br. David Carlson

   Date:  November 12, 1997

   Last Modified:  December 21, 2001

   This program opens reads a hash table (in a file) as produced by the
   makehash program.  The user interactively looks up data items in the
   table by entering each desired key value.  The corresponding data field
   value is shown on screen.  Lookups are repeated until the user enters a
   (fake) key of 0 to quit.

   To compile this program under Visual C++, create a project that contains
   the following files:
   hash.cpp      itemtype.h   hash.h        hashread.cpp  table.h

   To run this program, make sure that the hash table file hash.dat is in
   the correct directory so that hashread can access it.  The hash.dat file
   is the file produced by the companion makehash program.

   Tested with:
      Microsoft Visual C++ 6.0
      Microsoft Visual C++ .NET
      g++ under Linux
*/

#include "hash.h"


int main(void)
   {
   ItemType Item;
   long SearchKey;
   HashTableClass HashTable('r', "hash.dat");   // open in read mode

   if (HashTable.Empty())
      Error("Hash table is empty");

   cout << "Enter the integer key to be looked up (or 0 to quit): ";
   cin >> SearchKey;

   while (SearchKey != 0)
      {
      if (HashTable.Retrieve(SearchKey, Item))
         cout << "Data field contains: " << Item.DataField << endl;
      else
         cout << "Not found" << endl;
      cout << "Enter the integer key to be looked up (or 0 to quit): ";
      cin >> SearchKey;
      }

   return 0;
   // HashTable destructor is automatically called
   }

