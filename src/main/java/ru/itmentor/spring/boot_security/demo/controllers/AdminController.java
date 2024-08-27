package ru.itmentor.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.RoleService;
import ru.itmentor.spring.boot_security.demo.services.RoleServiceImpl;
import ru.itmentor.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/admin")
public class AdminController {
    private RoleService roleService;
    private UserService userService;
    @Autowired
    public AdminController(UserService userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/users")
    public String showUsers(ModelMap model, @ModelAttribute("newUser") User newUser, Principal principal){
        User user = userService.findByUsername(principal.getName());

        List<Role> adminRoles = new ArrayList<>(user.getRoles());
        model.addAttribute("admin", user);
        model.addAttribute("adminRoles", adminRoles);

        List<User> userList = userService.listUsers();
        model.addAttribute("users", userList);
        model.addAttribute("roles", roleService.roleList());
        return "users";
    }

    @PostMapping("/adduser")
    public String create(@ModelAttribute("user") User newUser) {
        userService.add(newUser);
        return "redirect:/admin/users";
    }
    @PostMapping("/edit")
    public String update(@ModelAttribute("editedUser") User editedUser) {
        userService.update(editedUser);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
