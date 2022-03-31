#include <SPI.h>
#include <MFRC522.h>
#include <Keypad.h>

#include "Adafruit_Thermal.h"    // thermal printer
#include "SoftwareSerial.h"

#define SS_PIN 53
#define RST_PIN 48
MFRC522 mfrc522(SS_PIN, RST_PIN);   // Create MFRC522 instance.

#define TX_PIN 10 // Arduino transmit  BLUE WIRE  labeled RX on printer
#define RX_PIN 11 // Arduino receive   GREEN WIRE   labeled TX on printer

SoftwareSerial mySerial(RX_PIN, TX_PIN); // Declare SoftwareSerial obj first
Adafruit_Thermal printer(&mySerial);

int wantsReceipt;
String dateTime = "";
String IBAN = "";

const byte ROWS = 4;
const byte COLS = 4;

String passwordInput;
int passwordLength;
int attemptCounter;

char hexaKeys[ROWS][COLS] = {
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};

byte rowPins[ROWS] = {9, 8, 7, 6};
byte colPins[COLS] = {5, 4, 3, 2};

Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);

void setup()
{
  mySerial.begin(9600);  // Initialize SoftwareSerial
  Serial.begin(9600);   // Initiate a serial communication
  SPI.begin();      // Initiate  SPI bus
  mfrc522.PCD_Init();   // Initiate MFRC522
  //  Serial.println();
  printer.begin();       // Init printer (same regardless of serial type)
}
void loop()
{
  String allDataIn = "";
  while (Serial.available()) {
    allDataIn += Serial.readString();
  }
  int dataLength = allDataIn.length();
  dateTime = allDataIn.substring(0, 15);
  IBAN = allDataIn.substring(15,19);
  wantsReceipt = (int)allDataIn.substring(dataLength - 1, dataLength).toInt();

  if (wantsReceipt == 1) {
    printReceipt();;
  }

  UIDSend();
  checkKeyInput();
}

void UIDSend() {
  //Look for new cards
  if ( ! mfrc522.PICC_IsNewCardPresent())
  {
    return;
  }
  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial())
  {
    return;
  }

  String UID;
  byte letter;
  for (byte i = 0; i < mfrc522.uid.size; i++)
  {
    UID.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
    UID.concat(String(mfrc522.uid.uidByte[i], HEX));
  }
  UID.toUpperCase();

  String UIDsend = UID.substring(1, 3) + UID.substring(4, 6) + UID.substring(7, 9) + UID.substring(10, 12);

  Serial.print("U");
  Serial.println(UIDsend);
  delay(2000);
}

void checkKeyInput() {
  char pressedKey = customKeypad.getKey();

  if (pressedKey) {
    Serial.print(pressedKey);
  }
}

void printReceipt() {

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

  printer.println(addSpaces("IBAN :", "XXXX XXXX " + IBAN));  // IBAN Number shows only last 4
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
