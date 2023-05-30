package main

import (
	"fmt"
	"log"
	"math/rand"
	"time"

	"github.com/streadway/amqp"
)

type Agency struct {
	name          string
	nextJobNumber int
	jobs          []string
	queueName     string
	connection    *amqp.Connection
}

func NewAgency(name string, jobs []string, queueName string, connection *amqp.Connection) *Agency {
	return &Agency{
		name:          name,
		nextJobNumber: 1,
		jobs:          jobs,
		queueName:     queueName,
		connection:    connection,
	}
}

func (a *Agency) Run(numberOfJobs int) {
	log.Println("Agencja " + a.name + " wystartowała!")

	// Nasłuchiwanie na potwierdzenia zleceń
	go a.listenForConfirmations()

	// Publikowanie zleceń przez agencję
	go a.publishJobs(numberOfJobs)
}

func (a *Agency) Close() {
	a.connection.Close()
}

func (a *Agency) listenForConfirmations() {
	ch, err := a.connection.Channel()
	if err != nil {
		log.Fatalf("%s: %s", "failed to open a channel", err)
	}
	defer ch.Close()

	msgs, err := ch.Consume(
		a.queueName,
		"",
		true,
		false,
		false,
		false,
		nil,
	)
	if err != nil {
		log.Fatalf("%s: %s", "Failed to register a consumer", err)
	}

	for msg := range msgs {
		log.Printf("Agencja %s otrzymała potwierdzenie: %s\n", a.name, msg.Body)
	}
}

func (a *Agency) publishJobs(numberOfJobs int) {
	ch, err := a.connection.Channel()
	if err != nil {
		log.Fatalf("%s: %s", "failed to open a channel", err)
	}
	defer ch.Close()

	for i := 0; i < numberOfJobs; i++ {
		jobType := a.jobs[rand.Intn(len(a.jobs))]

		message := fmt.Sprintf("%s:%d:%s", a.name, a.nextJobNumber, jobType)
		a.nextJobNumber++

		if err := ch.Publish(
			"amq.direct",
			jobType,
			false,
			false,
			amqp.Publishing{
				ContentType: "text/plain",
				Body:        []byte(message),
			},
		); err != nil {
			log.Fatalf("%s: %s", "Failed to publish a job", err)
		}

		log.Printf("Agencja %s wysłała zlecenie: %s\n", a.name, message)

		time.Sleep(time.Second * 2) // Symulacja czasu między zleceniami
	}
}
