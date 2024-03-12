<template lang="html">
  <section>
    <div class="container">
      <h1 class="text-center fw-semibold mt-4 mb-4">Modify My Booking</h1>
      <div class="row justify-content-center align-items-center">
        <div class="col-11 mb-3">
          <div class="card">
            <div class="card-body text-black">
              <div>
                <h4 class="text-center fw-semibold mb-3 mt-1">
                  Existing Booking
                </h4>

                <div class="text-center"> <!-- Center-align the input and details -->
                  <div v-if="!hasBookingId" class="details-container">
                    <div class="d-flex justify-content-center align-items-center">
                      <input v-model="bookingId" type="text" placeholder="Confirm Booking ID">
                      <button @click="confirmBooking" class="confirm-button">Confirm</button>
                    </div>
                  </div>

                  <div v-if="hasBookingId" class="details-container"> <!-- Conditionally render this block and apply CSS class -->
                    <p>Confirmed Booking ID: {{ confirmedId }}</p>

                    <h6 class="text-center fw-semibold mb-4 mt-2">
                      {{ this.storeIdToLocation(this.bookingStoreId) }}
                    </h6>

                    <div class="text-center">
                      <div class="mb-2 me-5 d-inline d-xl-block">{{ this.bookingTime }}</div>
                      <div class="mb-2 me-5 d-inline d-xl-block">{{ this.bookingDate }}</div>
                      <div class="mb-2 d-inline d-xl-block">{{ this.bookingPax }} pax</div>
                    </div>
                  </div>
                </div>

              </div>
            </div>
          </div>
        </div>
        <form @submit.prevent="submitBooking">
          <div class="card-body p-3 text-black">
            <form action="">
              <div class="row">
                <div class="col-md-6 mb-4">
                  <div
                    class="form-control d-flex flex-column"
                    style="background-color: #ded8cf"
                  >
                    <p class="fw-semibold mt-1 mb-1">Location</p>
                    <select
                      class="border-0 outline-none"
                      @change="selectLocation"
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
                      style="height: 100%"
                      @change="updatePax"
                    >
                      <option value="" hidden selected>0</option>
                      <option value="1">1</option>
                      <option value="2">2</option>
                      <option value="3">3</option>
                      <option value="4">4</option>
                    </select> 
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6 mb-4">
                  <div
                    class="form-control d-flex flex-column"
                    style="background-color: #ded8cf"
                  >
                    <p class="fw-semibold mt-1 mb-1">Date</p>
                    <h5 class="error-message">{{ dateErrorMessage }}</h5> <!-- Error message displayed above the input box -->
                    <input
                      class="inputbox rounded-3 border-0 text-muted"
                      type="date"
                      v-model="taskData.newDate"
                      :min="minDate"
                      :max="maxDate"
                      @input="updateSelectedDate"
                      :class="{ 'disabled-date': isDateBeforeToday(taskData.date) }">
                  </div>
                </div>

                <div class="col-md-6 mb-4">
                  <div class="form-control" style="background-color: #ded8cf; max-height: 300px; overflow-y: auto;">
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
                type="button"
                @click="submitBooking"
                class="btn form-control text-center btn btn-outline-light btn-rounded btn-block mx-auto"
                style="background-color: #5f4d30"
              >
                Update Booking
              </button>
            </form>
          </div>
        </form>
      </div>
    </div>
  </section>
</template>

<script>
import axios from "axios";

