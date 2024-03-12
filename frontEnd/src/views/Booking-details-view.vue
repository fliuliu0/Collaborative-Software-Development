<template>
  <div>
    <section>
      <div class="container">
        <h1 class="text-center fw-semibold mt-4 mb-3">Make a Booking</h1>
        <div class="row d-flex justify-content-center align-items-center h-100">
          <div class="col col-md-15">
            <div class="card-body p-2 text-black">
              <div class="container rounded">
                <form @submit.prevent="submitBooking">
                  <div class="row">
                    <div class="col-md-6 mb-4">
                      <div
                        class="form-control d-flex flex-column"
                        style="background-color: #ded8cf"
                      >
                        <p class="fw-semibold mt-1 mb-1">Location</p>
                        <select
                          class="border-0 outline-none"
                          @change="updateSelectedLocation"
                        >
                          <option value="" hidden selected>Select</option>
                          <option value="1">Ion Orchard</option>
                          <option value="2">Airport Boulevard</option>
                          <option value="3">Bayfront</option>
                        </select>
                      </div>
                    </div>

                    <div class="col-md-6 mb-4">
                      <div
                        class="form-control d-flex flex-column"
                        style="background-color: #ded8cf"
                      >
                        <p class="fw-semibold mt-1 mb-1">No. of Pax</p>
                        <select
                          class="border-0 outline-none"
                          style="height: 100"
                          @change="updatePax"
                        >
                          <option value="" hidden selected>Select</option>
                          <option value="1">1</option>
                          <option value="2">2</option>
                          <option value="3">3</option>
                          <option value="4">4</option>
                        </select>
                      </div>
                    </div>

                    <div class="col-md-6 mb-4">
                      <div
                        class="form-control d-flex flex-column"
                        style="background-color: #ded8cf"
                      >
                        <p class="fw-semibold mt-1 mb-1">Date</p>
                        <h5 class="error-message">{{ dateErrorMessage }}</h5>
                        <!-- Error message displayed above the input box -->
                        <input
                          class="inputbox rounded-3 border-0 text-muted"
                          type="date"
                          v-model="taskData.date"
                          :min="minDate"
                          :max="maxDate"
                          @input="updateSelectedDate"
                          :class="{
                            'disabled-date': isDateBeforeToday(taskData.date),
                          }"
                        />
                      </div>
                    </div>

                    <div class="col-md-6 mb-4">
                      <div
                        class="form-control"
                        style="
                          background-color: #ded8cf;
                          max-height: 300px;
                          overflow-y: auto;
                        "
                      >
                        <p class="fw-bold mt-1 mb-2">Time Selection</p>
                        <div class="container">
                          <div class="row">
                            <!-- Your time selection buttons here -->
                                <div
                                  v-for="timeSlot in availableTimeSlots"
                                  :key="timeSlot"
                                  :class=" {
                                    'btn': true,
                                    'form-control': true,
                                    'btn-custom': true,
                                    'mx-auto': true,
                                    'selected': !hasError && taskData[timeSlot],
                                  }"
                                  @click="updateSelectedTime(timeSlot)"
                                >
                                  {{ timeSlot }}
                                </div>
                              </div>
                          </div>
                      </div>
                    </div>
                  </div>

                  <button
                    type="submit"
                    class="btn form-control text-centerbtn btn btn-outline-light btn-rounded btn-block mx-auto"
                    style="background-color: #5f4d30"
                  >
                    Make Booking
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>


<script>
import axios from "axios";

