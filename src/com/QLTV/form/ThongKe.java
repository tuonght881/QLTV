/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.QLTV.form;

import com.QLTV.dao.DonThueChiTietDAO;
import com.QLTV.dao.DonThueDAO;
import com.QLTV.dao.HoaDonChiTietDAO;
import com.QLTV.dao.HoaDonDAO;
import com.QLTV.entity.slSachBan;
import com.QLTV.entity.slSachThue;
import com.QLTV.entity.tongdoanhthu;
import com.QLTV.utils.XAuth;
import com.raven.chart2.ModelChart;
import java.awt.Color;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javaswingdev.chart.ModelPieChart;
import javaswingdev.chart.PieChart;
import raven.tabbed.TabbedForm;

/**
 *
 * @author Tuong
 */
public final class ThongKe extends TabbedForm {

    HoaDonDAO hdDAO = new HoaDonDAO();
    DonThueDAO dthueDAO = new DonThueDAO();
    HoaDonChiTietDAO hdctDAO = new HoaDonChiTietDAO();
    DonThueChiTietDAO dthuectDAO = new DonThueChiTietDAO();
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

    /**
     * Creates new form ThongKe
     */
    public ThongKe() {
        initComponents();
        initchartngay();
        initchartthang();
        bieudo.addLegend("Doanh thu bán", new Color(12, 84, 175), new Color(0, 108, 247));
        bieudo.addLegend("Doanh thu thuê", new Color(5, 125, 0), new Color(95, 209, 69));
        bieudothang.addLegend("Doanh thu bán", new Color(12, 84, 175), new Color(0, 108, 247));
        bieudothang.addLegend("Doanh thu thuê", new Color(5, 125, 0), new Color(95, 209, 69));
        pieChart.setChartType(PieChart.PeiChartType.DONUT_CHART);
        chartslthue.setChartType(PieChart.PeiChartType.DEFAULT);
        initpiechart();
        initpiechartsls_tg();
        initChartDT();

        jbl_tdoanhthu1.setVisible(false);
        jbl_tdoanhthu2.setVisible(false);
        lbl_doanhthu1.setVisible(false);
        lbl_doanhthu2.setVisible(false);
        
    }

    public void initChartDT() {
        List<tongdoanhthu> sblist = hdDAO.dthu();
        //System.out.println(sblist.getFirst());
        //pieChart.add(new ModelPieChart("Doanh thu bán sách",sblist.getDoanhThuBanSach(),color));
        for (tongdoanhthu sban : sblist) {
            doanhthu.addData(new ModelPieChart("Doanh thu bán sách", sban.getDoanhThuBanSach(), new Color(23, 126, 238)));
            doanhthu.addData(new ModelPieChart("Doanh thuê sách bán sách", sban.getDoanhThuThueSach(), new Color(47, 157, 64)));
            jbl_tdoanhthu.setText(currencyVN.format(sban.getTongDoanhThu()));
        }
    }

    public void initpiechart() {
        // Mảng các màu sẵn có
        Color[] colors = new Color[]{
            new Color(23, 126, 238),
            new Color(47, 157, 64),
            new Color(221, 65, 65),
            new Color(196, 151, 58),
            new Color(23, 126, 238),
            new Color(255, 99, 132),
            new Color(54, 162, 235), // Thêm các màu khác ở đây nếu cần
        };
        int colorIndex = 0;
        List<slSachBan> sblist = hdctDAO.slSachBans();
        for (slSachBan sban : sblist) {
            Color color = colors[colorIndex];
            pieChart.addData(new ModelPieChart(sban.getTenSach(), sban.getSoLuongBan(), color));
            colorIndex = (colorIndex + 1) % colors.length;
        }
    }

    public void initpiechartsls_tg() {
        // Mảng các màu sẵn có
        Color[] colors = new Color[]{
            new Color(23, 126, 238),
            new Color(47, 157, 64),
            new Color(221, 65, 65),
            new Color(196, 151, 58),
            new Color(23, 126, 238),
            new Color(255, 99, 132),
            new Color(54, 162, 235), // Thêm các màu khác ở đây nếu cần
        };
        int colorIndex = 0;
        List<slSachThue> sblist = dthuectDAO.slSachthue();
        //System.out.println(sblist);
        for (slSachThue sban : sblist) {
            Color color = colors[colorIndex];
            chartslthue.addData(new ModelPieChart(sban.getTenSach(), sban.getSoLuongThue(), color));
            colorIndex = (colorIndex + 1) % colors.length;
        }
    }

