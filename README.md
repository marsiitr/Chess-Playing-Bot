Chess Playing Robot(a.k.a ‘The Turk’)
 
 Abstract:

Chess playing robot is a semi automated robot.It consists of a mechanical arm to pick and drop chess pieces.The arm is controlled using microcontroller Arduino uno and various motors.The robot uses camera to detect opponent’s move and play according to the chess engine code that is fed through arduino.

Acknowledgement:

We would like to express our special thanks of gratitude to our mentors Sushant Samuel(EE 3rd year),Animesh (GPT 4th year)and Anmol Popli (EE 3rd Year) as well as our guide and senior mentor Prashant Shekhar Singh(EE 4th  year & secretary, Models and Robotics Section) and Sparsh Gupta (EE 4th Year) who gave us the golden opportunity to do this wonderful project  which helped us in doing a lot of research and we came to know about so many new things.

Motivation:

Garry Kasparov VS Deep Blue
Deep Blue was a chess-playing computer developed by IBM. Deep Blue won its first game against a world champion on February 10, 1996, when it defeated Garry Kasparov in game one of a six-game match.This game is a living example what a machine is capable of.To explore it ourselves we built the chess playing robot.

Hardware:

The mechanical design of the bot includes:
1)	Stepper  Motor (12 volt)(1):
2)	Servo Motor (6 volt)(x3):
3)	Arduino Uno(x1):
4)	Adapter(12V,5A)(x2)
5)	L298 Motor Drivers(1):
6)	Gears(x3):
7)	Combination of LM317 and potentiometer  for voltage regulation:
 8) Castor Wheels(3):
 
Work:

The bot consists of Stepper motor mounted at the base which provides angular movement.The mechanical arm picks and drops pieces with the help of gripper.The arm movement is controlled by using 3 servo motors.The motors are synced by using microcontroller Arduino Uno.Camera is mounted at top to detect opponent’s move.We have developed our own version of chess engine which is in Java but used opensource chessengine stockfish.

Description on code is as follows:

We have used open source chessengine stockfish to compute move made by BOT. We have written our own code to validate the move made by BOT and player.
‘movegen.hpp’ sets up values for different pieces and setup the starting position of board. ‘move.hpp’ validates the move executed by player.We have created two boards for this purpose.One named board, which is our actual board at any game point.And second named iboard, on which we execute the move and then check if the king of that player who is making move is not under check after this move(As it will be invalid move if the player comes under check after the move).Every move will be executed on iboard and then on the actual board (if it turns out to be a valid move). 
main.cpp (source file) contains sequential program to play moves one by one by player and BOT.
‘connector.hpp’ connects our code to stockfish (opensource) chessengine.It sends string to the chessengine, which shows current position of chessboard to chessengine.Chessengine returns string for next move of computer (BOT) which is executed by BOT.As the string of chessengine does not shows exact sequence of moves of BOT(e.g., for castling or en passant or capturing a piece BOT has to move to pieces while the string will contain move of only one piece), the moves for BOT is determined by move_erray.hpp which is sent to arduino. tserial.cpp, bot_control.hpp and send_arduno.hpp contains code for communication with arduino. 
The important and biggest part in this code is move validation written in “move.hpp” file. It contains simple code written using for loops, if _else if expressions and mathematical operations. It is simple mathematical expression of chess rules. For example player playing white pieces should not move black piece (sounds silly but we have to check that. And don’t confuse it with capturing move, player has to move his own piece to capture opponent’s piece) so we have to check that if it’s white’s turn then the piece which is moved should be positive (white pieces have positive value in our code).  Moreover, if moved piece is bishop then it should only move diagonally which means we have to check that difference between ranks and files of initial and final positions are same. Now we think you got the idea that we have to check some mathematical condition to validate a move, which we wrote in the code, you can study rest conditions by yourself. The rest of the code is quite simple to understand.
For determining move made by player through taking two pictures with the web cam we mounted in our BOT, one before player makes move and one after player makes move. Difference between these two pictures tells the BOT what player moved.

Future improvements:

1.For better control of arm,use DC motor instead of stepper motor.

2.Use Deep learning to improve and make your own chessengine.

Team members:

1)Prashant Shekhar Singh(EE 4th year, Mentor and secretary of Models and Robotics Section)

2)Sparsh Gupta (EE 4th year,Mentor)

3)Sushant (EE 3rd year,Mentor)

4)Animesh(GPT 4th year,Mentor)

5)Anmol Popli (EE 3rd year,Mentor)

6)Ashwani Srivastava(EE,2nd Year)

7)Vipin Kumar (ME,2nd year

8)Abhijeetsinh Vansia (BT,2nd Year)

9)Satyam Singh (EE,2nd Year)

10)Subramania Siva (ME,1st Year)
 

Thank You!!!!





