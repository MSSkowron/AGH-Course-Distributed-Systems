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
	exchange      string
	connection    *amqp.Connection
}

func NewAgency(name string, jobs []string, queueName string, exchange string, connection *amqp.Connection) *Agency {
	return &Agency{
		name:          name,
		nextJobNumber: 1,
		jobs:          jobs,
		queueName:     queueName,
		exchange:      exchange,
		connection:    connection,
	}
}

func (a *Agency) Run(numberOfJobs int) {
	log.Println("Agency", a.name, "has started!")

	// Listen for job confirmations
	go a.listenForConfirmations()

	// Publish jobs
	go a.publishJobs(numberOfJobs)
}

func (a *Agency) listenForConfirmations() {
	ch, err := a.getChannel()
	if err != nil {
		log.Fatalf("failed to open a channel: %s\n", err)
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
		log.Fatalf("failed to register a consumer: %s\n", err)
	}

	for msg := range msgs {
		log.Printf("Agency %s received confirmation: %s\n", a.name, msg.Body)
	}
}

func (a *Agency) publishJobs(numberOfJobs int) {
	ch, err := a.getChannel()
	if err != nil {
		log.Fatalf("failed to open a channel: %s\n", err)
	}
	defer ch.Close()

	for i := 0; i < numberOfJobs; i++ {
		jobType := a.getRandomJobType()

		message := fmt.Sprintf("%s:%d:%s", a.name, a.nextJobNumber, jobType)
		a.nextJobNumber++

		if err := a.publishJob(ch, jobType, message); err != nil {
			log.Fatalf("failed to publish a job: %s\n", err)
		}

		log.Printf("Agency %s sent a job: %s\n", a.name, message)

		time.Sleep(time.Second * 2) // Simulate time between jobs
	}
}

func (a *Agency) getChannel() (*amqp.Channel, error) {
	if a.connection == nil {
		return nil, fmt.Errorf("connection is nil")
	}

	ch, err := a.connection.Channel()
	if err != nil {
		return nil, fmt.Errorf("failed to open a channel: %s", err)
	}

	return ch, nil
}

func (a *Agency) getRandomJobType() string {
	if len(a.jobs) == 0 {
		return ""
	}

	randomIndex := rand.Intn(len(a.jobs))
	return a.jobs[randomIndex]
}

func (a *Agency) publishJob(ch *amqp.Channel, jobType, message string) error {
	if ch == nil {
		return fmt.Errorf("channel is nil")
	}

	return ch.Publish(
		a.exchange,
		jobType,
		false,
		false,
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(message),
		},
	)
}
