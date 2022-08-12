package ru.kata.spring.boot_security.demo.controller;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {

    final PasswordEncoder passwordEncoder;
    final UserService userService;
    final RoleService roleService;

    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/allUsers")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "allUsers";
    }

    @GetMapping("/new")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }

    @PostMapping("/saveuser")
    public String addUser(@ModelAttribute("user") User user) {
        user.getRoles().stream().filter(x -> roleService.findByRoleName(x.getName()) == null).forEach(x -> roleService.add(x));
        user.setRoles(user.getRoles().stream().map(x -> roleService.findByRoleName(x.getName())).collect(Collectors.toList()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin/allUsers";
    }

    @GetMapping("/change/{id}")
    public String changeUser(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.get(id));
        model.addAttribute("roles", userService.get(id).getRoles());
        return "changeUser";
    }

    @PatchMapping ("/update/{id}")
    public String saveChanges(@ModelAttribute("user") User user) {
        user.getRoles().stream().filter(x -> roleService.findByRoleName(x.getName()) == null).forEach(x -> roleService.add(x));
        user.setRoles(user.getRoles().stream().map(x -> roleService.findByRoleName(x.getName())).collect(Collectors.toList()));

        if (!user.getPassword().equals(userService.get(user.getId()).getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.update(user);
        return "redirect:/admin/allUsers";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin/allUsers";
    }

}
