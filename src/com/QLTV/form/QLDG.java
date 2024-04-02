/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.KhachHangDAO;
import com.QLTV.entity.KhachHang;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Component;
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

/**
 *
 * @author Tuong
 */
public class QLDG extends javax.swing.JPanel {
    KhachHangDAO khDAO = new KhachHangDAO();
    int index = -1;
    /**
     * Creates new form QLDG
     */
    public QLDG() {
        initComponents();
        applyTableStyle(tbl_khachhang);
        loaddataKhachHang();
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);
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
        txt_idkhachhang.setEnabled(false);
        txt_idkhachhang.setEditable(false);
        btn_them.setEnabled(false);
        btn_sua.setEnabled(true);
        btn_xoa.setEnabled(true);
        String idkh = (String) tbl_khachhang.getValueAt(index, 0);
        KhachHang kh = khDAO.select_byID(idkh);
        if (kh==null) {
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
            JOptionPane.showMessageDialog(this, "Xoá thất bại\n" + e.getMessage());
        }
    }

    public void resetForm() {
        loaddataKhachHang();
        txt_idkhachhang.setEnabled(true);
        txt_idkhachhang.setEditable(true);
        txt_idkhachhang.setText("");
        txt_tenkhachhang.setText("");
        txt_diemuytin.setText("");
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

    public boolean batloi_kh() {
        String idKhachhang = txt_idkhachhang.getText();
        String hotenkhachhang = txt_tenkhachhang.getText();
        String sdt = txt_sdt.getText();
        String diemuytin = txt_diemuytin.getText();

        String loi = "";

        if (idKhachhang.equalsIgnoreCase("")) {
            loi += "ID khách hàng\n";
        } else {
            if (idKhachhang.length() != 5) {
                loi += "ID Khách hàng phải có 5 ký tự\n";
            }
        }
        if (hotenkhachhang.equalsIgnoreCase("")) {
            loi += "Họ tên khách hàng\n";
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

        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        txt_timkiem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_khachhang = new javax.swing.JTable();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_idkhachhang = new javax.swing.JTextField();
        btn_them = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_tenkhachhang = new javax.swing.JTextField();
        btn_xoa = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txt_sdt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_diemuytin = new javax.swing.JTextField();

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

        txt_idkhachhang.setToolTipText("");
        crazyPanel3.add(txt_idkhachhang);

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
        crazyPanel3.add(txt_diemuytin);

        crazyPanel1.add(crazyPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
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
