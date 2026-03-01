package com.loanmanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Genel Müdür ana ekranı.
 * Eski GmPage.java, GMKredi.java, Personeller_1.java ve Müdürler.java'nın yerine geçer.
 */
public class GenelMudurDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    private final String username;
    private JPanel contentArea;
    private CardLayout cardLayout;

    // Tablo modelleri
    private DefaultTableModel basvuruModel;
    private DefaultTableModel personelModel;
    private DefaultTableModel mudurModel;

    public GenelMudurDashboard(String username) {
        this.username = username;

        setTitle("Yapı Kredi - Genel Müdür Yönetim Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 660);
        setMinimumSize(new Dimension(950, 580));
        UITheme.centerFrame(this);

        buildUI();
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.PANEL_BG);
        setContentPane(mainPanel);

        // HEADER
        JPanel header = UITheme.createHeaderPanel("⭐  GENEL MÜDÜR YÖNETİM PANELİ");
        JLabel userLabel = UITheme.createWhiteLabel("Hoş geldiniz, Sayın " + username.toUpperCase());
        userLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
        header.add(userLabel, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);

        // SIDEBAR
        JPanel sidebar = UITheme.createSidebarPanel();

        JLabel menuTitle = UITheme.createWhiteLabel("MENÜ");
        menuTitle.setFont(UITheme.FONT_SMALL);
        menuTitle.setForeground(UITheme.SILVER);
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        sidebar.add(menuTitle);

        JButton btnBasvurular  = UITheme.createSidebarButton("📋  Kredi Başvuruları");
        JButton btnPersoneller = UITheme.createSidebarButton("👥  Personeller");
        JButton btnMudurler    = UITheme.createSidebarButton("🏢  Müdürler");
        JButton btnCikis       = UITheme.createSidebarButton("🚪  Çıkış");

        sidebar.add(btnBasvurular);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnPersoneller);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnMudurler);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnCikis);

        mainPanel.add(sidebar, BorderLayout.WEST);

        // CONTENT
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UITheme.PANEL_BG);
        contentArea.add(buildBasvurularPanel(), "basvurular");
        contentArea.add(buildPersonellerPanel(), "personeller");
        contentArea.add(buildMudurlerPanel(), "mudurler");
        mainPanel.add(contentArea, BorderLayout.CENTER);

        // Aksiyonlar
        btnBasvurular.addActionListener(e -> {
            loadBasvurular();
            cardLayout.show(contentArea, "basvurular");
        });
        btnPersoneller.addActionListener(e -> {
            loadPersoneller();
            cardLayout.show(contentArea, "personeller");
        });
        btnMudurler.addActionListener(e -> {
            loadMudurler();
            cardLayout.show(contentArea, "mudurler");
        });
        btnCikis.addActionListener(e -> {
            new WelcomePage().setVisible(true);
            dispose();
        });
    }

    // ========== BAŞVURULAR PANELİ ==========

    private JPanel buildBasvurularPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.PANEL_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Tüm Kredi Başvuruları");
        title.setFont(UITheme.FONT_HEADER);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"AD", "SOYAD", "MESLEK", "YILLIK GELİR", "KREDİ TÜRÜ", "VADE", "FAİZ", "DURUM"};
        basvuruModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable t = new JTable(basvuruModel);
        UITheme.styleTable(t);

        // Durum sütunu için renk
        t.getColumnModel().getColumn(7).setCellRenderer((tbl, value, selected, focused, row, col) -> {
            JLabel lbl = new JLabel(value != null ? value.toString() : "", SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setFont(UITheme.FONT_SMALL);
            String v = value != null ? value.toString() : "";
            if (v.equals("ONAYLANDI"))      { lbl.setBackground(new Color(200, 240, 200)); lbl.setForeground(UITheme.SUCCESS_GREEN); }
            else if (v.equals("ONAYLANMADI")) { lbl.setBackground(new Color(255, 220, 220)); lbl.setForeground(UITheme.DANGER_RED); }
            else                             { lbl.setBackground(new Color(255, 245, 200)); lbl.setForeground(new Color(150, 100, 0)); }
            if (selected) lbl.setBackground(UITheme.ACCENT_BLUE);
            return lbl;
        });

        JScrollPane scroll = new JScrollPane(t);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomPanel.setBackground(UITheme.PANEL_BG);

        JButton loadBtn   = UITheme.createPrimaryButton("🔄 Tümünü Yükle");
        JButton deleteBtn = UITheme.createDangerButton("🗑 Seçileni Sil");
        deleteBtn.setPreferredSize(new Dimension(150, 38));

        loadBtn.addActionListener(e -> loadBasvurular());
        deleteBtn.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row != -1) basvuruModel.removeRow(row);
            else JOptionPane.showMessageDialog(this, "Lütfen bir satır seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
        });

        bottomPanel.add(loadBtn);
        bottomPanel.add(deleteBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadBasvurular() {
        basvuruModel.setRowCount(0);
        List<String[]> list = DatabaseManager.getInstance().tumBasvurulariGetir();
        for (String[] row : list) basvuruModel.addRow(row);
    }

    // ========== PERSONELLER PANELİ ==========

    private JPanel buildPersonellerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.PANEL_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Personel Listesi");
        title.setFont(UITheme.FONT_HEADER);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"AD", "SOYAD", "DEPARTMAN", "TELEFON"};
        personelModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable t = new JTable(personelModel);
        UITheme.styleTable(t);

        JScrollPane scroll = new JScrollPane(t);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void loadPersoneller() {
        personelModel.setRowCount(0);
        List<String[]> list = DatabaseManager.getInstance().personelleriGetir();
        for (String[] row : list) personelModel.addRow(row);
    }

    // ========== MÜDÜRLER PANELİ ==========

    private JPanel buildMudurlerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.PANEL_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Şube Müdürleri");
        title.setFont(UITheme.FONT_HEADER);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"AD", "SOYAD", "ŞUBE", "İLETİŞİM"};
        mudurModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable t = new JTable(mudurModel);
        UITheme.styleTable(t);

        JScrollPane scroll = new JScrollPane(t);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void loadMudurler() {
        mudurModel.setRowCount(0);
        List<String[]> list = DatabaseManager.getInstance().mudurleriGetir();
        for (String[] row : list) mudurModel.addRow(row);
    }
}
