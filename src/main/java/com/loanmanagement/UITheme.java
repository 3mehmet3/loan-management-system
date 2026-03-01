package com.loanmanagement;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Merkezi UI tema sınıfı.
 * Tüm ekranlar bu sınıfı kullanarak tutarlı bir görünüm elde eder.
 */
public class UITheme {

    // YapıKredi Renk Paleti
    public static final Color PRIMARY_BLUE    = new Color(0, 51, 153);
    public static final Color DARK_BLUE       = new Color(0, 30, 100);
    public static final Color ACCENT_BLUE     = new Color(0, 102, 204);
    public static final Color LIGHT_BLUE      = new Color(230, 240, 255);
    public static final Color SILVER          = new Color(180, 190, 200);
    public static final Color WHITE           = Color.WHITE;
    public static final Color DARK_TEXT       = new Color(30, 30, 30);
    public static final Color SUCCESS_GREEN   = new Color(34, 139, 34);
    public static final Color DANGER_RED      = new Color(180, 30, 30);
    public static final Color PANEL_BG        = new Color(245, 247, 252);

    // Fontlar
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Uygulamayı başlatırken çağrılır. FlatLaf temasını ayarlar.
     */
    public static void applyTheme() {
        try {
            // FlatLaf modern tema
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            // FlatLaf yoksa sistem teması kullan
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Global ayarlar
        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("Table.alternateRowColor", new Color(245, 248, 255));
        UIManager.put("TableHeader.background", PRIMARY_BLUE);
        UIManager.put("TableHeader.foreground", WHITE);
    }

    // ========== BUTON FABRİKASI ==========

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(PRIMARY_BLUE);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT_BLUE);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(PRIMARY_BLUE);
            }
        });
        return btn;
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(SILVER);
        btn.setForeground(DARK_TEXT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 40));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(160, 170, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(SILVER);
            }
        });
        return btn;
    }

    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(DANGER_RED);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(210, 50, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(DANGER_RED);
            }
        });
        return btn;
    }

    public static JButton createSuccessButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(SUCCESS_GREEN);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ========== ALAN FABRİKASI ==========

    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_REGULAR);
        field.setPreferredSize(new Dimension(220, 38));
        return field;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(FONT_REGULAR);
        field.setPreferredSize(new Dimension(220, 38));
        return field;
    }

    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(DARK_TEXT);
        return lbl;
    }

    public static JLabel createWhiteLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(WHITE);
        return lbl;
    }

    public static JLabel createTitleLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(WHITE);
        return lbl;
    }

    // ========== TABLO STİLİ ==========

    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(32);
        table.setGridColor(new Color(220, 225, 235));
        table.setSelectionBackground(ACCENT_BLUE);
        table.setSelectionForeground(WHITE);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(PRIMARY_BLUE);
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(0, 36));
        header.setReorderingAllowed(false);
    }

    // ========== HEADER PANELİ ==========

    public static JPanel createHeaderPanel(String title) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_BLUE);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(WHITE);
        header.add(titleLabel, BorderLayout.CENTER);

        return header;
    }

    // ========== SIDEBAR PANELİ ==========

    public static JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(DARK_BLUE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));
        return sidebar;
    }

    public static JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(new Color(0, 45, 120));
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT_BLUE);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(0, 45, 120));
            }
        });
        return btn;
    }

    // ========== FRAME AYARI ==========

    public static void centerFrame(JFrame frame) {
        frame.setLocationRelativeTo(null);
    }

    public static ImageIcon loadIcon(String path) {
        try {
            java.net.URL url = UITheme.class.getResource(path);
            if (url != null) return new ImageIcon(url);
        } catch (Exception e) {
            // icon bulunamadı, null döner
        }
        return null;
    }
}
