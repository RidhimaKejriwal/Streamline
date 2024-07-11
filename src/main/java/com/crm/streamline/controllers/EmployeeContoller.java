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

import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.entities.Project;
import com.crm.streamline.forms.CustomerForm;
import com.crm.streamline.forms.CustomerSearchForm;
import com.crm.streamline.forms.ProjectSearchForm;
import com.crm.streamline.forms.UpdateStatusForm;
import com.crm.streamline.helpers.AppConstants;
import com.crm.streamline.helpers.Helper;
import com.crm.streamline.helpers.Message;
import com.crm.streamline.helpers.MessageType;
import com.crm.streamline.repositories.CustomerRepository;
import com.crm.streamline.services.CustomerService;
import com.crm.streamline.services.EmployeeService;
import com.crm.streamline.services.ProjectService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/employee")
public class EmployeeContoller {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProjectService projectService;
    
    // dashboard page
    @GetMapping("/dashboard")
    public String employeeDashboard(Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        Employee employee = employeeService.getEmployeeByEmail(username);
        int employeeId = employee.getId();

        long customerCount = customerService.getNumberOfCustomersByEmployeeId(employeeId);
        long projectCount = projectService.getNumberOfProjectsByEmployeeId(employeeId);

        model.addAttribute("customerCount", customerCount);
        model.addAttribute("projectCount", projectCount);
        return "employee/dashboard";
    }

