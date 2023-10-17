package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping
    public String getAll(Model model){
        model.addAttribute("users", userService.getAll());
        return "/userlist";
    }
    @GetMapping("/{id}")
    public String showById(@PathVariable("id") int id, Model model){
        model.addAttribute("user", userService.getById(id));
        return "/show";
    }
    @GetMapping("/newuser")
    public String addUser(Model model){
        model.addAttribute("user", new User());
        return "/newuser";
    }
    @PostMapping()
    public String createUser(@ModelAttribute("user") User user){
        userService.save(user);
        return "redirect:/";
    }
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("user", userService.getById(id));
        return "/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") int id){
        userService.update(id, user);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        userService.delete(id);
        return "redirect:/";

    }


}
