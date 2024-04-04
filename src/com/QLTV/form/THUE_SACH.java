/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.DonThueChiTietDAO;
import com.QLTV.dao.DonThueDAO;
import com.QLTV.dao.KhachHangDAO;
import com.QLTV.dao.SachDAO;
import com.QLTV.dao.TacGiaDAO;
import com.QLTV.dao.TheLoaiDAO;
import com.QLTV.dao.ViTriDAO;
import com.QLTV.entity.DonThue;
import com.QLTV.entity.DonThueChiTiet;
import com.QLTV.entity.KhachHang;
import com.QLTV.entity.Sach;
import com.QLTV.entity.TacGia;
import com.QLTV.entity.TheLoai;
import com.QLTV.entity.ViTri;
import com.QLTV.utils.XAuth;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import java.awt.HeadlessException;
import static java.awt.Window.Type.POPUP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import raven.calendar.model.ModelDate;
import raven.calendar.utils.CalendarSelectedListener;

/**
 *
 * @author Tuong
 */
public class THUE_SACH extends javax.swing.JPanel {

    Date ngay;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    SachDAO sachDAO = new SachDAO();
    TheLoaiDAO tlDAO = new TheLoaiDAO();
    TacGiaDAO tgDAO = new TacGiaDAO();
    ViTriDAO vtDAO = new ViTriDAO();
    KhachHangDAO khDAO = new KhachHangDAO();
    DonThueDAO dthueDAO = new DonThueDAO();
    DonThueChiTietDAO dthuectDAO = new DonThueChiTietDAO();

    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    DecimalFormat D_format = new DecimalFormat("0.#");
    int index = -1;
    Timer timer;
    DefaultTableModel model_sach, model_hd;
    Double tongcong = 0.0;
    Double khachdua = 0.0;
    Double thoilai = 0.0;
    Double tienphat = 0.0;
    Double tongtiendambao = 0.0;
    Double tongtien = 0.0;
    Double phantram = 0.0;
    int id;

