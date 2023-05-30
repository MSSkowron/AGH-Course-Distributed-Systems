package main

import (
	"fmt"
	"log"
	"math/rand"
	"strconv"
	"strings"
	"time"

	"github.com/streadway/amqp"
)

type Carrier struct {
	name       string
	jobs       []string
	jobQueues  []string
	connection *amqp.Connection
}

func NewCarrier(name string, jobs []string, channel *amqp.Channel, jobQueues []string, connection *amqp.Connection) *Carrier {
	return &Carrier{
		name:       name,
		jobs:       jobs,
		jobQueues:  jobQueues,
		connection: connection,
	}
}

func (c *Carrier) Run() {
	log.Printf("Przewoźnik %s wystartował!", c.name)

	for _, queue := range c.jobQueues {
		go c.listenForJobs(queue)
	}
}

func (c *Carrier) listenForJobs(queueName string) {
	ch, err := c.connection.Channel()
	if err != nil {
		log.Fatalf("%s: %s", "failed to open a channel", err)
	}
	defer ch.Close()

	msgs, err := ch.Consume(
		queueName,
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

	log.Printf("Przewoźnik %s nasłuchuje na %s", c.name, queueName)

	for msg := range msgs {
		split := strings.Split(string(msg.Body), ":")
		if len(split) == 3 {
			jobNumber, _ := strconv.Atoi(split[1])
			job := Job{
				Agency:     split[0],
				JobType:    split[2],
				JobNumber:  jobNumber,
				AssignedTo: c.name,
			}
			c.handleJob(job)
		}
	}
}

// Funkcja do obsługi zlecenia przez przewoźnika
func (c *Carrier) handleJob(job Job) {
	log.Printf("Przewoźnik %s otrzymał zlecenie: %s-%d:%s\n", job.AssignedTo, job.Agency, job.JobNumber, job.JobType)

	// Symulacja czasu wykonania usługi
	rand.Seed(time.Now().UnixNano())
	time.Sleep(time.Second * time.Duration(rand.Intn(5)))

	// Potwierdzenie wykonania zlecenia
	confirmation := fmt.Sprintf("%s:%d:%s::%s", job.Agency, job.JobNumber, job.JobType, c.name)
	c.publishConfirmation("agency."+job.Agency, confirmation)

	log.Printf("Przewoźnik %s wysłał potwierdzenie: %s\n", c.name, confirmation)
}

// Funkcja do publikowania zlecenia przez przewoznika
func (c *Carrier) publishConfirmation(routingKey, message string) {
	ch, err := c.connection.Channel()
	if err != nil {
		log.Fatalf("%s: %s", "failed to open a channel", err)
	}
	defer ch.Close()

	if err := ch.Publish(
		"amq.topic",
		routingKey,
		false,
		false,
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(message),
		},
	); err != nil {
		log.Fatalf("%s: %s", "Failed to publish a job", err)
	}
}
