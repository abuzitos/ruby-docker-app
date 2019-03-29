pipeline {
    agent {
      docker { image 'abu-projeto/ruby-docker-app:latest' }
     }
    stages {
      stage('Initialize')
      {
          sh "echo 'Initialize'"
          sh "docker build ."
      }

      stage('Checkout')
      {
          sh "echo 'Checkout'"
      }

      stage('Build')
      {
          sh "echo 'Build'"
          sh "docker run ruby-app"
      }

      stage('Test')
      {
          sh "echo 'Test'"
          sh "bundle exec rspec spec"
      }

      stage('Deliver')
      {
          sh "echo 'Deliver'"
      }
    }
}
