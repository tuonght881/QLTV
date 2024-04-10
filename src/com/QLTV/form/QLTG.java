/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.TacGiaDAO;
import com.QLTV.entity.TacGia;
import com.QLTV.utils.XAuth;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven.tabbed.TabbedForm;
import raven.toast.Notifications;

/**
 *
 * @author Tuong
 */
public final class QLTG extends TabbedForm {

    TacGiaDAO tgDAO = new TacGiaDAO();
    int index = -1;

    /**
     * Creates new form QLTG
     */
    public QLTG() {
        initComponents();
        loaddataTacGia();
        loadIDTG();
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

    public void loadIDTG() {
        try {
            String idkh = "";
            List<TacGia> list = tgDAO.select_all();
            for (TacGia kh : list) {
                idkh = kh.getIdtg();
            }
            if (idkh.equalsIgnoreCase("")) {
                idkh = "TG001";
                txt_idtacgia.setText(idkh);
            } else {
                int number = Integer.parseInt(idkh.substring(2));
                number++;
                String newText = "TG" + String.format("%03d", number);
                txt_idtacgia.setText(newText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFormTG(TacGia tg) {
        txt_idtacgia.setText(tg.getIdtg());
        txt_tentacgia.setText(tg.getTentg());
        if (tg.getTrangthaitg() == true) {
            rdo_hd.setSelected(true);
        } else {
            rdo_nhd.setSelected(true);
        }
    }

    public TacGia getFormTG() {
        TacGia tgNew = new TacGia();

        tgNew.setIdtg(txt_idtacgia.getText());
        tgNew.setTentg(txt_tentacgia.getText());
        if (rdo_hd.isSelected()) {
            tgNew.setTrangthaitg(true);
        } else if (rdo_nhd.isSelected()) {
            tgNew.setTrangthaitg(false);
        }
        return tgNew;
    }

    public void fillFormTG() {
        txt_idtacgia.setEnabled(false);
        txt_idtacgia.setEditable(false);
        btn_them.setEnabled(false);
        btn_sua.setEnabled(true);
        btn_xoa.setEnabled(true);
        String idtg = (String) tbl_tacgia.getValueAt(index, 0);
        TacGia tg = tgDAO.select_byID(idtg);
        if (tg == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setFormTG(tg);
        }
    }

    public void themTG() {
        if (batloi_tg()) {
            try {
                TacGia tgNew = getFormTG();
                tgDAO.insert(tgNew);
                loaddataTacGia();
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Thêm thành công");
                //JOptionPane.showMessageDialog(this, "Thêm thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        }
    }

    public void suaTG() {
        if (batloi_tg()) {
            try {
                TacGia tgNew = getFormTG();
                tgDAO.update(tgNew);
                loaddataTacGia();
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Cập nhật thành công");
                //JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }

    public void xoaTG() {
        String entity = txt_idtacgia.getText();
        try {
            tgDAO.delete(entity);
            loaddataTacGia();
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Xoá thành công");
            //JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
        } catch (Exception e) {
            // Bắt các loại ngoại lệ, bao gồm SQLServerException
            if (e.getMessage().contains("conflicted with the REFERENCE constraint")) {
                // Xử lý lỗi về ràng buộc tham chiếu ở đây
                int choice = JOptionPane.showConfirmDialog(this, "Xóa thất bại do tác giả này đã tồn tại sách.\nHành động xoá này chỉ thay đổi trạng thái của tác giả.", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    TacGia tg = tgDAO.select_byID(entity);
                    tg.setTrangthaitg(false);
                    tgDAO.update(tg);
                    loaddataTacGia();
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
        loaddataTacGia();
        loadIDTG();
        txt_idtacgia.setEnabled(false);
        txt_idtacgia.setEditable(false);
        //txt_idtacgia.setText("");
        txt_tentacgia.setText("");
        hd_tg.clearSelection();
        btn_them.setEnabled(true);
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);
    }

    public void loaddataTacGia() {
        DefaultTableModel modelTacGia = (DefaultTableModel) tbl_tacgia.getModel();
        modelTacGia.setColumnIdentifiers(new Object[]{"ID", "Tên Tác Giả", "Trạng Thái"});
        modelTacGia.setRowCount(0);
        try {
            List<TacGia> list = tgDAO.selectAll();
            for (TacGia tg : list) {
                Object[] row = {tg.getIdtg(), tg.getTentg(), tg.getTrangthaitg() ? "Đang hoạt động" : "Ngưng hoạt động"};
                modelTacGia.addRow(row);
            }
            if (modelTacGia.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                modelTacGia.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timkiem() {
        String tukhoa = txt_timkiem.getText();
        DefaultTableModel modelTacGia = (DefaultTableModel) tbl_tacgia.getModel();
        modelTacGia.setColumnIdentifiers(new Object[]{"ID", "Tên Tác Giả", "Trạng Thái"});
        modelTacGia.setRowCount(0);
        try {
            List<TacGia> list = tgDAO.timkiemTG(tukhoa);
            for (TacGia tg : list) {
                Object[] row = {tg.getIdtg(), tg.getTentg(), tg.getTrangthaitg() ? "Đang hoạt động" : "Ngưng hoạt động"};
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

    public boolean batloi_tg() {
        String idtacgia = txt_idtacgia.getText();
        String tentacgia = txt_tentacgia.getText();

        String loi = "";

        if (idtacgia.equalsIgnoreCase("")) {
            loi += "ID tác giả\n";
        } else {
            if (idtacgia.length() != 5) {
                loi += "ID Khách hàng phải có 5 ký tự\n";
            }
        }
        if (tentacgia.equalsIgnoreCase("")) {
            loi += "Tên tác giả\n";
        }
        boolean OnlyLetters = tentacgia.matches(".*\\d.*");
        if (OnlyLetters) {
            loi += "Tên tác giả không được nhập số\n";
        }
        if (rdo_hd.isSelected() == false && rdo_nhd.isSelected() == false) {
            loi += "Vui lòng chọn tình trạng tác giả";
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

        hd_tg = new javax.swing.ButtonGroup();
        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        txt_timkiem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_tacgia = new javax.swing.JTable();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_idtacgia = new javax.swing.JTextField();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_tentacgia = new javax.swing.JTextField();
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
        jLabel4.setText("QUẢN LÝ TÁC GIẢ");
        crazyPanel2.add(jLabel4);

        crazyPanel1.add(crazyPanel2);

        tbl_tacgia.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_tacgia.setEditingColumn(0);
        tbl_tacgia.setEditingRow(0);
        tbl_tacgia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_tacgiaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_tacgia);

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

        jLabel1.setText("(*)ID Tác giả");
        crazyPanel3.add(jLabel1);

        txt_idtacgia.setEditable(false);
        txt_idtacgia.setToolTipText("");
        txt_idtacgia.setEnabled(false);
        crazyPanel3.add(txt_idtacgia);

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

        jLabel2.setText("(*)Tên tác giả");
        crazyPanel3.add(jLabel2);

        txt_tentacgia.setToolTipText("");
        crazyPanel3.add(txt_tentacgia);

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

        hd_tg.add(rdo_hd);
        rdo_hd.setText("Đang hoạt động");
        crazyPanel3.add(rdo_hd);

        hd_tg.add(rdo_nhd);
        rdo_nhd.setText("Ngưng hoạt động");
        crazyPanel3.add(rdo_nhd);

        crazyPanel1.add(crazyPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
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
    }// </editor-fold>//GEN-END:initComponents

    private void txt_timkiemCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemCaretUpdate
        timkiem();
    }//GEN-LAST:event_txt_timkiemCaretUpdate

    private void tbl_tacgiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_tacgiaMouseClicked
        if (evt.getClickCount() == 2) {
            index = tbl_tacgia.getSelectedRow();
            fillFormTG();
        }
    }//GEN-LAST:event_tbl_tacgiaMouseClicked

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
        loaddataTacGia();
    }//GEN-LAST:event_txt_timkiemMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xoa;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private javax.swing.ButtonGroup hd_tg;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdo_hd;
    private javax.swing.JRadioButton rdo_nhd;
    private javax.swing.JTable tbl_tacgia;
    private javax.swing.JTextField txt_idtacgia;
    private javax.swing.JTextField txt_tentacgia;
    private javax.swing.JTextField txt_timkiem;
    // End of variables declaration//GEN-END:variables
}
