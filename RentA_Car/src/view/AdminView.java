package view;

import business.BookManager;
import business.BrandManager;
import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;

public class AdminView extends Layout {
    private JPanel container;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JTabbedPane tabmenu;
    private JPanel pnl_brand;
    private JScrollPane scrl_brand;
    private JTable tbl_brand;
    private JPanel pnl_model;
    private JTable tbl_model;
    private JScrollPane scrl_model;
    private JComboBox cmb_s_model_brand;
    private JComboBox cbm_s_model_type;
    private JComboBox cmb_s_model_gear;
    private JComboBox cmb_s_model_fuel;
    private JButton btn_searc_model;
    private JButton btn_clear_model;
    private JPanel pnl_car;
    private JTable tbl_car;
    private JTextField fld_book_start_date;
    private JTextField fld_book_end_date;
    private JComboBox cmb_book_gear;
    private JComboBox cmb_book_fuel;
    private JComboBox cmb_book_type;
    private JComboBox<ComboItem> cmb_book_car;
    private JButton btn_book_search;
    private JTable tbl_book;
    private JButton btn_clear_book;
    private JTable tbl_booking;
    private JButton btn_search;
    private JButton btn_clear_booking_car;
    private User user;
    private DefaultTableModel tbl_brand_model = new DefaultTableModel();
    private DefaultTableModel tbl_model_model = new DefaultTableModel();
    private DefaultTableModel tbl_car_model = new DefaultTableModel();
    private DefaultTableModel tbl_book_model = new DefaultTableModel();
    private DefaultTableModel tbl_booking_model = new DefaultTableModel();

    private BrandManager brandManager;
    private JPopupMenu brand_menu;
    private JPopupMenu model_menu;
    private JPopupMenu car_menu;
    private JPopupMenu book_menu;
    private JPopupMenu booking_menu;
    private ModelManager modelManager;
    private CarManager carManager;
    private BookManager bookManager;
    private Object[] col_model;
    private Object[] col_car;
    private Object[] col_book;



    public AdminView(User user) {
        this.brandManager = new BrandManager();
        this.modelManager = new ModelManager();
        this.carManager = new CarManager();
        this.bookManager = new BookManager();
        this.add(container);
        this.guiInitilaze(1000, 700);
        this.user = user;
        if (user == null) {
            dispose();
        }
        this.lbl_welcome.setText("Hoşgeldiniz ," + this.user.getUsername());
        loadComponent();
        loadBrandTable();
        loadBrandComponent();

        loadModelTable(null);
        loadModelComponent();
        loadModelFilter();

        loadCarTable(null);
        loadCarComponent();

        loadBookingTable(null);
        loadBookingComponent();
        loadBookingFilter();

        loadBookTable(null);
        loadBookComponent();
        loadBookFilterCar();
        loadBookingFilter();


    }

