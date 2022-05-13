int pin_LED = 13;    // port numérique associé à la LED intégrée
volatile bool a = true; //volatile pour pouvoir modifier la variable depuis une interruption

void setup() {
  //initialisation de la communication avec le port série
  Serial.begin(9600);
  // réglage du port de la LED en mode SORTIE
  pinMode(pin_LED, OUTPUT);
  // Création de l'interruption
  attachInterrupt(0, blink, CHANGE);

  pinMode(2, INPUT_PULLUP);
  delay(500);
}

void loop() {
  while (a) {
    Serial.println("A EST VRAI");
    delay(5000);
  }
  if (a == false) Serial.println("A EST FAUX");
}

void blink() { //permet de sortir du while mais attends quand-même la fin du delay
  a = false;
}
