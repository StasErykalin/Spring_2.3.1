package web.dao;

import org.springframework.stereotype.Component;
import web.model.User;

import java.util.List;
@Component
public interface UserDAO {

    List<User> getAll();
    User getById(int id);
    void save(User user);
    void update(int id, User updatedUser);
    void delete(int id);


}
