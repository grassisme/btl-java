package com.chess.gui;

import com.chess.gui.Table.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameSetup extends JDialog {

    // Đã xóa các biến PlayerType và SearchDepth vì không còn AI

    GameSetup(final JFrame frame,
              final boolean modal) {
        super(frame, modal);

        final JPanel myPanel = new JPanel(new GridLayout(0, 1));

        // Tạo giao diện đơn giản thông báo chế độ chơi
        JLabel title = new JLabel("Game Options");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel modeLabel = new JLabel("Mode: Player vs Player (Hot Seat)");
        JLabel infoLabel = new JLabel("Play moves alternatively on this device.");
        JLabel infoLabel2 = new JLabel("Board will rotate automatically.");

        // Thêm các thành phần vào Panel
        getContentPane().add(myPanel);
        myPanel.add(title);
        myPanel.add(new JSeparator());
        myPanel.add(modeLabel);
        myPanel.add(infoLabel);
        myPanel.add(infoLabel2);
        myPanel.add(new JSeparator());

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("Start Game");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Mặc định cả 2 phe là HUMAN, không cần check radio button nữa
                GameSetup.this.setVisible(false);

                // Thông báo cho Table để cập nhật trạng thái
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

    void promptUser() {
        setVisible(true);
        repaint();
    }

    // Các phương thức getter liên quan đến AI (isAIPlayer, getSearchDepth...) đã được xóa bỏ hoàn toàn
    // vì mặc định game bây giờ luôn là Human vs Human.
}