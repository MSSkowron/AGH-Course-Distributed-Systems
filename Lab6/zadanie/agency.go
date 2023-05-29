package main

import (
	"fmt"
	"log"
	"math/rand"

	"github.com/streadway/amqp"
)

type Agency struct {
	name          string
	nextJobNumber int
	jobs          []string
	channel       *amqp.Channel
	queue         amqp.Queue
}

func NewAgency(name string, jobs []string, channel *amqp.Channel) *Agency {
	agency := &Agency{
		name:          name,
		nextJobNumber: 1,
		jobs:          jobs,
		channel:       channel,
	}

	q, err := agency.channel.QueueDeclare(
		agency.name,
		false,
		false,
		true,
		false,
		nil,
	)
	if err != nil {
		log.Fatalf("%s: %s", "Failed to declare a queue", err)
	}

	agency.queue = q

	if err := agency.channel.QueueBind(
		agency.queue.Name,
		"agency."+agency.name,
		"amq.topic",
		false,
		nil,
	); err != nil {
		log.Fatalf("%s: %s", "Failed to bind a queue", err)
	}

	return agency
}

func (a *Agency) Run() {
	// Nasłuchiwanie na potwierdzenia zleceń
	go a.listenForConfirmations()

	// Publikowanie zleceń przez agencję
	go a.publishJobs()
}

func (a *Agency) listenForConfirmations() {
	msgs, err := a.channel.Consume(
		a.queue.Name,
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
		fmt.Printf("Agencja %s otrzymała potwierdzenie: %s\n", a.name, msg.Body)
	}
}

func (a *Agency) publishJobs() {
	for i := 1; i <= 10; i++ {
		jobType := a.jobs[rand.Intn(len(a.jobs))]

		message := fmt.Sprintf("%s:%d:%s", a.name, a.nextJobNumber, jobType)
		a.nextJobNumber++

		if err := a.channel.Publish(
			"amq.topic",
			"job."+jobType,
			false,
			false,
			amqp.Publishing{
				ContentType: "text/plain",
				Body:        []byte(message),
			},
		); err != nil {
			log.Fatalf("%s: %s", "Failed to publish a job", err)
		}

		fmt.Printf("Agencja %s wysłała zlecenie: %s\n", a.name, message)

		// time.Sleep(time.Second * 2) // Symulacja czasu między zleceniami
	}
}
