package com.loanmanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Tek bir giriş ekranı — Personel, Müdür ve Genel Müdür için kullanılır.
 * Eski 3 ayrı login sayfasının (PLoginPage, MLoginPage, GmLoginPage) yerine geçer.
 */
public class LoginPage extends JFrame {

    private static final long serialVersionUID = 1L;

    private final String userType; // "personel", "mudur", "gm"
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage(String userType) {
        this.userType = userType;

        setTitle("Yapı Kredi - Giriş");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 420);
        setResizable(false);
        UITheme.centerFrame(this);

        buildUI();
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.PRIMARY_BLUE);
        setContentPane(mainPanel);

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        headerPanel.setBackground(UITheme.PRIMARY_BLUE);
        headerPanel.setBorder(new EmptyBorder(30, 0, 20, 0));

        JLabel logoLabel = new JLabel(getRoleEmoji() + " " + getRoleTitle(), SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(UITheme.WHITE);

        JLabel subLabel = new JLabel("Giriş Bilgilerinizi Giriniz", SwingConstants.CENTER);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLabel.setForeground(new Color(180, 200, 255));

        headerPanel.add(new JLabel()); // boşluk
        headerPanel.add(logoLabel);
        headerPanel.add(subLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- FORM ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;

        // Kullanıcı adı
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Kullanıcı Adı:"), gbc);

        usernameField = UITheme.createTextField();
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);

        // Şifre
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(UITheme.createLabel("Şifre:"), gbc);

        passwordField = UITheme.createPasswordField();
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        // Butonlar
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setBackground(UITheme.WHITE);

        JButton backBtn  = UITheme.createSecondaryButton("← GERİ");
        JButton loginBtn = UITheme.createPrimaryButton("GİRİŞ YAP →");

        backBtn.addActionListener(e -> {
            new WelcomePage().setVisible(true);
            dispose();
        });

        loginBtn.addActionListener(e -> handleLogin());

        // Enter tuşu ile giriş
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin();
            }
        });

        btnPanel.add(backBtn);
        btnPanel.add(loginBtn);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 0, 5);
        formPanel.add(btnPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Alt bilgi
        JLabel footerLabel = new JLabel("© 2024 Yapı Kredi Bankası A.Ş.", SwingConstants.CENTER);
        footerLabel.setFont(UITheme.FONT_SMALL);
        footerLabel.setForeground(new Color(180, 200, 255));
        footerLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        footerLabel.setBackground(UITheme.PRIMARY_BLUE);
        footerLabel.setOpaque(true);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DatabaseManager db = DatabaseManager.getInstance();
        boolean valid = false;

        switch (userType) {
            case "personel": valid = db.personelGirisKontrol(username, password); break;
            case "mudur":    valid = db.mudurGirisKontrol(username, password);    break;
            case "gm":       valid = db.patronGirisKontrol(username, password);   break;
        }

        if (valid) {
            JOptionPane.showMessageDialog(this, "Hoş geldiniz, " + username.toUpperCase() + "!", "Giriş Başarılı", JOptionPane.INFORMATION_MESSAGE);
            openDashboard(username);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Kullanıcı adı veya şifre hatalı!", "Giriş Hatası", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    private void openDashboard(String username) {
        switch (userType) {
            case "personel": new PersonelDashboard(username).setVisible(true); break;
            case "mudur":    new MudurDashboard(username).setVisible(true);    break;
            case "gm":       new GenelMudurDashboard(username).setVisible(true); break;
        }
    }

    private String getRoleTitle() {
        switch (userType) {
            case "personel": return "PERSONEL GİRİŞİ";
            case "mudur":    return "ŞUBE MÜDÜRÜ GİRİŞİ";
            case "gm":       return "GENEL MÜDÜR GİRİŞİ";
            default:         return "GİRİŞ";
        }
    }

    private String getRoleEmoji() {
        switch (userType) {
            case "personel": return "👤";
            case "mudur":    return "🏢";
            case "gm":       return "⭐";
            default:         return "🔐";
        }
    }
}
