/*---------------------------------------------------------------*/
/*--------------------------ROBOT ARDUINO------------------------*/
/*---------------------------------------------------------------*/
/* AVANT LA MISE EN MARCHE                                       */
/* PLACER LE ROBOT PRET D'UN MUR A LA GAUCHE DU ROBOT            */
/*---------------------------------------------------------------*/

#include "fonctions_robot.h"
#include "fonctions_Moteurs.h"
Servo sg, sd;

float c1distance = 100;//on met par défaut une valeur non critique pour ne pas entrer
float c2distance = 100;//dans un if dès la mise en marche du robot
float c3distance = 30;
boolean obstacle;

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

  //déclaration des interruptions
  attachInterrupt(digitalPinToInterrupt(3), arretUrgence, RISING);//quand pin 3 passe de état bas à état haut, execute arretUrgence

  //faire 3 bips pour annoncer départ du robot
  bipInitialisation();
  c1distance = lectureCapteurAvant();
  c2distance = lectureCapteurAvantGauche();
  c3distance = lectureCapteurLateral();

  delay (1000);

  //on fait avancer le robot tout droit
  avancer(sd, sg, 1580);
}//fin setup

void loop() {

  //on déclenche les capteurs et on lit leurs valeurs en continu
  //tant qu'aucun capteur ne détecte une distance critique
  c1distance = lectureCapteurAvant();
  c2distance = lectureCapteurAvantGauche();
  c3distance = lectureCapteurLateral();

  //affiche valeur distance dans terminal
  affichageDansLeTerminal(c1distance, c2distance, c3distance);

  /*A FAIRE : ARRET D'uRgEnCeeee*/
  
  //evitement d'obstacle PRIORITAIRE
  if (c1distance < 40 && c1distance > 5 ) { //pour eviter les valeurs extremes en cas de non detection de mur
    Serial.println("ARRET D'URGENCE ACTIVE");
    //retour sonore
    //bip();
    //arret
    arretTotal(sg, sd, 500);
    //tourne à droite pour éviter obstacle (tourne à 90° a droite)
    sg.writeMicroseconds(1300);
    sd.writeMicroseconds(1600);
    delay(650);
    //on remet le robot droit en marche avant
    avancer(sg, sd, 1580);
    while (c3distance < 100 || c1distance < 50) { //tant qu'il y a l'obstacle, le robot roule droit vers l'avant
      c1distance = lectureCapteurAvant();
      c3distance = lectureCapteurLateral();
    }
  }
  
  //pour longer le mur intervalle gauche
  else if ((c3distance < 30 && c3distance != 0) /*|| (c2distance < 60 && c2distance != 0)*/) {
    //retour sonore
    //bip();
    //tourne a droite car trop prêt du mur
    //jusqu'à revenir dans le bon intervalle
    sd.writeMicroseconds(1680);
    delay(150);
    //on remet le robot droit en marche avant
    sd.writeMicroseconds(1580);
    delay(150);
  }

  //pour longer le mur intervalle droit
  else if (c3distance > 40 && c3distance < 60 ) {
    //retour sonore
    //bip();
    //tourne a gauche car trop loin du mur
    sg.writeMicroseconds(1680);
    delay(150);
    //on remet le robot droit en marche avant
    sg.writeMicroseconds(1580);
    delay(150);
  }

  //si "plus de mur à gauche" alors tourne de 90° à gauche
  else if (c3distance == 0 || c3distance >= 60) {
    //retour sonore
    //bip();
    //arret
    arretTotal(sg, sd, 1600);
    //tourne à gauche pour relonger le du mur (tourne à 90° a gauche)
    sd.writeMicroseconds(1500);
    sg.writeMicroseconds(1700);
    delay(900);
    //on remet le robot droit en marche avant
    avancer(sg, sd, 1580);
    /*delay(2000);//on fait avancer le robot pdt 2s avant de reprendre les mesures des capteurs
                //pour éviter de remesurer une "absence de mur" et partir dans un cas non désiré
                //(tourne en rond en l'infini)*/
    while (c3distance > 30 && c1distance > 50) {
      c1distance = lectureCapteurAvant();
      c3distance = lectureCapteurLateral();
    }
    while (c3distance < 30 && c1distance > 50) {
      c1distance = lectureCapteurAvant();
      c3distance = lectureCapteurLateral();
    }
  }
}//fin loop

//interruptions
void arretUrgence(void){
  arretTotal(sg, sd, 500);
}

//problemes
//peut etre "conflit" entre c2 et c3 car ils regardent tous les 2 le meme mur
//utiliser millis() pour mesurer continuellement via c1 c2 c3?

//mamaaaaaa ouhouhouhouuuuuuuuuu
