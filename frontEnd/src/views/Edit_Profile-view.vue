<template lang="html">
  <div>
  <section style="background-color: #FFFFFF">
    <div class="container p-4">
      <div class="row">
        <div class="col-lg-4">
          <div class="card mb-2">
            <div class="card-body text-center" style="background-color: #FEFBF7">
              <img
                src="../assets/imageHolder.jpg"
                class="rounded-circle"
                style="width: 150px"
                alt="Avatar"
              />
              <h4 class="my-3 fw-semibold text-uppercase">{{list.name}}</h4>
            </div>
          </div>
        </div>
        <div class="col-lg-8">
          <div class="card mb-4">
            <div class="card-body" style="background-color: #FEFBF7">
              <div class="col-sm-3">
                <p class="mb-0 fw-semibold">Name</p>
              </div>
              <div class="input-group mb-3">
                <input
                  type="text"
                  class="form-control rounded-3"
                  id="fullName"
                  v-model="list.name"
                />
              </div>
              <hr />

              <div class="col-sm-3">
                <p class="mb-0 fw-semibold">Email</p>
              </div>
              <div class="input-group mb-3">
                <input
                  type="text"
                  class="form-control rounded-3"
                  id="email"
                  v-model="list.email"
                />
              </div>
              <hr />

              <div class="col-sm-3">
                <p class="mb-0 fw-semibold">Mobile</p>
              </div>
              <div class="input-group mb-3">
                <input
                  type="text"
                  class="form-control rounded-3"
                  id="mobileno"
                  v-model="list.number" 
                />
              </div>
              <hr />

              <div class="col-sm-3">
                <form @submit.prevent="updateUserPassword">
                  <div class="col-sm-12">
                    <div>
                      <p class="mb-0 fw-semibold">Current Password:</p>
                      <input type="password" class="form-control mb-2" id="oldPassword" v-model="list.oldPassword" required>
                    </div>
                    <div>
                      <p class="mb-0 fw-semibold">New Password:</p>
                      <input type="password" class="form-control mb-3" id="newPassword" v-model="list.newPassword" required>
                    </div>
                  </div>
                  <button type="submit">Change Password</button>
                </form>
              </div>
              

              <div class="col d-flex justify-content-center my-auto mx-auto pt-3 p-0">
                  <button
                      type="submit"
                      class="btn form-control text-centerbtn btn-outline-light btn-rounded btn-block mx-auto"  
                      style="background-color: #5F4D30"
                      @click="updateAndGoToMemberId" >
                  Update
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
import axios from 'axios';
// import { useStore } from 'vuex';

