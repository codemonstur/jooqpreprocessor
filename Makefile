clean:
	@mvn -q clean

build:
	@mvn -q clean package

deploy:
	@mvn clean deploy -P release