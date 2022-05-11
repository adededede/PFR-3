using System.Windows.Forms;

namespace ProjetFilRouge3
{
    partial class FromPrincipal
    {
        /// <summary>
        /// Variable nécessaire au concepteur.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Nettoyage des ressources utilisées.
        /// </summary>
        /// <param name="disposing">true si les ressources managées doivent être supprimées ; sinon, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Code généré par le Concepteur Windows Form

        /// <summary>
        /// Méthode requise pour la prise en charge du concepteur - ne modifiez pas
        /// le contenu de cette méthode avec l'éditeur de code.
        /// </summary>
        private void InitializeComponent()
        {
            this.grpBoxEtats = new System.Windows.Forms.GroupBox();
            this.lblPosition = new System.Windows.Forms.Label();
            this.lblNom = new System.Windows.Forms.Label();
            this.lblDistance = new System.Windows.Forms.Label();
            this.lblVitesse = new System.Windows.Forms.Label();
            this.btnCartographier = new System.Windows.Forms.Button();
            this.btnAvancer = new System.Windows.Forms.Button();
            this.btnGauche = new System.Windows.Forms.Button();
            this.btnDroit = new System.Windows.Forms.Button();
            this.pctrCartographie = new System.Windows.Forms.PictureBox();
            this.menuStrip = new System.Windows.Forms.MenuStrip();
            this.menuItemBluetooth = new System.Windows.Forms.ToolStripMenuItem();
            this.sousMenuItemActiver = new System.Windows.Forms.ToolStripMenuItem();
            this.sousMenuItemInterface = new System.Windows.Forms.ToolStripMenuItem();
            this.menuItemMode = new System.Windows.Forms.ToolStripMenuItem();
            this.sousMenuItemAutomatique = new System.Windows.Forms.ToolStripMenuItem();
            this.sousMenuItemManuel = new System.Windows.Forms.ToolStripMenuItem();
            this.btnReculer = new System.Windows.Forms.Button();
            this.grpBoxEtats.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pctrCartographie)).BeginInit();
            this.menuStrip.SuspendLayout();
            this.SuspendLayout();
            // 
            // grpBoxEtats
            // 
            this.grpBoxEtats.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.grpBoxEtats.BackColor = System.Drawing.SystemColors.ButtonHighlight;
            this.grpBoxEtats.Controls.Add(this.lblPosition);
            this.grpBoxEtats.Controls.Add(this.lblNom);
            this.grpBoxEtats.Controls.Add(this.lblDistance);
            this.grpBoxEtats.Controls.Add(this.lblVitesse);
            this.grpBoxEtats.Font = new System.Drawing.Font("Microsoft YaHei Light", 9F, System.Drawing.FontStyle.Italic);
            this.grpBoxEtats.Location = new System.Drawing.Point(40, 279);
            this.grpBoxEtats.Name = "grpBoxEtats";
            this.grpBoxEtats.Size = new System.Drawing.Size(183, 213);
            this.grpBoxEtats.TabIndex = 2;
            this.grpBoxEtats.TabStop = false;
            this.grpBoxEtats.Text = "Etats du robot";
            // 
            // lblPosition
            // 
            this.lblPosition.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.lblPosition.AutoSize = true;
            this.lblPosition.Font = new System.Drawing.Font("Microsoft YaHei UI Light", 9F);
            this.lblPosition.Location = new System.Drawing.Point(6, 71);
            this.lblPosition.Name = "lblPosition";
            this.lblPosition.Size = new System.Drawing.Size(70, 20);
            this.lblPosition.TabIndex = 2;
            this.lblPosition.Text = "Position :";
            // 
            // lblNom
            // 
            this.lblNom.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.lblNom.AutoSize = true;
            this.lblNom.Font = new System.Drawing.Font("Microsoft YaHei UI Light", 9F);
            this.lblNom.Location = new System.Drawing.Point(6, 34);
            this.lblNom.Name = "lblNom";
            this.lblNom.Size = new System.Drawing.Size(50, 20);
            this.lblNom.TabIndex = 1;
            this.lblNom.Text = "Nom :";
            // 
            // lblDistance
            // 
            this.lblDistance.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.lblDistance.AutoSize = true;
            this.lblDistance.Font = new System.Drawing.Font("Microsoft YaHei UI Light", 9F);
            this.lblDistance.Location = new System.Drawing.Point(6, 163);
            this.lblDistance.Name = "lblDistance";
            this.lblDistance.Size = new System.Drawing.Size(78, 20);
            this.lblDistance.TabIndex = 4;
            this.lblDistance.Text = "Distance : ";
            // 
            // lblVitesse
            // 
            this.lblVitesse.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.lblVitesse.AutoSize = true;
            this.lblVitesse.Font = new System.Drawing.Font("Microsoft YaHei UI Light", 9F);
            this.lblVitesse.Location = new System.Drawing.Point(6, 114);
            this.lblVitesse.Name = "lblVitesse";
            this.lblVitesse.Size = new System.Drawing.Size(63, 20);
            this.lblVitesse.TabIndex = 3;
            this.lblVitesse.Text = "Vitesse :";
            // 
            // btnCartographier
            // 
            this.btnCartographier.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnCartographier.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(249)))), ((int)(((byte)(248)))), ((int)(((byte)(232)))));
            this.btnCartographier.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnCartographier.FlatAppearance.BorderSize = 0;
            this.btnCartographier.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnCartographier.Font = new System.Drawing.Font("Microsoft YaHei", 10.2F, System.Drawing.FontStyle.Bold);
            this.btnCartographier.Location = new System.Drawing.Point(510, 452);
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
            this.btnAvancer.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(249)))), ((int)(((byte)(248)))), ((int)(((byte)(232)))));
            this.btnAvancer.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnAvancer.FlatAppearance.BorderSize = 0;
            this.btnAvancer.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnAvancer.Font = new System.Drawing.Font("Microsoft YaHei", 16.2F, System.Drawing.FontStyle.Bold);
            this.btnAvancer.Location = new System.Drawing.Point(100, 57);
            this.btnAvancer.Name = "btnAvancer";
            this.btnAvancer.Size = new System.Drawing.Size(50, 52);
            this.btnAvancer.TabIndex = 12;
            this.btnAvancer.Text = "^";
            this.btnAvancer.UseVisualStyleBackColor = false;
            this.btnAvancer.Visible = false;
            this.btnAvancer.Click += new System.EventHandler(this.btnAvancer_Click);
            // 
            // btnGauche
            // 
            this.btnGauche.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnGauche.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(249)))), ((int)(((byte)(248)))), ((int)(((byte)(232)))));
            this.btnGauche.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnGauche.FlatAppearance.BorderSize = 0;
            this.btnGauche.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnGauche.Font = new System.Drawing.Font("Microsoft YaHei", 16.2F, System.Drawing.FontStyle.Bold);
            this.btnGauche.Location = new System.Drawing.Point(46, 113);
            this.btnGauche.Name = "btnGauche";
            this.btnGauche.Size = new System.Drawing.Size(50, 54);
            this.btnGauche.TabIndex = 13;
            this.btnGauche.Text = "<";
            this.btnGauche.UseVisualStyleBackColor = false;
            this.btnGauche.Visible = false;
            // 
            // btnDroit
            // 
            this.btnDroit.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnDroit.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(249)))), ((int)(((byte)(248)))), ((int)(((byte)(232)))));
            this.btnDroit.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnDroit.FlatAppearance.BorderSize = 0;
            this.btnDroit.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnDroit.Font = new System.Drawing.Font("Microsoft YaHei", 16.2F, System.Drawing.FontStyle.Bold);
            this.btnDroit.Location = new System.Drawing.Point(150, 115);
            this.btnDroit.Name = "btnDroit";
            this.btnDroit.Size = new System.Drawing.Size(50, 54);
            this.btnDroit.TabIndex = 14;
            this.btnDroit.Text = ">";
            this.btnDroit.UseVisualStyleBackColor = false;
            this.btnDroit.Visible = false;
            // 
            // pctrCartographie
            // 
            this.pctrCartographie.BackColor = System.Drawing.SystemColors.ControlLightLight;
            this.pctrCartographie.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.pctrCartographie.BackgroundImage = global::ProjetFilRouge3.Properties.Resources.blanc;
            this.pctrCartographie.Location = new System.Drawing.Point(288, 40);
            this.pctrCartographie.Name = "pctrCartographie";
            this.pctrCartographie.Size = new System.Drawing.Size(597, 396);
            this.pctrCartographie.TabIndex = 0;
            this.pctrCartographie.TabStop = false;
            // 
            // menuStrip
            // 
            this.menuStrip.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(251)))), ((int)(((byte)(242)))));
            this.menuStrip.ImageScalingSize = new System.Drawing.Size(20, 20);
            this.menuStrip.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.menuItemBluetooth,
            this.menuItemMode});
            this.menuStrip.Location = new System.Drawing.Point(0, 0);
            this.menuStrip.Name = "menuStrip";
            this.menuStrip.Size = new System.Drawing.Size(951, 28);
            this.menuStrip.TabIndex = 15;
            this.menuStrip.Text = "menuStrip1";
            // 
            // menuItemBluetooth
            // 
            this.menuItemBluetooth.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.sousMenuItemActiver,
            this.sousMenuItemInterface});
            this.menuItemBluetooth.Name = "menuItemBluetooth";
            this.menuItemBluetooth.Size = new System.Drawing.Size(88, 24);
            this.menuItemBluetooth.Text = "Bluetooth";
            // 
            // sousMenuItemActiver
            // 
            this.sousMenuItemActiver.Name = "sousMenuItemActiver";
            this.sousMenuItemActiver.Size = new System.Drawing.Size(230, 26);
            this.sousMenuItemActiver.Text = "Activer";
            this.sousMenuItemActiver.Click += new System.EventHandler(this.sousMenuItemActiver_Click);
            // 
            // sousMenuItemInterface
            // 
            this.sousMenuItemInterface.Name = "sousMenuItemInterface";
            this.sousMenuItemInterface.Size = new System.Drawing.Size(230, 26);
            this.sousMenuItemInterface.Text = "Interfaces disponible";
            // 
            // menuItemMode
            // 
            this.menuItemMode.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.sousMenuItemAutomatique,
            this.sousMenuItemManuel});
            this.menuItemMode.Name = "menuItemMode";
            this.menuItemMode.Size = new System.Drawing.Size(62, 24);
            this.menuItemMode.Text = "Mode";
            // 
            // sousMenuItemAutomatique
            // 
            this.sousMenuItemAutomatique.Name = "sousMenuItemAutomatique";
            this.sousMenuItemAutomatique.Image = global::ProjetFilRouge3.Properties.Resources.point;
            this.sousMenuItemAutomatique.Size = new System.Drawing.Size(179, 26);
            this.sousMenuItemAutomatique.Text = "Automatique";
            this.sousMenuItemAutomatique.Click += new System.EventHandler(this.sousMenuItemAutomatique_Click);
            // 
            // sousMenuItemManuel
            // 
            this.sousMenuItemManuel.Name = "sousMenuItemManuel";
            this.sousMenuItemManuel.Size = new System.Drawing.Size(179, 26);
            this.sousMenuItemManuel.Text = "Manuel";
            this.sousMenuItemManuel.Click += new System.EventHandler(this.sousMenuItemManuel_Click);
            // 
            // btnReculer
            // 
            this.btnReculer.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.btnReculer.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(249)))), ((int)(((byte)(248)))), ((int)(((byte)(232)))));
            this.btnReculer.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.btnReculer.FlatAppearance.BorderSize = 0;
            this.btnReculer.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnReculer.Font = new System.Drawing.Font("Microsoft YaHei", 16.2F);
            this.btnReculer.Location = new System.Drawing.Point(100, 180);
            this.btnReculer.Name = "btnReculer";
            this.btnReculer.Size = new System.Drawing.Size(50, 52);
            this.btnReculer.TabIndex = 16;
            this.btnReculer.Text = "v";
            this.btnReculer.UseVisualStyleBackColor = false;
            this.btnReculer.Visible = false;
            // 
            // FromPrincipal
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(12F, 27F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.ButtonHighlight;
            this.ClientSize = new System.Drawing.Size(951, 534);
            this.Controls.Add(this.btnReculer);
            this.Controls.Add(this.pctrCartographie);
            this.Controls.Add(this.btnDroit);
            this.Controls.Add(this.btnGauche);
            this.Controls.Add(this.btnAvancer);
            this.Controls.Add(this.btnCartographier);
            this.Controls.Add(this.grpBoxEtats);
            this.Controls.Add(this.menuStrip);
            this.Font = new System.Drawing.Font("Microsoft YaHei", 12F);
            this.MainMenuStrip = this.menuStrip;
            this.Margin = new System.Windows.Forms.Padding(4);
            this.Name = "FromPrincipal";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Pilotage robotique";
            this.Load += new System.EventHandler(this.FromPrincipal_Load);
            this.grpBoxEtats.ResumeLayout(false);
            this.grpBoxEtats.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pctrCartographie)).EndInit();
            this.menuStrip.ResumeLayout(false);
            this.menuStrip.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion
        private GroupBox grpBoxEtats;
        private Label lblPosition;
        private Label lblNom;
        private Label lblDistance;
        private Label lblVitesse;
        private Button btnCartographier;
        private Button btnAvancer;
        private Button btnGauche;
        private Button btnDroit;
        private PictureBox pctrCartographie;
        private MenuStrip menuStrip;
        private ToolStripMenuItem menuItemBluetooth;
        private ToolStripMenuItem sousMenuItemInterface;
        private ToolStripMenuItem sousMenuItemActiver;
        private ToolStripMenuItem menuItemMode;
        private ToolStripMenuItem sousMenuItemAutomatique;
        private ToolStripMenuItem sousMenuItemManuel;
        private Button btnReculer;
    }
}