export default {
  name: "Edit-Profile-view",
  components: {},
  data() {
    return {
      errorList: '',
      list: {
        name: "",
        number: "",
        email: "",
        memberID: "",
        // oldPassword: "",
        // newPassword: ""
      },
    };
  },
  methods: {
    goToEditProfileMemberPage() {
      console.log("otw to edit profile page for members");
      this.$router.push('/EditProfileMemberPage');
    },
    goToMemberIdPage() {
      console.log("otw to MemberID Page");
      this.$router.push('/MemberIdPage');
    },
    logout(){
      const store = useAuthStore();
      store.logout();
    },
    async updateUserPassword() {
      const emailUser = localStorage.getItem('user').replace(/"/g, '');
      const token = "Bearer " + localStorage.getItem('token').replace(/"/g, '');
      console.log(token);

      if (!emailUser || !token) {
        console.error('userEmail or token is missing.');
        return;
      }
      if (this.list.newPassword === ''|| this.list.newPassword.trim() === '') {
        alert('New password cannot be empty or null.');
        return;
      }

      console.log('new pw:' + this.list.newPassword);
      console.log('old pw:' + this.list.oldPassword);

      // Create a JSON payload for changing the password
      const data = JSON.stringify({
        email: this.list.email,
        oldPassword: this.list.oldPassword,
        newPassword: this.list.newPassword,
      });

      const headers = {
        'Authorization': token,
        'Content-Type': 'application/json',
      };

      const apiUrl = `http://4.156.87.88:8080/users/update-password`;

      console.log('API URL: ', apiUrl);
      console.log('Request Data: ', data);

      axios
        .put(apiUrl, data, { headers })
        .then((response) => {
          console.log('JSON Data:', data);
          console.log(response.data);
          alert("Password changed successfully!");

          if (this.list.oldPassword !== this.list.newPassword) {
            if (confirm("You are changing your password. To continue, you will need to re-login. Do you want to proceed?")) {
              // Update the password in the auth store
              // useAuthStore().updateUserPassword(this.list.newPassword);
              this.logout(); // Logout
              this.$router.push({ name: 'login' }); // Navigate to the login page
              return;
            }
            this.list.oldPassword = this.list.password;
          }
        })
        .catch((error) => {
          console.error('Error:', error);
          if (error.response && error.response.status === 400) {
            alert('Failed to change password: ' + error.response.data);
          } if (error.response && error.response.status === 401) {
            alert('Failed to change password. Please try again later.');
          }
        });
    },
    async getUserData() {
      const userEmail = localStorage.getItem('user');
      const emailUser = userEmail.replace(/"/g, "");
      const token = localStorage.getItem('token').replace(/"/g, "");

      // Ensure there's a valid token before making the request
      if (userEmail && token) {
        try {
          // Include the token in the Authorization header
          const response = await axios.get(`http://4.156.87.88:8080/users/email/${emailUser}`, {
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json',
            },
          });

          console.log(response.data);
          const userData = response.data;
          this.list.name = userData.name;
          this.list.email = userData.email;
          this.list.number = userData.number;

        } catch (error) {
          if (error.response && error.response.status === 404) {
            alert("User not found");
          } else {
            console.error("An error occurred:", error);
          }
        }
      } else {
        console.error('userEmail or token is missing.');
      }
    },

    updateUser() {
      const userEmail = localStorage.getItem('user');
      const token = localStorage.getItem('token').replace(/"/g, "");

      if (!userEmail || !token) {
        console.error('userEmail or token is missing.');
        return;
      }

      const emailUser = userEmail.replace(/"/g, "");

      // Create a payload that matches the backend DTO 
      // Serialize booking data as JSON
      const userData = JSON.stringify({
        name: this.list.name,
        email: this.list.email,
        number: this.list.number,
        password: this.list.password
        // Add other properties if needed
      });

      const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      };

      const apiUrl = `http://4.156.87.88:8080/users/update/${emailUser}`;

      axios
        .put(apiUrl, userData, {headers})
        .then((response) => {
          console.log('JSON Data:', userData);
          console.log(response.data);
          console.log(localStorage.getItem('token'));
          alert("Profile updated successfully!");

          // Check if the email is being updated
          if (this.list.email !== emailUser) {
            // Email is being updated, prompt the user to re-login
            if (confirm("You are changing your email. To continue, you will need to re-login. Do you want to proceed?")) {
              this.logout();
              console.log("otw to login page");
            // Replace with your login page route
              return;
            }
            // User canceled the email change, revert the email input to the original value
            this.list.email = emailUser;
          }
          //this.$router.go(0); 
          this.$router.push("/");
        })
        .catch((error) => {
          console.error('Error:', error); // Log the entire error object
          if (error.response.status === 404) {
            console.error('Error 404!:', error.response.data);
            alert('Error 404!!!!.');
          } else if (error.response.status === 400) {
            console.error('error 400', error.data);
            alert('Failed to update profile.' + error.response.data);
          } else if (error.response) {
            console.error('JSON Data:', userData);
            console.error("Response error:", error.response);
            alert("Failed to update profile. Error response.\n" + error);
          } 
      });
    },
    
    async updateAndGoToMemberId() {
      this.updateUser();
      this.goToMemberIdPage();
    },
  },

   // async updateMemberId(){
    //   await axios({
    //   method: 'put',
    //   url: `https://api.spotify.com/v1/playlists/${playlistID}/tracks`,
    //   headers: { 'Authorization': 'Bearer ' +this.token },
    //   data: {
    //     "range_start": 1, // first item to change
    //     "insert_before": 3, // location to insert item
    //     "range_length": 1 // number of items to change
    //   }
    // })
    // }
  // },
  // computed: {
  //   user() {
  //     return this.$store.state.user;
  //   },
  //   updatedUser() {
  //     return this.$store.state.updatedUser;
  //   },
  // },
  mounted() {
    this.getUserData();
  }
}
</script>
<style lang="css"></style>