package com.crm.streamline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crm.streamline.entities.Admin;
import com.crm.streamline.forms.AdminForm;
import com.crm.streamline.helpers.Message;
import com.crm.streamline.helpers.MessageType;
import com.crm.streamline.services.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class pageController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    
    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage()
    {
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage()
    {
        return "service";
    }

    @RequestMapping("/contact")
    public String contactPage()
    {
        return "contact";
    }

    // login page
    @RequestMapping("/login")
    public String loginPage()
    {
        return "login";
    }

    @RequestMapping("/register")
    public String registerPage(Model model)
    {
        AdminForm adminForm = new AdminForm();
        model.addAttribute("adminForm", adminForm);
        return "register";
    }

    // processing register
    @RequestMapping(value="/do-register", method=RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute AdminForm adminForm, BindingResult rBindingResult, HttpSession session) {
        

        // validate form data
        if(rBindingResult.hasErrors()) {
            return "register";
        }

        // save to database
        Admin admin = new Admin();
        admin.setName(adminForm.getName());
        admin.setEmail(adminForm.getEmail());
        admin.setPassword(passwordEncoder.encode(adminForm.getPassword()));
        admin.setPhoneNumber(adminForm.getPhoneNumber());
        admin.setCompanyName(adminForm.getCompanyName());
        admin.setAddress(adminForm.getAddress());
        admin.setCity(adminForm.getCity());
        admin.setState(adminForm.getState());


        Admin savedAdmin = adminService.saveAdmin(admin);
        System.out.println(savedAdmin);

        // message = "Registration successful"
        Message message = Message.builder().content("Registration Successful!!").type(MessageType.green).build();
        session.setAttribute("message", message);

        // redirect to page
        return "redirect:/register";
    }

    @PostMapping("/default")
    public String defaultAfterLogin() {
        // Redirect based on role
        System.out.println("this is default login handler ..................");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SALES_EMPLOYEE"))
                || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPPORT_EMPLOYEE"))) {
            return "redirect:/employee/dashboard";
        } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            return "redirect:/customer/dashboard";
        } else {
            throw new IllegalStateException();
        }
    }

}
