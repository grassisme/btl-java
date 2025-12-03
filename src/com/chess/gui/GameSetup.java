package com.chess.gui;

import com.chess.gui.Table.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameSetup extends JDialog {
    

    GameSetup(final JFrame frame,
              final boolean modal) {
        super(frame, modal);

        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        
        JLabel title = new JLabel("Game Options");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        getContentPane().add(myPanel);
        myPanel.add(title);
        myPanel.add(new JSeparator());
        myPanel.add(new JSeparator());

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("Start Game");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameSetup.this.setVisible(false);

                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                GameSetup.this.setVisible(false);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        myPanel.add(buttonPanel);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }
}
