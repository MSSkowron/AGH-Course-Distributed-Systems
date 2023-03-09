package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"strconv"
	"time"
)

const formHTML = `
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GoRESTAPI</title>
</head>
<body>
    <form action="/" method="post">
		<h1> Weather Forecast </h1>
        <label for="username">Username</label>
        <input id="username" type="text" name="username">
		<br>
		<br>
        <label for="password">Password</label>
        <input id="password" type="password" name="password">
		<br>
		<br>
		<br>
		<br>
        <label for="latitude">Latitude</label>
        <input id="latitude" type="text" name="latitude">
        <label for="longitude">Longitude</label>
        <input id="longitude" type="text" name="longitude">
        <label for="days">Days (max 10)</label>
        <input id="days" type="text" name="days">
        <br>
		<br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
`

var users = map[string]string{"username": "password"}

type CityAPIResponse struct {
	apiName    string
	cityName   string
	statusCode int
	status     string
}

type WeatherAPIResponse struct {
	apiName    string
	tempMax    []float64
	tempMin    []float64
	statusCode int
	status     string
}

type CalculationsResult struct {
	minTemp1   float64
	minTemp2   float64
	avgMinTemp float64
	maxTemp1   float64
	maxTemp2   float64
	avgMaxTemp float64
	avgTemp1   float64
	avgTemp2   float64
	avgTemp    float64
}

