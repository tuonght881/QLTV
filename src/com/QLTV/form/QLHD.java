/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.HoaDonChiTietDAO;
import com.QLTV.dao.HoaDonDAO;
import com.QLTV.dao.SachDAO;
import com.QLTV.dao.TacGiaDAO;
import com.QLTV.dao.TheLoaiDAO;
import com.QLTV.dao.ViTriDAO;
import com.QLTV.entity.HoaDon;
import com.QLTV.entity.HoaDonChiTiet;
import com.QLTV.entity.Sach;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import raven.calendar.model.ModelDate;
import raven.calendar.utils.CalendarSelectedListener;

/**
 *
 * @author Tuong
 */
public class QLHD extends javax.swing.JPanel {
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
    int index = -1;
    DefaultTableModel model_hd, model_hdct;
    /**
     * Creates new form QLHD
     */
    public QLHD() {
        initComponents();
        applyTableStyle(tbl_hoadon);
        applyTableStyle(tbl_hoadonCT);
        loaddataHoaDon();
        btn_xoa.setEnabled(false);
        TAB.setEnabledAt(1, false);
        POPUP.add(jPanel1);
        calendar1.addCalendarSelectedListener(new CalendarSelectedListener() {
            @Override
            public void selected(MouseEvent evt, ModelDate date) {
                ngay = date.toDate();
                String ngayF = sdf.format(ngay);
                txt_ngaytao.setText(ngayF.toString());
                System.out.println("=>" + ngayF);
            }
        });
    }
private void showPopup() {
        //int x = txt_ngaysinh.getLocationOnScreen().x;
        //int y = txt_ngaysinh.getLocationOnScreen().y + txt_ngaysinh.getHeight();
        POPUP.show(txt_ngaytao, 0, txt_ngaytao.getHeight());
    }

    public void setFormHD(HoaDon hd) throws ParseException {
        txt_idhd2.setText(hd.getIdhoadon());
        txt_manv.setText(hd.getManv());
        txt_ngaytao.setText(hd.getNgaytao());
        String khachdua = D_format.format(hd.getKhachdua());
        String thoilai = D_format.format(hd.getThoilai());
        String thanhtien = D_format.format(hd.getThanhtien());
        txt_khachdua.setText(khachdua);
        txt_thoilai.setText(thoilai);
        txt_thanhtien.setText(thanhtien);
    }

    public void setFormHDCT(HoaDonChiTiet hdct) throws ParseException {
        txt_idhd2.setText(hdct.getIdhoadon());
        Sach sach = sachDAO.select_byID(hdct.getIdsach());
        String tensach = sach.getTensach();
    }

    public HoaDon getFormHD() throws ParseException {
        HoaDon hdNEW = new HoaDon();
        hdNEW.setIdhoadon(txt_idhd2.getText());
        hdNEW.setManv(txt_manv.getText());
        hdNEW.setNgaytao(txt_ngaytao.getText());
        hdNEW.setKhachdua(Double.parseDouble(txt_khachdua.getText()));
        hdNEW.setThoilai(Double.parseDouble(txt_thoilai.getText()));
        hdNEW.setThanhtien(Double.parseDouble(txt_thanhtien.getText()));

        return hdNEW;
    }

    public HoaDonChiTiet getFormHDCT() throws ParseException {
        HoaDonChiTiet hdctNEW = new HoaDonChiTiet();
        hdctNEW.setIdhoadon(txt_idhd2.getText());



        return hdctNEW;
    }

    public void fillFormHD() throws ParseException {
        txt_idhd2.setEnabled(false);
        txt_idhd2.setEditable(false);

        btn_xoa.setEnabled(true);

        String idhd = (String) tbl_hoadon.getValueAt(index, 0);
        HoaDon hd = hdDAO.select_byID(idhd);
        if (hd == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setFormHD(hd);
        }
    }

    public void fillFormHDCT() throws ParseException {
        txt_idhd2.setEnabled(false);
        txt_idhd2.setEditable(false);


        int idhdct = (Integer) tbl_hoadonCT.getValueAt(index, 0);
        HoaDonChiTiet hdct = hdctDAO.select_byID_int(idhdct);
        if (hdct == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setFormHDCT(hdct);
        }
    }

    public void themHD() {
        if (batloi_tk()) {
            try {
                HoaDon hdNew = getFormHD();
                hdDAO.insert(hdNew);
                loaddataHoaDon();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!\n" + e.getMessage());
            }
        }
    }

