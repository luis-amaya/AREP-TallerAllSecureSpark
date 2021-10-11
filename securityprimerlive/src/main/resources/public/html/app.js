function ingresar() {
    var user = document.getElementById("user").value;
    var password = document.getElementById("password").value;
    location.href = 'https://localhost:4567/service?user=' + user + '&passw=' +
        password;
}
