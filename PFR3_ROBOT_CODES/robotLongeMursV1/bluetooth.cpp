
#include "bluetooth.h"
#include <SoftwareSerial.h>
#define BLUETOOTH_RX 19
#define BLUETOOTH_TX 18
SoftwareSerial mavoieserie(BLUETOOTH_TX, BLUETOOTH_RX);
void initBluetooth(void){

  mavoieserie.begin(9600);
}
