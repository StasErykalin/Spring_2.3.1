package web.dao;

import org.springframework.stereotype.Component;
import web.model.User;

import java.util.List;
@Component
public interface UserDAO {

    List<User> getAll();
    User getById(int id);
    void save(User user);
    void update(User updatedUser);
    void delete(User user);


}
