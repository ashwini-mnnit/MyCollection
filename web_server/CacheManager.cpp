#include "common.h"
#include <map>
using namespace std;
struct cacheobject{
 void *data;
};

map<string,struct cacheobject> CacheMap;


void addtoCacheMap(string url,char* buffdata)
{
    cacheobject obj;
    obj.data = (char*)buffdata; 
    CacheMap[url]= obj;
}

bool isCacheObjPresent(string _url)
{
    map<string,cacheobject>::iterator it = CacheMap.find(_url);
    if(it!= CacheMap.end())
    {
       return true;
    }
    return false;
}
cacheobject getFromCacheMap(string _url)
{
  return CacheMap[_url];
}



