package main

import (
	"fmt"
	"net/http"
)

const loginHTML = `
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GoRESTAPI</title>
</head>
<body>
    <form action="/login" method="post">
        <label for="username">Username</label>
        <input id="username" type="text" name="username">
		<br>
		<br>
        <label for="password">Password</label>
        <input id="password" type="password" name="password">
		<br>
		<br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
`

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
    <form action="/form" method="post">
		<h1> Weather Forecast </h1>
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

func generateExternalErrHTML(w http.ResponseWriter, addressAPIResponse AddressAPIResponse, weatherAPIResponse1, weatherAPIReponse2 WeatherAPIResponse) {
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
            `, addressAPIResponse.apiName, addressAPIResponse.status, weatherAPIResponse1.apiName, weatherAPIResponse1.status, weatherAPIReponse2.apiName, weatherAPIReponse2.status)

	w.Header().Set("Content-Type", "text/html")
	w.WriteHeader(http.StatusBadGateway)
	w.Write([]byte(responseHTML))
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
