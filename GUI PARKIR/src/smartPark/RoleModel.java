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
        String query = "UPDATE ketersediaan SET tersedia = 0, platNomor = ?, idUser = ? WHERE idSlot = ? AND tersedia = 1";
        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setString(1, platNomor);
            stmt.setString(2, idUser);
            stmt.setString(3, slot);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated != 0) {
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
        String query = "UPDATE ketersediaan SET tersedia = 0, idUser = ? WHERE idSlot = ? AND tersedia = 1";
        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setString(1, idUser);
            stmt.setString(2, slot);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated != 0) {
                JOptionPane.showMessageDialog(null, "Berhasil menutup slot " + slot);
            } else {
                JOptionPane.showMessageDialog(null, "Slot " + slot + " sudah tertutup!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Slot sudah terbuka");
        }
    }

    // Method untuk cek out customer (mengosongkan slot parkir)
    public void cekOut(String platNomor, String slot, String idUser) {
        String query = "UPDATE ketersediaan SET tersedia = 1, platNomor = NULL, idUser = NULL WHERE idSlot = ? AND platNomor = ? AND idUser = ? AND tersedia = 0 ";
        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setString(1, slot);
            stmt.setString(2, platNomor);
            stmt.setString(3, idUser);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                JOptionPane.showMessageDialog(null, "Plat Nomor atau ID User salah!");
            } else {
                JOptionPane.showMessageDialog(null, "Berhasil melakukan cek out pada slot slot " + slot + "!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal melakukan cek out!");
        }
    }

    // Method untuk cek out customer (mengosongkan slot parkir)
    public void cekOut(String slot, String idUser) {
        String query = "UPDATE ketersediaan SET tersedia = 1, platNomor = NULL, idUser = NULL WHERE idSlot = ? AND idUser = ? AND tersedia = 0 ";
        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setString(1, slot);
            stmt.setString(2, idUser);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                JOptionPane.showMessageDialog(null, "Slot " + slot + " sudah terbuka!");
            } else {
                JOptionPane.showMessageDialog(null, "Berhasil membuka slot " + slot + "!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal melakukan cek out!");
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
    String query = "SELECT idSlot, platNomor,tanggal, entryTime, exitTime, " +
                   "TIMESTAMPDIFF(MINUTE, entryTime, exitTime) AS durasi_parkir " +
                   "FROM history_parkir";

    try (PreparedStatement preparedStatement = connect.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
            String slot = resultSet.getString("idSlot");
            String platNomor = resultSet.getString("platNomor");
            String tanggal = resultSet.getString("tanggal");
            String waktuMasuk = resultSet.getString("entryTime");
            String waktuKeluar = resultSet.getString("exitTime");
            String durasiParkir = resultSet.getString("durasi_parkir") + " menit";

            historyList.add(new String[]{slot, platNomor, tanggal, waktuMasuk, waktuKeluar, durasiParkir});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return historyList;
}

public List<String[]> searchParkingHistoryByPlateNumber(String plateNumber) {
    List<String[]> filteredList = new ArrayList<>();
    String query = "SELECT idSlot, platNomor,tanggal, entryTime, exitTime, " +
                   "TIMESTAMPDIFF(MINUTE, entryTime, exitTime) AS durasi_parkir " +
                   "FROM history_parkir WHERE platNomor LIKE ?";

    try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {

        preparedStatement.setString(1, "%" + plateNumber + "%");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String slot = resultSet.getString("idSlot");
            String platNomor = resultSet.getString("platNomor");
            String tanggal = resultSet.getString("tanggal");
            String waktuMasuk = resultSet.getString("entryTime");
            String waktuKeluar = resultSet.getString("exitTime");
            String durasiParkir = resultSet.getString("durasi_parkir") + " menit";

            filteredList.add(new String[]{slot, platNomor, tanggal, waktuMasuk, waktuKeluar, durasiParkir});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return filteredList;
}
public List<String[]> searchParkingHistoryByIdUser(String idUser) {
    List<String[]> filteredList = new ArrayList<>();
    String query = "SELECT idSlot, idUser, platNomor,tanggal, entryTime, exitTime, " +
                   "TIMESTAMPDIFF(MINUTE, entryTime, exitTime) AS durasi_parkir " +
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
            String durasiParkir = resultSet.getString("durasi_parkir") + " menit";

            filteredList.add(new String[]{slot, platNomor, tanggal, waktuMasuk, waktuKeluar, durasiParkir});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return filteredList;
}
}
