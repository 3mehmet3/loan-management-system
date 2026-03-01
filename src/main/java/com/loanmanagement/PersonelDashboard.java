package com.loanmanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * Personel ana ekranı — Kredi başvurusu yapma.
 * Eski PePage.java'nın yerine geçer.
 */
public class PersonelDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    private final String username;
    private DefaultTableModel tableModel;
    private JTable table;

    // Form alanları
    private JTextField adField, soyadField, meslekField, gelirField;
    private JComboBox<String> krediTuruBox, vadeBox, krediFaiziBox;

    public PersonelDashboard(String username) {
        this.username = username;

        setTitle("Yapı Kredi - Kredi Başvuru Ekranı");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 620);
        setMinimumSize(new Dimension(900, 550));
        UITheme.centerFrame(this);

        buildUI();
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.PANEL_BG);
        setContentPane(mainPanel);

        // HEADER
        JPanel header = UITheme.createHeaderPanel("📋  KREDİ BAŞVURU SİSTEMİ");
        JLabel userLabel = UITheme.createWhiteLabel("Hoş geldiniz, " + username.toUpperCase());
        userLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
        header.add(userLabel, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);

        // TABLO
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UITheme.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(15, 15, 5, 15),
            BorderFactory.createLineBorder(new Color(220, 225, 235))
        ));

        String[] columns = {"AD", "SOYAD", "MESLEK", "YILLIK GELİR", "KREDİ TÜRÜ", "VADE", "KREDİ FAİZİ"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // FORM + BUTONLAR
        JPanel bottomPanel = buildFormPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel buildFormPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(UITheme.PANEL_BG);
        bottomPanel.setBorder(new EmptyBorder(10, 15, 15, 15));

        // Form alanları
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235)),
            new EmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Satır 1: AD, SOYAD, MESLEK, YILLIK GELİR
        adField     = UITheme.createTextField();
        soyadField  = UITheme.createTextField();
        meslekField = UITheme.createTextField();
        gelirField  = UITheme.createTextField();

        addFormField(formPanel, gbc, "Ad:", adField, 0, 0);
        addFormField(formPanel, gbc, "Soyad:", soyadField, 2, 0);
        addFormField(formPanel, gbc, "Meslek:", meslekField, 4, 0);
        addFormField(formPanel, gbc, "Yıllık Gelir (₺):", gelirField, 6, 0);

        // Satır 2: KREDİ TÜRÜ, VADE, KREDİ FAİZİ
        krediTuruBox  = new JComboBox<>(new String[]{"", "KONUT KREDİSİ", "TAŞIT KREDİSİ", "İHTİYAÇ KREDİSİ"});
        vadeBox       = new JComboBox<>(new String[]{"", "12 AY", "24 AY", "36 AY"});
        krediFaiziBox = new JComboBox<>(new String[]{"", "%1", "%2", "%3.9", "%12", "%17.4"});

        styleComboBox(krediTuruBox);
        styleComboBox(vadeBox);
        styleComboBox(krediFaiziBox);

        addFormField(formPanel, gbc, "Kredi Türü:", krediTuruBox, 0, 1);
        addFormField(formPanel, gbc, "Vade:", vadeBox, 2, 1);
        addFormField(formPanel, gbc, "Kredi Faizi:", krediFaiziBox, 4, 1);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Butonlar
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(UITheme.PANEL_BG);

        JButton backBtn   = UITheme.createSecondaryButton("← GERİ");
        JButton deleteBtn = UITheme.createDangerButton("🗑 SİL");
        JButton addBtn    = UITheme.createPrimaryButton("+ EKLE");
        JButton saveBtn   = UITheme.createSuccessButton("💾 KAYDET");

        saveBtn.setPreferredSize(new Dimension(150, 40));

        backBtn.addActionListener(e -> {
            new LoginPage("personel").setVisible(true);
            dispose();
        });

        addBtn.addActionListener(e -> handleAdd());

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silmek istediğiniz satırı seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            }
        });

        saveBtn.addActionListener(e -> handleSave());

        btnPanel.add(backBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(addBtn);
        btnPanel.add(saveBtn);

        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int x, int y) {
        gbc.gridx = x; gbc.gridy = y; gbc.weightx = 0;
        panel.add(UITheme.createLabel(labelText), gbc);

        gbc.gridx = x + 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setFont(UITheme.FONT_REGULAR);
        box.setPreferredSize(new Dimension(160, 38));
    }

    private void handleAdd() {
        String ad          = adField.getText().trim();
        String soyad       = soyadField.getText().trim();
        String meslek      = meslekField.getText().trim();
        String gelir       = gelirField.getText().trim();
        String krediTuru   = (String) krediTuruBox.getSelectedItem();
        String vade        = (String) vadeBox.getSelectedItem();
        String krediFaizi  = (String) krediFaiziBox.getSelectedItem();

        if (ad.isEmpty() || soyad.isEmpty() || meslek.isEmpty() || gelir.isEmpty()
                || krediTuru.isEmpty() || vade.isEmpty() || krediFaizi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.", "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{ad, soyad, meslek, gelir, krediTuru, vade, krediFaizi});

        // Alanları temizle
        adField.setText(""); soyadField.setText(""); meslekField.setText(""); gelirField.setText("");
        krediTuruBox.setSelectedIndex(0); vadeBox.setSelectedIndex(0); krediFaiziBox.setSelectedIndex(0);
    }

    private void handleSave() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Kaydetmek için önce başvuru ekleyin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DatabaseManager db = DatabaseManager.getInstance();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            db.krediBasvurusuEkle(
                (String) tableModel.getValueAt(i, 0),
                (String) tableModel.getValueAt(i, 1),
                (String) tableModel.getValueAt(i, 2),
                (String) tableModel.getValueAt(i, 3),
                (String) tableModel.getValueAt(i, 4),
                (String) tableModel.getValueAt(i, 5),
                (String) tableModel.getValueAt(i, 6)
            );
        }

        JOptionPane.showMessageDialog(this, "Başvurularınız başarıyla kaydedildi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
        tableModel.setRowCount(0);
    }
}