    public void initchartngay() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM");
        // Lấy ngày hiện tại và giảm đi 6 ngày
        cal.add(Calendar.DATE, -6);
        for (int i = 1; i <= 7; i++) {
            String date = df.format(cal.getTime());
            double dthuban = hdDAO.doanhThuNgay(date);
            double dthuthue = dthueDAO.doanhThuthueNgayTong(date);
            bieudo.addData(new ModelChart(date, new double[]{dthuban, dthuthue}));
            cal.add(Calendar.DATE, 1);
        }
        bieudo.start();
    }

    public void initchartthang() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM");
        // Lấy tháng hiện tại và trừ đi 6
        cal.add(Calendar.MONTH, -6);
        for (int i = 1; i <= 7; i++) {
            String date = df.format(cal.getTime());
            int month = Integer.parseInt(date);
            double dthuban = hdDAO.doanhThuthang(month);
            double dthuthue = dthueDAO.doanhThuthuethangTong(month);
            bieudothang.addData(new ModelChart("Tháng " + date, new double[]{dthuban, dthuthue}));
            cal.add(Calendar.MONTH, 1);
        }
        bieudothang.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new raven.crazypanel.CrazyPanel();
        crazyPanel4 = new raven.crazypanel.CrazyPanel();
        crazyPanel5 = new raven.crazypanel.CrazyPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        pieChart = new javaswingdev.chart.PieChart();
        crazyPanel10 = new raven.crazypanel.CrazyPanel();
        lbl_doanhthu2 = new javax.swing.JLabel();
        jbl_tdoanhthu2 = new javax.swing.JLabel();
        crazyPanel9 = new raven.crazypanel.CrazyPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        chartslthue = new javaswingdev.chart.PieChart();
        crazyPanel8 = new raven.crazypanel.CrazyPanel();
        lbl_doanhthu1 = new javax.swing.JLabel();
        jbl_tdoanhthu1 = new javax.swing.JLabel();
        crazyPanel7 = new raven.crazypanel.CrazyPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        doanhthu = new javaswingdev.chart.PieChart();
        crazyPanel6 = new raven.crazypanel.CrazyPanel();
        lbl_doanhthu = new javax.swing.JLabel();
        jbl_tdoanhthu = new javax.swing.JLabel();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        bieudo = new com.raven.chart2.LineChart();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        jLabel2 = new javax.swing.JLabel();
        bieudothang = new com.raven.chart2.Chart();

        main.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "fill,wrap 1",
            "",
            "",
            null
        ));

        crazyPanel4.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "fill",
            "",
            "",
            null
        ));

        crazyPanel5.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "fill,wrap 1",
            "[][]",
            "[][]",
            new String[]{
                "",
                ""
            }
        ));

        jLabel3.setText("Số lượng sách bán được theo tựa sách");
        crazyPanel5.add(jLabel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pieChart, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(pieChart, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        crazyPanel5.add(jPanel1);

        lbl_doanhthu2.setText("Tổng doanh thu:");
        crazyPanel10.add(lbl_doanhthu2);

        jbl_tdoanhthu2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jbl_tdoanhthu2.setText("jLabel6");
        crazyPanel10.add(jbl_tdoanhthu2);

        crazyPanel5.add(crazyPanel10);

        crazyPanel4.add(crazyPanel5);

        crazyPanel9.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "fill,wrap 1",
            "[][]",
            "[][]",
            new String[]{
                "",
                ""
            }
        ));

        jLabel5.setText("Số lượng sách cho thuê theo tựa sách");
        crazyPanel9.add(jLabel5);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chartslthue, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(chartslthue, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        crazyPanel9.add(jPanel3);

        lbl_doanhthu1.setText("Tổng doanh thu:");
        crazyPanel8.add(lbl_doanhthu1);

        jbl_tdoanhthu1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jbl_tdoanhthu1.setText("jLabel6");
        crazyPanel8.add(jbl_tdoanhthu1);

        crazyPanel9.add(crazyPanel8);

        crazyPanel4.add(crazyPanel9);

        crazyPanel7.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "fill,wrap 1",
            "[][]",
            "[][]",
            new String[]{
                "",
                ""
            }
        ));

        jLabel4.setText("Doanh thu");
        crazyPanel7.add(jLabel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(doanhthu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(doanhthu, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        crazyPanel7.add(jPanel2);

        lbl_doanhthu.setText("Tổng doanh thu:");
        crazyPanel6.add(lbl_doanhthu);

        jbl_tdoanhthu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jbl_tdoanhthu.setText("jLabel6");
        crazyPanel6.add(jbl_tdoanhthu);

        crazyPanel7.add(crazyPanel6);

        crazyPanel4.add(crazyPanel7);

        main.add(crazyPanel4);

        crazyPanel3.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "fill",
            "",
            "",
            null
        ));

        crazyPanel1.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "fill,wrap 1",
            "[]",
            "[][]",
            new String[]{
                "",
                ""
            }
        ));

        jLabel1.setText("Doanh thu theo ngày (7 ngày gần nhất)");
        crazyPanel1.add(jLabel1);

        bieudo.setPreferredSize(new java.awt.Dimension(500, 280));
        crazyPanel1.add(bieudo);

        crazyPanel3.add(crazyPanel1);

        crazyPanel2.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "fill,wrap 1",
            "[]",
            "[][]",
            new String[]{
                "",
                ""
            }
        ));

        jLabel2.setText("Doanh thu theo tháng (7 tháng gần nhất)");
        crazyPanel2.add(jLabel2);

        bieudothang.setPreferredSize(new java.awt.Dimension(500, 280));
        crazyPanel2.add(bieudothang);

        crazyPanel3.add(crazyPanel2);

        main.add(crazyPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.chart2.LineChart bieudo;
    private com.raven.chart2.Chart bieudothang;
    private javaswingdev.chart.PieChart chartslthue;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel10;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private raven.crazypanel.CrazyPanel crazyPanel4;
    private raven.crazypanel.CrazyPanel crazyPanel5;
    private raven.crazypanel.CrazyPanel crazyPanel6;
    private raven.crazypanel.CrazyPanel crazyPanel7;
    private raven.crazypanel.CrazyPanel crazyPanel8;
    private raven.crazypanel.CrazyPanel crazyPanel9;
    private javaswingdev.chart.PieChart doanhthu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel jbl_tdoanhthu;
    private javax.swing.JLabel jbl_tdoanhthu1;
    private javax.swing.JLabel jbl_tdoanhthu2;
    private javax.swing.JLabel lbl_doanhthu;
    private javax.swing.JLabel lbl_doanhthu1;
    private javax.swing.JLabel lbl_doanhthu2;
    private raven.crazypanel.CrazyPanel main;
    private javaswingdev.chart.PieChart pieChart;
    // End of variables declaration//GEN-END:variables
}
