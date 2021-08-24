# springboot-aws

## Util Command

//Create SNS
aws --endpoint-url=http://localhost:4566 sns create-topic --name Paysandu

//Create SQS
aws sqs create-queue --endpoint-url=http://localhost:4566 --queue-name local_queue

//Subscribing to SNS to SQS
aws --endpoint-url=http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:Paysandu --protocol sqs --notification-endpoint http://localhost:4566/000000000000/local_queue

//Listt Message From SNS With SQS
aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/local_queue

