package api

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"strconv"
	"sync"
	"time"

	"github.com/gorilla/mux"
)

const formHTML = `
<html>
    <head>
    <title></title>
    </head>
    <body>
        <form action="/submit" method="post">
            <label for="latitude "> Latitude </label>
            <input id="latitude" type="text" name="latitude">
			<label for="longitude "> Longitude </label>
            <input id="longitude" type="text" name="longitude">
			<label for="days"> Days </label>
            <input id="days" type="text" name="days">
			<input type="submit" value="Submit">
        </form>
    </body>
</html>`

type HTTPStatus struct {
	code   int
	status string
}

type Server struct {
	listenAddr string
}

func New(listenAddr string) *Server {
	return &Server{
		listenAddr: listenAddr,
	}
}

func (s *Server) Run() error {
	r := mux.NewRouter()

	r.HandleFunc("/form", s.handleForm).Methods("GET")
	r.HandleFunc("/submit", s.handleSubmit).Methods("POST")

	log.Println("Server is running on: " + s.listenAddr)

	if err := http.ListenAndServe(s.listenAddr, r); err != nil {
		return err
	}

	return nil
}

func (s *Server) handleForm(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Header().Set("Content-Type", "text/html; charset=utf-8")

	if _, err := w.Write([]byte(formHTML)); err != nil {
		log.Printf("erorr while writing formHTML response: %s\n", err.Error())
	}
}

func (s *Server) handleSubmit(w http.ResponseWriter, r *http.Request) {
	// get data from HTML Form
	err := r.ParseForm()
	if err != nil {
		log.Printf("erorr while parsing HTML Form: %s\n", err.Error())
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	latitude := r.Form.Get("latitude")
	longitude := r.Form.Get("longitude")
	days := r.Form.Get("days")

	latitudeFloat, err := strconv.ParseFloat(latitude, 64)
	if err != nil || latitude == "" || latitudeFloat < -90.0 || latitudeFloat > 90.0 {
		// generate proper HTML
		log.Fatalln("INVALID INPUT")
	}

	longitudeFloat, err := strconv.ParseFloat(latitude, 64)
	if err != nil || longitude == "" || longitudeFloat < -180.0 || longitudeFloat > 180.0 {
		// generate proper HTML
		log.Fatalln("INVALID INPUT")
	}

	daysInt, err := strconv.Atoi(days)
	if err != nil || days == "" || daysInt < 1 || daysInt > 10 {
		// generate proper HTML
		log.Fatalln("INVALID INPUT")
	}

	// asynchronous requests to APIs
	wg := &sync.WaitGroup{}
	var weatherAPIResponse WeatherAPIResponse
	var openMeteoResponse OpenMeteoAPIResponse

	wg.Add(2)
	go func() {
		weatherAPIResponse = callWeatherAPI(latitudeFloat, longitudeFloat, daysInt)
		wg.Done()
	}()
	go func() {
		openMeteoResponse = callOpenMeteoAPI(latitudeFloat, longitudeFloat, daysInt)
		wg.Done()
	}()
	wg.Wait()

	// respond
	w.WriteHeader(http.StatusOK)
	w.Header().Set("Content-Type", "text/html; charset=utf-8")

	responseHTML := `
	<html lang="en">
	<head>
		<meta charset="UTF-8" />
		<title>Document</title>
	</head>
	<body>
		<h1> Average temperature: ` + avg + `</h1>
		<h1> Status Code: ` + "200 - OK" + `</h1>
	</body>
	</html>`

	if _, err := w.Write([]byte(responseHTML)); err != nil {
		log.Printf("erorr while writing formHTML response: %s\n", err.Error())
	}
}

type WeatherAPIResponse struct {
	Location struct {
		Name    string  `json:"name"`
		Country string  `json:"country"`
		Lat     float64 `json:"lat"`
		Lon     float64 `json:"lon"`
	} `json:"location"`
	Current struct {
		TempC float64 `json:"temp_c"`
	} `json:"current"`
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

func callWeatherAPI(latitude, longitude float64, days int) WeatherAPIResponse {
	latitudeStr := fmt.Sprintf("%f", latitude)
	longitudeStr := fmt.Sprintf("%f", longitude)
	daysStr := strconv.Itoa(days)

	httpResponse, err := http.Get("http://api.weatherapi.com/v1/forecast.json?key=ef95ee45d3e94837b26195852230703&q=" + latitudeStr + "," + longitudeStr + "&days=" + daysStr)
	if err != nil {
		log.Fatalln(err)
	}
	defer httpResponse.Body.Close()
	body, err := ioutil.ReadAll(httpResponse.Body)

	var response WeatherAPIResponse
	if err := json.Unmarshal(body, &response); err != nil {
		log.Println("Can not unmarshal JSON " + err.Error())
	}

	fmt.Printf("%+v\n\n", response)

	return response
}

type OpenMeteoAPIResponse struct {
	Latitude  float64 `json:"latitude"`
	Longitude float64 `json:"longitude"`
	Daily     struct {
		Time             []string  `json:"time"`
		Temperature2mMax []float64 `json:"temperature_2m_max"`
		Temperature2mMin []float64 `json:"temperature_2m_min"`
	} `json:"daily"`
}

func callOpenMeteoAPI(latitude, longitude float64, days int) OpenMeteoAPIResponse {
	latitudeStr := fmt.Sprintf("%f", latitude)
	longitudeStr := fmt.Sprintf("%f", longitude)
	startDate := time.Now().AddDate(0, 0, 1).Format("2006-01-02")
	endDate := time.Now().AddDate(0, 0, days).Format("2006-01-02")

	httpResponse, err := http.Get("https://api.open-meteo.com/v1/forecast?latitude=" + latitudeStr + "&longitude=" + longitudeStr + "&daily=temperature_2m_max,temperature_2m_min&timezone=Europe%2FBerlin&start_date=" + startDate + "&end_date=" + endDate)
	if err != nil {
		log.Fatalln(err)
	}
	defer httpResponse.Body.Close()
	body, err := ioutil.ReadAll(httpResponse.Body)

	var response OpenMeteoAPIResponse
	if err := json.Unmarshal(body, &response); err != nil {
		log.Println("Can not unmarshal JSON " + err.Error())
	}

	fmt.Printf("%+v\n\n", response)

	return response
}
