#!/usr/bin/env groovy
import hudson.model.*
export PATH="$HOME/.rbenv/bin:$PATH"

pipeline {
    agent any
    stages {
      stage('Initialize')
      {
        steps {
          sh("echo @@@@@@@@@@")
          sh("echo Initialize")
          sh("echo @@@@@@@@@@")
          //sh("docker build .")
        }
      }

      stage('Checkout')
      {
        steps {
          sh("echo @@@@@@@@@@")
          sh("echo Checkout")
          sh("echo @@@@@@@@@@")
        }
      }

      stage('Build')
      {
        steps {
          sh("echo @@@@@@@@@@")
          sh("echo Build")
          sh("echo @@@@@@@@@@")
          //sh("/usr/local/bin/docker run ruby-app")
        }
      }

      stage('Test')
      {
        steps {
          sh("echo @@@@@@@@@@")
          sh("echo Test")
          sh("echo @@@@@@@@@@")

          sh("bundle")
            //sh("bundle exec rspec spec")
        }
      }

      stage('Deliver')
      {
        steps {
          sh("echo @@@@@@@@@@")
          sh("echo Deliver")
          sh("echo @@@@@@@@@@")
        }
      }
    }
}
