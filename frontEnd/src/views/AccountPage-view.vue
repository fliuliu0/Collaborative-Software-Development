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
                      <div class="d-flex align-items-center">
                        <img
                          src="https://i.imgur.com/lhlX7sC.gif"
                          width="210"
                          class="mt-6"/>
                      </div>
                      
                      <div
                        class="d-flex flex-row justify-content-between mt-1"
                      ></div>
                      <div class="d-flex flex-row justify-content-center mt-3">
                        <div class="star">
                          <h6>{{ list.email }}</h6>
                          <h6>Member ID: {{ list.membershipId }}</h6>
                          <h6>Membership Tier: {{ list.tier }}</h6>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="card mb-4 mb-lg-0">
              <div class="card-body p-0">
                <ul class="list-group list-group-flush rounded-3"></ul>
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
                    <div class="mb-0 fw-semibold">Mobile</div>
                  </div>
                  <div class="text-muted mb-0">
                    {{ list.number }}
                  </div>
                </div>
                <hr />

                <div class="col d-flex justify-content-center my-auto mx-auto pt-3 pb-2">
                  <button
                    type="button"
                    class="form-control text-centerbtn btn btn-outline-light btn-rounded btn-block mx-auto" 
                    style="background-color: #5F4D30"
                    @click="goToEditProfileMemberPage"
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
import axios from 'axios';

export default {
  name: "AccountPage",
  components: {},
  methods: {
    logout() {
      const store = useAuthStore();
      store.logout();
    },
    goToEditProfileMemberPage() {
      console.log("otw to edit profile page for members");
      this.$router.push('/EditProfileMemberPage');
    },
    async updateMemberTier(memberId) {
      const token = localStorage.getItem('token').replace(/"/g, '');

      try {
        // Send a PUT request to update the member's tier
        const updateResponse = await axios.put(`/memberships/${memberId}`, null, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        // Check if the update was successful (200 OK status) and retrieve the updated member
        if (updateResponse.status === 200) {
          const updatedMember = updateResponse.data;
          // Assuming the tier is stored in 'memberTier' property
          this.list.tier = updatedMember.tier;
          console.log('Member tier updated successfully:', updatedMember.tier);
        } else {
          console.error('Error updating member tier:', updateResponse.data);
          // Handle errors, display an error message, or take other actions as needed
        }
      } catch (error) {
        console.error('Error updating member tier:', error);
        // Handle network errors, invalid responses, or other issues
      }
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
      membershipId: "",
      email: localStorage.getItem('user').replace(/["']/g, ''), // Remove surrounding quotes
      memberTier: "",
    },
  };
},
mounted() {
  const userEmail = localStorage.getItem('user');
  const accessToken = localStorage.getItem('token');
  
  let emailUser = userEmail.replace(/"/g, "");
  let tokenTest = accessToken.replace(/"/g, "");
  console.log(`/users/email/${emailUser}`);
  console.log(`Bearer ${tokenTest}`);

  if (userEmail && accessToken) {
    axios.get(`http://4.156.87.88:8080/users/email/${emailUser}`, {
      headers: {
      'Authorization': `Bearer ${tokenTest}`, 
      'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        console.log('Fetched data:', response.data);
        // Assuming the response contains the user's data
        const user = response.data;
        const userName = user.name; // Access "name"
        const phone = user.number; // Access "number"
        const email = user.email; // Access "email"
        const membershipId = user.membershipId;
        const tier = user.tier;

        console.log(userName);
        console.log(phone);
        console.log(membershipId);
        console.log(tier);

        this.list.name = userName;
        this.list.number = phone;
        this.list.userEmail = email;
        this.list.membershipId = membershipId;
        this.list.tier = tier;

        this.updateMemberTier(this.list.membershipId);
      })
      .catch(error => {
        console.error('Error fetching user data:', error);
      });
  } 
},
};
</script>
<style lang="css"></style>