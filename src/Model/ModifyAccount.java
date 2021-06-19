package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ModifyAccount {
    public static int addAccount(Account ac, UserInfo ui){
        PreparedStatement statement=null;
        int n=0;
        try{
            Connection con=SQLConnect.getConnection();
            String sql2="Exec USP_addAcount @username=?, @password=?, @name=?, @phone=?,@address=?";
            statement=con.prepareCall(sql2);
            statement.setString(1,ac.getUsername());
            statement.setString(2,ac.getPassword());
            statement.setString(3,ui.getName());
            statement.setInt(4,ui.getPhonenumber());
            statement.setString(5,ui.getAddress());

            n=statement.executeUpdate();
        }catch (Exception e){
            //To do
        }
        return n;
    }
    public static UserInfo getUserInfoByUserName(String username){
        UserInfo ui=null;
        PreparedStatement statement=null;
        try{
            Connection connection=SQLConnect.getConnection();
            String sql="SELECT * FROM UserInfo WHERE Username=?";
            statement=connection.prepareCall(sql);
            statement.setString(1,username);
            ResultSet rs= statement.executeQuery();
            while (rs.next()){
                ui=new UserInfo(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getString(5));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ui;
    }
    public static boolean  updateInfo(UserInfo ui){
        PreparedStatement statement=null;
        try{
            Connection con=SQLConnect.getConnection();
            String sql2="UPDATE UserInfo SET name=?,phonenumber=?, address=? WHERE username=?";
            statement=con.prepareCall(sql2);
            statement.setString(1,ui.getName());
            statement.setInt(2,ui.getPhonenumber());
            statement.setString(3,ui.getAddress());
            statement.setString(4,ui.getUsername());
            ResultSet rs=statement.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch (Exception e){
            //To do
        }
        return false;
    }
    public static boolean updateAccount(Account ac){
        Connection con= SQLConnect.getConnection();
        String query="UPDATE Account SET password=? WHERE username=?";
        try{
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,ac.getPassword());
            ps.setString(2,ac.getUsername());
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return  true;
            }
        }catch (SQLException e){

        }

        return false;

    }
    public static boolean checkAccount(Account ac){
        Connection con= SQLConnect.getConnection();
        String query="SELECT * FROM Account WHERE username=? AND password=?";
        try{
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,ac.getUsername());
            ps.setString(2,ac.getPassword());
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return  true;
            }
        }catch (SQLException e){

        }

        return false;

    }
}
