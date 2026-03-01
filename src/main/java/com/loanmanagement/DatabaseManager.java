package com.loanmanagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tüm veritabanı işlemlerini yöneten merkezi sınıf.
 * Eski .txt dosya sisteminin yerini alır.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:kredi_sistemi.db";
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        initialize();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initialize() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            seedDefaultUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        Statement stmt = connection.createStatement();

        // Personel tablosu
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS personeller (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kullanici_adi TEXT NOT NULL UNIQUE," +
            "sifre TEXT NOT NULL," +
            "ad TEXT, soyad TEXT, departman TEXT, telefon TEXT)"
        );

        // Müdür tablosu
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS mudurler (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kullanici_adi TEXT NOT NULL UNIQUE," +
            "sifre TEXT NOT NULL," +
            "ad TEXT, soyad TEXT, sube TEXT, iletisim TEXT)"
        );

        // Genel müdür tablosu
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS patron (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kullanici_adi TEXT NOT NULL UNIQUE," +
            "sifre TEXT NOT NULL)"
        );

        // Kredi başvuruları tablosu
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS kredi_basvurulari (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ad TEXT NOT NULL, soyad TEXT NOT NULL," +
            "meslek TEXT, yillik_gelir TEXT," +
            "kredi_turu TEXT, vade TEXT, kredi_faizi TEXT," +
            "kredi_onayi TEXT DEFAULT 'BEKLEMEDE'," +
            "basvuru_tarihi TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
        );

        stmt.close();
    }

    private void seedDefaultUsers() {
        try {
            // Personeller
            insertPersonelIfNotExists("kubra", "8945", "KÜBRA", "KALE", "Satış Danışmanı", "5551234001");
            insertPersonelIfNotExists("sema", "2823", "SEMA", "BAKAN", "Satış Danışmanı", "5551234002");
            insertPersonelIfNotExists("mehmet", "4226", "MEHMET", "YILMAZ", "Satış Danışmanı", "5551234003");
            insertPersonelIfNotExists("ahmet", "6366", "AHMET", "CAN", "Satış Danışmanı", "5551234004");
            insertPersonelIfNotExists("ali", "1991", "ALİ", "KURT", "Satış Danışmanı", "5551234005");
            insertPersonelIfNotExists("siddik", "4445", "SIDDIK", "AÇAN", "Satış Danışmanı", "5551234006");
            insertPersonelIfNotExists("fuat", "4406", "FUAT", "YAKA", "Satış Danışmanı", "5551234007");
            insertPersonelIfNotExists("salih", "7621", "SALİH", "ÖZCAN", "Satış Danışmanı", "5551234008");
            insertPersonelIfNotExists("halime", "2329", "HALİME", "DOĞAN", "Satış Danışmanı", "5551234009");

            // Müdürler
            insertMudurIfNotExists("ademoglu", "4721", "KEMAL", "ADEMOĞLU", "KADIKÖY", "5559001001");
            insertMudurIfNotExists("bturhan", "8834", "BURCU", "TURHAN", "BEŞİKTAŞ", "5559001002");
            insertMudurIfNotExists("tkocer", "3156", "TARIK", "KOÇER", "ÜSKÜDAR", "5559001003");

            // Genel Müdür
            insertPatronIfNotExists("aydinoglu", "5342");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertPersonelIfNotExists(String kullaniciAdi, String sifre, String ad, String soyad, String departman, String tel) throws SQLException {
        PreparedStatement check = connection.prepareStatement("SELECT id FROM personeller WHERE kullanici_adi = ?");
        check.setString(1, kullaniciAdi);
        ResultSet rs = check.executeQuery();
        if (!rs.next()) {
            PreparedStatement insert = connection.prepareStatement(
                "INSERT INTO personeller (kullanici_adi, sifre, ad, soyad, departman, telefon) VALUES (?, ?, ?, ?, ?, ?)");
            insert.setString(1, kullaniciAdi);
            insert.setString(2, sifre);
            insert.setString(3, ad);
            insert.setString(4, soyad);
            insert.setString(5, departman);
            insert.setString(6, tel);
            insert.execute();
            insert.close();
        }
        check.close();
    }

    private void insertMudurIfNotExists(String kullaniciAdi, String sifre, String ad, String soyad, String sube, String iletisim) throws SQLException {
        PreparedStatement check = connection.prepareStatement("SELECT id FROM mudurler WHERE kullanici_adi = ?");
        check.setString(1, kullaniciAdi);
        ResultSet rs = check.executeQuery();
        if (!rs.next()) {
            PreparedStatement insert = connection.prepareStatement(
                "INSERT INTO mudurler (kullanici_adi, sifre, ad, soyad, sube, iletisim) VALUES (?, ?, ?, ?, ?, ?)");
            insert.setString(1, kullaniciAdi);
            insert.setString(2, sifre);
            insert.setString(3, ad);
            insert.setString(4, soyad);
            insert.setString(5, sube);
            insert.setString(6, iletisim);
            insert.execute();
            insert.close();
        }
        check.close();
    }

    private void insertPatronIfNotExists(String kullaniciAdi, String sifre) throws SQLException {
        PreparedStatement check = connection.prepareStatement("SELECT id FROM patron WHERE kullanici_adi = ?");
        check.setString(1, kullaniciAdi);
        ResultSet rs = check.executeQuery();
        if (!rs.next()) {
            PreparedStatement insert = connection.prepareStatement("INSERT INTO patron (kullanici_adi, sifre) VALUES (?, ?)");
            insert.setString(1, kullaniciAdi);
            insert.setString(2, sifre);
            insert.execute();
            insert.close();
        }
        check.close();
    }

    // ========== GİRİŞ KONTROLÜ ==========

    public boolean personelGirisKontrol(String kullaniciAdi, String sifre) {
        return girisKontrol("personeller", kullaniciAdi, sifre);
    }

    public boolean mudurGirisKontrol(String kullaniciAdi, String sifre) {
        return girisKontrol("mudurler", kullaniciAdi, sifre);
    }

    public boolean patronGirisKontrol(String kullaniciAdi, String sifre) {
        return girisKontrol("patron", kullaniciAdi, sifre);
    }

    private boolean girisKontrol(String tablo, String kullaniciAdi, String sifre) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT id FROM " + tablo + " WHERE kullanici_adi = ? AND sifre = ?");
            stmt.setString(1, kullaniciAdi);
            stmt.setString(2, sifre);
            ResultSet rs = stmt.executeQuery();
            boolean sonuc = rs.next();
            stmt.close();
            return sonuc;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========== KREDİ BAŞVURULARI ==========

    public void krediBasvurusuEkle(String ad, String soyad, String meslek, String yillikGelir,
                                    String krediTuru, String vade, String krediFaizi) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO kredi_basvurulari (ad, soyad, meslek, yillik_gelir, kredi_turu, vade, kredi_faizi) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, ad);
            stmt.setString(2, soyad);
            stmt.setString(3, meslek);
            stmt.setString(4, yillikGelir);
            stmt.setString(5, krediTuru);
            stmt.setString(6, vade);
            stmt.setString(7, krediFaizi);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> bekleyenBasvurulariGetir() {
        return basvurulariGetir("BEKLEMEDE");
    }

    public List<String[]> tumBasvurulariGetir() {
        try {
            List<String[]> liste = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT ad, soyad, meslek, yillik_gelir, kredi_turu, vade, kredi_faizi, kredi_onayi FROM kredi_basvurulari ORDER BY basvuru_tarihi DESC");
            while (rs.next()) {
                liste.add(new String[]{
                    rs.getString("ad"), rs.getString("soyad"), rs.getString("meslek"),
                    rs.getString("yillik_gelir"), rs.getString("kredi_turu"), rs.getString("vade"),
                    rs.getString("kredi_faizi"), rs.getString("kredi_onayi")
                });
            }
            stmt.close();
            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<String[]> basvurulariGetir(String durum) {
        try {
            List<String[]> liste = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT ad, soyad, meslek, yillik_gelir, kredi_turu, vade, kredi_faizi, kredi_onayi FROM kredi_basvurulari WHERE kredi_onayi = ?");
            stmt.setString(1, durum);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                liste.add(new String[]{
                    rs.getString("ad"), rs.getString("soyad"), rs.getString("meslek"),
                    rs.getString("yillik_gelir"), rs.getString("kredi_turu"), rs.getString("vade"),
                    rs.getString("kredi_faizi"), rs.getString("kredi_onayi")
                });
            }
            stmt.close();
            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void krediOnayiGuncelle(String ad, String soyad, String onayDurumu) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE kredi_basvurulari SET kredi_onayi = ? WHERE ad = ? AND soyad = ? AND kredi_onayi = 'BEKLEMEDE'");
            stmt.setString(1, onayDurumu);
            stmt.setString(2, ad);
            stmt.setString(3, soyad);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========== PERSONEL LİSTESİ ==========

    public List<String[]> personelleriGetir() {
        return kullanicilariGetir("SELECT ad, soyad, departman, telefon FROM personeller");
    }

    public List<String[]> mudurleriGetir() {
        return kullanicilariGetir("SELECT ad, soyad, sube, iletisim FROM mudurler");
    }

    private List<String[]> kullanicilariGetir(String sql) {
        try {
            List<String[]> liste = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            while (rs.next()) {
                String[] row = new String[cols];
                for (int i = 0; i < cols; i++) row[i] = rs.getString(i + 1);
                liste.add(row);
            }
            stmt.close();
            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
