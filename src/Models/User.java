package Models;

import Controllers.User_Controllers;

public class User extends User_Controllers {
   private String username;
   private String password;
   User(String username, String password) {
       this.username = username;
        this.password = password;
   }
   public String getUsername() {
       return username;
   }
   public String getPassword() {
       return password;
   }
   public boolean checkUser(String username, String password) {
       return this.username.equals(username) && this.password.equals(password);
   }
   public boolean changePassword(String username, String OldPassword, String NewPassword) {
       if (checkUser(username, OldPassword)) {
           this.username = username;
           this.password = NewPassword;
           return true;
       };
       return false;
   }
}
