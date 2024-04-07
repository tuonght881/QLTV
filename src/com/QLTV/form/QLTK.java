/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.TaiKhoanDAO;
import com.QLTV.entity.TaiKhoan;
import com.QLTV.utils.XAuth;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.raven.properties.SystemProperties;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class QLTK extends javax.swing.JPanel {

    Date ngay;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    TaiKhoanDAO tkDAO = new TaiKhoanDAO();
    int index = -1;
    SystemProperties pro = new SystemProperties();

    /**
     * Creates new form QLTK
     */
    public QLTK() {
        initComponents();
        loaddataTaiKhoan();
        loadMaNV();
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);
        POPUP.add(jPanel1);
        calendar1.addCalendarSelectedListener(new CalendarSelectedListener() {
            @Override
            public void selected(MouseEvent evt, ModelDate date) {
                ngay = date.toDate();
                String ngayF = sdf.format(ngay);
                txt_ngaysinh.setText(ngayF.toString());
                POPUP.setVisible(false);
                System.out.println("=>" + ngayF);
            }
        });
    }

    private void init() {
        try {
            //FlatRobotoFont.install();
            FlatLaf.registerCustomDefaultsSource("tableview");
            //UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
            System.out.println(pro.isDarkMode());
            if (pro.isDarkMode() == true) {
                EventQueue.invokeLater(() -> {
                    //FlatAnimatedLafChange.showSnapshot();
                    FlatDarculaLaf.setup();
                    FlatLaf.updateUI();
                    //FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            } else {
                EventQueue.invokeLater(() -> {
                    //FlatAnimatedLafChange.showSnapshot();
                    FlatIntelliJLaf.setup();
                    FlatLaf.updateUI();
                    //FlatAnimatedLafChange.hideSnapshotWithAnimation();
                });
            }
        } catch (Exception e) {
        }
    }

    public void setFormTK(TaiKhoan tk) throws ParseException {
        txt_manv.setText(tk.getManv());
        txt_matkhau.setText(tk.getMatkhau());
        if (tk.getVaitro() == true) {
            rdo_ql.setSelected(true);
        } else {
            rdo_nhanvien.setSelected(true);
        }
        if (tk.getTrangthai() == true) {
            rdo_hd.setSelected(true);
        } else {
            rdo_nhd.setSelected(true);
        }
        txt_hoten.setText(tk.getHoten());
        if (tk.getGioitinh() == true) {
            rdo_nam.setSelected(true);
        } else {
            rdo_nu.setSelected(true);
        }
        txt_sdt.setText(tk.getSdt());
        txt_ngaysinh.setText(tk.getNgaysinh());
        txt_diachi.setText(tk.getDiachi());
    }

    public TaiKhoan getFormTK() throws ParseException {
        TaiKhoan tkNew = new TaiKhoan();

        tkNew.setManv(txt_manv.getText());
        tkNew.setMatkhau(txt_matkhau.getText());
        if (rdo_ql.isSelected()) {
            tkNew.setVaitro(true);
        } else {
            tkNew.setVaitro(false);
        }
        if (rdo_hd.isSelected()) {
            tkNew.setTrangthai(true);
        } else {
            tkNew.setTrangthai(false);
        }
        tkNew.setHoten(txt_hoten.getText());
        if (rdo_nam.isSelected()) {
            tkNew.setGioitinh(true);
        } else {
            tkNew.setGioitinh(false);
        }
        tkNew.setSdt(txt_sdt.getText());
        tkNew.setNgaysinh(txt_ngaysinh.getText());
        tkNew.setDiachi(txt_diachi.getText());
        return tkNew;
    }

    public void fillFormTK() throws ParseException {
        txt_manv.setEnabled(false);
        txt_manv.setEditable(false);
        btn_them.setEnabled(false);
        btn_sua.setEnabled(true);
        btn_xoa.setEnabled(true);

        String ma = (String) tbl_tk.getValueAt(index, 0);
        TaiKhoan tk = tkDAO.select_byID(ma);
        if (tk == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setFormTK(tk);
        }
    }

    public void themTK() {
        if (batloi_tk()) {
            try {
                TaiKhoan tkNew = getFormTK();
                tkDAO.insert(tkNew);
                loaddataTaiKhoan();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!\n" + e.getMessage());
            }
        }
    }

    public void suaTK() {
        if (batloi_tk()) {
            try {
                TaiKhoan tkNew = getFormTK();
                tkDAO.update(tkNew);
                loaddataTaiKhoan();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!\n" + e.getMessage());
            }
        }
    }

    public void xoaTK() {
        String us = XAuth.user.getManv();
        String entity = txt_manv.getText();
        if (us.equalsIgnoreCase(entity)) {
            JOptionPane.showMessageDialog(this, "Bạn không thể xoá chính bạn!", "Thông báo", JOptionPane.OK_OPTION);
        } else {
            try {
                tkDAO.delete(entity);
                loaddataTaiKhoan();
                JOptionPane.showMessageDialog(this, "Xoá thành công");
                resetForm();
            } catch (Exception e) {
                // Bắt các loại ngoại lệ, bao gồm SQLServerException
                if (e.getMessage().contains("conflicted with the REFERENCE constraint")) {
                    // Xử lý lỗi về ràng buộc tham chiếu ở đây
                    int choice = JOptionPane.showConfirmDialog(this, "Xóa thất bại do tài khoản này đã tạo hoá đơn.\nHành động xoá này chỉ thay đổi trạng thái của tài khoản.", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
                    if (choice == JOptionPane.OK_OPTION) {
                        TaiKhoan tk = tkDAO.select_byID(entity);
                        tk.setTrangthai(false);
                        tkDAO.update(tk);
                        loaddataTaiKhoan();
                        resetForm();
                    }
                } else {
                    // Xử lý các loại lỗi khác
                    JOptionPane.showMessageDialog(this, "Xóa thất bại.\n" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void resetForm() {
        loaddataTaiKhoan();
        loadMaNV();
        txt_manv.setEnabled(false);
        txt_manv.setEditable(false);
        btn_them.setEnabled(true);
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);
        txt_matkhau.setText("");
        txt_sdt.setText("");
        txt_hoten.setText("");
        txt_diachi.setText("");
        txt_ngaysinh.setText("");
        btnG_gioitinh.clearSelection();
        btnG_trangthai.clearSelection();
        btnG_vaitro.clearSelection();
    }

    public void loaddataTaiKhoan() {
        DefaultTableModel modelTK = (DefaultTableModel) tbl_tk.getModel();
        modelTK.setColumnIdentifiers(new Object[]{"Mã NV", "Họ tên", "Vai trò", "Trạng thái", "Giới tính", "SĐT", "Ngày sinh", "Địa chỉ"});
        modelTK.setRowCount(0);
        try {
            List<TaiKhoan> list = tkDAO.selectAll();
            for (TaiKhoan tk : list) {
                Object[] row = {tk.getManv(), tk.getHoten(), tk.getVaitro() ? "Quản lý" : "Nhân Viên", tk.getTrangthai() ? "Hoạt động" : "Ngưng hoạt động", tk.getGioitinh() ? "Nam" : "Nữ", tk.getSdt(), tk.getNgaysinh(), tk.getDiachi()};
                modelTK.addRow(row);
            }
            if (modelTK.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                modelTK.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMaNV() {
        try {
            String mnv = "";
            List<TaiKhoan> list = tkDAO.select_al();
            for (TaiKhoan tk : list) {
                mnv = tk.getManv();
            }
            if (mnv.equalsIgnoreCase("")) {
                mnv = "NV001";
                txt_manv.setText(mnv);
            } else {
                int number = Integer.parseInt(mnv.substring(2));
                number++;
                String newText = "NV" + String.format("%03d", number);
                txt_manv.setText(newText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timkiem() {
        String tukhoa = txt_timkiem.getText();
        DefaultTableModel modelTK = (DefaultTableModel) tbl_tk.getModel();
        modelTK.setColumnIdentifiers(new Object[]{"Mã NV", "Họ tên", "Vai trò", "Trạng thái", "Giới tính", "SĐT", "Ngày sinh", "Địa chỉ"});
        modelTK.setRowCount(0);
        try {
            List<TaiKhoan> list = tkDAO.timkiemTK(tukhoa);
            for (TaiKhoan tk : list) {
                Object[] row = {tk.getManv(), tk.getHoten(), tk.getVaitro() ? "Quản lý" : "Nhân Viên", tk.getTrangthai() ? "Hoạt động" : "Ngưng hoạt động", tk.getGioitinh() ? "Nam" : "Nữ", tk.getSdt(), tk.getNgaysinh(), tk.getDiachi()};
                modelTK.addRow(row);
            }
            if (modelTK.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                modelTK.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean batloi_tk() {
        String manv = txt_manv.getText().trim();
        String matkhau = txt_matkhau.getText();
        String hoten = txt_hoten.getText();
        String sdt = txt_sdt.getText().trim();
        String ngaysinh = txt_ngaysinh.getText();
        String diachi = txt_diachi.getText();
        boolean OnlyLetters = hoten.matches(".*\\d.*");
        String loi = "";

        if (manv.equalsIgnoreCase("")) {
            loi += "Mã nhân viên\n";
        } else {
            if (manv.length() != 5) {
                loi += "Mã NV phải có 5 ký tự\n";
            }
        }
        Pattern patternMNV = Pattern.compile("^(QL|NV)\\d{3}$");
        Matcher regexMatcherMNV = patternMNV.matcher(manv);
        if (!regexMatcherMNV.matches()) {
            loi += "Định dạng mã NV (VD: NV001)\n";
        }
        if (hoten.equalsIgnoreCase("")) {
            loi += "Họ tên\n";
        }
        if (OnlyLetters) {
            loi += "Họ tên không được nhập số\n";
        }
        if (matkhau.equalsIgnoreCase("")) {
            loi += "Mật khẩu\n";
        }
        if (sdt.equalsIgnoreCase("")) {
            loi += "SDT\n";
        } else {
            Pattern pattern = Pattern.compile("^0[1-9][0-9]{8}+$");
            Matcher regexMatcher = pattern.matcher(sdt);
            if (!regexMatcher.matches()) {
                loi += "Không đúng định dang SDT\n (SDT không nhập chữ, SDT gồm có 10 số)\n";
            }
        }
        int loisdt = 0;
        List<TaiKhoan> list = tkDAO.selectAll();
        for (TaiKhoan tk : list) {
            String sdtString = tk.getSdt();
            if (sdt.equalsIgnoreCase(sdtString) && !tk.getManv().equalsIgnoreCase(txt_manv.getText())) {
                loisdt = 1;
                break;
            }
        }
        if (loisdt == 1) {
            loi += "SDT đã được sử dụng\n";
        }
        if (ngaysinh.equalsIgnoreCase("")) {
            loi += "Ngày sinh\n";
        }
        if (diachi.equalsIgnoreCase("")) {
            loi += "Địa chỉ\n";
        }
        if (rdo_nam.isSelected() == false && rdo_nu.isSelected() == false) {
            loi += "Giới tính\n";
        }
        if (rdo_ql.isSelected() == false && rdo_nhanvien.isSelected() == false) {
            loi += "Vai trò\n";
        }
        if (rdo_hd.isSelected() == false && rdo_nhd.isSelected() == false) {
            loi += "Trạng thái";
        }
        if (!loi.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "--Vui lòng kiểm tra lại thông tin!!--\n" + loi, "Lỗi", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    private void showPopup() {
        //int x = txt_ngaysinh.getLocationOnScreen().x;
        //int y = txt_ngaysinh.getLocationOnScreen().y + txt_ngaysinh.getHeight();
        POPUP.show(txt_ngaysinh, 0, txt_ngaysinh.getHeight());
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

        btnG_vaitro = new javax.swing.ButtonGroup();
        btnG_trangthai = new javax.swing.ButtonGroup();
        btnG_gioitinh = new javax.swing.ButtonGroup();
        POPUP = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        calendar1 = new raven.calendar.Calendar();
        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        txt_timkiem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_tk = new javax.swing.JTable();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_manv = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_hoten = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_matkhau = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        txt_sdt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_diachi = new javax.swing.JTextField();
        crazyPanel8 = new raven.crazypanel.CrazyPanel();
        jLabel8 = new javax.swing.JLabel();
        txt_ngaysinh = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        crazyPanel9 = new raven.crazypanel.CrazyPanel();
        rdo_nam = new javax.swing.JRadioButton();
        rdo_nu = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        crazyPanel6 = new raven.crazypanel.CrazyPanel();
        rdo_ql = new javax.swing.JRadioButton();
        rdo_nhanvien = new javax.swing.JRadioButton();
        crazyPanel4 = new raven.crazypanel.CrazyPanel();
        jLabel6 = new javax.swing.JLabel();
        crazyPanel7 = new raven.crazypanel.CrazyPanel();
        rdo_hd = new javax.swing.JRadioButton();
        rdo_nhd = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        crazyPanel5 = new raven.crazypanel.CrazyPanel();
        btn_xoa = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();

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
            "[]push[][]",
            "",
            new String[]{
                "width 200"
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

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("QUẢN LÝ TÀI KHOẢN");
        crazyPanel2.add(jLabel4);

        crazyPanel1.add(crazyPanel2);

        tbl_tk.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_tk.setEditingColumn(0);
        tbl_tk.setEditingRow(0);
        tbl_tk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_tkMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_tk);

        crazyPanel1.add(jScrollPane1);

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
                "background:@background"
            }
        ));
        crazyPanel3.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 6",
            "[][][][][][]",
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
                "width 200"
            }
        ));
        crazyPanel3.setName(""); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("(*)MaNV:");
        crazyPanel3.add(jLabel1);

        txt_manv.setEditable(false);
        txt_manv.setToolTipText("");
        txt_manv.setEnabled(false);
        crazyPanel3.add(txt_manv);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("(*)Họ tên:");
        crazyPanel3.add(jLabel3);
        crazyPanel3.add(txt_hoten);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("(*)Mật khẩu:");
        crazyPanel3.add(jLabel2);

        txt_matkhau.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txt_matkhauMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txt_matkhauMouseExited(evt);
            }
        });
        crazyPanel3.add(txt_matkhau);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("(*)SDT:");
        crazyPanel3.add(jLabel5);
        crazyPanel3.add(txt_sdt);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("(*)Địa chỉ:");
        crazyPanel3.add(jLabel9);
        crazyPanel3.add(txt_diachi);

        crazyPanel1.add(crazyPanel3);

        crazyPanel8.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "",
                "background:@background"
            }
        ));
        crazyPanel8.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[][][][]",
            "[][]",
            new String[]{
                "width 80",
                "width 200",
                "width 80",
                "width 80",
                "width 170",
                "width 80",
                ""
            }
        ));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("(*)Ngày sinh:");
        crazyPanel8.add(jLabel8);

        txt_ngaysinh.setEditable(false);
        txt_ngaysinh.setEnabled(false);
        txt_ngaysinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_ngaysinhMouseClicked(evt);
            }
        });
        crazyPanel8.add(txt_ngaysinh);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("(*)Giới tính:");
        crazyPanel8.add(jLabel10);

        crazyPanel9.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            null
        ));

        btnG_gioitinh.add(rdo_nam);
        rdo_nam.setText("Nam");
        crazyPanel9.add(rdo_nam);

        btnG_gioitinh.add(rdo_nu);
        rdo_nu.setText("Nữ");
        crazyPanel9.add(rdo_nu);

        crazyPanel8.add(crazyPanel9);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("(*)Vai trò:");
        crazyPanel8.add(jLabel7);

        crazyPanel6.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            null
        ));

        btnG_vaitro.add(rdo_ql);
        rdo_ql.setText("Quản lý");
        rdo_ql.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_qlActionPerformed(evt);
            }
        });
        crazyPanel6.add(rdo_ql);

        btnG_vaitro.add(rdo_nhanvien);
        rdo_nhanvien.setText("Nhân viên");
        rdo_nhanvien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_nhanvienActionPerformed(evt);
            }
        });
        crazyPanel6.add(rdo_nhanvien);

        crazyPanel8.add(crazyPanel6);

        crazyPanel1.add(crazyPanel8);

        crazyPanel4.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            null
        ));
        crazyPanel4.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "",
            "",
            new String[]{
                "width 80",
                "width 200"
            }
        ));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("(*)Trạng thái:");
        crazyPanel4.add(jLabel6);

        crazyPanel7.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            null
        ));

        btnG_trangthai.add(rdo_hd);
        rdo_hd.setText("Hoạt động");
        crazyPanel7.add(rdo_hd);

        btnG_trangthai.add(rdo_nhd);
        rdo_nhd.setText("Ngưng hoạt động");
        crazyPanel7.add(rdo_nhd);

        crazyPanel4.add(crazyPanel7);

        crazyPanel1.add(crazyPanel4);
        crazyPanel1.add(jSeparator1);

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

        btn_sua.setText("Cập nhật");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });
        crazyPanel5.add(btn_sua);

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });
        crazyPanel5.add(btn_them);

        crazyPanel1.add(crazyPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void calendar1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calendar1MousePressed
        if (evt.getClickCount() == 2) {
            POPUP.setVisible(false);
        }
    }//GEN-LAST:event_calendar1MousePressed

    private void txt_timkiemCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemCaretUpdate
        timkiem();
    }//GEN-LAST:event_txt_timkiemCaretUpdate

    private void tbl_tkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_tkMouseClicked
        if (evt.getClickCount() == 2) {
            index = tbl_tk.getSelectedRow();
            try {
                fillFormTK();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi\n" + e.getMessage());
            }
        }
    }//GEN-LAST:event_tbl_tkMouseClicked

    private void txt_ngaysinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_ngaysinhMouseClicked
        showPopup();
    }//GEN-LAST:event_txt_ngaysinhMouseClicked

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        xoaTK();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        resetForm();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        suaTK();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        themTK();
    }//GEN-LAST:event_btn_themActionPerformed

    private void rdo_qlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_qlActionPerformed
        String manv1 = txt_manv.getText();
        if (manv1.length() >= 2 && manv1.length() <= 5) {
            String ql = "QL" + manv1.substring(2);
            txt_manv.setText(ql);
        }
    }//GEN-LAST:event_rdo_qlActionPerformed

    private void rdo_nhanvienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_nhanvienActionPerformed
        String manv1 = txt_manv.getText();
        if (manv1.length() >= 2 && manv1.length() <= 5) {
            String ql = "NV" + manv1.substring(2);
            txt_manv.setText(ql);
        }
    }//GEN-LAST:event_rdo_nhanvienActionPerformed

    private void txt_matkhauMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_matkhauMouseEntered
        txt_matkhau.setEchoChar((char) 0);
    }//GEN-LAST:event_txt_matkhauMouseEntered

    private void txt_matkhauMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_matkhauMouseExited
        txt_matkhau.setEchoChar('\u2022');
    }//GEN-LAST:event_txt_matkhauMouseExited

    private void txt_timkiemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_timkiemMouseExited
        loaddataTaiKhoan();
    }//GEN-LAST:event_txt_timkiemMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu POPUP;
    private javax.swing.ButtonGroup btnG_gioitinh;
    private javax.swing.ButtonGroup btnG_trangthai;
    private javax.swing.ButtonGroup btnG_vaitro;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xoa;
    private raven.calendar.Calendar calendar1;
    private raven.crazypanel.CrazyPanel crazyPanel1;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JRadioButton rdo_hd;
    private javax.swing.JRadioButton rdo_nam;
    private javax.swing.JRadioButton rdo_nhanvien;
    private javax.swing.JRadioButton rdo_nhd;
    private javax.swing.JRadioButton rdo_nu;
    private javax.swing.JRadioButton rdo_ql;
    private javax.swing.JTable tbl_tk;
    private javax.swing.JTextField txt_diachi;
    private javax.swing.JTextField txt_hoten;
    private javax.swing.JTextField txt_manv;
    private javax.swing.JPasswordField txt_matkhau;
    private javax.swing.JTextField txt_ngaysinh;
    private javax.swing.JTextField txt_sdt;
    private javax.swing.JTextField txt_timkiem;
    // End of variables declaration//GEN-END:variables
}