    // view customers handler
    @RequestMapping("/customers")
    public String viewCustomers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE+"") int size,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {

        // get user 
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Employee employee = employeeService.getEmployeeByEmail(username);

        // get contacts of logged in user
        Page<Customer> pageCustomer = customerService.getByEmployee(employee, page, size, sortBy, direction);

        model.addAttribute("pageCustomer", pageCustomer);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("customerSearchForm", new CustomerSearchForm());

        return "employee/customers";
    }


    // add customers view handler
    @RequestMapping("/customers/add")
    public String addCustomerView(Model model) {

        CustomerForm customerForm = new CustomerForm();
        model.addAttribute("customerForm", customerForm);

        return "employee/add_customers";
    }


    // add customers process handler
    @RequestMapping(value = "/customers/add", method = RequestMethod.POST)
    public String saveCustomer(@Valid @ModelAttribute CustomerForm customerForm, BindingResult result, Authentication authentication, HttpSession session) {

        // get the loggedin user
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Employee employee = employeeService.getEmployeeByEmail(username);

        // validate form
        if (result.hasErrors()) {
            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "employee/add_customers";
        }

        Customer customer1 = customerRepository.findByEmail(customerForm.getEmail()).orElse(null);
        if(customer1 != null) {
            session.setAttribute("message", Message.builder()
                .content("Customer with this Email already exist.")
                .type(MessageType.red)
                .build());
            return "employee/add_customers";
        }

        Customer customer = new Customer();

        customer.setEmployee(employee);
        customer.setCity(customerForm.getCity());
        customer.setEmail(customerForm.getEmail());
        customer.setAddress(customerForm.getAddress());
        customer.setPhoneNumber(customerForm.getPhoneNumber());
        customer.setCompanyName(customerForm.getCompanyName());
        customer.setFirstName(customerForm.getFirstName());
        customer.setLastName(customerForm.getLastName());
        customer.setPassword(passwordEncoder.encode(customerForm.getPassword()));
        customer.setState(customerForm.getState());

        // save contact
        customerService.save(customer);

        // set message to be displayed on view 
        session.setAttribute("message", Message.builder()
                .content("You have successfully added a new customer.")
                .type(MessageType.green)
                .build());

        return "redirect:/employee/customers/add";
    }


    // search handler
    @RequestMapping("/customers/search")
    public String searchHandler(
        @ModelAttribute CustomerSearchForm customerSearchForm,
        @RequestParam(value="page", defaultValue="0") int page,
        @RequestParam(value="size", defaultValue=AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value="sortBy", defaultValue="firstName") String sortBy,
        @RequestParam(value="direction", defaultValue="asc") String direction,
        Model model, Authentication authentication) {

        var employee = employeeService.getEmployeeByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Customer> pageCustomer = null;
        if(customerSearchForm.getField().equalsIgnoreCase("firstName")) {
            pageCustomer = customerService.searchByFirstName(customerSearchForm.getValue(), page, size, sortBy, direction, employee);
        }
        else if(customerSearchForm.getField().equalsIgnoreCase("email")) {
            pageCustomer = customerService.searchByEmail(customerSearchForm.getValue(), page, size, sortBy, direction, employee);
        }
        else if(customerSearchForm.getField().equalsIgnoreCase("phone")) {
            pageCustomer = customerService.searchByPhoneNumber(customerSearchForm.getValue(), page, size, sortBy, direction, employee);
        }

        model.addAttribute("pageCustomer", pageCustomer);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("customerSearchForm", customerSearchForm);

        return "employee/search";
    }


    // delete customers
    @RequestMapping("/customers/delete/{customerId}")
    public String deleteCustomer(@PathVariable("customerId") int customerId, 
    HttpSession session) {

        customerService.delete(customerId);

        session.setAttribute("message", 
        Message.builder()
        .content("Customer is Deleted successfully !!")
        .type(MessageType.green)
        .build());

        return "redirect:/employee/customers";
    }

    // update customer form view
    @GetMapping("/customers/view/{customerId}")
    public String updateCustomerFormView(
        @PathVariable("customerId") int customerId, 
        Model model) {

        var customer = customerService.getById(customerId);
        CustomerForm customerForm = new CustomerForm();
        
        customerForm.setFirstName(customer.getFirstName());
        customerForm.setEmail(customer.getEmail());
        customerForm.setPhoneNumber(customer.getPhoneNumber());
        customerForm.setAddress(customer.getAddress());
        customerForm.setCity(customer.getCity());
        customerForm.setCompanyName(customer.getCompanyName());
        // employeeForm.setPassword(employee.getPassword());
        customerForm.setLastName(customer.getLastName());
        customerForm.setState(customer.getState());
        customerForm.setPassword(customer.getPassword());
        
        model.addAttribute("customerForm", customerForm);
        model.addAttribute("customerId", customerId);
        
        return "employee/update_customer_view";
    }


    // update customer
    @RequestMapping(value="/customers/update/{customerId}", method=RequestMethod.POST)
    public String updateCustomer(
        @PathVariable("customerId") int customerId, 
        @Valid @ModelAttribute CustomerForm customerForm,
        BindingResult bindingResult,
        HttpSession session) {

        if(bindingResult.hasErrors()) {
            session.setAttribute("message", Message.builder().content("Correct the following errors!!").type(MessageType.red).build());
            return "employee/update_customer_view";
        }

        customerService.update(customerForm, customerId);

        session.setAttribute("message", Message.builder().content("Customer Updated successfully !!").type(MessageType.green).build());

        return "redirect:/employee/customers/view/" + customerId;
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
        Employee employee = employeeService.getEmployeeByEmail(username);

        // get contacts of logged in user
        Page<Project> pageProject = projectService.getByEmployee(employee, page, size, sortBy, direction);

        model.addAttribute("pageProject", pageProject);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("projectSearchForm", new ProjectSearchForm());

        return "employee/projects";
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

        var employee = employeeService.getEmployeeByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Project> pageProject = null;
        if(projectSearchForm.getField().equalsIgnoreCase("name")) {
            pageProject = projectService.searchByName(projectSearchForm.getValue(), page, size, sortBy, direction, employee);
        }
        else if(projectSearchForm.getField().equalsIgnoreCase("status")) {
            pageProject = projectService.searchByStatus(projectSearchForm.getValue(), page, size, sortBy, direction, employee);
        }
        

        model.addAttribute("pageProject", pageProject);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("projectSearchForm", projectSearchForm);

        return "employee/search_projects";
    }


    // detailed project view
    @GetMapping("/projects/view/{projectId}")
    public String projectView(
        @PathVariable("projectId") int projectId, 
        Model model) {

        Project project = projectService.getById(projectId);
        String status = project.getStatus();
        Customer customer = project.getCustomer();


        UpdateStatusForm statusForm = new UpdateStatusForm();
        statusForm.setStatus(project.getStatus());

        
        model.addAttribute("project", project);
        model.addAttribute("status", status);
        model.addAttribute("customer", customer);
        model.addAttribute("statusForm", statusForm);
        model.addAttribute("projectId", projectId);
        
        return "employee/view_project";
    }


    // update project status
    @RequestMapping(value="/projects/update/{projectId}", method=RequestMethod.POST)
    public String updateProject(
        @PathVariable("projectId") int projectId, 
        @Valid @ModelAttribute UpdateStatusForm statusForm,
        BindingResult bindingResult,
        HttpSession session) {

        // if(bindingResult.hasErrors()) {
        //     session.setAttribute("message", Message.builder().content(bindingResult.getAllErrors().toString()).type(MessageType.red).build());
        //     return "redirect:/employee/projects/view/"+projectId;
        // }

        Project project = projectService.getById(projectId);
        project.setStatus(statusForm.getStatus());

        projectService.save(project);

        // session.setAttribute("message", Message.builder().content("Customer Updated successfully !!").type(MessageType.green).build());

        return "redirect:/employee/projects/view/" + projectId;
    }

}
