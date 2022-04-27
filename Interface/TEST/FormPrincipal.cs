using System;

namespace TEST
{
    public partial class FormPrincipal : Form
    {
        private int mise_en_pause=0;
        public FormPrincipal()
        {
            InitializeComponent();
        }

        private void btnCartographier_Click(object sendser, EventArgs e)
        {
            //Si on click sur le bouton cartographier on lance la cartographie et tout ces objets apparaissent
            this.btnCartographier.Visible = false;

            //Creation des différents objets
            this.pgsBar = new System.Windows.Forms.ProgressBar();
            this.lblCartographie = new System.Windows.Forms.Label();
            this.btnRecommencer = new System.Windows.Forms.Button();
            this.btnPause = new System.Windows.Forms.Button();
            this.btnArret = new System.Windows.Forms.Button();
            this.btnEnregistrer = new System.Windows.Forms.Button();
            
            //INITIALISATION
            // 
            // pgsBar
            // 
            this.pgsBar.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            this.pgsBar.Location = new System.Drawing.Point(410, 430);
            this.pgsBar.Name = "pgsBar";
            this.pgsBar.Size = new System.Drawing.Size(465, 20);
            this.pgsBar.TabIndex = 4;
            // 
            // lblCartographie
            // 
            this.lblCartographie.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            this.lblCartographie.AutoSize = true;
            this.lblCartographie.Font = new System.Drawing.Font("Microsoft YaHei UI", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.lblCartographie.Location = new System.Drawing.Point(284, 431);
            this.lblCartographie.Name = "lblCartographie";
            this.lblCartographie.Size = new System.Drawing.Size(120, 19);
            this.lblCartographie.TabIndex = 0;
            this.lblCartographie.Text = "Cartographie : ";
            // 
            // btnRecommencer
            // 
            this.btnRecommencer.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnRecommencer.BackColor = System.Drawing.Color.Transparent;
            this.btnRecommencer.BackgroundImage = global::PFR3.Properties.Resources.recommencer;
            this.btnRecommencer.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnRecommencer.ForeColor = System.Drawing.Color.Transparent;
            this.btnRecommencer.Location = new System.Drawing.Point(455, 458);
            this.btnRecommencer.Name = "btnRecommencer";
            this.btnRecommencer.Size = new System.Drawing.Size(50, 52);
            this.btnRecommencer.TabIndex = 5;
            this.btnRecommencer.UseVisualStyleBackColor = false;
            // 
            // btnPause
            // 
            this.btnPause.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnPause.BackColor = System.Drawing.Color.Transparent;
            this.btnPause.BackgroundImage = global::PFR3.Properties.Resources.pause;
            this.btnPause.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnPause.Location = new System.Drawing.Point(528, 458);
            this.btnPause.Name = "btnPause";
            this.btnPause.Size = new System.Drawing.Size(60, 52);
            this.btnPause.TabIndex = 6;
            this.btnPause.UseVisualStyleBackColor = false;
            this.btnPause.Click += new System.EventHandler(this.btnPause_Click);
            // 
            // btnArret
            // 
            this.btnArret.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnArret.BackColor = System.Drawing.Color.Transparent;
            this.btnArret.BackgroundImage = global::PFR3.Properties.Resources.arret;
            this.btnArret.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnArret.Location = new System.Drawing.Point(609, 458);
            this.btnArret.Name = "btnArret";
            this.btnArret.Size = new System.Drawing.Size(57, 52);
            this.btnArret.TabIndex = 7;
            this.btnArret.UseVisualStyleBackColor = false;
            // 
            // btnEnregistrer
            // 
            this.btnEnregistrer.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
            | System.Windows.Forms.AnchorStyles.Left)
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnEnregistrer.BackColor = System.Drawing.Color.YellowGreen;
            this.btnEnregistrer.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnEnregistrer.Font = new System.Drawing.Font("Microsoft YaHei", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.btnEnregistrer.ForeColor = System.Drawing.Color.Black;
            this.btnEnregistrer.Location = new System.Drawing.Point(825, 489);
            this.btnEnregistrer.Name = "btnEnregistrer";
            this.btnEnregistrer.Size = new System.Drawing.Size(114, 33);
            this.btnEnregistrer.TabIndex = 8;
            this.btnEnregistrer.Text = "Enregistrer";
            this.btnEnregistrer.UseVisualStyleBackColor = false;

            //ajout des composants au form courant
            this.Controls.Add(this.btnEnregistrer);
            this.Controls.Add(this.btnArret);
            this.Controls.Add(this.btnPause);
            this.Controls.Add(this.btnRecommencer);
            this.Controls.Add(this.lblCartographie);
            this.Controls.Add(this.pgsBar);
        }

        private void btnPause_Click(object sender, EventArgs e)
        {
            if (mise_en_pause % 2 == 0)
            {
                this.btnPause.BackgroundImage = global::PFR3.Properties.Resources.jouer;
            }
            else
            {
                this.btnPause.BackgroundImage = global::PFR3.Properties.Resources.pause;
            }
            mise_en_pause++;
        }
    }
}