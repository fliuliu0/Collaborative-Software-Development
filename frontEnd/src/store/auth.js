import { defineStore } from "pinia";
import router from "@/router";

export const useAuthStore = defineStore({
  id: "auth",
  state: () => {
    const userItem = localStorage.getItem("user");
    const tokenItem = localStorage.getItem("token");

    return {
      user: userItem ? JSON.parse(userItem) : null,
      token: tokenItem ? JSON.parse(tokenItem) : null,
      returnUrl: "/",
    };
  },
  actions: {
    async login(email, password) {
      const response = await fetch("http://4.156.87.88:8080/api/auth/token", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username: email, password: password }),
      });

      if (response.status === 200) {
        const token = await response.text();
        localStorage.setItem("user", JSON.stringify(email));
        localStorage.setItem("token", JSON.stringify(token));
        this.user = email;
        this.token = token;
        router.push(this.returnUrl || "/");
      } else {
        throw new Error("Invalid credentials" + email + password);
      }
      //springboot will reply a token
    },
    async signup(username, password, email, name, number) {
      //keep this http hardcoded here
      const response = await fetch("http://4.156.87.88:8080/api/sign-up", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: email,
          password: password,
          email: email,
          name: name,
          number: number,
        }),
      });

      if (response.status === 201) {
        // Assuming a successful signup returns a 201 status code
        // Perform any additional actions or navigation you need upon successful signup
        // For example, you can redirect the user to the login page
        router.push("/LogIn");
      } else {
        throw new Error("Failed to sign up");
      }
    },
    logout() {
      console.log("logging out");
      this.user = "";
      this.token = "";

      window.localStorage.removeItem("user");
      window.localStorage.removeItem("token");
      window.localStorage.removeItem("isMember");
      window.localStorage.removeItem("MemberID");

      router.push("/LogIn");
    },
  },
});

// import { createStore } from "vuex";
// import router from "../router";

// export default createStore({
//   state() {
//     return {
//       user: null
//     };
//   },
//   mutations: {
//     SET_USER(state, user) {
//       state.user = user;
//     },
//     CLEAR_USER(state) {
//       state.user = null;
//     },
//   },
//   actions: {
//     async login({ commit }, details) {
//       const { email, password } = details;
//       try {
//         console.log(email);
//         console.log(password);
//         alert("Test Login");
//       } catch (error) {

//         return;
//       }
//       // commit("SET_USER", auth.currentUser);

//       router.push('/');
//     },
//     async register({ commit }, details) {
//       const { email, password } = details;
//       try {
//         console.log(email);
//         console.log(password);
//         alert('Trying to Login');
//       } catch (error) {

//       }
//       // commit("SET_USER", auth.currentUser);

//       router.push('/');
//     },

//     async logout({ commit }) {
//       // await signOut(auth)
//       commit('CLEAR_USER')
//       router.push('/LogInPage');
//     },
//   },
// });
