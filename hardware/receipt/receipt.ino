#include "Adafruit_Thermal.h"
#include "SoftwareSerial.h"
#include <arduinio.h>
#include "logo.h"

#define TX_PIN 10 // Arduino transmit  BLUE WIRE  labeled RX on printer
#define RX_PIN 11 // Arduino receive   GREEN WIRE   labeled TX on printer

SoftwareSerial mySerial(RX_PIN, TX_PIN); // Declare SoftwareSerial obj first
Adafruit_Thermal printer(&mySerial);     // Pass addr to printer constructor

void setup(){
    mySerial.begin(9600);  // Initialize SoftwareSerial
    Serial.begin(9600);
    printer.begin();       // Init printer (same regardless of serial type)

}

void loop(){
  cout << "Wil jij een bon " << endl;
  String keuze;
  cin >> keuze;
  if (keuze == "Ja") {
    printReceipt();
    cout << "Fijne dag" << endl;
  }else if (keuze == "Nee"){
    cout << "Fijne dag" << endl;
  }
}

void printReceipt(){
  
   printer.justify('C');  // Center
   printer.boldOn();      // Bold
   printer.setSize('L');  // Large

   printer.printBitmap(hrlogoklein_width, hrlogoklein_height, hrlogoklein_data);
   printer.feed(2);
   
   printer.println("Money Bank");  // bank name on top
   printer.println("________________");  // seperating line

   printer.justify('L');       // Left
   printer.setSize('S');       // Small

   printer.println(addSpaces("14:20", "10/03/2022"));  // Time en Date

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