    private void loadComponent() {
        this.btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginView loginView = new LoginView();
            }
        });
    }

    private void loadModelComponent() {
        tableRowSelect(tbl_model);
        model_menu = new JPopupMenu();
        model_menu.add("Yeni").addActionListener(e -> {
            ModelView modelView = new ModelView(new Model());
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                }
            });
        });
        model_menu.add("Güncelle").addActionListener(e -> {
            int selectModelId = this.getTableSelectedRow(tbl_model, 0);
            ModelView modelView = new ModelView(this.modelManager.getById(selectModelId));
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                }
            });
        });
        model_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectModelId = this.getTableSelectedRow(tbl_model, 0);
                if (this.modelManager.delete(selectModelId)) {
                    Helper.showMsg("Succeed !");
                    loadModelTable(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        this.tbl_model.setComponentPopupMenu(model_menu);
        this.btn_searc_model.addActionListener(e -> {
            ComboItem selectedBrand = (ComboItem) this.cmb_s_model_brand.getSelectedItem();
            int brandId = 0;
            if (selectedBrand != null) {
                brandId = selectedBrand.getKey();
            }
            ArrayList<Model> modelListBySearch = this.modelManager.searchForTable(
                    brandId,
                    (Model.Fuel) cmb_s_model_fuel.getSelectedItem(),
                    (Model.Type) cbm_s_model_type.getSelectedItem(),
                    (Model.Gear) cmb_s_model_gear.getSelectedItem()
            );


            ArrayList<Object[]> modelRowListBySearch = this.modelManager.getForTable(this.col_model.length, modelListBySearch);
            loadModelTable(modelRowListBySearch);
        });


        this.btn_clear_model.addActionListener(e -> {
            this.cbm_s_model_type.setSelectedItem(null);
            this.cmb_s_model_gear.setSelectedItem(null);
            this.cmb_s_model_fuel.setSelectedItem(null);
            this.cmb_s_model_brand.setSelectedItem(null);
            loadModelTable(null);
        });
    }

    public void loadBrandComponent() {
        tableRowSelect(tbl_brand);
        tbl_brand.setEnabled(false);
        this.brand_menu = new JPopupMenu();
        brand_menu.add("Yeni").addActionListener(e -> {
            BrandView brandView = new BrandView(null);
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();

                }
            });
        });
        brand_menu.add("Güncelle").addActionListener(e -> {
            int selectBrandId = getTableSelectedRow(tbl_brand, 0);
            BrandView brandView = new BrandView(this.brandManager.getById(selectBrandId));
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadCarTable(null);
                    loadBookTable(null);

                }
            });
        });
        brand_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectModelId = this.getTableSelectedRow(tbl_brand, 0);
                if (this.brandManager.delete(selectModelId)) {
                    Helper.showMsg("done");
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadCarTable(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        this.tbl_brand.setComponentPopupMenu(brand_menu);

    }

    public void loadBrandTable() {
        Object[] col_brand = {"Marka ID", "Marka Adı"};
        ArrayList<Object[]> brandList = this.brandManager.getForTable(col_brand.length);
        this.createTable(this.tbl_brand_model, this.tbl_brand, col_brand, brandList);

    }

    public void loadModelTable(ArrayList<Object[]> modelList) {
        this.col_model = new Object[]{"Model ID", "Brand", "Name", "Type", "Year", "Fuel", "Gear"};

        if (modelList == null) {
            modelList = this.modelManager.getForTable(col_model.length, this.modelManager.findAll());
        }
        createTable(tbl_model_model, this.tbl_model, col_model, modelList);

    }

    public void loadModelFilter() {
        this.cbm_s_model_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cbm_s_model_type.setSelectedItem(null);
        this.cmb_s_model_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_s_model_gear.setSelectedItem(null);
        this.cmb_s_model_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_s_model_fuel.setSelectedItem(null);
        loadModelFilterBrand();

    }

    public void loadModelFilterBrand() {
        this.cmb_s_model_brand.removeAllItems();
        for (Brand obj : brandManager.findAll()) {
            this.cmb_s_model_brand.addItem(new ComboItem(obj.getId(), obj.getName()));
        }
        this.cmb_s_model_brand.setSelectedItem(null);
    }


    private void loadCarComponent() {
        tableRowSelect(tbl_car);
        car_menu = new JPopupMenu();
        car_menu.add("Yeni").addActionListener(e -> {
            CarView carView = new CarView(new Car());
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable(null);
                }
            });
        });
        car_menu.add("Güncelle").addActionListener(e -> {
            int selectCarId = this.getTableSelectedRow(tbl_car, 0);
            CarView carView = new CarView(this.carManager.getById(selectCarId));
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable(null);
                }
            });
        });
        car_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectCarId = this.getTableSelectedRow(tbl_car, 0);
                if (this.modelManager.delete(selectCarId)) {
                    Helper.showMsg("Succeed !");
                    loadCarTable(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        this.tbl_car.setComponentPopupMenu(car_menu);

    }

    public void loadCarTable(ArrayList<Object[]> carList) {
        col_car = new Object[]{"ID", "Marka", "Model", "Plaka", "Renk", "KM", "Yıl", "Tip", "Yakıt Türü", "Vites"};
        carList = this.carManager.getForTable(col_car.length, this.carManager.findAll());
        createTable(this.tbl_car_model, this.tbl_car, col_car, carList);
    }

    private void loadBookComponent() {
        tableRowSelect(this.tbl_book);
        this.book_menu = new JPopupMenu();
        this.book_menu.add("İptal Et").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectBookId = this.getTableSelectedRow(this.tbl_book, 0);
                if (this.bookManager.delete(selectBookId)) {
                    Helper.showMsg("done");
                    loadBookTable(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        this.book_menu.add("Rezervasyon Yap").addActionListener(e -> {
            int selectCarId = this.getTableSelectedRow(this.tbl_booking, 0);
            BookView bookingView = new BookView(
                    this.carManager.getById(selectCarId),
                    this.fld_book_start_date.getText(),
                    this.fld_book_end_date.getText()
            );

            bookingView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBookingTable(null);
                    loadBookingFilter();
                    loadBookTable(null);
                }
            });
        });
        this.tbl_book.setComponentPopupMenu(book_menu);

        btn_book_search.addActionListener(e -> {
            ComboItem selectedCar = (ComboItem) this.cmb_book_car.getSelectedItem();
            int carId = 0;
            if (selectedCar != null) {
                carId = selectedCar.getKey();
            }

            ArrayList<Book> bookListBySearch = this.bookManager.searchForTable(carId);
            ArrayList<Object[]> bookRowListBySearch = this.bookManager.getForTable(this.col_book.length, bookListBySearch);
            loadBookTable(bookRowListBySearch);
        });

        this.btn_book_search.addActionListener(e -> {
            loadBookFilterCar();
        });
    }

    private void loadBookTable(ArrayList<Object[]> bookList) {
        col_book = new Object[]{"ID", "Plaka", "Araç Marka", "Araç Model", "Müşteri", "Telefon", "Mail", "T.C.", "Başlangıç Tarihi", "Bitiş Tarihi", "Fiyat"};
        if (bookList == null) {
            bookList = this.bookManager.getForTable(col_book.length, this.bookManager.findAll());
        }
        createTable(this.tbl_book_model, this.tbl_book, col_book, bookList);
    }

    public void loadBookFilterCar() {
        this.cmb_book_car.removeAllItems();
        for (Car obj : this.carManager.findAll()) {
            this.cmb_book_car.addItem(new ComboItem(obj.getId(), obj.getPlate()));
        }
        this.cmb_book_car.setSelectedItem(null);
    }

    public void loadBookingFilter() {
        this.cmb_book_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_book_type.setSelectedItem(null);
        this.cmb_book_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_book_gear.setSelectedItem(null);
        this.cmb_book_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_book_fuel.setSelectedItem(null);
    }
    private void loadBookingComponent() {
        tableRowSelect(this.tbl_booking);
        this.booking_menu = new JPopupMenu();
        this.booking_menu.add("Rezervasyon Yap").addActionListener(e -> {
                    int selectCarId = this.getTableSelectedRow(this.tbl_booking, 0);
                    BookView bookingView = new BookView(
                            this.carManager.getById(selectCarId),
                            this.fld_book_start_date.getText(),
                            this.fld_book_end_date.getText()
                    );

            bookingView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBookingTable(null);
                    loadBookingFilter();
                    loadBookTable(null);
                }
            });
        });
        this.tbl_booking.setComponentPopupMenu(booking_menu);

        btn_search.addActionListener(e -> {
            ArrayList<Car> carList = this.carManager.searchForBooking(
                    fld_book_start_date.getText(),
                    fld_book_end_date.getText(),
                    (Model.Type) cmb_book_type.getSelectedItem(),
                    (Model.Gear) cmb_book_gear.getSelectedItem(),
                    (Model.Fuel) cmb_book_fuel.getSelectedItem()
            );

            ArrayList<Object[]> carBookingRow = this.carManager.getForTable(this.col_car.length, carList);
            loadBookingTable(carBookingRow);
        });
        btn_clear_booking_car.addActionListener(e -> {
            loadBookingFilter();
        });
    }
    private void loadBookingTable(ArrayList<Object[]> carList) {
        Object[] col_booking_list = {"ID", "Marka", "Model", "Plaka", "Renk", "KM", "Yıl", "Tip", "Yakıt Türü", "Vites"};
        createTable(this.tbl_booking_model, this.tbl_booking, col_booking_list, carList);
    }

    private void createUIComponents() throws ParseException {


        this.fld_book_start_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_book_start_date.setText("10/10/2023");
        this.fld_book_end_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_book_end_date.setText("16/10/2023");
    }


}

