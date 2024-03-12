<template lang="html">
 <section style="background-color: #FEFBF7">
  
  <div class="container">
    <h1 class="text-center fw-semibold mt-4 mb-4">My Bookings</h1>
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-11 col-lg-7 col-xl-5">

        <div v-if="anyBookingExists">
          <div class="card" v-for="booking in bookings" :key="booking.bookingId">
            <div class="my-card">
              <div class="card-body p-4 text-black"
                style="background-color: #DED8CF">

                <div>
                  <h6 class="fw-bold mb-4"> Louis Vuitton @ {{storeIdToLocation(booking.storeId)}}</h6>
                  <div class="d-flex align-items-center justify-content-between mb-3">
                    <p class="mb-2">ID: {{booking.bookingId}}</p>
                    <p class="mb-2">{{booking.time}}</p>
                    <p class="mb-2 me-1">{{booking.date}}</p>
                    <p class="mb-2">{{booking.pax}} pax</p>
                  </div>
                </div>
              
              <div class="row">
                <div class="col-2"></div>
                  <div class="col-5 ">
                    <button type="button" 
                    class="btn btn-outline-light 
                    btn-rounded btn-block mx-auto border-0"
                    style="background-color: #5F4D30"
                    @click="goToChangeAppointmentPage">Modify</button>
                  </div>
              
                <div class="col-4">
                  <button type="button" class="btn btn-outline-danger btn-rounded btn-block mx-auto"
                  @click="deleteBooking(booking.bookingId)" >
                    Cancel</button>
                </div>

                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <div class="row justify-content-center text-center mx-auto mt-3 pt-3">
              <h2>No bookings!</h2>

          </div>
          
        </div>
        
      </div>
    </div>
  </div>
    
 </section>
</template>

<script>
import axios from "axios";

export default {
  name: "Confirmed-booking-view",
  components: {},

  methods: {
    storeIdToLocation(storeId) {
      // const bookingStoreId = booking.storeId;

      if (storeId == 100) {
        return "Ion Orchard";
      } else if (storeId == 200) {
        return "Airport Boulevard";
      } else if (storeId == 300) {
        return "Bayfront";
      }
    },

    deleteBooking(bookingId) {
      console.log("deleting booking ID: " + bookingId);

      fetch(`http://4.156.87.88:8080/bookings/${bookingId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage
            .getItem("token")
            .replace(/"/g, "")}`, // If authentication is required
        },
      })
        .then((response) => {
          if (response.status === 200) {
            // Booking was successfully canceled
            alert("Booking cancelled successfully.");
            console.log("refreshing ConfirmedBookingPage");
            //this.$router.go(0); // Refresh the page
            this.$router.push("/");
          } else if (response.status === 403) {
            alert("Unauthenticated");
          } else if (response.status === 401) {
            alert("Unauthorized");
          } else {
            // Handle errors or display error messages
            alert("Failed to cancel booking.");
          }
        })
        .catch((error) => {
          console.error("Error fetching user data:", error);
        });
    },

    goToChangeAppointmentPage() {
      console.log("Going to the changeAppointment page");
      this.$router.push("/ChangeAppointmentPage");
    },
  },

  data() {
    return {
      bookings: [], // Store multiple bookings in an array
      anyBookingExists: false,
    };
  },

  mounted() {
    const userEmail = localStorage.getItem("user");
    const accessToken = localStorage.getItem("token");
    let emailUser = userEmail.replace(/"/g, "");
    let tokenTest = accessToken.replace(/"/g, "");

    console.log(`/bookings/user/${emailUser}`);
    console.log(`Bearer ${tokenTest}`);

    if (userEmail && accessToken) {
      axios
        .get(`http://4.156.87.88:8080/bookings/user/${emailUser}`, {
          headers: {
            Authorization: `Bearer ${tokenTest}`,
            "Content-Type": "application/json",
          },
        })
        .then((response) => {
          if (response.status == 200) {
            // Assuming the response contains the user's data
            this.bookings = response.data; // Access the data property
            console.log("Number of bookings: " + this.bookings.length);
            this.anyBookingExists = this.bookings.length > 0; // Check if any bookings exist
          } else {
            console.error("Error: Invalid response status:", response.status);
          }
        })
        .catch((error) => {
          console.error("Error fetching user data:", error);
        });
    }
  },
};
</script>

<style lang="css">
.card {
  margin: 20px 0; /* Adjust the margin value as needed */
}
</style>