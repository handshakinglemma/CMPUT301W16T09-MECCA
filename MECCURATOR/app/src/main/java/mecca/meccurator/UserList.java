package mecca.meccurator;


import java.util.ArrayList;

/**
 * Created by svetlanna on 16-02-27.
 */
public class UserList {

    public static ArrayList<User> users = new ArrayList<User>();

    private Integer size;


    public void addUser(User user) {

        if(users == null){

            UserList users = new UserList();

        }
        users.add(user);
    }

    public boolean hasUser(User user) {
        return users.contains(user);
    }


    public void updateUser() {

    }

    public void deleteUser(User user) {
        users.remove(user);

    }

    public ArrayList<User> getUsers(){
        return users;
    }

    public Integer getSize() {
        return size;
    }

}
