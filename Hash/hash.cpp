/*
   This file implements the functions for HashTableClass, as shown in
   hash.h.  This class creates a file-based table by using a hash table.
*/

#include "hash.h"




/* Given:  msg    A message.
   Task:   Display msg and exit the program.
   Return: Nothing to the program, but does return an error code of 1 to
           the operating system.
*/
void Error(char * msg)
   {
   cerr << msg << endl;
   exit(1);
   }


/* Given:  FileName  A char-array type of string holding the filename.
   Task:   The file is opened for reading and the first record is
           read and used to initialize the NumItems field of the object.
           Note that the Info.KeyField field of record (node) zero is
           where NumItems is kept.  The NodeSize field is set to contain
           the number of bytes in a node (record).
   Return: Nothing directly, though the implicit object is modified.
*/
void HashTableClass::HandleOpenRead(char * FileName)
   {
   NodeType Node;

   NodeSize = sizeof(Node);   // fill in this field for later use

   DataFile.open(FileName, ios::in | ios::binary);
   if (DataFile.fail())
      Error("Binary data file cannot be opened for input");

   DataFile.read(reinterpret_cast <char *> (&Node), NodeSize);
   if (DataFile.fail())
      NumItems = 0;  // if cannot read 1st record, assume empty table
   else
      NumItems = Node.Info.KeyField;
   }


/* Given:  FileName  A char-array type of string holding the filename.
   Task:   The file is opened in write mode and record 0 plus MaxFile more
           records are written to the file.  Each record (node) contains 0
           in Info.KeyField and a NilPtr in the Next field.  The 0 in
           record 0 indicates that zero items are currently in the table.
           The other records are just empty nodes.  In addition, the
           object's NumItems field is set to zero and OverflowIndex to
           Prime + 1, the first location in the overflow area beyond the
           regular table area.  The NodeSize field is set to contain the
           number of bytes in a node (record).
   Return: Nothing, though the implicit object is modified.
*/
void HashTableClass::HandleOpenWrite(char * FileName)
   {
   NodeType Node;
   long k;

   NodeSize = sizeof(Node);   // fill in this field for later use

   DataFile.open(FileName, ios::out | ios::in | ios::binary | ios::trunc);
   if (DataFile.fail())
      Error("Binary data file cannot be opened for input/output");

   Node.Info.KeyField = 0;
   //  leave garbage data in the DataField
   Node.Next = NilPtr;

   // Record 0 of the file is to be used to keep the current
   // number of items in the hash table -- in Info.KeyField
   for (k = 0; k <= MaxFile; k++)
      DataFile.write(reinterpret_cast <char *> (&Node), NodeSize);
   NumItems = 0;
   OverflowIndex = Prime + 1;
   }


/* Given:  Mode      An r or w letter to indicate read or write mode.
           FileName  A char-array type of string holding the filename.
   Task:   This is the constructor for the HashTableClass.  An object of
           this class can be created in read mode or write mode.  In read
           mode the file is opened for reading and the first record is
           read and used to initialize the NumItems field of the object.
           Note that the Info.KeyField field of record (node) zero is
           where NumItems is kept.  In write mode, the file is opened in
           write mode and record 0 plus MaxFile more records are written
           to the file.  Each record (node) contains 0 in Info.KeyField
           and a NilPtr in the Next field.  The 0 in record 0 indicates
           that zero items are currently in the table.  The other records
           are just empty nodes.  In addition, the object's NumItems field
           is set to zero and OverflowIndex to Prime + 1, the first location
           in the overflow area beyond the regular table area.
           Note that in both modes, the NodeSize field is set to contain
           the number of bytes in a node (record).
   Return: Nothing, though the implicit object is created.
*/
HashTableClass::HashTableClass(char Mode, char * FileName)
   {
   OpenMode = Mode;   // record the Mode in this field of the object

   if (Mode == 'r')
      HandleOpenRead(FileName);
   else if (Mode == 'w')
      HandleOpenWrite(FileName);
   else
      Error("Incorrect mode given to HashTableClass constructor");
   }


