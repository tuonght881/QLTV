/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.HoaDonChiTietDAO;
import com.QLTV.dao.HoaDonDAO;
import com.QLTV.dao.KhachHangDAO;
import com.QLTV.dao.SachDAO;
import com.QLTV.dao.TacGiaDAO;
import com.QLTV.dao.TheLoaiDAO;
import com.QLTV.dao.ViTriDAO;
import com.QLTV.entity.HoaDon;
import com.QLTV.entity.HoaDonChiTiet;
import com.QLTV.entity.Sach;
import com.QLTV.entity.TacGia;
import com.QLTV.entity.TheLoai;
import com.QLTV.entity.ViTri;
import com.QLTV.utils.XAuth;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.raven.properties.SystemProperties;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
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
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Tuong
 */
public class BANSACH_FORM extends javax.swing.JFrame {
    Date ngay;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    SachDAO sachDAO = new SachDAO();
    TheLoaiDAO tlDAO = new TheLoaiDAO();
    TacGiaDAO tgDAO = new TacGiaDAO();
    ViTriDAO vtDAO = new ViTriDAO();
    KhachHangDAO khDAO = new KhachHangDAO();
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
     * Creates new form tesst
     */
    public BANSACH_FORM() {
        initComponents();
        applyTableStyle(tbl_sach);
        applyTableStyle(tbl_hoadon);
        //txt_manv.setText(XAuth.user.getManv());
        loaddataSach();
        loadHoaDon();
        xuongdong();
        ActionListener act = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = new Date();
                String time = sdf.format(date);
                txt_ngaybansach.setText(time);
            }
        };
        timer = new Timer(1000, act);
        timer.setInitialDelay(0);
        timer.start();

    }

    public HoaDon getHoaDonNew() throws ParseException {
        HoaDon hdNEW = new HoaDon();
        hdNEW.setIdhoadon(txt_idhoadon.getText());
        hdNEW.setManv(txt_manv.getText());
        hdNEW.setNgaytao(txt_ngaybansach.getText());
        hdNEW.setThanhtien(tongcong);
        hdNEW.setKhachdua(Double.valueOf(txt_khachdua.getText()));
        hdNEW.setThoilai(thoilai);

        return hdNEW;
    }

    public void xuongdong() {
        // Tạo renderer cho cột đầu tiên (căn lề trái)
        //DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        //leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        // Áp dụng renderer cho cột đầu tiên
        //tbl_hoadon.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        // Tạo renderer cho các cột còn lại (căn lề phải)
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment(SwingConstants.CENTER);
        // Áp dụng renderer cho các cột còn lại
        for (int i = 1; i < tbl_hoadon.getColumnCount(); i++) {
            tbl_hoadon.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }


        tbl_hoadon.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbl_sach.getColumnModel().getColumn(0).setPreferredWidth(300);
        tbl_hoadon.getColumnModel().getColumn(0).setCellRenderer(new MultiLineTableCellRenderer());
        tbl_sach.getColumnModel().getColumn(0).setCellRenderer(new MultiLineTableCellRenderer());
        tbl_sach.getColumnModel().getColumn(1).setCellRenderer(new MultiLineTableCellRenderer());
    }

    public void ThemHoaDon() {
        if (batloi_tk()) {
            try {
                HoaDon hdNew = getHoaDonNew();
                hdDAO.insert(hdNew);

                HoaDonChiTiet hdctNew = new HoaDonChiTiet();
                hdctNew.setIdhoadon(txt_idhoadon.getText());

                for (int i = 0; i < tbl_hoadon.getRowCount(); i++) {
                    hdctNew.setIdhoadon(hdDAO.getmaHD());
                    String idsach = sachDAO.getMasach(tbl_hoadon.getValueAt(i, 0).toString());
                    hdctNew.setIdsach(idsach);
                    hdctNew.setSoluong(Integer.parseInt(tbl_hoadon.getValueAt(i, 2).toString()));
                    hdctDAO.insert(hdctNew);
                }

                loaddataSach();
                model_hd.setRowCount(0);
                JOptionPane.showMessageDialog(this, "Thêm thành công");
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
        xuongdong();
    }

    public void loadHoaDon() {
        try {
            List<HoaDon> list = hdDAO.selectAll();
            for (HoaDon hd : list) {
                id = Integer.parseInt(hd.getIdhoadon());
            }
            int id2 = id + 1;
            txt_idhoadon.setText(Integer.toString(id2));
        } catch (Exception e) {
        }
    }

    public void loaddataSach() {
        Date ngay = new Date();
        //txt_ngaybansach.setText(dateFormat.format(ngay));
        //txt_manv.setText(XAuth.user.getHoten());
        model_sach = (DefaultTableModel) tbl_sach.getModel();
        model_sach.setColumnIdentifiers(new Object[]{"Tên sách", "Tác giả", "Vị trí", "Số lượng", "Giá bán"});
        model_sach.setRowCount(0);
        try {
            List<Sach> list = sachDAO.selectAll();
            for (Sach sach : list) {
                TheLoai tl = tlDAO.select_byID(sach.getIdtheloai().toString());
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
                TheLoai tl = tlDAO.select_byID(sach.getIdtheloai().toString());
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

    public boolean batloi_tk() {
        String khachdua = txt_khachdua.getText();
        String thoilai = txt_thoilai.getText();

        String loi = "";

        if (khachdua.equalsIgnoreCase("")) {
            loi += "Khách đưa\n";
        }
        if (thoilai.equalsIgnoreCase("")) {
            loi += "Thối lại\n";
        }
        if (!loi.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "--Vui lòng kiểm tra lại thông tin!!--\n" + loi, "Lỗi", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
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
            if (sl != null) {
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
                            Double thanhtien2 = Double.parseDouble(tbl_hoadon.getValueAt(hang, 3).toString()) + thanhtien;
                            tbl_hoadon.setValueAt(D_format.format(thanhtien2), hang, 3);
                            tinhtien();
                        }
                    }
                }
            }
        }
    }
    // Renderer để tự động xuống hàng trong các ô của bảng

    static class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {

        MultiLineTableCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setFont(table.getFont());
            setText((value == null) ? "" : value.toString());
            return this;
        }
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
    }

    private TableCellRenderer getAlignmentCellRender(TableCellRenderer oldRender, boolean header) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component com = oldRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (com instanceof JLabel) {
                    JLabel label = (JLabel) com;
                    if (column == 0) {
                        label.setHorizontalAlignment(SwingConstants.LEADING);
                    } else if (column == 3 || column == 4) {
                        label.setHorizontalAlignment(SwingConstants.TRAILING);
                    } else if (column == 2) {
                        label.setHorizontalAlignment(SwingConstants.CENTER);
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

        btn = new javax.swing.JButton();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn.setText("change");
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionPerformed(evt);
            }
        });

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
        crazyPanel2.add(txt_timkiem);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Ngày:");
        crazyPanel2.add(jLabel3);

        txt_ngaybansach.setEnabled(false);
        crazyPanel2.add(txt_ngaybansach);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("ID Nhân viên");
        crazyPanel2.add(jLabel1);

        txt_manv.setToolTipText("");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1112, Short.MAX_VALUE)
                        .addGap(65, 65, 65))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionPerformed

        if (!FlatLaf.isLafDark()) {
            EventQueue.invokeLater(() -> {
                FlatAnimatedLafChange.showSnapshot();
                FlatDarculaLaf.setup();
                FlatLaf.updateUI();
                FlatAnimatedLafChange.hideSnapshotWithAnimation();
            });
        } else {
            EventQueue.invokeLater(() -> {
                FlatAnimatedLafChange.showSnapshot();
                FlatIntelliJLaf.setup();
                FlatLaf.updateUI();
                FlatAnimatedLafChange.hideSnapshotWithAnimation();
            });
        }
    }//GEN-LAST:event_btnActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        ThemHoaDon();
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        resetForm();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void txt_timkiemCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemCaretUpdate
        timkiem();
    }//GEN-LAST:event_txt_timkiemCaretUpdate

    private void tbl_sachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_sachMouseClicked
        taoHoaDon(evt);
    }//GEN-LAST:event_tbl_sachMouseClicked

    private void txt_khachduaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_khachduaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txt_khachdua.getText().equalsIgnoreCase("")) {
            khachdua = Double.parseDouble(txt_khachdua.getText());
            if (khachdua >= tongcong) {
                thoilai = khachdua - tongcong;
                txt_thoilai.setText(currencyVN.format(thoilai));
            } else {
                JOptionPane.showMessageDialog(this, "Kiểm tra lại khách đưa!");
            }
        }
    }//GEN-LAST:event_txt_khachduaKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BANSACH_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BANSACH_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BANSACH_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BANSACH_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("tableview");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        SystemProperties pro = new SystemProperties();
        pro.loadFromFile();
        if (!pro.isDarkMode()) {
            FlatIntelliJLaf.setup();
        } else {
            FlatMacDarkLaf.setup();
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BANSACH_FORM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn;
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
