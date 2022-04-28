using System;
using System.Collections;

namespace TEST
{
    public partial class FormPrincipal : Form
    {
        public FormPrincipal()
        {
            InitializeComponent();
        }

        private int mise_en_pause = 0;
        private Point[] points = new Point[100];
        private int index = 0;
        private void btnCartographier_Click(object sendser, EventArgs e)
        {
            mise_en_pause = 0;
            //Si on click sur le bouton cartographier on lance la cartographie et tout ces objets apparaissent
            this.btnCartographier.Visible = false;

            //Creation des différents objets
            ProgressBar pgsBar = new ProgressBar();
            Label lblCartographie = new Label();
            Button btnRecommencer = new Button();
            Button btnPause = new Button();
            Button btnArret = new Button();
            Button btnEnregistrer = new Button();
            
            //INITIALISATION
            // 
            // pgsBar
            // 
            pgsBar.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            pgsBar.Location = new System.Drawing.Point(410, 430);
            pgsBar.Name = "pgsBar";
            pgsBar.Size = new System.Drawing.Size(465, 20);
            pgsBar.TabIndex = 4;
            // 
            // lblCartographie
            // 
            lblCartographie.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            lblCartographie.AutoSize = true;
            lblCartographie.Font = new System.Drawing.Font("Microsoft YaHei UI", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            lblCartographie.Location = new System.Drawing.Point(284, 431);
            lblCartographie.Name = "lblCartographie";
            lblCartographie.Size = new System.Drawing.Size(120, 19);
            lblCartographie.TabIndex = 0;
            lblCartographie.Text = "Cartographie : ";
            // 
            // btnRecommencer
            // 
            btnRecommencer.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            btnRecommencer.BackColor = System.Drawing.Color.Transparent;
            btnRecommencer.BackgroundImage = global::PFR3.Properties.Resources.recommencer;
            btnRecommencer.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            btnRecommencer.FlatAppearance.BorderSize = 0;
            btnRecommencer.FlatAppearance.MouseDownBackColor = System.Drawing.Color.Transparent;
            btnRecommencer.FlatAppearance.MouseOverBackColor = System.Drawing.Color.Transparent;
            btnRecommencer.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            btnRecommencer.Location = new System.Drawing.Point(435, 455);
            btnRecommencer.Margin = new System.Windows.Forms.Padding(0);
            btnRecommencer.Name = "btnRecommencer";
            btnRecommencer.Size = new System.Drawing.Size(60, 63);
            btnRecommencer.UseVisualStyleBackColor = false;
            btnRecommencer.TabIndex = 5;
            btnRecommencer.Click += new System.EventHandler(this.btnRecommencer_Click);
            // 
            // btnPause
            // 
            btnPause.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            btnPause.BackColor = System.Drawing.Color.Transparent;
            btnPause.BackgroundImage = global::PFR3.Properties.Resources.pause;
            btnPause.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            btnPause.FlatAppearance.BorderSize = 0;
            btnPause.FlatAppearance.MouseDownBackColor = System.Drawing.Color.Transparent;
            btnPause.FlatAppearance.MouseOverBackColor = System.Drawing.Color.Transparent;
            btnPause.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            btnPause.Location = new System.Drawing.Point(508, 458);
            btnPause.Margin = new System.Windows.Forms.Padding(0);
            btnPause.Name = "btnPause";
            btnPause.Size = new System.Drawing.Size(70, 60);
            btnPause.UseVisualStyleBackColor = false;
            btnPause.TabIndex = 6;
            btnPause.Click += new System.EventHandler(this.btnPause_Click);
            // 
            // btnArret
            // 
            btnArret.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            btnArret.BackColor = System.Drawing.Color.Transparent;
            btnArret.BackgroundImage = global::PFR3.Properties.Resources.arret;
            btnArret.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            btnArret.FlatAppearance.BorderSize = 0;
            btnArret.FlatAppearance.MouseDownBackColor = System.Drawing.Color.Transparent;
            btnArret.FlatAppearance.MouseOverBackColor = System.Drawing.Color.Transparent;
            btnArret.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            btnArret.Location = new System.Drawing.Point(600, 460);
            btnArret.Margin = new System.Windows.Forms.Padding(0);
            btnArret.Name = "btnArret";
            btnArret.Size = new System.Drawing.Size(57, 52);
            btnArret.UseVisualStyleBackColor = false;
            btnArret.TabIndex = 7;
            btnArret.Click += new EventHandler(this.btnArret_Click);
            // 
            // btnEnregistrer
            // 
            btnEnregistrer.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            btnEnregistrer.BackColor = System.Drawing.Color.YellowGreen;
            btnEnregistrer.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            btnEnregistrer.Font = new System.Drawing.Font("Microsoft YaHei", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            btnEnregistrer.ForeColor = System.Drawing.Color.Black;
            btnEnregistrer.Location = new System.Drawing.Point(825, 489);
            btnEnregistrer.Name = "btnEnregistrer";
            btnEnregistrer.Size = new System.Drawing.Size(114, 33);
            btnEnregistrer.TabIndex = 8;
            btnEnregistrer.Text = "Enregistrer";
            btnEnregistrer.UseVisualStyleBackColor = false;
            btnEnregistrer.Click += new System.EventHandler(this.btnEnregistrer_Click);

            //ajout des composants au form courant
            this.Controls.Add(btnEnregistrer);
            this.Controls.Add(btnArret);
            this.Controls.Add(btnPause);
            this.Controls.Add(btnRecommencer);
            this.Controls.Add(lblCartographie);
            this.Controls.Add(pgsBar);
        }

        private void btnEnregistrer_Click(object sender, EventArgs e)
        {
            //On peut enregistrer la carte obtenue seulement si la cartograpie est terminé
            if(getProgressBar("pgsBar").Value >= 100)
            {
                this.pctrCartographie.Image.Save("d:\\carte.Jpeg", System.Drawing.Imaging.ImageFormat.Jpeg);
            }            
        }

        private void btnArret_Click(object sender, EventArgs e)
        {
            //suppression des composants du form courant
            this.Controls.Remove(getButton("btnEnregistrer"));
            this.Controls.Remove(getButton("btnArret"));
            this.Controls.Remove(getButton("btnPause"));
            this.Controls.Remove(getButton("btnRecommencer"));
            this.Controls.Remove(getLabel("lblCartographie"));
            this.Controls.Remove(getProgressBar("pgsBar"));

            this.btnCartographier.Visible = true;
        }

        private void btnRecommencer_Click(object sender, EventArgs e)
        {
            //remet la progress bar à 0
            getProgressBar("pgsBar").Value = 0;
            //rappelle la méthode de cartographie

        }

        private void btnPause_Click(object sender, EventArgs e)
        {
            if (mise_en_pause % 2 == 0)
            {
                getButton("btnPause").BackgroundImage = global::PFR3.Properties.Resources.jouer;
            }
            else
            {
                getButton("btnPause").BackgroundImage = global::PFR3.Properties.Resources.pause;
            }
            mise_en_pause++;
            this.dessiner_point(mise_en_pause, mise_en_pause + 1);
        }

        private Button getButton(String nom)
        {
            foreach (Button b in this.Controls.OfType<Button>())
            {
                if (b.Name == nom)
                {
                    return b;
                }
            }
            return null;
        }

        private Label getLabel(String nom)
        {
            foreach (Label l in this.Controls.OfType<Label>())
            {
                if (l.Name == nom)
                {
                    return l;
                }
            }
            return null;
        }

        private ProgressBar getProgressBar(String nom)
        {
            foreach (ProgressBar pb in this.Controls.OfType<ProgressBar>())
            {
                if (pb.Name == nom)
                {
                    return pb;
                }
            }
            return null;
        }

        private void dessiner_point( int x, int y)
        {
            //objet graphique
            this.points[this.index] = new Point(x, y);
            this.index++;
            if(this.points.Length >= 2)
            {
                Graphics.FromImage(this.pctrCartographie.BackgroundImage).DrawLine(new Pen(Color.Black, 3), this.points[this.index-1],this.points[this.index]);
                pctrCartographie.Refresh();
            }
            else
            {
                Graphics.FromImage(this.pctrCartographie.BackgroundImage).DrawImage(this.pctrCartographie.BackgroundImage, this.points[this.index]);
                pctrCartographie.Refresh();
            }
        }
    }
}