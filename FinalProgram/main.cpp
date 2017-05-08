
#include<iostream>
#include<string>
#include "Connector.hpp"
#include "movegen.hpp"
#include "move.hpp"



int main (void)
{
n=0;
using namespace std;

cout << "please enter your moves in 4 letter algebraic" << endl << "ie e2e4 in lower case only" << endl;
cout << "commands: exit/E = quit,print = displays the board," << endl  << endl;

string passd;
setup();

//int x=0;
int m=0;

string pos;

char path[20]={'s','t','o','c','k','f','i','s','h','_','8','_','x','6','4','.','e','x','e','\0'};

while (1){

        if((n%2)==0)
        {

        //White (Computer) move
        ConnectToEngine(path);

        passd=getNextMove(pos);

        cout<<passd<<endl;

        move_piece(passd,board,n);

            if(move_finalized==true)
            {
                        Finalize_iboard(iboard);

                        if(m==0)
                        {
                            pos=passd;
                            m=1;
                        }
                        else
                        {
                            pos+=" "+passd;
                        }
                        printb();
                        n++;
                        validate_check(b_a,b_b,n);

                        passd.clear();
                    }
                    else
                    {
                        cout<<"PLEASE ENTER VALID !! MOVE !!!"<<endl;
                        passd.clear();
                    }

          CloseConnection();
        }
        else if((n%2)==1)
        {

        //user (black) move
        cout<<"Enter your move ( Enter 'E' to Exit.)\n";
        getline (cin, passd );
        if (passd.substr(0, 4) == "exit" || passd.substr(0, 4) == "quit" || passd.substr(0,1)=="E")   { //test //for quit or exit statements
          break;
         }
         if (passd.substr(0, 5) == "print")   {
          printb();
         }

            move_piece(passd,board,n);

            if(move_finalized==true)
            {
                Finalize_iboard(iboard);

                if(m==0)
                {
                    pos=passd;
                    m=1;
                }
                else
                {
                    pos+=" "+passd;
                }

                printb();

                passd.clear();

                n++;

                validate_check(w_a,w_b,n);

            }
            else if(move_finalized==false)
            {
                cout<<"PLEASE ENTER VALID !! MOVE !!!"<<endl;
                passd.clear();
            }
         }
}
}
