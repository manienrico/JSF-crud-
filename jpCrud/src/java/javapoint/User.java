package javapoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Enrico
 */
@ManagedBean
@RequestScoped
public class User {

    int id;
    String name;
    String email;
    String password;
    String gender;
    String address;
    ArrayList usersList;
    private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    Connection connection;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //Establish connection
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/User", "root", "");
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }

    //Fetch records
    public ArrayList usersList() {
        try {
            usersList = new ArrayList();
            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from users");
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGender(rs.getString("gender"));
                user.setAddress(rs.getString("address"));
                usersList.add(user);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return usersList;
    }

    //Save user record

    public String save() {
        int result = 0;
        try {
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "insert into users(name,email,password,gender,address) values(?,?,?,?,?)");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, gender);
            stmt.setString(5, address);
            result = stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (result != 0) {
            return "index.xhtml?faces-redirect=true";
        } else {
            return "create.xhtml?faces-redirect=true";
        }
    }

    //Update user

//    public String update(User u) {
////int result = 0;  
//        try {
//            connection = getConnection();
//            PreparedStatement stmt = connection.prepareStatement(
//                    "update users set name=?,email=?,password=?,gender=?,address=? where id=?");
//            stmt.setString(1, u.getName());
//            stmt.setString(2, u.getEmail());
//            stmt.setString(3, u.getPassword());
//            stmt.setString(4, u.getGender());
//            stmt.setString(5, u.getAddress());
//            stmt.setInt(6, u.getId());
//            stmt.executeUpdate();
//            connection.close();
//        } catch (Exception e) {
//            System.out.println(e);
//            e.printStackTrace();
//        }
//        return "/index.xhtml?faces-redirect=true";
//    }
    
    public String findStudentById(int id){
        try {
            usersList = new ArrayList();
            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from users where id ='"+id+"'");
            
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGender(rs.getString("gender"));
                user.setAddress(rs.getString("address"));
                
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                Map<String,Object> requestMap = externalContext.getRequestMap();
                requestMap.put("user", user);
            }
            
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return "updateUser.xhtml";
    }
    
    public String updateUser(User user){
        try{
            connection = getConnection();
            //Statement stmt = getConnection().createStatement();
            //String sql = "update users set name='"+user.getName()+"',email='"+user.getEmail()+"',gender='"+user.getGender()+"',address='"+user.getAddress()+"' where id='"+user.getPassword()+"'";
            //ResultSet rs = stmt.executeQuery("update users set name=?,email=?,gender=?,address=? where id =?");
            //int rowAffected = stmt.executeUpdate(sql);
            
            PreparedStatement pstmt = connection.prepareStatement("UPDATE users SET name=?, email=?, gender=?, address=? WHERE id=?");
            pstmt.setString(1,user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getGender());
            pstmt.setString(4, user.getAddress());
            pstmt.setInt(5, user.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        return "index.xhtml";
    }
// Used to delete user record  

    public void delete(int id) {
        try {
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("delete from users where id = " + id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
// Used to set user gender  

    public String getGenderName(char gender) {
        if (gender == 'M') {
            return "Male";
        } else {
            return "Female";
        }
    }

}
