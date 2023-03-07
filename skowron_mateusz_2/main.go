package main

import "github.com/MSSkowron/skowron_mateusz_2/api"

func main() {
	s := api.New(":8080")
	s.Run()
}
