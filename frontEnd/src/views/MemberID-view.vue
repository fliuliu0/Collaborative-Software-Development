<template lang="html">
  <div>
    <section style="background-color: #FFFFFF">
      <div class="container p-4">
        <div class="row">
          <div class="col-lg-3">
            <div class="card mb-2">
              <div class="card-body text-center" 
                style="background-color: #FEFBF7">
                <img
                  src="../assets/imageHolder.jpg"
                  class="rounded-circle"
                  style="width: 150px"
                  alt="Avatar"
                />
                <h4 class="my-3 fw-semibold text-uppercase">{{ list.name }}</h4>
                <div class="container d-flex justify-content-center mt-9" >
                  <div class="card" style="background-color: #FFFFFF">
                    <div class="p-3 px-4">
                      
                    <div
                        class="d-flex flex-row justify-content-between mt-1"
                      ></div>
                      <div class="d-flex flex-row justify-content-center mt-2">
                        <div class="star">
                          <div class="col-sm-3">
                            <p class="mb-0 fw-semibold">LV Member? </p>
                            <p class="mb-2 ">Enter Member ID: </p>
                          </div>
                          <div class="input-group mb-3">
                            <input type="text" 
                            class="form-control rounded-3 "
                            style="background-colour: #FEFBF7" 
                            id="memberID" v-model="memberID">
                          </div>
                        </div>
                      </div>
                    <div class="col d-flex justify-content-center my-auto mx-auto pt-0 pb-2">
                        <button
                            type="button"
                            class="form-control text-centerbtn btn btn-outline-light btn-rounded btn-block mx-auto" 
                            style="background-color: #5F4D30"
                            @click="updateMemberIdinUser"
                        >
                            Enter 
                        </button>
                    </div>
                      
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="col-lg-8">
            <div class="card mb-4">
              <div class="card-body" 
              style="background-color: #FEFBF7">
                <div class="row">
                  <div class="col-sm-3">
                    <p class="mb-0 fw-semibold">Name</p>
                  </div>
                  <div class="text-muted mb-0">
                    {{ list.name }}
                  </div>
                </div>
                <hr />
                
                <div class="row">
                  <div class="col-sm-3">
                    <p class="mb-0 fw-semibold">Email</p>
                  </div>
                  <div class="text-muted mb-0">
                    {{ list.email }}
                  </div>
                </div>
                <hr />
                
                <div class="row">
                  <div class="col-sm-3">
                    <p class="mb-0 fw-semibold">Mobile</p>
                  </div>
                  <div class="text-muted mb-0">
                    {{ list.number }}
                  </div>
                </div>
                <hr /><div class="col d-flex justify-content-center my-auto mx-auto pt-3 pb-2">
                  <button
                    type="button"
                    class="form-control text-centerbtn btn btn-outline-light btn-rounded btn-block mx-auto" 
                    style="background-color: #5F4D30"
                    @click="goToEditProfile"
                  >
                    Edit Profile 
                  </button>
                </div>

                <div class="col d-flex justify-content-center my-auto mx-auto pt-0 pb-3">
                  <button 
                    type="button" 
                    class="btn btn-outline-danger form-control text-centerbtn btn-rounded mx-auto" 
                    @click="logout"
                  >
                    Log Out
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { useAuthStore } from '../store/auth'
import axios from 'axios'

export default {
  name: "MemberID",
  components: {},
  methods:{
    logout(){
      const store = useAuthStore();
      store.logout();
    },

    goToAccountPage() {
      console.log("otw to Account Page");
      this.$router.push('/AccountPage');
    },

    goToEditProfile() {
      console.log("otw to Edit profile Page");
      this.$router.push('/EditProfilePage');
    },

    async updateMemberIdinUser() {
      const accessToken = localStorage.getItem("token");
      let tokenTest = accessToken.replace(/"/g, "");
      const userEmail = localStorage.getItem("user");
      const emailUser = userEmail.replace(/"/g, "");
      console.log("updating memberid :");
      console.log(this.memberID);
      await axios({
        method: "put",
        url: `http://4.156.87.88:8080/users/update-membership/${emailUser}/${this.memberID}`,
        headers: {
          Authorization: `Bearer ${tokenTest}`,
          "Content-Type": "application/json",
        },
        data: {
          memberId: "",
        },
      }).then(response => {
          if (response.status === 200) {
            // Status is 200, navigate to the account page
            this.$router.push('/AccountPage');
          }
        })
        .catch((error) => {
          if (error.response) {
            // Handle specific HTTP error responses
            if (error.response.status === 404) {
              // Handle 404 error (Not Found)
              alert(error.response.data);
              console.error("Error: " + error.response.data);
            } else {
              // Handle other HTTP errors
              console.error("HTTP error: " + error.response.status);
            }
          } else if (error.request) {
            // Handle network error (no response received)
            console.error("Network error: " + error.message);
          } else {
            // Handle unexpected errors
            console.error("Unexpected error");
          }
        });
    },
  },
  computed: {
    user() {
      return this.$store.state.user;
    },
    updatedUser() {
      return this.$store.state.updatedUser;
    },
  },
  data() {
  return {
    list: {
      name: "",
      number: "",
      email: localStorage.getItem("user").replace(/["']/g, ""), // Remove surrounding quotes
      memberID:"",
    },
  };
  },
  mounted() {
  const userEmail = localStorage.getItem("user");
  const accessToken = localStorage.getItem("token");

  let emailUser = userEmail.replace(/"/g, "");
  let tokenTest = accessToken.replace(/"/g, "");
  console.log(`/users/email/${emailUser}`);
  console.log(`Bearer ${tokenTest}`);

  if (userEmail && accessToken) {
      axios
        .get(`http://4.156.87.88:8080/users/email/${emailUser}`, {
          headers: {
            Authorization: `Bearer ${tokenTest}`,
            "Content-Type": "application/json",
          },
        })
        .then((response) => {
          console.log("Fetched data:", response.data);
          // Assuming the response contains the user's data
          const user = response.data;
          const userName = user.name; // Access "name"
          const phone = user.number; // Access "number"
          const email = user.email; // Access "email"
          console.log(userName);
          console.log(phone);
          this.list.name = userName;
          this.list.number = phone;
          this.list.userEmail = email;
        })
        .catch((error) => {
          console.error("Error fetching user data:", error);
        });
    }
  },
};
</script>