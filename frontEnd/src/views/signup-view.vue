<template lang="html">
 <section style="background-color: #FEFBF7">
  <div class="container">
    <div class="row mt-5">
      <div class="col-lg-4 m-auto rounded-top">
        <div class="d-flex justify-content-center align-items-center mb-5 pb-2">
          <a href="#">
            <img
              src="../assets/LVlogo.png"
              alt=""
              style="opacity: 100; width: 100px; height: 120px"
            />
          </a>
        </div>

        <form @submit.prevent="onSubmit">
          <div class="row ms-4 fw-semibold">Enter Name</div>

          <div class="input group mb-3 mx-3">
            <input type="text" class="form-control" placeholder="Name" v-model="this.user.name" />
          </div>

          <div class="row ms-4 fw-semibold">Enter Phone Number</div>

          <div class="input group mb-3 mx-3">
            <input type="tel" class="form-control" placeholder="Phone Number" v-model="this.user.number" />
          </div>

          <div class="row ms-4 fw-semibold">Enter Email</div>

          <div class="input group mb-3 mx-3">
            <input type="email" class="form-control" placeholder="Email" v-model="this.user.email" />
          </div>

          <div class="row ms-4 fw-semibold">Enter Password</div>
          <div class="input group mb-3 mx-3">
            <input
              type="password"
              class="form-control"
              placeholder="Password"
              v-model="this.user.password"
            />
          </div>

          <div class="d-grid mx-3">
            <button type="submit" 
                    class="form-control text-centerbtn btn btn-outline-light btn-rounded btn-block mx-auto" 
                    style="background-color: #5F4D30"
            >Sign Up</button>
            <p class="text-center">
              Already have an account?
              <a href="" @click="goToLogIn">Log In</a>
            </p>
          </div>
        </form>
      </div>
    </div>
  </div>
 </section>
</template>
<script>
// import axios from 'axios';
// import { ref } from 'vue'
// import { useStore } from 'vuex'

import {useAuthStore} from "@/store/auth";
import {reactive} from "vue";

export default {
  name: "Signup-view",
  components: {},
  setup(){
    const user = reactive({
      email: '',
      password: '',
      name:'',
      number:'',
    });
    return {
      user,

    };


  },
  // data() {
  //   return {
  //     name: "",
  //     number: "",
  //     email: "",
  //     password: "",
  //   };
  // },
  methods: {
    goToLogIn() {
      console.log("otw to log in page");
      this.$router.push("/LogIn");
    },
    onSubmit() {
      console.log(this.user);
      if(this.user.email != '' && this.user.password != '' && this.user.name != '' && this.user.number != ''){
        useAuthStore().signup(this.user.email, this.user.password, this.user.email, this.user.name , this.user.number);
      }

      // const response = await axios.post('/signin', {
      //   email: this.email,
      //   password: this.password,
      // });
      // console.log("form submitted");

      // localStorage.setItem('token', response.data.token);
      // this.$router.push("/");
    },
    // async userSignUp(){
    //   await axios.post('/user/create', {
    //     name: this.name,
    //     number: this.number,
    //     email: this.email,
    //     password: this.password,
    //   });
    //   this.$router.push("/LogIn");
    //
    //
    // },
  },

  // setup () {
  //   const signup_form = ref({});
  //   const store = useStore();

  //   const register = () => {
  //       store.dispatch('register', signup_form.value);
  //   }

  //   return {
  //       signup_form,
  //       register

  //   }
  // }
};
</script>
<style lang="css"></style>
