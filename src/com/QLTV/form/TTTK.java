/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.TaiKhoanDAO;
import com.QLTV.entity.TaiKhoan;
import com.QLTV.utils.XAuth;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven.calendar.model.ModelDate;
import raven.tabbed.TabbedForm;
import raven.toast.Notifications;

/**
 *
 * @author Tuong
 */
public final class TTTK extends TabbedForm {

    Date ngay;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    TaiKhoanDAO tkDAO = new TaiKhoanDAO();

    /**
     * Creates new form QLTK
     *
     * @throws java.text.ParseException
     */
    public TTTK() throws ParseException {
        initComponents();
        loaddataTaiKhoan();
        loadMaNV();
        btn_sua.setEnabled(false);
        //btn_xoa.setEnabled(false);
        POPUP.add(jPanel1);
        calendar1.addCalendarSelectedListener((MouseEvent evt, ModelDate date) -> {
            ngay = date.toDate();
            String ngayF = sdf.format(ngay);
            txt_ngaysinh.setText(ngayF.toString());
            POPUP.setVisible(false);
            System.out.println("=>" + ngayF);
        });
        fillFormTK();
    }

    public void setFormTK(TaiKhoan tk) throws ParseException {
        txt_manv.setText(tk.getManv());
        txt_matkhau.setText(tk.getMatkhau());

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
        //btn_them.setEnabled(false);
        btn_sua.setEnabled(true);
        //btn_xoa.setEnabled(true);

        String ma = (String) tbl_tk.getValueAt(0, 0);
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
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Cập nhật thành công");
                //JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!\n" + e.getMessage());
            }
        }
    }

    public void xoaTK() {
        try {
            String entity = txt_manv.getText();
            tkDAO.delete(entity);
            loaddataTaiKhoan();
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xoá thất bại\n" + e.getMessage());
        }
    }

    public void resetForm() {
        loaddataTaiKhoan();
        loadMaNV();
        txt_manv.setEnabled(false);
        txt_manv.setEditable(false);
        //btn_them.setEnabled(true);
        btn_sua.setEnabled(false);
        //btn_xoa.setEnabled(false);
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
        String manv = XAuth.user.getManv();
        DefaultTableModel modelTK = (DefaultTableModel) tbl_tk.getModel();
        modelTK.setColumnIdentifiers(new Object[]{"Mã NV", "Họ tên", "Vai trò", "Trạng thái", "Giới tính", "SĐT", "Ngày sinh", "Địa chỉ"});
        modelTK.setRowCount(0);
        try {
            TaiKhoan tk = tkDAO.select_byID(manv);
            Object[] row = {tk.getManv(), tk.getHoten(), tk.getVaitro() ? "Quản lý" : "Nhân Viên", tk.getTrangthai() ? "Hoạt động" : "Ngưng hoạt động", tk.getGioitinh() ? "Nam" : "Nữ", tk.getSdt(), tk.getNgaysinh(), tk.getDiachi()};
            modelTK.addRow(row);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMaNV() {
        try {
            String mnv = "";
            List<TaiKhoan> list = tkDAO.selectAll();
            for (TaiKhoan tk : list) {
                mnv = tk.getManv();
            }
            int number = Integer.parseInt(mnv.substring(2));
            number++;
            String newText = "NV" + String.format("%03d", number);
            txt_manv.setText(newText);
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
        boolean OnlyLetters = hoten.matches("^[a-zA-Z]*$");
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
        if (OnlyLetters == false) {
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
        if (!loi.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "--Vui lòng kiểm tra lại thông tin!!--\n" + loi, "Lỗi", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    private void showPopup() {
        POPUP.show(txt_ngaysinh, 0, txt_ngaysinh.getHeight());
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
        jPanel2 = new javax.swing.JPanel();
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
        jSeparator1 = new javax.swing.JSeparator();
        crazyPanel5 = new raven.crazypanel.CrazyPanel();
        btn_reset = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();

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
                "background:lighten(@background,8%)",
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        crazyPanel2.add(jPanel2);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("THÔNG TIN TÀI KHOẢN");
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
        jLabel1.setText("MaNV:");
        crazyPanel3.add(jLabel1);

        txt_manv.setEditable(false);
        txt_manv.setToolTipText("");
        txt_manv.setEnabled(false);
        crazyPanel3.add(txt_manv);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Họ tên:");
        crazyPanel3.add(jLabel3);
        crazyPanel3.add(txt_hoten);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Mật khẩu:");
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
        jLabel5.setText("SDT:");
        crazyPanel3.add(jLabel5);
        crazyPanel3.add(txt_sdt);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Địa chỉ:");
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
        jLabel8.setText("Ngày sinh:");
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
        jLabel10.setText("Giới tính:");
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

        crazyPanel1.add(crazyPanel8);
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

    private void tbl_tkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_tkMouseClicked
        if (evt.getClickCount() == 2) {
            tbl_tk.getSelectedRow();
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

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        resetForm();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        suaTK();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void txt_matkhauMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_matkhauMouseEntered
        txt_matkhau.setEchoChar((char) 0);
    }//GEN-LAST:event_txt_matkhauMouseEntered

    private void txt_matkhauMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_matkhauMouseExited
        txt_matkhau.setEchoChar('\u2022');
    }//GEN-LAST:event_txt_matkhauMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu POPUP;
    private javax.swing.ButtonGroup btnG_gioitinh;
    private javax.swing.ButtonGroup btnG_trangthai;
    private javax.swing.ButtonGroup btnG_vaitro;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_sua;
    private raven.calendar.Calendar calendar1;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private raven.crazypanel.CrazyPanel crazyPanel5;
    private raven.crazypanel.CrazyPanel crazyPanel8;
    private raven.crazypanel.CrazyPanel crazyPanel9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JRadioButton rdo_nam;
    private javax.swing.JRadioButton rdo_nu;
    private javax.swing.JTable tbl_tk;
    private javax.swing.JTextField txt_diachi;
    private javax.swing.JTextField txt_hoten;
    private javax.swing.JTextField txt_manv;
    private javax.swing.JPasswordField txt_matkhau;
    private javax.swing.JTextField txt_ngaysinh;
    private javax.swing.JTextField txt_sdt;
    // End of variables declaration//GEN-END:variables
}
