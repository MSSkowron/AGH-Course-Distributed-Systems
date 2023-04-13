package main

import (
	"fmt"
	"log"
	"net/http"
	"os"
	"strconv"
	"strings"
)

var users = map[string]string{"username": "password"}

func main() {
	value := os.Getenv("ADDRESS")
	if len(value) == 0 {
		value = "http://localhost:8080"
	}

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		http.Redirect(w, r, value+"/login", http.StatusSeeOther)
	})

	// login
	http.HandleFunc("/login", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "GET" {
			w.Header().Set("Content-Type", "text/html")
			w.WriteHeader(http.StatusOK)
			w.Write([]byte(loginHTML))

			return
		} else if r.Method == "POST" {
			if r.Header.Get("Content-Type") != "application/x-www-form-urlencoded" {
				generateErrHTML(w, http.StatusUnsupportedMediaType, "UnsupportedMediaType", "only application/x-www-form-urlencoded is supported")

				return
			}

			if err := signin(w, r); err != nil {
				return
			}

			http.Redirect(w, r, value+"/form", http.StatusSeeOther)
		}
	})

	// form
	http.HandleFunc("/form", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "GET" {
			if err := authorize(w, r); err != nil {
				return
			}

			w.Header().Set("Content-Type", "text/html")
			w.WriteHeader(http.StatusOK)
			w.Write([]byte(formHTML))

			return
		} else if r.Method == "POST" {
			if err := authorize(w, r); err != nil {
				return
			}

			if r.Header.Get("Content-Type") != "application/x-www-form-urlencoded" {
				generateErrHTML(w, http.StatusUnsupportedMediaType, "UnsupportedMediaType", "only application/x-www-form-urlencoded is supported")

				return
			}

			// get HTML form parameters from the request body
			formParameters := parseBody(r)

			// validate HTML form parameters
			valid := true
			latitude, ok := formParameters["latitude"]
			if !ok || latitude == "" {
				valid = false
			}

			longitude, ok := formParameters["longitude"]
			if !ok || longitude == "" {
				valid = false
			}

			days, ok := formParameters["days"]
			if !ok || days == "" {
				valid = false
			}

			if !valid {
				generateErrHTML(w, http.StatusBadRequest, "BadRequest", "All form fields are required")

				return
			}

			latitude = strings.TrimLeft(strings.TrimRight(latitude, " "), " ")
			longitude = strings.TrimLeft(strings.TrimRight(longitude, " "), " ")
			days = strings.TrimLeft(strings.TrimRight(days, " "), " ")

			// validate user's input
			valid = true
			if latitudeFloat, err := strconv.ParseFloat(latitude, 64); err != nil || latitudeFloat < -90.0 || latitudeFloat > 90.0 {
				valid = false
			}
			if longitudeFloat, err := strconv.ParseFloat(longitude, 64); err != nil || longitudeFloat < -180.0 || longitudeFloat > 180.0 {
				valid = false
			}
			if daysInt, err := strconv.Atoi(days); err != nil || daysInt < 1 || daysInt > 10 {
				valid = false
			}
			if !valid {
				generateErrHTML(w, http.StatusBadRequest, "BadRequest", "invalid input")

				return
			}

			// send API calls concurrently
			addressCh := make(chan AddressAPIResponse)
			weather1Ch := make(chan WeatherAPIResponse)
			weather2Ch := make(chan WeatherAPIResponse)

			go fetchAddress(latitude, longitude, addressCh)
			go fetchWeatherFromAPI1(latitude, longitude, days, weather1Ch)
			go fetchWeatherFromAPI2(latitude, longitude, days, weather2Ch)

			address := <-addressCh
			weather1 := <-weather1Ch
			weather2 := <-weather2Ch

			// check reponse codes
			if !isCodeSuccessful(address.statusCode) || !isCodeSuccessful(weather1.statusCode) || !isCodeSuccessful(weather2.statusCode) {
				generateExternalErrHTML(w, address, weather1, weather2)

				return
			}

			// calculate result
			calcResult := calculateResults(weather1, weather2)

			// generate response
			responseHTML := fmt.Sprintf(`
				<html lang="en">
					<head>
						<meta charset="UTF-8">
						<meta http-equiv="X-UA-Compatible" content="IE=edge">
						<meta name="viewport" content="width=device-width, initial-scale=1.0">
						<title>GoRESTAPI</title>
					</head>
                    <body>
						<h1>Status: %s</h1>
						<h1>StatusCode: %d</h1>
                        <h1>Weather in %s for the next %s days</h1>
						<table>
							<tr>
								<th>Name</th>
								<th>Value</th>
							</tr>
							<tr>
								<td>Average temperature based on both APIs</td>
								<td>%.1f C</td>
							</tr>
							<tr>
								<td>Average temperature based on first API</td>
								<td>%.1f C</td>
							</tr>
							<tr>
								<td>Average temperature based on second API</td>
								<td>%.1f C</td>
							</tr>
							<tr>
								<td>  </td>
							</tr>
							<tr>
								<td>Minimum temperature based on both APIs</td>
								<td>%.1f C</td>
							</tr>
							<tr>
								<td>Minimum temperature based on first API</td>
								<td>%.1f C</td>
							</tr>
							<tr>
								<td>Minimum temperature based on second API</td>
								<td>%.1f C</td>
							</tr>
							<tr>
								<td>  </td>
							</tr>
							<tr>
								<td>Maximum temperature based on both APIs</td>
								<td>%.1f C</td>
							</tr>
							<tr>
								<td>Maximum temperature based on first API</td>
								<td>%.1f C</td>
							</tr>
							<tr>
								<td>Maximum temperature based on second API</td>
								<td>%.1f C</td>
							</tr>
						</table>
                    </body>
                </html>
            `, "OK", http.StatusOK, address.address, days, calcResult.avgTemp, calcResult.avgTemp1, calcResult.avgTemp2, calcResult.avgMinTemp, calcResult.minTemp1, calcResult.minTemp2, calcResult.avgMaxTemp, calcResult.maxTemp1, calcResult.maxTemp2)

			w.Header().Set("Content-Type", "text/html")
			w.WriteHeader(http.StatusOK)
			w.Write([]byte(responseHTML))
		}
	})

	log.Println("server is listening on port :8080")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal(err)
	}
}