export default {
  name: "ChangeAppointmentPage",
  components: {},

  data() {
    return {
      hasBookingId: false,
      bookingId: 0,
      confirmedId: null,
      bookingPax: 0, // Property to store the number of pax
      bookingTime: "", // Property to store the time
      bookingDate: "", // Property to store the date
      bookingStoreId: 0,

      taskData: {
        newDate: null,
        newPax: 0,
        newStoreId: 0,
        newTime: this.selectedTime,
        email: "",
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
        today.setDate(today.getDate() + 4);
        return today.toISOString().split("T")[0];
      }
    },
  },

  methods: {
    filterAvailableTimeSlots() {
      this.availableTimeSlots = this.timeSlots.filter(
        (timeSlot, index) => this.availability[index]
      );
    },

    confirmBooking() {
      // Make an API request to fetch store availability /bookings/{bookingId}
      const apiUrl = `http://4.156.87.88:8080/bookings/${this.bookingId}`;
      const accessToken = localStorage.getItem("token");
      const tokenTest = accessToken.replace(/"/g, "");

      const headers = {
        Authorization: `Bearer ${tokenTest}`,
        "Content-Type": "application/json",
      };

      axios
        .get(apiUrl, { headers })
        .then((response) => {
          const bookingDetails = response.data; // Assuming the response contains booking details
          if (bookingDetails) {
            // Set the confirmed ID
            this.confirmedId = this.bookingId;
            this.hasBookingId = true;

            // Set other booking details
            this.bookingPax = bookingDetails.pax; // Assuming "pax" is a property in the response
            this.bookingStoreId = bookingDetails.storeId; // Assuming "location" is a property
            this.bookingLocation = this.storeIdToLocation(
              bookingDetails.storeId
            );
            this.bookingTime = bookingDetails.time; // Assuming "time" is a property
            this.bookingDate = bookingDetails.date; // Assuming "date" is a property
          } else {
            // Handle the case where the booking was not found
            alert("Booking not found. Please try again.");
          }
        })
        .catch((error) => {
          if (error.response && error.response.status === 404) {
            alert("Invalid booking ID. Please enter a valid booking ID.");
          } else {
            console.error("Error:", error);
          }
        });
    },

    storeIdToLocation(storeId) {
      if (storeId == 100) {
        return "Ion Orchard";
      } else if (storeId == 200) {
        return "Airport Boulevard";
      } else if (storeId == 300) {
        return "Bayfront";
      }
    },

    updatePax(pax) {
      const selectedPax = pax.target.value; // Get the selected value from the event
      this.taskData.newPax = parseInt(selectedPax); // Set the selected pax
      console.log("Selected pax:", this.taskData.newPax);
    },

    updateSelectedDate() {
      const temp = new Date(this.taskData.newDate);

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
          " onwards.";
      } else {
        // Clear error message if the date is valid
        this.dateErrorMessage = "";
        console.log("Selected date:", this.taskData.newDate);
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

    updateSelectedTime(time) {
      // Format the time as a valid java.sql.Time
      this.taskData.newTime = this.formatTimeAsSQLTime(time);

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
      let errorMessage = "";
      if (!this.taskData.newStoreId) {
        errorMessage += "Location\n";
      }
      if (this.taskData.newPax <= 0) {
        errorMessage += "Number of Pax\n";
      }

      if (errorMessage) {
        errorMessage = errorMessage.trim(); // Remove trailing space
        this.hasError = true;
        alert(`${errorMessage}\nnot set. Please select before selecting date.`);
        //this.$router.go(0); // Refresh the page
        return;
      }

      const dateObject = new Date(this.taskData.newDate);
      const formattedDate = dateObject.toISOString().split("T")[0]; // Format as 'yyyy-MM-dd'

      const accessToken = localStorage.getItem("token");
      const tokenTest = accessToken.replace(/"/g, "");

      // Construct the API URL with the selected storeId and date
      const apiUrl = `http://4.156.87.88:8080/stores/${this.taskData.newStoreId}/availability/${formattedDate}`;

      // Make the API request to get availability
      axios
        .get(apiUrl, {
          headers: {
            Authorization: `Bearer ${tokenTest}`,
            "Content-Type": "application/json",
          },
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
      const dateObject = new Date(this.taskData.newDate);
      const formattedDate = dateObject.toISOString().split("T")[0]; // Format as 'yyyy-MM-dd'

      // Make an API request to fetch store availability
      const apiUrl = `http://4.156.87.88:8080/stores/${this.taskData.newStoreId}/availability?date=${formattedDate}&time=${this.taskData.newTime}`;
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

          if (this.taskData.newPax > availabilityInt) {
            this.hasError = true;
            console.error("pax exceeded availability.");
            alert(
              "Availability: " +
                storeAvailability +
                ". Store does not have enough capacity left. Please choose a different time slot or location."
            );
            //this.$router.go(0); // Refresh the page
          } else {
            console.log(this.taskData.newPax);
          }
        })
        .catch((error) => {
          console.error("Error fetching store availability:", error);
        });
    },

    selectLocation(event) {
      const selectedLocation = event.target.value; // Get the selected value from the event
      let selectedId = 0;

      if (selectedLocation == 1) {
        selectedId = 100;
      } else if (selectedLocation == 2) {
        selectedId = 200;
      } else if (selectedLocation == 3) {
        selectedId = 300;
      }
      this.taskData.newStoreId = selectedId; // Set the selected pax
      console.log(this.taskData.newStoreId);
    },

    submitBooking() {
      if (!this.hasBookingId) {
        alert("Please enter and confirm your booking ID before updating.");
        return;
      }
      this.getUserByEmail();
      this.updateBooking();
    },

    getUserByEmail() {
      const userEmailWithQuotes = localStorage.getItem("user");
      const userEmail = userEmailWithQuotes.replace(/^"([^"]*)"$/, "$1");

      const tokenWithQuotes = localStorage.getItem("token");
      const accessToken = tokenWithQuotes.replace(/^"([^"]*)"$/, "$1");
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

    updateBooking() {
      // Serialize booking data as JSON
      this.getUserByEmail();

      const jsonData = JSON.stringify({
        bookingId: this.confirmedId,
        date: this.taskData.newDate,
        pax: this.taskData.newPax,
        storeId: this.taskData.newStoreId,
        time: this.taskData.newTime,
        email: this.taskData.email,
      });

      const apiUrl = `http://4.156.87.88:8080/bookings/${this.confirmedId}`; // Use the full URL
      const accessToken = localStorage.getItem("token");

      let tokenTest = accessToken.replace(/"/g, "");

      const headers = {
        Authorization: `Bearer ${tokenTest}`,
        "Content-Type": "application/json",
      };

      axios
        .put(apiUrl, jsonData, { headers })
        .then((response) => {
          console.log("JSON Data:", jsonData);
          console.log(response.data);
          alert("Booking updated successfully!");
          console.log("Going to the ConfirmedBooking page");
          this.$router.push("/ConfirmedBookingPage");
        })
        .catch((error) => {
          if (error.response && error.response.status === 500) {
            alert("Please fill in all details before updating.");
            return;
          } else if (error.response) {
            console.log("JSON Data:", jsonData);
            console.error("Response error:", error.response);
            alert("Failed to update booking. Error response.\n" + error);
          } else if (error.request) {
            console.log("JSON Data:", jsonData);
            console.error("Request error:", error.request);
            alert("Failed to update booking. Error request.\n" + error);
          } else {
            console.log("JSON Data:", jsonData);
            console.error("Error adding booking:", error);
            alert("Failed to update booking. Please try again.\n" + error);
          }
        });
    },

    goToHome() {
      console.log("Going to the Booking page");
      this.$router.push("/");
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
.details-container {
  margin-top: 20px; /* Adjust this value to control the space between the input and details */
}

.confirm-button {
  background-color: #5f4d30; /* Change this to your desired background color */
  color: #fff; /* Change this to your desired text color */
  /* Add any additional styling as needed */
}
</style>