    public void suaHD() {
        if (batloi_tk()) {
            try {
                HoaDon HDnew = getFormHD();
                hdDAO.update(HDnew);
                loaddataHoaDon();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!\n" + e.getMessage());
            }
        }
    }

    public void suaHDCT() {
        if (batloi_tk()) {
            try {
                HoaDonChiTiet HDCTnew = getFormHDCT();
                hdctDAO.update(HDCTnew);
                loaddataHoaDon();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!\n" + e.getMessage());
            }
        }
    }

    public void xoaHD() {
        try {
            String idhoadon = txt_idhd2.getText();
            List<HoaDonChiTiet> list = hdctDAO.select_by_HD(idhoadon);
            for (HoaDonChiTiet hdct : list) {
                hdctDAO.delete_int(hdct.getIdhoadonct());
            }
            hdDAO.delete(idhoadon);
            loaddataHoaDon();
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xoá thất bại\n" + e.getMessage());
        }
    }

    public void resetForm() {
        loaddataHoaDon();
        txt_idhd2.setEnabled(true);
        txt_idhd2.setEditable(true);
        btn_xoa.setEnabled(false);

        txt_idhd2.setText("");
        txt_ngaytao.setText("");
        txt_khachdua.setText("");
        txt_manv.setText("");
        txt_thoilai.setText("");
        txt_thanhtien.setText("");
    }

    public void resetHDCT() {
        txt_idhd2.setText("");
        if (model_hdct != null) {
            model_hdct.setRowCount(0);
        }
    }

