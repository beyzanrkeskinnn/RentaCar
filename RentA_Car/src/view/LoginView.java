package view;
import business.UserManager;
import core.Helper;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginView extends Layout {
    private JPanel container;
    private JPanel w_top;
    private JLabel lbl_Welcome;
    private JLabel lbl_Welcome2;
    private JPanel w_bottom;
    private JLabel lbl_username;
    private JTextField fld_username;
    private JLabel lbl_password;
    private JPasswordField fld_password;
    private JButton btn_login;
    private final UserManager usermanager;



    public LoginView(){
        this.usermanager=new UserManager();
        this.add(container);
this.guiInitilaze(400,400);
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField[] checkFieldList={fld_username, fld_password};
                if(Helper.isFieldListEmpty(checkFieldList)){
                    Helper.showMsg("fill");
                }
                else{
                    User loginUser=usermanager.findByLogin(fld_username.getText(), fld_password.getText());
                    if(loginUser==null){
                        Helper.showMsg("notFound");
                    }
                    else{
                        AdminView adminView=new AdminView(loginUser);
                        dispose();
                    }
                }

            }
        });
    }
}

