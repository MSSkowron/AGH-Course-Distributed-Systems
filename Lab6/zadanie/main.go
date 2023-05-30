package main

import (
	"flag"
	"log"
	"strings"

	"github.com/streadway/amqp"
)

const address = "amqp://guest:guest@localhost:5672/"

func main() {
	agencyNameFlag := flag.String("agency", "", "Name of an agency")
	carrierNameFlag := flag.String("carrier", "", "Name of a carrier")
	jobsFlag := flag.String("jobs", "", "Comma-separated list of jobs")
	numberOfJobsFlag := flag.Int("numberOfJobs", 0, "Number of jobs")
	flag.Parse()

	if *agencyNameFlag != "" {
		if *jobsFlag == "" {
			log.Fatal("You need to specify jobs for an agency")
		}

		startAgency(address, *agencyNameFlag, strings.Split(*jobsFlag, ","), *numberOfJobsFlag)
	} else if *carrierNameFlag != "" {
		if *jobsFlag == "" {
			log.Fatal("You need to specify jobs for a carrier")
		}

		startCarrier(address, *carrierNameFlag, strings.Split(*jobsFlag, ","))
	} else {
		log.Fatal("You need to specify either an agency or a carrier")
	}
}

func startAgency(address string, agencyName string, jobs []string, numberOfJobs int) {
	// Połączenie z serwerem RabbitMQ
	conn, err := amqp.Dial(address)
	if err != nil {
		log.Fatalf("%s: %s", "failed to connect to RabbitMQ", err)
	}
	defer conn.Close()

	// Utworzenie kanału
	ch, err := conn.Channel()
	if err != nil {
		log.Fatalf("%s: %s", "failed to open a channel", err)
	}
	defer ch.Close()

	// Deklaracja wymiany typu direct dla zleceń i potwierdzeń
	if err := ch.ExchangeDeclare(
		"amq.direct",
		amqp.ExchangeDirect,
		true,
		false,
		false,
		false,
		nil,
	); err != nil {
		log.Fatalf("%s: %s", "Failed to declare an exchange", err)
	}

	agencyQueue, err := ch.QueueDeclare(
		agencyName,
		false,
		false,
		true,
		false,
		nil,
	)
	if err != nil {
		log.Fatalf("%s: %s", "Failed to declare an agency queue", err)
	}

	if err := ch.QueueBind(
		agencyQueue.Name,
		agencyName,
		"amq.direct",
		false,
		nil,
	); err != nil {
		log.Fatalf("%s: %s", "Failed to bind a queue", err)
	}

	for _, jobType := range jobs {
		q, err := ch.QueueDeclare(
			jobType,
			false,
			false,
			false,
			false,
			nil,
		)
		if err != nil {
			log.Fatalf("%s: %s", "Failed to declare a queue", err)
		}

		if err := ch.QueueBind(
			q.Name,
			jobType,
			"amq.direct",
			false,
			nil,
		); err != nil {
			log.Fatalf("%s: %s", "Failed to bind a queue", err)
		}
	}

	NewAgency(agencyName, jobs, agencyQueue.Name, conn).Run(numberOfJobs)

	// Blokada głównego wątku
	<-make(chan struct{})
}

func startCarrier(address string, carrierName string, jobs []string) {
	// Połączenie z serwerem RabbitMQ
	conn, err := amqp.Dial(address)
	if err != nil {
		log.Fatalf("%s: %s", "failed to connect to RabbitMQ", err)
	}
	defer conn.Close()

	// Utworzenie kanału
	ch, err := conn.Channel()
	if err != nil {
		log.Fatalf("%s: %s", "failed to open a channel", err)
	}
	defer ch.Close()

	// Deklaracja wymiany typu direct dla zleceń i potwierdzeń
	if err := ch.ExchangeDeclare(
		"amq.direct",
		amqp.ExchangeDirect,
		true,
		false,
		false,
		false,
		nil,
	); err != nil {
		log.Fatalf("%s: %s", "Failed to declare an exchange", err)
	}

	jobQueues := []string{}
	for _, jobType := range jobs {
		q, err := ch.QueueDeclare(
			jobType,
			false,
			false,
			false,
			false,
			nil,
		)
		if err != nil {
			log.Fatalf("%s: %s", "Failed to declare a queue", err)
		}

		jobQueues = append(jobQueues, q.Name)

		if err := ch.QueueBind(
			q.Name,
			jobType,
			"amq.direct",
			false,
			nil,
		); err != nil {
			log.Fatalf("%s: %s", "Failed to bind a queue", err)
		}
	}

	NewCarrier(carrierName, jobs, ch, jobQueues, conn).Run()

	// Blokada głównego wątku
	<-make(chan struct{})
}