/* Given:  Nothing (other than the implicit object).
   Task:   This is the destructor.  Its job is to close the data file
           associated with the table.  Also, if the table was open in
           write mode, the updated node 0 info is written to the front
           of the file.
   Return: Nothing.
*/
HashTableClass::~HashTableClass(void)
   {
   NodeType Node;

   if (OpenMode == 'w')
      {
      //  Be sure to write out the updated number of items in the table.
      Node.Info.KeyField = NumItems;
      //  leave garbage data in DataField
      Node.Next = NilPtr;
      DataFile.seekp(0, ios::beg);
      DataFile.write(reinterpret_cast <char *> (&Node), NodeSize);
      }
   DataFile.close();

   cout << "Data file has been closed" << endl;
   }


/* Given:   Nothing (other than the implicit object).
   Assumes: That the Table is already open (in read or write mode).
   Task:    To see if the Table object is empty.
   Return:  In the function name, true if table is empty, false otherwise.
*/
bool HashTableClass::Empty(void) const
   {
   return (NumItems == 0);
   }


/* Given:  Item   The item to be inserted into the table.
   Task:   To insert item into the table.
   Return: In function name, true if Item was inserted OK, false otherwise.
*/
bool HashTableClass::Insert(const ItemType & Item)
   {
   bool Success;
   long Location, RecordNumber;
   NodeType CurrentNode, OverflowNode;

   Success = true;
   RecordNumber = Hash(Item.KeyField);
   Location = RecordNumber * NodeSize;
   DataFile.seekg(Location, ios::beg);
   DataFile.read(reinterpret_cast <char *> (&CurrentNode), NodeSize);

   //  KeyField of 0 indicates an empty node - use it
   if (CurrentNode.Info.KeyField == 0)
      {
      CurrentNode.Info = Item;
      DataFile.seekp(Location, ios::beg);
      DataFile.write(reinterpret_cast <char *> (&CurrentNode), NodeSize);
      NumItems++;
      }
   else   // look for a slot in the overflow area
      {
      cout << "Processing a collision" << endl;
      if (OverflowIndex > MaxFile)   // overflow area is full - give up
         Success = false;
      else
         {
         OverflowNode.Info = Item;
         OverflowNode.Next = CurrentNode.Next;
         DataFile.seekp(OverflowIndex * NodeSize, ios::beg);
         DataFile.write(reinterpret_cast <char *> (&OverflowNode),
            NodeSize);
         CurrentNode.Next = OverflowIndex;
         DataFile.seekp(Location, ios::beg);
         DataFile.write(reinterpret_cast <char *> (&CurrentNode),
            NodeSize);
         OverflowIndex++;
         NumItems++;
         }
      }

   return Success;
   }


/* Given:  SearchKey  The value to search for.
   Task:   To search for a record in the table containing SearchKey.
   Return: In function name, true if Item was found, false otherwise.
           Item       A copy of the record that contained SearchKey.
*/
bool HashTableClass::Retrieve(KeyFieldType SearchKey, ItemType & Item)
   {
   NodeType CurrentNode;
   long RecordNumber;
   bool Success;

   Success = false;
   RecordNumber = Hash(SearchKey);

   while ((! Success) && (RecordNumber != NilPtr))
      {
      DataFile.seekg(RecordNumber * NodeSize, ios::beg);
      DataFile.read(reinterpret_cast <char *> (&CurrentNode), NodeSize);
      if (CurrentNode.Info.KeyField == SearchKey)
         {
         Success = true;
         Item = CurrentNode.Info;
         }
      else
         RecordNumber = CurrentNode.Next;
      }

   return Success;
   }


/* Given:   Key   A KeyFieldType value.
   Assumes: That KeyFieldType is an int or long (or that % is overloaded
            to handle appropriately some other data type).
   Task:    To find the hash function value for Key.
   Return:  This hash function value.
*/
long HashTableClass::Hash(KeyFieldType Key) const
   {
   return(Key % Prime + 1);
   }

