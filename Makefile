.SILENT:

build:
	docker build -t micro-service-example .

run:
	docker run -p:5000:8080 micro-service-example

tag:
	docker tag micro-service-example dtr.predix.io/212702649/micro-service-example