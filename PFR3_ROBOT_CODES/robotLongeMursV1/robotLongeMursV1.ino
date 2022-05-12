/*---------------------------------------------------------------*/
/*--------------------------ROBOT ARDUINO------------------------*/
/*---------------------------------------------------------------*/
/* AVANT LA MISE EN MARCHE                                       */
/* PLACER LE ROBOT PRET D'UN MUR A LA GAUCHE DU ROBOT            */
/*---------------------------------------------------------------*/

#include "fonctions_robot.h"

Servo sg, sd;

float c1distance = 100;//on met par défaut une valeur non critique pour ne pas entrer
float c2distance = 100;//dans un if dès la mise en marche du robot
float c3distance = 30;

//c1 : capteur avant
//c2 : capteur avant gauche
//c3 : capteur lateral


void setup()
{
  Serial.begin(9600);

  sg.attach(9);//  paire droite
  sd.attach(10); // paire gauche (oui il y a inversion)

  pinMode(c1TrigPin, OUTPUT);
  pinMode(c1EchoPin, INPUT);
  pinMode(c2TrigPin, OUTPUT);
  pinMode(c2EchoPin, INPUT);
  pinMode(c3TrigPin, OUTPUT);
  pinMode(c3EchoPin, INPUT);

  pinMode(bipPin, OUTPUT); //pour le bip

  //faire 3 bips pour annoncer départ du robot
  bipInitialisation();

  delay (1000);

  //on fait avancer le robot tout droit
  sg.writeMicroseconds(1580);
  sd.writeMicroseconds(1580);
}//fin setup

void loop() {
  
  //on déclenche les capteurs et on lit leurs valeurs en continu
  //tant qu'aucun capteur ne détecte une distance critique
  c1distance = lectureCapteurAvant();
  c2distance = lectureCapteurAvantGauche();
  c3distance = lectureCapteurLateral();
  
  //affiche valeur distance dans terminal
  //affichageDansLeTerminal(c1distance, c2distance, c3distance);
  
  //arret d'urgence
  if (c1distance > 5 && c1distance < 60 ) { //pour eviter les valeurs extremes en cas de non detection de mur
    Serial.println("ARRET D'URGENCE ACTIVE");
    //retour sonore
    bip();
    //arret
    sg.writeMicroseconds(1500);
    sd.writeMicroseconds(1500);
    delay(500);
    //tourne à droite pour éviter obstacle (tourne à 90° a droite)
    sg.writeMicroseconds(1410);
    sd.writeMicroseconds(1590);
    delay(2200);
    //on remet le robot droit en marche avant
    sg.writeMicroseconds(1580);
    sd.writeMicroseconds(1580);
  }

  //pour longer le mur
  else if (c3distance < 10 ) {
    //retour sonore
    bip();
    //tourne a droite car trop prêt du mur
    //jusqu'à revenir dans le bon intervalle
    sd.writeMicroseconds(1680);
    delay(200);
    //on remet le robot droit en marche avant
    sd.writeMicroseconds(1580);
  }
  
  //pour longer le mur
  else if (c3distance > 30) {
    //retour sonore
    bip();
    //tourne a gauche car trop loin du mur
    sg.writeMicroseconds(1680);
    delay(200);
    //on remet le robot droit en marche avant
    sg.writeMicroseconds(1580);
  }
}//fin loop


//problemes
//peut etre "conflit" entre c2 et c3 car ils regardent tous les 2 le meme mur
//utiliser millis() pour mesurer continuellement via c1 c2 c3?
