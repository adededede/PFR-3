#include "fonctions_robot.h"

Servo sg, sd;

float c1distance = 100;
float c2distance = 100;
float c3distance = 20;

//c1 : capteur avant
//c2 : capteur avant gauche
//c3 : capteur lateral
void setup()
{
  Serial.begin(9600);

  sg.attach(9);//  paire gauche
  sd.attach(10); // paire droite

  sg.writeMicroseconds(1500);
  sd.writeMicroseconds(1500);
  delay(1000);

  pinMode(c1TrigPin, OUTPUT);
  pinMode(c1EchoPin, INPUT);
  pinMode(c2TrigPin, OUTPUT);
  pinMode(c2EchoPin, INPUT);
  pinMode(c3TrigPin, OUTPUT);
  pinMode(c3EchoPin, INPUT);

  //on fait avancer le robot tout droit
  sg.writeMicroseconds(1600);
  sd.writeMicroseconds(1600);

}

void loop() {

  //on déclenche les capteurs et on lit leurs valeurs en continu
  c1distance = lectureCapteurAvant();
  c2distance = lectureCapteurAvantGauche();
  c3distance = lectureCapteurLateral();

  //affiche valeur distance dans terminal
  affichageDansLeTerminal(c1distance, c2distance, c3distance);

  //arret d'urgence
  if (c1distance < 50 || c2distance < 20 ) {
    Serial.println("ARRET D'URGENCE ACTIVE");
    //arret
    sg.writeMicroseconds(1400);
    sd.writeMicroseconds(1400);
    delay(200);
    sg.writeMicroseconds(1500);
    sd.writeMicroseconds(1500);
    delay(500);

    //tourne tant qu'il y a un obstacle
    sg.writeMicroseconds(1400);
    sd.writeMicroseconds(1600);
    while (c1distance < 50 || c2distance < 20) {
      c1distance = lectureCapteurAvant();
      c2distance = lectureCapteurAvantGauche();
    }
    sg.writeMicroseconds(1600);
    sd.writeMicroseconds(1600);
  }

  //pour longer le mur
  else if (c3distance < 10 ) {
    //tourne a droite car trop prêt du mur
    sd.writeMicroseconds(1700);
    while (c3distance < 10 ) {
      c3distance = lectureCapteurLateral();
    }
    sd.writeMicroseconds(1600);
  }
  
  else if (c3distance > 30) {
    //tourne a gauche car trop loin du mur
    sg.writeMicroseconds(1700);
    while (c3distance > 30) {
      c3distance = lectureCapteurLateral();
    }
    sg.writeMicroseconds(1600);
  }

}
