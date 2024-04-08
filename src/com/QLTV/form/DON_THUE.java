/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.DonThueChiTietDAO;
import com.QLTV.dao.DonThueDAO;
import com.QLTV.dao.HoaDonChiTietDAO;
import com.QLTV.dao.HoaDonDAO;
import com.QLTV.dao.KhachHangDAO;
import com.QLTV.dao.SachDAO;
import com.QLTV.dao.TacGiaDAO;
import com.QLTV.dao.TheLoaiDAO;
import com.QLTV.dao.ViTriDAO;
import com.QLTV.entity.DonThue;
import com.QLTV.entity.DonThueChiTiet;
import com.QLTV.entity.HoaDonChiTiet;
import com.QLTV.entity.KhachHang;
import com.QLTV.entity.Sach;
import com.QLTV.utils.XAuth;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven.calendar.model.ModelDate;
import raven.calendar.utils.CalendarSelectedListener;

/**
 *
 * @author Tuong
 */
public class DON_THUE extends javax.swing.JPanel {

    Date ngay;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DecimalFormat D_format = new DecimalFormat("0.#");
    SachDAO sachDAO = new SachDAO();
    TheLoaiDAO tlDAO = new TheLoaiDAO();
    TacGiaDAO tgDAO = new TacGiaDAO();
    ViTriDAO vtDAO = new ViTriDAO();
    HoaDonDAO hdDAO = new HoaDonDAO();
    HoaDonChiTietDAO hdctDAO = new HoaDonChiTietDAO();
    DonThueDAO dthueDAO = new DonThueDAO();
    DonThueChiTietDAO dthuectDAO = new DonThueChiTietDAO();
    KhachHangDAO khDAO = new KhachHangDAO();
    int index = -1;
    DefaultTableModel model_donthue, model_donthuect;
    Double tienphat = 0.0;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

