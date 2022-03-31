#include <Keypad.h>
#include "Adafruit_Thermal.h"    // thermal printer
#include "SoftwareSerial.h" 

#define TX_PIN 10 // Arduino transmit  BLUE WIRE  labeled RX on printer
#define RX_PIN 11 // Arduino receive   GREEN WIRE   labeled TX on printer
#define greenLed 12
#define redLed 13

SoftwareSerial mySerial(RX_PIN, TX_PIN); // Declare SoftwareSerial obj first
Adafruit_Thermal printer(&mySerial);

int wantsReceipt;
String dateTime = "";

const byte rowSize = 4;
const byte colSize = 4;

byte rowPins[rowSize] = {9,8,7,6};
byte colPins[colSize] = {5,4,3,2};

char keymatrix[rowSize][colSize] = {
  {'1','2','3','A'},
  {'4','5','6','B'},
  {'7','8','9','C'},
  {'*','0','#','D'}
};

Keypad customKeypad = Keypad(makeKeymap(keymatrix), rowPins, colPins, rowSize, colSize);

void setup() {
  mySerial.begin(9600);  // Initialize SoftwareSerial
  Serial.begin(9600);
  printer.begin();       // Init printer (same regardless of serial type)
  pinMode(greenLed, OUTPUT);
  pinMode(redLed, OUTPUT);

}

void loop() {
  // put your main code here, to run repeatedly:
  String allDataIn ="";
  while(Serial.available()){
    allDataIn += Serial.readString();
  }
  int dataLength = allDataIn.length();
  dateTime = allDataIn.substring(0, 15);
  wantsReceipt = (int)allDataIn.substring(dataLength - 1, dataLength).toInt();
  
  checkKeyInput();
  
  if(wantsReceipt == 1){
      printReceipt();;
    }
}

void checkKeyInput(){
    char pressedKey = customKeypad.getKey();
    
    if (pressedKey) {
        Serial.print(pressedKey); 
        
    }
}

void printReceipt(){
  
   printer.justify('C');  // Center
   printer.boldOn();      // Bold
   printer.setSize('L');  // Large
   
   printer.println("Money Bank");  // bank name on top
   printer.println("________________");  // seperating line

   printer.justify('L');       // Left
   printer.setSize('S');       // Small

   printer.println(addSpaces(dateTime.substring(0, 5), dateTime.substring(5, 15)));  // Time en Date

   printer.boldOff();          

   printer.println(("- - - - - - - - - - - - - - - -"));   // LINE

   printer.println(addSpaces("IBAN :", "XXXX XXXX XXXX 69"));  // IBAN Number shows only last 2
   printer.println(addSpaces("Amount :", "20 EUR"));  // Amount printed
   printer.println(addSpaces("Transaction# :", "14260"));   // transaction ID
  
   printer.println("- - - - - - - - - - - - - - - -");   // line

   printer.feed(1);
   
   printer.justify('C');
   printer.println("Money Bank loves you <3");
   
   printer.feed(2);

   delay(3000L);
   printer.wake();
   printer.setDefault(); // Restore printer to defaults
  
}

String addSpaces(String lStr, String rStr) {    // Adds spaces in between two strings
    int len = (lStr + rStr).length();
                          
    if (len > 31) {                 // Receipt length is only 32 (normal) characters
        return lStr + rStr;
    }

    String spaces;
    for (int i = len; i < 32; i++) {
        spaces += ' ';
    }

    return lStr + spaces + rStr;
}
