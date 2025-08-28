package service;

import interfaces.UserDao;
import dao.UserDAOImpl;
import model.User;
import security.PasswordUtil;

import java.util.Optional;

public class AuthService {

    private final UserDao userDAO;

    public AuthService() {
        this.userDAO = new UserDAOImpl();
    }

    public AuthService(UserDao userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<User> authenticate(String username, String passwordPlain) {
        Optional<User> opt = userDAO.findByUsername(username);
        if (opt.isPresent()) {
            User u = opt.get();
            if (PasswordUtil.verify(passwordPlain, u.getPasswordHash())) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }
}
