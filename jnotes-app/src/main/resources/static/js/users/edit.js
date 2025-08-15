function check() {
    const password = document.getElementById("password");
    const confirmPassword = document.getElementById("confirmPassword");
    if (!password.value) {
        password.disabled = true;
        confirmPassword.disabled = true;
    }
}
