package view;

import business.BrandManager;
import core.Helper;
import entity.Brand;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BrandView extends Layout {
    private JPanel container;
    private JLabel lbl_brand;
    private JLabel lbl_brand_name;
    private JTextField fld_brand_name;
    private JButton save;
    private Brand brand;
    private BrandManager brandManager;

    public BrandView(Brand brand) {
        this.brandManager=new BrandManager();
        this.brand = brand;
        this.add(container);
        this.guiInitilaze(300,300);
        if (brand != null) {
           fld_brand_name.setText(brand.getName());
        }
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_brand_name)) {
                    Helper.showMsg("fill");
                } else {
                    boolean result;
                    if (brand == null) {
                        Brand obj = new Brand(fld_brand_name.getText());
                        result = brandManager.save(obj);
                    } else {
                        brand.setName(fld_brand_name.getText());
                        result = brandManager.update(brand);
                    }

                        if (result) {
                            Helper.showMsg("done");
                            dispose();
                        } else {
                            Helper.showMsg("error");
                        }
                    }

            }
        });
    }

}

