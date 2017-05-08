#ifndef MOVE_array_H
#define MOVE_array_H

#include<iostream>
#include<string>

string moved,movedn,moveToexecute;

string A[10]={"a","b","c","d","e","f","g","h","x","q"};
string B[10]={"1","2","3","4","5","6","7","8","x","q"};


void add_move(int a,int b, int c,int d,int iboard[8][8],string &sr)
{
    string str;
    if (iboard[d][c]!=0)
    {
        str=A[c]+B[d]+A[8]+B[8]+A[a]+B[b]+A[c]+B[d];
    }
    else
    {
        str=A[a]+B[b]+A[c]+B[d];
    }

    sr+=str;
}
/*void execute_move(bool move_finalized,string moveToexecute)
{
    if(move_finalized==true)
    {

    }
    else
    {
        moveToexecute=movedn;
    }
}*/
#endif
