#include <math.h>
#include <Servo.h>
#include <Stepper.h>

int stepper1Pin1=8;
int stepper1Pin2=9;
int stepper1Pin3=10;
int stepper1Pin4=11;

Stepper Base(200,stepper1Pin1,stepper1Pin2,stepper1Pin3,stepper1Pin4);

int servo1Pin=7;
int servo2Pin=6;
int servo3Pin=12;

Servo servo1;
Servo servo2;
Servo servo3;

int InputG,Input,InputGi=60;
int refB=0;
int ref1;
int ref2;

int a,b,c,d,Pos2;

//float coordinate[8][8]={59.6,58.6,57.8,57.1,57,58.7,57.2,57.8,
//                        56,55.2,55.3,55.2,54.6,54.5,54.7,55.4,
//                        53.5,52.5,51.6,50.9,50.6,50.4,50.8,51.3,
//                        49.8,48.7,47.7,47,46.6,46.7,47.1,47.7,
//                        45.4,44.3,43.4,42.6,42.4,42.2,42.5,43.3,
//                        41.5,40.4,39.4,38.5,37.8,38,38.6,39.6,
//                        37.5,36,35.2,34.2,33.9,33.8,34.4,35.5,
//                        31.2,30.7,30.5,30,28.9,28.8,30.3,31.8};

                        
int servo1pos[8][8]={100,99,98,97,96,97,98,99,
                     91,94,93,91,91,91,93,94,
                     86,89,87,87,87,87,87,89,
                     83,83,83,83,83,83,83,83,
                     80,80,80,78,78,78,80,80,
                     76,76,76,74,74,74,76,76,
                     72,72,70,70,70,70,70,72,
                     69,69,68,68,68,68,68,69};

int servo2pos[8][8]={125,122,120,119,117,119,120,122,
                     115,115,112,111,111,111,112,115,
                     109,107,106,105,105,105,106,107,
                     103,102,100,98,98,98,100,102,
                     96,95,94,94,94,94,94,95,
                     92,91,87,86,86,86,87,91,
                     88,85,84,82,82,82,84,85,
                     84,81,76,74,74,74,76,81};

int servo1Pos,servo2Pos,servo1Posi,servo2Posi;

int sgn(int a)
{
  if(a<0){
    return -1;
  }
  else if(a>0)
  {
    return 1;
  }
  else{
    return 0;
  }
}

void setup() {
  // put your setup code here, to run once:
  Base.setSpeed(20);

  pinMode(5,OUTPUT);
  pinMode(3,OUTPUT);

  analogWrite(5,100);
  analogWrite(3,100);
  
  servo1.attach(servo1Pin);
  servo1.write(45);
  ref1=45;
  servo2.attach(servo2Pin);
  servo2.write(155);
  ref2=155;
  servo3.attach(servo3Pin);
  servo3.write(60);
  Serial.begin(9600);
}

int B;
float x,U;

float r(int a,int b)
{
  B=servo1pos[b][a];
  U=servo2pos[b][a];
  Serial.print("B:");
  Serial.print(B);
  Serial.print(" U:");
  Serial.println(U);
  int A1=135-B;
  int A2=U-65;
  A2=90-A2;
  float y=(38*cos(A1*22/(180*7)))+(39*cos(A2*22/(180*7)))-2;
  return y; 
}

int pos(int M , float n)
{
  B=135-M;
  x=n+2-(40*cos((B*22)/(7*180)));
  Serial.println(x);
  U=acos(x/40);
  Serial.println(U);
  U=U*180*7/22;
   Serial.println(U);
  U=90-U;
  U=U+65;
  return (int)U;
}




void loop() {
  // put your main code here, to run repeatedly:
  Serial.println("Enter coordinate");
  while(Serial.available()==0)
  {
    
  }
  Input=Serial.parseInt();

  d=Input%10;
  c=((Input%100)-d)/10;
  b=((Input%1000)-(Input%100))/100;
  a=((Input)-(Input%1000))/1000;
    Serial.println(a);
    Serial.println(b);
  
//   Base.step(steps-refB);
//   refB=steps;

   x=r(a,b);

   servo1Pos=servo1pos[b][a];
  servo1Posi=servo1Pos-10;

  servo2Posi=pos(servo1Posi,x);
  Serial.print("Bi:");
  Serial.print(servo1Posi);
  Serial.print(" Ui:");
  Serial.println(servo2Posi);

  for (int i=1;i<=((servo1Posi-ref1)*sgn(servo1Posi-ref1));i++)
  {
    servo1.write(ref1+(i*sgn(servo1Posi-ref1)));
    delay(12);
    servo2.write(ref2);
  }

  for (int i=1;i<=((servo2Posi-ref2)*sgn(servo2Posi-ref2));i++)
  {
    servo2.write(ref2+(i*sgn(servo2Posi-ref2)));
    delay(12);
    servo1.write(servo1Posi);
  }  

  for (int i=1;i<=10;i++)
  {
    servo1.write(servo1Posi+i);
    delay(10);
    Pos2=pos(servo1Posi+i,x);
    Serial.print("Bc:");
    Serial.print(servo1Posi+i);
    Serial.print(" Uc:");
    Serial.println(Pos2);
    servo2.write(Pos2);
    delay(10);
  }
  ref1=servo1Pos;
  ref2=servo2Pos;

  Serial.println("Gripper servo");
  delay(100);
  while(Serial.available()==0)
  {
  servo1.write(servo1Pos);
  servo2.write(servo2Pos);
  servo3.write(InputG);
  }
   InputG=Serial.parseInt();
  Serial.print(InputG);
  //servo3.write(InputG);

  for (int i=1;i<=((InputG-InputGi)*sgn(InputG-InputGi));i++){
    servo3.write(InputGi+(i*sgn(InputG-InputGi)));
    delay(5);
  }
  InputGi=InputG;
}
