package com.crm.streamline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.crm.streamline.entities.Admin;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.forms.EmployeeForm;
import com.crm.streamline.forms.EmployeeSearchForm;
import com.crm.streamline.helpers.AppConstants;
import com.crm.streamline.helpers.Helper;
import com.crm.streamline.helpers.Message;
import com.crm.streamline.helpers.MessageType;
import com.crm.streamline.repositories.EmployeeRepository;
import com.crm.streamline.services.AdminService;
import com.crm.streamline.services.CustomerService;
import com.crm.streamline.services.EmployeeService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerService customerService;
    
    // dashboad page
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        long employeeCount = employeeService.countEmployees();
        model.addAttribute("employeeCount", employeeCount);
        long customerCount = customerService.countCustomers();
        model.addAttribute("customerCount", customerCount);
        return "admin/dashboard";
    }

    // view employees handler
    @RequestMapping("/employees")
    public String viewEmployees(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE+"") int size,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {

        // get user 
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Admin admin = adminService.getAdminByEmail(username);

        // get contacts of logged in user
        Page<Employee> pageEmployee = employeeService.getByAdmin(admin, page, size, sortBy, direction);

        model.addAttribute("pageEmployee", pageEmployee);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("employeeSearchForm", new EmployeeSearchForm());

        return "admin/employees";
    }


    // add employees view handler
    @RequestMapping("/employees/add")
    public String addEmployeeView(Model model) {

        EmployeeForm employeeForm = new EmployeeForm();
        model.addAttribute("employeeForm", employeeForm);

        return "admin/add_employees";
    }

    // add employees process handler
    @RequestMapping(value = "/employees/add", method = RequestMethod.POST)
    public String saveEmployee(@Valid @ModelAttribute EmployeeForm employeeForm, BindingResult result, Authentication authentication, HttpSession session) {

        // get the loggedin user
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Admin admin = adminService.getAdminByEmail(username);

        // validate form
        if (result.hasErrors()) {
            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "admin/add_employees";
        }

        Employee emp = employeeRepository.findByEmail(employeeForm.getEmail()).orElse(null);
        if(emp != null) {
            session.setAttribute("message", Message.builder()
                .content("Employee with this Email already exist.")
                .type(MessageType.red)
                .build());
            return "admin/add_employees";
        }

        Employee employee = new Employee();

        employee.setAdmin(admin);
        employee.setCity(employeeForm.getCity());
        employee.setEmail(employeeForm.getEmail());
        employee.setAddress(employeeForm.getAddress());
        employee.setPhoneNumber(employeeForm.getPhoneNumber());
        employee.setEmployeeType(employeeForm.getEmployeeType());
        employee.setFirstName(employeeForm.getFirstName());
        employee.setLastName(employeeForm.getLastName());
        employee.setPassword(passwordEncoder.encode(employeeForm.getPassword()));
        employee.setState(employeeForm.getState());

        // save contact
        employeeService.save(employee);

        // set message to be displayed on view 
        session.setAttribute("message", Message.builder()
                .content("You have successfully added a new employee.")
                .type(MessageType.green)
                .build());

        return "redirect:/admin/employees/add";
    }


    // search handler
    @RequestMapping("/employees/search")
    public String searchHandler(
        @ModelAttribute EmployeeSearchForm employeeSearchForm,
        @RequestParam(value="page", defaultValue="0") int page,
        @RequestParam(value="size", defaultValue=AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value="sortBy", defaultValue="firstName") String sortBy,
        @RequestParam(value="direction", defaultValue="asc") String direction,
        Model model, Authentication authentication) {

        var admin = adminService.getAdminByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Employee> pageEmployee = null;
        if(employeeSearchForm.getField().equalsIgnoreCase("firstName")) {
            pageEmployee = employeeService.searchByFirstName(employeeSearchForm.getValue(), page, size, sortBy, direction, admin);
        }
        else if(employeeSearchForm.getField().equalsIgnoreCase("email")) {
            pageEmployee = employeeService.searchByEmail(employeeSearchForm.getValue(), page, size, sortBy, direction, admin);
        }
        else if(employeeSearchForm.getField().equalsIgnoreCase("phone")) {
            pageEmployee = employeeService.searchByPhoneNumber(employeeSearchForm.getValue(), page, size, sortBy, direction, admin);
        }

        model.addAttribute("pageEmployee", pageEmployee);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("employeeSearchForm", employeeSearchForm);

        return "admin/search";
    }


    // delete employee
    @RequestMapping("/employees/delete/{employeeId}")
    public String deleteEmployee(@PathVariable("employeeId") int employeeId, 
    HttpSession session) {

        employeeService.delete(employeeId);

        session.setAttribute("message", 
        Message.builder()
        .content("Employee is Deleted successfully !!")
        .type(MessageType.green)
        .build());

        return "redirect:/admin/employees";
    }

    // update employee form view
    @GetMapping("/employees/view/{employeeId}")
    public String updateEmployeeFormView(
        @PathVariable("employeeId") int employeeId, 
        Model model) {

        var employee = employeeService.getById(employeeId);
        EmployeeForm employeeForm = new EmployeeForm();
        
        employeeForm.setFirstName(employee.getFirstName());
        employeeForm.setEmail(employee.getEmail());
        employeeForm.setPhoneNumber(employee.getPhoneNumber());
        employeeForm.setAddress(employee.getAddress());
        employeeForm.setCity(employee.getCity());
        employeeForm.setEmployeeType(employee.getEmployeeType());
        // employeeForm.setPassword(employee.getPassword());
        employeeForm.setLastName(employee.getLastName());
        employeeForm.setState(employee.getState());
        employeeForm.setPassword(employee.getPassword());
        
        model.addAttribute("employeeForm", employeeForm);
        model.addAttribute("employeeId", employeeId);
        
        return "admin/update_employee_view";
    }


    // update employee
    @RequestMapping(value="/employees/update/{employeeId}", method=RequestMethod.POST)
    public String updateEmployee(
        @PathVariable("employeeId") int employeeId, 
        @Valid @ModelAttribute EmployeeForm employeeForm,
        BindingResult bindingResult,
        HttpSession session) {

        if(bindingResult.hasErrors()) {
            session.setAttribute("message", Message.builder().content("Correct the following errors!!").type(MessageType.red).build());
            return "admin/update_employee_view";
        }

        employeeService.update(employeeForm, employeeId);

        session.setAttribute("message", Message.builder().content("Employee Updated successfully !!").type(MessageType.green).build());

        return "redirect:/admin/employees/view/" + employeeId;
    }
}
