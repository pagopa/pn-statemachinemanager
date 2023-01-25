aws dynamodb create-table --table-name Transaction --attribute-definitions AttributeName=ProcessClientId,AttributeType=S AttributeName=CurrStatus,AttributeType=S --key-schema AttributeName=ProcessId,KeyType=HASH AttributeName=CurrStatus,KeyType=RANGE --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 --table-class STANDARD

