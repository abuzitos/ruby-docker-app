#!/usr/bin/env groovy
import hudson.model.*

node('master') {
    sh("your shell script")
}

pipeline {

    /*
    agent {
      docker { image 'ruby' }
     }
    */

    agent none
    environment {
        VERSION = sh(returnStdout: true, script: 'git describe --tags')
    }

    stages {
      stage('Initialize')
      {
        steps {
          sh "echo 'Initialize'"
          sh "docker build ."
        }
      }

      stage('Checkout')
      {
        steps {
          sh "echo 'Checkout'"
        }
      }

      stage('Build')
      {
        steps {
          sh "echo 'Build'"
          sh "docker run ruby-app"
        }
      }

      stage('Test')
      {
        steps {
          sh "echo 'Test'"
          sh "bundle exec rspec spec"
        }
      }

      stage('Deliver')
      {
        steps {
          sh "echo 'Deliver'"
        }
      }
    }
}
