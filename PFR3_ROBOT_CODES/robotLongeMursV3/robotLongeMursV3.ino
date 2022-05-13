//il faut un delay de 300 entre 2 mesures de C2 et C3  pour ne pas fausser la mesure

#include "fonctions_robot.h"
#include "fonctions_Moteurs.h"
Servo sg, sd;

float c1distance = 100;//on met par défaut une valeur non critique pour ne pas entrer
float c2distance = 30;//dans un if dès la mise en marche du robot
float c3distance = 30;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  sg.attach(9);//  paire droite
  sd.attach(10); // paire gauche (oui il y a inversion)
  pinMode(c1TrigPin, OUTPUT);
  pinMode(c1EchoPin, INPUT);
  pinMode(c2TrigPin, OUTPUT);
  pinMode(c2EchoPin, INPUT);
  pinMode(c3TrigPin, OUTPUT);
  pinMode(c3EchoPin, INPUT);
  delay(1000);
}

void loop() {
  /*BOUCLE PRINCIPALE, ROBOT PRES DU MUR*/
  //lecture capteur avant en continu
  c1distance = lectureCapteurAvant();

  //si pas d'obstacle, check le mur
  if (c1distance > 50) {
    //lecture capteurs latéraux
    delay(300);
    c2distance = lectureCapteurLateralAvant();
    delay(300);
    c3distance = lectureCapteurLateralArriere();
    delay(300);

    //si fin de mur, tourne a gauche pour le  longer
    if (c2distance > 70 && c3distance > 70) {
      Serial.println("PLUS DE MUR");
      //arret
      arretTotal(sg, sd, 1600);
      //tourne à 90 a gauche
      sd.writeMicroseconds(1300);
      sg.writeMicroseconds(1600);
      delay(600);
      //on remet le robot droit en marche avant
      avancer(sg, sd, 1580);
      //avance jusqua revenir pres du mur
      while (c2distance > 70 || c3distance > 70) {
        c2distance = lectureCapteurLateralAvant();
        delay(300);
        c3distance = lectureCapteurLateralArriere();
        delay(300);
      }
    }
  }//retour boucle principale

  //si obstacle, tourne à droite
  else if (c1distance < 50) {
    Serial.println("OBSTAAAAAACLE");
    //arret
    arretTotal(sg, sd, 500);
    //tourne à droite à 90
    sg.writeMicroseconds(1300);
    sd.writeMicroseconds(1600);
    delay(650);
    //remise en marche avant droite
    avancer(sg, sd, 1580);
  }//retour boucle principale
}

/* delay(5000);
    //avance jusqu'a que c2 ne détecte plus obstacle puis c3 non plus
    while (c2distance < 70 || c3distance < 70) {
      Serial.println("J'ESQUIVE L'OBSTACLE");
      affichageDansLeTerminal(c1distance, c2distance, c3distance);
      c2distance = lectureCapteurLateralAvant();
      delay(300);
      c3distance = lectureCapteurLateralArriere();
      delay(300);
    }
    Serial.println("OBSTACLE ESQUIVE");
    //tourne à gauche à 90*/
