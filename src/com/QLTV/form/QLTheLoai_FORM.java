/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.TheLoaiDAO;
import com.QLTV.entity.TheLoai;
import com.QLTV.utils.XAuth;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.raven.properties.SystemProperties;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class QLTheLoai_FORM extends javax.swing.JFrame {

    TheLoaiDAO tlDAO = new TheLoaiDAO();
    int index = -1;
    int id;

    /**
     * Creates new form tesst
     */
    public QLTheLoai_FORM() {
        initComponents();
        loaddataTheLoai();
        loadIDTL();
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

    public void loadIDTL() {
        try {
            String idkh = "";
            List<TheLoai> list = tlDAO.select_ALL();
            for (TheLoai kh : list) {
                idkh = kh.getIdtheloai();
            }
            int number = Integer.parseInt(idkh.substring(2));
            number++;
            String newText = "TL" + String.format("%03d", number);
            txt_idtheloai.setText(newText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFormTL(TheLoai tl) {
        txt_idtheloai.setText(tl.getIdtheloai());
        txt_tentheloai.setText(tl.getTentheloai());
        if (tl.getTrangthaitl() == true) {
            rdo_hd.setSelected(true);
        } else {
            rdo_nhd.setSelected(true);
        }
    }

    public TheLoai getFormTL() {
        TheLoai tlNew = new TheLoai();

        tlNew.setIdtheloai(txt_idtheloai.getText());
        tlNew.setTentheloai(txt_tentheloai.getText());
        if (rdo_hd.isSelected()) {
            tlNew.setTrangthaitl(true);
        } else if (rdo_nhd.isSelected()) {
            tlNew.setTrangthaitl(false);
        }
        return tlNew;
    }

    public void fillFormTL() {
        txt_idtheloai.setEnabled(false);
        txt_idtheloai.setEditable(false);
        btn_them.setEnabled(false);
        btn_sua.setEnabled(true);
        btn_xoa.setEnabled(true);
        String idtl = (String) tbl_theloai.getValueAt(index, 0);
        TheLoai tl = tlDAO.select_byID(idtl);
        if (tl == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setFormTL(tl);
        }
    }

    public void themTG() {
        if (batloi_tl()) {
            try {
                TheLoai tlNew = getFormTL();
                tlDAO.insert(tlNew);
                loaddataTheLoai();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!\n" + e.getMessage());
            }
        }
    }

    public void suaTG() {
        if (batloi_tl()) {
            try {
                TheLoai tlNew = getFormTL();
                tlDAO.update(tlNew);
                loaddataTheLoai();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!\n" + e.getMessage());
            }
        }
    }

    public void xoaTG() {
        String entity = txt_idtheloai.getText();
        try {
            tlDAO.delete(entity);
            loaddataTheLoai();
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
        } catch (Exception e) {
            // Bắt các loại ngoại lệ, bao gồm SQLServerException
            if (e.getMessage().contains("conflicted with the REFERENCE constraint")) {
                // Xử lý lỗi về ràng buộc tham chiếu ở đây
                int choice = JOptionPane.showConfirmDialog(this, "Xóa thất bại do thể loại này đang có trong sách.\nHành động xoá này chỉ thay đổi trạng thái của thể loại.", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    TheLoai tl = tlDAO.select_byID(entity);
                    tl.setTrangthaitl(false);
                    tlDAO.update(tl);
                    loaddataTheLoai();
                    resetForm();
                }
            } else {
                // Xử lý các loại lỗi khác
                JOptionPane.showMessageDialog(this, "Xóa thất bại.\n" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void resetForm() {
        loaddataTheLoai();
        loadIDTL();
        txt_idtheloai.setEnabled(true);
        txt_idtheloai.setEditable(true);
        txt_idtheloai.setText("");
        txt_tentheloai.setText("");
        hd_tl.clearSelection();
        btn_them.setEnabled(true);
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);
    }

    public void loaddataTheLoai() {
        DefaultTableModel modelTheLoai = (DefaultTableModel) tbl_theloai.getModel();
        modelTheLoai.setColumnIdentifiers(new Object[]{"ID", "Tên thể loại", "Trạng Thái"});
        modelTheLoai.setRowCount(0);
        try {
            List<TheLoai> list = tlDAO.selectAll();
            for (TheLoai tg : list) {
                Object[] row = {tg.getIdtheloai(), tg.getTentheloai(), tg.getTrangthaitl() ? "Đang hoạt động" : "Ngưng hoạt động"};
                modelTheLoai.addRow(row);
            }
            if (modelTheLoai.getRowCount() == 0) {
                Object[] row = {"", "Không tìm thấy"};
                modelTheLoai.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timkiem() {
        String tukhoa = txt_timkiem.getText();
        DefaultTableModel modelTacGia = (DefaultTableModel) tbl_theloai.getModel();
        modelTacGia.setColumnIdentifiers(new Object[]{"ID", "Tên thể loại", "Trạng thái"});
        modelTacGia.setRowCount(0);
        try {
            List<TheLoai> list = tlDAO.timkiemTL(tukhoa);
            for (TheLoai tl : list) {
                Object[] row = {tl.getIdtheloai(), tl.getTentheloai(), tl.getTrangthaitl() ? "Đang hoạt động" : "Ngưng hoạt động"};
                modelTacGia.addRow(row);
            }
            if (modelTacGia.getRowCount() == 0) {
                Object[] row = {"", "Không tìm thấy"};
                modelTacGia.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean batloi_tl() {
        String idtheloai = txt_idtheloai.getText();
        String tentheloai = txt_tentheloai.getText();

        String loi = "";

        if (idtheloai.equalsIgnoreCase("")) {
            loi += "ID thể loại\n";
        } else {
            if (idtheloai.length() != 5) {
                loi += "ID thể loại phải có 5 ký tự\n";
            }
        }
        if (tentheloai.equalsIgnoreCase("")) {
            loi += "Tên thể loại\n";
        }
        boolean OnlyLetters = tentheloai.matches("^[a-zA-Z]*$");
        if (OnlyLetters == false) {
            loi += "Tên thể loại không được nhập số\n";
        }
        if (rdo_hd.isSelected() == false && rdo_nhd.isSelected() == false) {
            loi += "Vui lòng chọn tình trạng thể loại";
        }

        if (!loi.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "--Kiểm tra lại thông tin!!--\n" + loi, "Lỗi", JOptionPane.INFORMATION_MESSAGE);
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

        hd_tl = new javax.swing.ButtonGroup();
        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        txt_timkiem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_theloai = new javax.swing.JTable();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_idtheloai = new javax.swing.JTextField();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_tentheloai = new javax.swing.JTextField();
        btn_xoa = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        rdo_hd = new javax.swing.JRadioButton();
        rdo_nhd = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

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
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1"
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
        jLabel4.setText("QUẢN LÝ THỂ LOẠI");
        crazyPanel2.add(jLabel4);

        crazyPanel1.add(crazyPanel2);

        tbl_theloai.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_theloai.setEditingColumn(0);
        tbl_theloai.setEditingRow(0);
        tbl_theloai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_theloaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_theloai);

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
                "background:lighten(@background,8%);borderWidth:1"
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
                "width 82"
            }
        ));

        jLabel1.setText("ID Thể loại");
        crazyPanel3.add(jLabel1);

        txt_idtheloai.setEditable(false);
        txt_idtheloai.setToolTipText("");
        txt_idtheloai.setEnabled(false);
        crazyPanel3.add(txt_idtheloai);

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

        jLabel2.setText("Tên thể loại");
        crazyPanel3.add(jLabel2);

        txt_tentheloai.setToolTipText("");
        crazyPanel3.add(txt_tentheloai);

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

        jLabel3.setText("Trạng thái");
        crazyPanel3.add(jLabel3);

        hd_tl.add(rdo_hd);
        rdo_hd.setText("Đang hoạt động");
        crazyPanel3.add(rdo_hd);

        hd_tl.add(rdo_nhd);
        rdo_nhd.setText("Ngưng hoạt động");
        crazyPanel3.add(rdo_nhd);

        crazyPanel1.add(crazyPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
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

    private void txt_timkiemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_timkiemMouseExited
        loaddataTheLoai();
    }//GEN-LAST:event_txt_timkiemMouseExited

    private void tbl_theloaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_theloaiMouseClicked
        if (evt.getClickCount() == 2) {
            index = tbl_theloai.getSelectedRow();
            fillFormTL();
        }
    }//GEN-LAST:event_tbl_theloaiMouseClicked

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
            java.util.logging.Logger.getLogger(QLTheLoai_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLTheLoai_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLTheLoai_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLTheLoai_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
            FlatRobotoFont.install();
            FlatLaf.registerCustomDefaultsSource("tableview");
            UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
            SystemProperties pro = new SystemProperties();
            pro.loadFromFile();
            if (!pro.isDarkMode()) {
                System.out.println("có chạy");
                FlatIntelliJLaf.setup();
            } else {
                System.out.println("có chạy22");
                FlatMacDarkLaf.setup();
            }
        } catch (Exception e) {
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QLTheLoai_FORM().setVisible(true);
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
    private javax.swing.ButtonGroup hd_tl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdo_hd;
    private javax.swing.JRadioButton rdo_nhd;
    private javax.swing.JTable tbl_theloai;
    private javax.swing.JTextField txt_idtheloai;
    private javax.swing.JTextField txt_tentheloai;
    private javax.swing.JTextField txt_timkiem;
    // End of variables declaration//GEN-END:variables
}
