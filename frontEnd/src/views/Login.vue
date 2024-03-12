<script>
//import axios from "axios";
import { reactive  } from 'vue';
import {useAuthStore} from '../store/auth';




export default {
  name: "Log-InPage",
  components: {},

  setup(){
    const user = reactive({
      email: '',
      password: '',
    });
    return {
      user,
    };


  },

  
  methods: {



    goToSignUp() {
      console.log("otw to sign up page");
      this.$router.push("/SignUpPage");
    },
    
    onSubmit() {
      console.log(this.user);
      if(this.user.email != '' && this.user.password != ''){
        useAuthStore().login(this.user.email, this.user.password);
      }
      
      // const response = await axios.post('/signin', {
      //   email: this.email,
      //   password: this.password,
      // });
      // console.log("form submitted");

      // localStorage.setItem('token', response.data.token);
      // this.$router.push("/");
    },
  },

};




</script>
<template lang="html">
 <section style="background-color: #FEFBF7">
  <div class="container">
    <div class="row mt-5">
      <div class="col-lg-4 m-auto rounded-top">
        <div class="d-flex justify-content-center align-items-center mb-4 pb-2">
          <a href="#">
            <img
              src="../assets/LVlogo.png"
              alt=""
              style="opacity: 100; width: 100px; height: 120px"
            />
          </a>
        </div>

        <div class="row ms-4 fw-semibold">Enter Email</div>

        <form action="#" @submit.prevent="onSubmit">
          <!-- <form action="#"> -->
          <div class="input group mb-3 mx-3">
            <input
              type="email"
              class="form-control"
              id="email"
              placeholder="Email"
              v-model="this.user.email"
            />
          </div>

          <div class="row ms-4 fw-semibold">Enter Password</div>
          <div class="input group mb-3 mx-3">
            <input
              type="password"
              class="form-control"
              id="password"
              placeholder="Password"
              v-model="this.user.password"
            />
          </div>

          <div class="d-grid mx-3">
            <button type="submit" 
                    class="form-control text-centerbtn btn btn-outline-light btn-rounded btn-block mx-auto" 
                    style="background-color: #5F4D30"
            >Log In</button>
            <p class="text-center">
              Don't have an account?
              <a href="" @click="goToSignUp">Sign up now</a>
            </p>
          </div>
        </form>
      </div>
    </div>
  </div>
 </section>
</template>

<style lang="css"></style>