    public void loaddataHoaDon() {
        model_hd = (DefaultTableModel) tbl_hoadon.getModel();
        model_hd.setColumnIdentifiers(new Object[]{"ID Hoá đơn", "Mã nhân viên", "Ngày tạo", "Thành tiền"});
        model_hd.setRowCount(0);
        try {
            List<HoaDon> list = hdDAO.selectAll();
            for (HoaDon hd : list) {
                Object[] row = {hd.getIdhoadon(), hd.getManv(), hd.getNgaytao(), D_format.format(hd.getThanhtien())};
                model_hd.addRow(row);
            }
            if (model_hd.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                model_hd.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataHDCT(String idhoadon) {
        model_hdct = (DefaultTableModel) tbl_hoadonCT.getModel();
        model_hdct.setColumnIdentifiers(new Object[]{"ID Hoá đơn CT", "Tên sách", "Số lượng", "Giá bán", "Thành tiền"});
        model_hdct.setRowCount(0);
        try {
            List<HoaDonChiTiet> list = hdctDAO.select_by_HD(idhoadon);
            for (HoaDonChiTiet hdct : list) {
                String tensach = sachDAO.getTenSach(hdct.getIdsach());
                String giaban = sachDAO.getGiaBan(hdct.getIdsach());
                Double thanhtien = hdct.getSoluong() * Double.parseDouble(giaban);
                Object[] row = {hdct.getIdhoadonct(), tensach, hdct.getSoluong(), giaban, thanhtien};
                model_hdct.addRow(row);
            }
            if (model_hdct.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                model_hdct.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timkiemHD() {
        String tukhoa = txt_timkiemHD.getText();
        DefaultTableModel modelHD = (DefaultTableModel) tbl_hoadon.getModel();
        modelHD.setColumnIdentifiers(new Object[]{"ID Hoá đơn", "Mã nhân viên", "Ngày tạo", "Thành tiền"});
        modelHD.setRowCount(0);
        try {
            List<HoaDon> list = hdDAO.timHD(tukhoa);
            for (HoaDon hd : list) {
                Object[] row = {hd.getIdhoadon(), hd.getManv(), hd.getNgaytao(), D_format.format(hd.getThanhtien())};
                modelHD.addRow(row);
            }
            if (modelHD.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                modelHD.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean batloi_tk() {
        String idsach = txt_idhd2.getText();
        String theloai = txt_ngaytao.getText();
        String tensach = txt_manv.getText();
        String tacgia = txt_khachdua.getText();
        String sl = txt_thanhtien.getText();
        String vitri = txt_thoilai.getText();

        String loi = "";

        if (idsach.equalsIgnoreCase("")) {
            loi += "ID sách\n";
        }
//        else {
//            if (idsach.length() != 5) {
//                loi += "ID tài khoản phải có 5 ký tự\n";
//            }
//        }
        if (theloai.equalsIgnoreCase("")) {
            loi += "Thể loại\n";
        }
        if (tensach.equalsIgnoreCase("")) {
            loi += "Tên sách\n";
        }
        if (tacgia.equalsIgnoreCase("")) {
            loi += "Tác giả\n";
        }
        if (vitri.equalsIgnoreCase("")) {
            loi += "Vị trí\n";
        }
        if (sl.equalsIgnoreCase("")) {
            loi += "Số lượng\n";
        }
        if (!loi.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "--Vui lòng kiểm tra lại thông tin!!--\n" + loi, "Lỗi", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    private void applyTableStyle(JTable table) {
        //btn_them.setIcon(new FlatSVGIcon("/asda/asdasd", 0.35f));
        //txt_timkiem.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, new FlatSVGIcon(""),0.35f);
        //changeScrollStyle
        JScrollPane scroll = (JScrollPane) table.getParent().getParent();
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Table.background;"
                + "track:$Table.background;"
                + "trackArc:999");
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
        table.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
        //Table Alignment
        table.getTableHeader().setDefaultRenderer(getAlignmentCellRender(table.getTableHeader().getDefaultRenderer(), true));
        table.setDefaultRenderer(Object.class, getAlignmentCellRender(table.getDefaultRenderer(Object.class), false));
        // Điều chỉnh chiều ngang của cột thứ 7
        TableColumn column = table.getColumnModel().getColumn(6); // Cột thứ 7 (index bắt đầu từ 0)
        column.setPreferredWidth(500);
    }

    private TableCellRenderer getAlignmentCellRender(TableCellRenderer oldRender, boolean header) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component com = oldRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (com instanceof JLabel) {
                    JLabel label = (JLabel) com;
                    if (column == 0 || column == 4) {
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                    } else if (column == 2 || column == 3) {
                        label.setHorizontalAlignment(SwingConstants.TRAILING);
                    } else {
                        label.setHorizontalAlignment(SwingConstants.LEADING);
                    }
//                    if (header == false) {
//                        if (column == 4) {
//                            if (Double.parseDouble(value.toString()) > 0) {
//                                com.setForeground(new Color(17, 182, 60));
//                                label.setText("+" + value);
//                            } else {
//                                com.setForeground(new Color(202, 48, 48));
//                            }
//                        } else {
//                            if (isSelected) {
//                                com.setForeground(table.getSelectionForeground());
//                            } else {
//                                com.setForeground(table.getForeground());
//                            }
//                        }
//                    }
                }
                return com;
            }
        };
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
        txt_timkiemHD = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_hoadon = new javax.swing.JTable();
        crazyPanel4 = new raven.crazypanel.CrazyPanel();
        btn_reset1 = new javax.swing.JButton();
        HdctPanel = new raven.crazypanel.CrazyPanel();
        crazyPanel6 = new raven.crazypanel.CrazyPanel();
        txt_timkiemHDCT = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        crazyPanel7 = new raven.crazypanel.CrazyPanel();
        jLabel7 = new javax.swing.JLabel();
        txt_idhd2 = new javax.swing.JTextField();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_manv = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_ngaytao = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_khachdua = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_thoilai = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txt_thanhtien = new javax.swing.JTextField();
        crazyPanel5 = new raven.crazypanel.CrazyPanel();
        btn_xoa = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_hoadonCT = new javax.swing.JTable();

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

        txt_timkiemHD.setForeground(new java.awt.Color(153, 153, 153));
        txt_timkiemHD.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_timkiemHDCaretUpdate(evt);
            }
        });
        crazyPanel2.add(txt_timkiemHD);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("QUẢN LÝ HOÁ ĐƠN");
        crazyPanel2.add(jLabel4);

        HoadonPanel.add(crazyPanel2);

        tbl_hoadon.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_hoadon.setEditingColumn(0);
        tbl_hoadon.setEditingRow(0);
        tbl_hoadon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_hoadonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_hoadon);

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

        txt_timkiemHDCT.setForeground(new java.awt.Color(153, 153, 153));
        txt_timkiemHDCT.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_timkiemHDCTCaretUpdate(evt);
            }
        });
        crazyPanel6.add(txt_timkiemHDCT);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("HOÁ ĐƠN CHI TIẾT");
        crazyPanel6.add(jLabel6);

        HdctPanel.add(crazyPanel6);
        HdctPanel.add(jSeparator2);

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
        jLabel7.setText("ID Hoá đơn");
        crazyPanel7.add(jLabel7);

        txt_idhd2.setEditable(false);
        txt_idhd2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_idhd2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_idhd2.setToolTipText("");
        txt_idhd2.setEnabled(false);
        crazyPanel7.add(txt_idhd2);

        HdctPanel.add(crazyPanel7);

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
        crazyPanel3.setName(""); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Mã NV");
        crazyPanel3.add(jLabel3);
        crazyPanel3.add(txt_manv);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Ngày tạo");
        crazyPanel3.add(jLabel2);

        txt_ngaytao.setEditable(false);
        txt_ngaytao.setToolTipText("");
        txt_ngaytao.setEnabled(false);
        txt_ngaytao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_ngaytaoMouseClicked(evt);
            }
        });
        crazyPanel3.add(txt_ngaytao);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Khách đưa");
        crazyPanel3.add(jLabel5);
        crazyPanel3.add(txt_khachdua);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Thối lại");
        crazyPanel3.add(jLabel9);
        crazyPanel3.add(txt_thoilai);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Thành tiền");
        crazyPanel3.add(jLabel11);
        crazyPanel3.add(txt_thanhtien);

        HdctPanel.add(crazyPanel3);

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

        btn_xoa.setText("Xoá");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });
        crazyPanel5.add(btn_xoa);

        btn_reset.setText("Làm mới");
        btn_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resetActionPerformed(evt);
            }
        });
        crazyPanel5.add(btn_reset);

        HdctPanel.add(crazyPanel5);

        tbl_hoadonCT.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_hoadonCT.setEditingColumn(0);
        tbl_hoadonCT.setEditingRow(0);
        tbl_hoadonCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_hoadonCTMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_hoadonCT);

        HdctPanel.add(jScrollPane2);

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

    private void txt_timkiemHDCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemHDCaretUpdate
        timkiemHD();
    }//GEN-LAST:event_txt_timkiemHDCaretUpdate

    private void tbl_hoadonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_hoadonMouseClicked
        if (evt.getClickCount() == 2) {
            TAB.setSelectedIndex(1);
            index = tbl_hoadon.getSelectedRow();
            try {
                fillFormHD();
                String idhoadon = txt_idhd2.getText();
                loadDataHDCT(idhoadon);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi\n" + e.getMessage());
            }
        }
    }//GEN-LAST:event_tbl_hoadonMouseClicked

    private void btn_reset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reset1ActionPerformed
        loaddataHoaDon();
    }//GEN-LAST:event_btn_reset1ActionPerformed

    private void txt_timkiemHDCTCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemHDCTCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_timkiemHDCTCaretUpdate

    private void txt_ngaytaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_ngaytaoMouseClicked
        showPopup();
    }//GEN-LAST:event_txt_ngaytaoMouseClicked

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        int choice = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.out.println("Người dùng đã chọn YES");
            xoaHD();
        } else if (choice == JOptionPane.NO_OPTION) {
            return;
        } else {
            return;
        }
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        resetForm();
        resetHDCT();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void tbl_hoadonCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_hoadonCTMouseClicked
        //        if (evt.getClickCount() == 2) {
            //            index = tbl_hoadonCT.getSelectedRow();
            //            try {
                //                fillFormHDCT();
                //            } catch (Exception e) {
                //                JOptionPane.showMessageDialog(this, "Lỗi\n" + e.getMessage());
                //            }
            //        }
    }//GEN-LAST:event_tbl_hoadonCTMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private raven.crazypanel.CrazyPanel HdctPanel;
    private raven.crazypanel.CrazyPanel HoadonPanel;
    private javax.swing.JPopupMenu POPUP;
    private javax.swing.JTabbedPane TAB;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_reset1;
    private javax.swing.JButton btn_xoa;
    private raven.calendar.Calendar calendar1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private raven.crazypanel.CrazyPanel crazyPanel4;
    private raven.crazypanel.CrazyPanel crazyPanel5;
    private raven.crazypanel.CrazyPanel crazyPanel6;
    private raven.crazypanel.CrazyPanel crazyPanel7;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable tbl_hoadon;
    private javax.swing.JTable tbl_hoadonCT;
    private javax.swing.JTextField txt_idhd2;
    private javax.swing.JTextField txt_khachdua;
    private javax.swing.JTextField txt_manv;
    private javax.swing.JTextField txt_ngaytao;
    private javax.swing.JTextField txt_thanhtien;
    private javax.swing.JTextField txt_thoilai;
    private javax.swing.JTextField txt_timkiemHD;
    private javax.swing.JTextField txt_timkiemHDCT;
    // End of variables declaration//GEN-END:variables
}
