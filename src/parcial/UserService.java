package parcial;
public class UserService {
  /*
   * dicha clase debe contener una instancia de la clase UsuarioRepositorio como miembro de clase.
   */

  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean ValidateUser(String email, String password) {
    User user = userRepository.getUserByEmail(email);
    if(user != null) {
      if(user.getPassword().equals(password)) {
        return true;
      }
    }

  public void createUser(User user) {
    userRepository.createUser(user);
  }

  
    
    return false;
  }




  
}
