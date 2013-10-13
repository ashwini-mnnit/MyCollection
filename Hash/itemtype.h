/* Filename:  itemtype.h

   Programmer:  Br. David Carlson

   Date:  May 23, 1999 
   
   Modified:  July 16, 2000 to use modern headers.

   This header file sets up ItemType and associated items.
*/


#include <iostream>
using namespace std;


const int DataFieldMax = 20;


typedef long KeyFieldType;

typedef char DataFieldType[DataFieldMax];

typedef struct
   {
   KeyFieldType KeyField;
   DataFieldType DataField;
   } ItemType;