func main() {
	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == "GET" {
			w.Header().Set("Content-Type", "text/html")
			w.WriteHeader(http.StatusOK)
			w.Write([]byte(formHTML))

			return
		} else if r.Method == "POST" {
			// check header type
			if r.Header.Get("Content-Type") != "application/x-www-form-urlencoded" {
				generateErrHTML(w, http.StatusUnsupportedMediaType, "UnsupportedMediaType", "only application/x-www-form-urlencoded is supported")

				return
			}

			// get HTML form parameters from the request body
			formParameters := parseBody(r)

			// validate HTML form parameters
			valid := true
			inUsername, ok := formParameters["username"]
			if !ok {
				valid = false
			}

			inPassword, ok := formParameters["password"]
			if !ok {
				valid = false
			}

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

			// validate user
			password, ok := users[inUsername]
			if !ok || inPassword != password || inUsername == "" || inPassword == "" {
				generateErrHTML(w, http.StatusUnauthorized, "Unauthorized", "invalid credentials")

				return
			}

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
			cityCh := make(chan CityAPIResponse)
			weather1Ch := make(chan WeatherAPIResponse)
			weather2Ch := make(chan WeatherAPIResponse)

			go fetchCityName(latitude, longitude, cityCh)
			go fetchWeatherFromAPI1(latitude, longitude, days, weather1Ch)
			go fetchWeatherFromAPI2(latitude, longitude, days, weather2Ch)

			city := <-cityCh
			weather1 := <-weather1Ch
			weather2 := <-weather2Ch

			// check reponse codes
			if !isCodeSuccessful(city.statusCode) || !isCodeSuccessful(weather1.statusCode) || !isCodeSuccessful(weather2.statusCode) {
				generateExternalErrHTML(w, city, weather1, weather2)

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
            `, "OK", http.StatusOK, city.cityName, days, calcResult.avgTemp, calcResult.avgTemp1, calcResult.avgTemp2, calcResult.avgMinTemp, calcResult.minTemp1, calcResult.minTemp2, calcResult.avgMaxTemp, calcResult.maxTemp1, calcResult.maxTemp2)

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

// ograniczenie: 1 zapytanie na sekunde
func fetchCityName(latitude, longitude string, ch chan CityAPIResponse) {
	var (
		cityAPIResponse CityAPIResponse = CityAPIResponse{apiName: "geocode"}
		url             string          = fmt.Sprintf("https://geocode.xyz/%s,%s?json=1", latitude, longitude)
	)

	resp, err := http.Get(url)
	if err != nil {
		cityAPIResponse.statusCode = http.StatusInternalServerError
		cityAPIResponse.status = "Internal server error"

		ch <- cityAPIResponse

		return
	}

	cityAPIResponse.statusCode = resp.StatusCode
	cityAPIResponse.status = resp.Status

	if !isCodeSuccessful(resp.StatusCode) {
		ch <- cityAPIResponse

		return
	}

	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		cityAPIResponse.statusCode = http.StatusInternalServerError
		cityAPIResponse.status = "Internal server error"

		ch <- cityAPIResponse

		return
	}

	var location struct {
		City string `json:"city"`
	}
	err = json.Unmarshal(body, &location)
	if err != nil {
		cityAPIResponse.statusCode = http.StatusInternalServerError
		cityAPIResponse.status = "Internal server error"

		ch <- cityAPIResponse

		return
	}

	cityAPIResponse.cityName = location.City

	ch <- cityAPIResponse
}

func fetchWeatherFromAPI1(latitude, longitude, days string, ch chan WeatherAPIResponse) {
	var (
		weatherAPIResponse WeatherAPIResponse = WeatherAPIResponse{apiName: "weatherapi"}
		url                string             = fmt.Sprintf("http://api.weatherapi.com/v1/forecast.json?key=ef95ee45d3e94837b26195852230703&q=%s,%s&days=%s", latitude, longitude, days)
	)

	resp, err := http.Get(url)
	if err != nil {
		weatherAPIResponse.statusCode = http.StatusInternalServerError
		weatherAPIResponse.status = "Internal server error"

		ch <- weatherAPIResponse

		return
	}

	weatherAPIResponse.statusCode = resp.StatusCode
	weatherAPIResponse.status = resp.Status

	if !isCodeSuccessful(resp.StatusCode) {
		ch <- weatherAPIResponse

		return
	}

	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		weatherAPIResponse.statusCode = http.StatusInternalServerError
		weatherAPIResponse.status = "Internal server error"

		ch <- weatherAPIResponse

		return
	}

	var weatherData struct {
		Forecast struct {
			Forecastday []struct {
				Date string `json:"date"`
				Day  struct {
					MaxtempC float64 `json:"maxtemp_c"`
					MintempC float64 `json:"mintemp_c"`
				} `json:"day"`
			} `json:"forecastday"`
		} `json:"forecast"`
	}
	err = json.Unmarshal(body, &weatherData)
	if err != nil {
		weatherAPIResponse.statusCode = http.StatusInternalServerError
		weatherAPIResponse.status = "Internal server error"

		ch <- weatherAPIResponse

		return
	}

	var (
		tempMax []float64
		tempMin []float64
	)
	for _, forecast := range weatherData.Forecast.Forecastday {
		tempMax = append(tempMax, forecast.Day.MaxtempC)
		tempMin = append(tempMin, forecast.Day.MintempC)
	}

	weatherAPIResponse.tempMax = tempMax
	weatherAPIResponse.tempMin = tempMin

	ch <- weatherAPIResponse
}

func fetchWeatherFromAPI2(latitude, longitude, days string, ch chan WeatherAPIResponse) {
	weatherAPIResponse := WeatherAPIResponse{apiName: "open-meteo"}

	daysInt, _ := strconv.Atoi(days)
	startDate := time.Now().AddDate(0, 0, 1).Format("2006-01-02")
	endDate := time.Now().AddDate(0, 0, daysInt).Format("2006-01-02")

	url := fmt.Sprintf("https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s", latitude, longitude) + "&daily=temperature_2m_max,temperature_2m_min&timezone=Europe%2FBerlin&start_date=" + fmt.Sprintf("%s&end_date=%s", startDate, endDate)
	resp, err := http.Get(url)
	if err != nil {
		weatherAPIResponse.statusCode = http.StatusInternalServerError
		weatherAPIResponse.status = "Internal server error"

		ch <- weatherAPIResponse

		return
	}

	weatherAPIResponse.statusCode = resp.StatusCode
	weatherAPIResponse.status = resp.Status

	if !isCodeSuccessful(resp.StatusCode) {
		ch <- weatherAPIResponse

		return
	}

	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		weatherAPIResponse.statusCode = http.StatusInternalServerError
		weatherAPIResponse.status = "Internal server error"

		ch <- weatherAPIResponse

		return
	}

	var weatherData struct {
		Daily struct {
			Time             []string  `json:"time"`
			Temperature2mMax []float64 `json:"temperature_2m_max"`
			Temperature2mMin []float64 `json:"temperature_2m_min"`
		} `json:"daily"`
	}
	err = json.Unmarshal(body, &weatherData)
	if err != nil {
		weatherAPIResponse.statusCode = http.StatusInternalServerError
		weatherAPIResponse.status = "Internal server error"

		ch <- weatherAPIResponse

		return
	}

	var (
		tempMax []float64
		tempMin []float64
	)

	weatherAPIResponse.tempMax = append(tempMax, weatherData.Daily.Temperature2mMax...)
	weatherAPIResponse.tempMin = append(tempMin, weatherData.Daily.Temperature2mMin...)

	ch <- weatherAPIResponse
}

func calculateResults(res1, res2 WeatherAPIResponse) CalculationsResult {
	var calcResult CalculationsResult
	// min temp based on first, second, average from both APIs
	calcResult.minTemp1 = min(res1.tempMin)
	calcResult.minTemp2 = min(res2.tempMin)
	calcResult.avgMinTemp = (calcResult.minTemp1 + calcResult.minTemp2) / float64(2)

	// max temp based on first, second, average from both APIs
	calcResult.maxTemp1 = max(res1.tempMax)
	calcResult.maxTemp2 = max(res2.tempMax)
	calcResult.avgMaxTemp = (calcResult.maxTemp1 + calcResult.maxTemp2) / float64(2)

	// Calculate average temperatures
	calcResult.avgTemp1 = avg(avgSlice(res1.tempMin, res1.tempMax))
	calcResult.avgTemp2 = avg(avgSlice(res2.tempMin, res2.tempMax))
	calcResult.avgTemp = (calcResult.avgTemp1 + calcResult.avgTemp2) / float64(2)

	return calcResult
}

func avgSlice(s1, s2 []float64) []float64 {
	var s []float64

	for i := 0; i < len(s1); i++ {
		s = append(s, (s1[i]+s2[i])/float64(2))
	}

	return s
}

func avg(s []float64) float64 {
	var sum float64 = 0

	for _, number := range s {
		sum += number
	}

	return sum / float64(len(s))
}

func max(s []float64) float64 {
	var max float64 = s[0]

	for _, number := range s {
		if number > max {
			max = number
		}
	}

	return max
}

func min(s []float64) float64 {
	var min float64 = s[0]

	for _, number := range s {
		if number < min {
			min = number
		}
	}

	return min
}

func parseBody(r *http.Request) map[string]string {
	if err := r.ParseForm(); err != nil {
		return nil
	}

	formValues := make(map[string]string)
	for key, value := range r.Form {
		formValues[key] = value[0]
	}

	return formValues
}

func isCodeSuccessful(code int) bool {
	return code >= 200 && code <= 299
}

func generateErrHTML(w http.ResponseWriter, code int, status, message string) {
	responseHTML := fmt.Sprintf(`
	<!DOCTYPE html>
	<html lang="en">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>GoRESTAPI</title>
	</head>
	<body>
		<h1> %d - %s </h1>
		<h1> %s </h1>
	</body>
	</html>
	`, code, status, message)

	w.Header().Set("Content-Type", "text/html")
	w.WriteHeader(code)
	w.Write([]byte(responseHTML))
}

func generateExternalErrHTML(w http.ResponseWriter, cityAPIResponse CityAPIResponse, weatherAPIResponse1, weatherAPIReponse2 WeatherAPIResponse) {
	responseHTML := fmt.Sprintf(`
				<html lang="en">
					<head>
						<meta charset="UTF-8">
						<meta http-equiv="X-UA-Compatible" content="IE=edge">
						<meta name="viewport" content="width=device-width, initial-scale=1.0">
						<title>GoRESTAPI</title>
					</head>
                    <body>
						<h1>Error occured while calling external API</h1>
						<table>
							<tr>
								<th>API Name</th>
								<th>Status</th>
							</tr>
							<tr>
								<td>%s</td>
								<td>%s</td>
							</tr>
							<tr>
								<td>%s</td>
								<td>%s</td>
							</tr>
							<tr>
								<td>%s</td>
								<td>%s</td>
							</tr>
						</table>
                    </body>
                </html>
            `, cityAPIResponse.apiName, cityAPIResponse.status, weatherAPIResponse1.apiName, weatherAPIResponse1.status, weatherAPIReponse2.apiName, weatherAPIReponse2.status)

	w.Header().Set("Content-Type", "text/html")
	w.WriteHeader(http.StatusBadGateway)
	w.Write([]byte(responseHTML))
}
