#include <SPI.h>
#include <MFRC522.h>
#include <Keypad.h>

#include "Adafruit_Thermal.h"    // thermal printer
#include "SoftwareSerial.h"

#define SS_PIN 53
#define RST_PIN 48
MFRC522 mfrc522(SS_PIN, RST_PIN);   // Create MFRC522 instance.

MFRC522::MIFARE_Key key;
MFRC522::StatusCode status;

#define TX_PIN 10 // Arduino transmit  BLUE WIRE  labeled RX on printer
#define RX_PIN 11 // Arduino receive   GREEN WIRE   labeled TX on printer

#define mBoven 13
#define mBeneden 12

SoftwareSerial mySerial(RX_PIN, TX_PIN); // Declare SoftwareSerial obj first
Adafruit_Thermal printer(&mySerial);

int wantsReceipt;
String dateTime = "";
String IBAN = "";
int withdraw = 0;
String amount = "";

const byte ROWS = 4;
const byte COLS = 4;

String passwordInput;
int passwordLength;
int attemptCounter;

//Gelddispenser
String geldData = "";
int totaal10 = 13;
int totaal50 = 13;
int draaiTijd = 1200;

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
  printer.begin();       // Init printer (same regardless of serial type)
  pinMode(mBoven, OUTPUT);
  pinMode(mBeneden, OUTPUT);
  analogWrite(mBeneden, 0);
  analogWrite(mBoven, 0);
}

void loop()
{
  String allDataIn = "";
  while (Serial.available()) {
    allDataIn += Serial.readString();
  }
  int dataLength = allDataIn.length();
  dateTime = allDataIn.substring(0, 15);
  IBAN = allDataIn.substring(15, 19);
  wantsReceipt = (int)allDataIn.substring(19, 20).toInt();
  withdraw = allDataIn.substring(20, 21).toInt();
  geldData = allDataIn.substring(21, 23);
  amount = allDataIn.substring(23, 26);

  if (wantsReceipt == 1) {
    printReceipt();
  }
  if (withdraw == 1) {
    checkAantal(geldData);
  }
  IBANSend();
//  UIDSend();
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

void IBANSend() {
  // Prepare key - all keys are set to FFFFFFFFFFFFh at chip delivery from the factory.
  for (byte i = 0; i < 6; i++) key.keyByte[i] = 0xFF;
  byte block;
  byte len;
  if ( ! mfrc522.PICC_IsNewCardPresent()) {
    return;
  }
  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial()) {
    return;
  }
  delay(100);
  byte buffer1[18];
  block = 1;
  len = 18;
  status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, 1, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("Authentication failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  status = mfrc522.MIFARE_Read(block, buffer1, &len);
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("Reading failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  String value = "";
  for (uint8_t i = 0; i < 16; i++)
  {
    value += (char)buffer1[i];
  }
  value.trim();
  Serial.print("I");
  Serial.println(value);
  delay(1000);
  mfrc522.PICC_HaltA();
  mfrc522.PCD_StopCrypto1();
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
  printer.println(addSpaces("Amount :", amount));  // Amount printed

  printer.println("- - - - - - - - - - - - - - - -");   // line

  printer.justify('C');
  printer.println("Money Bank loves you <3");

  printer.feed(6);

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

void checkAantal(String geldData) {
  int hoeveelheid10 = geldData.substring(0, 1).toInt();
  int hoeveelheid50 = geldData.substring(1, 2).toInt();

  boolean conditie10 = totaal10 >= hoeveelheid10;
  boolean conditie50 = totaal50 >= hoeveelheid50;

  Serial.print("B");
  if (totaal10 < 10) Serial.print("0");
  Serial.print(totaal10);
  if (totaal50 < 10) Serial.print("0");
  Serial.print(totaal50);
  Serial.print("B");

  if (conditie10 && conditie50) {
    draaiMBoven(hoeveelheid10);
    draaiMBeneden(hoeveelheid50);
  }
}

void draaiMBoven(int aantal) {
  analogWrite(mBoven, 200);
  delay(aantal * draaiTijd);
  analogWrite(mBoven, 0);
  for (int i = 0; i < aantal; i++) {
    totaal10--;
  }
}

void draaiMBeneden(int aantal) {
  analogWrite(mBeneden, 200);
  delay(aantal * draaiTijd);
  analogWrite(mBeneden, 0);
  for (int i = 0; i < aantal; i++) {
    totaal50--;
  }
}
