<template>
  <section>
    <nav class="navbar navbar-expand-lg" style="background-color: #ded8cf">
      <div class="container">
        <div class="navbar-nav ml-auto">
          <li class="nav-item mx-auto my-auto p-auto">
            <a href="#">
              <img
                src="../assets/person.svg"
                alt=""
                style="opacity: 100"
                @click="redirectToAccount"
              />
            </a>
          </li>
        </div>
        <div class="navbar-text mx-auto fw-bold text-dark" @click="goToHome">
          Louis Vuitton
        </div>
        <!-- Icon on the right end -->
        <ul class="navbar-nav ml-auto">
          <li class="nav-item">
            <a href="#">
              <img
                src="../assets/confirm.svg"
                alt=""
                @click="goToConfirmedBooking"
              />
            </a>
          </li>
        </ul>
      </div>
    </nav>
  </section>
</template>
    
<script>
import axios from "axios";

export default {
  name: "NavBar",
  components: {},

  methods: {
    goToHome() {
      console.log("otw to home page");
      this.$router.push("/");
    },
    async updateMemberId(memberId) {
      const accessToken = localStorage.getItem("token");

      let tokenTest = accessToken.replace(/"/g, "");
      console.log("updating memberid :");
      console.log(memberId);
      await axios({
        method: "put",
        url: `/memberships/${memberId}`,
        headers: {
          Authorization: `Bearer ${tokenTest}`,
          "Content-Type": "application/json",
        },
        data: {
          memberId: "",
        },
      }).catch((error) => {
        if (error.response.status === 404) {
          // Handle error (404 Not Found)
          // Access error.response.data for the error message
          console.error("Error: " + error.response.data);
        } else {
          // Handle other errors
          console.error("Unexpected error: " + error.message);
        }
      });
    },
    redirectToAccount() {
      const userEmail = localStorage.getItem("user");
      const accessToken = localStorage.getItem("token");
      let emailUser = userEmail.replace(/"/g, "");
      let tokenTest = accessToken.replace(/"/g, "");
      console.log(accessToken);

      if (userEmail && accessToken) {
        axios
          .get(`/users/email/${emailUser}`, {
            headers: {
              Authorization: `Bearer ${tokenTest}`,
              "Content-Type": "application/json",
            },
          })
          .then((response) => {
            console.log("Fetched data:", response.data);
            // Assuming the response contains the user's data
            const user = response.data;
            // const userName = user.name; // Access "name"
            try {
              if (user.membershipId != null) {
                localStorage.setItem("isMember", "true");
                localStorage.setItem("MemberID", user.membershipId);
                this.$router.push("/AccountPage");
                console.log("member!");

                //user is a member, so update the membership table
                this.updateMemberId(user.membershipId);
              } else {
                localStorage.setItem("isMember", "false");
                this.$router.push("/MemberIdPage");
                console.log("Not a member");
              }
            } catch (error) {
              console.error("Error:", error);
            }
          })
          .catch((error) => {
            console.error("Error fetching user data:", error);
          });
      }
    },
    
    goToConfirmedBooking() {
     
      const userEmail = localStorage.getItem("user");
      const accessToken = localStorage.getItem("token");
      let emailUser = userEmail.replace(/"/g, "");
      let tokenTest = accessToken.replace(/"/g, "");
      console.log(accessToken);
      if (userEmail && accessToken) {
        axios
          .get(`/users/email/${emailUser}`, {
            headers: {
              Authorization: `Bearer ${tokenTest}`,
              "Content-Type": "application/json",
            },
          })
          .then((response) => {
            // Assuming the response contains the user's data
            const user = response.data;
            // const userName = user.name; // Access "name"
            try {
              if (user.membershipId != null) {
                localStorage.setItem("isMember", "true");
                localStorage.setItem("MemberID", user.membershipId);
                console.log("member! checked by booking page");
                //user is a member, so update the membership table
                this.updateMemberId(user.membershipId);
              } else {
                localStorage.setItem("isMember", "false");
                console.log("Not a member checked by booking page");
              }
            } catch (error) {
              console.error("Error:", error);
              //console.log("Not a member");
            }

            //console.log(userName);
          })
          .catch((error) => {
            console.error("Error fetching user data:", error);
          });
      }
      console.log("otw to confirmed booking page");
      this.$router.push("/ConfirmedBookingPage");
    },
  },
};
</script>
    
    
<style lang="css">
</style>