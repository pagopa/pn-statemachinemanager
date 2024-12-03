echo " - Create pn-SmStates TABLE"


aws --profile default --region eu-south-1 --endpoint-url=http://localstack:4566 \
    dynamodb create-table \
    --table-name pn-SmStates \
    --attribute-definitions \
        AttributeName=processClientId,AttributeType=S \
        AttributeName=currStatus,AttributeType=S \
    --key-schema \
        AttributeName=processClientId,KeyType=HASH \
        AttributeName=currStatus,KeyType=SORT \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5

echo "Initialization terminated"
