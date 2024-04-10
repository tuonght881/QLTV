/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.HoaDonChiTietDAO;
import com.QLTV.dao.HoaDonDAO;
import com.QLTV.dao.SachDAO;
import com.QLTV.dao.TacGiaDAO;
import com.QLTV.dao.ViTriDAO;
import com.QLTV.entity.HoaDon;
import com.QLTV.entity.HoaDonChiTiet;
import com.QLTV.entity.Sach;
import com.QLTV.entity.TacGia;
import com.QLTV.entity.ViTri;
import com.QLTV.utils.XAuth;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import raven.tabbed.TabbedForm;
import raven.toast.Notifications;

/**
 *
 * @author Tuong
 */
public final class BAN_SACH extends TabbedForm {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    SachDAO sachDAO = new SachDAO();
    TacGiaDAO tgDAO = new TacGiaDAO();
    ViTriDAO vtDAO = new ViTriDAO();
    HoaDonDAO hdDAO = new HoaDonDAO();
    HoaDonChiTietDAO hdctDAO = new HoaDonChiTietDAO();

    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    DecimalFormat D_format = new DecimalFormat("0.#");
    int index = -1;
    Timer timer;
    DefaultTableModel model_sach, model_hd;
    Double tongcong = 0.0;
    Double khachdua = 0.0;
    Double thoilai = 0.0;

    int id;

    /**
     * Creates new form BAN_SACH
     */
    public BAN_SACH() {
        initComponents();
        txt_manv.setText(XAuth.user.getManv());
        loaddataSach();
        loadHoaDon();
        layngay();
        jLabel2.setVisible(false);
        txt_idhoadon.setVisible(false);
    }

    public void layngay() {
        ActionListener act = (ActionEvent e) -> {
            Date date = new Date();
            String time = sdf.format(date);
            txt_ngaybansach.setText(time);
        };
        timer = new Timer(30000, act);
        timer.setInitialDelay(0);
        timer.start();
    }

    public HoaDon getHoaDonNew() throws ParseException {
        HoaDon hdNEW = new HoaDon();
        //hdNEW.setIdhoadon(Integer.parseInt(txt_idhoadon.getText()));
        hdNEW.setManv(txt_manv.getText());
        hdNEW.setNgaytao(txt_ngaybansach.getText());
        hdNEW.setThanhtien(tongcong);
        hdNEW.setKhachdua(Double.valueOf(txt_khachdua.getText()));
        hdNEW.setThoilai(thoilai);

        return hdNEW;
    }

    public void ThemHoaDon() {
        if (batloi_bansach()) {
            try {
                HoaDon hdNew = getHoaDonNew();
                hdDAO.insert(hdNew);

                HoaDonChiTiet hdctNew = new HoaDonChiTiet();
                //hdctNew.setIdhoadon(Integer.parseInt(txt_idhoadon.getText()));

                for (int i = 0; i < tbl_hoadon.getRowCount(); i++) {
                    hdctNew.setIdhoadon(hdDAO.getmaHD());
                    String idsach = sachDAO.getMasach(tbl_hoadon.getValueAt(i, 0).toString());
                    hdctNew.setIdsach(idsach);
                    hdctNew.setSoluong(Integer.parseInt(tbl_hoadon.getValueAt(i, 2).toString()));
                    hdctDAO.insert(hdctNew);
                    Sach s = sachDAO.select_byID(idsach);
                    int sls = s.getSl();
                    int slsm = sls - (Integer.parseInt(tbl_hoadon.getValueAt(i, 2).toString()));
                    s.setSl(slsm);
                    sachDAO.update(s);
                }

                loaddataSach();
                model_hd.setRowCount(0);
                tongcong = 0.0;
                khachdua = 0.0;
                thoilai = 0.0;
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Thêm hoá đơn mới thành công");
                //JOptionPane.showMessageDialog(this, "Thêm thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!\n" + e.getMessage());
            }
        }
    }

    public void resetForm() {
        loadHoaDon();
        loaddataSach();
        txt_tongcong.setText("");
        txt_khachdua.setText("");
        txt_thoilai.setText("");
        if (model_hd != null) {
            model_hd.setRowCount(0);
        }
        tongcong = 0.0;
        khachdua = 0.0;
        thoilai = 0.0;
    }

    public void loadHoaDon() {
        try {
            List<HoaDon> list = hdDAO.select_al();
            for (HoaDon hd : list) {
                id = hd.getIdhoadon();
            }
            int id2 = id + 1;
            txt_idhoadon.setText(Integer.toString(id2));
        } catch (Exception e) {
        }
    }

