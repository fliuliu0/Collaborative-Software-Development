import axios from "axios";

axios.defaults.baseURL = "http://4.156.87.88:8080";

//automatically add headers without having to do in every request
axios.defaults.headers.common["Authorization"] =
  "Bearer" + localStorage.getItem("token");