    /**
     * Creates new form DON_THUE
     */
    public DON_THUE() {
        initComponents();
        loaddataDonThue();
        btn_xoa.setEnabled(false);
        TAB.setEnabledAt(1, false);
        POPUP.add(jPanel1);
        calendar1.addCalendarSelectedListener(new CalendarSelectedListener() {
            @Override
            public void selected(MouseEvent evt, ModelDate date) {
                ngay = date.toDate();
                String ngayF = sdf.format(ngay);
                txt_ngaytra.setText(ngayF.toString());
                POPUP.setVisible(false);
                try {
                    tinhtienphat();
                } catch (ParseException ex) {
                    Logger.getLogger(DON_THUE.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        if (XAuth.isManager() == true) {
            btn_xoa.setVisible(true);
        } else {
            btn_xoa.setVisible(false);
        }
    }

    private void showPopup() {
        //int x = txt_ngaysinh.getLocationOnScreen().x;
        //int y = txt_ngaysinh.getLocationOnScreen().y + txt_ngaysinh.getHeight();
        POPUP.show(txt_ngaytra, 0, txt_ngaytra.getHeight());
    }

    public void setFormDonThue(DonThue dthue) throws ParseException {
        txt_iddonthue2.setText(dthue.getIddonthue());
        KhachHang kh = khDAO.select_byID(dthue.getIdkhach());
        txt_idKhach.setText(dthue.getIdkhach());
        txt_manv.setText(dthue.getManv());
        txt_ngaythue.setText(dthue.getNgaytao());
        txt_ngaytradukien.setText(dthue.getNgaytradukien());
        txt_tenkhach.setText(kh.getHotenkhach());
        txt_tiendambao.setText(Double.toString(dthue.getTongtiendambao()));
        txt_ngaytra.setText(dthue.getNgaytra());
        txt_tienphat.setText(Double.toString(dthue.getTienphat()));
        String khachdua = D_format.format(dthue.getKhachdua());
        String thoilai = D_format.format(dthue.getThoilai());
        String thanhtien = D_format.format(dthue.getThanhtien());
        txt_khachdua.setText(khachdua);
        txt_thoilai.setText(thoilai);
        txt_tongtien.setText(thanhtien);
    }

    void tinhtienphat() throws ParseException {
        Date ngaytra = sdf.parse(txt_ngaytra.getText());
        Date ngaytradukien = sdf.parse(txt_ngaytradukien.getText());

        long miliS = ngaytra.getTime() - ngaytradukien.getTime();
        // Tính toán số ngày
        long diffInDays = miliS / (1000 * 60 * 60 * 24);

        // Tính toán số giờ còn lại sau khi tính số ngày
        long remainingMillis = miliS % (1000 * 60 * 60 * 24);
        long diffInHours = remainingMillis / (1000 * 60 * 60);

        // Tính toán số phút còn lại sau khi tính số ngày và số giờ
        remainingMillis = remainingMillis % (1000 * 60 * 60);
        long diffInMinutes = remainingMillis / (1000 * 60);
        // Tính giá tiền
        long totalPrice = (diffInDays * 15000) + (diffInHours * 500);
        if (totalPrice <= 0) {
            lbl_ngaytre.setText("");
            txt_tienphat.setText("0");
        } else {
            lbl_ngaytre.setText("Trễ " + diffInDays + " ngày " + diffInHours + " giờ " + diffInMinutes + " phút");
            txt_tienphat.setText(Long.toString(totalPrice));
        }
//        System.out.println(diffInDays + " ngay " + diffInHours + " gio " + diffInMinutes + " phut");
//        System.out.println("Giá tiền: " + totalPrice);
    }

    public void setFormHDCT(HoaDonChiTiet hdct) throws ParseException {
        txt_iddonthue2.setText(hdct.getIdhoadon());
        Sach sach = sachDAO.select_byID(hdct.getIdsach());
        String tensach = sach.getTensach();
    }

    public DonThue getFormDonThue() throws ParseException {
        DonThue dthueNew = new DonThue();
        dthueNew.setIddonthue(txt_iddonthue2.getText().trim());
        dthueNew.setIdkhach(txt_idKhach.getText().trim());
        dthueNew.setManv(txt_manv.getText().trim());
        dthueNew.setNgaytao(txt_ngaythue.getText());
        dthueNew.setNgaythue(txt_ngaythue.getText());
        String ntrdk = txt_ngaytradukien.getText().trim();
        dthueNew.setNgaytradukien(ntrdk);
        dthueNew.setTrangthai(true);
        dthueNew.setNgaytra(txt_ngaytra.getText());
        dthueNew.setTienphat(Double.valueOf(txt_tienphat.getText().trim()));

        dthueNew.setTongtiendambao(Double.valueOf(txt_tiendambao.getText().trim()));
        dthueNew.setKhachdua(Double.valueOf(txt_khachdua.getText().trim()));
        dthueNew.setThoilai(Double.valueOf(txt_thoilai.getText().trim()));
        dthueNew.setThanhtien(Double.valueOf(txt_tongtien.getText().trim()));
        return dthueNew;
    }

    public void fillFormDonThue() throws ParseException {
        txt_iddonthue2.setEnabled(false);
        txt_iddonthue2.setEditable(false);

        btn_xoa.setEnabled(true);

        String iddonthue = (String) tbl_donthue.getValueAt(index, 0);
        DonThue dthue = dthueDAO.select_byID(iddonthue);
        if (dthue == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setFormDonThue(dthue);
        }
    }

    public void fillFormHDCT() throws ParseException {
        txt_iddonthue2.setEnabled(false);
        txt_iddonthue2.setEditable(false);

        int idhdct = (Integer) tbl_donthueCT.getValueAt(index, 0);
        HoaDonChiTiet hdct = hdctDAO.select_byID_int(idhdct);
        if (hdct == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setFormHDCT(hdct);
        }
    }

    public void suaDonThue() {
        try {
            KhachHang kh = khDAO.select_byID(txt_idKhach.getText());
            int uytin = kh.getDiemuytin();
            Double tien = Double.valueOf(txt_tienphat.getText());
            DonThue dthueNew = getFormDonThue();
            dthueDAO.update(dthueNew);
            if (tien > 0) {
                if (kh.getDiemuytin() < 15 || kh.getDiemuytin() == 0) {
                    kh.setDiemuytin(0);
                    khDAO.update(kh);
                } else {
                    uytin = uytin - 15;
                    kh.setDiemuytin(uytin);
                    khDAO.update(kh);
                }
            } else if (kh.getDiemuytin() == 100) {
                kh.setDiemuytin(100);
                khDAO.update(kh);
            } else {
                uytin = uytin + 5;
                kh.setDiemuytin(uytin);
                khDAO.update(kh);
            }
            for (int i = 0; i < model_donthuect.getRowCount(); i++) {
                String tensach = tbl_donthueCT.getValueAt(i, 1).toString();
                String mas = sachDAO.getMasach(tensach);
                Sach s = sachDAO.select_byID(mas);
                int sls = s.getSl();
                int slsm = sls + (Integer.parseInt(tbl_donthueCT.getValueAt(i, 2).toString()));
                s.setSl(slsm);
                sachDAO.update(s);
            }
            TAB.setSelectedIndex(0);
            loaddataDonThue();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!\n" + e.getMessage());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void xoaDonThue() {
        try {
            String iddonthue = txt_iddonthue2.getText();
            List<DonThueChiTiet> list = dthuectDAO.select_by_HD(iddonthue);
            for (DonThueChiTiet dthuect : list) {
                dthuectDAO.delete(Integer.toString(dthuect.getIddonthuect()));
            }
            dthueDAO.delete(iddonthue);
            loaddataDonThue();
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
            TAB.setSelectedIndex(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xoá thất bại\n" + e.getMessage());
        }
    }

    public void resetForm() {
        loaddataDonThue();
        model_donthuect.setRowCount(0);
        txt_iddonthue2.setEnabled(true);
        txt_iddonthue2.setEditable(true);
        btn_xoa.setEnabled(false);

        txt_iddonthue2.setText("");
        txt_idKhach.setText("");
        txt_tenkhach.setText("");
        txt_tiendambao.setText("");
        txt_tienphat.setText("");
        txt_khachdua.setText("");
        txt_thoilai.setText("");
        txt_ngaythue.setText("");
        txt_ngaytradukien.setText("");
        txt_manv.setText("");
        txt_ngaytra.setText("");
        txt_tongtien.setText("");
    }

    public void resetHDCT() {
        txt_iddonthue2.setText("");
        if (model_donthuect != null) {
            model_donthuect.setRowCount(0);
        }
    }

    public void loaddataDonThue() {
        model_donthue = (DefaultTableModel) tbl_donthue.getModel();
        model_donthue.setColumnIdentifiers(new Object[]{"ID Đơn Thuê", "Tên khách", "Số điện thoại", "Ngày thuê", "Trạng thái"});
        model_donthue.setRowCount(0);
        try {
            List<DonThue> list = dthueDAO.selectAll();
            for (DonThue dthue : list) {
                KhachHang kh = khDAO.select_byID(dthue.getIdkhach());
                Object[] row = {dthue.getIddonthue(), kh.getHotenkhach(), kh.getSdt(), dthue.getNgaythue(), dthue.getTrangthai() ? "Hoàn thành" : "Chưa hoàn thành"};
                model_donthue.addRow(row);
            }
            if (model_donthue.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                model_donthue.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataDonThueCT(String iddonthue) {
        model_donthuect = (DefaultTableModel) tbl_donthueCT.getModel();
        model_donthuect.setColumnIdentifiers(new Object[]{"ID Đơn thuê CT", "Tên sách", "Số lượng", "Giá thuê", "Thành tiền"});
        model_donthuect.setRowCount(0);
        try {
            List<DonThueChiTiet> list = dthuectDAO.select_by_HD(iddonthue);
            for (DonThueChiTiet dthueCT : list) {
                String tensach = sachDAO.getTenSach(dthueCT.getIdsach());
                String giaban = sachDAO.getGiaBan(dthueCT.getIdsach());
                Double thanhtien = dthueCT.getSoluong() * Double.parseDouble(giaban);
                Object[] row = {dthueCT.getIddonthuect(), tensach, dthueCT.getSoluong(), giaban, thanhtien};
                model_donthuect.addRow(row);
            }
            if (model_donthuect.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                model_donthuect.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean batloi_tk() {
        String ngaytra = txt_tienphat.getText();
        String tienphat = txt_ngaytra.getText();

        String loi = "";

        if (tienphat.equalsIgnoreCase("")) {
            loi += "Tiền phạt\n";
        }
        if (ngaytra.equalsIgnoreCase("")) {
            loi += "Ngày trả\n";
        }
        if (!loi.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "--Vui lòng kiểm tra lại thông tin!!--\n" + loi, "Lỗi", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    public void timkiemDonthue() {
        String tukhoa = txt_timkiemdonthue.getText();
        model_donthue = (DefaultTableModel) tbl_donthue.getModel();
        model_donthue.setColumnIdentifiers(new Object[]{"ID Đơn Thuê", "Tên khách", "Số điện thoại", "Ngày thuê", "Trạng thái"});
        model_donthue.setRowCount(0);
        try {
            List<DonThue> list = dthueDAO.timDthue(tukhoa);
            for (DonThue dthue : list) {
                KhachHang kh = khDAO.select_byID(dthue.getIdkhach());
                Object[] row = {dthue.getIddonthue(), kh.getHotenkhach(), kh.getSdt(), dthue.getNgaythue(), dthue.getTrangthai() ? "Hoàn thành" : "Chưa hoàn thành"};
                model_donthue.addRow(row);
            }
            if (model_donthue.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                model_donthue.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        POPUP = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        calendar1 = new raven.calendar.Calendar();
        TAB = new javax.swing.JTabbedPane();
        HoadonPanel = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        txt_timkiemdonthue = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_donthue = new javax.swing.JTable();
        crazyPanel4 = new raven.crazypanel.CrazyPanel();
        btn_reset1 = new javax.swing.JButton();
        HdctPanel = new raven.crazypanel.CrazyPanel();
        crazyPanel6 = new raven.crazypanel.CrazyPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_donthueCT = new javax.swing.JTable();
        crazyPanel7 = new raven.crazypanel.CrazyPanel();
        jLabel7 = new javax.swing.JLabel();
        txt_iddonthue2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_idKhach = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_manv = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_ngaythue = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_ngaytradukien = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_ngaytra = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        crazyPanel8 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_tenkhach = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_tiendambao = new javax.swing.JTextField();
        btn_resettiendambao = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txt_tienphat = new javax.swing.JTextField();
        lbl_ngaytre = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        crazyPanel9 = new raven.crazypanel.CrazyPanel();
        jLabel18 = new javax.swing.JLabel();
        txt_khachdua = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txt_thoilai = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txt_tongtien = new javax.swing.JTextField();
        crazyPanel5 = new raven.crazypanel.CrazyPanel();
        btn_xoa = new javax.swing.JButton();
        btn_capnhat = new javax.swing.JButton();

        calendar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                calendar1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(calendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(calendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        HoadonPanel.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background;[light]border:0,0,0,0,shade(@background,5%),,20;[dark]border:0,0,0,0,tint(@background,5%),,20",
            null
        ));
        HoadonPanel.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap,fill,insets 15",
            "[fill]",
            "[grow 0][grow 0][fill]",
            null
        ));

        crazyPanel2.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "JTextField.placeholderText=Tìm kiếm;background:@background",
                "background:lighten(@background,8%)",
                "background:lighten(@background,8%)",
                "background:lighten(@background,8%)"
            }
        ));
        crazyPanel2.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[][]",
            "",
            new String[]{
                "width 200"
            }
        ));

        txt_timkiemdonthue.setForeground(new java.awt.Color(153, 153, 153));
        txt_timkiemdonthue.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_timkiemdonthueCaretUpdate(evt);
            }
        });
        txt_timkiemdonthue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txt_timkiemdonthueMouseExited(evt);
            }
        });
        crazyPanel2.add(txt_timkiemdonthue);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("QUẢN LÝ ĐƠN THUÊ");
        crazyPanel2.add(jLabel4);

        HoadonPanel.add(crazyPanel2);

        tbl_donthue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_donthue.setEditingColumn(0);
        tbl_donthue.setEditingRow(0);
        tbl_donthue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_donthueMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_donthue);

        HoadonPanel.add(jScrollPane1);

        crazyPanel4.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "background:lighten(@background,8%)",
                "background:lighten(@background,8%)",
                "background:lighten(@background,8%)",
                "background:lighten(@background,8%)"
            }
        ));
        crazyPanel4.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[][]",
            "",
            new String[]{
                "width 200"
            }
        ));

        btn_reset1.setText("Làm mới");
        btn_reset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reset1ActionPerformed(evt);
            }
        });
        crazyPanel4.add(btn_reset1);

        HoadonPanel.add(crazyPanel4);

        TAB.addTab("Hoá đơn", HoadonPanel);

        HdctPanel.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background;[light]border:0,0,0,0,shade(@background,5%),,20;[dark]border:0,0,0,0,tint(@background,5%),,20",
            null
        ));
        HdctPanel.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap,fill,insets 15",
            "[fill]",
            "[grow 0][grow 0][fill]",
            null
        ));

        crazyPanel6.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "JTextField.placeholderText=Tìm kiếm;background:@background",
                "background:lighten(@background,8%)",
                "background:lighten(@background,8%)",
                "background:lighten(@background,8%)"
            }
        ));
        crazyPanel6.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[][]",
            "",
            new String[]{
                "width 200"
            }
        ));
        crazyPanel6.add(jLabel12);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("ĐƠN THUÊ CHI TIẾT");
        crazyPanel6.add(jLabel6);

        HdctPanel.add(crazyPanel6);

        tbl_donthueCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_donthueCT.setEditingColumn(0);
        tbl_donthueCT.setEditingRow(0);
        tbl_donthueCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_donthueCTMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_donthueCT);

        HdctPanel.add(jScrollPane2);

        crazyPanel7.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background"
            }
        ));
        crazyPanel7.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 4",
            "[][][][]",
            "[][]",
            new String[]{
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200"
            }
        ));
        crazyPanel7.setName(""); // NOI18N

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("ID Đơn thuê");
        crazyPanel7.add(jLabel7);

        txt_iddonthue2.setEditable(false);
        txt_iddonthue2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_iddonthue2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_iddonthue2.setToolTipText("");
        txt_iddonthue2.setEnabled(false);
        crazyPanel7.add(txt_iddonthue2);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("ID Khách");
        crazyPanel7.add(jLabel8);

        txt_idKhach.setEditable(false);
        txt_idKhach.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_idKhach.setEnabled(false);
        crazyPanel7.add(txt_idKhach);

        HdctPanel.add(crazyPanel7);

        jSeparator3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        HdctPanel.add(jSeparator3);

        crazyPanel3.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background"
            }
        ));
        crazyPanel3.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 8",
            "[][][][][][][][]",
            "[][][]",
            new String[]{
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200"
            }
        ));
        crazyPanel3.setName(""); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Mã NV");
        crazyPanel3.add(jLabel3);

        txt_manv.setEditable(false);
        txt_manv.setEnabled(false);
        crazyPanel3.add(txt_manv);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Ngày thuê");
        crazyPanel3.add(jLabel2);

        txt_ngaythue.setEditable(false);
        txt_ngaythue.setToolTipText("");
        txt_ngaythue.setEnabled(false);
        crazyPanel3.add(txt_ngaythue);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Ngày trả dự kiến");
        crazyPanel3.add(jLabel5);

        txt_ngaytradukien.setEditable(false);
        txt_ngaytradukien.setEnabled(false);
        crazyPanel3.add(txt_ngaytradukien);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Ngày trả");
        crazyPanel3.add(jLabel9);

        txt_ngaytra.setEditable(false);
        txt_ngaytra.setEnabled(false);
        txt_ngaytra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_ngaytraMouseClicked(evt);
            }
        });
        crazyPanel3.add(txt_ngaytra);

        HdctPanel.add(crazyPanel3);

        jSeparator5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        HdctPanel.add(jSeparator5);

        crazyPanel8.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background"
            }
        ));
        crazyPanel8.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 8",
            "[][][][][][]",
            "[][][]",
            new String[]{
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 20",
                "width 80",
                "width 200"
            }
        ));
        crazyPanel8.setName(""); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Tên khách");
        crazyPanel8.add(jLabel1);

        txt_tenkhach.setEditable(false);
        txt_tenkhach.setEnabled(false);
        crazyPanel8.add(txt_tenkhach);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Tiền đảm bảo");
        crazyPanel8.add(jLabel10);

        txt_tiendambao.setEditable(false);
        txt_tiendambao.setEnabled(false);
        crazyPanel8.add(txt_tiendambao);

        btn_resettiendambao.setText("...");
        btn_resettiendambao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resettiendambaoActionPerformed(evt);
            }
        });
        crazyPanel8.add(btn_resettiendambao);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel15.setText("Tiền phạt");
        crazyPanel8.add(jLabel15);

        txt_tienphat.setEditable(false);
        txt_tienphat.setToolTipText("");
        txt_tienphat.setEnabled(false);
        txt_tienphat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_tienphatMouseClicked(evt);
            }
        });
        crazyPanel8.add(txt_tienphat);
        crazyPanel8.add(lbl_ngaytre);

        HdctPanel.add(crazyPanel8);

        jSeparator4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        HdctPanel.add(jSeparator4);

        crazyPanel9.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background"
            }
        ));
        crazyPanel9.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 6",
            "[][][][][][]",
            "[][][]",
            new String[]{
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200",
                "width 80",
                "width 200"
            }
        ));
        crazyPanel9.setName(""); // NOI18N

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel18.setText("Khách đưa");
        crazyPanel9.add(jLabel18);

        txt_khachdua.setEditable(false);
        txt_khachdua.setEnabled(false);
        crazyPanel9.add(txt_khachdua);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel19.setText("Thối lại");
        crazyPanel9.add(jLabel19);

        txt_thoilai.setEditable(false);
        txt_thoilai.setEnabled(false);
        crazyPanel9.add(txt_thoilai);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel20.setText("Thành tiền");
        crazyPanel9.add(jLabel20);

        txt_tongtien.setEditable(false);
        txt_tongtien.setEnabled(false);
        crazyPanel9.add(txt_tongtien);

        HdctPanel.add(crazyPanel9);

        crazyPanel5.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1"
            }
        ));
        crazyPanel5.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[][]",
            "",
            new String[]{
                "width 100",
                "width 100",
                "width 100",
                "width 100"
            }
        ));

        btn_xoa.setText("Xoá");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });
        crazyPanel5.add(btn_xoa);

        btn_capnhat.setText("Cập nhật");
        btn_capnhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capnhatActionPerformed(evt);
            }
        });
        crazyPanel5.add(btn_capnhat);

        HdctPanel.add(crazyPanel5);

        TAB.addTab("Hoá đơn chi tiết", HdctPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1153, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(TAB, javax.swing.GroupLayout.DEFAULT_SIZE, 1141, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 604, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(TAB, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void calendar1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calendar1MousePressed
        if (evt.getClickCount() == 2) {
            POPUP.setVisible(false);
        }
    }//GEN-LAST:event_calendar1MousePressed

    private void tbl_donthueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_donthueMouseClicked
        if (evt.getClickCount() == 2) {
            txt_ngaytra.setText("");
            txt_tienphat.setText("");
            lbl_ngaytre.setText("");
            TAB.setSelectedIndex(1);
            index = tbl_donthue.getSelectedRow();
            try {
                fillFormDonThue();
                String idhoadon = txt_iddonthue2.getText();
                loadDataDonThueCT(idhoadon);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi\n" + e.getMessage());
            }
        }
    }//GEN-LAST:event_tbl_donthueMouseClicked

    private void btn_reset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reset1ActionPerformed
        loaddataDonThue();
    }//GEN-LAST:event_btn_reset1ActionPerformed

    private void tbl_donthueCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_donthueCTMouseClicked
        //        if (evt.getClickCount() == 2) {
        //            index = tbl_hoadonCT.getSelectedRow();
        //            try {
        //                fillFormHDCT();
        //            } catch (Exception e) {
        //                JOptionPane.showMessageDialog(this, "Lỗi\n" + e.getMessage());
        //            }
        //        }
    }//GEN-LAST:event_tbl_donthueCTMouseClicked

    private void txt_ngaytraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_ngaytraMouseClicked
        showPopup();
    }//GEN-LAST:event_txt_ngaytraMouseClicked

    private void btn_resettiendambaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resettiendambaoActionPerformed
        int choice = JOptionPane.showConfirmDialog(null, "Xác nhận hoàn phí đảm bảo cho khách?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            txt_tiendambao.setText("0");
        } else if (choice == JOptionPane.NO_OPTION) {
            return;
        } else {
            return;
        }
    }//GEN-LAST:event_btn_resettiendambaoActionPerformed

    private void txt_tienphatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_tienphatMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tienphatMouseClicked

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        int choice = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            xoaDonThue();
        } else if (choice == JOptionPane.NO_OPTION) {
            return;
        } else {
            return;
        }
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_capnhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capnhatActionPerformed
        if (batloi_tk()) {
            Double tiendb = Double.parseDouble(txt_tiendambao.getText());
            if (tiendb == 0) {
                suaDonThue();
            } else {
                int choice = JOptionPane.showConfirmDialog(null, "Bạn đã hoàn phí đảm bảo cho khách chưa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    //System.out.println("Người dùng đã chọn YES");
                    return;
                } else if (choice == JOptionPane.NO_OPTION) {
                    return;
                } else {
                    return;
                }
            }
        }
    }//GEN-LAST:event_btn_capnhatActionPerformed

    private void txt_timkiemdonthueCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemdonthueCaretUpdate
        timkiemDonthue();
    }//GEN-LAST:event_txt_timkiemdonthueCaretUpdate

    private void txt_timkiemdonthueMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_timkiemdonthueMouseExited
        loaddataDonThue();
    }//GEN-LAST:event_txt_timkiemdonthueMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private raven.crazypanel.CrazyPanel HdctPanel;
    private raven.crazypanel.CrazyPanel HoadonPanel;
    private javax.swing.JPopupMenu POPUP;
    private javax.swing.JTabbedPane TAB;
    private javax.swing.JButton btn_capnhat;
    private javax.swing.JButton btn_reset1;
    private javax.swing.JButton btn_resettiendambao;
    private javax.swing.JButton btn_xoa;
    private raven.calendar.Calendar calendar1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private raven.crazypanel.CrazyPanel crazyPanel4;
    private raven.crazypanel.CrazyPanel crazyPanel5;
    private raven.crazypanel.CrazyPanel crazyPanel6;
    private raven.crazypanel.CrazyPanel crazyPanel7;
    private raven.crazypanel.CrazyPanel crazyPanel8;
    private raven.crazypanel.CrazyPanel crazyPanel9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lbl_ngaytre;
    private javax.swing.JTable tbl_donthue;
    private javax.swing.JTable tbl_donthueCT;
    private javax.swing.JTextField txt_idKhach;
    private javax.swing.JTextField txt_iddonthue2;
    private javax.swing.JTextField txt_khachdua;
    private javax.swing.JTextField txt_manv;
    private javax.swing.JTextField txt_ngaythue;
    private javax.swing.JTextField txt_ngaytra;
    private javax.swing.JTextField txt_ngaytradukien;
    private javax.swing.JTextField txt_tenkhach;
    private javax.swing.JTextField txt_thoilai;
    private javax.swing.JTextField txt_tiendambao;
    private javax.swing.JTextField txt_tienphat;
    private javax.swing.JTextField txt_timkiemdonthue;
    private javax.swing.JTextField txt_tongtien;
    // End of variables declaration//GEN-END:variables
}