    /**
     * Creates new form THUE_SACH
     */
    public THUE_SACH() {
        initComponents();
        //applyTableStyle(tbl_sach);
        //applyTableStyle(tbl_hoadon);
        txt_manv.setText(XAuth.user.getManv());
        POPUP.add(jPanel1);
        calendar1.addCalendarSelectedListener(new CalendarSelectedListener() {
            @Override
            public void selected(MouseEvent evt, ModelDate date) {
                ngay = date.toDate();
                String ngayF = sdf.format(ngay);
                txt_ngaytradukien.setText(ngayF.toString());
                System.out.println("=>" + ngayF);
            }
        });
        loaddataSach();
        loadDonThue();
        xuongdong();
        ActionListener act = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = new Date();
                String time = sdf.format(date);
                txt_ngaythuesach.setText(time);
            }
        };
        timer = new Timer(1000, act);
        timer.setInitialDelay(0);
        timer.start();
    }

    public DonThue getDonThueNew() throws ParseException {
        DonThue dthueNew = new DonThue();
        dthueNew.setIddonthue(txt_iddonthue.getText());
        dthueNew.setManv(txt_manv.getText());
        dthueNew.setIdkhach(txt_idkh.getText());
        dthueNew.setNgaytao(txt_ngaythuesach.getText());
        dthueNew.setNgaythue(txt_ngaythuesach.getText());
        dthueNew.setNgaytradukien(txt_ngaytradukien.getText());
        dthueNew.setNgaytra("");
        dthueNew.setTrangthai(false);
        dthueNew.setTienphat(0.0);
        dthueNew.setTongtiendambao(tongtiendambao);
        dthueNew.setThanhtien(tongtien);
        dthueNew.setKhachdua(Double.valueOf(txt_khachdua.getText()));
        dthueNew.setThoilai(thoilai);

        return dthueNew;
    }

    public void xuongdong() {
        // Tạo renderer cho cột đầu tiên (căn lề trái)
        //DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        //leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        // Áp dụng renderer cho cột đầu tiên
        //tbl_hoadon.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        // Tạo renderer cho các cột còn lại (căn lề phải)
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment(SwingConstants.CENTER);
        // Áp dụng renderer cho các cột còn lại
        for (int i = 1; i < tbl_thuesach.getColumnCount(); i++) {
            tbl_thuesach.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        tbl_thuesach.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbl_sach.getColumnModel().getColumn(0).setPreferredWidth(300);
        tbl_thuesach.getColumnModel().getColumn(0).setCellRenderer(new MultiLineTableCellRenderer());
        tbl_sach.getColumnModel().getColumn(0).setCellRenderer(new MultiLineTableCellRenderer());
        tbl_sach.getColumnModel().getColumn(1).setCellRenderer(new MultiLineTableCellRenderer());
    }

    public void ThemDonThue() {
        if (batloi_tk()) {
            try {
                DonThue dthueNew = getDonThueNew();
                //System.out.println(dthueNew.getIddonthue());
                //System.out.println(dthueNew.getIdkhach());
                dthueDAO.insert(dthueNew);//thêm vào đơn thuê
                //System.out.println("tới đây rồi");

                DonThueChiTiet dthueCT = new DonThueChiTiet();
                dthueCT.setIddonthue(txt_iddonthue.getText());
                //System.out.println("tới đây rồi 2");
                for (int i = 0; i < tbl_thuesach.getRowCount(); i++) {
                    dthueCT.setIddonthue(dthueDAO.getIDdonthue());
                    String idsach = sachDAO.getMasach(tbl_thuesach.getValueAt(i, 0).toString());
                    dthueCT.setIdsach(idsach);
                    dthueCT.setSoluong(Integer.parseInt(tbl_thuesach.getValueAt(i, 2).toString()));
                    dthueCT.setTiendambao(Double.parseDouble(tbl_thuesach.getValueAt(i, 6).toString()));
                    dthuectDAO.insert(dthueCT);//thêm vào đơn thuê chi tiết
                    Sach s = sachDAO.select_byID(idsach);
                    int sls = s.getSl();
                    int slsm = sls - (Integer.parseInt(tbl_thuesach.getValueAt(i, 2).toString()));
                    s.setSl(slsm);
                    sachDAO.update(s);
                }
                //System.out.println("tới đây rồi 3");
                loaddataSach();
                model_hd.setRowCount(0);
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!\n" + e.getMessage());
            }
        }
    }

    public void resetForm() {
        loadDonThue();
        loaddataSach();
        loadDonThue();
        tongcong = 0.0;
        khachdua = 0.0;
        thoilai = 0.0;
        tienphat = 0.0;
        tongtiendambao = 0.0;
        tongtien = 0.0;
        phantram = 0.0;
        txt_idkh.setText("");
        txt_tenkh.setText("");
        txt_diemuytin.setText("");
        txt_phantramdambao.setText("");
        txt_sdtkhach.setText("");
        txt_songaymuon.setText("");
        txt_ngaytradukien.setText("");
        txt_tiendambao.setText("");
        txt_tonggiathue.setText("");
        txt_khachdua.setText("");
        txt_thanhtien.setText("");
        txt_thoilai.setText("");
        if (model_hd != null) {
            model_hd.setRowCount(0);
        }
        xuongdong();
    }

    public void loadDonThue() {
        try {
            List<DonThue> list = dthueDAO.selectAll();
            for (DonThue dt : list) {
                id = Integer.parseInt(dt.getIddonthue());
            }
            int id2 = id + 1;
            txt_iddonthue.setText(Integer.toString(id2));
        } catch (Exception e) {
        }
    }

    public void loaddataSach() {
        Date ngay = new Date();
        //txt_ngaybansach.setText(dateFormat.format(ngay));
        //txt_manv.setText(XAuth.user.getHoten());
        model_sach = (DefaultTableModel) tbl_sach.getModel();
        model_sach.setColumnIdentifiers(new Object[]{"Tên sách", "Tác giả", "Vị trí", "Số lượng", "Giá thuê 1 ngày"});
        model_sach.setRowCount(0);
        try {
            List<Sach> list = sachDAO.selectOnStock();
            for (Sach sach : list) {
                TheLoai tl = tlDAO.select_byID(sach.getIdtheloai().toString());
                TacGia tg = tgDAO.select_byID(sach.getIdtacgia());
                ViTri vt = vtDAO.select_byID(sach.getIdvitri());
                Object[] row = {sach.getTensach(), tg.getTentg(), vt.getTenvt(), sach.getSl(), D_format.format(sach.getGiathue1ngay())};
                model_sach.addRow(row);
            }
            if (model_sach.getRowCount() == 0) {
                Object[] row = {"", "Không có dữ liệu"};
                model_sach.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timkiem() {
        String tukhoa = txt_timkiem.getText();
        model_sach = (DefaultTableModel) tbl_sach.getModel();
        model_sach.setColumnIdentifiers(new Object[]{"Tên sách", "Tác giả", "Vị trí", "Số lượng", "Giá thuê 1 ngày"});
        model_sach.setRowCount(0);
        try {
            List<Sach> list = sachDAO.timkiemSach(tukhoa);
            for (Sach sach : list) {
                TheLoai tl = tlDAO.select_byID(sach.getIdtheloai().toString());
                TacGia tg = tgDAO.select_byID(sach.getIdtacgia());
                ViTri vt = vtDAO.select_byID(sach.getIdvitri());
                Object[] row = {sach.getTensach(), tg.getTentg(), vt.getTenvt(), sach.getSl(), D_format.format(sach.getGiathue1ngay())};
                model_sach.addRow(row);
            }
            if (model_sach.getRowCount() == 0) {
                Object[] row = {"", "Không tìm thấy"};
                model_sach.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean batloi_tk() {
        String kh = txt_idkh.getText();
        String khachdua = txt_khachdua.getText();
        String thoilai = txt_thoilai.getText();
        String sdtk = txt_sdtkhach.getText();
        String songaymuon = txt_songaymuon.getText();
        String regex = "^\\d*[1-9]\\d*$";
        boolean isPositiveNumberKD = khachdua.matches(regex);
        boolean isPositiveNumberSDT = sdtk.matches(regex);
        boolean isPositiveNumberNM = songaymuon.matches(regex);

        String loi = "";
        if (tbl_thuesach.getRowCount() == 0) {
            loi += "Chưa có quyển sách nào!\n";
        }
        if (sdtk.equalsIgnoreCase("")) {
            loi += "Số điện thoại khách\n";
        }
        if (isPositiveNumberSDT == false) {
            loi += "Vui lòng chỉ nhập số dương\n";
        }
        if (songaymuon.equalsIgnoreCase("")) {
            loi += "Số ngày mượn\n";
        }
        if (isPositiveNumberNM == false) {
            loi += "Vui lòng chỉ nhập số dương\n";
        }
        if (khachdua.equalsIgnoreCase("")) {
            loi += "Khách đưa\n";
        }
        if (isPositiveNumberKD == false) {
            loi += "Vui lòng chỉ nhập số dương\n";
        }
        if (thoilai.equalsIgnoreCase("")) {
            loi += "Thối lại\n";
        }
        if (kh.equalsIgnoreCase("")) {
            loi += "Kiểm tra lại thông tin khách hàng\n";
        }
        if (!loi.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "--Vui lòng kiểm tra lại thông tin!!--\n" + loi, "Lỗi", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    public void checkinputSo(JTextField textField) {
// Thêm DocumentListener để lắng nghe sự kiện thay đổi trong Document
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleDocumentChange(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleDocumentChange(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleDocumentChange(e);
            }

            private void handleDocumentChange(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Document doc = e.getDocument();
                    try {
                        String text = doc.getText(0, doc.getLength());
                        if (!text.isEmpty() && !isNumber(text)) {
                            // Nếu không phải là số, xóa dữ liệu vừa nhập
                            doc.remove(e.getOffset(), e.getLength());
                        }
                    } catch (BadLocationException ex) {
                        //System.out.println("Vui lòng nhập số!");
                    }
                });
            }

            private boolean isNumber(String text) {
                try {
                    double number = Double.parseDouble(text);
                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        });
    }

    void tinhTONGtiendambao() {
        tongtiendambao = 0.0;
        for (int i = 0; i < tbl_thuesach.getRowCount(); i++) {
            tongtiendambao += Double.parseDouble(tbl_thuesach.getValueAt(i, 6).toString());
        }
        txt_tiendambao.setText(currencyVN.format(tongtiendambao));
    }

    void tinhtonggiathue() {
        tongcong = 0.0;
        for (int i = 0; i < tbl_thuesach.getRowCount(); i++) {
            tongcong += Double.parseDouble(tbl_thuesach.getValueAt(i, 5).toString());
        }
        //tinhtiendambao();
        txt_tonggiathue.setText(currencyVN.format(tongcong));
    }

    void tinhngaytradukien() throws ParseException {
        Date ngaymuon = sdf.parse(txt_ngaythuesach.getText());
        int songaymuon;
        if (txt_songaymuon.getText().equalsIgnoreCase("")) {
            songaymuon = 0;
        } else {
            songaymuon = Integer.parseInt(txt_songaymuon.getText());
        }
        Calendar calendarNgayMuon = Calendar.getInstance();
        calendarNgayMuon.setTime(ngaymuon);
        calendarNgayMuon.add(Calendar.DAY_OF_MONTH, songaymuon);
        Date newNgayMuon = calendarNgayMuon.getTime();
        txt_ngaytradukien.setText(sdf.format(newNgayMuon));
    }

    public void timKH() {
        String sdt = txt_sdtkhach.getText();
        Pattern pattern = Pattern.compile("^0[1-9][0-9]{8}+$");
        Matcher regexMatcher = pattern.matcher(sdt);
        if (regexMatcher.matches()) {
            try {
                KhachHang kh = khDAO.select_bysdt(sdt);
                txt_idkh.setText(kh.getIdkhach());
                txt_tenkh.setText(kh.getHotenkhach());
                txt_diemuytin.setText(Integer.toString(kh.getDiemuytin()));
                phantram = ((100 - Double.valueOf(kh.getDiemuytin())));//tính phần trăm phí đảm bảo
                txt_phantramdambao.setText(D_format.format(phantram) + "%");
                tinhngaymuon();
            } catch (NullPointerException e) {
                int choice = JOptionPane.showConfirmDialog(this, "Khách hàng không tồn tại!\n Thêm khách hàng mới?", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
                QLDocGia_FORM qlkh = new QLDocGia_FORM();
                if (choice == JOptionPane.OK_OPTION) {
                    qlkh.setVisible(true);
                    qlkh.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                } else {
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ", "Thông báo", JOptionPane.OK_OPTION);
        }

    }

    public void taoHoaDon(MouseEvent evt) throws NumberFormatException, HeadlessException {
        if (evt.getClickCount() == 2) {
            index = tbl_sach.getSelectedRow();
            String sl = JOptionPane.showInputDialog(this, "Nhập số lượng", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
            try {
                while (sl.equalsIgnoreCase("")) {
                    sl = JOptionPane.showInputDialog(this, "Nhập số lượng", "Thông báo", JOptionPane.OK_CANCEL_OPTION);
                }
            } catch (NullPointerException e) {
                //JOptionPane.showMessageDialog(this, "Lỗi\n" + e.getMessage());
            }
            int slc = Integer.parseInt(tbl_sach.getValueAt(index, 3).toString());
            if (Integer.parseInt(sl) > slc) {
                JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ!", "Thông báo", JOptionPane.OK_OPTION);
            } else if (sl != null) {
                Object[] row = new Object[7];
                model_hd = (DefaultTableModel) tbl_thuesach.getModel();
                if (index != -1) {
                    String selectBook = model_sach.getValueAt(index, 0).toString();
                    if (tbl_thuesach.getRowCount() <= 0) {
                        row[0] = selectBook;
                        row[1] = model_sach.getValueAt(index, 4).toString();//lấy giá của sách
                        row[2] = sl;
                        Double thanhtien = (Double.parseDouble(sl) * Double.parseDouble(row[1].toString()));
                        row[3] = D_format.format(thanhtien);
                        row[4] = 0.0;
                        row[5] = 0.0;
                        row[6] = 0.0;
                        model_hd.addRow(row);
                        tinhtonggiathue();
                    } else {
                        int hang = -1;
                        for (int i = 0; i < tbl_thuesach.getRowCount(); i++) {
                            if (selectBook.equalsIgnoreCase(model_hd.getValueAt(i, 0).toString())) {
                                hang = i;
                                break;
                            } else {
                                hang = -1;
                            }
                        }
                        if (hang == -1) {
                            row[0] = selectBook;
                            row[1] = model_sach.getValueAt(index, 4).toString(); //row 1 là giá sách
                            row[2] = sl;
                            Double thanhtien = (Double.parseDouble(sl) * Double.parseDouble(row[1].toString()));
                            row[3] = D_format.format(thanhtien);
                            row[4] = 0.0;
                            row[5] = 0.0;
                            row[6] = 0.0;
                            model_hd.addRow(row);
                            tinhtonggiathue();
                        } else {
                            row[0] = selectBook;
                            row[1] = model_sach.getValueAt(index, 4).toString(); //row 1 là giá sách
                            int sl2 = Integer.parseInt(tbl_thuesach.getValueAt(hang, 2).toString()) + Integer.parseInt(sl);
                            tbl_thuesach.setValueAt(sl2, hang, 2);
                            Double thanhtien = (Double.parseDouble(sl) * Double.parseDouble(row[1].toString()));
                            Double thanhtien2 = Double.parseDouble(tbl_thuesach.getValueAt(hang, 3).toString()) + thanhtien;
                            tbl_thuesach.setValueAt(D_format.format(thanhtien2), hang, 3);
                            tinhtonggiathue();
                        }
                    }
                }
            }
        }
    }

    private void showPopup() {
        //int x = txt_ngaysinh.getLocationOnScreen().x;
        //int y = txt_ngaysinh.getLocationOnScreen().y + txt_ngaysinh.getHeight();
        POPUP.show(txt_ngaytradukien, 0, txt_ngaytradukien.getHeight());
    }
    // Renderer để tự động xuống hàng trong các ô của bảng

    static class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {

        MultiLineTableCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setFont(table.getFont());
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    public void tinhngaymuon() {
        try {
            double ngaymuon;
            tinhngaytradukien();
            if (txt_songaymuon.getText().equalsIgnoreCase("")) {
                ngaymuon = 0;
            } else {
                ngaymuon = Double.parseDouble(txt_songaymuon.getText());
            }
            for (int i = 0; i < tbl_thuesach.getRowCount(); i++) {
                tbl_thuesach.setValueAt(txt_songaymuon.getText(), i, 4);
            }
            for (int i = 0; i < tbl_thuesach.getRowCount(); i++) {
                Double tien = Double.valueOf(tbl_thuesach.getValueAt(i, 3).toString());
                tbl_thuesach.setValueAt(D_format.format(ngaymuon * tien), i, 5);//giá thuê của sách

                Double giathue = Double.valueOf(tbl_thuesach.getValueAt(i, 5).toString());
                tbl_thuesach.setValueAt(D_format.format(giathue * (phantram / 100)), i, 6);//tiền đảm bảo của mỗi cuốn sách
            }
            tinhtonggiathue();
            tinhTONGtiendambao();
            tongtien = tongcong + tongtiendambao;
            txt_thanhtien.setText(currencyVN.format(tongtien));
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(THUE_SACH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Chưa có thông tin của khách hàng!");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        POPUP = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        calendar1 = new raven.calendar.Calendar();
        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        txt_timkiem = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_ngaythuesach = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txt_manv = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        crazyPanel4 = new raven.crazypanel.CrazyPanel();
        crazyPanel6 = new raven.crazypanel.CrazyPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_sach = new javax.swing.JTable();
        crazyPanel7 = new raven.crazypanel.CrazyPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_iddonthue = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txt_idkh = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_tenkh = new javax.swing.JTextField();
        lbl_diemuytin = new javax.swing.JLabel();
        txt_diemuytin = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txt_phantramdambao = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_thuesach = new javax.swing.JTable();
        crazyPanel8 = new raven.crazypanel.CrazyPanel();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel5 = new javax.swing.JLabel();
        txt_sdtkhach = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        txt_songaymuon = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_ngaytradukien = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        txt_tonggiathue = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_tiendambao = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txt_thanhtien = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        txt_khachdua = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_thoilai = new javax.swing.JTextField();
        crazyPanel5 = new raven.crazypanel.CrazyPanel();
        btn_reset = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();

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
                "JTextField.placeholderText=Tìm kiếm;background:@background",
                "background:lighten(@background,8%)",
                "background:lighten(@background,8%)",
                "background:lighten(@background,8%)"
            }
        ));
        crazyPanel2.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[][][][][]push[]",
            "",
            new String[]{
                "width 200",
                "",
                "width 180"
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

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Ngày:");
        crazyPanel2.add(jLabel3);

        txt_ngaythuesach.setEnabled(false);
        crazyPanel2.add(txt_ngaythuesach);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("ID Nhân viên");
        crazyPanel2.add(jLabel1);

        txt_manv.setEditable(false);
        txt_manv.setToolTipText("");
        txt_manv.setEnabled(false);
        crazyPanel2.add(txt_manv);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("THUÊ SÁCH");
        crazyPanel2.add(jLabel4);

        crazyPanel1.add(crazyPanel2);

        crazyPanel4.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background;",
            null
        ));
        crazyPanel4.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[]",
            "",
            new String[]{
                "",
                ""
            }
        ));

        crazyPanel6.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background;",
            null
        ));
        crazyPanel6.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 1",
            "[]",
            "[][]",
            new String[]{
                "width 800",
                "",
                "width 800"
            }
        ));

        tbl_sach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tên sách", "Tác giả", "Vị trí", "Số lượng", "Giá thuê 1 ngày"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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

        crazyPanel6.add(jScrollPane1);

        crazyPanel7.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background;",
            new String[]{
                "background:@background",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:@background"
            }
        ));
        crazyPanel7.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "",
            "",
            new String[]{
                "",
                "",
                "",
                "",
                "",
                "width 150"
            }
        ));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("ID Đơn thuê");
        crazyPanel7.add(jLabel2);

        txt_iddonthue.setEditable(false);
        txt_iddonthue.setToolTipText("");
        txt_iddonthue.setEnabled(false);
        txt_iddonthue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_iddonthueActionPerformed(evt);
            }
        });
        crazyPanel7.add(txt_iddonthue);

        jLabel12.setText("ID khách");
        crazyPanel7.add(jLabel12);

        txt_idkh.setEditable(false);
        txt_idkh.setEnabled(false);
        crazyPanel7.add(txt_idkh);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Khách hàng");
        crazyPanel7.add(jLabel6);

        txt_tenkh.setEditable(false);
        txt_tenkh.setEnabled(false);
        crazyPanel7.add(txt_tenkh);

        lbl_diemuytin.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_diemuytin.setText("Điểm uy tín");
        crazyPanel7.add(lbl_diemuytin);

        txt_diemuytin.setEditable(false);
        txt_diemuytin.setEnabled(false);
        crazyPanel7.add(txt_diemuytin);

        jLabel13.setText("Phí đảm bảo");
        crazyPanel7.add(jLabel13);

        txt_phantramdambao.setEditable(false);
        txt_phantramdambao.setEnabled(false);
        crazyPanel7.add(txt_phantramdambao);

        crazyPanel6.add(crazyPanel7);

        tbl_thuesach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên sách (1)", "Đơn giá (2)", "Số lượng (3)", "Giá x SL (4)", "Số ngày mượn (5)", "Giá thuê(4) x (5)", "Tiền đảm bảo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbl_thuesach);

        crazyPanel6.add(jScrollPane2);

        crazyPanel4.add(crazyPanel6);

        crazyPanel8.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background;",
            null
        ));
        crazyPanel8.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 1",
            "[]",
            "[][][]",
            new String[]{
                ""
            }
        ));

        crazyPanel3.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "background:lighten(@background,8%)",
                "background:@background",
                "background:lighten(@background,8%)",
                "background:lighten(@background,8%)",
                "background:@background",
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
                "background:@background",
                "background:lighten(@background,8%)",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:@background",
                "background:@background"
            }
        ));
        crazyPanel3.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 2",
            "[][]",
            "[][][][][][][][][][]",
            new String[]{
                "width 80",
                "width 240",
                "width 80",
                "width 240",
                "width 80",
                "width 240",
                "width 80",
                "width 240",
                "width 80",
                "width 240",
                "width 80",
                "width 240",
                "width 80",
                "width 240",
                "width 80",
                "width 240",
                "width 80",
                "width 240",
                "width 80",
                "width 240",
                "width 80",
                "width 240"
            }
        ));
        crazyPanel3.setName(""); // NOI18N

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("SDT khách");
        crazyPanel3.add(jLabel5);

        txt_sdtkhach.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_sdtkhachCaretUpdate(evt);
            }
        });
        txt_sdtkhach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_sdtkhachKeyPressed(evt);
            }
        });
        crazyPanel3.add(txt_sdtkhach);
        crazyPanel3.add(jSeparator1);
        crazyPanel3.add(jSeparator2);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel14.setText("Số ngày mượn");
        crazyPanel3.add(jLabel14);

        txt_songaymuon.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_songaymuonCaretUpdate(evt);
            }
        });
        txt_songaymuon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_songaymuonKeyPressed(evt);
            }
        });
        crazyPanel3.add(txt_songaymuon);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Ngày trả dự kiến");
        crazyPanel3.add(jLabel7);

        txt_ngaytradukien.setEditable(false);
        txt_ngaytradukien.setEnabled(false);
        crazyPanel3.add(txt_ngaytradukien);
        crazyPanel3.add(jSeparator3);
        crazyPanel3.add(jSeparator4);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Tổng giá thuê");
        crazyPanel3.add(jLabel9);

        txt_tonggiathue.setEditable(false);
        txt_tonggiathue.setEnabled(false);
        crazyPanel3.add(txt_tonggiathue);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("Tổng tiền đảm bảo");
        crazyPanel3.add(jLabel8);

        txt_tiendambao.setEditable(false);
        txt_tiendambao.setEnabled(false);
        crazyPanel3.add(txt_tiendambao);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel15.setText("Thành tiền:");
        crazyPanel3.add(jLabel15);

        txt_thanhtien.setEditable(false);
        txt_thanhtien.setEnabled(false);
        crazyPanel3.add(txt_thanhtien);
        crazyPanel3.add(jSeparator5);
        crazyPanel3.add(jSeparator6);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Khách đưa");
        crazyPanel3.add(jLabel11);

        txt_khachdua.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_khachduaCaretUpdate(evt);
            }
        });
        txt_khachdua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_khachduaKeyPressed(evt);
            }
        });
        crazyPanel3.add(txt_khachdua);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Thối lại");
        crazyPanel3.add(jLabel10);

        txt_thoilai.setEditable(false);
        txt_thoilai.setEnabled(false);
        crazyPanel3.add(txt_thoilai);

        crazyPanel8.add(crazyPanel3);

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

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });
        crazyPanel5.add(btn_them);

        crazyPanel8.add(crazyPanel5);

        crazyPanel4.add(crazyPanel8);

        crazyPanel1.add(crazyPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1259, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1247, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 604, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_timkiemCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_timkiemCaretUpdate
        timkiem();
    }//GEN-LAST:event_txt_timkiemCaretUpdate

    private void tbl_sachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_sachMouseClicked
        taoHoaDon(evt);
        tinhngaymuon();
    }//GEN-LAST:event_tbl_sachMouseClicked

    private void txt_iddonthueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_iddonthueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_iddonthueActionPerformed

    private void txt_sdtkhachKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_sdtkhachKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txt_sdtkhach.getText().equalsIgnoreCase("")) {
            timKH();
        }
    }//GEN-LAST:event_txt_sdtkhachKeyPressed
    int dem = 0;
    private void txt_khachduaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_khachduaKeyPressed
        boolean check = false;
        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txt_khachdua.getText().equalsIgnoreCase("") && dem == 0) {
            khachdua = Double.parseDouble(txt_khachdua.getText());
            if (khachdua >= tongcong) {
                thoilai = khachdua - tongtien;
                txt_thoilai.setText(currencyVN.format(thoilai));
                check = true;
                dem++;
            } else {
                JOptionPane.showMessageDialog(this, "Kiểm tra lại khách đưa!");
                check = false;
            }
        }else
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && dem == 1) {
            ThemDonThue();
            dem = 0;
        }
    }//GEN-LAST:event_txt_khachduaKeyPressed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        resetForm();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        ThemDonThue();
    }//GEN-LAST:event_btn_themActionPerformed

    private void calendar1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calendar1MousePressed
        if (evt.getClickCount() == 2) {
            POPUP.setVisible(false);
        }
    }//GEN-LAST:event_calendar1MousePressed

    private void txt_songaymuonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_songaymuonKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !txt_songaymuon.getText().equalsIgnoreCase("")) {
            tinhngaymuon();
        }
    }//GEN-LAST:event_txt_songaymuonKeyPressed

    private void txt_timkiemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_timkiemMouseExited
        loaddataSach();
    }//GEN-LAST:event_txt_timkiemMouseExited

    private void txt_sdtkhachCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_sdtkhachCaretUpdate
        checkinputSo(txt_sdtkhach);
    }//GEN-LAST:event_txt_sdtkhachCaretUpdate

    private void txt_songaymuonCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_songaymuonCaretUpdate
        checkinputSo(txt_songaymuon);
    }//GEN-LAST:event_txt_songaymuonCaretUpdate

    private void txt_khachduaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_khachduaCaretUpdate
        checkinputSo(txt_khachdua);
    }//GEN-LAST:event_txt_khachduaCaretUpdate


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu POPUP;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_them;
    private raven.calendar.Calendar calendar1;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private raven.crazypanel.CrazyPanel crazyPanel4;
    private raven.crazypanel.CrazyPanel crazyPanel5;
    private raven.crazypanel.CrazyPanel crazyPanel6;
    private raven.crazypanel.CrazyPanel crazyPanel7;
    private raven.crazypanel.CrazyPanel crazyPanel8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel lbl_diemuytin;
    private javax.swing.JTable tbl_sach;
    private javax.swing.JTable tbl_thuesach;
    private javax.swing.JTextField txt_diemuytin;
    private javax.swing.JTextField txt_iddonthue;
    private javax.swing.JTextField txt_idkh;
    private javax.swing.JTextField txt_khachdua;
    private javax.swing.JTextField txt_manv;
    private javax.swing.JTextField txt_ngaythuesach;
    private javax.swing.JTextField txt_ngaytradukien;
    private javax.swing.JTextField txt_phantramdambao;
    private javax.swing.JTextField txt_sdtkhach;
    private javax.swing.JTextField txt_songaymuon;
    private javax.swing.JTextField txt_tenkh;
    private javax.swing.JTextField txt_thanhtien;
    private javax.swing.JTextField txt_thoilai;
    private javax.swing.JTextField txt_tiendambao;
    private javax.swing.JTextField txt_timkiem;
    private javax.swing.JTextField txt_tonggiathue;
    // End of variables declaration//GEN-END:variables
}
