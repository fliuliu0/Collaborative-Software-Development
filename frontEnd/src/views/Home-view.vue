<template lang="html">
 <section style="background-color: #FEFBF7">
  <div class="container mx-auto my-auto">
    <div id="myCarousel" class="carousel slide" data-bs-ride="carousel">
      <div class="carousel-inner">
        <div class="carousel-item active">
          <img src="../assets/LVhouse.png" class="d-block w-100" alt="..." />
          <div class="carousel-caption d-md-block o">
            
            <div class="row">
              <div class="col-6 my-auto mx-auto">
                <button
                  type="submit"
                  class="btn btn-sm rounded-3"
                  style="background-color: #DED8CF"
                  @click="checkBookingMade"
                >
                  <p class="fw-bold mt-1 mb-1">MAKE BOOKING</p>
                </button>
              </div>
              <div class="col-6 my-auto mx-auto">
                <button
                  type="submit"
                  class="btn btn-sm rounded-3"
                  style="background-color: #DED8CF"
                  @click="goToChangeAppt"
                >
                  <p class="fw-bold mt-1 mb-1">MODIFY BOOKING</p>
                </button>
              </div>
            </div>

          </div>
        </div>
        <div class="carousel-item">
          <img src="../assets/LVmodels1.jpg" class="d-block w-100" alt="..." />
        </div>
        <div class="carousel-item">
          <img src="../assets/LVmodels2.webp" class="d-block w-100" alt="..." />
        </div>
      </div>
      <button
        class="carousel-control-prev"
        type="button"
        data-bs-target="#myCarousel"
        data-bs-slide="prev"
      >
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button
        class="carousel-control-next"
        type="button"
        data-bs-target="#myCarousel"
        data-bs-slide="next"
      >
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>

    <div class="row text-center mt-3 p-1 bg-mute">
      <h2>Our Stores</h2>

    </div>
    <div 
    class="row border rounded-3 bg-mute text-dark m-3 mb-2 pt-3 pb-1 text-center "
    style="background-color: #DED8CF"
    >
      <h5>LV Bayfront</h5>
      <p>Island Maison B2-36, 2 Bayfront Ave
        018972 Singapore, Singapore
      </p>
      <p>+ 65 6788 3888</p>
    </div>

    <div 
      class="row border rounded-3 bg-mute text-dark m-3 pt-3 pb-1 text-center" 
      style="background-color: #DED8CF"
    >
      <h5 >LV Ion Orchard </h5>
      <p>ION Orchard 01-23, 2 Orchard Turn
        238801 Singapore, Singapore
      </p>
      <p>+ 65 6788 3888</p>
    </div>

    <div 
      class="row border rounded-3 bg-mute text-dark m-3 pt-3 pb-1 text-center" 
      style="background-color: #DED8CF"
    >
      <h5 >LV Airport Boulevard</h5>
      <p>65 Airport Boulevard T3, 
        02-36A 819663 Singapore, Singapore
      </p>
      <p>+ 65 6788 3888</p>
    </div>
   
  </div>
 </section>
</template>

<script>
import { Carousel } from "bootstrap";
import axios from "axios";

export default {
  
  name: "Home-view",
  components: {},
  mounted() {
    const myCarousel = document.querySelector("#myCarousel");
    new Carousel(myCarousel);
  },
  methods: {
    checkBookingMade() {
      const userEmail = localStorage.getItem("user");
      const accessToken = localStorage.getItem("token");
      let emailUser = userEmail.replace(/"/g, "");
      let tokenTest = accessToken.replace(/"/g, "");
      axios
        .get(`http://4.156.87.88:8080/users/future-booking-count/${emailUser}`, {
          headers: {
            Authorization: `Bearer ${tokenTest}`,
            "Content-Type": "application/json",
          },
        })
        .then((response) => {
          // Assuming the response contains the user's data
          const bookingMade = response.data;
          console.log("booking made" + bookingMade);
          if (bookingMade >= 3) {
            alert("Booking Limit exceeded 3. You can't make any more bookings.");
          } else {
            console.log("otw to appointment making page");
            this.$router.push("/BookingDetailsPage");
          }
        })
        .catch((error) => {
          console.error("Error fetching user data:", error);
        });
    },
    
    goToChangeAppt() {
      console.log("otw to change appointment page");
      this.$router.push("/ChangeAppointmentPage");
    },
  },
};


</script>
<style lang="css">

  .carousel-item img {
      width: 100%; 
      height: 350px; 
      
  }
  </style>
