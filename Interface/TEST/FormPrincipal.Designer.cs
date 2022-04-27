namespace TEST
{
    partial class FormPrincipal
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.grpBoxEtats = new System.Windows.Forms.GroupBox();
            this.lblConnexion = new System.Windows.Forms.Label();
            this.lblNom = new System.Windows.Forms.Label();
            this.lblCollision = new System.Windows.Forms.Label();
            this.lblVitesse = new System.Windows.Forms.Label();
            this.pnlCartographie = new System.Windows.Forms.Panel();
            this.btnCartographier = new System.Windows.Forms.Button();
            this.btnAvancer = new System.Windows.Forms.Button();
            this.btnGauche = new System.Windows.Forms.Button();
            this.btnDroite = new System.Windows.Forms.Button();
            this.grpBoxEtats.SuspendLayout();
            this.SuspendLayout();
            // 
            // grpBoxEtats
            // 
            this.grpBoxEtats.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.grpBoxEtats.BackColor = System.Drawing.SystemColors.Control;
            this.grpBoxEtats.Controls.Add(this.lblConnexion);
            this.grpBoxEtats.Controls.Add(this.lblNom);
            this.grpBoxEtats.Controls.Add(this.lblCollision);
            this.grpBoxEtats.Controls.Add(this.lblVitesse);
            this.grpBoxEtats.Font = new System.Drawing.Font("Microsoft YaHei Light", 9F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point);
            this.grpBoxEtats.Location = new System.Drawing.Point(40, 279);
            this.grpBoxEtats.Name = "grpBoxEtats";
            this.grpBoxEtats.Size = new System.Drawing.Size(183, 213);
            this.grpBoxEtats.TabIndex = 2;
            this.grpBoxEtats.TabStop = false;
            this.grpBoxEtats.Text = "Etats du robot";
            // 
            // lblConnexion
            // 
            this.lblConnexion.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.lblConnexion.AutoSize = true;
            this.lblConnexion.Font = new System.Drawing.Font("Microsoft YaHei UI Light", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.lblConnexion.Location = new System.Drawing.Point(6, 71);
            this.lblConnexion.Name = "lblConnexion";
            this.lblConnexion.Size = new System.Drawing.Size(90, 20);
            this.lblConnexion.TabIndex = 2;
            this.lblConnexion.Text = "Connexion :";
            // 
            // lblNom
            // 
            this.lblNom.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.lblNom.AutoSize = true;
            this.lblNom.Font = new System.Drawing.Font("Microsoft YaHei UI Light", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.lblNom.Location = new System.Drawing.Point(6, 34);
            this.lblNom.Name = "lblNom";
            this.lblNom.Size = new System.Drawing.Size(50, 20);
            this.lblNom.TabIndex = 1;
            this.lblNom.Text = "Nom :";
            // 
            // lblCollision
            // 
            this.lblCollision.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.lblCollision.AutoSize = true;
            this.lblCollision.Font = new System.Drawing.Font("Microsoft YaHei UI Light", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.lblCollision.Location = new System.Drawing.Point(6, 163);
            this.lblCollision.Name = "lblCollision";
            this.lblCollision.Size = new System.Drawing.Size(76, 20);
            this.lblCollision.TabIndex = 4;
            this.lblCollision.Text = "Collision : ";
            // 
            // lblVitesse
            // 
            this.lblVitesse.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.lblVitesse.AutoSize = true;
            this.lblVitesse.Font = new System.Drawing.Font("Microsoft YaHei UI Light", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.lblVitesse.Location = new System.Drawing.Point(6, 114);
            this.lblVitesse.Name = "lblVitesse";
            this.lblVitesse.Size = new System.Drawing.Size(63, 20);
            this.lblVitesse.TabIndex = 3;
            this.lblVitesse.Text = "Vitesse :";
            // 
            // pnlCartographie
            // 
            this.pnlCartographie.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.pnlCartographie.BackColor = System.Drawing.SystemColors.ControlLightLight;
            this.pnlCartographie.Location = new System.Drawing.Point(275, 25);
            this.pnlCartographie.Name = "pnlCartographie";
            this.pnlCartographie.Size = new System.Drawing.Size(600, 399);
            this.pnlCartographie.TabIndex = 3;
            // 
            // btnCartographier
            // 
            this.btnCartographier.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnCartographier.BackColor = System.Drawing.SystemColors.Control;
            this.btnCartographier.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnCartographier.Font = new System.Drawing.Font("Microsoft YaHei", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.btnCartographier.Location = new System.Drawing.Point(509, 442);
            this.btnCartographier.Name = "btnCartographier";
            this.btnCartographier.Size = new System.Drawing.Size(141, 40);
            this.btnCartographier.TabIndex = 9;
            this.btnCartographier.Text = "Cartographier";
            this.btnCartographier.UseVisualStyleBackColor = false;
            this.btnCartographier.Click += new System.EventHandler(this.btnCartographier_Click);
            // 
            // btnAvancer
            // 
            this.btnAvancer.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnAvancer.BackColor = System.Drawing.SystemColors.Control;
            this.btnAvancer.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnAvancer.Font = new System.Drawing.Font("Microsoft YaHei", 16.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.btnAvancer.Location = new System.Drawing.Point(100, 108);
            this.btnAvancer.Name = "btnAvancer";
            this.btnAvancer.Size = new System.Drawing.Size(50, 52);
            this.btnAvancer.TabIndex = 12;
            this.btnAvancer.Text = "^";
            this.btnAvancer.UseVisualStyleBackColor = false;
            // 
            // btnGauche
            // 
            this.btnGauche.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnGauche.BackColor = System.Drawing.SystemColors.Control;
            this.btnGauche.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnGauche.Font = new System.Drawing.Font("Microsoft YaHei", 16.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.btnGauche.Location = new System.Drawing.Point(46, 164);
            this.btnGauche.Name = "btnGauche";
            this.btnGauche.Size = new System.Drawing.Size(50, 54);
            this.btnGauche.TabIndex = 13;
            this.btnGauche.Text = "<";
            this.btnGauche.UseVisualStyleBackColor = false;
            // 
            // btnDroite
            // 
            this.btnDroite.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnDroite.BackColor = System.Drawing.SystemColors.Control;
            this.btnDroite.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnDroite.Font = new System.Drawing.Font("Microsoft YaHei", 16.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.btnDroite.Location = new System.Drawing.Point(153, 164);
            this.btnDroite.Name = "btnDroite";
            this.btnDroite.Size = new System.Drawing.Size(50, 54);
            this.btnDroite.TabIndex = 14;
            this.btnDroite.Text = ">";
            this.btnDroite.UseVisualStyleBackColor = false;
            // 
            // FormPrincipal
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(12F, 27F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(951, 534);
            this.Controls.Add(this.btnDroite);
            this.Controls.Add(this.btnGauche);
            this.Controls.Add(this.btnAvancer);
            this.Controls.Add(this.btnCartographier);
            this.Controls.Add(this.pnlCartographie);
            this.Controls.Add(this.grpBoxEtats);
            this.Font = new System.Drawing.Font("Microsoft YaHei", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.Margin = new System.Windows.Forms.Padding(4);
            this.Name = "FormPrincipal";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Pilotage robotique";
            this.grpBoxEtats.ResumeLayout(false);
            this.grpBoxEtats.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion
        private GroupBox grpBoxEtats;
        private Label lblConnexion;
        private Label lblNom;
        private Label lblCollision;
        private Label lblVitesse;
        private Panel pnlCartographie;
        private ProgressBar pgsBar;
        private Label lblCartographie;
        private Button btnRecommencer;
        private Button btnPause;
        private Button btnArret;
        private Button btnEnregistrer;
        private Button btnCartographier;
        private Button btnAvancer;
        private Button btnGauche;
        private Button btnDroite;
    }
}