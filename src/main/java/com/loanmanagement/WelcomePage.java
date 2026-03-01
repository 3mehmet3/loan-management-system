package com.loanmanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class WelcomePage extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        UITheme.applyTheme();
        EventQueue.invokeLater(() -> {
            try {
                DatabaseManager.getInstance(); // DB'yi başlat
                new WelcomePage().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WelcomePage() {
        setTitle("Yapı Kredi - Kredi Yönetim Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 480);
        setResizable(false);
        UITheme.centerFrame(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.PRIMARY_BLUE);
        setContentPane(mainPanel);

        // --- ÜST LOGO ALANI ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UITheme.PRIMARY_BLUE);
        topPanel.setBorder(new EmptyBorder(40, 0, 20, 0));

        // Logo
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icon = UITheme.loadIcon("/images/yapi-ve-kredi--600.png");
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(220, 60, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaled));
        } else {
            logoLabel.setText("YAPI KREDİ");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            logoLabel.setForeground(UITheme.WHITE);
        }
        topPanel.add(logoLabel, BorderLayout.CENTER);

        JLabel altBaslik = new JLabel("KREDİ YÖNETİM SİSTEMİ", SwingConstants.CENTER);
        altBaslik.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        altBaslik.setForeground(new Color(180, 200, 255));
        altBaslik.setBorder(new EmptyBorder(8, 0, 0, 0));
        topPanel.add(altBaslik, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- ORTA HOŞGELDINIZ ---
        JLabel hosgeldiniz = new JLabel("Lütfen giriş türünüzü seçiniz", SwingConstants.CENTER);
        hosgeldiniz.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        hosgeldiniz.setForeground(new Color(200, 215, 255));
        hosgeldiniz.setBorder(new EmptyBorder(10, 0, 25, 0));
        mainPanel.add(hosgeldiniz, BorderLayout.CENTER);

        // --- ALT BUTONLAR ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        buttonPanel.setBackground(UITheme.PRIMARY_BLUE);
        buttonPanel.setBorder(new EmptyBorder(0, 60, 60, 60));

        JButton btnPersonel = createLoginButton("👤", "PERSONEL", "Kredi başvurusu yapın");
        JButton btnMudur    = createLoginButton("🏢", "ŞUBE MÜDÜRÜ", "Başvuruları onaylayın");
        JButton btnGm       = createLoginButton("⭐", "GENEL MÜDÜR", "Sistemi yönetin");

        btnPersonel.addActionListener(e -> {
            new LoginPage("personel").setVisible(true);
            dispose();
        });

        btnMudur.addActionListener(e -> {
            new LoginPage("mudur").setVisible(true);
            dispose();
        });

        btnGm.addActionListener(e -> {
            new LoginPage("gm").setVisible(true);
            dispose();
        });

        buttonPanel.add(btnPersonel);
        buttonPanel.add(btnMudur);
        buttonPanel.add(btnGm);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createLoginButton(String emoji, String title, String subtitle) {
        JButton btn = new JButton("<html><center>" + emoji + "<br><b>" + title + "</b><br><small>" + subtitle + "</small></center></html>");
        btn.setFont(UITheme.FONT_REGULAR);
        btn.setBackground(new Color(0, 45, 130));
        btn.setForeground(UITheme.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 90));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(UITheme.ACCENT_BLUE); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(0, 45, 130)); }
        });

        return btn;
    }
}
