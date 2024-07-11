package com.crm.streamline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
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
import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.entities.Project;
import com.crm.streamline.forms.ProjectForm;
import com.crm.streamline.forms.ProjectSearchForm;
import com.crm.streamline.helpers.AppConstants;
import com.crm.streamline.helpers.Helper;
import com.crm.streamline.helpers.Message;
import com.crm.streamline.helpers.MessageType;
import com.crm.streamline.services.AdminService;
import com.crm.streamline.services.CustomerService;
import com.crm.streamline.services.EmployeeService;
import com.crm.streamline.services.ProjectService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired 
    private ProjectService projectService;

    @Autowired
    private AdminService adminService;

    @Autowired EmployeeService employeeService;
    
    // dashboard customer
    @GetMapping("/dashboard")
    public String customerDashboard(Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        Customer customer = customerService.getCustomerByEmail(username);
        int customerId = customer.getId();
        long totalProjects = projectService.getNumberOfProjectsByCustomerId(customerId);
        long ongoing = projectService.getNumberOfProjectsByCustomerIdAndStatus(customerId, "In Progress");
        long completed = projectService.getNumberOfProjectsByCustomerIdAndStatus(customerId, "Completed");

        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("ongoing", ongoing);
        model.addAttribute("completed", completed);
        return "customer/dashboard";
    }

    // add project page
    @RequestMapping("/projects/add")
    public String addProjectView(Model model) {

        ProjectForm projectForm = new ProjectForm();
        projectForm.setStatus("Pending");
        model.addAttribute("projectForm", projectForm);

        return "customer/add_projects";
    }


    // process create project form
    @RequestMapping(value = "/projects/add", method = RequestMethod.POST)
    public String saveProject(@Valid @ModelAttribute ProjectForm projectForm, BindingResult result, Authentication authentication, HttpSession session) {

        // get the loggedin user
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Customer customer = customerService.getCustomerByEmail(username);

        Employee employee = customer.getEmployee();

        // validate form
        if (result.hasErrors()) {
            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "customer/add_projects";
        }

        Project project = new Project();

        project.setName(projectForm.getName());
        project.setAmountPaid(projectForm.getAmountPaid());
        project.setCustomer(customer);
        project.setDescription(projectForm.getDescription());
        project.setEmployee(employee);
        project.setStartDate(projectForm.getStartDate());
        project.setEndDate(projectForm.getEndDate());
        project.setStatus(projectForm.getStatus());

        // update sales of employee
        employee.setSales(employee.getSales() + projectForm.getAmountPaid());
        employeeService.save(employee);

        // update sales of admin
        Admin admin = employee.getAdmin();
        admin.setSales(admin.getSales() + projectForm.getAmountPaid());
        adminService.saveAdmin(admin);

        // save project
        projectService.save(project);

        // set message to be displayed on view 
        session.setAttribute("message", Message.builder()
                .content("You have successfully created a new project.")
                .type(MessageType.green)
                .build());

        return "redirect:/customer/projects/add";
    } 



    // view projects handler
    @RequestMapping("/projects")
    public String viewProjects(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE+"") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {

        // get user 
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Customer customer = customerService.getCustomerByEmail(username);

        // get contacts of logged in user
        Page<Project> pageProject = projectService.getByCustomer(customer, page, size, sortBy, direction);

        model.addAttribute("pageProject", pageProject);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("projectSearchForm", new ProjectSearchForm());

        return "customer/projects";
    }


    // search handler
    @RequestMapping("/projects/search")
    public String searchHandler(
        @ModelAttribute ProjectSearchForm projectSearchForm,
        @RequestParam(value="page", defaultValue="0") int page,
        @RequestParam(value="size", defaultValue=AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value="sortBy", defaultValue="name") String sortBy,
        @RequestParam(value="direction", defaultValue="asc") String direction,
        Model model, Authentication authentication) {

        var customer = customerService.getCustomerByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Project> pageProject = null;
        if(projectSearchForm.getField().equalsIgnoreCase("name")) {
            pageProject = projectService.searchByName(projectSearchForm.getValue(), page, size, sortBy, direction, customer);
        }
        else if(projectSearchForm.getField().equalsIgnoreCase("status")) {
            pageProject = projectService.searchByStatus(projectSearchForm.getValue(), page, size, sortBy, direction, customer);
        }
        

        model.addAttribute("pageProject", pageProject);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("projectSearchForm", projectSearchForm);

        return "customer/search";
    }


    // detailed project view
    @GetMapping("/projects/view/{projectId}")
    public String updateCustomerFormView(
        @PathVariable("projectId") int projectId, 
        Model model) {

        Project project = projectService.getById(projectId);
        String status = project.getStatus();
        Employee employee = project.getEmployee();
        
        model.addAttribute("project", project);
        model.addAttribute("status", status);
        model.addAttribute("employee", employee);
        
        return "customer/view_project.html";
    }


}
