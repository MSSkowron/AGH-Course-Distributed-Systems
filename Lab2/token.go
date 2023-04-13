package main

import (
	"fmt"
	"net/http"
	"time"

	"github.com/golang-jwt/jwt/v4"
)

var jwtKey = []byte("my_secret_key")

type Claims struct {
	Username string `json:"username"`
	jwt.RegisteredClaims
}

func signin(w http.ResponseWriter, r *http.Request) error {
	formParameters := parseBody(r)

	inUsername, ok := formParameters["username"]
	if !ok || inUsername == "" {
		generateErrHTML(w, http.StatusBadRequest, "bad request", "all form fields are required")
		return fmt.Errorf("bad request")
	}

	inPassword, ok := formParameters["password"]
	if !ok || inPassword == "" {
		generateErrHTML(w, http.StatusBadRequest, "bad request", "all form fields are required")
		return fmt.Errorf("bad request")
	}

	expectedPassword, ok := users[inUsername]
	if !ok || expectedPassword != inPassword {
		generateErrHTML(w, http.StatusUnauthorized, "unauthorized", "invalid credentials")
		return fmt.Errorf("unauthorized")
	}

	expirationTime := time.Now().Add(5 * time.Minute)
	claims := &Claims{
		Username: inUsername,
		RegisteredClaims: jwt.RegisteredClaims{
			ExpiresAt: jwt.NewNumericDate(expirationTime),
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenString, err := token.SignedString(jwtKey)
	if err != nil {
		generateErrHTML(w, http.StatusInternalServerError, "internal server error", "error while generating token")
		return fmt.Errorf("error while generating token")
	}

	http.SetCookie(w, &http.Cookie{
		Name:    "token",
		Value:   tokenString,
		Expires: expirationTime,
	})

	return nil
}

func authorize(w http.ResponseWriter, r *http.Request) error {
	c, err := r.Cookie("token")
	if err != nil {
		if err == http.ErrNoCookie {
			generateErrHTML(w, http.StatusUnauthorized, "unauthorized", "no token")
			return err
		}
		generateErrHTML(w, http.StatusBadRequest, "bad request", "invalid token")
		return err
	}

	tknStr := c.Value

	claims := &Claims{}

	tkn, err := jwt.ParseWithClaims(tknStr, claims, func(token *jwt.Token) (interface{}, error) {
		return jwtKey, nil
	})
	if err != nil {
		if err == jwt.ErrSignatureInvalid {
			generateErrHTML(w, http.StatusUnauthorized, "unauthorized", "invalid token")
			return err
		}
		generateErrHTML(w, http.StatusBadRequest, "bad request", "invalid token")
		w.WriteHeader(http.StatusBadRequest)
		return err
	}
	if !tkn.Valid {
		generateErrHTML(w, http.StatusUnauthorized, "unauthorized", "invalid token")
		return fmt.Errorf("invalid token")
	}

	return nil
}
