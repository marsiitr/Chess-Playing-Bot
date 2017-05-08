#ifndef AUDINOCOMM_H
#define AUDINOCOMM_H
#include<iostream>
#include <conio.h>
#include "bot_control.h"
#include "tserial.h"
using namespace std;
serial comm; //serial is a class type defined in these files, used for referring to the communication device
int audinocomm()
{
	char data; //To store the character to send
	cout << "Enter character to be sent"; //User prompt
	cin >> data; //User input
	comm.startDevice("COM3", 9600);
	/* “COM 2” refers to the com port in which the USB to SERIAL port is attached. It is shown by right clicking on my computer, then going to properties and then device manager
	9600 is the baud-rate in bits per second */
	comm.send_data(data); //The data is sent through the port
	comm.stopDevice();
	//The device is closed down
	_getch();
	return 0;
}
#endif
