package com.loanmanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Şube Müdürü ana ekranı.
 * Eski MPage.java, MKredi.java ve Personeller.java'nın yerine geçer.
 */
public class MudurDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    private final String username;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> onayBox;
    private JPanel contentArea;
    private CardLayout cardLayout;

    public MudurDashboard(String username) {
        this.username = username;

        setTitle("Yapı Kredi - Şube Müdürü Yönetim Paneli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 640);
        setMinimumSize(new Dimension(900, 560));
        UITheme.centerFrame(this);

        buildUI();
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.PANEL_BG);
        setContentPane(mainPanel);

        // HEADER
        JPanel header = UITheme.createHeaderPanel("🏢  ŞUBE MÜDÜRÜ YÖNETİM PANELİ");
        JLabel userLabel = UITheme.createWhiteLabel("Hoş geldiniz, " + username.toUpperCase());
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

        JButton btnBasvurular = UITheme.createSidebarButton("📋  Başvurular");
        JButton btnPersoneller = UITheme.createSidebarButton("👥  Personeller");
        JButton btnCikis = UITheme.createSidebarButton("🚪  Çıkış");

        sidebar.add(btnBasvurular);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnPersoneller);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnCikis);

        mainPanel.add(sidebar, BorderLayout.WEST);

        // CONTENT AREA (CardLayout)
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UITheme.PANEL_BG);
        contentArea.add(buildBasvurularPanel(), "basvurular");
        contentArea.add(buildPersonellerPanel(), "personeller");
        mainPanel.add(contentArea, BorderLayout.CENTER);

        // Buton aksiyonları
        btnBasvurular.addActionListener(e -> cardLayout.show(contentArea, "basvurular"));
        btnPersoneller.addActionListener(e -> {
            loadPersoneller();
            cardLayout.show(contentArea, "personeller");
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

        JLabel title = new JLabel("Bekleyen Kredi Başvuruları");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.DARK_TEXT);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        // Tablo
        String[] columns = {"AD", "SOYAD", "MESLEK", "YILLIK GELİR", "KREDİ TÜRÜ", "VADE", "KREDİ FAİZİ", "DURUM"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        panel.add(scroll, BorderLayout.CENTER);

        // Alt panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomPanel.setBackground(UITheme.PANEL_BG);

        onayBox = new JComboBox<>(new String[]{"", "ONAYLANDI", "ONAYLANMADI"});
        onayBox.setFont(UITheme.FONT_REGULAR);
        onayBox.setPreferredSize(new Dimension(180, 38));

        JButton loadBtn  = UITheme.createPrimaryButton("🔄 Başvuruları Yükle");
        JButton onayBtn  = UITheme.createSuccessButton("✅ Onayla");
        JButton redBtn   = UITheme.createDangerButton("❌ Reddet");
        JButton deleteBtn = UITheme.createDangerButton("🗑 Sil");

        onayBtn.setPreferredSize(new Dimension(130, 38));
        redBtn.setPreferredSize(new Dimension(130, 38));
        deleteBtn.setPreferredSize(new Dimension(100, 38));

        loadBtn.addActionListener(e -> loadBasvurular());

        onayBtn.addActionListener(e -> updateOnay("ONAYLANDI"));
        redBtn.addActionListener(e -> updateOnay("ONAYLANMADI"));

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) tableModel.removeRow(row);
            else JOptionPane.showMessageDialog(this, "Lütfen bir satır seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
        });

        bottomPanel.add(loadBtn);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(onayBtn);
        bottomPanel.add(redBtn);
        bottomPanel.add(deleteBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadBasvurular() {
        tableModel.setRowCount(0);
        List<String[]> list = DatabaseManager.getInstance().bekleyenBasvurulariGetir();
        for (String[] row : list) tableModel.addRow(row);

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bekleyen başvuru bulunmamaktadır.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateOnay(String durum) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir satır seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String ad    = (String) tableModel.getValueAt(row, 0);
        String soyad = (String) tableModel.getValueAt(row, 1);
        DatabaseManager.getInstance().krediOnayiGuncelle(ad, soyad, durum);
        tableModel.setValueAt(durum, row, 7);
        JOptionPane.showMessageDialog(this, "Başvuru güncellendi: " + durum, "Başarılı", JOptionPane.INFORMATION_MESSAGE);
    }

    // ========== PERSONELLER PANELİ ==========

    private DefaultTableModel personelTableModel;

    private JPanel buildPersonellerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.PANEL_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Personel Listesi");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.DARK_TEXT);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"AD", "SOYAD", "DEPARTMAN", "TELEFON"};
        personelTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable personelTable = new JTable(personelTableModel);
        UITheme.styleTable(personelTable);

        JScrollPane scroll = new JScrollPane(personelTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void loadPersoneller() {
        personelTableModel.setRowCount(0);
        List<String[]> list = DatabaseManager.getInstance().personelleriGetir();
        for (String[] row : list) personelTableModel.addRow(row);
    }
}
