package main

import (
	"fmt"
	"log"
	"strconv"
	"strings"

	"github.com/streadway/amqp"
)

type Carrier struct {
	name    string
	jobs    []string
	channel *amqp.Channel
	queues  []amqp.Queue
}

func NewCarrier(name string, jobs []string, channel *amqp.Channel) *Carrier {
	carrier := &Carrier{
		name:    name,
		jobs:    jobs,
		channel: channel,
		queues:  []amqp.Queue{},
	}

	for _, jobType := range carrier.jobs {
		q, err := carrier.channel.QueueDeclare(
			jobType,
			false,
			false,
			true,
			false,
			nil,
		)
		if err != nil {
			log.Fatalf("%s: %s", "Failed to declare a queue", err)
		}

		carrier.queues = append(carrier.queues, q)

		if err := carrier.channel.QueueBind(
			q.Name,
			"job."+jobType,
			"amq.topic",
			false,
			nil,
		); err != nil {
			log.Fatalf("%s: %s", "Failed to bind a queue", err)
		}
	}

	return carrier
}

func (c *Carrier) Run() {
	for _, queue := range c.queues {
		go c.listenForJobs(queue)
	}
}
func (c *Carrier) listenForJobs(queue amqp.Queue) {
	msgs, err := c.channel.Consume(
		queue.Name,
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
	fmt.Printf("Przewoźnik %s otrzymał zlecenie %s-%d: %s\n", job.AssignedTo, job.Agency, job.JobNumber, job.JobType)

	// // Symulacja czasu wykonania usługi
	// time.Sleep(time.Second * time.Duration(rand.Intn(5)))

	// Potwierdzenie wykonania zlecenia
	confirmation := fmt.Sprintf("Potwierdzenie zlecenia: %s - %d", c.name, job.JobNumber)
	c.publishConfirmation("agency."+job.Agency, confirmation)
}

// Funkcja do publikowania zlecenia przez przewoznika
func (c *Carrier) publishConfirmation(routingKey, message string) {
	err := c.channel.Publish(
		"amq.topic",
		routingKey,
		false,
		false,
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(message),
		},
	)
	if err != nil {
		log.Fatalf("%s: %s", "Failed to publish a job", err)
	}
}
