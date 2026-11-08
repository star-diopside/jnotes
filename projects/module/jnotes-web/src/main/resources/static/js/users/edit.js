document.forms["main"].onsubmit = () => {
    const password = document.getElementById("password");
    const confirmPassword = document.getElementById("confirmPassword");
    if (!password.value && !confirmPassword.value) {
        password.disabled = true;
        confirmPassword.disabled = true;
    }
};
