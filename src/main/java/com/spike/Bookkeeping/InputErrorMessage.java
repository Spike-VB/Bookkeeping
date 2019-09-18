package com.spike.Bookkeeping;

import java.awt.*;
import javax.swing.JFrame;

public class InputErrorMessage {
	public InputErrorMessage() {
		JFrame frame = new JFrame("Message");
		frame.setBounds(700, 300, 300, 200);
		Label message = new Label("Insert an amount.", Label.CENTER);
		Font font = new Font("Default", Font.PLAIN, 22);
		message.setFont(font);
		frame.getContentPane().add(BorderLayout.CENTER, message);
		frame.setVisible(true);
	}
}
