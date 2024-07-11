let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
})

// TODO:
function changeTheme() {
    //set to web page
    changePageTheme(currentTheme, currentTheme);

    // set the listener to change theme button
    const changeThemeButton = document.querySelector('#theme_change_button');
    changeThemeButton.addEventListener('click', (event) => {
        const oldTheme = currentTheme;
        if(currentTheme === "dark") {
            // theme light
            currentTheme = "light";
        }
        else {
            // theme dark
            currentTheme = "dark";
        }

        changePageTheme(currentTheme, oldTheme);
    
    });
}

// set theme to local storage ---> type of object that stores key-value pairs
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

// get theme from local storage
function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}

function changePageTheme(currentTheme, oldTheme) {
    // update localstorage
    setTheme(currentTheme);
    // remove old theme
    document.querySelector("html").classList.remove(oldTheme);
    // set the current theme
    document.querySelector("html").classList.add(currentTheme);
    // change the text of button
    document.querySelector('#theme_change_button').querySelector("span").textContent = currentTheme == "light" ? "Dark" : "Light";
}
