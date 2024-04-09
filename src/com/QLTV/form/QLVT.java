/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.ViTriDAO;
import com.QLTV.entity.ViTri;
import com.QLTV.utils.XAuth;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven.tabbed.TabbedForm;

/**
 *
 * @author Tuong
 */
public final class QLVT extends TabbedForm {

    ViTriDAO vitriDAO = new ViTriDAO();
    int index = -1;
    int id;

    /**
     * Creates new form QLVT
     */
    public QLVT() {
        initComponents();
        loaddataViTri();
        loadIDvt();
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

    public void setFormTG(ViTri vt) {
        txt_idvitri.setText(vt.getIdvitri());
        txt_tenvitri.setText(vt.getTenvt());
        if (vt.getTrangthaivt() == true) {
            rdo_hd.setSelected(true);
        } else {
            rdo_nhd.setSelected(true);
        }
    }

    public ViTri getFormTG() {
        ViTri vtNew = new ViTri();

        vtNew.setIdvitri(txt_idvitri.getText());
        vtNew.setTenvt(txt_tenvitri.getText());
        if (rdo_hd.isSelected()) {
            vtNew.setTrangthaivt(true);
        } else if (rdo_nhd.isSelected()) {
            vtNew.setTrangthaivt(false);
        }
        return vtNew;
    }

    public void fillFormViTri() {
        txt_idvitri.setEnabled(false);
        txt_idvitri.setEditable(false);
        btn_them.setEnabled(false);
        btn_sua.setEnabled(true);
        btn_xoa.setEnabled(true);
        String idvt = String.valueOf(tbl_vitri.getValueAt(index, 0));
        ViTri vt = vitriDAO.select_byID(idvt);
        if (vt == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setFormTG(vt);
        }
    }

    public void themTG() {
        if (batloi_vt()) {
            try {
                ViTri vtNew = getFormTG();
                vitriDAO.insert(vtNew);
                loaddataViTri();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!\n" + e.getMessage());
            }
        }
    }

    public void suaTG() {
        if (batloi_vt()) {
            try {
                ViTri vtNew = getFormTG();
                vitriDAO.update(vtNew);
                loaddataViTri();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!\n" + e.getMessage());
            }
        }
    }

    public void xoaTG() {
        String entity = txt_idvitri.getText();
        try {
            vitriDAO.delete(entity);
            loaddataViTri();
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
        } catch (Exception e) {
            // Bắt các loại ngoại lệ, bao gồm SQLServerException
            if (e.getMessage().contains("conflicted with the REFERENCE constraint")) {
                // Xử lý lỗi về ràng buộc tham chiếu ở đây
                int choice = JOptionPane.showConfirmDialog(this, "Xóa thất bại do vị trí này đang có trong sách.\nHành động xoá này chỉ thay đổi trạng thái của vị trí.", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    ViTri vt = vitriDAO.select_byID(entity);
                    vt.setTrangthaivt(false);
                    vitriDAO.update(vt);
                    loaddataViTri();
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
        loaddataViTri();
        loadIDvt();
        txt_idvitri.setEnabled(false);
        txt_idvitri.setEditable(false);
        //txt_idvitri.setText("");
        txt_tenvitri.setText("");
        hd_vt.clearSelection();
        btn_them.setEnabled(true);
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);
    }

    public void loaddataViTri() {
        DefaultTableModel modelViTri = (DefaultTableModel) tbl_vitri.getModel();
        modelViTri.setColumnIdentifiers(new Object[]{"ID", "Tên vị tri", "Trạng Thái"});
        modelViTri.setRowCount(0);
        try {
            List<ViTri> list = vitriDAO.selectAll();
            for (ViTri vt : list) {
                Object[] row = {vt.getIdvitri(), vt.getTenvt(), vt.getTrangthaivt() ? "Đang hoạt động" : "Ngưng hoạt động"};
                modelViTri.addRow(row);
            }
            if (modelViTri.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                modelViTri.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadIDvt() {
        try {
            List<ViTri> list = vitriDAO.select_all();
            for (ViTri dt : list) {
                id = Integer.parseInt(dt.getIdvitri());
            }
            int id2 = id + 1;
            txt_idvitri.setText(Integer.toString(id2));
        } catch (Exception e) {
        }
    }

    public void timkiem() {
        String tukhoa = txt_timkiem.getText();
        DefaultTableModel modelViTri = (DefaultTableModel) tbl_vitri.getModel();
        modelViTri.setColumnIdentifiers(new Object[]{"ID", "Tên vị trí", "Trạng Thái"});
        modelViTri.setRowCount(0);
        try {
            List<ViTri> list = vitriDAO.timkiemTvt(tukhoa);
            for (ViTri vt : list) {
                Object[] row = {vt.getIdvitri(), vt.getTenvt(), vt.getTrangthaivt() ? "Đang hoạt động" : "Ngưng hoạt động"};
                modelViTri.addRow(row);
            }
            if (modelViTri.getRowCount() == 0) {
                Object[] row = {"", "Không tìm thấy"};
                modelViTri.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean batloi_vt() {
        String idkhachhang = txt_idvitri.getText();
        String tentacgia = txt_tenvitri.getText();

        String loi = "";
        if (idkhachhang.length() > 5) {
            loi += "ID phải nhỏ hơn 100000";
        }
        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher regexMatcher = pattern.matcher(idkhachhang);
        if (!regexMatcher.matches()) {
            loi += "ID phải nhập số";
        }
        if (tentacgia.equalsIgnoreCase("")) {
            loi += "Tên vị trí\n";
        }
//        boolean OnlyLetters = tentacgia.matches(".*\\d.*");
//        if (OnlyLetters) {
//            loi += "Tên vị trí không được nhập số\n";
//        }
        if (rdo_hd.isSelected() == false && rdo_nhd.isSelected() == false) {
            loi += "Vui lòng chọn tình trạng hoạt động";
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

        hd_vt = new javax.swing.ButtonGroup();
        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        txt_timkiem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_vitri = new javax.swing.JTable();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_idvitri = new javax.swing.JTextField();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_tenvitri = new javax.swing.JTextField();
        btn_xoa = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        rdo_hd = new javax.swing.JRadioButton();
        rdo_nhd = new javax.swing.JRadioButton();

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
        jLabel4.setText("QUẢN LÝ VỊ TRÍ");
        crazyPanel2.add(jLabel4);

        crazyPanel1.add(crazyPanel2);

        tbl_vitri.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_vitri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_vitriMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_vitri);

        crazyPanel1.add(jScrollPane2);

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

        jLabel1.setText("(*)ID Vị trí");
        crazyPanel3.add(jLabel1);

        txt_idvitri.setEditable(false);
        txt_idvitri.setToolTipText("");
        txt_idvitri.setEnabled(false);
        crazyPanel3.add(txt_idvitri);

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

        jLabel2.setText("(*)Tên vị trí");
        crazyPanel3.add(jLabel2);

        txt_tenvitri.setToolTipText("");
        crazyPanel3.add(txt_tenvitri);

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

        jLabel3.setText("(*)Trạng thái");
        crazyPanel3.add(jLabel3);

        hd_vt.add(rdo_hd);
        rdo_hd.setText("Đang hoạt động");
        crazyPanel3.add(rdo_hd);

        hd_vt.add(rdo_nhd);
        rdo_nhd.setText("Ngưng hoạt động");
        crazyPanel3.add(rdo_nhd);

        crazyPanel1.add(crazyPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
    }// </editor-fold>//GEN-END:initComponents

    private void txt_timkiemCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemCaretUpdate
        timkiem();
    }//GEN-LAST:event_txt_timkiemCaretUpdate

    private void tbl_vitriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_vitriMouseClicked
        if (evt.getClickCount() == 2) {
            index = tbl_vitri.getSelectedRow();
            fillFormViTri();
        }
    }//GEN-LAST:event_tbl_vitriMouseClicked

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

    private void txt_timkiemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_timkiemMouseExited
        loaddataViTri();
    }//GEN-LAST:event_txt_timkiemMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xoa;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private javax.swing.ButtonGroup hd_vt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rdo_hd;
    private javax.swing.JRadioButton rdo_nhd;
    private javax.swing.JTable tbl_vitri;
    private javax.swing.JTextField txt_idvitri;
    private javax.swing.JTextField txt_tenvitri;
    private javax.swing.JTextField txt_timkiem;
    // End of variables declaration//GEN-END:variables
}