export default {
  name: "BookingDetailsView",

  data() {
    return {
      taskData: {
        date: null,
        pax: 0,
        storeId: 0,
        time: null,
        email: "",
        selectedDate: null,
      },
      availability: [],
      availableTimeSlots: [],
      hasError: false, // Initialize the error flag
      timeSlots: [
        // Define the timeSlots array
        "11:00",
        "12:00",
        "13:00",
        "14:00",
        "15:00",
        "16:00",
        "17:00",
        "18:00",
      ],
      minDate: new Date(),
      dateErrorMessage: "",
      memberTier: "",
    };
  },
  computed: {
    // minDate() {
    //   // Calculate your minDate based on your requirements
    //   return 'yyyy-MM-dd'; // Replace with the actual minDate value
    // },
    maxDate() {
      // Check the localStorage value to determine maxDate
      const isMember = localStorage.getItem("isMember") === "true";
      if (isMember) {
        // Fetch the member's tier and calculate maxDate based on the result

        if (this.memberTier === "VIP") {
          // If the user is a VIP member, set maxDate 10 days from today
          const today = new Date();
          today.setDate(today.getDate() + 11);
          return today.toISOString().split("T")[0];
        } else {
          // If the user is a normal member, set maxDate 5 days from today
          const today = new Date();
          today.setDate(today.getDate() + 6);
          return today.toISOString().split("T")[0];
        }
      } else {
        // If the user is not a member, set maxDate 3 days from today
        const today = new Date();
        today.setDate(today.getDate() +4 );
        return today.toISOString().split("T")[0];
      }
    },
  },

  methods: {
    filterAvailableTimeSlots() {
      this.availableTimeSlots = this.timeSlots.filter((timeSlot, index) => this.availability[index]);
    },

    fetchMemberTier() {
      // Make an API request to get the member's tier
      // Replace 'YOUR_API_ENDPOINT' with the actual URL
      const memberId = localStorage.getItem("MemberID");
      const apiUrl = `http://4.156.87.88:8080/memberships/${memberId}`;

      const tokenWithQuotes = localStorage.getItem("token");
      const accessToken = tokenWithQuotes.replace(/^"([^"]*)"$/, "$1");
      let tokenTest = accessToken.replace(/"/g, "");

      // Make a GET request to the API
      axios
        .get(apiUrl, {
          headers: {
            Authorization: `Bearer ${tokenTest}`,
            "Content-Type": "application/json",
          },
        })
        .then((response) => {
          // Access the "tier" property in the response data
          this.memberTier = response.data.tier; // Set the memberTier property
          console.log(this.memberTier);
        })
        .catch((error) => {
          console.error("Error fetching member tier:", error);
        });
    },
    
    getUserByEmail() {
      const userEmailWithQuotes = localStorage.getItem("user");
      const userEmail = userEmailWithQuotes.replace(/^"([^"]*)"$/, "$1");

      const tokenWithQuotes = localStorage.getItem("token");
      const accessToken = tokenWithQuotes.replace(/^"([^"]*)"$/, "$1");
      //const accessToken = localStorage.getItem('token');
      console.log(userEmail);
      console.log(accessToken);
      this.taskData.email = userEmail;

      let tokenTest = accessToken.replace(/"/g, "");

      if (userEmail && accessToken) {
        axios
          .get(`http://4.156.87.88:8080/users/email/${userEmail}`, {
            headers: {
              Authorization: `Bearer ${tokenTest}`,
              "Content-Type": "application/json",
            },
          })
          .catch((error) => {
            console.error("Error fetching user data:", error);
          });
      }
    },

    updateSelectedDate() {
      const temp = new Date(this.taskData.date);

      // Check if the selected date is today
      const today = new Date();
      today.setHours(0, 0, 0, 0);

      if (
        temp.getFullYear() === today.getFullYear() &&
        temp.getMonth() === today.getMonth() &&
        temp.getDate() === today.getDate()
      ) {
        this.dateErrorMessage =
          "Bookings cannot be made on the day itself. Please select a date from tomorrow onwards.";
      } else if (temp < new Date()) {
        this.dateErrorMessage =
          "Invalid date. Please select a date after " +
          today.getDate() +
          "/" +
          today.getMonth() +
          "/" +
          today.getFullYear() +
          ".";
      } else {
        // Clear error message if the date is valid
        this.dateErrorMessage = "";
        
        console.log('Selected date:', this.taskData.date);
        this.fetchAvailableTimeslots();
      }
    },

    isDateBeforeToday(selectedDate) {
      if (!selectedDate) return false;
      const today = new Date();
      today.setHours(0, 0, 0, 0); // Set the time to the start of today for accurate comparison
      const selected = new Date(selectedDate);

      if (
        selected.getFullYear() === today.getFullYear() &&
        selected.getMonth() === today.getMonth() &&
        selected.getDate() === today.getDate()
      ) {
        return today;
      }
      return selected < today;
    },

    formatTimeAsSQLTime(time) {
      // Format the time as 'HH:mm:ss' (hours, minutes, seconds)
      return `${time}:00`; // Add seconds part as '00'
    },

    updatePax(pax) {
      const selectedPax = pax.target.value; // Get the selected value from the event
      this.taskData.pax = parseInt(selectedPax); // Set the selected pax
      console.log("Selected pax:", this.taskData.pax);
    },

    updateSelectedLocation(event) {
      const selectedLocation = event.target.value; // Get the selected value from the event
      let selectedId = 0;

      if (selectedLocation == 1) {
        selectedId = 100;
      } else if (selectedLocation == 2) {
        selectedId = 200;
      } else if (selectedLocation == 3) {
        selectedId = 300;
      }
      this.taskData.storeId = selectedId; // Set the selected pax
      console.log("Selected storeId:", this.taskData.storeId);
    },

    updateSelectedTime(time) {
      // Format the time as a valid java.sql.Time
      this.taskData.time = this.formatTimeAsSQLTime(time);

      // Remove the 'selected' class from all time slots
      this.unselectAllTimes();
      this.taskData[time] = true; // Add the 'selected' class to the clicked time slot
      console.log("Selected time:", time);

      this.fetchStoreAvailability();
    },

    unselectAllTimes() {
      // This function removes the 'selected' class from all time slots
      const times = [
        "11:00",
        "12:00",
        "13:00",
        "14:00",
        "15:00",
        "16:00",
        "17:00",
        "18:00",
      ];
      for (const t of times) {
        this.taskData[t] = false;
      }
    },


    fetchAvailableTimeslots() {
      let errorMessage = '';
      if (!this.taskData.storeId) {
        errorMessage += "Location\n";
      }
      if (this.taskData.pax <= 0) {
        errorMessage += "Number of Pax\n";
      }
      
      if (errorMessage) {
        errorMessage = errorMessage.trim(); // Remove trailing space
        this.hasError = true;
        alert(`${errorMessage}\nnot set. Please select before selecting date.`);
       //this.$router.go(0); // Refresh the page
        return;
      }

      const dateObject = new Date(this.taskData.date);
      const formattedDate = dateObject.toISOString().split("T")[0]; // Format as 'yyyy-MM-dd'

      const accessToken = localStorage.getItem('token');
      const tokenTest = accessToken.replace(/"/g, "");
      
      // Construct the API URL with the selected storeId and date
      const apiUrl = `http://4.156.87.88:8080/stores/${this.taskData.storeId}/availability/${formattedDate}`;

      // Make the API request to get availability
      axios
        .get(apiUrl, {
          headers: {
            'Authorization': `Bearer ${tokenTest}`,
            'Content-Type': 'application/json',
          }
        })
        .then((response) => {
          this.availability = response.data; // Update the availability data
          this.filterAvailableTimeSlots(); // Filter available time slots
        })
        .catch((error) => {
          console.error("Error fetching store availability:", error);
        });
    },

    fetchStoreAvailability() {
      const dateObject = new Date(this.taskData.date);
      const formattedDate = dateObject.toISOString().split('T')[0]; // Format as 'yyyy-MM-dd'
    
      // Make an API request to fetch store availability
      const apiUrl = `http://4.156.87.88:8080/stores/${this.taskData.storeId}/availability?date=${formattedDate}&time=${this.taskData.time}`;
      const accessToken = localStorage.getItem("token");
      const tokenTest = accessToken.replace(/"/g, "");

      const headers = {
        Authorization: `Bearer ${tokenTest}`,
        "Content-Type": "application/json",
      };

      axios
      .get(apiUrl, { headers })
      .then((response) => {
        const storeAvailability = response.data;
        console.log("avail: " + storeAvailability);
        const availabilityInt = parseInt(storeAvailability);
  
        // Check the response to determine availability
        if (this.taskData.pax > availabilityInt) {
          this.hasError = true;
          console.error("pax exceeded availability.");
          alert("Availability: " + storeAvailability + ". Store does not have enough capacity left. Please choose a different time slot or location.");
          //this.$router.go(0); // Refresh the page
        }
      })
      .catch((error) => {
        console.error("Error fetching store availability:", error);
      });
    },

    
    submitBooking() {
      this.getUserByEmail();
      this.createBooking();
    },

    createBooking() {
      // Serialize booking data as JSON
      const jsonData = JSON.stringify({
        date: this.taskData.date,
        pax: this.taskData.pax,
        storeId: this.taskData.storeId,
        time: this.taskData.time,
        email: this.taskData.email,
      });

      const apiUrl = "http://4.156.87.88:8080/bookings"; // Use the full URL
      const accessToken = localStorage.getItem("token");

      let tokenTest = accessToken.replace(/"/g, "");

      const headers = {
        Authorization: `Bearer ${tokenTest}`,
        "Content-Type": "application/json",
      };

      axios
        //.post(apiUrl, jsonData, this.taskData, {headers})
        .post(apiUrl, jsonData, { headers })
        .then((response) => {
          console.log("JSON Data:", jsonData);
          console.log(response.data);
          alert("Booking added successfully!");
          console.log("Going to the ConfirmedBooking page");
          this.$router.push("/ConfirmedBookingPage");
        })
        .catch((error) => {
          if (error.response && error.response.status === 404) {
            // Check for 404 status
            // Handle the "Booking cannot be made, capacity of store cannot accommodate no. pax" error here
            // Show an error message to the user
            this.dateErrorMessage = error.response.data;
            console.error('Booking capacity exceeded:', error.response.data);
            alert('Booking capacity exceeded. Please choose a different time slot or location.');
            
          } else if (error.response) {
            console.log("JSON Data:", jsonData);
            console.error("Response error:", error.response);
            alert("Failed to add booking. Error response.\n" + error);
          } else if (error.request) {
            console.log("JSON Data:", jsonData);
            console.error("Request error:", error.request);
            alert("Failed to add booking. Error request.\n" + error);
          } else {
            console.log("JSON Data:", jsonData);
            console.error("Error adding booking:", error);
            alert("Failed to add booking. Please try again.\n" + error);
          }
        });
    },

    goToHome() {
      console.log("Going to the Booking page");
      this.$router.push("/");
    },
  },
  mounted() {
    // When the component is mounted, fetch the member's tier
    this.fetchMemberTier();
  },
};
</script>

<style scoped>
  .disabled-date {
    color: #ccc; /* Set the text color to gray */
    pointer-events: none; /* Disable pointer events, making the date unclickable */
    cursor: not-allowed; /* Change the cursor to not-allowed */
  }
  .btn-custom {
    background-color: white;
    border: 1px solid #5f4d30;
    color: black;
    margin: 8px;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0s, color 0s;
  }

  .btn-custom:hover {
    background-color: #8b8377;
    color: white; /* Change to your desired text color on hover */
  }
  .error-message {
    color: red; /* Set the text color to red */
    font-size: 12px;
  }

  .selected {
    background-color: #8b8377; /* Change to your desired selected background color */
    color: white; /* Change to your desired selected text color */
  }
</style>



