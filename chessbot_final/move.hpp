
#ifndef MOVE_H
#define MOVE_H

#include<iostream>
#include<string>
#include"movegen.hpp"
#include"move_array.hpp"

using namespace std;

int w_a=4,w_b=0,b_a=4,b_b=7;

//Generate copy of board.
int iboard[8][8];
void Gen_iboard(int  board[8][8])
{
    for(int i=0;i<8;i++)
    {
        for (int j=0;j<8;j++)
        {
            iboard[j][i]=board[j][i];
            if(iboard[j][i]==king)
            {
                w_a=i;
                w_b=j;
            }
            else if(iboard[j][i]==-king)
            {
                b_a=i;
                b_b=j;
            }
        }
    }
}

void Finalize_iboard(int  iboard[8][8])
{
    for(int i=0;i<8;i++)
    {
        for (int j=0;j<8;j++)
        {
            board[j][i]=iboard[j][i];
        }
    }
}


void validate_check(int p,int q,int n)
{
    check=false;
        for(int i=1;i<(8-max(p,q));i++)
        {
            if(((iboard[q+i][p+i])!=((2*(n%2))-1)*bishop )&&( (iboard[q+i][p+i])!=((2*(n%2))-1)*queen )&& iboard[q+i][p+i]!=0 && check==false){
                 break;
            }
            else if(iboard[q+i][p+i]==0 && check==false){
            }
            else{
                check=true;
                break;
            }
        }
        for(int i=1;i<=(min(p,8-q));i++)
        {
            if(((iboard[q+i][p-i])!=((2*(n%2))-1)*bishop )&&( (iboard[q+i][p-i])!=((2*(n%2))-1)*queen )&& iboard[q+i][p-i]!=0 && check==false){
                 break;
            }
            else if(iboard[q+i][p-i]==0 && check==false){
            }
            else{
                check=true;
                break;
            }
        }
        for(int i=1;i<=(min(q,8-p));i++)
        {
            if(((iboard[q-i][p+i])!=((2*(n%2))-1)*bishop )&&( (iboard[q-i][p+i])!=((2*(n%2))-1)*queen )&& iboard[q-i][p+i]!=0 && check==false){
                 break;
            }
            else if(iboard[q-i][p+i]==0 && check==false){
            }
            else{
                check=true;
                break;
            }
        }
        for(int i=1;i<=min(p,q);i++)
        {
            if(((iboard[q-i][p-i])!=((2*(n%2))-1)*bishop )&&( (iboard[q-i][p-i])!=((2*(n%2))-1)*queen )&& iboard[q-i][p-i]!=0 && check==false){
                 break;
            }
            else if(iboard[q-i][p-i]==0  && check==false){
            }
            else{
                check=true;
                break;
            }
        }
        for(int i=1;i<=q;i++)
        {
            if(((iboard[q-i][p])!=((2*(n%2))-1)*rook) && ((iboard[q-i][p])!=((2*(n%2))-1)*queen) && iboard[q-i][p]!=0 && check==false){
                 break;
            }
            else if((iboard[q-i][p])==0 && check==false){
            }
            else{
                check=true;
                break;
            }
        }
        for(int i=1;q+i<8;i++)
        {
            if(((iboard[q+i][p])!=((2*(n%2))-1)*rook) && ((iboard[q+i][p])!=((2*(n%2))-1)*queen) && iboard[q+i][p]!=0 && check==false){
                 break;
            }
            else if((iboard[q+i][p])==0 && check==false){
            }
            else{
                check=true;
                break;
            }
        }
        for(int i=1;(p+i<8);i++)
        {
            if(((iboard[q][p+i])!=((2*(n%2))-1)*rook) && ((iboard[q][p+i])!=((2*(n%2))-1)*queen) && iboard[q][p+i]!=0 && check==false){
                 break;
            }
            else if((iboard[q][p+i])==0 && check==false){
            }
            else{
                check=true;
                break;
            }
        }
        for(int i=1;i<=p;i++)
        {
            if(((iboard[q][p-i])!=((2*(n%2))-1)*rook) && ((iboard[q][p-i])!=((2*(n%2))-1)*queen) && iboard[q][p-i]!=0 && check==false){
                 break;
            }
            else if((iboard[q][p-i])==0 && check==false){
            }
            else{
                check=true;
                break;
            }
        }

        if((((iboard[q+1][p+1])==(((2*(n%2))-1)*pawn)) || ((iboard[q+1][p-1])==(((2*(n%2))-1)*pawn))) && check==false){
            check=true;
        }
        else {
            int lrb=max(0,q-2),lfb=max(0,p-2),urb=min(7,q+2),ufb=min(7,p+2);
            if(iboard[q-1][p-2]==((2*(n%2))-1)*knight && q-1>=lrb && p-2>=lfb && check==false){
                check=true;
            }
            else if(iboard[q-2][p-1]==((2*(n%2))-1)*knight && q-2>=lrb && p-1>=lfb && check==false){
                check=true;
            }
            else if(iboard[q+1][p-2]==((2*(n%2))-1)*knight && q+1<=urb && p-2>=lfb && check==false){
                check=true;
            }
            else if(iboard[q-1][p+2]==((2*(n%2))-1)*knight && q-1>=lrb && p+2<=ufb && check==false){
                check=true;
            }
            else if(iboard[q+2][p-1]==((2*(n%2))-1)*knight && q+2<=urb && p-1>=lfb && check==false){
                check=true;
            }
            else if(iboard[q-2][p+1]==((2*(n%2))-1)*knight && q-2>=lrb && p+1<=ufb && check==false){
                check=true;
            }
            else if(iboard[q+1][p+2]==((2*(n%2))-1)*knight && q+1<=urb && p+2<=ufb && check==false){
                check=true;
            }
            else if(iboard[q+2][p+1]==((2*(n%2))-1)*knight && q+2<=urb && p+1<=ufb && check==false){
                check=true;
            }
        }

        if (check==true)
        {
            cout<<"!! CHECK !! \n";
        }
}



