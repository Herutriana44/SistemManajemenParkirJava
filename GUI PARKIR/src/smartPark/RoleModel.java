package smartPark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class RoleModel {
    private static String username, password, status, idUser;

    private Connection connect;

    public RoleModel() {
        try {
            String url = "jdbc:mysql://localhost:3306/parkir_management";
            String user = "root";
            String password = "";
            connect = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection failed!");
        }
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void setIdUser(String username) {
        String query = "SELECT idUser FROM user WHERE username = ?";
        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            // Mengecek dan mengambil nilai boolean
            if (rs.next()) {
                idUser = rs.getString("idUser");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil id user!");
        }
    }

    public String getIdUser() {
        return idUser;
    }

    // Method untuk melakukan proses login
    public void login(String username, String password, JFrame loginFrame) {
        this.username = username;
        this.password = password;
        setIdUser(username);
        String query = "SELECT status FROM user WHERE BINARY username = ? AND password = ?";

        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");

                if ("admin".equalsIgnoreCase(status)) {
                    JOptionPane.showMessageDialog(null, "Login berhasil sebagai Admin");
                    loginFrame.dispose();
                    new PilihanAdmin().setVisible(true); // Buka GUI untuk Admin
                } else if ("mahasiswa".equalsIgnoreCase(status) || "dosen".equalsIgnoreCase(status)) {
                    JOptionPane.showMessageDialog(null, "Login berhasil sebagai " + username);
                    loginFrame.dispose();
                    new LahanParkirCustomer().setVisible(true); // Buka GUI untuk Customer
                } else {
                    JOptionPane.showMessageDialog(null, "Status tidak dikenali.");
                }
                this.status = status;
            } else {
                JOptionPane.showMessageDialog(null, "Username atau Password salah!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat login!");
        }
    }

    // Method untuk cek in customer (mengisi slot parkir)
    public void cekIn(String platNomor, String slot, String idUser) {
        String queryUpdateSlot = "UPDATE ketersediaan SET tersedia = 0, platNomor = ?, idUser = ? WHERE idSlot = ? AND tersedia = 1";
        String queryInsertHistory = "INSERT INTO history_parkir (idSlot, platNomor, idUser, tanggal, entryTime) VALUES (?, ?, ?, CURDATE(), NOW())";

        try (PreparedStatement stmtUpdateSlot = connect.prepareStatement(queryUpdateSlot);
             PreparedStatement stmtInsertHistory = connect.prepareStatement(queryInsertHistory)) {

            // Update slot availability
            stmtUpdateSlot.setString(1, platNomor);
            stmtUpdateSlot.setString(2, idUser);
            stmtUpdateSlot.setString(3, slot);
            int rowsUpdated = stmtUpdateSlot.executeUpdate();

            if (rowsUpdated != 0) {
                // Insert into history
                stmtInsertHistory.setString(1, slot);
                stmtInsertHistory.setString(2, platNomor);
                stmtInsertHistory.setString(3, idUser);
                stmtInsertHistory.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil cek in di slot " + slot);
            } else {
                JOptionPane.showMessageDialog(null, "Slot " + slot + " sudah terisi!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal melakukan cek in!");
        }
    }

    // Method untuk cek in admin (mengisi slot parkir)
    public void cekIn(String slot, String idUser) {
        String queryUpdateSlot = "UPDATE ketersediaan SET tersedia = 0, idUser = NULL WHERE idSlot = ? AND tersedia = 1";
        String queryInsertHistory = "INSERT INTO history_parkir (idSlot, platNomor, idUser, tanggal, entryTime) VALUES (?, 'Admin', ?, CURDATE(), NOW())";

        try (PreparedStatement stmtUpdateSlot = connect.prepareStatement(queryUpdateSlot);
             PreparedStatement stmtInsertHistory = connect.prepareStatement(queryInsertHistory)) {

            // Update slot availability
            stmtUpdateSlot.setString(1, slot);
            int rowsUpdated = stmtUpdateSlot.executeUpdate();

            if (rowsUpdated != 0) {
                // Insert into history
                stmtInsertHistory.setString(1, slot);
                stmtInsertHistory.setString(2, idUser);
                stmtInsertHistory.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil menutup slot " + slot);
            } else {
                JOptionPane.showMessageDialog(null, "Slot " + slot + " sudah tertutup!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "gagal menutup slot");
        }
    }

    // Method untuk cek out customer (mengosongkan slot parkir)
    public void cekOut(String platNomor, String slot, String idUser) {
        String queryUpdateSlot = "UPDATE ketersediaan SET tersedia = 1, platNomor = NULL, idUser = NULL WHERE idSlot = ? AND platNomor = ? AND idUser = ? AND tersedia = 0";
        String queryUpdateHistory = "UPDATE history_parkir SET exitTime = NOW(), durasiParkir = TIMESTAMPDIFF(MINUTE, entryTime, NOW()) WHERE idSlot = ? AND platNomor = ? AND idUser = ? AND exitTime IS NULL";

        try (PreparedStatement stmtUpdateSlot = connect.prepareStatement(queryUpdateSlot);
             PreparedStatement stmtUpdateHistory = connect.prepareStatement(queryUpdateHistory)) {

            // Update slot availability
            stmtUpdateSlot.setString(1, slot);
            stmtUpdateSlot.setString(2, platNomor);
            stmtUpdateSlot.setString(3, idUser);
            int rowsUpdated = stmtUpdateSlot.executeUpdate();

            if (rowsUpdated != 0) {
                // Update history
                stmtUpdateHistory.setString(1, slot);
                stmtUpdateHistory.setString(2, platNomor);
                stmtUpdateHistory.setString(3, idUser);
                stmtUpdateHistory.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil melakukan cek out pada slot " + slot + "!");
            } else {
                JOptionPane.showMessageDialog(null, "Plat Nomor atau ID User salah!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal melakukan cek out!");
        }
    }

    // Method untuk cek out admin (mengosongkan slot parkir)
    public void cekOut(String slot, String idUser) {
        String queryUpdateSlot = "UPDATE ketersediaan SET tersedia = 1, platNomor = NULL, idUser = NULL WHERE idSlot = ? AND tersedia = 0";
        String queryUpdateHistory = "UPDATE history_parkir SET exitTime = NOW(), durasiParkir = TIMESTAMPDIFF(MINUTE, entryTime, NOW()) WHERE idSlot = ? AND idUser = ? AND exitTime IS NULL";

        try (PreparedStatement stmtUpdateSlot = connect.prepareStatement(queryUpdateSlot);
             PreparedStatement stmtUpdateHistory = connect.prepareStatement(queryUpdateHistory)) {

            // Update slot availability
            stmtUpdateSlot.setString(1, slot);
            int rowsUpdated = stmtUpdateSlot.executeUpdate();

            if (rowsUpdated != 0) {
                // Update history
                stmtUpdateHistory.setString(1, slot);
                stmtUpdateHistory.setString(2, idUser);
                stmtUpdateHistory.executeUpdate();
                JOptionPane.showMessageDialog(null, "Berhasil membuka slot " + slot + "!");
            } else {
                JOptionPane.showMessageDialog(null, "Slot " + slot + " sudah terbuka!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "gagal membuka slot!");
        }
    }


    public boolean tersedia(String idSlot) {
        boolean isActive = true;
        String query = "SELECT tersedia FROM ketersediaan WHERE idSlot = ?";
        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setString(1, idSlot);
            ResultSet rs = stmt.executeQuery();

            // Mengecek dan mengambil nilai boolean
            if (rs.next()) {
                isActive = rs.getBoolean("tersedia"); // Ganti dengan nama kolom yang benar
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal melakukan refresh!");
        }
        return isActive;
    }
    public List<String[]> getAllUsers() {
        List<String[]> users = new ArrayList<>();
        String sql = "SELECT username, password, status FROM user WHERE status <> 'admin'";

        try (PreparedStatement stmt = connect.prepareStatement(sql);) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String status = rs.getString("status");

                users.add(new String[]{username, password, status});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    // Method to search for users by username
    public List<String[]> searchUsersByUsername(String username) {
        List<String[]> users = new ArrayList<>();
        String sql = "SELECT username, password, status FROM user WHERE status <> 'admin' AND username LIKE ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql);) {
            
            stmt.setString(1, "%" + username + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String uname = rs.getString("username");
                String password = rs.getString("password");
                String status = rs.getString("status");

                users.add(new String[]{uname, password, status});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }
    
    public List<String[]> getParkingHistory() {
        List<String[]> historyList = new ArrayList<>();
        String query = "SELECT h.idSlot, h.platNomor, u.username, h.tanggal, h.entryTime, h.exitTime, " +
                       "TIMESTAMPDIFF(MINUTE, h.entryTime, h.exitTime) AS durasiParkir " +
                       "FROM history_parkir h LEFT JOIN user u ON h.idUser = u.idUser";

        try (PreparedStatement preparedStatement = connect.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String slot = resultSet.getString("idSlot");
                String platNomor = resultSet.getString("platNomor");
                String username = resultSet.getString("username"); // Ambil username
                String tanggal = resultSet.getString("tanggal");
                String waktuMasuk = resultSet.getString("entryTime");
                String waktuKeluar = resultSet.getString("exitTime");
                int durasiParkir = resultSet.getInt("durasiParkir"); // Durasinya dalam menit

                // Format durasi parkir dalam menit
                String durasiParkirString = durasiParkir + " menit";

                // Tambahkan data ke dalam list
                historyList.add(new String[]{slot, platNomor, username, tanggal, waktuMasuk, waktuKeluar, durasiParkirString});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }
    
    public List<String[]> searchParkingHistoryByPlateNumber(String plateNumber) {
        List<String[]> filteredList = new ArrayList<>();
        String query = "SELECT h.idSlot, h.platNomor, u.username, h.tanggal, h.entryTime, h.exitTime, " +
                       "TIMESTAMPDIFF(MINUTE, h.entryTime, h.exitTime) AS durasiParkir " +
                       "FROM history_parkir h " +
                       "LEFT JOIN user u ON h.idUser = u.idUser " +
                       "WHERE h.platNomor LIKE ?";

        try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + plateNumber + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String slot = resultSet.getString("idSlot");
                String platNomor = resultSet.getString("platNomor");
                String username = resultSet.getString("username"); // Ambil username
                String tanggal = resultSet.getString("tanggal");
                String waktuMasuk = resultSet.getString("entryTime");
                String waktuKeluar = resultSet.getString("exitTime");
                int durasiParkir = resultSet.getInt("durasiParkir"); // Durasinya dalam menit

                // Format durasi parkir dalam menit
                String durasiParkirString = durasiParkir + " menit";

                // Tambahkan data ke dalam list
                filteredList.add(new String[]{slot, platNomor, username, tanggal, waktuMasuk, waktuKeluar, durasiParkirString});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filteredList;
    }
    
    public List<String[]> historyParkingCustomer(String idUser) {
        List<String[]> filteredList = new ArrayList<>();
        String query = "SELECT idSlot, idUser, platNomor,tanggal, entryTime, exitTime, " +
                       "TIMESTAMPDIFF(MINUTE, entryTime, exitTime) AS durasiParkir " +
                       "FROM history_parkir WHERE idUser LIKE ?";

        try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + idUser + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String slot = resultSet.getString("idSlot");
                String platNomor = resultSet.getString("platNomor");
                String tanggal = resultSet.getString("tanggal");
                String waktuMasuk = resultSet.getString("entryTime");
                String waktuKeluar = resultSet.getString("exitTime");
                String durasiParkir = resultSet.getString("durasiParkir") + " menit";

                filteredList.add(new String[]{slot, platNomor, tanggal, waktuMasuk, waktuKeluar, durasiParkir});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filteredList;
    }
    
    // Metode untuk memperbarui data pengguna di database
    public void updateUserDataInDatabase(String userId, String newUsername, String newPassword, String newStatus) {
        String query = "UPDATE user SET username = ?, password = ?, status = ? WHERE idUser = ?";
        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            // Mengatur parameter untuk query
            stmt.setString(1, newUsername);
            stmt.setString(2, newPassword);
            stmt.setString(3, newStatus);
            stmt.setString(4, userId);

            // Eksekusi update
            int rowsUpdated = stmt.executeUpdate();

            // Cek apakah update berhasil
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Data pengguna berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Gagal memperbarui data pengguna!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            // Menangani exception jika terjadi error saat mengupdate database
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat memperbarui data pengguna!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}