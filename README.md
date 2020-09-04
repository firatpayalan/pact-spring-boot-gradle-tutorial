# pact-poc
Simple gradle-based Spring application for contract testing with `pact` platform.

📘 **Prerequisite**
1. Docker

```bash
# Fetch latest version of homebrew and formula.
brew update              
# Tap the Caskroom/Cask repository from Github using HTTPS.
brew tap caskroom/cask                
# Searches all known Casks for a partial or exact match.
brew search docker                    
# Displays information about the given Cask
brew cask info docker
# Install the given cask.
brew cask install docker              
# Remove any older versions from the cellar.
brew cleanup
# Validate installation
docker -v
```

📘 **Dependencies**

You must install `pact-broker` as a Docker image. You can follow these steps for installation.


`cd src/main/resources/pact-broker`


`docker-compose up -d`


📗 **Configuration**

you can change your pact-broker host address and port number in your build.gradle`.
```
pact {
    publish {
        pactDirectory = "$buildDir/pacts"
        pactBrokerUrl = 'http://127.0.0.1:8081'
    }
}
```