void move_finalize()
{
    if(n%2==0 && move_executed==true)
    {
        validate_check(w_a,w_b,n);
        move_finalized=!check;
    }
    else if(n%2==1 && move_executed==true)
    {
        validate_check(b_a,b_b,n);
        move_finalized=!check;
    }
    else if(move_executed==false)
    {
        move_finalized=move_executed;
    }
}



//string passd;
//int board[8][8];




void move_piece (string passd , int board[8][8],int n)
{
    move_executed=false;
    Gen_iboard(board);
    moved=movedn;

    if (passd.substr(0, 1) >= "a" && passd.substr(0, 1) <= "h" && passd.substr(1, 1) >= "1" && passd.substr(1, 1) <= "8" && passd.substr(2, 1) >= "a" && passd.substr(2, 1) <= "h" && passd.substr(3, 1) >= "1" && passd.substr(3, 1) <= "8")
    {
        a = passd[0] - 'a';
        b = passd[1] - '1';
        c = passd[2] - 'a';
        d = passd[3] - '1';
        if ( ( (n%2==0 && sgn(iboard[b][a])==1 ) || (n%2==1 && sgn(iboard[b][a])==-1) ) && (sgn((iboard[b][a])*(iboard[d][c]))<=0) )
        {
        switch( iboard[b][a] )
        {

        case -pawn:
            if( ((a==c && iboard[d][c]==0) && ((b==6 && d==4 && iboard[5][c]==0) || b==d+1)) || ((a==c+1 || a==c-1) && b==d+1 && iboard[d][c]>0) )
            {
                add_move(a,b,c,d,iboard,moved);

                iboard[d][c]=iboard[b][a];
                iboard[b][a]=0;
                if(d==0)
                {
                    /*
                    string piece;
                    cout<<"Enter resurrected piece? Queen/Rook/Knight/Bishop";
                    cin>>piece;
                    switch statement likhde ek, jisme wo string input ke hisaab se tune jo
                    integers likhe hai har piece ke wo dal dega iboard[d][c] mein
                    */
                    moved=movedn;
                    add_move(a,b,8,8,iboard,moved);
                    add_move(9,9,c,d,iboard,moved);

                    iboard[d][c]=-queen;

                }
                if(b==6 && d==4)
                {
                    en_passant=true;
                }
                else
                {
                    en_passant=false;
                }
                move_executed=true;
            }
            else if ((a==c+1 || a==c-1) && b==3 && iboard[2][c]==0 && iboard[3][c]==pawn && en_passant==true)
            {
                add_move(c,3,8,8,iboard,moved);
                add_move(a,b,c,d,iboard,moved);

                iboard[3][c]=0;
                iboard[d][c]=iboard[b][a];
                iboard[b][a]=0;

                move_executed=true;
            }
            break;
        case pawn:
            if( ((a==c && iboard[d][c]==0) && ((b==1 && d==3 && iboard[2][c]==0) || b==d-1)) || ((a==c+1 || a==c-1) && b==d-1 && iboard[d][c]<0) )
            {
                add_move(a,b,c,d,iboard,moved);

                iboard[d][c]=iboard[b][a];
                iboard[b][a]=0;
                if(d==7)
                {
                    moved=movedn;
                    add_move(a,b,8,8,iboard,moved);
                    add_move(9,9,c,d,iboard,moved);

                    iboard[d][c]=queen;
                }
                if(b==1 && d==3)
                {
                    en_passant=true;
                }
                else
                {
                    en_passant=false;
                }
                move_executed=true;
            }
            else if ((a==c+1 || a==c-1) && b==4 && iboard[5][c]==0 && iboard[4][c]==-pawn && en_passant==true)
            {
                add_move(c,4,8,8,iboard,moved);
                add_move(a,b,c,d,iboard,moved);

                iboard[4][c]=0;
                iboard[d][c]=iboard[b][a];
                iboard[b][a]=0;

                move_executed=true;
            }
            break;
        case -bishop:
            if( (((a-c)*sgn(a-c))==((b-d)*sgn(b-d))) && iboard[d][c]>=0 )
            {
                val=true;
                if (((a-c)*sgn(a-c))>1)
                {
                    for(i=1;i<((a-c)*sgn(a-c));i++)
                    {
                        if ((iboard[(b-(sgn(b-d)*i))][(a-(sgn(a-c)*i))])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                if (val==true)
                {
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;

                    move_executed=true;
                }
                break;
            }
        case bishop:
            if( (((a-c)*sgn(a-c))==((b-d)*sgn(b-d))) && iboard[d][c]<=0 )
            {
                val=true;
                if (((a-c)*sgn(a-c))>1)
                {
                    for(i=1;i<((a-c)*sgn(a-c));i++)
                    {
                        if ((iboard[(b-(sgn(b-d)*i))][(a-(sgn(a-c)*i))])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                if (val==true)
                {
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;

                    move_executed=true;
                }
                break;
            }
        case -rook:
            if( (a-c==0 || b-d==0) && (iboard[d][c])>=0 )
            {
                val=true;
                if (a-c==0 && ((b-d)*sgn(b-d))>1)
                {
                    for (i=1;i<((b-d)*sgn(b-d));i++)
                    {
                        if ((iboard[(b-(sgn(b-d)*i))][a])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                else if ( b-d==0 && ((a-c)*sgn(a-c))>1 )
                {
                    for (i=1;i<((a-c)*sgn(a-c));i++)
                    {
                        if ((iboard[b][(a-(sgn(a-c)*i))])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                if (val==true)
                {
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;

                    move_executed=true;
                    if(a==0)
                    {
                        black_longcastle=false;
                    }
                    else if(a==7)
                    {
                        black_shortcastle=false;
                    }
                }
                break;
            }
        case rook:
            if( (a-c==0 || b-d==0) && (iboard[d][c])<=0 )
            {
                val=true;
                if (a-c==0 && ((b-d)*sgn(b-d))>1)
                {
                    for (i=1;i<((b-d)*sgn(b-d));i++)
                    {
                        if ((iboard[(b-(sgn(b-d)*i))][a])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                else if ( b-d==0 && ((a-c)*sgn(a-c))>1 )
                {
                    for (i=1;i<((a-c)*sgn(a-c));i++)
                    {
                        if ((iboard[b][(a-(sgn(a-c)*i))])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                if (val==true)
                {
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;

                    move_executed=true;
                    if(a==0)
                    {
                        white_longcastle=false;
                    }
                    else if(a==7)
                    {
                        white_shortcastle=false;
                    }
                }
                break;
            }
        case -queen:
            if( (a-c==0 || b-d==0) && (iboard[d][c])>=0 )
            {
                val=true;
                if (a-c==0 && ((b-d)*sgn(b-d))>1)
                {
                    for (i=1;i<((b-d)*sgn(b-d));i++)
                    {
                        if ((iboard[(b-(sgn(b-d)*i))][a])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                else if ( b-d==0 && ((a-c)*sgn(a-c))>1 )
                {
                    for (i=1;i<((a-c)*sgn(a-c));i++)
                    {
                        if ((iboard[b][(a-(sgn(a-c)*i))])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                if (val==true)
                {
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;

                    move_executed=true;
                }
                break;
            }
            else if( (((a-c)*sgn(a-c))==((b-d)*sgn(b-d))) && (iboard[d][c])>=0 )
            {
                val=true;
                if (((a-c)*sgn(a-c))>1)
                {
                    for(i=1;i<((a-c)*sgn(a-c));i++)
                    {
                        if ((iboard[(b-(sgn(b-d)*i))][(a-(sgn(a-c)*i))])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                if (val==true)
                {
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;

                    move_executed=true;
                }
                break;
            }
        case queen:
            if( (a-c==0 || b-d==0) && iboard[d][c]<=0 )
            {
                val=true;
                if (a-c==0 && ((b-d)*sgn(b-d))>1)
                {
                    for (i=1;i<((b-d)*sgn(b-d));i++)
                    {
                        if ((iboard[(b-(sgn(b-d)*i))][a])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                else if ( b-d==0 && ((a-c)*sgn(a-c))>1 )
                {
                    for (i=1;i<((a-c)*sgn(a-c));i++)
                    {
                        if ((iboard[b][(a-(sgn(a-c)*i))])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                if (val==true)
                {
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;

                    move_executed=true;
                }
                break;
            }
            else if( (((a-c)*sgn(a-c))==((b-d)*sgn(b-d))) && iboard[d][c]<=0 )
            {
                val=true;
                if (((a-c)*sgn(a-c))>1)
                {
                    for(i=1;i<((a-c)*sgn(a-c));i++)
                    {
                        if ((iboard[(b-(sgn(b-d)*i))][(a-(sgn(a-c)*i))])!=0)
                        {
                            val=false;
                            move_executed=false;
                            break;
                        }
                    }
                }
                if (val==true)
                {
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;

                    move_executed=true;
                }
                break;
            }
        case -knight:
            if ( iboard[d][c]>=0 && ((a-c)*sgn(a-c))>=1 && ((b-d)*sgn(b-d))>=1 && (((a-c)*sgn(a-c))+((b-d)*sgn(b-d)))==3 )
            {
                add_move(a,b,c,d,iboard,moved);

                iboard[d][c]=iboard[b][a];
                iboard[b][a]=0;

                move_executed=true;
            }
            break;
        case knight:
            if ( iboard[d][c]<=0 && ((a-c)*sgn(a-c))>=1 && ((b-d)*sgn(b-d))>=1 && (((a-c)*sgn(a-c))+((b-d)*sgn(b-d)))==3 )
            {
                add_move(a,b,c,d,iboard,moved);

                iboard[d][c]=iboard[b][a];
                iboard[b][a]=0;

                move_executed=true;
            }
            break;
        case -king:
            if ((((a-c)*sgn(a-c))==2) && b==7 && d==7 )
            {
                if (c==6 && iboard[d][5]==0 && iboard[d][6]==0 && black_shortcastle==true)
                {
                    validate_check(a,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    validate_check(5,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    validate_check(6,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    add_move(7,d,5,d,iboard,moved);
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][5]=iboard[d][7];
                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;
                    iboard[d][7]=0;

                    b_b=d;
                    b_a=c;
                    move_executed=true;
                    black_longcastle=false;
                    black_shortcastle=false;
                    break;
                }
                else if (c==2 && (iboard[d][1])==0 && (iboard[d][2])==0 && (iboard[d][3])==0 && black_longcastle==true)
                {
                    validate_check(a,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    validate_check(1,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    validate_check(2,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    validate_check(3,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    add_move(0,d,3,d,iboard,moved);
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][3]=iboard[d][0];
                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;
                    iboard[d][0]=0;

                    b_b=d;
                    b_a=c;
                    move_executed=true;
                    black_longcastle=false;
                    black_shortcastle=false;
                    break;
                }
            }
            else if ( iboard[d][c]>=0 && ((a-c)*sgn(a-c))<=1 && ((b-d)*sgn(b-d))<=1)
            {
                add_move(a,b,c,d,iboard,moved);

                iboard[d][c]=iboard[b][a];
                iboard[b][a]=0;

                validate_check(c,d,n);
                        if(check==true)
                        {
                            break;
                        }
                        else{
                            b_b=d;
                            b_a=c;
                        }
                move_executed=true;
                black_longcastle=false;
                black_shortcastle=false;
                break;
            }
        case king:
            if ((((a-c)*sgn(a-c))==2) && b==0 && d==0 )
                {
                if (c==6 && (iboard[d][5]==0) && (iboard[d][6]==0) && white_shortcastle==true)
                    {
                        validate_check(a,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    validate_check(5,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    validate_check(6,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    add_move(7,d,5,d,iboard,moved);
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][5]=iboard[d][7];
                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;
                    iboard[d][7]=0;

                    w_b=d;
                    w_a=c;
                    move_executed=true;
                    white_longcastle=false;
                    white_shortcastle=false;
                    break;
                    }
                else if (c==2 && ((iboard[d][1])==(iboard[d][2])==(iboard[d][3])==0) && white_longcastle==true)
                    {
                        validate_check(a,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    validate_check(1,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    validate_check(2,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    validate_check(3,b,n);
                    {
                        if(check==true)
                        {
                            break;
                        }
                    }
                    add_move(0,d,3,d,iboard,moved);
                    add_move(a,b,c,d,iboard,moved);

                    iboard[d][3]=iboard[d][0];
                    iboard[d][c]=iboard[b][a];
                    iboard[b][a]=0;
                    iboard[d][0]=0;

                    w_b=d;
                    w_a=c;
                    move_executed=true;
                    white_longcastle=false;
                    white_shortcastle=false;
                    break;
                    }
                }
            else if ( iboard[d][c]<=0 && ((a-c)*sgn(a-c))<=1 && ((b-d)*sgn(b-d))<=1 )
                {
                    add_move(a,b,c,d,iboard,moved);

                iboard[d][c]=iboard[b][a];
                iboard[b][a]=0;

                validate_check(c,d,n);
                        if(check==true)
                        {
                            break;
                        }
                        else{
                            w_b=d;
                            w_a=c;
                        }
                move_executed=true;
                white_longcastle=false;
                white_shortcastle=false;
                break;
                }
            }
        }
    }
    move_finalize();
    moveToexecute=moved;
}




void printb (void)
{
int x , y;
string piece;
cout << endl<<"  'a'  'b'  'c'  'd'  'e'  'f'  'g'  'h'" <<endl;
for (x = 7; x > -1; x--){
cout << endl<<x+1;
 for (y = 0; y < 8; y++){
 switch (board[x][y]){
 case 0:
 piece = "-";
 break;
 case pawn:
 piece = "P";
 break;
 case knight:
 piece = "N";
 break;
 case bishop:
 piece = "B";
 break;
 case rook:
  piece = "R";
 break;
 case queen:
 piece = "Q";
 break;
 case king:
 piece = "K";
 break;
 case -pawn:
 piece = "p";
 break;
 case -knight:
 piece = "n";
 break;
 case -bishop:
 piece = "b";
 break;
 case -rook:
 piece = "r";
 break;
 case -queen:
 piece = "q";
 break;
 case -king:
 piece = "k";
 break;
 }
  cout << "  " << piece << "  ";
 }
 cout<<x+1<<endl;
}
 cout << endl<<"  'a'  'b'  'c'  'd'  'e'  'f'  'g'  'h'" << endl;
}



#endif
