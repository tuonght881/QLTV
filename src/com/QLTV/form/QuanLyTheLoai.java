package com.QLTV.form;

import com.QLTV.dao.TheLoaiDAO;
import com.QLTV.entity.TheLoai;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.raven.component.Form;
import com.raven.properties.SystemProperties;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
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

public class QuanLyTheLoai extends Form {

    public QuanLyTheLoai() {
        pro.loadFromFile();
        init();
        initComponents();
        applyTableStyle(tbl_theloai);
        loaddataTheLoai();
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);
    }
    TheLoaiDAO tlDAO = new TheLoaiDAO();
    int index = -1;
    SystemProperties pro = new SystemProperties();
    /**
     * Creates new form QLTheLoai_FormPanel
     */
private void init() {
        try {
            //FlatRobotoFont.install();
            //FlatLaf.registerCustomDefaultsSource("tableview");
            //UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
            System.out.println(pro.isDarkMode());
            if (pro.isDarkMode() == true) {
                //System.out.println("->sáng");
                System.out.println("==true");
                FlatDarculaLaf.setup();
//                EventQueue.invokeLater(() -> {
//                    FlatDarculaLaf.setup();
//                });
            } else {
                //System.out.println("->tối");
                System.out.println("==false");
                FlatIntelliJLaf.setup();
//                EventQueue.invokeLater(() -> {
//                    FlatIntelliJLaf.setup();
//                });
            }
        } catch (Exception e) {
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
        try {
            String entity = txt_idtheloai.getText();
            tlDAO.delete(entity);
            loaddataTheLoai();
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xoá thất bại\n" + e.getMessage());
        }
    }

    public void resetForm() {
        loaddataTheLoai();
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
        if (rdo_hd.isSelected() == false && rdo_nhd.isSelected() == false) {
            loi += "Vui lòng chọn tình trạng thể loại";
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
                        label.setHorizontalAlignment(SwingConstants.LEADING);
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
        btn_them = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_tentheloai = new javax.swing.JTextField();
        btn_xoa = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        rdo_hd = new javax.swing.JRadioButton();
        rdo_nhd = new javax.swing.JRadioButton();

        crazyPanel1.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            null
        ));
        crazyPanel1.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap,fill,insets 15",
            "[fill]",
            "[grow 0][grow 0][fill]",
            null
        ));
        crazyPanel1.setOpaque(false);

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
        crazyPanel2.setOpaque(false);

        txt_timkiem.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_timkiemCaretUpdate(evt);
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
        crazyPanel3.setOpaque(false);

        jLabel1.setText("ID Thể loại");
        crazyPanel3.add(jLabel1);

        txt_idtheloai.setToolTipText("");
        crazyPanel3.add(txt_idtheloai);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 947, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 533, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(crazyPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_timkiemCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemCaretUpdate
        timkiem();
    }//GEN-LAST:event_txt_timkiemCaretUpdate

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
