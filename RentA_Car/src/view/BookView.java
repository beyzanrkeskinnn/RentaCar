package view;

import business.BookManager;
import core.Helper;
import entity.Book;
import entity.Car;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookView extends Layout{
    private JPanel container;
    private JTextField fld_booking_name;
    private JTextField fld_bookin_idno;
    private JTextField fld_booking_phone;
    private JTextField fld_booking_mail;
    private JTextField fld_booking_start_date;
    private JTextField fld_booking_total;
    private JTextField fld_booking_end_date;
    private JTextPane txt_booking_note;
    private JButton btn_booking_save;
    private JLabel lbl_car_info;
    private Car car;
    private BookManager bookManager;

    public BookView() {

    }



    public  BookView(Car selectedCar, String strt_date, String fnsh_date) {
        this.car = selectedCar;
        this.bookManager = new BookManager();

        this.add(container);
        guiInitilaze(300, 600);

        lbl_car_info.setText("Araç : " +
                this.car.getPlate() + " / " +
                this.car.getModel().getBrand().getName() + " / " +
                this.car.getModel().getName());

        this.fld_booking_start_date.setText(strt_date);
        this.fld_booking_end_date.setText(fnsh_date);

        // test için
        this.fld_booking_name.setText("Mustafa Çetindağ");
        this.fld_bookin_idno.setText("1234123411");
        this.fld_booking_mail.setText("test@patika.dev");
        this.fld_booking_phone.setText("05501234567");
        this.fld_booking_total.setText("2350");
        this.txt_booking_note.setText("Not bırakıldı");

        btn_booking_save.addActionListener(e -> {
            JTextField[] checkFieldList = {
                    this.fld_booking_name,
                    this.fld_booking_mail,
                    this.fld_booking_total,
                    this.fld_booking_phone,
                    this.fld_bookin_idno,
                    this.fld_booking_start_date,
                    this.fld_booking_end_date
            };

            if (Helper.isFieldListEmpty(checkFieldList)) {
                Helper.showMsg("fill");
            } else {
                Book book = new Book();
                book.setbCase("done");
                book.setCar_id(this.car.getId());
                book.setName(this.fld_booking_name.getText());
                book.setStrt_date(LocalDate.parse(strt_date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                book.setFnsh_date(LocalDate.parse(fnsh_date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                book.setIdno(this.fld_bookin_idno.getText());
                book.setMpno(this.fld_booking_phone.getText());
                book.setMail(this.fld_booking_mail.getText());
                book.setNote(this.txt_booking_note.getText());
                book.setPrc(Integer.parseInt(this.fld_booking_total.getText()));

                if (this.bookManager.save(book)) {
                    Helper.showMsg("done");
                    dispose();
                } else {
                    Helper.showMsg("error");
                }

            }
        });
    }
}

