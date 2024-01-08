import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class KlinikApp {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNama, txtAlamat, txtNIK, txtTanggalLahir;

    private ArrayList<Pasien> dataPasien = new ArrayList<>();
    private int currentIndex = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                KlinikApp window = new KlinikApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public KlinikApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        model = new DefaultTableModel();
        model.addColumn("Nama");
        model.addColumn("Alamat");
        model.addColumn("NIK");
        model.addColumn("Tanggal Lahir");

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        txtNama = new JTextField(20);
        txtAlamat = new JTextField(50);
        txtNIK = new JTextField(15);
        txtTanggalLahir = new JTextField(15);

        JButton btnTambah = new JButton("Tambah");
        btnTambah.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tambahData();
            }
        });

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateData();
            }
        });

        JButton btnHapus = new JButton("Hapus");
        btnHapus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hapusData();
            }
        });

        JButton btnPrev = new JButton("Prev");
        btnPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigateData(-1);
            }
        });

        JButton btnNext = new JButton("Next");
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigateData(1);
            }
        });

        JButton btnDaftar = new JButton("Daftar Pasien");
        btnDaftar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tampilkanDaftarPasien();
            }
        });

        JButton btnKeluar = new JButton("Keluar");
        btnKeluar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel.add(txtNama);
        panel.add(txtAlamat);
        panel.add(txtNIK);
        panel.add(txtTanggalLahir);
        panel.add(btnTambah);
        panel.add(btnUpdate);
        panel.add(btnHapus);
        panel.add(btnPrev);
        panel.add(btnNext);
        panel.add(btnDaftar);
        panel.add(btnKeluar);
    }

    private void tambahData() {
        // Validasi NIK unik
        String nik = txtNIK.getText();
        if (isNIKExist(nik)) {
            JOptionPane.showMessageDialog(frame, "NIK sudah ada!");
            return;
        }

        // Tambah data ke ArrayList
        Pasien pasien = new Pasien(
                txtNama.getText(),
                txtAlamat.getText(),
                nik,
                txtTanggalLahir.getText()
        );
        dataPasien.add(pasien);

        // Tambah data ke tabel
        Object[] rowData = {
                pasien.getNama(),
                pasien.getAlamat(),
                pasien.getNik(),
                pasien.getTanggalLahir()
        };
        model.addRow(rowData);

        // Kosongkan input fields
        kosongkanInputFields();
    }

    private void updateData() {
        // Validasi indeks
        if (currentIndex < 0 || currentIndex >= dataPasien.size()) {
            JOptionPane.showMessageDialog(frame, "Tidak ada data yang dipilih!");
            return;
        }

        // Update data di ArrayList
        Pasien pasien = dataPasien.get(currentIndex);
        pasien.setNama(txtNama.getText());
        pasien.setAlamat(txtAlamat.getText());
        pasien.setTanggalLahir(txtTanggalLahir.getText());

        // Update data di tabel
        model.setValueAt(pasien.getNama(), currentIndex, 0);
        model.setValueAt(pasien.getAlamat(), currentIndex, 1);
        model.setValueAt(pasien.getNik(), currentIndex, 2);
        model.setValueAt(pasien.getTanggalLahir(), currentIndex, 3);

        // Kosongkan input fields
        kosongkanInputFields();
    }

    private void hapusData() {
        // Validasi indeks
        if (currentIndex < 0 || currentIndex >= dataPasien.size()) {
            JOptionPane.showMessageDialog(frame, "Tidak ada data yang dipilih!");
            return;
        }

        // Hapus data dari ArrayList
        dataPasien.remove(currentIndex);

        // Hapus data dari tabel
        model.removeRow(currentIndex);

        // Kosongkan input fields
        kosongkanInputFields();
    }

    private void navigateData(int direction) {
        currentIndex += direction;

        // Pastikan indeks berada dalam batas yang benar
        if (currentIndex < 0) {
            currentIndex = 0;
        } else if (currentIndex >= dataPasien.size()) {
            currentIndex = dataPasien.size() - 1;
        }

        // Tampilkan data pada indeks saat ini
        if (!dataPasien.isEmpty()) {
            Pasien pasien = dataPasien.get(currentIndex);
            tampilkanDataPasien(pasien);
        }
    }

    private void tampilkanDaftarPasien() {
        JFrame frameDaftar = new JFrame("Daftar Pasien");
        frameDaftar.setBounds(100, 100, 500, 300);
        frameDaftar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameDaftar.getContentPane().setLayout(new BorderLayout(0, 0));

        JTable tableDaftar = new JTable();
        DefaultTableModel modelDaftar = new DefaultTableModel();
        modelDaftar.addColumn("No");
        modelDaftar.addColumn("Nama Pasien");
        modelDaftar.addColumn("NIK");
        modelDaftar.addColumn("Tanggal Lahir");
        modelDaftar.addColumn("Alamat");

        for (int i = 0; i < dataPasien.size(); i++) {
            Pasien pasien = dataPasien.get(i);
            Object[] rowData = {
                    i + 1,
                    pasien.getNama(),
                    pasien.getNik(),
                    pasien.getTanggalLahir(),
                    pasien.getAlamat()
            };
            modelDaftar.addRow(rowData);
        }

        tableDaftar.setModel(modelDaftar);

        JScrollPane scrollPaneDaftar = new JScrollPane(tableDaftar);
        frameDaftar.getContentPane().add(scrollPaneDaftar, BorderLayout.CENTER);

        frameDaftar.setVisible(true);
    }

    private void tampilkanDataPasien(Pasien pasien) {
        txtNama.setText(pasien.getNama());
        txtAlamat.setText(pasien.getAlamat());
        txtNIK.setText(pasien.getNik());
        txtTanggalLahir.setText(pasien.getTanggalLahir());
    }

    private void kosongkanInputFields() {
        txtNama.setText("");
        txtAlamat.setText("");
        txtNIK.setText("");
        txtTanggalLahir.setText("");
    }

    private boolean isNIKExist(String nik) {
        for (Pasien pasien : dataPasien) {
            if (pasien.getNik().equals(nik)) {
                return true;
            }
        }
        return false;
    }

    private class Pasien {
        private String nama;
        private String alamat;
        private String nik;
        private String tanggalLahir;

        public Pasien(String nama, String alamat, String nik, String tanggalLahir) {
            this.nama = nama;
            this.alamat = alamat;
            this.nik = nik;
            this.tanggalLahir = tanggalLahir;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getAlamat() {
            return alamat;
        }

        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }

        public String getNik() {
            return nik;
        }

        public String getTanggalLahir() {
            return tanggalLahir;
        }

        public void setTanggalLahir(String tanggalLahir) {
            this.tanggalLahir = tanggalLahir;
        }
    }
}
