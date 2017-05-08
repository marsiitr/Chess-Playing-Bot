#ifndef SEND_ARDUINO_H
#define SEND_ARDUINO_H

#include<iostream>
#include<string>
#include<math.h>
#include"move.hpp"
#include"movegen.hpp"
#include"move_array.hpp"

using namespace std;

int servo1pos[8][8]={58,58,60,60,60,60,60,58,
                     60,60,62,62,62,62,62,62,
                     63,64,65,65,65,65,65,65,
                     65,67,67,67,68,68,67,67,
                     70,69,71,72,72,72,72,70,
                     72,72,73,74,74,74,76,73,
                     75,76,78,77,77,77,76,73,
                     78,78,78,79,79,79,78,78};

int servo2pos[8][8]={119,117,116,114,114,114,116,117,
                     111,110,108,106,106,106,106,108,
                     105,103,100,100,100,100,100,100,
                     100,97,94,94,92,92,94,94,
                     93,90,90,88,88,88,88,88,
                     88,85,82,82,82,82,83,83,
                     82,80,77,74,74,74,75,80,
                     76,74,70,69,69,70,70,72};

int stepperPos[8][8]={};

int Base;
float x,U;

float dist;
int pos2;

float r(int a,int b)
{
  Base=servo1pos[b][a];
  U=servo2pos[b][a];
  int A1=Base-10;
  int A2=135-U;
  float L1=38*cos((A1*22)/(180*7));
  float L2=39*cos((A2*22)/(180*7));
  float y=L1+L2-2;

  return y;
}

int pos(int M , float n)
{
  Base=M-10;
  x=n+2-(38*cos((Base*22)/(7*180)));
  U=acos(x/39);
  U=(U*180*7)/22;
  U=135-U;
  return (int)U;
}

int m;
int a_c,b_c,c_c,d_c;

string Send_Arduino(string moveToexecute)
{
    int l=0;
    while(moveToexecute[l])
    {
        if(l%2==0){
            m=moveToexecute[l]-'a';}
        else{
            m=moveToexecute[l]-'1';}

            if(l%4==0){
                a_c=m;
            }
            else if(l%4==1){
                b_c=m;
            }
            else if(l%4==2){
                c_c=m;
            }
            else{
                d_c=m;
            }

            if(l%4==3){
                dist=r(a_c,b_c);
                pos2=pos(10+(servo1pos[b_c][a_c]),dist);

               	
                for (int i=0;i<10;i++)
                {

                    pos2=pos(servo1pos[b_c][a_c],dist);
                }

            }

            l++;
    }


}


#endif // SEND_ARDUINO_H
