/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
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
import com.QLTV.utils.XAuth;
import com.QLTV.utils.Ximg;
import java.awt.Image;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Tuong
 */
public class QLS extends javax.swing.JPanel {

    Date ngay;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SachDAO sachDAO = new SachDAO();
    TheLoaiDAO tlDAO = new TheLoaiDAO();
    TacGiaDAO tgDAO = new TacGiaDAO();
    ViTriDAO vtDAO = new ViTriDAO();
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    int index = -1;

    /**
     * Creates new form QLS
     */
    public QLS() {
        initComponents();
        loaddataSach();
        loaddataTL();
        loaddataTG();
        loaddataVT();
        loadIDS();
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

    public void loadIDS() {
        try {
            String idkh = "";
            List<Sach> list = sachDAO.select_all();
            for (Sach kh : list) {
                idkh = kh.getIdsach();
            }
            if (idkh.equalsIgnoreCase("")) {
                idkh = "S0001";
                txt_idsach.setText(idkh);
            } else {
                int number = Integer.parseInt(idkh.substring(1));
                number++;
                String newText = "S" + String.format("%04d", number);
                txt_idsach.setText(newText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFormSach(Sach sach) throws ParseException {
        TheLoai tl = tlDAO.select_byID(sach.getIdtheloai());
        TacGia tg = tgDAO.select_byID(sach.getIdtacgia());
        ViTri vt = vtDAO.select_byID(sach.getIdvitri());
        txt_idsach.setText(sach.getIdsach());
        txt_tensach.setText(sach.getTensach());

        cbo_TL.setSelectedItem(tl.getTentheloai());
        cbo_tg.setSelectedItem(tg.getTentg());
        cbo_vt.setSelectedItem(vt.getTenvt());

        cbo_TL.setSelectedItem(tl.getTentheloai());
        cbo_tg.setSelectedItem(tg.getTentg());
        cbo_vt.setSelectedItem(vt.getTenvt());

        txt_soluong.setText(Integer.toString(sach.getSl()));
        txt_giathue1ngay.setText(Double.toString(sach.getGiathue1ngay()));
        txt_giaban.setText(Double.toString(sach.getGiaban()));
        if (sach.getTrangthaisach() == true) {
            rdo_hd.setSelected(true);
        } else {
            rdo_nhd.setSelected(true);
        }
        if (sach.getAnhsach() == null) {
            lbl_anh.setIcon(null);
            lbl_anh.setText("Không có ảnh");
        } else {
            ImageIcon icon = Ximg.read_img(sach.getAnhsach());
            Image img = icon.getImage();
            Image imgScale = img.getScaledInstance(120, 130, Image.SCALE_SMOOTH);

            lbl_anh.setToolTipText(sach.getAnhsach());
            lbl_anh.setIcon(new ImageIcon(imgScale));
            lbl_anh.setText("");
        }
    }

    public Sach getFormSach() throws ParseException {
        Sach sachNew = new Sach();
        TheLoai tl = tlDAO.select_byTheLoai(cbo_TL.getSelectedItem().toString());
        TacGia tg = tgDAO.select_byTenTG(cbo_tg.getSelectedItem().toString());
        ViTri vt = vtDAO.select_byTenVT(cbo_vt.getSelectedItem().toString());
        sachNew.setIdsach(txt_idsach.getText());
        sachNew.setTensach(txt_tensach.getText());
        sachNew.setIdtheloai(tl.getIdtheloai());
        sachNew.setIdtacgia(tg.getIdtg());
        sachNew.setIdvitri(vt.getIdvitri());
        sachNew.setSl(Integer.parseInt(txt_soluong.getText()));
        sachNew.setGiathue1ngay(Double.valueOf(txt_giathue1ngay.getText()));
        sachNew.setGiaban(Double.valueOf(txt_giaban.getText()));
        if (rdo_hd.isSelected()) {
            sachNew.setTrangthaisach(true);
        } else {
            sachNew.setTrangthaisach(false);
        }
        sachNew.setAnhsach(lbl_anh.getToolTipText());
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
        String entity = txt_idsach.getText();
        try {
            sachDAO.delete(entity);
            loaddataSach();
            JOptionPane.showMessageDialog(this, "Xoá thành công");
            resetForm();
        } catch (Exception e) {
            // Bắt các loại ngoại lệ, bao gồm SQLServerException
            if (e.getMessage().contains("conflicted with the REFERENCE constraint")) {
                // Xử lý lỗi về ràng buộc tham chiếu ở đây
                int choice = JOptionPane.showConfirmDialog(this, "Xóa thất bại do sách này đã tồn tại trong hoá đơn.\nHành động xoá này chỉ thay đổi trạng thái của sách.", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    Sach s = sachDAO.select_byID(entity);
                    s.setTrangthaisach(false);
                    sachDAO.update(s);
                    loaddataSach();
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
        loaddataSach();
        loaddataTG();
        loaddataTL();
        loaddataVT();
        loadIDS();
        txt_idsach.setEnabled(false);
        txt_idsach.setEditable(false);
        btn_them.setEnabled(true);
        btn_sua.setEnabled(false);
        btn_xoa.setEnabled(false);

        //txt_idsach.setText("");
        txt_tensach.setText("");
        txt_giaban.setText("");
        txt_soluong.setText("");
        txt_giaban.setText("");
        txt_giathue1ngay.setText("");
        btnG_trangthai.clearSelection();
        lbl_anh.setToolTipText("");
        lbl_anh.setIcon(null);
        lbl_anh.setText("Hình ảnh");
    }

    public void loaddataTL() {
        cbo_TL.removeAllItems();
        List<TheLoai> list = tlDAO.selectAll();
        for (TheLoai tl : list) {
            cbo_TL.addItem(tl.getTentheloai());
        }
    }

    public void loaddataTG() {
        cbo_tg.removeAllItems();
        List<TacGia> list = tgDAO.selectAll();
        for (TacGia tg : list) {
            cbo_tg.addItem(tg.getTentg());
        }
    }

    public void loaddataVT() {
        cbo_vt.removeAllItems();
        List<ViTri> list = vtDAO.selectAll();
        for (ViTri vt : list) {
            cbo_vt.addItem(vt.getTenvt());
        }
    }

    public void loaddataSach() {
        DefaultTableModel modelTK = (DefaultTableModel) tbl_sach.getModel();
        modelTK.setColumnIdentifiers(new Object[]{"ID Sách", "Tên sách", "Thể loại", "Tác giả", "Vị trí", "Số lượng", "Giá thuê", "Giá bán", "Trạng thái"});
        modelTK.setRowCount(0);
        try {
            List<Sach> list = sachDAO.selectAll();
            for (Sach sach : list) {
                TheLoai tl = tlDAO.select_byID(sach.getIdtheloai().toString());
                TacGia tg = tgDAO.select_byID(sach.getIdtacgia());
                ViTri vt = vtDAO.select_byID(sach.getIdvitri());
                Object[] row = {sach.getIdsach(), sach.getTensach(), tl.getTentheloai(), tg.getTentg(), vt.getTenvt(), sach.getSl(), currencyVN.format(sach.getGiathue1ngay()), currencyVN.format(sach.getGiaban()), sach.getTrangthaisach() ? "Hoạt động" : "Ngưng hoạt động"};
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
                Object[] row = {sach.getIdsach(), sach.getTensach(), tl.getTentheloai(), tg.getTentg(), vt.getTenvt(), sach.getSl(), currencyVN.format(sach.getGiathue1ngay()), currencyVN.format(sach.getGiaban())};
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
        //String theloai = txt_theloai.getText();
        String tensach = txt_tensach.getText();
        //String tacgia = txt_tacgia.getText();
        String sl = txt_soluong.getText();
        String giathue = txt_giathue1ngay.getText();
        String giaban = txt_giaban.getText();
        //String vitri = txt_vitri.getText();

        String loi = "";

        if (idsach.equalsIgnoreCase("")) {
            loi += "ID sách\n";
        }
//        else {
//            if (idsach.length() != 5) {
//                loi += "ID tài khoản phải có 5 ký tự\n";
//            }
//        }

        if (tensach.equalsIgnoreCase("")) {
            loi += "Tên sách\n";
        }
        boolean OnlyLetters = tensach.matches(".*\\d.*");
        //System.out.println(OnlyLetters);
        if (OnlyLetters) {
            loi += "Tên sách không được nhập số\n";
        }
        boolean Onlynb = sl.matches("\\d+");
        if (sl.equalsIgnoreCase("")) {
            loi += "Số lượng\n";
        }
        if(!Onlynb){
            loi += "Số lượng không được nhập chữ\n";
        }
        if (giaban.equalsIgnoreCase("")) {
            loi += "Giá bán\n";
        }
        if(!giaban.matches("\\d+")){
            loi+="Giá bán không được nhập chữ";
        }
        if (giathue.equalsIgnoreCase("")) {
            loi += "Giá thuê\n";
        }
        if(!giathue.matches("\\d+")){
            loi+="Giá thuê không được nhập chữ";
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnG_trangthai = new javax.swing.ButtonGroup();
        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        txt_timkiem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_sach = new javax.swing.JTable();
        crazyPanel8 = new raven.crazypanel.CrazyPanel();
        crazyPanel6 = new raven.crazypanel.CrazyPanel();
        lbl_anh = new javax.swing.JLabel();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_idsach = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_tensach = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbo_TL = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cbo_tg = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cbo_vt = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txt_soluong = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_giathue1ngay = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_giaban = new javax.swing.JTextField();
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
        jLabel4.setText("QUẢN LÝ SÁCH");
        crazyPanel2.add(jLabel4);

        crazyPanel1.add(crazyPanel2);

        tbl_sach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
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
        crazyPanel8.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "fill",
            "",
            "",
            null
        ));

        crazyPanel6.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "",
            "",
            new String[]{
                "w 100,h 100"
            }
        ));
        crazyPanel6.setPreferredSize(new java.awt.Dimension(100, 120));
        crazyPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_anh.setText("Ảnh");
        lbl_anh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_anhMouseClicked(evt);
            }
        });
        crazyPanel6.add(lbl_anh, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 130));

        crazyPanel8.add(crazyPanel6);

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
        jLabel1.setText("(*)ID Sách:");
        crazyPanel3.add(jLabel1);

        txt_idsach.setEditable(false);
        txt_idsach.setToolTipText("");
        txt_idsach.setEnabled(false);
        crazyPanel3.add(txt_idsach);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("(*)Tên sách:");
        crazyPanel3.add(jLabel3);
        crazyPanel3.add(txt_tensach);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Thể loại:");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        crazyPanel3.add(jLabel2);

        cbo_TL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_TLMouseClicked(evt);
            }
        });
        crazyPanel3.add(cbo_TL);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Tác giả:");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        crazyPanel3.add(jLabel5);

        cbo_tg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_tgMouseClicked(evt);
            }
        });
        crazyPanel3.add(cbo_tg);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Vị trí:");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });
        crazyPanel3.add(jLabel9);

        cbo_vt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbo_vtMouseClicked(evt);
            }
        });
        crazyPanel3.add(cbo_vt);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("(*)Số lượng:");
        crazyPanel3.add(jLabel11);
        crazyPanel3.add(txt_soluong);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("(*)Giá thuê/ngày");
        crazyPanel3.add(jLabel10);
        crazyPanel3.add(txt_giathue1ngay);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("(*)Giá bán:");
        crazyPanel3.add(jLabel8);
        crazyPanel3.add(txt_giaban);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("(*)Trạng thái:");
        crazyPanel3.add(jLabel6);

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

        crazyPanel3.add(crazyPanel7);

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

        crazyPanel1.add(crazyPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 813, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 801, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 471, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_timkiemCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemCaretUpdate
        timkiem();
    }//GEN-LAST:event_txt_timkiemCaretUpdate

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

    private void lbl_anhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_anhMouseClicked
        try {
            JFileChooser fc = new JFileChooser("image\\");

            //	    lọc file hình ảnh
            FileFilter filter = new FileNameExtensionFilter("Image Files", "png", "GIF", "jpg");
            fc.setFileFilter(filter);
            //	    Mở hộp thoại
            fc.showOpenDialog(null);
            File f = fc.getSelectedFile();
            Ximg.save_img(f);
            ImageIcon icon = Ximg.read_img(f.getName());
            Image img = icon.getImage();
            Image imgScale = img.getScaledInstance(120, 130, Image.SCALE_SMOOTH);

            lbl_anh.setText("");
            lbl_anh.setIcon(new ImageIcon(imgScale));
            lbl_anh.setToolTipText(f.getName());
        } catch (IllegalArgumentException e) {
            //            Msg_Box.alert(this, "Bạn chưa chọn hình ảnh");
        } catch (NullPointerException e) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }//GEN-LAST:event_lbl_anhMouseClicked

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        xoaSach();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        resetForm();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        suaSach();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        themSach();
    }//GEN-LAST:event_btn_themActionPerformed

    private void txt_timkiemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_timkiemMouseExited
        loaddataSach();
    }//GEN-LAST:event_txt_timkiemMouseExited

    private void cbo_TLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_TLMouseClicked
        if (evt.getClickCount() == 2) {
            QLTheLoai_FORM qls = new QLTheLoai_FORM();
            qls.setVisible(true);
            qls.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }//GEN-LAST:event_cbo_TLMouseClicked

    private void cbo_tgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_tgMouseClicked
        if (evt.getClickCount() == 2) {
            QLTG_FORM qls = new QLTG_FORM();
            qls.setVisible(true);
            qls.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }//GEN-LAST:event_cbo_tgMouseClicked

    private void cbo_vtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbo_vtMouseClicked
        if (evt.getClickCount() == 2) {
            QLViTri_FORM qls = new QLViTri_FORM();
            qls.setVisible(true);
            qls.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }//GEN-LAST:event_cbo_vtMouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        if (evt.getClickCount() == 2) {
            QLTheLoai_FORM qls = new QLTheLoai_FORM();
            qls.setVisible(true);
            qls.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        if (evt.getClickCount() == 2) {
            QLTG_FORM qls = new QLTG_FORM();
            qls.setVisible(true);
            qls.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        if (evt.getClickCount() == 2) {
            QLViTri_FORM qls = new QLViTri_FORM();
            qls.setVisible(true);
            qls.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }//GEN-LAST:event_jLabel9MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnG_trangthai;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JComboBox<String> cbo_TL;
    private javax.swing.JComboBox<String> cbo_tg;
    private javax.swing.JComboBox<String> cbo_vt;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private raven.crazypanel.CrazyPanel crazyPanel5;
    private raven.crazypanel.CrazyPanel crazyPanel6;
    private raven.crazypanel.CrazyPanel crazyPanel7;
    private raven.crazypanel.CrazyPanel crazyPanel8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbl_anh;
    private javax.swing.JRadioButton rdo_hd;
    private javax.swing.JRadioButton rdo_nhd;
    private javax.swing.JTable tbl_sach;
    private javax.swing.JTextField txt_giaban;
    private javax.swing.JTextField txt_giathue1ngay;
    private javax.swing.JTextField txt_idsach;
    private javax.swing.JTextField txt_soluong;
    private javax.swing.JTextField txt_tensach;
    private javax.swing.JTextField txt_timkiem;
    // End of variables declaration//GEN-END:variables
}
