package main

import (
	"flag"
	"log"
	"strings"

	"github.com/streadway/amqp"
)

const (
	address  = "amqp://guest:guest@localhost:5672/"
	exchange = "amq.direct"
)

func main() {
	agencyNameFlag := flag.String("agency", "", "Name of an agency")
	carrierNameFlag := flag.String("carrier", "", "Name of a carrier")
	jobsFlag := flag.String("jobs", "", "Comma-separated list of jobs")
	numberOfJobsFlag := flag.Int("numberOfJobs", 0, "Number of jobs")
	flag.Parse()

	if *agencyNameFlag != "" {
		if *jobsFlag == "" {
			log.Fatal("you need to specify jobs for an agency")
		}

		startAgency(*agencyNameFlag, strings.Split(*jobsFlag, ","), *numberOfJobsFlag)
	} else if *carrierNameFlag != "" {
		if *jobsFlag == "" {
			log.Fatal("you need to specify jobs for a carrier")
		}

		startCarrier(*carrierNameFlag, strings.Split(*jobsFlag, ","))
	} else {
		log.Fatal("you need to specify either an agency or a carrier")
	}
}

func startAgency(agencyName string, jobs []string, numberOfJobs int) {
	conn, ch := setupConnection()

	defer conn.Close()
	defer ch.Close()

	if err := declareExchange(ch); err != nil {
		log.Fatalf("failed to declare an exchange: %s", err)
	}

	agencyQueue, err := declareQueue(ch, agencyName, true)
	if err != nil {
		log.Fatalf("failed to declare an agency queue: %s", err)
	}

	if err := bindQueue(ch, agencyQueue.Name, agencyName); err != nil {
		log.Fatalf("failed to bind a queue: %s", err)
	}

	for _, jobType := range jobs {
		q, err := declareQueue(ch, jobType, false)
		if err != nil {
			log.Fatalf("failed to declare a queue: %s", err)
		}

		if err := bindQueue(ch, q.Name, jobType); err != nil {
			log.Fatalf("failed to bind a queue: %s", err)
		}
	}

	NewAgency(agencyName, jobs, agencyQueue.Name, exchange, conn).Run(numberOfJobs)

	<-make(chan struct{})
}

func startCarrier(carrierName string, jobs []string) {
	conn, ch := setupConnection()

	defer conn.Close()
	defer ch.Close()

	if err := declareExchange(ch); err != nil {
		log.Fatalf("failed to declare an exchange: %s", err)
	}

	jobQueues := []string{}
	for _, jobType := range jobs {
		q, err := declareQueue(ch, jobType, false)
		if err != nil {
			log.Fatalf("failed to declare a queue: %s", err)
		}

		jobQueues = append(jobQueues, q.Name)

		if err := bindQueue(ch, q.Name, jobType); err != nil {
			log.Fatalf("failed to bind a queue: %s", err)
		}
	}

	NewCarrier(carrierName, jobs, jobQueues, exchange, conn).Run()

	<-make(chan struct{})
}

func setupConnection() (*amqp.Connection, *amqp.Channel) {
	conn, err := amqp.Dial(address)
	if err != nil {
		log.Fatalf("failed to connect to RabbitMQ: %s", err)
	}

	ch, err := conn.Channel()
	if err != nil {
		log.Fatalf("failed to open a channel: %s", err)
	}

	return conn, ch
}

func declareExchange(ch *amqp.Channel) error {
	return ch.ExchangeDeclare(
		exchange,
		amqp.ExchangeDirect,
		true,
		false,
		false,
		false,
		nil,
	)
}

func declareQueue(ch *amqp.Channel, queueName string, exclusive bool) (amqp.Queue, error) {
	return ch.QueueDeclare(
		queueName,
		false,
		false,
		exclusive,
		false,
		nil,
	)
}

func bindQueue(ch *amqp.Channel, queueName, routingKey string) error {
	return ch.QueueBind(
		queueName,
		routingKey,
		exchange,
		false,
		nil,
	)
}
