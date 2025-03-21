package parcial;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
  private List<User> users = new ArrayList<User>();

  public UsuarioRepositorio(List<User> users) {
    this.users = users;
  }

  public User createUser(User user) {
    users.add(user);
    return user;
  }

  public Object modificateUser(User user, int id) {
    for (int i = 0; i < users.size(); i++) {
      if (users.get(i).getId() == id) {
        users.set(i, user);
        return user;
      }
    }
    return null;
  }

  public User getUserByEmail(String email) {
    return users.stream()
        .filter(user -> user.getEmail().equals(email))
        .findFirst()
        .orElse(null);
  }

  public List<User> getUsers() {
    return users;
  }

  public boolean isActiveUser(int id) {
    User userfind = users.stream()
      .filter(user -> user.getId() == id)
      .findFirst()
      .orElse(null);

    if(userfind != null) {
      if(userfind.getStatus() == true) {
        userfind.setStatus(false);
        return true;
      }else{
        userfind.setStatus(true);
        return true;
      }
    }else{
      return false;
    }
  }

}
