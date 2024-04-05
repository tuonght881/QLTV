/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.KhachHangDAO;
import com.QLTV.entity.KhachHang;
import com.QLTV.utils.XAuth;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Font;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Tuong
 */
public class QLDocGia_FORM extends javax.swing.JFrame {

    KhachHangDAO khDAO = new KhachHangDAO();
    int index = -1;
    int id;

    /**
     * Creates new form tesst
     */
    public QLDocGia_FORM() {
        initComponents();
        loaddataKhachHang();
        loadIDDG();
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);

        if (XAuth.isManager() == true) {
            btn_sua.setVisible(true);
            btn_sua.setVisible(true);
            btn_xoa.setVisible(true);
            btn_xoa.setVisible(true);
        } else {
            btn_sua.setVisible(false);
            btn_sua.setVisible(false);
            btn_xoa.setVisible(false);
            btn_xoa.setVisible(false);
        }
    }

    public void setFormTG(KhachHang kh) {
        txt_idkhachhang.setText(kh.getIdkhach());
        txt_tenkhachhang.setText(kh.getHotenkhach());
        txt_sdt.setText(kh.getSdt());
        txt_diemuytin.setText(Integer.toString(kh.getDiemuytin()));
    }

    public KhachHang getFormTG() {
        KhachHang khNew = new KhachHang();

        khNew.setIdkhach(txt_idkhachhang.getText());
        khNew.setHotenkhach(txt_tenkhachhang.getText());
        khNew.setSdt(txt_sdt.getText());
        khNew.setDiemuytin(Integer.parseInt(txt_diemuytin.getText()));
        return khNew;
    }

    public void fillFormTG() {
        if (XAuth.isManager() == true) {
            txt_diemuytin.setEditable(true);
            txt_diemuytin.setEnabled(true);
        } else {
            txt_diemuytin.setEditable(false);
            txt_diemuytin.setEnabled(false);
        }
        txt_idkhachhang.setEnabled(false);
        txt_idkhachhang.setEditable(false);
        btn_them.setEnabled(false);
        btn_sua.setEnabled(true);
        btn_xoa.setEnabled(true);
        String idkh = (String) tbl_khachhang.getValueAt(index, 0);
        KhachHang kh = khDAO.select_byID(idkh);
        if (kh == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setFormTG(kh);
        }
    }

    public void themTG() {
        if (batloi_kh()) {
            try {
                KhachHang khNew = getFormTG();
                khDAO.insert(khNew);
                loaddataKhachHang();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!\n" + e.getMessage());
            }
        }
    }

    public void suaTG() {
        if (batloi_kh()) {
            try {
                KhachHang khNew = getFormTG();
                khDAO.update(khNew);
                loaddataKhachHang();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!\n" + e.getMessage());
            }
        }
    }

    public void xoaTG() {
        try {
            String entity = txt_idkhachhang.getText();
            khDAO.delete(entity);
            loaddataKhachHang();
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
        } catch (Exception e) {
            // Bắt các loại ngoại lệ, bao gồm SQLServerException
            if (e.getMessage().contains("conflicted with the REFERENCE constraint")) {
                // Xử lý lỗi về ràng buộc tham chiếu ở đây
                JOptionPane.showMessageDialog(this, "Không thể xoá khách hàng, Vì KH này có dữ liệu trong hoá đơn", "Thông báo", JOptionPane.OK_OPTION);
            } else {
                // Xử lý các loại lỗi khác
                JOptionPane.showMessageDialog(this, "Xóa thất bại.\n" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void resetForm() {
        loadIDDG();
        txt_diemuytin.setEditable(false);
        txt_diemuytin.setEnabled(false);
        loaddataKhachHang();
        txt_idkhachhang.setEnabled(true);
        txt_idkhachhang.setEditable(true);
        txt_idkhachhang.setText("");
        txt_tenkhachhang.setText("");
        txt_diemuytin.setText("90");
        txt_sdt.setText("");
        btn_them.setEnabled(true);
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);
    }

    public void loaddataKhachHang() {
        DefaultTableModel modelKhachHang = (DefaultTableModel) tbl_khachhang.getModel();
        modelKhachHang.setColumnIdentifiers(new Object[]{"ID", "Họ tên khách hàng", "SDT", "Điểm uy tín"});
        modelKhachHang.setRowCount(0);
        try {
            List<KhachHang> list = khDAO.selectAll();
            for (KhachHang kh : list) {
                Object[] row = {kh.getIdkhach(), kh.getHotenkhach(), kh.getSdt(), kh.getDiemuytin()};
                modelKhachHang.addRow(row);
            }
            if (modelKhachHang.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                modelKhachHang.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timkiem() {
        String tukhoa = txt_timkiem.getText();
        DefaultTableModel modelKhachHang = (DefaultTableModel) tbl_khachhang.getModel();
        modelKhachHang.setColumnIdentifiers(new Object[]{"ID", "Họ tên khách hàng", "SDT", "Điểm uy tín"});
        modelKhachHang.setRowCount(0);
        try {
            List<KhachHang> list = khDAO.timkiemTG(tukhoa);
            for (KhachHang kh : list) {
                Object[] row = {kh.getIdkhach(), kh.getHotenkhach(), kh.getSdt(), kh.getDiemuytin()};
                modelKhachHang.addRow(row);
            }
            if (modelKhachHang.getRowCount() == 0) {
                Object[] row = {"", "Không tìm thấy"};
                modelKhachHang.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadIDDG() {
        try {
            String idkh = "";
            List<KhachHang> list = khDAO.select_all();
            for (KhachHang kh : list) {
                idkh = kh.getIdkhach();
            }
            if (idkh.equalsIgnoreCase("")) {
                idkh = "KH001";
                txt_idkhachhang.setText(idkh);
            } else {
                int number = Integer.parseInt(idkh.substring(2));
                number++;
                String newText = "KH" + String.format("%03d", number);
                txt_idkhachhang.setText(newText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean batloi_kh() {
        String idKhachhang = txt_idkhachhang.getText().trim();
        String hotenkhachhang = txt_tenkhachhang.getText().trim();
        String sdt = txt_sdt.getText().trim();
        String diemuytin = txt_diemuytin.getText().trim();

        String loi = "";

        if (idKhachhang.equalsIgnoreCase("")) {
            loi += "ID khách hàng\n";
        } else {
            if (idKhachhang.length() != 5) {
                loi += "ID Khách hàng phải có 5 ký tự\n";
            }
        }
        Pattern patternMNV = Pattern.compile("^(KH)\\d{3}$");
        Matcher regexMatcherMNV = patternMNV.matcher(idKhachhang);
        if (!regexMatcherMNV.matches()) {
            loi += "Định dạng ID khách hàng(VD: KH001)\n";
        }
        if (hotenkhachhang.equalsIgnoreCase("")) {
            loi += "Họ tên khách hàng\n";
        }
        boolean OnlyLetters = hotenkhachhang.matches(".*\\d.*");
        //System.out.println(OnlyLetters);
        if (OnlyLetters) {
            loi += "Họ tên không được nhập số\n";
        }
        if (sdt.equalsIgnoreCase("")) {
            loi += "SDT\n";
        } else {
            Pattern pattern = Pattern.compile("^0[1-9]{9}+$");
            Matcher regexMatcher = pattern.matcher(sdt);
            if (!regexMatcher.matches()) {
                loi += "Không đúng định dang SDT\n (SDT không nhập chữ, SDT gồm có 10 số)\n";
            }
        }
        if (diemuytin.equalsIgnoreCase("")) {
            loi += "Điểm uy tín\n";
        } else {
            Pattern pattern = Pattern.compile("^[0-9]+$");
            Matcher regexMatcher = pattern.matcher(diemuytin);
            if (!regexMatcher.matches()) {
                loi += "Điểm uy tin phải nhập số";
            }
        }
        if (!loi.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "--Vui lòng kiểm tra lại thông tin!!--\n" + loi, "Lỗi", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
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
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_khachhang = new javax.swing.JTable();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_idkhachhang = new javax.swing.JTextField();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_tenkhachhang = new javax.swing.JTextField();
        btn_xoa = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txt_sdt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_diemuytin = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        crazyPanel2.add(txt_timkiem);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("QUẢN LÝ ĐỘC GIẢ");
        crazyPanel2.add(jLabel4);

        crazyPanel1.add(crazyPanel2);

        tbl_khachhang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_khachhang.setEditingColumn(0);
        tbl_khachhang.setEditingRow(0);
        tbl_khachhang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_khachhangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_khachhang);

        crazyPanel1.add(jScrollPane1);

        crazyPanel3.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background"
            }
        ));
        crazyPanel3.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 4",
            "[][]push[][]",
            "[][]",
            new String[]{
                "width 100",
                "width 200",
                "width 82",
                "width 82",
                "width 100",
                "width 200",
                "width 82",
                "width 82",
                "",
                "width 200",
                "width 82",
                "width 82"
            }
        ));

        jLabel1.setText("ID Khách hàng");
        crazyPanel3.add(jLabel1);

        txt_idkhachhang.setEditable(false);
        txt_idkhachhang.setToolTipText("");
        txt_idkhachhang.setEnabled(false);
        crazyPanel3.add(txt_idkhachhang);

        btn_sua.setText("Cập nhật");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });
        crazyPanel3.add(btn_sua);

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });
        crazyPanel3.add(btn_them);

        jLabel2.setText("Họ tên khách hàng");
        crazyPanel3.add(jLabel2);

        txt_tenkhachhang.setToolTipText("");
        crazyPanel3.add(txt_tenkhachhang);

        btn_xoa.setText("Xoá");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });
        crazyPanel3.add(btn_xoa);

        btn_reset.setText("Làm mới");
        btn_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resetActionPerformed(evt);
            }
        });
        crazyPanel3.add(btn_reset);

        jLabel3.setText("SDT");
        crazyPanel3.add(jLabel3);
        crazyPanel3.add(txt_sdt);

        jLabel5.setText("Điểm uy tín");
        crazyPanel3.add(jLabel5);

        txt_diemuytin.setEditable(false);
        txt_diemuytin.setText("90");
        txt_diemuytin.setEnabled(false);
        crazyPanel3.add(txt_diemuytin);

        crazyPanel1.add(crazyPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_timkiemCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemCaretUpdate
        timkiem();
    }//GEN-LAST:event_txt_timkiemCaretUpdate

    private void tbl_khachhangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_khachhangMouseClicked
        if (evt.getClickCount() == 2) {
            index = tbl_khachhang.getSelectedRow();
            fillFormTG();
        }
    }//GEN-LAST:event_tbl_khachhangMouseClicked

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        themTG();
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        suaTG();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        xoaTG();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        resetForm();
    }//GEN-LAST:event_btn_resetActionPerformed

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
            java.util.logging.Logger.getLogger(QLDocGia_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLDocGia_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLDocGia_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLDocGia_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("tableview");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QLDocGia_FORM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xoa;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_khachhang;
    private javax.swing.JTextField txt_diemuytin;
    private javax.swing.JTextField txt_idkhachhang;
    private javax.swing.JTextField txt_sdt;
    private javax.swing.JTextField txt_tenkhachhang;
    private javax.swing.JTextField txt_timkiem;
    // End of variables declaration//GEN-END:variables
}
