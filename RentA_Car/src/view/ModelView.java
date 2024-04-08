package view;

import business.BrandManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.Brand;
import entity.Model;

import javax.swing.*;

public class ModelView extends Layout {
    private JPanel container;
    private JLabel label_heading;
 private JComboBox<ComboItem> combo_model_brand;
    private JLabel label_brand;
    private JTextField field_model_name;
    private JLabel label_model_year;
    private JLabel label_model_name;
    private JTextField field_model_year;
    private JLabel label_model_type;
    private JComboBox<Model.Type> combo_model_type;
    private JLabel label_model_fuel;
    private JComboBox<Model.Fuel> combo_model_fuel;
    private JLabel label_model_gear;
    private JComboBox<Model.Gear> combo_model_gear;
    private JButton button_model_save;
    private final Model model;
    private ModelManager modelManager;
    private BrandManager brandManager;
    private JLabel lbl_heading;
    private JComboBox cmb_brand;
    private JTextField fld_model_name;
    private JTextField fld_model_year;
    private JComboBox cmb_model_type;
    private JComboBox cmb_model_fuel;
    private JComboBox cmb_model_gear;
    private JButton btn_model_save;

    public ModelView(Model model) {
        this.add(container);
        this.model = model;
        this.modelManager = new ModelManager();
        this.brandManager = new BrandManager();
        this.guiInitilaze(350, 450);

        for (Brand brand : this.brandManager.findAll()) {
            this.cmb_brand.addItem(new ComboItem(brand.getId(), brand.getName()));
        }
        this.cmb_model_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_model_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_model_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));

        if (this.model.getId() != 0) {
            this.fld_model_year.setText(this.model.getYear());
            this.fld_model_name.setText(this.model.getName());
            this.cmb_model_fuel.getModel().setSelectedItem(this.model.getFuel());
            this.cmb_model_gear.getModel().setSelectedItem(this.model.getGear());
            this.cmb_model_type.getModel().setSelectedItem(this.model.getType());
           ComboItem defaultBrand = new ComboItem(this.model.getBrand().getId(), this.model.getBrand().getName());
           this.cmb_brand.getModel().setSelectedItem(defaultBrand);
     }
            this.btn_model_save.addActionListener(e -> {
                if (Helper.isFieldListEmpty(new JTextField[]{this.fld_model_name, this.fld_model_year})) {
                    Helper.showMsg("fill");
                } else {
                    boolean result;
                ComboItem selectedItem = (ComboItem) cmb_brand.getSelectedItem();
                this.model.setYear(fld_model_year.getText());
                    this.model.setName(fld_model_name.getText());
                assert selectedItem != null;
                this.model.setBrand_id(selectedItem.getKey());
                    this.model.setType((Model.Type) cmb_model_type.getSelectedItem());
                    this.model.setFuel((Model.Fuel) cmb_model_fuel.getSelectedItem());
                    this.model.setGear((Model.Gear) cmb_model_gear.getSelectedItem());

                    if (this.model.getId() != 0) {
                        result = this.modelManager.update(this.model);
                        if (result) {
                            Helper.showMsg("Update Successful!");

                            dispose();
                        } else {
                            Helper.showMsg("Error occurred during update.");
                        }
                    } else {
                        result = this.modelManager.save(this.model);
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


