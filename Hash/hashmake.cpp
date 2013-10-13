/* Filename:  hashmake.cpp

   Programmer:  Br. David Carlson

   Date:  November 12, 1997

   Last Modified:  December 21, 2001

   This program creates a hash table in a file.  It reads the data from
   the source text file hash.txt.  This file must contain an integer key on
   one line, followed by a string on the next, etc.  The file hash.dat is
   used to stored the hash table.  Use the accompanying readhash program
   to look up data that is in the hash table.

   To compile this program under Visual C++ you will need to set up a project
   containing the following files:
   hash.cpp      hashmake.cpp  itemtype.h   hash.h        table.h

   Tested with:
      Microsoft Visual C++ 6.0
      Microsoft Visual C++ .NET
      g++ under Linux
*/

#include <fstream>
#include "hash.h"


int main(void)
   {
   ItemType Item;
   fstream Source;

   HashTableClass HashTable('w', "hash.dat");

   Source.open("hash.txt", ios::in);
   if (Source.fail())
      Error("ERROR: Unable to open file hash.txt for input");

   Source >> Item.KeyField;
   while (! Source.fail())
      {
      Source >> Item.DataField;
      cout << "Inserting: " << Item.KeyField << "   " << Item.DataField
         << endl;
      if (! HashTable.Insert(Item))
         cout << "Warning:  unable to insert item with KeyField: "
            << Item.KeyField << endl;
      Source >> Item.KeyField;
      }

   Source.close();
   return 0;
   // HashTable destructor is automatically called
   }

