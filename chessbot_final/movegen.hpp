#ifndef MOVEGEN_H
#define MOVEGEN_H

#include<iostream>
#include<string>

using namespace std;

const int pawn = 1;
const int bishop = 2;
const int knight = 3;
const int rook = 4;
const int queen = 5;
const int king = 6;

int a, b, c, d,n=0;
bool en_passant,check;
bool val;
bool move_executed;
bool move_finalized;
int i,j;
bool white_longcastle=true;
bool black_longcastle=true;
bool white_shortcastle=true;
bool black_shortcastle=true;

int board[8][8];

// board [rank] [file];
// where rank = 0 - 7 (1 to 8 on a real chess board) and file = 0 - 7 (a - h)

const int startup[8][8] = { rook, knight, bishop, queen, king, bishop, knight, rook, pawn, pawn,pawn,pawn,pawn,pawn,pawn, pawn, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -pawn, -pawn, -pawn, -pawn, -pawn, -pawn, -pawn, -pawn, -rook, -knight, -bishop, -queen, -king, -bishop, -knight, -rook};

// black pieces = -piecetype. (negative)

void setup (void) {
int i, j;
    for (i = 0; i < 8; i++)
        {
          for (j = 0; j < 8; j++)
            {
                board[i][j] = startup[i][j]; //setup starting position
            }
        }
}


int sgn(int x)
{
    if(x<0)
    {
        return -1;
    }
    else if(x>0)
    {
        return 1;
    }
    else if(x==0)
    {
        return 0;
    }
}




#endif
