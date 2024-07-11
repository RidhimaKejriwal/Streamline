console.log("customers.js");

const baseURL = "http://localhost:9090";

const viewCustomerModal = document.getElementById("view_customer_modal");

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
    id: 'view_customer_modal',
    override: true
};

const customerModal = new Modal(viewCustomerModal, options, instanceOptions);

function openCustomerModal() {
    customerModal.show();
}

function closeCustomerModal() {
    customerModal.hide();
}

async function loadCustomerdata(id) {
    try {
        console.log(id);
        const data = await (await fetch(`${baseURL}/api/customers/${id}`)).json();
        console.log(data);
        document.querySelector("#customer_companyName").innerHTML = data.companyName;
        document.querySelector("#customer_firstName").innerHTML = data.firstName;
        document.querySelector("#customer_lastName").innerHTML = data.lastName;
        document.querySelector("#customer_email").innerHTML = data.email;
        document.querySelector("#customer_phoneNumber").innerHTML = data.phoneNumber;
        document.querySelector("#customer_address").innerHTML = data.address;
        document.querySelector("#customer_city").innerHTML = data.city;
        document.querySelector("#customer_state").innerHTML = data.state;

        openCustomerModal();
    } catch (error) {
        console.log(error);
    }
}

// delete customer
async function deleteCustomer(id) {
    
    Swal.fire({
        title: "Do you want to delete the Customer?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
      }).then((result) => {
        if (result.isConfirmed) {
          const url = `${baseURL}/employee/customers/delete/` + id;
          window.location.replace(url);
        //   Swal.fire("Deleted!", "", "success");
        }
      });

}