package raven.drawer;

import com.QLTV.entity.HoaDon;
import com.QLTV.form.BAN_SACH;
import com.QLTV.form.DON_THUE;
import com.QLTV.form.QLDG;
import com.QLTV.form.QLHD;
import com.QLTV.form.QLS;
import com.QLTV.form.QLTG;
import com.QLTV.form.QLTK;
import com.QLTV.form.QLTL;
import com.QLTV.form.QLVT;
import com.QLTV.form.THUE_SACH;
import com.QLTV.form.TTTK;
import com.QLTV.form.ThongKe;
import com.QLTV.utils.XAuth;
import java.awt.Component;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import raven.drawer.component.SimpleDrawerBuilder;
import raven.drawer.component.footer.SimpleFooterData;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.menu.MenuAction;
import raven.drawer.component.menu.MenuEvent;
import raven.drawer.component.menu.MenuValidation;
import raven.drawer.component.menu.SimpleMenuOption;
import com.QLTV.main.Main;
import raven.swing.AvatarIcon;
import raven.tabbed.ThemesChange;
import raven.tabbed.WindowsTabbed;

/**
 *
 * @author RAVEN
 */
public class MyDrawerBuilder extends SimpleDrawerBuilder {

    private ThemesChange themesChange = null;

    public MyDrawerBuilder() {
        themesChange = new ThemesChange();
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return new SimpleHeaderData()
                .setIcon(new AvatarIcon(getClass().getResource("/raven/image/logo.png"), 60, 60, 999))
                .setTitle(XAuth.user.getHoten())
                .setDescription(XAuth.isManager() ? "Quản lý" : "Nhân viên");
    }

    @Override
    public SimpleMenuOption getSimpleMenuOption() {
        String NV[][] = {
            {"~Chức năng chính~"},
            {"Bán sách"},
            {"Hoá đơn bán sách"},
            {"Thuê sách"},
            {"Đơn thuê sách"},
            {"~Quản lý~"},
            {"Quản lý", "Tài khoản", "Độc giả", "Sách", "Tác giả", "Thể loại", "Vị trí"},
            {"~Khác~"},
            {"Thông tin tài khoản"},
            {"Đăng xuất"}};
        String QL[][] = {
            {"~Chức năng chính~"},
            {"Bán sách"},
            {"Hoá đơn bán sách"},
            {"Thuê sách"},
            {"Đơn thuê sách"},
            {"~Quản lý~"},
            {"Quản lý", "Tài khoản", "Độc giả", "Sách", "Tác giả", "Thể loại", "Vị trí"},
            {"Thống kê"},
            {"~Khác~"},
            {"Thông tin tài khoản"},
            {"Đăng xuất"}
        };
        String menus[][] = {};
        if (XAuth.user.getVaitro() == true) {
            menus = QL;
        } else {
            menus = NV;
        }

        String iconQL[] = {
            "dashboard.svg",
            "page.svg",
            "icon.svg",
            "calendar.svg",
            "forms.svg",
            "chart.svg",
            "ui.svg",
            "logout.svg"};
        String iconNV[] = {
            "dashboard.svg",
            "page.svg",
            "icon.svg",
            "calendar.svg",
            "forms.svg",
            "ui.svg",
            "logout.svg"};
        String icons[] = {};
        if (XAuth.user.getVaitro() == true) {
            icons = iconQL;
        } else {
            icons = iconNV;
        }
        return new SimpleMenuOption()
                .setMenus(menus)
                .setIcons(icons)
                .setBaseIconPath("raven/drawer/icon")
                .setIconScale(0.45f)
                .addMenuEvent(new MenuEvent() {
                    @Override
                    public void selected(MenuAction action, int index, int subIndex) {
                        if (XAuth.user.getVaitro() == true) {
                            if (index == 0) {
                                WindowsTabbed.getInstance().addTab("Bán sách", new BAN_SACH());
                            }
                            if (index == 1) {
                                WindowsTabbed.getInstance().addTab("Hoá đơn bán sách", new QLHD());
                            }
                            if (index == 2) {
                                WindowsTabbed.getInstance().addTab("Thuê sách", new THUE_SACH());
                            }
                            if (index == 3) {
                                WindowsTabbed.getInstance().addTab("Đơn thuê", new DON_THUE());
                            }
                            if (index == 4) {
                                switch (subIndex) {
                                    case 1:
                                        WindowsTabbed.getInstance().addTab("Tài khoản", new QLTK());
                                        break;
                                    case 2:
                                        WindowsTabbed.getInstance().addTab("Độc giả", new QLDG());
                                        break;
                                    case 3:
                                        WindowsTabbed.getInstance().addTab("Sách", new QLS());
                                        break;
                                    case 4:
                                        WindowsTabbed.getInstance().addTab("Tác giả", new QLTG());
                                        break;
                                    case 5:
                                        WindowsTabbed.getInstance().addTab("Thể loại", new QLTL());
                                        break;
                                    case 6:
                                        WindowsTabbed.getInstance().addTab("Vị trí", new QLVT());
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (index == 5 && XAuth.user.getVaitro() == true) {
                                WindowsTabbed.getInstance().addTab("Thống kê", new ThongKe());
                            }
                            if (index == 6) {
                                try {
                                    WindowsTabbed.getInstance().addTab("Thông tin tài khoản", new TTTK());
                                } catch (ParseException ex) {
                                    Logger.getLogger(MyDrawerBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else if (index == 7) {
                                Main.main.logout();
                            }
                        } else {
                            if (index == 0) {
                                WindowsTabbed.getInstance().addTab("Bán sách", new BAN_SACH());
                            }
                            if (index == 1) {
                                WindowsTabbed.getInstance().addTab("Hoá đơn bán sách", new QLHD());
                            }
                            if (index == 2) {
                                WindowsTabbed.getInstance().addTab("Thuê sách", new THUE_SACH());
                            }
                            if (index == 3) {
                                WindowsTabbed.getInstance().addTab("Đơn thuê", new DON_THUE());
                            }
                            if (index == 4) {
                                switch (subIndex) {
                                    case 1:
                                        WindowsTabbed.getInstance().addTab("Tài khoản", new QLTK());
                                        break;
                                    case 2:
                                        WindowsTabbed.getInstance().addTab("Độc giả", new QLDG());
                                        break;
                                    case 3:
                                        WindowsTabbed.getInstance().addTab("Sách", new QLS());
                                        break;
                                    case 4:
                                        WindowsTabbed.getInstance().addTab("Tác giả", new QLTG());
                                        break;
                                    case 5:
                                        WindowsTabbed.getInstance().addTab("Thể loại", new QLTL());
                                        break;
                                    case 6:
                                        WindowsTabbed.getInstance().addTab("Vị trí", new QLVT());
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (index == 5) {
                                try {
                                    WindowsTabbed.getInstance().addTab("Thông tin tài khoản", new TTTK());
                                } catch (ParseException ex) {
                                    Logger.getLogger(MyDrawerBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else if (index == 6) {
                                Main.main.logout();
                            }
                        }
                    }
                })
                .setMenuValidation(new MenuValidation() {
                    @Override
                    public boolean menuValidation(int index, int subIndex) {
//                        if(index==0){
//                            return false;
//                        }else if(index==3){
//                            return false;
//                        }
                        return true;
                    }

                });
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData()
                .setTitle("Quản lý thư viện")
                .setDescription("Version 1.1.0");
    }

    @Override
    public int getDrawerWidth() {
        return 275;
    }

    @Override
    public Component getFooter() {
        return themesChange;
    }

}
