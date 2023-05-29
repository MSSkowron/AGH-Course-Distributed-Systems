package main

import (
	"log"

	"github.com/streadway/amqp"
)

func main() {
	// Połączenie z serwerem RabbitMQ
	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	if err != nil {
		log.Fatalf("%s: %s", "failed to connect to RabbitMQ", err)
	}
	defer conn.Close()

	// Utworzenie kanału
	ch, err := conn.Channel()
	if err != nil {
		log.Fatalf("%s: %s", "Failed to open a channel", err)
	}
	defer ch.Close()

	// Deklaracja wymiany typu topic dla zleceń i potwierdzeń
	if err := ch.ExchangeDeclare(
		"amq.topic",
		amqp.ExchangeTopic,
		true,
		false,
		false,
		false,
		nil,
	); err != nil {
		log.Fatalf("%s: %s", "Failed to declare an exchange", err)
	}

	agency1 := NewAgency("agency1", []string{"person_transport", "cargo_transport", "satellite_placement"}, ch)
	agency2 := NewAgency("agency2", []string{"person_transport", "cargo_transport", "satellite_placement"}, ch)

	// carrier1 := NewCarrier("carrier1", []string{"person_transport", "cargo_transport"}, ch)
	// carrier2 := NewCarrier("carrier2", []string{"cargo_transport", "satellite_placement"}, ch)

	agency1.Run()
	agency2.Run()
	// carrier1.Run()
	// carrier2.Run()

	// Oczekiwanie na wiadomości
	forever := make(chan bool)
	<-forever
}
