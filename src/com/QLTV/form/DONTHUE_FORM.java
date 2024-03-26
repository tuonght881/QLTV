/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.SachDAO;
import com.QLTV.dao.TacGiaDAO;
import com.QLTV.dao.TheLoaiDAO;
import com.QLTV.dao.ViTriDAO;
import com.QLTV.entity.Sach;
import com.QLTV.entity.TacGia;
import com.QLTV.entity.TheLoai;
import com.QLTV.entity.ViTri;
import com.QLTV.utils.Ximg;
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
import java.awt.Image;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;

/**
 *
 * @author Tuong
 */
public class DONTHUE_FORM extends javax.swing.JFrame {

    Date ngay;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SachDAO sachDAO = new SachDAO();
    TheLoaiDAO tlDAO = new TheLoaiDAO();
    TacGiaDAO tgDAO = new TacGiaDAO();
    ViTriDAO vtDAO = new ViTriDAO();
    int index = -1;

    /**
     * Creates new form tesst
     */
    public DONTHUE_FORM() {
        initComponents();
        applyTableStyle(tbl_sach);
        loaddataSach();
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);
    }

    public void setFormSach(Sach sach) throws ParseException {
        TheLoai tl = tlDAO.select_byID(sach.getIdtheloai());
        TacGia tg = tgDAO.select_byID(sach.getIdtacgia());
        ViTri vt = vtDAO.select_byID(sach.getIdvitri());
        txt_idsach.setText(sach.getIdsach());
        txt_tensach.setText(sach.getTensach());

        txt_theloai.setText(tl.getTentheloai());
        txt_tacgia.setText(tg.getTentg());
        txt_vitri.setText(vt.getTenvt());

        txt_soluong.setText(Integer.toString(sach.getSl()));
        txt_giathue1ngay.setText(Double.toString(sach.getGiathue1ngay()));
        txt_giaban.setText(Double.toString(sach.getGiaban()));

    }

    public Sach getFormSach() throws ParseException {
        Sach sachNew = new Sach();
        TheLoai tl = tlDAO.select_byTheLoai(txt_theloai.getText());
        TacGia tg = tgDAO.select_byTenTG(txt_tacgia.getText());
        ViTri vt = vtDAO.select_byTenVT(txt_vitri.getText());
        sachNew.setIdsach(txt_idsach.getText());
        sachNew.setTensach(txt_tensach.getText());
        sachNew.setIdtheloai(tl.getIdtheloai());
        sachNew.setIdtacgia(tg.getIdtg());
        sachNew.setIdvitri(vt.getIdvitri());
        sachNew.setSl(Integer.parseInt(txt_soluong.getText()));
        sachNew.setGiathue1ngay(Double.valueOf(txt_giathue1ngay.getText()));
        sachNew.setGiaban(Double.valueOf(txt_giaban.getText()));

        return sachNew;
    }

    public void fillFormSach() throws ParseException {
        txt_idsach.setEnabled(false);
        txt_idsach.setEditable(false);
        btn_them.setEnabled(false);
        btn_sua.setEnabled(true);
        btn_xoa.setEnabled(true);

        String idsach = (String) tbl_sach.getValueAt(index, 0);
        Sach sach = sachDAO.select_byID(idsach);
        if (sach == null) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu", "Lỗi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setFormSach(sach);
        }
    }

    public void themSach() {
        if (batloi_tk()) {
            try {
                Sach sachNew = getFormSach();
                sachDAO.insert(sachNew);
                loaddataSach();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!\n" + e.getMessage());
            }
        }
    }

    public void suaSach() {
        if (batloi_tk()) {
            try {
                Sach sachNew = getFormSach();
                sachDAO.update(sachNew);
                loaddataSach();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!\n" + e.getMessage());
            }
        }
    }

    public void xoaSach() {
        try {
            String entity = txt_idsach.getText();
            sachDAO.delete(entity);
            loaddataSach();
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xoá thất bại\n" + e.getMessage());
        }
    }

    public void resetForm() {
        loaddataSach();
        txt_idsach.setEnabled(true);
        txt_idsach.setEditable(true);
        btn_them.setEnabled(true);
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);

        txt_idsach.setText("");
        txt_theloai.setText("");
        txt_tacgia.setText("");
        txt_tensach.setText("");
        txt_vitri.setText("");
        txt_giaban.setText("");
        txt_soluong.setText("");
        txt_giaban.setText("");
        txt_giathue1ngay.setText("");
        btnG_trangthai.clearSelection();
    }

    public void loaddataSach() {
        DefaultTableModel modelTK = (DefaultTableModel) tbl_sach.getModel();
        modelTK.setColumnIdentifiers(new Object[]{"ID Sách", "Tên sách", "Thể loại", "Tác giả", "Vị trí", "Số lượng", "Giá thuê", "Giá bán"});
        modelTK.setRowCount(0);
        try {
            List<Sach> list = sachDAO.selectAll();
            for (Sach sach : list) {
                TheLoai tl = tlDAO.select_byID(sach.getIdtheloai().toString());
                TacGia tg = tgDAO.select_byID(sach.getIdtacgia());
                ViTri vt = vtDAO.select_byID(sach.getIdvitri());
                Object[] row = {sach.getIdsach(), sach.getTensach(), tl.getTentheloai(), tg.getTentg(), vt.getTenvt(), sach.getSl(), sach.getGiathue1ngay(), sach.getGiaban()};
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

    public void timkiem() {
        String tukhoa = txt_timkiem.getText();
        DefaultTableModel modelTK = (DefaultTableModel) tbl_sach.getModel();
        modelTK.setColumnIdentifiers(new Object[]{"ID Sách", "Tên sách", "Thể loại", "Tác giả", "Vị trí", "Số lượng", "Giá thuê", "Giá bán"});
        modelTK.setRowCount(0);
        try {
            List<Sach> list = sachDAO.timkiemSach(tukhoa);
            for (Sach sach : list) {
                TheLoai tl = tlDAO.select_byID(sach.getIdtheloai().toString());
                TacGia tg = tgDAO.select_byID(sach.getIdtacgia());
                ViTri vt = vtDAO.select_byID(sach.getIdvitri());
                Object[] row = {sach.getIdsach(), sach.getTensach(), tl.getTentheloai(), tg.getTentg(), vt.getTenvt(), sach.getSl(), sach.getGiathue1ngay(), sach.getGiaban()};
                modelTK.addRow(row);
            }
            if (modelTK.getRowCount() == 0) {
                Object[] row = {"", "Không tìm thấy"};
                modelTK.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean batloi_tk() {
        String idsach = txt_idsach.getText();
        String theloai = txt_theloai.getText();
        String tensach = txt_tensach.getText();
        String tacgia = txt_tacgia.getText();
        String sl = txt_soluong.getText();
        String giathue = txt_giathue1ngay.getText();
        String giaban = txt_giaban.getText();
        String vitri = txt_vitri.getText();

        String loi = "";

        if (idsach.equalsIgnoreCase("")) {
            loi += "ID sách\n";
        }
//        else {
//            if (idsach.length() != 5) {
//                loi += "ID tài khoản phải có 5 ký tự\n";
//            }
//        }
        if (theloai.equalsIgnoreCase("")) {
            loi += "Thể loại\n";
        }
        if (tensach.equalsIgnoreCase("")) {
            loi += "Tên sách\n";
        }
        if (tacgia.equalsIgnoreCase("")) {
            loi += "Tác giả\n";
        }
        if (giaban.equalsIgnoreCase("")) {
            loi += "Giá bán\n";
        }
        if (giathue.equalsIgnoreCase("")) {
            loi += "Giá thuê\n";
        }
        if (vitri.equalsIgnoreCase("")) {
            loi += "Vị trí\n";
        }
        if (sl.equalsIgnoreCase("")) {
            loi += "Số lượng\n";
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

        btnG_trangthai = new javax.swing.ButtonGroup();
        btn = new javax.swing.JButton();
        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        txt_timkiem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_sach = new javax.swing.JTable();
        crazyPanel8 = new raven.crazypanel.CrazyPanel();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_idsach = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_tensach = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_theloai = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_tacgia = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_vitri = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txt_soluong = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_giathue1ngay = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_giaban = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        crazyPanel5 = new raven.crazypanel.CrazyPanel();
        btn_xoa = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

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
        jLabel4.setText("QUẢN LÝ HOÁ ĐƠN");
        crazyPanel2.add(jLabel4);

        crazyPanel1.add(crazyPanel2);

        tbl_sach.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_sach.setEditingColumn(0);
        tbl_sach.setEditingRow(0);
        tbl_sach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_sachMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_sach);

        crazyPanel1.add(jScrollPane1);

        crazyPanel8.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            null
        ));

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
            "wrap 6",
            "[][][][][][]",
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

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("ID Sách:");
        crazyPanel3.add(jLabel1);

        txt_idsach.setToolTipText("");
        crazyPanel3.add(txt_idsach);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Tên sách:");
        crazyPanel3.add(jLabel3);
        crazyPanel3.add(txt_tensach);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Thể loại:");
        crazyPanel3.add(jLabel2);

        txt_theloai.setToolTipText("");
        crazyPanel3.add(txt_theloai);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Tác giả:");
        crazyPanel3.add(jLabel5);
        crazyPanel3.add(txt_tacgia);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Vị trí:");
        crazyPanel3.add(jLabel9);
        crazyPanel3.add(txt_vitri);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Số lượng:");
        crazyPanel3.add(jLabel11);
        crazyPanel3.add(txt_soluong);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Giá thuê/ngày");
        crazyPanel3.add(jLabel10);
        crazyPanel3.add(txt_giathue1ngay);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("Giá bán:");
        crazyPanel3.add(jLabel8);
        crazyPanel3.add(txt_giaban);

        crazyPanel8.add(crazyPanel3);

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

        jButton1.setText("QUYỀN ADMIN");
        crazyPanel5.add(jButton1);

        crazyPanel1.add(crazyPanel5);

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
                        .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1022, Short.MAX_VALUE)
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
        themSach();
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        resetForm();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        suaSach();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        xoaSach();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void tbl_sachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_sachMouseClicked
        if (evt.getClickCount() == 2) {
            index = tbl_sach.getSelectedRow();
            try {
                fillFormSach();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi\n" + e.getMessage());
            }
        }
    }//GEN-LAST:event_tbl_sachMouseClicked

    private void txt_timkiemCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemCaretUpdate
        timkiem();
    }//GEN-LAST:event_txt_timkiemCaretUpdate

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
            java.util.logging.Logger.getLogger(DONTHUE_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DONTHUE_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DONTHUE_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DONTHUE_FORM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new DONTHUE_FORM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn;
    private javax.swing.ButtonGroup btnG_trangthai;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xoa;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private raven.crazypanel.CrazyPanel crazyPanel5;
    private raven.crazypanel.CrazyPanel crazyPanel8;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tbl_sach;
    private javax.swing.JTextField txt_giaban;
    private javax.swing.JTextField txt_giathue1ngay;
    private javax.swing.JTextField txt_idsach;
    private javax.swing.JTextField txt_soluong;
    private javax.swing.JTextField txt_tacgia;
    private javax.swing.JTextField txt_tensach;
    private javax.swing.JTextField txt_theloai;
    private javax.swing.JTextField txt_timkiem;
    private javax.swing.JTextField txt_vitri;
    // End of variables declaration//GEN-END:variables
}
