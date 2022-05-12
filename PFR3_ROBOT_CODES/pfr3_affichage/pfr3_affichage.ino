#include "LiquidCrystal.h"
LiquidCrystal lcd(3,4,8,9,10,11);
void setup() {
  // put your setup code here, to run once:
  lcd.begin(16,2);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  lcd.clear();
  lcd.setCursor(3,0);
  lcd.print("UPSSITECH!");
  lcd.setCursor(0,1);
  lcd.print("robot robot robot");
  delay(100);
}
