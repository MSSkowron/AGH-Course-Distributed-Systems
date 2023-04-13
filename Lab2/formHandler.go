package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"strconv"
	"time"
)

type AddressAPIResponse struct {
	apiName    string
	address    string
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

func fetchAddress(latitude, longitude string, ch chan AddressAPIResponse) {
	var (
		addressAPIResponse AddressAPIResponse = AddressAPIResponse{apiName: "nominatim.openstreetmap.org"}
		url                string             = fmt.Sprintf("https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json", latitude, longitude)
	)

	resp, err := http.Get(url)
	if err != nil {
		addressAPIResponse.statusCode = http.StatusInternalServerError
		addressAPIResponse.status = "Internal server error"

		ch <- addressAPIResponse

		return
	}

	addressAPIResponse.statusCode = resp.StatusCode
	addressAPIResponse.status = resp.Status

	if !isCodeSuccessful(resp.StatusCode) {
		ch <- addressAPIResponse

		return
	}

	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		addressAPIResponse.statusCode = http.StatusInternalServerError
		addressAPIResponse.status = "Internal server error"

		ch <- addressAPIResponse

		return
	}

	var location struct {
		Address struct {
			City    string `json:"city"`
			Country string `json:"country"`
		} `json:"address"`
	}
	if err = json.Unmarshal(body, &location); err != nil {
		addressAPIResponse.statusCode = http.StatusInternalServerError
		addressAPIResponse.status = "Internal server error"

		ch <- addressAPIResponse

		return
	}

	addressAPIResponse.address = location.Address.Country + " " + location.Address.City

	ch <- addressAPIResponse
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

	for _, forecast := range weatherData.Forecast.Forecastday {
		weatherAPIResponse.tempMax = append(weatherAPIResponse.tempMax, forecast.Day.MaxtempC)
		weatherAPIResponse.tempMin = append(weatherAPIResponse.tempMin, forecast.Day.MintempC)
	}

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

	weatherAPIResponse.tempMax = append(weatherAPIResponse.tempMax, weatherData.Daily.Temperature2mMax...)
	weatherAPIResponse.tempMin = append(weatherAPIResponse.tempMin, weatherData.Daily.Temperature2mMin...)

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
