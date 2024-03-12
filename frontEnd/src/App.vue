<template>

  <html>
    <div >
      <!-- Include the Navbar component -->
      <Navbar v-if="showNavbar"/>
      <!-- Router View (The main content) -->
      <router-view></router-view>
    </div>
  </html>

</template>

<script>
import Navbar from "./components/NavBar.vue"; // Adjust the path to your Navbar component

export default {
  components: {
    Navbar,
  },
  data() {
    return {
      showNavbar: true, // Set to true by default
    };
  },
  methods: {
    isTokenExpired(token) {
      const tokenData = JSON.parse(atob(token.split(".")[1])); // Parse the payload
      const expiration = tokenData.exp * 1000; // Convert to milliseconds
      // Compare with the current time and return true if the token has expired
      return Date.now() >= expiration;
    },
  },
  created() {
    // Check token expiration on component creation
    if (localStorage.getItem("token")) {
      const token = localStorage.getItem("token");
      if (this.isTokenExpired(token)) {
        // Token has expired, log the user out or take appropriate action
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        // Clear any other user-related data

        // Redirect to the login page
        this.$router.push("/LogIn");
        console.log("session expired: log in again")
        // Optionally, display a logout message to the user
        // this.$notify({
        //   title: "Logged Out",
        //   message: "Your session has expired. Please log in again.",
        //   type: "info",
        // });
      }
    }

    // Set up a timer to periodically check token expiration
    setInterval(() => {
      if (localStorage.getItem("token")) {
        const token = localStorage.getItem("token");
        if (this.isTokenExpired(token)) {
          // Token has expired, log the user out or take appropriate action
          localStorage.removeItem("token");
          localStorage.removeItem("user");
          // Clear any other user-related data

          // Redirect to the login page
          this.$router.push("/LogIn");
          console.log("session expired: log in again")

          // Optionally, display a logout message to the user
          // this.$notify({
          //   title: "Logged Out",
          //   message: "Your session has expired. Please log in again.",
          //   type: "info",
          // });
        }
      }
    }, 30 * 60 * 1000); // Check every 30 minutes
  },

  watch: {
    $route(to) {
      // Update showNavbar based on the route path
      if (to.path === "/LogIn" || to.path === "/SignUpPage") {
        this.showNavbar = false;
      } else {
        this.showNavbar = true;
      }
    },
  },
};
</script>

<style>

body, html {
  height: 100vh;
  width: 100%;
  min-height: 100%;
  padding: 0;
  margin: 0;
  background-color: #FEFBF7;
}

</style>
 