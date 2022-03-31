#include <SPI.h>
#include <MFRC522.h>
#include <Keypad.h>

#define SS_PIN 10
#define RST_PIN 9
MFRC522 mfrc522(SS_PIN, RST_PIN);   // Create MFRC522 instance.

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

byte rowPins[ROWS] = {7, 6, 5, 4};
byte colPins[COLS] = {3, 2, 8, 0};

Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);

void setup()
{
  Serial.begin(9600);   // Initiate a serial communication
  SPI.begin();      // Initiate  SPI bus
  mfrc522.PCD_Init();   // Initiate MFRC522
  Serial.println();

}
void loop()
{
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
