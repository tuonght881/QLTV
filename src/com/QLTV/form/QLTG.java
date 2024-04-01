/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.TacGiaDAO;
import com.QLTV.entity.KhachHang;
import com.QLTV.entity.TacGia;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Component;
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

/**
 *
 * @author Tuong
 */
public class QLTG extends javax.swing.JPanel {
TacGiaDAO tgDAO = new TacGiaDAO();
    int index = -1;
    /**
     * Creates new form QLTG
     */
    public QLTG() {
        initComponents();
        applyTableStyle(tbl_tacgia);
        loaddataTacGia();
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);
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
                JOptionPane.showMessageDialog(this, "Thêm thành công");
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
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }

    public void xoaTG() {
        try {
            String entity = txt_idtacgia.getText();
            tgDAO.delete(entity);
            loaddataTacGia();
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xoá thất bại");
        }
    }

    public void resetForm() {
        loaddataTacGia();
        txt_idtacgia.setEnabled(true);
        txt_idtacgia.setEditable(true);
        txt_idtacgia.setText("");
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
        if (rdo_hd.isSelected() == false && rdo_nhd.isSelected() == false) {
            loi += "Vui lòng chọn tình trạng tác giả";
        }

        if (!loi.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "--Kiểm tra lại thông tin!!--\n" + loi, "Lỗi", JOptionPane.INFORMATION_MESSAGE);
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
                    if (header == false) {
                        if (column == 4) {
                            if (Double.parseDouble(value.toString()) > 0) {
                                com.setForeground(new Color(17, 182, 60));
                                label.setText("+" + value);
                            } else {
                                com.setForeground(new Color(202, 48, 48));
                            }
                        } else {
                            if (isSelected) {
                                com.setForeground(table.getSelectionForeground());
                            } else {
                                com.setForeground(table.getForeground());
                            }
                        }
                    }
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
        btn_them = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
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

        jLabel1.setText("ID Tác giả");
        crazyPanel3.add(jLabel1);

        txt_idtacgia.setToolTipText("");
        crazyPanel3.add(txt_idtacgia);

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });
        crazyPanel3.add(btn_them);

        btn_sua.setText("Cập nhật");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });
        crazyPanel3.add(btn_sua);

        jLabel2.setText("Tên tác giả");
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

        jLabel3.setText("Trạng thái");
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
