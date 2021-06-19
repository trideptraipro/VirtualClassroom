package Model;

import java.sql.*;

public class SQLConnect {
    public static void main(String[] args) {
        Connection con= SQLConnect.getInstance().getConnection();
        System.out.println(con);
    }
    private static SQLConnect instance;//Singleton
    private SQLConnect(){
    }
    public static SQLConnect getInstance(){
        if(instance ==null){
            instance =new SQLConnect();
        }
        return instance;
    }
    public static Connection getConnection(){
        Connection con=null;
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServer");
        }catch (ClassNotFoundException e){

        }
        String url="jdbc:sqlserver://localhost:54621;databaseName=VT;";//search cách tìm port sqlserver trên mạng
        String user="sa";
        String pass="sa";
        try {
            con= DriverManager.getConnection(url,user,pass);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return  con;
    }
    public static  void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    public boolean CheckUser(String user, String pass){
        Connection con= SQLConnect.getConnection();
        String query="SELECT * FROM users WHERE username=? AND password=?";
        try{
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,user);
            ps.setString(2,pass);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return  true;
            }
        }catch (SQLException e){

        }

        return false;

    }
}
