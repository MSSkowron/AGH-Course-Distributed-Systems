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
	exchange   string
	connection *amqp.Connection
}

type Job struct {
	Agency     string
	JobType    string
	JobNumber  int
	AssignedTo string
}

func NewCarrier(name string, jobs []string, jobQueues []string, exchange string, connection *amqp.Connection) *Carrier {
	return &Carrier{
		name:       name,
		jobs:       jobs,
		jobQueues:  jobQueues,
		exchange:   exchange,
		connection: connection,
	}
}

func (c *Carrier) Run() {
	log.Println("Carrier", c.name, "has started!")

	for _, queue := range c.jobQueues {
		go c.listenForJobs(queue)
	}
}

func (c *Carrier) Close() {
	if c.connection != nil {
		c.connection.Close()
	}
}

func (c *Carrier) listenForJobs(queueName string) {
	ch, err := c.getChannel()
	if err != nil {
		log.Fatalf("failed to open a channel: %s\n", err)
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
		log.Fatalf("failed to register a consumer: %s\n", err)
	}

	log.Printf("Carrier %s listening to %s\n", c.name, queueName)

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

func (c *Carrier) handleJob(job Job) {
	log.Printf("Carrier %s received job: %s-%d:%s\n", job.AssignedTo, job.Agency, job.JobNumber, job.JobType)

	c.simulateServiceTime()

	confirmation := fmt.Sprintf("%s:%d:%s::%s", job.Agency, job.JobNumber, job.JobType, c.name)
	if err := c.publishConfirmation(job.Agency, confirmation); err != nil {
		log.Fatalf("failed to publish a confirmation: %s\n", err)
	}

	log.Printf("Carrier %s send confirmation: %s\n", c.name, confirmation)
}

func (c *Carrier) simulateServiceTime() {
	rand.Seed(time.Now().UnixNano())
	time.Sleep(time.Second * time.Duration(rand.Intn(5)))
}

func (c *Carrier) publishConfirmation(routingKey, message string) error {
	ch, err := c.getChannel()
	if err != nil {
		return fmt.Errorf("failed to open a channel: %s", err)
	}
	defer ch.Close()

	return ch.Publish(
		c.exchange,
		routingKey,
		false,
		false,
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(message),
		},
	)
}

func (c *Carrier) getChannel() (*amqp.Channel, error) {
	if c.connection == nil {
		return nil, fmt.Errorf("connection is nil")
	}

	ch, err := c.connection.Channel()
	if err != nil {
		return nil, fmt.Errorf("failed to open a channel: %s", err)
	}

	return ch, nil
}
