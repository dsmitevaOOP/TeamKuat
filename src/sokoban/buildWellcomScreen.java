public void buildWelcomeScreen(Graphics g) {
		g.setColor(new Color(250, 240, 170));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(new Color(0, 0, 0));
		g.drawString("Welcome to Socoban", 100, 20);
		g.drawString("To move worker press arrow keys:", 0, 60);
		g.drawString(
				"▲-move up key, ▼- move down key, ◄- move left key, ► - move right key",
				0, 80);
		g.drawString("To reset level press \"r\" key", 0, 100);
		g.drawString("To undo last move press \"u\" key", 0, 120);

	}
