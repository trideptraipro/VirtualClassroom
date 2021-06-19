package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModifyRoom {
    public static int addRoom(Room room){
        PreparedStatement statement=null;
        int n = 0;
        try{
            Connection con=SQLConnect.getConnection();
            String sql2="INSERT INTO Room(roomname,CreatedBy) VALUES (?,?)";
            statement=con.prepareCall(sql2);
            statement.setString(1,room.getRoomname());
            statement.setString(2, room.getCreatedBy());
            n=statement.executeUpdate();
        }catch (Exception e){
            //To do
        }
        return n;
    }
    public static List<Room> getListRoomByUser(String user){
        List<Room> lr=new ArrayList<>();
        try {
            Connection connection=SQLConnect.getConnection();
            String sql="EXEC USP_GetRoom @username=?";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,user);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                lr.add(new Room(rs.getInt("id"),rs.getString("Roomname"),rs.getDate("CreatedDate"),rs.getString("CreatedBy")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return lr;
    }
    public static List<Room> getListRoomByUsernameAdmin(String user){
        List<Room> ls= new ArrayList<>();
        PreparedStatement statement=null;
        try{
            Connection con=SQLConnect.getConnection();
            String sql="SELECT * FROM room WHERE CreatedBy=?";
            statement=con.prepareCall(sql);
            statement.setString(1,user);
            ResultSet rs=statement.executeQuery();
            while (rs.next()){
                Room r=new Room(rs.getInt("id"),rs.getString("Roomname"),rs.getDate("CreatedDate"),rs.getString("CreatedBy"));
                ls.add(r);
            }
        }catch (Exception e){
            //To do
        }
        return ls;
    }
    public static int AddMembertoRoombyID(int id, Room room){
        int n=0;
        PreparedStatement statement=null;
        try{
            Connection con=SQLConnect.getConnection();
            String sql="INSERT INTO List(iduser, idroom) VALUES (?, ?)";
            statement=con.prepareCall(sql);
            statement.setInt(1,id);
            statement.setInt(2,room.getId());
            n=statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return n;
    }
    public static int AddMembertoRoombyUsername(String username, Room room){
        int n=0;
        PreparedStatement statement=null;
        try{
            Connection con=SQLConnect.getConnection();
            String sql="EXEC USP_Addmembertoroom @username=?, @idroom=?";
            statement=con.prepareCall(sql);
            statement.setString(1,username);
            statement.setInt(2,room.getId());
            n=statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return n;
    }
}