    public void loaddataSach() {
        //txt_ngaybansach.setText(dateFormat.format(ngay));
        //txt_manv.setText(XAuth.user.getHoten());
        model_sach = (DefaultTableModel) tbl_sach.getModel();
        model_sach.setColumnIdentifiers(new Object[]{"Tên sách", "Tác giả", "Vị trí", "Số lượng", "Giá bán"});
        model_sach.setRowCount(0);
        try {
            List<Sach> list = sachDAO.selectOnStock();
            for (Sach sach : list) {
                TacGia tg = tgDAO.select_byID(sach.getIdtacgia());
                ViTri vt = vtDAO.select_byID(sach.getIdvitri());
                Object[] row = {sach.getTensach(), tg.getTentg(), vt.getTenvt(), sach.getSl(), D_format.format(sach.getGiaban())};
                model_sach.addRow(row);
            }
            if (model_sach.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                model_sach.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timkiem() {
        String tukhoa = txt_timkiem.getText();
        model_sach = (DefaultTableModel) tbl_sach.getModel();
        model_sach.setColumnIdentifiers(new Object[]{"Tên sách", "Tác giả", "Vị trí", "Số lượng", "Giá bán"});
        model_sach.setRowCount(0);
        try {
            List<Sach> list = sachDAO.timkiemSach(tukhoa);
            for (Sach sach : list) {
                TacGia tg = tgDAO.select_byID(sach.getIdtacgia());
                ViTri vt = vtDAO.select_byID(sach.getIdvitri());
                Object[] row = {sach.getTensach(), tg.getTentg(), vt.getTenvt(), sach.getSl(), D_format.format(sach.getGiaban())};
                model_sach.addRow(row);
            }
            if (model_sach.getRowCount() == 0) {
                Object[] row = {"", "Không tìm thấy"};
                model_sach.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean batloi_bansach() {
        String khachduaString = txt_khachdua.getText();
        String thoilai2 = txt_thoilai.getText();
        //String regex = "^\\d*[1-9]\\d*$";
        boolean isPositiveNumber = khachduaString.matches("^\\d*[1-9]\\d*$");
        String loi = "";
        if (model_hd == null) {
            loi += "Chưa có sách nào trong hoá đơn\n";
        }
        if (khachduaString.equalsIgnoreCase("")) {
            loi += "Khách đưa\n";
        } else if (isPositiveNumber == false) {
            loi += "Vui lòng chỉ nhập số dương\n";
        } else if (khachdua < tongcong) {
            loi += "Khách đưa\n";
        }
        if (thoilai2.equalsIgnoreCase("")) {
            loi += "Thối lại\n";
        }

        if (!loi.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "--Vui lòng kiểm tra lại thông tin!!--\n" + loi, "Lỗi", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    public void checkKhachdua() {
// Thêm DocumentListener để lắng nghe sự kiện thay đổi trong Document
        txt_khachdua.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleDocumentChange(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleDocumentChange(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleDocumentChange(e);
            }

            private void handleDocumentChange(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Document doc = e.getDocument();
                    try {
                        String text = doc.getText(0, doc.getLength());
                        if (!text.isEmpty() && !isNumber(text)) {
                            // Nếu không phải là số, xóa dữ liệu vừa nhập
                            doc.remove(e.getOffset(), e.getLength());
                        }
                    } catch (BadLocationException ex) {
                        //System.out.println("Vui lòng nhập số!");
                    }
                });
            }

            private boolean isNumber(String text) {
                try {
                    double number = Double.parseDouble(text);
                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        });
    }

    void tinhtien() {
        tongcong = 0.0;
        for (int i = 0; i < tbl_hoadon.getRowCount(); i++) {
            tongcong += Double.parseDouble(tbl_hoadon.getValueAt(i, 3).toString());
        }

        txt_tongcong.setText(currencyVN.format(tongcong));
    }

    public void taoHoaDon(MouseEvent evt) throws NumberFormatException, HeadlessException {
        if (evt.getClickCount() == 2) {
            index = tbl_sach.getSelectedRow();
            String sl = JOptionPane.showInputDialog(this, "Nhập số lượng", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
            try {
                while (sl.equalsIgnoreCase("")) {
                    sl = JOptionPane.showInputDialog(this, "Nhập số lượng", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
                }
            } catch (NullPointerException e) {
                //JOptionPane.showMessageDialog(this, "Lỗi\n" + e.getMessage());
            }
            int slc = 0;
            slc = Integer.parseInt(tbl_sach.getValueAt(index, 3).toString());
            if (sl == null) {
            } else if (Integer.parseInt(sl) > slc) {
                JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ!", "Thông báo", JOptionPane.OK_OPTION);
            } else if (sl != null) {
                Object[] row = new Object[4];
                model_hd = (DefaultTableModel) tbl_hoadon.getModel();
                if (index != -1) {
                    String selectBook = model_sach.getValueAt(index, 0).toString();
                    if (tbl_hoadon.getRowCount() <= 0) {
                        row[0] = selectBook;
                        row[1] = model_sach.getValueAt(index, 4).toString();//lấy giá của sách
                        row[2] = sl;
                        Double thanhtien = (Double.parseDouble(sl) * Double.parseDouble(row[1].toString()));
                        row[3] = D_format.format(thanhtien);
                        model_hd.addRow(row);
                        tinhtien();
                    } else {
                        int hang = -1;
                        for (int i = 0; i < tbl_hoadon.getRowCount(); i++) {
                            if (selectBook.equalsIgnoreCase(model_hd.getValueAt(i, 0).toString())) {
                                hang = i;
                                break;
                            } else {
                                hang = -1;
                            }
                        }
                        if (hang == -1) {
                            row[0] = selectBook;
                            row[1] = model_sach.getValueAt(index, 4).toString(); //row 1 là giá sách
                            row[2] = sl;
                            Double thanhtien = (Double.parseDouble(sl) * Double.parseDouble(row[1].toString()));
                            row[3] = D_format.format(thanhtien);
                            model_hd.addRow(row);
                            tinhtien();
                        } else {
                            row[0] = selectBook;
                            row[1] = model_sach.getValueAt(index, 4).toString(); //row 1 là giá sách
                            int sl2 = Integer.parseInt(tbl_hoadon.getValueAt(hang, 2).toString()) + Integer.parseInt(sl);
                            tbl_hoadon.setValueAt(sl2, hang, 2);
                            Double thanhtien = (Double.parseDouble(sl) * Double.parseDouble(row[1].toString()));
                            Double thanhtien2 = Double.valueOf(tbl_hoadon.getValueAt(hang, 3).toString()) + thanhtien;
                            tbl_hoadon.setValueAt(D_format.format(thanhtien2), hang, 3);
                            tinhtien();
                        }
                    }
                }
            }
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

        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        txt_timkiem = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_ngaybansach = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txt_manv = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        crazyPanel4 = new raven.crazypanel.CrazyPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_sach = new javax.swing.JTable();
        crazyPanel8 = new raven.crazypanel.CrazyPanel();
        crazyPanel6 = new raven.crazypanel.CrazyPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_hoadon = new javax.swing.JTable();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_idhoadon = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_tongcong = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txt_khachdua = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_thoilai = new javax.swing.JTextField();
        crazyPanel5 = new raven.crazypanel.CrazyPanel();
        btn_reset = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();

        crazyPanel1.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background;[light]border:0,0,0,0,shade(@background,5%),,20;[dark]border:0,0,0,0,tint(@background,5%),,20",
            null
        ));
        crazyPanel1.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
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
            "[][][][][]push[]",
            "",
            new String[]{
                "width 200",
                "",
                "width 180"
            }
        ));

        txt_timkiem.setForeground(new java.awt.Color(153, 153, 153));
        txt_timkiem.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_timkiemCaretUpdate(evt);
            }
        });
        txt_timkiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txt_timkiemMouseExited(evt);
            }
        });
        crazyPanel2.add(txt_timkiem);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Ngày:");
        crazyPanel2.add(jLabel3);

        txt_ngaybansach.setEnabled(false);
        crazyPanel2.add(txt_ngaybansach);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("ID Nhân viên");
        crazyPanel2.add(jLabel1);

        txt_manv.setEditable(false);
        txt_manv.setToolTipText("");
        txt_manv.setEnabled(false);
        crazyPanel2.add(txt_manv);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("BÁN SÁCH");
        crazyPanel2.add(jLabel4);

        crazyPanel1.add(crazyPanel2);

        crazyPanel4.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background;[light]border:0,0,0,0,shade(@background,5%),,20;[dark]border:0,0,0,0,tint(@background,5%),,20",
            null
        ));
        crazyPanel4.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[]",
            "",
            new String[]{
                "width 800",
                "width 600"
            }
        ));

        tbl_sach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tên sách", "Tác giả", "Vị trí", "Số lượng", "Giá bán"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_sach.setEditingColumn(0);
        tbl_sach.setEditingRow(0);
        tbl_sach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_sachMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_sach);
        if (tbl_sach.getColumnModel().getColumnCount() > 0) {
            tbl_sach.getColumnModel().getColumn(0).setPreferredWidth(100);
        }

        crazyPanel4.add(jScrollPane1);

        crazyPanel8.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            null
        ));
        crazyPanel8.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 1",
            "[]",
            "[][][]",
            null
        ));

        tbl_hoadon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên sách", "Đơn giá", "Số lượng", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_hoadon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_hoadonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_hoadon);

        crazyPanel6.add(jScrollPane2);

        crazyPanel8.add(crazyPanel6);

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
            "wrap 2",
            "[][]",
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

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("ID hoá đơn");
        crazyPanel3.add(jLabel2);

        txt_idhoadon.setEditable(false);
        txt_idhoadon.setToolTipText("");
        txt_idhoadon.setEnabled(false);
        crazyPanel3.add(txt_idhoadon);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Tổng");
        crazyPanel3.add(jLabel9);

        txt_tongcong.setEditable(false);
        txt_tongcong.setEnabled(false);
        crazyPanel3.add(txt_tongcong);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Khách đưa");
        crazyPanel3.add(jLabel11);

        txt_khachdua.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_khachduaCaretUpdate(evt);
            }
        });
        txt_khachdua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_khachduaKeyPressed(evt);
            }
        });
        crazyPanel3.add(txt_khachdua);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Thối lại");
        crazyPanel3.add(jLabel10);

        txt_thoilai.setEditable(false);
        txt_thoilai.setEnabled(false);
        crazyPanel3.add(txt_thoilai);

        crazyPanel8.add(crazyPanel3);

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
            "[][]push[][]",
            "",
            new String[]{
                "width 100",
                "width 100",
                "width 100",
                "width 100"
            }
        ));

        btn_reset.setText("Làm mới");
        btn_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resetActionPerformed(evt);
            }
        });
        crazyPanel5.add(btn_reset);

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });
        crazyPanel5.add(btn_them);

        crazyPanel8.add(crazyPanel5);

        crazyPanel4.add(crazyPanel8);

        crazyPanel1.add(crazyPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 759, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 471, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_timkiemCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemCaretUpdate
        timkiem();
    }//GEN-LAST:event_txt_timkiemCaretUpdate

    public void tinhthoilaii() {
        if (!txt_khachdua.getText().equalsIgnoreCase("")) {
            khachdua = Double.valueOf(txt_khachdua.getText());
            if (khachdua >= tongcong) {
                thoilai = khachdua - tongcong;
                txt_thoilai.setText(currencyVN.format(thoilai));
            } else {
                JOptionPane.showMessageDialog(this, "Kiểm tra lại khách đưa!");
            }
        }
    }
    private void txt_khachduaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_khachduaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tinhthoilaii();
        }
    }//GEN-LAST:event_txt_khachduaKeyPressed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        resetForm();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        ThemHoaDon();
    }//GEN-LAST:event_btn_themActionPerformed

    private void txt_timkiemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_timkiemMouseExited
        loaddataSach();
    }//GEN-LAST:event_txt_timkiemMouseExited

    private void txt_khachduaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_khachduaCaretUpdate
        checkKhachdua();
    }//GEN-LAST:event_txt_khachduaCaretUpdate

    private void tbl_sachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_sachMouseClicked
        taoHoaDon(evt);
    }//GEN-LAST:event_tbl_sachMouseClicked

    private void tbl_hoadonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_hoadonMouseClicked
        if (evt.getClickCount() == 2) {
            int dem = tbl_hoadon.getSelectedRow();
            model_hd.removeRow(dem);
            tinhtien();
        }
    }//GEN-LAST:event_tbl_hoadonMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_them;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private raven.crazypanel.CrazyPanel crazyPanel4;
    private raven.crazypanel.CrazyPanel crazyPanel5;
    private raven.crazypanel.CrazyPanel crazyPanel6;
    private raven.crazypanel.CrazyPanel crazyPanel8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbl_hoadon;
    private javax.swing.JTable tbl_sach;
    private javax.swing.JTextField txt_idhoadon;
    private javax.swing.JTextField txt_khachdua;
    private javax.swing.JTextField txt_manv;
    private javax.swing.JTextField txt_ngaybansach;
    private javax.swing.JTextField txt_thoilai;
    private javax.swing.JTextField txt_timkiem;
    private javax.swing.JTextField txt_tongcong;
    // End of variables declaration//GEN-END:variables
}
