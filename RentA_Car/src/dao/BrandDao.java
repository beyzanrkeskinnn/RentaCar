package dao;

import core.Db;
import entity.Brand;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BrandDao {
    private final Connection con;

    public BrandDao() {
        this.con = Db.getInstance();
    }

    public ArrayList<Brand> findAll(){
        ArrayList<Brand> userList= new ArrayList<>();
        String sql ="SELECT * FROM public.brand ORDER BY brand_id ASC";
        try{
            ResultSet rs=con.createStatement().executeQuery(sql);
            while(rs.next()){
                userList.add(this.match(rs));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userList;
    }

    public boolean save(Brand brand) {
        String query = "INSERT INTO public.brand (brand_name) VALUES (?)";
        try {
            PreparedStatement pr = this.con.prepareStatement(query);
            pr.setString(1, brand.getName());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public Brand match(ResultSet rs) throws  SQLException{
        Brand obj=new Brand();
        obj.setId(rs.getInt("brand_id"));
        obj.setName(rs.getString("brand_name"));

        return  obj;
    }
    public Brand getById(int id) {
        Brand object = null;
        String query = "SELECT * FROM public.brand WHERE brand_id = ? ";
        try {
            PreparedStatement prepared = this.con.prepareStatement(query);
            prepared.setInt(1, id);
            ResultSet resultSet = prepared.executeQuery();

            if (resultSet.next()) {
                object = this.match(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return object;
    }
    public boolean update(Brand brand) {
        String query = "UPDATE public.brand SET brand_name = ? WHERE brand_id = ?";

        try {
            PreparedStatement prepared = this.con.prepareStatement(query);
            prepared.setString(1, brand.getName());
            prepared.setInt(2, brand.getId());
            return prepared.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean delete(int id) {
        String query = "DELETE FROM public.brand WHERE brand_id = ?";
        try {
            PreparedStatement prepared = this.con.prepareStatement(query);
            prepared.setInt(1, id);
            return prepared.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
