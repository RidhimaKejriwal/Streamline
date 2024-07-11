console.log("employees.js");

const baseURL = "http://localhost:9090";

const viewEmployeeModal = document.getElementById("view_employee_modal");

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
    id: 'view_employee_modal',
    override: true
};

const employeeModal = new Modal(viewEmployeeModal, options, instanceOptions);

function openEmployeeModal() {
    employeeModal.show();
}

function closeEmployeeModal() {
    employeeModal.hide();
}

async function loadEmployeedata(id) {
    try {
        console.log(id);
        const data = await (await fetch(`${baseURL}/api/employees/${id}`)).json();
        console.log(data);
        document.querySelector("#employee_employeeType").innerHTML = data.employeeType;
        document.querySelector("#employee_firstName").innerHTML = data.firstName;
        document.querySelector("#employee_lastName").innerHTML = data.lastName;
        document.querySelector("#employee_email").innerHTML = data.email;
        document.querySelector("#employee_phoneNumber").innerHTML = data.phoneNumber;
        document.querySelector("#employee_address").innerHTML = data.address;
        document.querySelector("#employee_city").innerHTML = data.city;
        document.querySelector("#employee_state").innerHTML = data.state;

        openEmployeeModal();
    } catch (error) {
        console.log(error);
    }
}

// delete employee
async function deleteEmployee(id) {
    
    Swal.fire({
        title: "Do you want to delete the Employee?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Delete"
      }).then((result) => {
        if (result.isConfirmed) {
          const url = `${baseURL}/admin/employees/delete/` + id;
          window.location.replace(url);
          Swal.fire("Deleted!", "", "success");
        }
      });

}